package eu.apenet.dashboard.archivallandscape;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.ead.xml.EadCreator;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dashboard.utils.ZipManager;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.Lang;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Class supports download and upload actions
 * for Archival Landscape. It's separated for the rest
 * of the Archival Landscape edition because these operations
 * only support the last Archival Landscape export/import 
 * operations (download and upload), which are based on DDBB 
 * and not in any File storage. 
 */
public class ArchivalLandscapeManager extends AbstractAction{
	
	private final Logger log = Logger.getLogger(getClass());
	
	private static final String AL_XMLNS = "urn:isbn:1-931666-22-9";
	private static final String AL_XMLNS_XLINK = "http://www.w3.org/1999/xlink";
	private static final String AL_XMLNS_XSI = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI; //"http://www.w3.org/2001/XMLSchema-instance";
	private static final String AL_AUDIENCE = "external";
	private static final String AL_XSI_SCHEMALOCATION = "urn:isbn:1-931666-22-9 ead.xsd";
	private static final String AL_EADHEADER_COUNTRYENCODING = "iso3166-1";
	private static final String AL_EADHEADER_DATAENCODING = "iso8601";
	private static final String AL_EADHEADER_LANGENCODING = "iso639-2b";
	private static final String AL_EADHEADER_RELATEDENCODING = "MARC21";
	private static final String AL_EADHEADER_REPOSITORYENCODING = "iso15511";
	private static final String AL_EADHEADER_SCRIPTENCODING = "iso15924";
	private static final String AL_EADID_IDENTIFIER = "ape_archival_landscape";
	private static final String AL_EADID_MAINAGENCYCODE = "-APEnet";
	private static final String AL_EADID_PREFIX = "AL-";
	private static final String AL_TITLEPROPER_ENCODINGANALOG = "245";
	private static final String AL_TITLEPROPER_TYPE = "eng";
	private static final String AL_TITLEPROPER = "Archives Portal Europe - Archival Landscape";
	private static final String AL_GLOBAL_ENCODINGANALOG = "3.1.4";
	private static final String AL_RELATEDENCODING = "ISAD(G)v2";
	private static final String AL_ARCHDESC_TYPE = "inventory"; //probably could be "archival_landscape" value like other ALs
	private static final String AI_DSC_TYPE = "othertype";
	
	private static final String AL_FILE_NAME = "AL.xml";
	private static final String LEVEL = "level";
	private static final String FONDS = "fonds";
	private static final String SERIES = "series";
	private static final String FILE = "file";
	
	private List<ArchivalInstitution> totalInstitutions;
	private Map<String,ArchivalInstitution> groupsInsertedIntoDDBB;
	private List<ArchivalInstitution> updatedInstitutions;
	private List<ArchivalInstitution> deletedInstitutions;
	private List<ArchivalInstitution> notChangedInstitutions;
	private List<ArchivalInstitution> insertedInstitutions;
	
	private File httpFile;
	private String httpFileFileName;
	private Country country;

	private ArchivalInstitutionDAO aIDAO;
	private AiAlternativeNameDAO aIANDAO;

	private Map<String, Integer> positions;

	public void setHttpFile(File httpFile){
		this.httpFile = httpFile;
	}
	
	public List<ArchivalInstitution> getTotalInstitutions() {
		return totalInstitutions;
	}

	public List<ArchivalInstitution> getUpdatedInstitutions() {
		return updatedInstitutions;
	}

	public List<ArchivalInstitution> getDeletedInstitutions() {
		return deletedInstitutions;
	}

	public List<ArchivalInstitution> getNotChangedInstitutions() {
		return notChangedInstitutions;
	}

	public List<ArchivalInstitution> getInsertedInstitutions() {
		return insertedInstitutions;
	}

	public void setHttpFileFileName(String httpFileFileName) {
		this.httpFileFileName = httpFileFileName;
	}
	
	public String upload(){
		if(this.httpFile!=null && this.httpFileFileName!=null){
			String path = this.httpFile.getAbsolutePath();
			String format = this.httpFileFileName.substring(this.httpFileFileName.lastIndexOf(".") + 1).toLowerCase();
			if(format.equalsIgnoreCase("xml")){
				return ingestArchivalLandscapeXML();
			}else if(format.equals("zip") && path.contains(File.separator)){ //1. unzip
	        	path = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR;
				String tempPath = path + "tmp" + this.httpFileFileName + new Date().getTime() + APEnetUtilities.FILESEPARATOR;	//This is the path in which the zip files are going to be unzipped
				String fullFileName = null;
				fullFileName = tempPath + this.httpFileFileName;
				File pathFile = new File(tempPath);
	    		File theFile = new File(fullFileName);
	    		if(pathFile.exists()){
	    			if(theFile.exists() && theFile.canWrite()){
						theFile.delete();
	    			}
				} else {
    				pathFile.mkdir();
	    		}
	    		try {
					FileUtils.moveFile(this.httpFile, theFile);
					this.httpFile = theFile;
				} catch (IOException e) {
					log.error("Problem trying to move temp uploaded file",e);
				}

				ZipManager zipManager = new ZipManager(tempPath);
                zipManager.unzip(fullFileName);
                if(this.httpFile.canWrite()){
                	this.httpFile.delete(); //remove upload file, now it's not needed
                }
				if(pathFile.isDirectory()){
					String[] files = pathFile.list();
					boolean found = false;
					for(int i=0;!found && i<files.length;i++){
						File targetFile = new File(tempPath + files[i]);
						found = checkFileLevel(targetFile);
					}
					if(found){ //3. launch logic for AL.xml file
						try{
							return ingestArchivalLandscapeXML();
						}catch(Exception e){
							log.error("Error trying to manage AL uploaded",e);
						}finally{
							if(pathFile.canWrite()){ //it's a temp file, so it should be removed.
								try {
									FileUtils.deleteDirectory(pathFile);
								} catch (IOException e) {
									log.error("Error trying to delete temp AL path, directory will keep there: "+pathFile.getAbsolutePath(),e);
								}
							}
						}
					}
				}
			}
		}
		return ERROR;
	}
	/**
	 * Search xml file from File (folders) structure and
	 * fill global this.httpFile.
	 * 
	 * @param targetFile
	 */
	private boolean checkFileLevel(File targetFile) {
		boolean found = false;
		if(targetFile.isFile() && targetFile.getAbsolutePath().contains(".")){
			String format2 = targetFile.getAbsolutePath().substring(targetFile.getAbsolutePath().lastIndexOf(".") + 1).toLowerCase();
			if(format2.equalsIgnoreCase("xml")){
				String path = targetFile.getAbsolutePath();
				this.httpFile = new File(path);
				found = true;
			}
		}else{
			String[] files = targetFile.list();
			for(int i=0;!found && i<files.length;i++){
				found = checkFileLevel(new File(targetFile.getAbsolutePath() + File.separator + files[i]));
			}
		}
		return found;
	}

	private List<ArchivalInstitution> checkChild(ArchivalInstitution parent){
		List<ArchivalInstitution> archivalInstitutionList = new ArrayList<ArchivalInstitution>();
		log.debug("children check for parent: "+parent.getAiname());
		Set<ArchivalInstitution> children = parent.getChildArchivalInstitutions();
		Iterator<ArchivalInstitution> itChildren = children.iterator();
		while(itChildren.hasNext()){
			ArchivalInstitution institution = itChildren.next();
			if(institution.isGroup()){
				log.debug("Group: "+institution.getAiname()+" parent: "+parent.getAiname());
				if(institution.getChildArchivalInstitutions()!=null && institution.getChildArchivalInstitutions().size()>0){
					archivalInstitutionList.addAll(checkChild(institution));
				}
			}else{
				log.debug("Institution: "+institution.getAiname()+" parent: "+parent.getAiname());
			}
			institution.setParentAiId((institution.getParent()!=null)?institution.getParent().getAiId():null); //parent_ai_id fix for bad hibernate mapping
			archivalInstitutionList.add(institution);
		}
		return archivalInstitutionList;
	}
	/**
	 * Main function called to ingest all content.
	 * 
	 * This function is explited in two parts:
	 * 	1) Read target file and returns his archival-institutions structure
	 * 	2) Checks if previews structure is possible to insert/update into 
	 * 		current system and make ingestion/update logic.
	 * 
	 * @return Structs2-RESPONSE
	 */
	private String ingestArchivalLandscapeXML() {
		Collection<ArchivalInstitution> archivalInstitutions = getInstitutionsByALFile(this.httpFile);
		boolean state = false;
		if(archivalInstitutions!=null){
			try{
				state = checkAndUpdateFromToDDBB(archivalInstitutions);
			}catch(Exception e){
				log.error("Exception checking institutions with ddbb to be replaced",e);
			}
			if(state){
				return SUCCESS;
			}
		}
		return ERROR;
	}

	/**
	 * Checks and work with an archival_institution structure.
	 * Tries to insert, update and delete institutions.
	 * 
	 * @param archivalInstitutions
	 * @return validOperation
	 */
	private boolean checkAndUpdateFromToDDBB(Collection<ArchivalInstitution> archivalInstitutions) {
		boolean validOperation = true; //flag used to rollback the process when some rule is wrong
		Integer state = 0;
		if(archivalInstitutions!=null){
			try{
				state = 1;
				this.aIDAO = DAOFactory.instance().getArchivalInstitutionDAO();
				this.aIANDAO = DAOFactory.instance().getAiAlternativeNameDAO();
				this.totalInstitutions = this.aIDAO.getArchivalInstitutionsByCountryId(SecurityContext.get().getCountryId(),false); //TODO check if this query affects next checkInstitutionStructure 
				if(this.totalInstitutions!=null && this.totalInstitutions.size()>0){
					log.warn("Archival landscape could not be ingested directly, there are some institutions to check. Checking...");
//					Iterator<ArchivalInstitution> currentIt = this.totalInstitutions.iterator();
					this.updatedInstitutions = new ArrayList<ArchivalInstitution>();
					this.insertedInstitutions = new ArrayList<ArchivalInstitution>();
//					archivalInstitutionsPlainList = null; //clean
					state = 2;
					//check if some institution of the new archivalInstitutions is/are into system and has content indexed
					validOperation = checkIfSomeInstitutionIsIngestedAndHasContentIndexed(archivalInstitutions);
					if(validOperation){
						JpaUtil.beginDatabaseTransaction();
						//when valid operation it's able to remove all institutions
//						checkUpdateArchivalInstitution(archivalInstitutions); //old check, update-troubles
						this.deletedInstitutions = new ArrayList<ArchivalInstitution>();
						
						this.totalInstitutions = this.aIDAO.getArchivalInstitutionsByCountryId(SecurityContext.get().getCountryId(),true);
						
						checkAndUpdateArchivalInstitutions(null,archivalInstitutions); //new check
						log.debug("Updated process has been finished successfull");
						state = 3;
						
						log.debug("Insert process for not updated institutions");
						
						//do an insert for rest file institutions
						insertNotUpdatedInstitutions(archivalInstitutions);
						log.debug("Done insert process!");
						state = 4;
//						//now delete all institutions which has not been updated (old institutions are not being processed)
						//institutions to be deleted = totalDDBBinstitution - (institutionsUpdated + institutionsDeleted)
						this.deletedInstitutions = new ArrayList<ArchivalInstitution>(); //clean
						boolean error = deleteSimpleUnusedInstitutions();
//						checkDeleteArchivalInstitutions(currentIngestedInstitutions);
						if(!error){
							state = 5;
							JpaUtil.commitDatabaseTransaction();
						}else{
							state = 6;
							log.debug("Invalid operation detected.");
							validOperation = false;
							JpaUtil.rollbackDatabaseTransaction();
						}
					}
				}else{
					state = 7;
//					archivalInstitutionsPlainList = null; //clean
					this.insertedInstitutions = new ArrayList<ArchivalInstitution>();
					JpaUtil.beginDatabaseTransaction();
					state = 8;
					insertChildren(archivalInstitutions,null); //insert institution
					JpaUtil.commitDatabaseTransaction();
					state = 9;
				}
			}catch(Exception e){
				validOperation = false;
				log.error("Some excepton comparing new AL structure with old AL structure. state: "+state, e.getCause());
				if(!JpaUtil.noTransaction()){
					JpaUtil.rollbackDatabaseTransaction();
				}
			}finally{
				if(!JpaUtil.noTransaction()){
					JpaUtil.closeDatabaseSession();
				}
			}
		}
		return validOperation;
	}
	/**
	 * Detects and checks if some institution exists into system which algorithm
	 * could overwrite. When it's detected an overwrite check it's launched.
	 * Returns if an override and/or normal process is possible.
	 * 
	 * @param archivalInstitutions
	 */
	private boolean checkIfSomeInstitutionIsIngestedAndHasContentIndexed(Collection<ArchivalInstitution> archivalInstitutions) {
		boolean valid = true;
//		boolean exit = false;
		List<ArchivalInstitution> archivalInstitutionsPlainList = parseCollectionToPlainList(archivalInstitutions); //parse archivalInstitutions structure to plain identifiers list structure
		if(this.totalInstitutions!=null && this.totalInstitutions.size()>0){
			Iterator<ArchivalInstitution> itTotalInstitutions = this.totalInstitutions.iterator(); //DDBB ingested institutions
			//first looper, total institutions ingested into DDBB
			boolean institutionExists = false;
			while(valid && !institutionExists && itTotalInstitutions.hasNext()){
				ArchivalInstitution currentIngestedInstitution = itTotalInstitutions.next();
				//second loop, new institutions into plain format
				Iterator<ArchivalInstitution> itAIPlain = archivalInstitutionsPlainList.iterator();
				while(valid && itAIPlain.hasNext()){
					//compare first looped institution with second looped institution.
					ArchivalInstitution plainInstitution = itAIPlain.next();
					if(currentIngestedInstitution.getInternalAlId().equals(plainInstitution.getInternalAlId())){ //detected
						institutionExists=true;//not needed seek into other branches
						if(currentIngestedInstitution.isContainSearchableItems()){ //then check if ingested institution has content indexed
							valid = false;
						}
					}
				}
			}
		}
		return valid;
	}

	/* additional code not included, remove that unused
	  	//check if new-parents are the same old-parents
		ArchivalInstitution ingestedParent = currentIngestedInstitution.getParent();
		exit = false;
		if(ingestedParent!=null){
			ArchivalInstitution foundedArchivalInstitution = null;
			while(!exit && ingestedParent!=null){
				Iterator<ArchivalInstitution> aiIt = archivalInstitutions.iterator();
				while(foundedArchivalInstitution==null && aiIt.hasNext()){
					foundedArchivalInstitution = searchCurrentArchivalInstitution(aiIt.next(),ingestedParent);
				}
				exit = (foundedArchivalInstitution==null); //last level or check error control
			}
		}else{
			//TODO
		}
		if(exit && ingestedParent.getParent()==null){ //not possible
			valid = true;
		}
	 */ /*
	private ArchivalInstitution searchCurrentArchivalInstitution(ArchivalInstitution nextInstitution, ArchivalInstitution ingestedParent) {
		ArchivalInstitution target = null;
		if(nextInstitution!=null && ingestedParent!=null){
			ArchivalInstitution tempInstitution = nextInstitution.getParent();
			if(tempInstitution!=null){
				if(tempInstitution.getInternalAlId().equals(ingestedParent.getInternalAlId())){
					target = tempInstitution;
				}
			}
		}
		return target;
	}*/

	/**
	 * Insert not updated institutions. It should be the rest of the 
	 * param Collection<ArchivalInstitution> not updated.
	 * @param archivalInstitutions
	 */
	private void insertNotUpdatedInstitutions(Collection<ArchivalInstitution> archivalInstitutions) {
		if(archivalInstitutions!=null){
			Iterator<ArchivalInstitution> itAI = archivalInstitutions.iterator();
			while(itAI.hasNext()){
				ArchivalInstitution targetToBeInserted = itAI.next();
				//check if institutions has been updated
				boolean found = false;
				Iterator<ArchivalInstitution> itUpdated = this.updatedInstitutions.iterator();
				while(!found && itUpdated.hasNext()){
					ArchivalInstitution updatedInstitution = itUpdated.next();
					if(updatedInstitution.getInternalAlId().equals(targetToBeInserted.getInternalAlId())){
						found = true;
					}
				}
				if(!found){
					log.debug("Institution to be inserted: "+targetToBeInserted.getInternalAlId());
					insertInstitution(targetToBeInserted); //it's recurse, so it's not needed call to himself
				}
			}
		}
	}

	private boolean deleteSimpleUnusedInstitutions() {
		boolean error = false;
		log.debug("Begin delete process for old institutions.");
		List<ArchivalInstitution> excludedInstitutions = new ArrayList<ArchivalInstitution>();
		excludedInstitutions.addAll(this.updatedInstitutions);
		excludedInstitutions.addAll(this.insertedInstitutions);
		List<ArchivalInstitution> institutionsToBeDeleted = this.aIDAO.getArchivalInstitutionsByCountryIdUnless(SecurityContext.get().getCountryId(),excludedInstitutions, false);
		log.debug("Institutions to be deleted: "+institutionsToBeDeleted.size());
		Iterator<ArchivalInstitution> deleteIt = institutionsToBeDeleted.iterator();
		while(!error && deleteIt.hasNext()){
			ArchivalInstitution targetToBeDeleted = deleteIt.next();
			if(!targetToBeDeleted.isContainSearchableItems()){
				log.debug("Deleting institution: "+targetToBeDeleted.getInternalAlId());
				Set<AiAlternativeName> alternativeNames = targetToBeDeleted.getAiAlternativeNames();
				if(alternativeNames!=null && alternativeNames.size()>0){
					log.debug("Deleting alternative names...");
					Iterator<AiAlternativeName> itAN = alternativeNames.iterator();
					while(itAN.hasNext()){
						this.aIANDAO.deleteSimple(itAN.next());
					}
				}else{
//					error = true;
				}
				this.aIDAO.deleteSimple(targetToBeDeleted);
				this.deletedInstitutions.add(targetToBeDeleted);
				log.debug("Deleted institution with aiId: "+targetToBeDeleted.getAiId());
			}else{
				log.debug("Detected institution with content indexed, so it could not be deleted. Marked operation like invalid.");
				error = true;
			}
		}
		return error;
	}

	private List<ArchivalInstitution> parseCollectionToPlainList(Collection<ArchivalInstitution> archivalInstitutions) {
		List<ArchivalInstitution> archivalInstitutionsPlainList = new ArrayList<ArchivalInstitution>();
		if(archivalInstitutions!=null){
			Iterator<ArchivalInstitution> it = archivalInstitutions.iterator();
			while(it.hasNext()){
				ArchivalInstitution institution = it.next();
				if(institution.isGroup()){
					log.debug("Group: "+institution.getAiname());
					if(institution.getChildArchivalInstitutions()!=null){
						archivalInstitutionsPlainList.addAll(checkChild(institution));
					}
				}else{
					log.debug("Institution: "+institution.getAiname());
				}
				institution.setParentAiId((institution.getParent()!=null)?institution.getParent().getAiId():null); //parent_ai_id fix for bad hibernate mapping
				archivalInstitutionsPlainList.add(institution);
			}
		}
		return archivalInstitutionsPlainList;
	}

	/**
	 * Checks if it's needed some delete operation.
	 * 
	 * @param currentIngestedArchivalInstitutions
	 */
	private void checkDeleteArchivalInstitutions(List<ArchivalInstitution> currentIngestedArchivalInstitutions) {
		this.deletedInstitutions = new ArrayList<ArchivalInstitution>();
		Iterator<ArchivalInstitution> currentIt = currentIngestedArchivalInstitutions.iterator();
		while(currentIt.hasNext()){
			ArchivalInstitution possibleDeletedInstitution = currentIt.next();
			Iterator<ArchivalInstitution> updatedIt = this.updatedInstitutions.iterator();
			boolean found = false;
			while(!found && updatedIt.hasNext()){ //it's better look inside updateInstituons because always the size is less than ingestedInstutions
				ArchivalInstitution institutionLookingFor = updatedIt.next();
				if(institutionLookingFor.getInternalAlId().equals(possibleDeletedInstitution.getInternalAlId()) && !(institutionLookingFor.getAiId()==possibleDeletedInstitution.getAiId())){
					deleteInstitution(possibleDeletedInstitution);
					found = true;
				}
			}
		}
	}
	
	private boolean checkAndUpdateArchivalInstitutions(ArchivalInstitution target,Collection<ArchivalInstitution> archivalInstitutions) {
		boolean found = false;
		if(this.positions==null){
			this.positions = new HashMap<String,Integer>();
		}
		try{
			if(target==null){
				if(this.totalInstitutions!=null){
					List<ArchivalInstitution> parentInstitution = new ArrayList<ArchivalInstitution>();
					//1. discriminate all institutions which are not children
					Iterator<ArchivalInstitution> itTI = this.totalInstitutions.iterator();
					log.debug("Getting root institutions...");
					this.groupsInsertedIntoDDBB = new HashMap<String,ArchivalInstitution>();
					while(itTI.hasNext()){
						ArchivalInstitution institution = itTI.next();
						if(institution.getParent()==null || institution.getParentAiId()==null){
							parentInstitution.add(institution);
							if(institution.isGroup()){
								this.groupsInsertedIntoDDBB.put(institution.getInternalAlId(),institution);
							}
							log.debug("Appending to parent list: "+institution.getAiname()+".");
						}
					}
					//2. start by parents institutions
					log.debug("Checking root parents with size: "+parentInstitution.size());
					found = checkParents(parentInstitution,archivalInstitutions);
				}
			}else{ //search institution into all archivalInstitutions-tree
				found = institutionUpdate(archivalInstitutions,target); //process to check recursively
			}
		}catch(Exception e){
			log.error("Some bad happened trying to update institution",e);
		}
		return found;
	}
	
	/**
	 * Recursive function to check root parents and all his children when
	 * target parent institution has other institutions pending of.
	 * 
	 * Returns boolean, if target institution is updated or not.
	 * 
	 * @param parentInstitutions
	 * @param archivalInstitutions
	 */
	private boolean checkParents(List<ArchivalInstitution> parentInstitutions,Collection<ArchivalInstitution> archivalInstitutions) {
		boolean found = false;
		Iterator<ArchivalInstitution> itParentInstitution = parentInstitutions.iterator();
		while(itParentInstitution.hasNext()){
			ArchivalInstitution currentParent = itParentInstitution.next();
			log.debug("Checking parent: "+currentParent.getAiname());
			//check parent
			found = institutionUpdate(archivalInstitutions,currentParent); //process to check recursively
			log.debug("Check done, found: "+found);
			//check child structure
			if(currentParent.isGroup()){
				//check each children first
				Set<ArchivalInstitution> children = currentParent.getChildArchivalInstitutions();
				if(children!=null && children.size()>0){
					log.debug("Checking children with size: "+children.size());
					checkParents(new ArrayList<ArchivalInstitution>(children),archivalInstitutions);
				}
			}
		}
		return found;
	}

	/**
	 * This function update an institution when it's found.
	 * When it's found List<ArchivalInstitution> this.deletedInstitutions is updated
	 * and the current founded institution is appended to this list.
	 * 
	 * It returns a boolean if the target Archival Institution is found or not.
	 * 
	 * @param archivalInstitutions
	 * @param target
	 */
	private boolean institutionUpdate(Collection<ArchivalInstitution> archivalInstitutions, ArchivalInstitution target) {
		boolean found = false;
		Iterator<ArchivalInstitution> archivalInstitutionsIt = archivalInstitutions.iterator();
		while(!found && archivalInstitutionsIt.hasNext()){
			ArchivalInstitution archivalInstitution = archivalInstitutionsIt.next();
			if(archivalInstitution.getInternalAlId().equals(target.getInternalAlId())){ //found
				log.debug("Found institution equal: "+target.getAiname()+". Replacing node...");
				found = true;
				this.deletedInstitutions.add(target); //not needed for search afterwards
				replaceNode(target,archivalInstitution); //replace node
				log.debug("Replace done for institution: "+target.getAiname());
			}else if(archivalInstitution.isGroup()){//not found
				Set<ArchivalInstitution> children = archivalInstitution.getChildArchivalInstitutions();
				if(children!=null && children.size()>0){
					log.debug("Checking children with size: "+children.size());
					found = institutionUpdate(children,target); //search into his children (not target, only archivalInstitutions provided).
				}
			}else{
				log.debug("Institution not found. Checked institution: "+archivalInstitution.getAiname());
			}
		}
		return found;
	}
	/**
	 * Update the current node with the new information.
	 * Institutions - Alternative Names are being updating.
	 * If institution has children they are not being updated.
	 * 
	 * @param oldDDBBInstitution
	 * @param updatedInstitution
	 */
	private boolean replaceNode(ArchivalInstitution oldDDBBInstitution,ArchivalInstitution updatedInstitution) { //TODO
		boolean state = true;
		//1. update institution information
		oldDDBBInstitution.setAlorder(updatedInstitution.getAlorder());
		oldDDBBInstitution.setGroup(updatedInstitution.isGroup());
		Integer parentAiId = null;
		ArchivalInstitution parent = null;
		if(updatedInstitution.getParent()!=null){
			//check all inserted groups and get current parent from ddbb results
			if(updatedInstitution.getParent()!= null && this.groupsInsertedIntoDDBB.containsKey(updatedInstitution.getParent().getInternalAlId())){
				parent = this.groupsInsertedIntoDDBB.get(updatedInstitution.getParent().getInternalAlId());
				parentAiId = parent.getAiId();
			}
		}
		oldDDBBInstitution.setParent(parent);
		oldDDBBInstitution.setParentAiId(parentAiId);
		oldDDBBInstitution.setAiname(updatedInstitution.getAiname());
		//2. update alternative names
		Set<AiAlternativeName> oldAlternativeNames = oldDDBBInstitution.getAiAlternativeNames();
		if(oldAlternativeNames!=null){
			Iterator<AiAlternativeName> itOldAN = oldAlternativeNames.iterator();
			Set<AiAlternativeName> newANs = updatedInstitution.getAiAlternativeNames();
			boolean found = false;
			while(itOldAN.hasNext()){
				found = false;
				AiAlternativeName oldAlternativeName = itOldAN.next();
				if(newANs!=null){
					Iterator<AiAlternativeName> itNewANs = newANs.iterator();
					while(!found && itNewANs.hasNext()){
						AiAlternativeName newAlternativeName = itNewANs.next();
						if(oldAlternativeName.getLang().equals(newAlternativeName.getLang())){
							found = true;
							oldAlternativeName.setAiAName(newAlternativeName.getAiAName());
//							oldAlternativeName.setPrimaryName(oldAlternativeName.getPrimaryName()!=null?oldAlternativeName.getPrimaryName():false);
							newANs.remove(oldAlternativeName);
							this.aIANDAO.updateSimple(oldAlternativeName);
						}
					}
					if(!found){
						this.aIANDAO.deleteSimple(oldAlternativeName);
					}
				}
			}
			if(!newANs.isEmpty()){
				Iterator<AiAlternativeName> itANToInsert = newANs.iterator(); //list has been updated including only the items which have to be inserted
				while(itANToInsert.hasNext()){
					AiAlternativeName aiAN = itANToInsert.next();
					aiAN.setPrimaryName(aiAN.getPrimaryName()!=null?aiAN.getPrimaryName():false);
					aiAN.setArchivalInstitution(oldDDBBInstitution);
					this.aIANDAO.insertSimple(aiAN);
				}
			}
		}
		//3. launch institution update
		this.aIDAO.updateSimple(oldDDBBInstitution);
		this.updatedInstitutions.add(oldDDBBInstitution); //TODO, review if this list is used in other places
		return state;
	}

	/**
	 * Deletes a simple institution (TODO at this moment it doesn't count children) and his alternative names.
	 * 
	 * @param possibleDeletedInstitution
	 */
	private void deleteInstitution(ArchivalInstitution possibleDeletedInstitution) {
		Set<AiAlternativeName> itAiAN = possibleDeletedInstitution.getAiAlternativeNames();
		if(itAiAN!=null){
			Iterator<AiAlternativeName> itAN = itAiAN.iterator();
			while(itAN.hasNext()){
				AiAlternativeName alternativeName = itAN.next();
				this.aIANDAO.deleteSimple(alternativeName);
				log.debug("Deleted alternative name: "+alternativeName.getAiAName());
			}
		}
		if(possibleDeletedInstitution.isGroup()){
			Set<ArchivalInstitution> possibleChildren = possibleDeletedInstitution.getChildArchivalInstitutions();
			if(possibleChildren!=null && possibleChildren.size()>0){
				log.debug("Checking children for delete, parent: "+possibleDeletedInstitution.getAiname());
				checkDeleteArchivalInstitutions(new ArrayList<ArchivalInstitution>(possibleChildren));
			}
		}
		this.aIDAO.deleteSimple(possibleDeletedInstitution);
		log.debug("Deleted institution: "+possibleDeletedInstitution.getAiname());
		this.deletedInstitutions.add(possibleDeletedInstitution);
	}

	/**
	 * Recursive function to ingest an Archival Institution 
	 * to DDBB and his children.
	 * 
	 * @param archivalInstitutions
	 * @param parent
	 */
	private void insertChildren(Collection<ArchivalInstitution> archivalInstitutions,ArchivalInstitution parent) {
		Iterator<ArchivalInstitution> aiIt = archivalInstitutions.iterator();
		List<ArchivalInstitution> institutionsToBeInserted = new ArrayList<ArchivalInstitution>();
		while(aiIt.hasNext()){
			ArchivalInstitution currentInstitution = aiIt.next();
			currentInstitution.setParent(parent);
			if(parent!=null){
				currentInstitution.setParentAiId(parent.getAiId()); //fix for hibernate map
			}
			if(currentInstitution.isGroup()){
				Set<ArchivalInstitution> institutionChildren = currentInstitution.getChildArchivalInstitutions();
				if(institutionChildren!=null && institutionChildren.size()>0){
					ArchivalInstitution childParent = this.aIDAO.insertSimple(currentInstitution);
					this.insertedInstitutions.add(currentInstitution);
					log.debug("Inserted parent "+currentInstitution.getInternalAlId()+" with id:"+childParent.getAiId()+" which contain children");
					insertChildren(institutionChildren,childParent);
				}else{
					insertInstitution(currentInstitution);
				}
			}else{
				insertInstitution(currentInstitution);
			}
		}
		if(institutionsToBeInserted.size()>0){
//			this.aIDAO.store(institutionsToBeInserted);
//			this.insertedInstitutions.addAll(institutionsToBeInserted);
			log.debug("Inserted institution children with size: "+institutionsToBeInserted.size()+".");
		}
	}
	/**
	 * Recursive function to ingest an Archival Institution.
	 * 
	 * It's called by insertChildren institution.
	 * 
	 * @param currentInstitution
	 */
	private void insertInstitution(ArchivalInstitution currentInstitution) {
		String internalAlId = currentInstitution.getInternalAlId();
		if(internalAlId==null || internalAlId.isEmpty()){
			internalAlId = ArchivalLandscapeEditor.getNewinternalIdentifier();
			currentInstitution.setInternalAlId(internalAlId);
		}
		ArchivalInstitution institution = this.aIDAO.insertSimple(currentInstitution);
		if(institution!=null && institution.isGroup()){
			this.groupsInsertedIntoDDBB.put(institution.getInternalAlId(),institution);
		}
		//alternative names logic
		Set<AiAlternativeName> alternativeNames = currentInstitution.getAiAlternativeNames();
		if(alternativeNames!=null){
			Iterator<AiAlternativeName> alternativeNamesIt = alternativeNames.iterator();
			while(alternativeNamesIt.hasNext()){
				AiAlternativeName alternativeName = alternativeNamesIt.next();
				alternativeName.setArchivalInstitution(institution);
				alternativeName.setPrimaryName(alternativeName.getPrimaryName()!=null?alternativeName.getPrimaryName():false); //TODO, change by some check
				this.aIANDAO.insertSimple(alternativeName);
				log.debug("Inserted alternative name: "+alternativeName.getAiAName());
			}
		}
		if(currentInstitution.isGroup()){
			Set<ArchivalInstitution> childrenToInsert = currentInstitution.getChildArchivalInstitutions();
			if(childrenToInsert!=null && childrenToInsert.size()>0){
				Iterator<ArchivalInstitution> itInstitutionChildren = childrenToInsert.iterator();
				while(itInstitutionChildren.hasNext()){
					ArchivalInstitution childInstitutionToInsert = itInstitutionChildren.next();
					childInstitutionToInsert.setParent(institution);
					childInstitutionToInsert.setParentAiId(institution.getAiId());
					log.debug("Trying to insert child institution");
					insertInstitution(childInstitutionToInsert);
				}
			}
		}
		this.insertedInstitutions.add(currentInstitution);
		log.debug("Inserted institution: "+currentInstitution.getAiname());
	}

	/**
	 * Check if some operation is valid by structure restrictions.
	 * Returns boolean [which state]
	 */
	private boolean checkInstitutionStructure(ArchivalInstitution currentInstitution,List<ArchivalInstitution> archivalInstitutionsPlainList) {
		boolean validOperation = true;
		Iterator<ArchivalInstitution> fileIt = archivalInstitutionsPlainList.iterator();
		while(validOperation && fileIt.hasNext()){
			ArchivalInstitution fileInstitution = fileIt.next();
			String fileIdentifier = fileInstitution.getInternalAlId();
			String currentIdentifier = currentInstitution.getInternalAlId();
			if(fileIdentifier!=null && currentIdentifier!=null && fileIdentifier.equals(currentIdentifier)){
				//check parents
				if(fileInstitution.getParent()!=null && currentInstitution.getParent()!=null){
					log.debug("CheckInstitutionsStructure: check parents for institution:"+currentInstitution.getInternalAlId());
					validOperation = checkInstitutionStructure(currentInstitution.getParent(), archivalInstitutionsPlainList);
				}else if(fileInstitution.getParent()!=null || currentInstitution.getParent()!=null){ //one of both
					log.debug("CheckInstitutionsStructure: not found parent for some comparable institution:"+currentInstitution.getInternalAlId()+" , marking like invalid operation");
					validOperation = false;
				}else{
					log.debug("CheckInstitutionsStructure: not found parent for some comparable institution:");
					//this.updatedInstitutions.add(currentInstitution);
				}
			}
		}
		return validOperation;
	}
	/**
	 * It extracts and returns file institutions. 
	 * 
	 * @param archivalInstitutionFile
	 */
	private Collection<ArchivalInstitution> getInstitutionsByALFile(File archivalInstitutionFile) {
		Collection<ArchivalInstitution> archivalInstitutions = null;
		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			XMLStreamReader r = factory.createXMLStreamReader(new FileReader(archivalInstitutionFile));
			archivalInstitutions = getXMLArchivalInstitutionLevel(r);
		} catch (FileNotFoundException e) {
			log.error("File not found :: "+archivalInstitutionFile.getAbsolutePath(), e.getCause());
		} catch (XMLStreamException e) {
			log.error("Archival Landscape reading exception", e.getCause());
		} catch (Exception e){
			log.error("Exception: ", e.getCause());
		}
		return archivalInstitutions;
	}
	
	/**
	 * Method which obtain and return country institution children
	 * readed from file by SAX.
	 * 
	 * @throws XMLStreamException
	 */
	private Collection<ArchivalInstitution> getXMLArchivalInstitutionLevel(XMLStreamReader r) throws XMLStreamException{
		ArchivalInstitution archivalInstitution = null;
		Set<ArchivalInstitution> archivalInstitutions = new HashSet<ArchivalInstitution>();
		String level = "";
		String id = "";
		String alternativeNameText = null;
		String alternativeLangText = null;
		boolean validXML = true;
		boolean continueLoop = false;
		Integer event = null;
		boolean openLevel = false;
		int counter = 0;
		while(validXML && r.hasNext()){
			if(!continueLoop){
				event = r.next();
			}else{
				continueLoop = false;
			}
			switch(event){
				case XMLStreamConstants.START_ELEMENT:
					String localName = r.getLocalName().trim();
					if(localName.equals("c")){
						//fill children
						if(openLevel && level!=null && level.equals(SERIES)){
							Set<ArchivalInstitution> children = getXMLArchivalInstitutionLevelChildren(r,archivalInstitution);
							if(children!=null){
								archivalInstitution.setChildArchivalInstitutions(children);
								log.debug("Children with size: "+children.size()+" has been added to institution(group) "+archivalInstitution.getAiname());
							}else{
								log.error("Bad XML file, has a bad children structure, null received.");
								validXML = false;
							}
							archivalInstitution.setGroup(true);
							event = r.getEventType();
							continueLoop = true;
						//end fill children
						}else{
							//it a local c level, no child level 
							id = level = alternativeNameText = alternativeLangText = "";
							archivalInstitution = new ArchivalInstitution();
							archivalInstitution.setParent(null);
//							archivalInstitution.setAlorder(counter++); //preserve file order
							counter++;
							for (int i = 0; i < r.getAttributeCount(); i++) {
								if(r.getAttributeLocalName(i)!=null && r.getAttributeLocalName(i).trim().equals("id")){
									id = r.getAttributeValue(i).trim();
								}else if (r.getAttributeLocalName(i).trim().equals(LEVEL)) {
									level = r.getAttributeValue(i).trim();
								}
							}
							if(!level.equals(FONDS)){
								if(!openLevel){
									openLevel = true;
									archivalInstitution.setAlorder(archivalInstitutions.size());
									archivalInstitution.setGroup(level.equals(SERIES));
									archivalInstitution.setInternalAlId((id!=null && id.length()>0)?id:null);
								}else{ //c level only could be opened into fonts or serial levels
									log.error("Bad sintaxys detected on xml, one C open tag should not be in some place");
									validXML = false;
								}
							}else{
								archivalInstitution = null;
								if(this.country==null){
									this.country = DAOFactory.instance().getCountryDAO().getCountryByCname(SecurityContext.get().getCountryName());
								}
							}
						}
					}else if(localName.equals("unittitle") && archivalInstitution!=null){
						alternativeNameText = "";
						for(int i=0;i<r.getAttributeCount();i++) {
							if(r.getAttributeLocalName(i)!=null && r.getAttributeLocalName(i).trim().equals("type")){
								alternativeLangText = r.getAttributeValue(i).trim()
								    .replaceAll("[\\s+&&[^\\n]]+"," ") //1. reduce all non-newline whitespaces to a unique space
								    .replaceAll("(?m)^\\s+|\\s$","") //2. remove spaces from start or end of the lines
								    .replaceAll("\\n+"," "); //3. remove all newlines, compress it in a unique line
							}
						}
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if(r.getLocalName().trim().equals("c")){
						if(!level.equals(FONDS)){ //fond level mustn't be used like an institution, it's a country
							archivalInstitutions.add(archivalInstitution); //first level
							openLevel = false;
						}
					}else if(r.getLocalName().trim().equals("unittitle") && archivalInstitution!=null){
						if(archivalInstitution.getAiname()==null || archivalInstitution.getAiname().trim().length()==0){
							archivalInstitution.setAiname(alternativeNameText);
						}
						Set<AiAlternativeName> alternativeNames = archivalInstitution.getAiAlternativeNames();
						if(alternativeNames==null){
							alternativeNames = new HashSet<AiAlternativeName>();
						}
						if(archivalInstitution.getAiname()==null){
							archivalInstitution.setAiname(alternativeNameText);
						}
						AiAlternativeName alternativeName = new AiAlternativeName();
						alternativeName.setAiAName(alternativeNameText);
						alternativeNames.add(alternativeName);
						archivalInstitution.setAiAlternativeNames(alternativeNames);
						Lang lang = null;
						List<Lang> languages = DAOFactory.instance().getLangDAO().findAll();
						for(int i=0;lang==null && i<languages.size();i++){
							String tempLanguage = languages.get(i).getIsoname();
							if(tempLanguage.equalsIgnoreCase(alternativeLangText)){
								lang = languages.get(i);
							}
						}
						if(lang!=null){
							alternativeName.setLang(lang);
							archivalInstitution.setCountry(this.country);
							archivalInstitution.setCountryId(this.country.getId()); //fix for current bad Hibernate mapping
						}else{
							log.error("Bad xml detected, reason: not lang for alternative name");
							validXML = false;
						}
						alternativeNameText = "";
						alternativeLangText = "";
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(!r.isWhiteSpace() && alternativeNameText!=null && archivalInstitution!=null){
//						if(archivalInstitution!=null && archivalInstitution.getAiname()!=null){
//							alternativeNameText = archivalInstitution.getAiname();
//						}
						alternativeNameText += r.getText().trim()
						    .replaceAll("[\\s+&&[^\\n]]+"," ")//1. reduce all non-newline whitespaces to a unique space
						    .replaceAll("(?m)^\\s+|\\s$","")//2. remove spaces from start or end of the lines
						    .replaceAll("\\n+"," ");//3. remove all newlines, compress it in a unique line
					}
			}
		}
		return archivalInstitutions;
	}
	/**
	 * Method which get all the series children, it's used to obtain recursive
	 * institution series children. 
	 * @throws XMLStreamException
	 */
	private Set<ArchivalInstitution> getXMLArchivalInstitutionLevelChildren(XMLStreamReader r, ArchivalInstitution aiParent) throws XMLStreamException {
		Set<ArchivalInstitution> archivalInstitutions = new HashSet<ArchivalInstitution>();
		boolean validXML = true;
		ArchivalInstitution archivalInstitution = null;
		Integer event = null;
		String unittitle = "";
		String uLang = null;
		String level = null;
		String id = null;
		boolean opened = false;
		boolean update = true;
		int counter = 0;
		int position = 0;
		while(validXML && r.hasNext()){
			if(!update){
				event = r.next();
			}else{
				update = false;
				event = r.getEventType();
			}
			switch(event){
				case XMLStreamConstants.START_ELEMENT:
					String localName = r.getLocalName().trim();
					if(localName.equals("c")){
						if(!opened){
							counter++;
							opened = true;
							archivalInstitution = new ArchivalInstitution();
//							archivalInstitution.setAlorder(position++);
							position++;
							for (int i = 0; i < r.getAttributeCount(); i++) {
								if(r.getAttributeLocalName(i)!=null && r.getAttributeLocalName(i).trim().equals("id")){
									id = r.getAttributeValue(i).trim();
								}else if (r.getAttributeLocalName(i).trim().equals(LEVEL)) {
									level = r.getAttributeValue(i).trim();
								}
							}
							archivalInstitution.setInternalAlId(id);
							archivalInstitution.setGroup((level!=null && level.equals(SERIES)));
							archivalInstitution.setAlorder(archivalInstitutions.size());
							archivalInstitution.setParent(aiParent);
						}else if(level!=null && level.equals(SERIES)){
							Set<ArchivalInstitution> children = getXMLArchivalInstitutionLevelChildren(r,archivalInstitution);
							log.debug("Children has been added with size: "+children.size()+" to institution with id: "+id);
							archivalInstitution.setChildArchivalInstitutions(children);
							update = true; //continue
						}
					}else if(localName.equals("unittitle") && archivalInstitution!=null){
						unittitle = ""; //clean old unittitle
						for(int i=0;i<r.getAttributeCount();i++) {
							if(r.getAttributeLocalName(i)!=null && r.getAttributeLocalName(i).trim().equals("type")){
								uLang = r.getAttributeValue(i).trim().replaceAll("[\\s+&&[^\\n]]+"," ") //1. reduce all non-newline whitespaces to a unique space
								    .replaceAll("(?m)^\\s+|\\s$","") //2. remove spaces from start or end of the lines
								    .replaceAll("\\n+"," "); //3. remove all newlines, compress it in a unique line
							}
						}
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if(r.getLocalName().trim().equals("c")){
						opened = false; //close detected
						log.debug("closing institution with id:"+id+" and level:"+level);
						if(!level.equals(FONDS)){ //fond level haven't be used like an institution, it's a country
							archivalInstitutions.add(archivalInstitution); //first level
							counter--;
						}
						if(counter<0){ //this condition mark when returns, it only happens when a parent </c> is detected
							return archivalInstitutions;
						}
					}else if(r.getLocalName().trim().equals("unittitle") && archivalInstitution!=null){
						if(archivalInstitution.getAiname()==null || archivalInstitution.getAiname().trim().length()==0){
							archivalInstitution.setAiname(unittitle);
						}
						Set<AiAlternativeName> alternativeNames = archivalInstitution.getAiAlternativeNames();
						if(alternativeNames==null){
							alternativeNames = new HashSet<AiAlternativeName>();
						}
						if(archivalInstitution.getAiname()==null){
							archivalInstitution.setAiname(unittitle);
						}
						AiAlternativeName alternativeName = new AiAlternativeName();
						alternativeName.setAiAName(unittitle);
						Lang lang = null;
						List<Lang> languages = DAOFactory.instance().getLangDAO().findAll();
						for(int i=0;lang==null && i<languages.size();i++){
							String tempLanguage = languages.get(i).getIsoname();
							if(tempLanguage.equalsIgnoreCase(uLang)){
								lang = languages.get(i);
							}
						}
						alternativeName.setLang(lang);
						alternativeNames.add(alternativeName);
						archivalInstitution.setAiAlternativeNames(alternativeNames);
						archivalInstitution.setCountry(this.country);
						archivalInstitution.setCountryId(this.country.getId()); //fix for current bad Hibernate mapping
						unittitle = "";
						uLang = "";
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(!r.isWhiteSpace() && unittitle!=null && archivalInstitution!=null){
//						if(archivalInstitution!=null && archivalInstitution.getAiname()!=null){
//							unittitle = archivalInstitution.getAiname();
//						}
						unittitle += r.getText().trim().replaceAll("[\\s+&&[^\\n]]+"," ")//1. reduce all non-newline whitespaces to a unique space
						    .replaceAll("(?m)^\\s+|\\s$","")//2. remove spaces from start or end of the lines
						    .replaceAll("\\n+"," ");//3. remove all newlines, compress it in a unique line
					}
			}
		}
		return archivalInstitutions;
	}

	public String download(){
		ByteArrayOutputStream xml = buildXMlFromDDBB();
		if(xml!=null){
			try {
				String fileName = SecurityContext.get().getCountryIsoname().toUpperCase()+AL_FILE_NAME;
				ContentUtils.download(getServletRequest(), getServletResponse(), new ByteArrayInputStream(xml.toByteArray()), fileName, ContentUtils.MIME_TYPE_APPLICATION_XML);
			} catch (IOException e) {
				log.error("Exception trying to download file with ContentUtils.downloadXML",e);
			}
			return SUCCESS;
		}
		return ERROR;
	}

	private ByteArrayOutputStream buildXMlFromDDBB() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		EadCreator eadCreator = null;
		try {
//			eadCreator = new EadCreator(outputStream); //TODO, fix EadCreator before use it
			StringBuilder eadContent = new StringBuilder();
			String countryName = getSecurityContext().getCountryName();
			if(countryName!=null){
				eadContent.append(openingXmlHeader());
				eadContent.append(openEadNode());
				
					eadContent.append(openEadHeader());
						eadContent.append(buildEadId());
						eadContent.append(buildFiledesc());
					eadContent.append(closeEadHeader());
					
					eadContent.append(openArchDesc());
						eadContent.append(openDsc());
							eadContent.append(buildCLevel(countryName,0)); //each archival institution is a C level
						eadContent.append(closeDsc());
					eadContent.append(closeArchDesc());
					
				eadContent.append(closeEadNode());
//				eadCreator.writeEadContent(eadContent.toString());
				outputStream.write(eadContent.toString().getBytes());
				eadContent = null;
			}
//		} catch (XMLStreamException e) {
//			log.error("Exception trying to call EadCreator() builder",e);
//		} catch (IOException e) {
//			log.error("Exception trying to call EadCreator.writeEadContent() method", e);
		} catch (Exception e){
			log.error("Unknown error into buildXMLFromDDBB",e);
		}finally {
//			if(eadCreator!=null){
//				try {
//					eadCreator.closeWriter();
//				} catch (XMLStreamException e) {
//					log.error("Exception trying to close writer with EadCreator", e);
//				}
//			}
		}
		return outputStream;
	}

	private String openingXmlHeader() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	}

	private String closeArchDesc() {
		return "\n\t</archdesc>";
	}

	private StringBuilder openArchDesc() {
		StringBuilder archDesc = new StringBuilder();
		archDesc.append("\n\t");
		archDesc.append("<archdesc");
		archDesc.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
		archDesc.append(" level=\"fonds\"");
		archDesc.append(" relatedencoding=\""+AL_RELATEDENCODING+"\"");
		archDesc.append(" type=\""+AL_ARCHDESC_TYPE+"\">");
		return archDesc;
	}

	private StringBuilder buildCLevel(Object parameter, int level) {
		StringBuilder cLevel = new StringBuilder();
		cLevel.append("\n");
		String tabs = "";
		for(int i=-3;i<level;i++){ //first level has 3 \t, so starts in that place
			tabs += "\t";
		}
		cLevel.append(tabs);
		if(parameter instanceof ArchivalInstitution){ //institution part, normal behavior, next cases
			ArchivalInstitution archivalInstitution = (ArchivalInstitution)parameter;
			if(archivalInstitution!=null){
				cLevel.append(buildInstitutionCLevel(archivalInstitution,tabs,level));
			}
		}else if(parameter instanceof String){ //country part, first case
			cLevel.append(buildFondsCLevel(parameter,tabs));
		}
		return cLevel;
	}

	private StringBuilder buildFondsCLevel(Object parameter, String tabs) {
		StringBuilder cLevel = new StringBuilder();
		cLevel.append("<c level=\""+FONDS+"\">");
		Country country = DAOFactory.instance().getCountryDAO().getCountryByCname(parameter.toString());
		Set<CouAlternativeName> couAlternativeNames = country.getCouAlternativeNames();
		if(couAlternativeNames!=null){
			Map<String,String> alternativeNames = new HashMap<String,String>();
			Iterator<CouAlternativeName> iteratorCouAlternativeNames = couAlternativeNames.iterator();
			while(iteratorCouAlternativeNames.hasNext()){
				CouAlternativeName couAlternativeName = iteratorCouAlternativeNames.next();
				String lang = couAlternativeName.getLang().getIsoname().toLowerCase(); //TODO, parse to 3_char_isoname
				alternativeNames.put(lang,couAlternativeName.getCouAnName());
			}
			cLevel.append(buildDidNode(alternativeNames,tabs));
		}
		ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> listCountryArchivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsByCountryId(country.getId(),true);
		if(listCountryArchivalInstitutions!=null){
			Iterator<ArchivalInstitution> itArchivalInstitutions = listCountryArchivalInstitutions.iterator();
			while(itArchivalInstitutions.hasNext()){
				cLevel.append(buildCLevel(itArchivalInstitutions.next(),2));
			}
		}
		cLevel.append("\n"+tabs);
		cLevel.append("</c>");
		return cLevel;
	}

	private StringBuilder buildInstitutionCLevel(ArchivalInstitution archivalInstitution,String tabs,Integer level) {
		StringBuilder cLevel = new StringBuilder();
		cLevel.append("<c");
		if(archivalInstitution.getInternalAlId()!=null && !archivalInstitution.getInternalAlId().isEmpty()){
			cLevel.append(" id=\""+archivalInstitution.getInternalAlId()+"\"");
		}
		if(archivalInstitution.isGroup()){
			cLevel.append(" level=\""+SERIES+"\">");
		}else{
			cLevel.append(" level=\""+FILE+"\">");
		}
		Set<AiAlternativeName> aiAlternativeNames = archivalInstitution.getAiAlternativeNames();
		if(aiAlternativeNames!=null){
			Map<String,String> alternativeNames = new HashMap<String,String>();
			Iterator<AiAlternativeName> itAlternativeNames = aiAlternativeNames.iterator();
			while(itAlternativeNames.hasNext()){
				AiAlternativeName alternativeName = itAlternativeNames.next();
				alternativeNames.put(alternativeName.getLang().getIsoname(),alternativeName.getAiAName());
			}
			cLevel.append(buildDidNode(alternativeNames, tabs));
		}
		Set<ArchivalInstitution> children = archivalInstitution.getChildArchivalInstitutions();
		if(children!=null){
			Iterator<ArchivalInstitution> itChildren = children.iterator();
			while(itChildren.hasNext()){
				cLevel.append(buildCLevel(itChildren.next(),level+1));
			}
		}
		//put HG link if exists
		HoldingsGuideDAO hgDao = DAOFactory.instance().getHoldingsGuideDAO();
		HoldingsGuide exampleHG = new HoldingsGuide();
		exampleHG.setAiId(archivalInstitution.getAiId());
		List<HoldingsGuide> hgList = hgDao.getHoldingsGuidesByArchivalInstitutionId(archivalInstitution.getAiId());
		if(hgList!=null && hgList.size()>0){
			Iterator<HoldingsGuide> itHg = hgList.iterator();
			while(itHg.hasNext()){
				cLevel.append(buildOtherfindaid(itHg.next(),tabs));
			}
		}
		cLevel.append("\n"+tabs);
		cLevel.append("</c>");
		return cLevel;
	}

	private StringBuilder buildOtherfindaid(HoldingsGuide holdingsGuide,String tabs) {
		StringBuilder otherfindaid = new StringBuilder();
		if(holdingsGuide!=null){
			otherfindaid.append("\n"+tabs);
			otherfindaid.append("<otherfindaid");
			otherfindaid.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
			otherfindaid.append(">");
			otherfindaid.append("\n"+tabs+"\t");
			otherfindaid.append("<p>");
			otherfindaid.append("\n"+tabs+"\t\t");
			otherfindaid.append("<extref");
			otherfindaid.append(" xlink:href=\""+holdingsGuide.getEadid()+"\" />");
			otherfindaid.append("\n"+tabs+"\t");
			otherfindaid.append("</p>");
			otherfindaid.append("\n"+tabs);
			otherfindaid.append("</otherfindaid>");
		}
		return otherfindaid;
	}

	private String closeDsc() {
		return "\n\t\t</dsc>";
	}

	private StringBuilder openDsc() {
		StringBuilder openedDsc = new StringBuilder();
		openedDsc.append("\n\t\t<dsc");
		openedDsc.append(" type=\""+AI_DSC_TYPE+"\">");
		return openedDsc;
	}

	private StringBuilder buildDidNode(Map<String, String> alternativeNames, String tabs) {
		StringBuilder didNode = new StringBuilder();
		didNode.append("\n"+tabs+"\t");
		didNode.append("<did>");
		Iterator<String> alternativeNamesIterator = alternativeNames.keySet().iterator();
		while(alternativeNamesIterator.hasNext()){
			String key = alternativeNamesIterator.next();
			didNode.append("\n"+tabs+"\t\t");
			didNode.append("<unittitle");
			didNode.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
			didNode.append(" type=\""+key+"\">");
			didNode.append(alternativeNames.get(key));
			didNode.append("</unittitle>");
		}
		didNode.append("\n"+tabs+"\t");
		didNode.append("</did>");
		return didNode;
	}

	private String closeEadHeader() {
		return "\n\t</eadheader>";
	}

	private StringBuilder buildFiledesc() {
		StringBuilder filedesc = new StringBuilder();
		filedesc.append("\n\t\t<filedesc>");
			filedesc.append("\n\t\t\t<titlestmt>");
				filedesc.append("\n\t\t\t\t<titleproper");
				filedesc.append(" encodinganalog=\""+AL_TITLEPROPER_ENCODINGANALOG+"\"");
				filedesc.append(" type=\""+AL_TITLEPROPER_TYPE+"\">");
				filedesc.append(AL_TITLEPROPER);
				filedesc.append("</titleproper>");
			filedesc.append("\n\t\t\t</titlestmt>");
		filedesc.append("\n\t\t</filedesc>");
		return filedesc;
	}

	private StringBuilder buildEadId() {
		String countryCode = "";
		StringBuilder eadId = new StringBuilder();
		eadId.append("\n\t\t<eadid");
		eadId.append(" countrycode=\""+countryCode+"\"");
		eadId.append(" identifier=\""+AL_EADID_IDENTIFIER+"\"");
		eadId.append(" mainagencycode=\""+countryCode+AL_EADID_MAINAGENCYCODE+"\"");
		eadId.append(">");
		eadId.append(AL_EADID_PREFIX+countryCode);
		eadId.append("</eadid>");
		return eadId;
	}

	private StringBuilder openEadHeader() {
		StringBuilder eadHeader = new StringBuilder();
		eadHeader.append("\n\t<eadheader");
		eadHeader.append(" countryencoding=\""+AL_EADHEADER_COUNTRYENCODING+"\"");
		eadHeader.append(" dateencoding=\""+AL_EADHEADER_DATAENCODING+"\"");
		eadHeader.append(" langencoding=\""+AL_EADHEADER_LANGENCODING+"\"");
		eadHeader.append(" relatedencoding=\""+AL_EADHEADER_RELATEDENCODING+"\"");
		eadHeader.append(" repositoryencoding=\""+AL_EADHEADER_REPOSITORYENCODING+"\"");
		eadHeader.append(" scriptencoding=\""+AL_EADHEADER_SCRIPTENCODING+"\"");
		eadHeader.append(">");
		return eadHeader;
	}

	private String closeEadNode() {
		return "\n</ead>";
	}

	private StringBuilder openEadNode() {
		StringBuilder eadNode = new StringBuilder();
		eadNode.append("<ead");
		eadNode.append(" xmlns=\""+AL_XMLNS+"\"");
		eadNode.append(" xmlns:xlink=\""+AL_XMLNS_XLINK+"\"");
		eadNode.append(" xmlns:xsi=\""+AL_XMLNS_XSI+"\"");
		eadNode.append(" audience=\""+AL_AUDIENCE+"\"");
		eadNode.append(" xsi:schemaLocation=\""+AL_XSI_SCHEMALOCATION+"\"");
		eadNode.append(">\n");
		return eadNode;
	}
}
