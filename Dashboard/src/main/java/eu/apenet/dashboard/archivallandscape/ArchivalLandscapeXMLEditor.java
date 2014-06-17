package eu.apenet.dashboard.archivallandscape;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

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

	/**
	 * Empty constructor.
	 */
	public ArchivalLandscapeXMLEditor() {
		super();
	}

	/**
	 * Constructor with params.
	 *
	 * @param oldSameNameInstitution List of internal identifiers in database.
	 * @param newSameNameInstitution List of internal identifiers in file.
	 */
	public ArchivalLandscapeXMLEditor(List<String> oldSameNameInstitution, List<String> newSameNameInstitution) {
		super();
		this.setOldSameNameInstitution(oldSameNameInstitution);
		this.setNewSameNameInstitution(newSameNameInstitution);
	}

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

			File repoFileDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + 
					File.separatorChar + SecurityContext.get().getCountryIsoname().toUpperCase() + File.separatorChar);
			try {
				if(!repoFileDir.exists()){
					repoFileDir.mkdirs();
				}
			} catch (Exception e) {
				log.error("Error creating path: " + repoFileDir.getAbsolutePath());
			}
			if(repoFileDir.exists()){
				httpFile = new File(repoFileDir.getAbsolutePath() + File.separatorChar + httpFileFileName);
				ArchivalLandscapeManager archivalLandscapeManager = new ArchivalLandscapeManager();
				archivalInstitutionList = archivalLandscapeManager.getInstitutionsByALFile(httpFile,false);
			}
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
	
	/**
	 * Removes temp file.
	 * It must be called when it's not used.
	 */
	private void removeTmpFile() {
		String httpFileFileName = SecurityContext.get().getCountryIsoname().toUpperCase() + TEMP_FILE_NAME;
		File httpFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + httpFileFileName);
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
		File httpFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + httpFileFileName);
		File httpTempFile =  new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + httpFileTempName);
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
			File.separatorChar + httpFileFileName);
		File httpTempFile =  new File(APEnetUtilities.getConfig().getRepoDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase() +
			File.separatorChar + httpFileTempName);
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


	public void setOldSameNameInstitution(List<String> oldSameNameInstitution) {
		this.oldSameNameInstitution = oldSameNameInstitution;
	}

	public void setNewSameNameInstitution(List<String> newSameNameInstitution) {
		this.newSameNameInstitution = newSameNameInstitution;
	}
}
