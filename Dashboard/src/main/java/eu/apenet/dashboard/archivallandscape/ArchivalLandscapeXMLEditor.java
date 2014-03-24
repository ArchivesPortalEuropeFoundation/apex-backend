package eu.apenet.dashboard.archivallandscape;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class ArchivalLandscapeXMLEditor extends AbstractAction {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 7520670010690021063L;
	private final static Logger log = Logger.getLogger(ArchivalLandscapeManager.class);
	protected static final String TEMP_FILE_NAME = "AL.xml.tmp";
	protected static final String FILE_NAME = "AL.xml";
	//changed identifiers
	private List<String> oldSameNameInstitution;
	private List<String> newSameNameInstitution;
	//empty identifiers
	private List<String> emptyIdentifiersNamed;
	private List<String> newEmptyIdentifierInstitution;
	//same identifiers, different name
	private List<String> sameIdentifiersNamed;
	private List<String> oldSameIdentifierInstitution;
	private List<String> newSameIdentifierInstitution;
	
	/**
	 * Action for change identifier
	 * @return String.ACTION_RESULT
	 */
	public String changeXMLIdentifier(){
		String state = ERROR;
		copyXMLFileToTmp();
		boolean valid = (this.oldSameNameInstitution!=null && this.newSameNameInstitution!=null);
		File httpFile = null;
		Collection<ArchivalInstitution> archivalInstitutionList = null;
		if(valid){
			String httpFileFileName = SecurityContext.get().getCountryIsoname().toUpperCase() + TEMP_FILE_NAME;
			httpFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + 
				File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
				File.separatorChar + "AL"+ File.separatorChar + httpFileFileName);
			ArchivalLandscapeManager archivalLandscapeManager = new ArchivalLandscapeManager();
			archivalInstitutionList = archivalLandscapeManager.getInstitutionsByALFile(httpFile);
		}
		//altered identifiers part
		if(this.oldSameNameInstitution!=null && this.newSameNameInstitution!=null){
			int counter = this.oldSameNameInstitution.size();
			if(valid && counter == this.newSameNameInstitution.size()){
				for(int i=0;i<counter;i++){
					archivalInstitutionList = changeXMLInstitutionByIdentifiers(this.oldSameNameInstitution.get(i),this.newSameNameInstitution.get(i),archivalInstitutionList); 
				}
			}
		}
		//empty identifiers part
		if(this.emptyIdentifiersNamed!=null && this.newEmptyIdentifierInstitution!=null){
			int counter = this.emptyIdentifiersNamed.size();
			if(valid && counter == this.newEmptyIdentifierInstitution.size()){
				for(int i=0;i<counter;i++){
					String emptyIdentifierName = this.emptyIdentifiersNamed.get(i);
					String emptyIdentifierValue = this.newEmptyIdentifierInstitution.get(i);
					if(emptyIdentifierValue!=null && !emptyIdentifierValue.trim().isEmpty()){
						archivalInstitutionList = changeXMLEmptyIdentifiedInstitutions(emptyIdentifierName,emptyIdentifierValue,archivalInstitutionList);
					}
				}
			}
		}
		//same identifiers, different names
		if(this.oldSameIdentifierInstitution!=null && this.newSameIdentifierInstitution!=null && this.sameIdentifiersNamed!=null){
			int counter = this.oldSameIdentifierInstitution.size();
			if(counter == this.newSameIdentifierInstitution.size() && counter == this.sameIdentifiersNamed.size()){
				Map<String, List<ArchivalInstitution>> archivalInstitutions = ArchivalLandscapeUtils.getInstitutionsWithSameIdentifierFromArchivalInstitutionStructure(archivalInstitutionList);
				if(archivalInstitutions!=null){
					for(int i=0;i<counter;i++){
						String sameName = this.sameIdentifiersNamed.get(i);
						String oldSameIdentifier = this.oldSameIdentifierInstitution.get(i);
						if(archivalInstitutions.containsKey(oldSameIdentifier)){
							List<ArchivalInstitution> oldSameInstitutions = archivalInstitutions.get(oldSameIdentifier);
							String newSameIdentifier = this.newSameIdentifierInstitution.get(i);
							if(oldSameInstitutions!=null){
								archivalInstitutionList = changeOldInstitutionIdentifierByNameAndIdentifier(oldSameInstitutions,archivalInstitutionList,sameName,newSameIdentifier);
							}
						}
					}
				}
			}
		}
		if(valid && archivalInstitutionList!=null){
			try {
				storeStructureToXML(archivalInstitutionList,httpFile);
			} catch (IOException e) {
				log.error("Error trying to manage xml generated file",e);
			}
			state = SUCCESS;
			supplyTmpByOriginalXmlFile();
		}else{
			removeTmpFile();
		}
		return state;
	}
	
	private Collection<ArchivalInstitution> changeOldInstitutionIdentifierByNameAndIdentifier(Collection<ArchivalInstitution> oldSameInstitutions,Collection<ArchivalInstitution> archivalInstitutionList,String sameIdentifierName,String newSameIdentifier) {
		ArchivalInstitution oldSameInstitution = null;
		Iterator<ArchivalInstitution> oldSameInstitutionsIt = oldSameInstitutions.iterator();
		while(oldSameInstitution==null && oldSameInstitutionsIt.hasNext()){
			ArchivalInstitution target = oldSameInstitutionsIt.next();
			if(sameIdentifierName.equals(target.getAiname())){ //found?
				oldSameInstitution = target;
			}
		}
		if(oldSameInstitution!=null){
			//create new institution to be replaced with altered data
			ArchivalInstitution newSameInstitution = new ArchivalInstitution();
			newSameInstitution.setAiAlternativeNames(oldSameInstitution.getAiAlternativeNames());
			newSameInstitution.setAiname(oldSameInstitution.getAiname());
			newSameInstitution.setGroup(oldSameInstitution.isGroup());
			newSameInstitution.setInternalAlId(newSameIdentifier);
			archivalInstitutionList = ArchivalLandscapeUtils.replaceXMLInstitutionByInstitution(oldSameInstitution,newSameInstitution,archivalInstitutionList);
		}
		return archivalInstitutionList;
	}

	private Collection<ArchivalInstitution> changeXMLEmptyIdentifiedInstitutions(String emptyIdentifierNamed,String emptyIdentifierValue,Collection<ArchivalInstitution> archivalInstitutionList) {
		ArchivalInstitution oldArchivalInstitution = ArchivalLandscapeUtils.getInstitutionByNameFromStructure(emptyIdentifierNamed,archivalInstitutionList);
		ArchivalInstitution target = null;
		if(oldArchivalInstitution!=null){
			target = oldArchivalInstitution;
			target.setInternalAlId(emptyIdentifierValue);
			archivalInstitutionList = ArchivalLandscapeUtils.replaceAnInstitutionToArchivalInstitutionStructure(oldArchivalInstitution, target, archivalInstitutionList);
		}
		return archivalInstitutionList;
	}
	
	/**
	 * Removes temp file.
	 * It must be called when it's not used.
	 */
	private void removeTmpFile() {
		String httpFileFileName = SecurityContext.get().getCountryIsoname().toUpperCase() + TEMP_FILE_NAME;
		File httpFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + "AL"+ File.separatorChar + httpFileFileName);
		if(httpFile.exists()){
			httpFile.delete();
		}
	}
	/**
	 * Creates a copy of original file to work with.
	 */
	public void copyXMLFileToTmp(){
		String httpFileFileName = SecurityContext.get().getCountryIsoname().toUpperCase() + "AL.xml";
		String httpFileTempName = SecurityContext.get().getCountryIsoname().toUpperCase() + TEMP_FILE_NAME;
		File httpFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + "AL"+ File.separatorChar + httpFileFileName);
		File httpTempFile =  new File(APEnetUtilities.getConfig().getRepoDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + "AL"+ File.separatorChar + httpFileTempName);
		try {
			FileUtils.copyFile(httpFile, httpTempFile);
		} catch (IOException e) {
			log.error("Error trying to copy: "+httpFile+" to: "+httpTempFile,e);
		}
		
	}
	/**
	 * Change an institution (ArchivalInstitution) by internalAiIdentifier
	 * @return state.BOOLEAN
	 */
	public Collection<ArchivalInstitution> changeXMLInstitutionByIdentifiers(String oldSameNameInstitution,String newSameNameInstitution,Collection<ArchivalInstitution> archivalInstitutionList){
		ArchivalInstitutionDAO aiDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution oldInstitution = aiDAO.getArchivalInstitutionByInternalAlId(oldSameNameInstitution,SecurityContext.get().getCountryId());
		ArchivalInstitution targetInstitution = ArchivalLandscapeUtils.getInstitutionByNameFromStructure(oldInstitution.getAiname(), archivalInstitutionList);
		targetInstitution.setInternalAlId(newSameNameInstitution);
		archivalInstitutionList = ArchivalLandscapeUtils.replaceAnInstitutionToArchivalInstitutionStructure(oldInstitution,targetInstitution,archivalInstitutionList);
		return archivalInstitutionList;
	}
	
	private void storeStructureToXML(Collection<ArchivalInstitution> archivalInstitutionList,File httpFile) throws IOException{
		ByteArrayOutputStream xml = ArchivalLandscapeUtils.buildXMlFromStructure(SecurityContext.get().getCountryName(),archivalInstitutionList);
		if(xml!=null){
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream (httpFile);
				xml.writeTo(outputStream);
			} catch (FileNotFoundException e) {
				log.error("File not found: "+httpFile.getAbsolutePath(),e);
			} catch (IOException e) {
				log.error("IOException on file: "+httpFile.getAbsolutePath(),e);
			} finally {
				if(outputStream!=null){
					outputStream.close();
				}
				if(xml!=null){
					xml.close();
				}
			} 
		}
	}
	
	/**
	 * Deletes temp file and replace original with new one.
	 */
	public void supplyTmpByOriginalXmlFile(){
		String httpFileFileName = SecurityContext.get().getCountryIsoname().toUpperCase() + FILE_NAME;
		String httpFileTempName = SecurityContext.get().getCountryIsoname().toUpperCase() + TEMP_FILE_NAME;
		File httpFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + "AL"+ File.separatorChar + httpFileFileName);
		File httpTempFile =  new File(APEnetUtilities.getConfig().getRepoDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + "AL"+ File.separatorChar + httpFileTempName);
		try {
			FileUtils.copyFile(httpTempFile,httpFile);
			httpTempFile.delete();
		} catch (IOException e) {
			log.error("Error trying to copy: "+httpTempFile+" to: "+httpFile,e);
		}
	}

	public List<String> getOldSameNameInstitution() {
		return this.oldSameNameInstitution;
	}

	public List<String> getNewSameNameInstitution() {
		return this.newSameNameInstitution;
	}

	public List<String> getEmptyIdentifiersNamed() {
		return this.emptyIdentifiersNamed;
	}

	public void setOldSameNameInstitution(List<String> oldSameNameInstitution) {
		this.oldSameNameInstitution = oldSameNameInstitution;
	}

	public void setNewSameNameInstitution(List<String> newSameNameInstitution) {
		this.newSameNameInstitution = newSameNameInstitution;
	}

	public void setEmptyIdentifiersNamed(List<String> emptyIdentifiersNamed) {
		this.emptyIdentifiersNamed = emptyIdentifiersNamed;
	}

	public List<String> getNewEmptyIdentifierInstitution() {
		return this.newEmptyIdentifierInstitution;
	}

	public void setNewEmptyIdentifierInstitution(
			List<String> newEmptyIdentifierInstitution) {
		this.newEmptyIdentifierInstitution = newEmptyIdentifierInstitution;
	}

	public List<String> getSameIdentifiersNamed() {
		return this.sameIdentifiersNamed;
	}

	public List<String> getOldSameIdentifierInstitution() {
		return this.oldSameIdentifierInstitution;
	}

	public List<String> getNewSameIdentifierInstitution() {
		return this.newSameIdentifierInstitution;
	}

	public void setSameIdentifiersNamed(List<String> sameIdentifiersNamed) {
		this.sameIdentifiersNamed = sameIdentifiersNamed;
	}

	public void setOldSameIdentifierInstitution(List<String> oldSameIdentifierInstitution) {
		this.oldSameIdentifierInstitution = oldSameIdentifierInstitution;
	}

	public void setNewSameIdentifierInstitution(List<String> newSameIdentifierInstitution) {
		this.newSameIdentifierInstitution = newSameIdentifierInstitution;
	}

}
