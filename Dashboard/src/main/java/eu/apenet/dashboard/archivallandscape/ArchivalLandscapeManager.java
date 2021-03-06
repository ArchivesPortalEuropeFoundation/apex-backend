package eu.apenet.dashboard.archivallandscape;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.ctc.wstx.exc.WstxLazyException;
import com.ctc.wstx.exc.WstxUnexpectedCharException;
import com.opensymphony.xwork2.Action;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.DashboardAPEnetException;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.tree.DynatreeAction;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dashboard.utils.ZipManager;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Lang;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * <p>
 * Class which supports download and upload actions
 * for Archival Landscape.
 * </p>
 * <p>
 * It's separated for the rest of the Archival Landscape 
 * Edition because these operations only support the last 
 * Archival Landscape export/import operations 
 * (download and upload), which are based on DDBB and not 
 * in any File storage.
 * </p> 
 */
public class ArchivalLandscapeManager extends DynatreeAction{
	
	private static final long serialVersionUID = 2998755137328333811L;

	private final static Logger log = Logger.getLogger(ArchivalLandscapeManager.class);
	
	private static final String AL_FILE_NAME = "AL.xml";
	
	// Error when an institution has content published.
	private static final String ERROR_CONTENT = "errorContent";
	private static final String ERROR_CONTENT_2 = "errorContent2";
	private static final String ERROR_IDENTIFIERS = "errorIdentifier";
	private static final String ERROR_COUNTRY = "errorCountry";
	private static final String ERROR_INVALID_CHARS = "errorInvalidChars";
	private static final String ERROR_SPECIAL_CHARACTERS = "errorSpecialCharacters";
	// Error when an institution has duplicated identifiers.
	private static final String ERROR_DUPLICATE_IDENTIFIERS = "errorDuplicateIdentifiers";
	private static final String ERROR_NAMES_CHANGED = "changedNames";
	// Error when the name of the institution hasn't language.
	private static final String ERROR_LANG = "errorLang";
	private static final String INVALID = "invalid";

	// Constants for type of XML ingest process.
	private static final String PROCESS_CONTINUE = "continue";
	private static final String PROCESS_KEEP = "keepIds";
	private static final String PROCESS_OVERWRITE = "overwriteIds";

	// Constants for the institutions with same name.
	private static final String ADD = "add";
	private static final String DELETE = "delete";

	private List<ArchivalInstitution> totalInstitutions;
	private Map<String,ArchivalInstitution> groupsInsertedIntoDDBB;
	private List<String>  ingestedIdentifiersClone;
	private List<ArchivalInstitution> deletedInstitutions;
	private List<ArchivalInstitution> notChangedInstitutions;
	private List<ArchivalInstitution> insertedInstitutions;
	
	private File httpFile;
	private String httpFileFileName;
	private Country country;

	private ArchivalInstitutionDAO aIDAO;
	private AiAlternativeNameDAO aIANDAO;

	private Map<String, Integer> positions;

	private Set<String> institutionsWithContent;

	private List<String> warnings_ead;

	// Variable for the name of the institution without lang.
	private String aiArchivalInstitutionName;

	private Set<String> institutionsWithContentNotPublished;

	private List<String> errors;
	
    //Set for the institutions with special characters
	private Set<String> institutionsWithSpecialCharacters;
	
	// Set for the duplicate identifiers.
	private Set<String> duplicateIdentifiers;

	private Set<String> pathsToBeDeleted;

	private boolean invalidChars = false;

	private Map<ArchivalInstitution, ArchivalInstitution> relatedInstitutionsByName; //Map which store old and new institution related by Map<old_ArchivalInstitution_key> -> new_ArchivalInstitution_value  
	private List<ArchivalInstitution> oldRelatedInstitutions; //old institutions related by name - it's used into jsp
	private List<ArchivalInstitution> newRelatedInstitutions; //new institutions related by name - it's used into jsp
	private List<ArchivalInstitution> updatedInstitutions; //institutions updated, f.i. parent has changed - it's used into jsp
	private List<ArchivalInstitution> institutionsToBeDeleted; //institutions which dissapears from file - it's used into jsp
	private List<ArchivalInstitution> newInstitutionsFile; //institutions to be inserted - it's used into jsp
	private List<ArchivalInstitution> oldSameIdentifierInstitutionsFile; //old institutions with same identifier - it's used into jsp
	private List<ArchivalInstitution> newSameIdentifierInstitutionsFile; //new institutions with same identifier - it's used into jsp
	private List<ArchivalInstitution> oldEmptyIdentifierInstitutionsFile; //old institutions with empty identifier - it's used into jsp
	private List<ArchivalInstitution> newEmptyIdentifierInstitutionsFile; //new institutions with empty identifier - it's used into jsp
	private List<ArchivalInstitution> oldDuplicateNameInstitutions; //old institutions with duplicate name - it's used into jsp
	private List<ArchivalInstitution> newDuplicateNameInstitutions; //new institutions with duplicate name - it's used into jsp
	private Map<String, List<ArchivalInstitution>> oldDuplicateNameInstitutionsMap; //old institutions with duplicate name - it's used into jsp
	private Map<String, List<ArchivalInstitution>> newDuplicateNameInstitutionsMap; //new institutions with duplicate name - it's used into jsp
	private List<String> addInstitutionsFromSelect; // List of identifiers of institutions that should be added  - it's used into jsp
	private List<String> deleteInstitutionsFromSelect; // List of identifiers of institutions that should be deleted  - it's used into jsp
	private List<String> oldInstitutionsFromSelect; // List of identifiers of institutions in database  - it's used into jsp
	private List<String> newInstitutionsFromSelect; // List of identifiers of institutions in file  - it's used into jsp

	private Set<String> nameSet; // Set of same names - it's used into jsp

	// Variable for overwrite identifiers.
	private String overwriteIDs;

	// List of elements which identifiers should be overwritten or maintained.
	private List<String> oldSameNameInstitution;
	private List<String> newSameNameInstitution;

	public List<ArchivalInstitution> getUpdatedInstitutions() {
		return this.updatedInstitutions;
	}

	public List<ArchivalInstitution> getOldRelatedInstitutions() {
		return this.oldRelatedInstitutions;
	}

	public void setOldRelatedInstitutions(List<ArchivalInstitution> oldRelatedInstitutions) {
		this.oldRelatedInstitutions = oldRelatedInstitutions;
	}

	public List<ArchivalInstitution> getNewRelatedInstitutions() {
		return this.newRelatedInstitutions;
	}

	public void setNewRelatedInstitutions(
			List<ArchivalInstitution> newRelatedInstitutions) {
		this.newRelatedInstitutions = newRelatedInstitutions;
	}
	
	public List<String> getIngestedIdentifiersClone() {
		return this.ingestedIdentifiersClone;
	}

	public List<ArchivalInstitution> getNewInstitutionsFile() {
		return this.newInstitutionsFile;
	}

	public List<ArchivalInstitution> getInstitutionsToBeDeleted() {
		return this.institutionsToBeDeleted;
	}

	public List<ArchivalInstitution> getOldSameIdentifierInstitutionsFile() {
		return this.oldSameIdentifierInstitutionsFile;
	}

	public List<ArchivalInstitution> getNewSameIdentifierInstitutionsFile() {
		return this.newSameIdentifierInstitutionsFile;
	}

	public List<ArchivalInstitution> getOldEmptyIdentifierInstitutionsFile() {
		return this.oldEmptyIdentifierInstitutionsFile;
	}

	public List<ArchivalInstitution> getNewEmptyIdentifierInstitutionsFile() {
		return this.newEmptyIdentifierInstitutionsFile;
	}
	
	/**
	 * @return the oldDuplicateNameInstitutions
	 */
	public List<ArchivalInstitution> getOldDuplicateNameInstitutions() {
		return this.oldDuplicateNameInstitutions;
	}

	/**
	 * @param oldDuplicateNameInstitutions the oldDuplicateNameInstitutions to set
	 */
	public void setOldDuplicateNameInstitutions(
			List<ArchivalInstitution> oldDuplicateNameInstitutions) {
		this.oldDuplicateNameInstitutions = oldDuplicateNameInstitutions;
	}

	/**
	 * @return the newDuplicateNameInstitutions
	 */
	public List<ArchivalInstitution> getNewDuplicateNameInstitutions() {
		return this.newDuplicateNameInstitutions;
	}

	/**
	 * @param newDuplicateNameInstitutions the newDuplicateNameInstitutions to set
	 */
	public void setNewDuplicateNameInstitutions(
			List<ArchivalInstitution> newDuplicateNameInstitutions) {
		this.newDuplicateNameInstitutions = newDuplicateNameInstitutions;
	}
	
	/**
	 * @return the oldDuplicateNameInstitutionsMap
	 */
	public Map<String, List<ArchivalInstitution>> getOldDuplicateNameInstitutionsMap() {
		return this.oldDuplicateNameInstitutionsMap;
	}

	/**
	 * @param oldDuplicateNameInstitutionsMap the oldDuplicateNameInstitutionsMap to set
	 */
	public void setOldDuplicateNameInstitutionsMap(
			Map<String, List<ArchivalInstitution>> oldDuplicateNameInstitutionsMap) {
		this.oldDuplicateNameInstitutionsMap = oldDuplicateNameInstitutionsMap;
	}

	/**
	 * @return the newDuplicateNameInstitutionsMap
	 */
	public Map<String, List<ArchivalInstitution>> getNewDuplicateNameInstitutionsMap() {
		return this.newDuplicateNameInstitutionsMap;
	}

	/**
	 * @param newDuplicateNameInstitutionsMap the newDuplicateNameInstitutionsMap to set
	 */
	public void setNewDuplicateNameInstitutionsMap(
			Map<String, List<ArchivalInstitution>> newDuplicateNameInstitutionsMap) {
		this.newDuplicateNameInstitutionsMap = newDuplicateNameInstitutionsMap;
	}

	/**
	 * @return the addInstitutionsFromSelect
	 */
	public List<String> getAddInstitutionsFromSelect() {
		return this.addInstitutionsFromSelect;
	}

	/**
	 * @param addInstitutionsFromSelect the addInstitutionsFromSelect to set
	 */
	public void setAddInstitutionsFromSelect(List<String> addInstitutionsFromSelect) {
		this.addInstitutionsFromSelect = addInstitutionsFromSelect;
	}

	/**
	 * @return the deleteInstitutionsFromSelect
	 */
	public List<String> getDeleteInstitutionsFromSelect() {
		return this.deleteInstitutionsFromSelect;
	}

	/**
	 * @param deleteInstitutionsFromSelect the deleteInstitutionsFromSelect to set
	 */
	public void setDeleteInstitutionsFromSelect(
			List<String> deleteInstitutionsFromSelect) {
		this.deleteInstitutionsFromSelect = deleteInstitutionsFromSelect;
	}

	public List<String> getOldInstitutionsFromSelect() {
		return this.oldInstitutionsFromSelect;
	}

	public void setOldInstitutionsFromSelect(List<String> oldInstitutionsFromSelect) {
		this.oldInstitutionsFromSelect = oldInstitutionsFromSelect;
	}

	public List<String> getNewInstitutionsFromSelect() {
		return this.newInstitutionsFromSelect;
	}

	public void setNewInstitutionsFromSelect(List<String> newInstitutionsFromSelect) {
		this.newInstitutionsFromSelect = newInstitutionsFromSelect;
	}

	/**
	 * @return the nameSet
	 */
	public Set<String> getNameSet() {
		return this.nameSet;
	}

	/**
	 * @param nameSet the nameSet to set
	 */
	public void setNameSet(Set<String> nameSet) {
		this.nameSet = nameSet;
	}

	public void setHttpFile(File httpFile){
		this.httpFile = httpFile;
	}
	
	public List<ArchivalInstitution> getTotalInstitutions() {
		return this.totalInstitutions;
	}

	public List<ArchivalInstitution> getDeletedInstitutions() {
		return this.deletedInstitutions;
	}

	public List<ArchivalInstitution> getNotChangedInstitutions() {
		return this.notChangedInstitutions;
	}

	public List<ArchivalInstitution> getInsertedInstitutions() {
		return this.insertedInstitutions;
	}

	public void setHttpFileFileName(String httpFileFileName) {
		this.httpFileFileName = httpFileFileName;
	}
	
	public Set<String> getInstitutionsWithContent() {
		return this.institutionsWithContent;
	}

	public void addInstitutionsWithContent(String institutionsWithContentName) {
		if (this.getInstitutionsWithContent() == null) {
			this.institutionsWithContent = new LinkedHashSet<String>();
		}
		
		this.getInstitutionsWithContent().add(institutionsWithContentName);
	}

	public void setInstitutionsWithContent(Set<String> institutionsWithContent) {
		this.institutionsWithContent = institutionsWithContent;
	}

	public String getAiArchivalInstitutionName() {
		return this.aiArchivalInstitutionName;
	}

	public void setAiArchivalInstitutionName(String aiArchivalInstitutionName) {
		this.aiArchivalInstitutionName = aiArchivalInstitutionName;
	}

	public Set<String> getInstitutionsWithContentNotPublished() {
		return this.institutionsWithContentNotPublished;
	}

	public void addInstitutionsWithContentNotPublished(String institutionsWithContentName) {
		if (this.getInstitutionsWithContentNotPublished() == null) {
			this.institutionsWithContentNotPublished = new LinkedHashSet<String>();
		}
		
		this.getInstitutionsWithContentNotPublished().add(institutionsWithContentName);
	}

	public void setInstitutionsWithContentNotPublished(
			Set<String> institutionsWithContentNotPublished) {
		this.institutionsWithContentNotPublished = institutionsWithContentNotPublished;
	}

	public Set<String> getDuplicateIdentifiers() {
		return this.duplicateIdentifiers;
	}

	public void setDuplicateIdentifiers(Set<String> duplicateIdentifiers) {
		this.duplicateIdentifiers = duplicateIdentifiers;
	}

	public boolean isInvalidChars() {
		return this.invalidChars;
	}

	public void setInvalidChars(boolean invalidChars) {
		this.invalidChars = invalidChars;
	}

	public Set<String> getInstitutionsWithSpecialCharacters() {
		return institutionsWithSpecialCharacters;
	}

	public void setInstitutionsWithSpecialCharacters(
			Set<String> institutionsWithSpecialCharacters) {
		this.institutionsWithSpecialCharacters = institutionsWithSpecialCharacters;
	}
	
	/**
	 * @return the overwriteIDs
	 */
	public String getOverwriteIDs() {
		return this.overwriteIDs;
	}

	/**
	 * @param overwriteIDs the overwriteIDs to set
	 */
	public void setOverwriteIDs(String overwriteIDs) {
		this.overwriteIDs = overwriteIDs;
	}

	/**
	 * @return the oldSameNameInstitution
	 */
	public List<String> getOldSameNameInstitution() {
		return this.oldSameNameInstitution;
	}

	/**
	 * @param oldSameNameInstitution the oldSameNameInstitution to set
	 */
	public void setOldSameNameInstitution(List<String> oldSameNameInstitution) {
		this.oldSameNameInstitution = oldSameNameInstitution;
	}

	/**
	 * @return the newSameNameInstitution
	 */
	public List<String> getNewSameNameInstitution() {
		return this.newSameNameInstitution;
	}

	/**
	 * @param newSameNameInstitution the newSameNameInstitution to set
	 */
	public void setNewSameNameInstitution(List<String> newSameNameInstitution) {
		this.newSameNameInstitution = newSameNameInstitution;
	}

	/**
	 * <p>
	 * Upload main action.
	 * </p>
	 * <p>
	 * It uses temporally AL.xml file internally to work with it.
	 * </p>
	 * @return Struts.STATE
	 * @throws {@link SAXException} -> see checkUnzipAndUpload() documentation
	 * @throws {@link APEnetException} -> see checkUnzipAndUpload() documentation
	 */
	public String upload() throws SAXException, APEnetException{
		String state = ERROR;
		String httpFileFileName = SecurityContext.get().getCountryIsoname().toUpperCase() + AL_FILE_NAME;
		File httpFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + 
				File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase(),
				httpFileFileName);
		try {
			if(this.httpFile==null && this.httpFileFileName==null){
				//use temp file
				fillMainFiles();
				if(!this.httpFile.exists()){
					this.httpFile = null;
					this.httpFileFileName = null;
				}
			}
			state = checkUnzipAndUpload();
		} catch (IOException e) {
			log.error(e);
		} finally {
			if(state!=null && this.httpFile!=null){
				try {
					if(httpFile.exists()){
						httpFile.delete(); //deletes possibles old files
					}
					if(!state.equals(ERROR)){
						FileUtils.moveFile(this.httpFile, httpFile); //copy to be used in the next oction
					}
				} catch (IOException e) {
					log.error("Error trying to remove old file: "+httpFile.getPath()+", country: "+SecurityContext.get().getCountryIsoname(),e);
				}
			}
		} 
		return state;
	}
	/**
	 * <p>
	 * Main launcher of logic.
	 * </p>
	 * <p>
	 * Checks the uploaded file and call to checkOverwrite() method 
	 * when discrimination checking file process finished.
	 * </p>
	 * <p>
	 * It unzips and call checks for ingestion process if a zip file is detected.
	 * </p>
	 * @return Struts.STATE
	 * @throws IOException -> see java.io.File and Apache {@link FileUtils} documentation for more details
	 * @throws {@link APEnetException} -> see project documentation for details (APEnetException management)
	 */
	private String checkUnzipAndUpload() throws IOException, SAXException, APEnetException {
		boolean error = false;
		if(this.httpFile!=null && this.httpFileFileName!=null){
			String path = this.httpFile.getAbsolutePath();
			String format = this.httpFileFileName.substring(this.httpFileFileName.lastIndexOf(".") + 1).toLowerCase();
			if(format.equalsIgnoreCase("xml")){
				return checkOverwrite();
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
							return checkOverwrite();
						}catch(Exception e){
							log.error("Error trying to manage AL uploaded" + APEnetUtilities.generateThrowableLog(e));
							error = true;
						}finally{
							if(pathFile.canWrite()){ //it's a temp file, so it should be removed.
								try {
									if(error){
										FileUtils.deleteDirectory(pathFile);
									}
								} catch (IOException e) {
									log.error("Error trying to delete temp AL path, directory will keep there: "+pathFile.getAbsolutePath());
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
	 * <p>
	 * Checks if there is needed an overwrite, if not continues 
	 * with the normal process (old new implementation).
	 * </p>
	 * <p>
	 * It calls to ingestArchivalLandscapeXML() method in last instance.
	 * </p>
	 * @return Struts.STATE
	 */
	private String checkOverwrite() {
		String state = INPUT;

		// Checks the country for the current file.
		if (this.httpFile == null) {
			this.fillMainFiles();
		}
		if (!this.checkCountryCode()) {
			return ERROR_COUNTRY;
		}

		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> archivalInstitutions = aiDao.getArchivalInstitutionsByCountryId(SecurityContext.get().getCountryId());
		if(archivalInstitutions==null || archivalInstitutions.size()==0){
			state = SUCCESS;
			try {
				state = ingestArchivalLandscapeXML();
			} catch (Exception e) {
				state = ERROR;
				log.error("Exception catched calling ingestArchivalLandscapeXML() method",e);
			}
		}
		return state;
	}
	/**
	 * <p>
	 * Cancel (clean) action.
	 * </p>
	 * <p>
	 * This method removes the uploaded file not ingested.
	 * His information is stored into a global attribute, so it uses that value.
	 * </p>
	 * <p>
	 * Also deletes internally .tmp files.
	 * </p>
	 * @return Structs.STATE
	 */
	public String cancelOverwrite(){
		String state = SUCCESS;
		//fill main files to be used
		fillMainFiles();
		if(this.httpFile!=null && this.httpFile.exists()){
			int clearState = 0;
			this.httpFile.delete();
			clearState = 1;
			if(clearState>0){
				this.httpFile = new File(this.httpFile.getAbsolutePath()+".tmp");
				if(this.httpFile!=null && this.httpFile.exists()){
					this.httpFile.delete();
					clearState = 2;
				}
			}
		}
		return state;
	}
	
	/**
	 * <p>
	 * Method which fills the global attributes to be used in the next steps.
	 * </p> 
	 */
	private void fillMainFiles() {
		this.httpFileFileName = SecurityContext.get().getCountryIsoname().toUpperCase() +AL_FILE_NAME;
		this.httpFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase(),
			this.httpFileFileName);
	}
	/**
	 * <p>
	 * Method which cleans the target files (temp files).
	 * </p>
	 * <p>
	 * This method try to delete all files and directory used for temp upload process.
	 * </p>
	 */
	private void cleanMainFiles(){
		if (httpFile != null){
			String tempPath = this.httpFile.getAbsolutePath() + ".tmp";
			File tempFile = new File(tempPath);

			try {
				File parentDirectory = httpFile.getParentFile();
				ContentUtils.deleteFile(httpFile, false);
				ContentUtils.deleteFile(tempFile, false);
				if (parentDirectory.list().length == 0){
					ContentUtils.deleteFile(parentDirectory, false);
				}
			}catch (IOException e){}
		}
	}

	/**
	 * <p>
	 * Action lauched to ingest xml archival-institution to ddbb.
	 * </p>
	 * <p>
	 * It call to fillMainFiles(), rightXml() (checks) and reportAction() methods.
	 * </p>
	 * @return String - state_constants[INVALID or reportAction() result]
	 * @throws {@link APEnetException} -> see project documentation for details ({@link APEnetException} management)
	 */
	public String checkReportAndIngestLogic() throws SAXException, APEnetException {
		String state = INVALID;
		//fill main files to be used
		fillMainFiles();
		if(rightXml()){
			//return ingestArchivalLandscapeXML();
			return reportAction();
		}
		return state;
	}
	
	/**
	 * <p>
	 * Launch all file validations (see validateUploadedAL() 
	 * documentation for more details).
	 * </p>
	 * <p>
	 * It validates the uploaded Archival Landscape (file) and next
	 * works with DDBB checking identifiers and if some institution
	 * has content indexed. 
	 * </p>
	 * 
	 * @return boolean -> is right or not
	 */
	private boolean rightXml(){
		boolean right = true;
		//validate xml structure
		try {
			validateUploadedAL(this.httpFile);
		} catch (SAXException e) {
			right = false;
			log.error("Error in SAX format: ",e);
		} catch (APEnetException e) {
			right = false;
			log.error(e);
		}
		if(this.warnings_ead!=null && this.warnings_ead.size()>0){
			right = false;
			Iterator<String> itWarnings = this.warnings_ead.iterator();
			while(itWarnings.hasNext()){
				addActionError(itWarnings.next());
			}
		}
		if(right){
			//validate country code
			if(this.checkCountryCode()) {
				//validate indexed institutions
				Collection<ArchivalInstitution> archivalInstitutions = getInstitutionsByALFile(this.httpFile,false);
				Collection<String> internalIdentifiers = ArchivalLandscapeUtils.checkIdentifiersForArchivalInstitutionStructure(archivalInstitutions);
				if(internalIdentifiers!=null){ //check result
					String result = checkIfSomeInstitutionIsIngestedAndHasContentIndexed(archivalInstitutions);
					boolean noContentIndexed = result!=null && result.equals(SUCCESS);
					if(!noContentIndexed){ //checks names for institutions that has content indexed, if they are not into the file -> then "no valid"
						ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
						List<ArchivalInstitution> indexedInstitutions = aiDao.getArchivalInstitutionsWithSearchableItems(SecurityContext.get().getCountryId(), null);
						right = checkIndexedInstitutions(indexedInstitutions,archivalInstitutions);
					}
				}
				right = right && internalIdentifiers!=null;
			}else{
				right = false;
			}
		}
		return right;
	}

	/**
	 * <p>
	 * Checks if the AL uploaded is for the current country (file).
	 * </p>
	 *
	 * @return boolean -> is right or not
	 */
	private boolean checkCountryCode() {
		boolean right = false;
		String countryCode = ArchivalLandscapeUtils.getXMLEadidCountrycode(this.httpFile);
		if(countryCode!=null && countryCode.equalsIgnoreCase(SecurityContext.get().getCountryIsoname())){
			right = true;
		}

		return right;
	}

	/**
	 * <p>
	 * Checks two structures, search for an indexed institutions 
	 * using the target structure.
	 * </p>
	 * <p>
	 * The first structure is plain (should be obtained by DDBB query).
	 * </p>
	 * <p>
	 * The second structure is in a tree way (should be obtained by 
	 * a file parsed to an structured no plain).
	 * </p> 
	 * @param indexedInstitutions - List<{@link ArchivalInstitution}> source
	 * @param archivalFileInstitutions - Collection<{@link ArchivalInstitution}> seeker
	 * @return Boolean - is right structure
	 */
	private boolean checkIndexedInstitutions(List<ArchivalInstitution> indexedInstitutions,Collection<ArchivalInstitution> archivalFileInstitutions) {
		boolean exit = false;
		if(this.relatedInstitutionsByName==null){ //used recursively, needs a check 
			this.relatedInstitutionsByName = new HashMap<ArchivalInstitution,ArchivalInstitution>();
		}
		Iterator<ArchivalInstitution> itIndexedInstitutions = indexedInstitutions.iterator();
		while(!exit && itIndexedInstitutions.hasNext()){ //check by each institution indexed
			ArchivalInstitution indexedInstitution = itIndexedInstitutions.next();
			boolean found = false;
			Iterator<ArchivalInstitution> archivalFileInstitutionsIterator = archivalFileInstitutions.iterator();
			String indexedName = indexedInstitution.getAiname();
			while(!found && archivalFileInstitutionsIterator.hasNext()){
				ArchivalInstitution iteratedInstitution = archivalFileInstitutionsIterator.next();
				String name = iteratedInstitution.getAiname();
				if(name.equalsIgnoreCase(indexedName)){
					this.relatedInstitutionsByName.put(indexedInstitution, iteratedInstitution);
					found = true;
				}else if(iteratedInstitution.isGroup()){ //check childrens
					found = checkChildrenIndexedInstitutionRecursive(iteratedInstitution.getChildArchivalInstitutions(),indexedInstitution);
					//found = this.relatedInstitutionsByName.containsKey(indexedInstitution); //it's replaced by returning state, TODO: check if it's valid in all cases
				}
			}
			exit = !found; //all institutions must be founded, if not exit and return state
		}
		return (!exit && (indexedInstitutions!=null && this.relatedInstitutionsByName.size()==indexedInstitutions.size())); //found all institutions indexed (yes = true, no = false)
	}
	/**
	 * <p>
	 * Check for checkIndexedInstitutions - recursive.
	 * </p>
	 * <p> 
	 * It should be called by checkIndexedInstitutions.
	 * </p>
	 * 
	 * @param childArchivalInstitutions - List<{@link ArchivalInstitution}> source
	 * @param indexedInstitution - {@link ArchivalInstitution} seeker
	 * @return boolean -> found
	 */
	private boolean checkChildrenIndexedInstitutionRecursive(List<ArchivalInstitution> childArchivalInstitutions,ArchivalInstitution indexedInstitution) {
		boolean found = false;
		if(childArchivalInstitutions!=null){
			Iterator<ArchivalInstitution> itChildren = childArchivalInstitutions.iterator();
			while(!found && itChildren.hasNext()){
				ArchivalInstitution iteratedInstitution = itChildren.next();
				String name = iteratedInstitution.getAiname();
				String indexedName = indexedInstitution.getAiname();
				if(name.equalsIgnoreCase(indexedName)){
					this.relatedInstitutionsByName.put(indexedInstitution, iteratedInstitution);
					found = true;
				}else if(iteratedInstitution.isGroup()){ //check childrens
					checkChildrenIndexedInstitutionRecursive(iteratedInstitution.getChildArchivalInstitutions(),indexedInstitution);
					found = this.relatedInstitutionsByName.containsKey(indexedInstitution);
				}
			}
		}
		return found;
	}

	/**
	 * <p>
	 * Action which checks the uploaded file and displays the error messages or
	 * the report while it's not being uploaded.
	 * </p>
	 * <p>
	 * The checks are as follow:
	 * </p>
	 * <ol>
	 *     <li>Checks if the file is a valid XML file.</li>
	 *     <li>Checks if the file contains duplicate identifiers.</li>
	 *     <li>Checks if the file contains changes in the institutions' names.</li>
	 * </ol>
	 * @return Struts.STATE
	 */
	public String reportAction(){
		if(this.httpFile==null){
			fillMainFiles();
		}
		Collection<ArchivalInstitution> archivalInstitutions = getInstitutionsByALFile(this.httpFile,false);
		if(archivalInstitutions!=null){
			Boolean specialCharacters = ArchivalLandscapeUtils.checkSpecialCharacter(archivalInstitutions);
			Boolean duplicateIds = ArchivalLandscapeUtils.checkIdentifiers(this.httpFile);
			if (specialCharacters != null && specialCharacters){
				this.setInstitutionsWithSpecialCharacters(ArchivalLandscapeUtils.getInstitutionsWithSpecialCharacters());
				return ERROR_SPECIAL_CHARACTERS;
			}else if (duplicateIds != null && !duplicateIds) {
				this.setDuplicateIdentifiers(ArchivalLandscapeUtils.getDuplicateIdentifiers());
				return ERROR_DUPLICATE_IDENTIFIERS;
			} else if (!institutionNamesHaveChanged(archivalInstitutions)) {
				return displayReport(archivalInstitutions);
			} else {
				return ERROR_NAMES_CHANGED;
			}
		}else if (this.isInvalidChars()) {
			return ERROR_INVALID_CHARS;
		}
		return ERROR;
	}

	/**
	 * <p>
	 * Compare, if possible identify an institution.
	 * </p>
	 * <p> 
	 * When method is able to detect that exits two institutions with the same identifier, it 
	 * compares the name, and if some institution detected has different name is rejected.
	 * </p>
	 * 
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @return state (rejected or not)
	 */
	private boolean institutionNamesHaveChanged(Collection<ArchivalInstitution> archivalInstitutions) {
		ArchivalInstitutionDAO dao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> ingestedInstitutions = dao.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),false);
		archivalInstitutions = ArchivalLandscapeUtils.parseCollectionToPlainList(archivalInstitutions);
		Iterator<ArchivalInstitution> itIngestedInstitutions = ingestedInstitutions.iterator();
		boolean state = true; //manage all are right
		while(state && itIngestedInstitutions.hasNext()){
			ArchivalInstitution targetInstitution = itIngestedInstitutions.next();
			Iterator<ArchivalInstitution> itArchivalStructure = archivalInstitutions.iterator();
			boolean found = false;
			while(!found && itArchivalStructure.hasNext()){
				ArchivalInstitution archivalInstitutionsComparable = itArchivalStructure.next();
				if(archivalInstitutionsComparable.getInternalAlId().equals(targetInstitution.getInternalAlId()) && !archivalInstitutionsComparable.isGroup()){
					found = true;
					if(archivalInstitutionsComparable.getAiname() != null
							&& !archivalInstitutionsComparable.getAiname().equals(targetInstitution.getAiname())){
						state = false;
					}
				}
			}
		}
		return !state;
	}

	/**
	 * <p>
	 * Action which displays three list, inserts, updates and deleted.
	 * </p>
	 * <p>
	 * Shows a report to user with the following information.
	 * </p>
	 * <p> 
	 * To get this information it runs steps:
	 * </p>
	 * <ol>
	 * 	<li>Extract all institutions identifiers</li>
	 * 	<li>Uses identifiers to discriminate which institutions are new, which are removed and which are updated.</li> 
	 * 	<li>Also uses this identifiers to differences between institutions which have the same identifier 
	 * 	   and institutions which have empty identifier (next steps).</li>
	 * 	<li>
	 * 	 <ul>
	 * 	  <li>Manage information for different actions:</li>
	 * 	  <li>Show ->  Same identifiers, Empty identifiers.</li>
	 * 	  <li>Execute -> Institution updates and deletes.</li>
	 *   </ul>
	 *  </li>
	 *  <li> Finally discriminates institutions which have the same name. </li>
	 * </ol>
	 * <p>
	 * This method doesn't change any internal institution in this step.
	 * </p>
	 * 
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @return String <=> ({@link Action}.INPUT)
	 */
	private String displayReport(Collection<ArchivalInstitution> archivalInstitutions){
		//all ingested identifiers, to be used for checks if there are deletes
		ArchivalInstitutionDAO dao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<String> ingestedIdentifiers = dao.getArchivalInstitutionInternalIdentifiersByCountryId(SecurityContext.get().getCountryId());
		List<ArchivalInstitution> ingestedInstitutions = dao.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),false);
		//new inserts (by institutions identifiers)
		boolean newIdentifiers = institutionHasNewIdentifiers(archivalInstitutions,ingestedIdentifiers);
		List<String> fileIdentifiers = ArchivalLandscapeUtils.getIdentifiersFromArchivalInstitutionStructure(archivalInstitutions);
		List<ArchivalInstitution> institutionsOutOfFileByIdentifier = null;
		if(newIdentifiers){
			institutionsOutOfFileByIdentifier = dao.getArchivalInstitutionsByCountryIdUnless(SecurityContext.get().getCountryId(), fileIdentifiers, false);
			//TODO tries to match up institutionsOutOfFile by name
			this.relatedInstitutionsByName = null;
			//next use institutions
			checkIndexedInstitutions(institutionsOutOfFileByIdentifier,archivalInstitutions);
//			this.apparentlyNewInstitutions = ArchivalLandscapeUtils.getExcludedInstitutions(archivalInstitutions,ingestedIdentifiers);
		}
		this.newInstitutionsFile = ArchivalLandscapeUtils.parseCollectionToPlainList(archivalInstitutions); //parse structure to a plain list to be displayed into jsp by an iterator
		//clean institutions from file 
		//this.fileIdentifiersClone.removeAll(ingestedInstitutions);
		Iterator<ArchivalInstitution> itFiles = this.newInstitutionsFile.iterator();
		List<ArchivalInstitution> tempListIdentifiersClone = new ArrayList<ArchivalInstitution>(this.newInstitutionsFile);
		while(itFiles.hasNext()){
			ArchivalInstitution institutionFile = itFiles.next();
			boolean found = false;
			Iterator<ArchivalInstitution> itIngestedInstitutions = ingestedInstitutions.iterator();
			while(!found && itIngestedInstitutions.hasNext()){
				ArchivalInstitution ingestedInstitution = itIngestedInstitutions.next();
				if(ingestedInstitution.getInternalAlId().equals(institutionFile.getInternalAlId())){
					tempListIdentifiersClone.remove(institutionFile);
					found = true;
				}
			}
			if(this.oldRelatedInstitutions!=null){
				Iterator<ArchivalInstitution> itOldRelatedInstitutions = this.oldRelatedInstitutions.iterator();
				//only get into if !found
				while(!found && itOldRelatedInstitutions.hasNext()){
					ArchivalInstitution relatedByNameInstitution = itOldRelatedInstitutions.next();
					if(relatedByNameInstitution.getInternalAlId().equals(institutionFile.getInternalAlId())){
						tempListIdentifiersClone.remove(institutionFile);
						found = true;
					}
				}
			}
		}

		// Same identifiers.
		this.oldSameIdentifierInstitutionsFile = new ArrayList<ArchivalInstitution>();
		if (archivalInstitutions != null) {
			Map<String,List<ArchivalInstitution>> institutionsSameIDMap = ArchivalLandscapeUtils.getInstitutionsWithSameIdentifierFromArchivalInstitutionStructure(archivalInstitutions);
			if (institutionsSameIDMap != null && !institutionsSameIDMap.isEmpty()) {
				Iterator<String> keysIt = institutionsSameIDMap.keySet().iterator();
				while (keysIt.hasNext()) {
					String key = keysIt.next();
					if(key!=null && !key.isEmpty()){
						this.oldSameIdentifierInstitutionsFile.addAll(institutionsSameIDMap.get(key));
					}
				}
			}
			// Assign the new list the values of the old list.
			this.newSameIdentifierInstitutionsFile = new ArrayList<ArchivalInstitution>(this.oldSameIdentifierInstitutionsFile);
			// Remove institutions from the new institutions list.
			this.newInstitutionsFile.removeAll(this.oldSameIdentifierInstitutionsFile);
			tempListIdentifiersClone.removeAll(this.oldSameIdentifierInstitutionsFile);
		}

		// Empty identifiers.
		this.oldEmptyIdentifierInstitutionsFile = new ArrayList<ArchivalInstitution>();
		if (archivalInstitutions != null) {
			this.oldEmptyIdentifierInstitutionsFile = (List<ArchivalInstitution>) ArchivalLandscapeUtils.getInstitutionsWithEmptyIdFromArchivalInstitutionStructure(archivalInstitutions);

			// Assign the new list the values of the old list.
			this.newEmptyIdentifierInstitutionsFile = new ArrayList<ArchivalInstitution>(this.oldEmptyIdentifierInstitutionsFile);

			// Remove institutions from the new institutions list.
			this.newInstitutionsFile.removeAll(this.oldEmptyIdentifierInstitutionsFile);
			tempListIdentifiersClone.removeAll(this.oldEmptyIdentifierInstitutionsFile);
		}

		//updates
		this.updatedInstitutions = new ArrayList<ArchivalInstitution>();
		if(ingestedInstitutions!=null){
			Iterator<ArchivalInstitution> ingestedInstitutionIt = ingestedInstitutions.iterator();
			while(ingestedInstitutionIt.hasNext()){
				ArchivalInstitution ingestedInstitution = ingestedInstitutionIt.next();
				ArchivalInstitution archivalInstitution = ArchivalLandscapeUtils.getInstitutionFromArchivalInstitutionStructure(ingestedInstitution,archivalInstitutions);
				if(archivalInstitution!=null){ //if null there is not updated possible
					//check parents, if they are the same
					if(!ArchivalLandscapeUtils.checkIfTwoInstitutionsHasTheSameParents(ingestedInstitution,archivalInstitution)){
						this.updatedInstitutions.add(archivalInstitution);
					}
				}
			}
		}
		List<String> oldIdentifiers = new ArrayList<String>();
		List<String> newListIdentifiers = new ArrayList<String>();
		List<ArchivalInstitution> newListInstitutions = new ArrayList<ArchivalInstitution>();
		List<String> updateIdentifiers = new ArrayList<String>();
		this.oldRelatedInstitutions = new LinkedList<ArchivalInstitution>();
		if(this.relatedInstitutionsByName!=null && this.relatedInstitutionsByName.size()>0){
			this.newRelatedInstitutions = new LinkedList<ArchivalInstitution>();
			Iterator<ArchivalInstitution> keysIt = this.relatedInstitutionsByName.keySet().iterator();
			while(keysIt.hasNext()){
				ArchivalInstitution keyInstitution = keysIt.next();
				// Check if the current Archival institutions has the same name that one in the
				// same identifiers or empty identifiers lists.
				boolean add = true;
				if (this.oldSameIdentifierInstitutionsFile != null && !this.oldSameIdentifierInstitutionsFile.isEmpty()) {
					Iterator<ArchivalInstitution> keysSameIdIt = new ArrayList<ArchivalInstitution>(this.oldSameIdentifierInstitutionsFile).iterator();
					while (add && keysSameIdIt.hasNext()) {
						ArchivalInstitution sameIdInstitution = keysSameIdIt.next();
						if (sameIdInstitution.getAiname().equals(keyInstitution.getAiname())) {
							tempListIdentifiersClone.remove(sameIdInstitution);
							add = false;
						}
					}
				}
				if (add && this.oldEmptyIdentifierInstitutionsFile != null && !this.oldEmptyIdentifierInstitutionsFile.isEmpty()) {
					Iterator<ArchivalInstitution> keysEmptyIdIt = new ArrayList<ArchivalInstitution>(this.oldEmptyIdentifierInstitutionsFile).iterator();
					while (add && keysEmptyIdIt.hasNext()) {
						ArchivalInstitution emptyIdInstitution = keysEmptyIdIt.next();
						if (emptyIdInstitution.getAiname().equals(keyInstitution.getAiname())) {
							tempListIdentifiersClone.remove(emptyIdInstitution);
							add = false;
						}
					}
				}

				if (add) {
					this.oldRelatedInstitutions.add(keyInstitution);
					//update list
					String index = keyInstitution.getInternalAlId();
					updateIdentifiers.add(index);
					//end update list
					oldIdentifiers.add(keyInstitution.getInternalAlId());
					ArchivalInstitution valueInstitution = this.relatedInstitutionsByName.get(keyInstitution);
					newListIdentifiers.add(valueInstitution.getInternalAlId());
					newListInstitutions.add(valueInstitution);
					this.newRelatedInstitutions.add(valueInstitution);
					//updates insert list, removing related institution which contains inserts and updates with same names
					Iterator<ArchivalInstitution> itList = tempListIdentifiersClone.iterator();
					boolean found = false;
					while(!found && itList.hasNext()){
						ArchivalInstitution tempInsti = itList.next();
						if(tempInsti.getInternalAlId().equals(valueInstitution.getInternalAlId())){
							found = true;
							tempListIdentifiersClone.remove(tempInsti);
						}
					}
				}
			}
			this.newInstitutionsFile.removeAll(newListIdentifiers);
		}

		this.newInstitutionsFile = tempListIdentifiersClone; //insert list
		//deletes
		this.institutionsToBeDeleted = new ArrayList<ArchivalInstitution>();
		List<ArchivalInstitution> institutionsDetected = new ArrayList<ArchivalInstitution>();
		Iterator<ArchivalInstitution> itIngestedInstitutions = ingestedInstitutions.iterator();
		while(itIngestedInstitutions.hasNext()){
			ArchivalInstitution tempInstitution = itIngestedInstitutions.next();
			Iterator<ArchivalInstitution> itFileArchivalInstitutions = archivalInstitutions.iterator();
			boolean found = false;
			if(fileIdentifiers.contains(tempInstitution.getInternalAlId()) || 
					tempInstitution.getInternalAlId()==null ||
					tempInstitution.getInternalAlId().trim().isEmpty()){
				institutionsDetected.add(tempInstitution);
			}else{
				while(!found && itFileArchivalInstitutions.hasNext()){
					ArchivalInstitution currentInstitution = itFileArchivalInstitutions.next();
					String currentIdentifier = currentInstitution.getInternalAlId();
					if(tempInstitution.getInternalAlId().equals(currentIdentifier)){
						institutionsDetected.add(currentInstitution);
						found = true;
					}
				}
			}
		}
		itIngestedInstitutions = ingestedInstitutions.iterator();
		List<ArchivalInstitution> cloneIngestedInstitutions = new ArrayList<ArchivalInstitution>(ingestedInstitutions);
		while(itIngestedInstitutions.hasNext()){
			ArchivalInstitution ingestedInstitution = itIngestedInstitutions.next();
			boolean found = false;
			Iterator<ArchivalInstitution> targetInstitutionsIt = institutionsDetected.iterator();
			while(!found && targetInstitutionsIt.hasNext()){
				ArchivalInstitution targetInstitution = targetInstitutionsIt.next();
				String targetInternalIdentifier = targetInstitution.getInternalAlId();
				if(ingestedInstitution.getInternalAlId().equals(targetInternalIdentifier)){
					cloneIngestedInstitutions.remove(ingestedInstitution);
					found = true;
				}
			}
		}

		// Check if the temp list contains an institution with same name that one in the
		// same identifiers or empty identifiers lists.
		if (cloneIngestedInstitutions != null) {
			Iterator<ArchivalInstitution> keyInstitutionIt = new ArrayList<ArchivalInstitution>(cloneIngestedInstitutions).iterator();
			while (keyInstitutionIt.hasNext()) {
				ArchivalInstitution keyInstitution = keyInstitutionIt.next();
				boolean found = false;
				Iterator<ArchivalInstitution> keysSameIdIt = new ArrayList<ArchivalInstitution>(this.oldSameIdentifierInstitutionsFile).iterator();
				while (!found && keysSameIdIt.hasNext()) {
					ArchivalInstitution sameIdInstitution = keysSameIdIt.next();
					if (sameIdInstitution.getAiname().equals(keyInstitution.getAiname())) {
						cloneIngestedInstitutions.remove(keyInstitution);
						found = true;
					}
				}
				if (!found) {
					Iterator<ArchivalInstitution> keysEmptyIdIt = new ArrayList<ArchivalInstitution>(this.oldEmptyIdentifierInstitutionsFile).iterator();
					while (!found && keysEmptyIdIt.hasNext()) {
						ArchivalInstitution emptyIdInstitution = keysEmptyIdIt.next();
						if (emptyIdInstitution.getAiname().equals(keyInstitution.getAiname())) {
							cloneIngestedInstitutions.remove(keyInstitution);
							found = true;
						}
					}
					if(!found){
						Iterator<ArchivalInstitution> keysNewInstitutionIt = new ArrayList<ArchivalInstitution>(this.newInstitutionsFile).iterator();
						while (!found && keysNewInstitutionIt.hasNext()) {
							ArchivalInstitution newInstitution = keysNewInstitutionIt.next();
							if (newInstitution.getAiname().equals(keyInstitution.getAiname())) { 
								//it suppose that are duplicated into delete and insert, so they are related by name
								//they must be removed from both list and inserted into related institutions
								cloneIngestedInstitutions.remove(keyInstitution);
								this.newInstitutionsFile.remove(newInstitution);
								this.oldRelatedInstitutions.add(keyInstitution);
								if(this.newRelatedInstitutions==null){
									this.newRelatedInstitutions = new ArrayList<ArchivalInstitution>();
								}
								this.newRelatedInstitutions.add(newInstitution);
								found = true;
							}
						}
					}
				}
			}
		}
		this.institutionsToBeDeleted = cloneIngestedInstitutions;
		this.institutionsToBeDeleted.removeAll(this.oldRelatedInstitutions);

		// Checks if exists more than one institution/group with same name.
		this.checkMultipleInstitutionsSameName(archivalInstitutions);

		// Add the rest of institutions names to the set of names.
		if (this.getNameSet() == null) {
			this.setNameSet(new HashSet<String>());
		}

		if (this.getOldRelatedInstitutions() != null && !this.getOldRelatedInstitutions().isEmpty()) {
			for (int i = 0; i < this.getOldRelatedInstitutions().size(); i++) {
				this.getNameSet().add(this.getOldRelatedInstitutions().get(i).getAiname());
			}
		}

		return INPUT;
	}

	/**
	 * <p>
	 * Method to check if the lists contains more than one institution or group
	 * with same name.
	 * </p>
	 * <p>
	 * Returns true if some change was detected.
	 * </p> 
	 * 
	 * @return boolean -> result / something has been detected
	 */
	private boolean checkMultipleInstitutionsSameName(Collection<ArchivalInstitution> archivalInstitutions) {
		boolean result = false;
		List<ArchivalInstitution> oldSameNameInstitutions = new ArrayList<ArchivalInstitution>();
		List<ArchivalInstitution> newSameNameInstitutions = new ArrayList<ArchivalInstitution>();
		// Set of repeated names.
		if (this.getNameSet() == null) {
			this.setNameSet(new HashSet<String>());
		}

		// Checks the institutions with same name in the list of database institutions.
		Map<String, List<ArchivalInstitution>> oldRelatedSameNameInstitutions = null;
		if (this.getOldRelatedInstitutions() != null) {
			Collection<ArchivalInstitution> oldArchivalInstitutions = this.getOldRelatedInstitutions();
			oldRelatedSameNameInstitutions = ArchivalLandscapeUtils.getInstitutionsWithSameNameFromArchivalInstitutionStructure(oldArchivalInstitutions);
		}

		if (oldRelatedSameNameInstitutions != null && !oldRelatedSameNameInstitutions.isEmpty()) {
			Iterator<String> keysIt = oldRelatedSameNameInstitutions.keySet().iterator();
			while (keysIt.hasNext()) {
				String key = keysIt.next();
				if (key != null && !key.isEmpty()) {
					oldSameNameInstitutions.addAll(oldRelatedSameNameInstitutions.get(key));
					this.getNameSet().add(key);
					result = true;
				}
			}
		}
		this.setOldDuplicateNameInstitutions(oldSameNameInstitutions);

		// Checks the institutions with same name in the list of file institutions.
		Map<String, List<ArchivalInstitution>> newRelatedSameNameInstitutions = null;
		if (this.getNewRelatedInstitutions() != null) {
			Collection<ArchivalInstitution> newArchivalInstitutions = this.getNewRelatedInstitutions();
			newRelatedSameNameInstitutions = ArchivalLandscapeUtils.getInstitutionsWithSameNameFromArchivalInstitutionStructure(newArchivalInstitutions);
		}

		if (newRelatedSameNameInstitutions != null && !newRelatedSameNameInstitutions.isEmpty()) {
			Iterator<String> keysIt = newRelatedSameNameInstitutions.keySet().iterator();
			while (keysIt.hasNext()) {
				String key = keysIt.next();
				if (key != null && !key.isEmpty()) {
					newSameNameInstitutions.addAll(newRelatedSameNameInstitutions.get(key));
					this.getNameSet().add(key);
					result = true;
				}
			}
		}
		this.setNewDuplicateNameInstitutions(newSameNameInstitutions);

		// Remove the institutions with same name from the lists.
		// List of institutions in database.
		this.setOldRelatedInstitutions(removeSameNameInstitutionsFromList(this.getOldRelatedInstitutions()));

		// List of institutions in file.
		this.setNewRelatedInstitutions(removeSameNameInstitutionsFromList(this.getNewRelatedInstitutions()));

		// Recover the maps of institutions with same name.
		if (this.aIDAO == null) {
			this.aIDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		}

		Map<String, List<ArchivalInstitution>> oldArchivesMap = new HashMap<String, List<ArchivalInstitution>>();
		Map<String, List<ArchivalInstitution>> newArchivesMap = new HashMap<String, List<ArchivalInstitution>>();
		
		Iterator<String> namesIt = this.getNameSet().iterator();
		while (namesIt.hasNext()) {
			String name = namesIt.next();
		
			oldArchivesMap.put(name, this.aIDAO.getArchivalInstitutionsByAiNameForCountryId(name, SecurityContext.get().getCountryId()));
			newArchivesMap.put(name, ArchivalLandscapeUtils.getInstitutionsByNameFromStructure(name, archivalInstitutions));
		}

		// Set the global maps.
		this.setOldDuplicateNameInstitutionsMap(oldArchivesMap);
		this.setNewDuplicateNameInstitutionsMap(newArchivesMap);

		// Check the lists in the maps.
		if ((this.getOldDuplicateNameInstitutionsMap()!= null && this.getOldDuplicateNameInstitutionsMap().size() > 0)
				|| (this.getNewDuplicateNameInstitutionsMap()!= null && this.getNewDuplicateNameInstitutionsMap().size() > 0)) {
			this.checkSizeOfListsInMap();
		}

		return result;
	}

	/**
	 * <p>
	 * Method to clean the archives with same name in the list passed.  
	 * </p>
	 * @param archives - List<{@link ArchivalInstitution}> sources
	 * @return List<{@link ArchivalInstitution}> -> Archives without repeated names.
	 */
	private List<ArchivalInstitution> removeSameNameInstitutionsFromList(List<ArchivalInstitution> archives) {
		if (this.getNameSet() != null && !this.getNameSet().isEmpty()
				&& archives != null && !archives.isEmpty()) {
			Iterator<String> nameIt = this.getNameSet().iterator();
			while (nameIt.hasNext()) {
				String name = nameIt.next();
				List<ArchivalInstitution> temp = new ArrayList<ArchivalInstitution>();

				for (int i = 0; i < archives.size(); i++) {
					ArchivalInstitution archivalInstitution = archives.get(i);
					if (archivalInstitution.getAiname().equals(name)) {
						temp.add(archivalInstitution);
					}
				}

				archives.removeAll(temp);
			}
		}

		return archives;
	}

	/**
	 * <p>
	 * Method to checks the sizes of the lists, in the maps of institutions
	 * with same name and add items if needed.
	 * </p>
	 * <p>
	 * The items added will be:
	 * </p>
	 * <ol>
	 *    <li>If the size of the list from database is less than the size from
	 *        the file, the value will be "Add".</li>
	 *    <li>If the size of the list from database is greater than the size
	 *        from the file, the value will be "Delete".</li>
	 * </ol>
	 * @return boolean -> false
	 */
	private boolean checkSizeOfListsInMap() {
		boolean result = false;

		// Recover the keySet.
		Set<String> keySet = this.getOldDuplicateNameInstitutionsMap().keySet();
		Iterator<String> keysIt = keySet.iterator();
		while (keysIt.hasNext()) {
			String key = keysIt.next();

			// Recover the lists.
			List<ArchivalInstitution> oldInstitutions = this.getOldDuplicateNameInstitutionsMap().get(key);
			List<ArchivalInstitution> newInstitutions = this.getNewDuplicateNameInstitutionsMap().get(key);

			int oldSize = oldInstitutions.size();
			int newSize = newInstitutions.size();

			if (oldSize > newSize) {
				// Add values to the new list in order to delete the old ones.
				for (int i = 0; i < (oldSize - newSize); i++) {
					ArchivalInstitution archivalInstitution = new ArchivalInstitution();
					archivalInstitution.setAiname(ArchivalLandscapeManager.DELETE);
					newInstitutions.add(archivalInstitution);
				}

				// Add the new values to the map.
				this.getNewDuplicateNameInstitutionsMap().put(key, newInstitutions);
			} else if (oldSize < newSize) {
				// Add values to the old list in order to add the new ones.
				for (int i = 0; i < (newSize - oldSize); i++) {
					ArchivalInstitution archivalInstitution = new ArchivalInstitution();
					archivalInstitution.setAiname(ArchivalLandscapeManager.ADD);
					oldInstitutions.add(archivalInstitution);
				}

				// Add the new values to the map.
				this.getOldDuplicateNameInstitutionsMap().put(key, oldInstitutions);
			}
		}

		return result;
	}

	/**
	 * <p>
	 * Search xml file from File (folders) structure and
	 * fill global this.httpFile.
	 * </p>
	 * 
	 * @param targetFile - File source
	 * @return boolean -> has been found or not
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

	/**
	 * <p>
	 * Method to discriminate between the different types of ingest the XML.
	 * </p>
	 * <ol>
	 *     <li>
	 *        "Continue", the XML file doesn't contains changes or only contains
	 *        changes related to a reorder of the institutions inside the same
	 *        group.
	 *     </li>
	 *     <li>
	 *       "Keep", some of the internal identifiers in the database and in
	 *        the file aren't the same, so the internal identifiers in database
	 *        will be maintained.
	 *     </li>
	 *     <li>
	 *        "Overwrite", some of the internal identifiers in the database and
	 *        in the file aren't the same, so the internal identifiers in
	 *        database will be overwritten with the identifiers in the file.
	 *     </li>
	 * </ol>
	 * @return Structs.STATE
	 * @throws {@link SAXException} -> see ingestArchivalLandscapeXML for details
	 * @throws {@link APEnetException} -> see project documentation for details ({@link APEnetException} management)
	 */
	public String checkIngestionMode() throws SAXException, APEnetException {
		String state = ERROR;

		if (this.getOverwriteIDs() != null) {
			// Check if the process to perform.
			if (this.getOverwriteIDs().equalsIgnoreCase(ArchivalLandscapeManager.PROCESS_CONTINUE)) {
				// 1. "Continue", no changes or only reorder inside same group.
				state = this.ingestArchivalLandscapeXML();
			} else if (this.getOverwriteIDs().equalsIgnoreCase(ArchivalLandscapeManager.PROCESS_KEEP)) {
				// 2. "Keep", maintain the identifiers in database.
				// First of all, add the elements to the correct lists if needed.
				this.parseUserSelections();

				ArchivalLandscapeXMLEditor archivalLandscapeXMLEditor = new ArchivalLandscapeXMLEditor(this.getOldSameNameInstitution(), this.getNewSameNameInstitution());
				state = archivalLandscapeXMLEditor.changeXMLIdentifier();
				if (state == Action.SUCCESS) {
					state = this.ingestArchivalLandscapeXML();
				}
			} else if (this.getOverwriteIDs().equalsIgnoreCase(ArchivalLandscapeManager.PROCESS_OVERWRITE)) {
				// 3. "Overwrite", replace the identifiers in database with the ones in file.
				// First of all, add the elements to the correct lists if needed.
				this.parseUserSelections();

				// Second, gets all the current ingested institutions into DDBB.
				this.aIDAO = DAOFactory.instance().getArchivalInstitutionDAO();
				this.totalInstitutions = this.aIDAO.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),false);
				ArchivalLandscapeDatabaseEditor archivalLandscapeDatabaseEditor = new ArchivalLandscapeDatabaseEditor(this.getOldSameNameInstitution(), this.getNewSameNameInstitution());
				state = archivalLandscapeDatabaseEditor.changeDatabaseIdentifier(this.totalInstitutions);

				if (state == Action.SUCCESS) {
					state = this.ingestArchivalLandscapeXML();
				}
			} 
		}

		return state;
	}

	/**
	 * <p>
	 * Main function called to ingest all content.
	 * </p>
	 * <p>
	 * This function is explited in two parts:
	 * </p>
	 * <ol>
	 * 	<li>
	 *     Read target file and returns his archival-institutions structure.
	 *  </li>
	 * 	<li>
	 *      Checks if previews structure is possible to insert/update into 
	 * 		current system and make ingestion/update logic.
	 *  </li>
	 * </ol>
	 * @return Structs.STATE
	 * @throws {@link APEnetException} -> see project documentation for details (APEnetException management)
	 * @throws {@link SAXException} -> see validateUploadedAL() for details
	 */
	private String ingestArchivalLandscapeXML() throws SAXException, APEnetException {
		String state = ERROR;
		try {
		
			if (this.httpFile == null) {
				this.fillMainFiles();
			}
			Boolean firstState = ArchivalLandscapeUtils.checkIdentifiers(this.httpFile);
			
			if (firstState==null){
				validateUploadedAL(this.httpFile);
				state=ERROR_IDENTIFIERS;
				Iterator<String> it = this.warnings_ead.iterator();
				while(it.hasNext()){
					addActionMessage(it.next());
				}
			} else if (!firstState) {
				this.setDuplicateIdentifiers(ArchivalLandscapeUtils.getDuplicateIdentifiers());
				return ERROR_DUPLICATE_IDENTIFIERS;
			} else{
				String countryCode = ArchivalLandscapeUtils.getXMLEadidCountrycode(this.httpFile);
				if(countryCode!=null && countryCode.equalsIgnoreCase(SecurityContext.get().getCountryIsoname())){
	//				if(this.country==null){
						this.country = DAOFactory.instance().getCountryDAO().getCountryByCname(SecurityContext.get().getCountryName());
	//				}
					Set<ArchivalInstitution> archivalInstitutions = getInstitutionsByALFile(this.httpFile,true);
					if(archivalInstitutions!=null){
						try{
							state = checkAndUpdateFromToDDBB(archivalInstitutions,true);
						}catch(Exception e){
							log.error("Exception checking institutions with ddbb to be replaced",e);
						}
					} else if (this.isInvalidChars()) {
						state = ERROR_INVALID_CHARS;
					} else {
						state = ERROR_LANG;
					}
				}else{
					state = ERROR_COUNTRY ;
				}
			}
		 
		}finally {
			cleanMainFiles();
		}
		return state;
	}

	/**
	 * <p>
	 * Method which validates an xml with DPT. See DPT documentation for more details.
	 * {@link DocumentValidation}.xmlValidation
	 * </p>
	 * <p>
	 * If there are some kind of validation errors a global list is filled with messages in HTML format.
	 * </p>
	 * 
	 * @param file - File source
	 * @throws {@link SAXException} -> see project documentation for details ({@link APEnetException} management)
	 * @throws {@link APEnetException} -> see project documentation for details ({@link APEnetException} management)
	 */
	private void validateUploadedAL(File file) throws SAXException, APEnetException {
		warnings_ead = new ArrayList<String>();
	//	boolean state = true;
		Xsd_enum schema = Xsd_enum.XSD_EAD_SCHEMA;
		try {
			InputStream in = new FileInputStream(file);
			List<SAXParseException> exceptions = DocumentValidation.xmlValidation(in, schema);
			if (exceptions != null) {
				StringBuilder warn;
				//state = false;
				for (SAXParseException exception : exceptions) {
					warn = new StringBuilder();
					warn.append("l.").append(exception.getLineNumber()).append(" c.")
							.append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
							.append("<br/>");
					warnings_ead.add(warn.toString());
				}
			}
		} catch (SAXException e) {
			throw e;
		} catch (Exception e) {
			throw new APEnetException("Exception while validating an EAD file", e);
		}
	//	return state;
	}

	/**
	 * <p>
	 * Method to add the selections of the user in the details view to the
	 * correct lists.
	 * </p>
	 */
	private void parseUserSelections() {

		// Check the list of old institutions in database.
		if (this.getOldInstitutionsFromSelect() != null
				&& !this.getOldInstitutionsFromSelect().isEmpty()) {
			for (int i = 0; i < this.getOldInstitutionsFromSelect().size(); i++) {
				String interalId = this.getOldInstitutionsFromSelect().get(i);
				this.getOldSameNameInstitution().add(interalId);
			}
		}

		// Check the list of new institutions in file.
		if (this.getNewInstitutionsFromSelect() != null
				&& !this.getNewInstitutionsFromSelect().isEmpty()) {
			for (int i = 0; i < this.getNewInstitutionsFromSelect().size(); i++) {
				String interalId = this.getNewInstitutionsFromSelect().get(i);
				this.getNewSameNameInstitution().add(interalId);
			}
		}
	}
	/**
	 * <p>
	 * Method which checks if an Collection<{@link ArchivalInstitution}> has new identifiers.
	 * </p>
	 * <p>
	 * This method is called recursively if child levels are found
	 * </p>
	 * <p>
	 * Return if new identifiers are found.
	 * </p>
	 * 
	 * @param newArchivalInstitutionStructure - Collection<{@link ArchivalInstitution}> source
	 * @param ingestedIdentifiers - List<String> second source, discrimination list
	 * @return boolean - returns state of institution with has new identifiers
	 */
	private boolean institutionHasNewIdentifiers(Collection<ArchivalInstitution> newArchivalInstitutionStructure,List<String> ingestedIdentifiers) {
		boolean state = false;
		if(newArchivalInstitutionStructure!=null){
			Iterator<ArchivalInstitution> it = newArchivalInstitutionStructure.iterator();
			while(!state && it.hasNext()){
				ArchivalInstitution tempAI = it.next();
				String tempInternalIdentifier = tempAI.getInternalAlId();
				if(!ingestedIdentifiers.contains(tempInternalIdentifier)){
					state = true;
				}else{
					if(tempAI.isGroup()){
						List<ArchivalInstitution> children = tempAI.getChildArchivalInstitutions();
						if(children!=null && children.size()>0){
							state = institutionHasNewIdentifiers(children,ingestedIdentifiers);
						}
					}
				}
			}
		}
		return state;
	}
	
	/**
	 * <p>
	 * Checks and work with an archival_institution structure.
	 * </p>
	 * <p>
	 * Tries to insert, update and delete institutions (DDBB operations).
	 * </p>
	 * 
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> sources used
	 * @return validOperation - String Struts.state / Internals STATE_IDENTIFIERS
	 * @throws IOException - see rollbackDeletedPaths() documentation for more details
	 */
	public String checkAndUpdateFromToDDBB(Collection<ArchivalInstitution> archivalInstitutions,boolean useddbb) throws IOException {
		String validOperation = SUCCESS; //flag used to rollback the process when some rule is wrong
		Integer state = 0;
		if(archivalInstitutions!=null){
			try{
				Collection<String> internalIdentifiers = ArchivalLandscapeUtils.checkIdentifiersForArchivalInstitutionStructure(archivalInstitutions);
				boolean isValid = internalIdentifiers!=null;
				if(!isValid && useddbb){
					log.debug("Some error has been detected related to checkIdentifiers for the given structure");
					validOperation = ERROR_IDENTIFIERS;
				}else{
					state = 1;
					this.aIDAO = DAOFactory.instance().getArchivalInstitutionDAO();
					this.aIANDAO = DAOFactory.instance().getAiAlternativeNameDAO();
					//first gets all the current ingested institutions into DDBB
					if (this.getOverwriteIDs() == null
							|| !this.getOverwriteIDs().equalsIgnoreCase(ArchivalLandscapeManager.PROCESS_OVERWRITE)) {
						this.totalInstitutions = this.aIDAO.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),false);
					}
					if(this.totalInstitutions!=null && this.totalInstitutions.size()>0){
						log.debug("Archival landscape could not be ingested directly, there are some institutions to check. Checking...");
						this.updatedInstitutions = new ArrayList<ArchivalInstitution>();
						this.insertedInstitutions = new ArrayList<ArchivalInstitution>();
						state = 2;
						//check if some institution of the new archivalInstitutions is/are into system and has content indexed
						validOperation = checkIfSomeInstitutionIsIngestedAndHasContentIndexed(archivalInstitutions);
						if((validOperation.equalsIgnoreCase(SUCCESS) && useddbb) || (!useddbb)){
							if(useddbb){
								JpaUtil.beginDatabaseTransaction();
							}
							//when valid operation it's able to manage all ingested institutions/groups
							this.deletedInstitutions = new ArrayList<ArchivalInstitution>();

							if (this.getOverwriteIDs() == null
									|| !this.getOverwriteIDs().equalsIgnoreCase(ArchivalLandscapeManager.PROCESS_OVERWRITE)) {
								this.totalInstitutions = this.aIDAO.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),true);
							}

							checkAndUpdateArchivalInstitutions(null,archivalInstitutions,useddbb); //new check
							log.debug("Updated process has been finished successfull");
							state = 3;

							log.debug("Inserting process for not updated institutions");
							//do an insert for rest file institutions
							String aiName = insertNotUpdatedInstitutions(archivalInstitutions,null,useddbb);
							if (aiName != null){
								// this institution has any lang error and trans may be closed
								state = 6;
								log.debug("Institution has not lang");
								validOperation = ERROR_LANG;//LANG_ERROR
								JpaUtil.rollbackDatabaseTransaction();
								this.setAiArchivalInstitutionName(aiName);
								return validOperation;
							}
							log.debug("Done insert process!");
							state = 4;
							//now delete all institutions which has not been updated (old institutions are not being processed)
							//institutions to be deleted = totalDDBBinstitution - (institutionsUpdated + institutionsDeleted)
							if(useddbb){
								this.deletedInstitutions = new ArrayList<ArchivalInstitution>(); //clean
								boolean error = deleteSimpleUnusedInstitutions();
								if(!error){
									error = deleteSimpleUnusedGroups();
									state = 5;
									JpaUtil.commitDatabaseTransaction();
									//finally remove deleted files from ddbb if they existed
									state = 10;
									removePathsToBeDeleted();
								}else{
									state = 6;
									log.debug("Invalid operation detected. There could be content into some institution.");
									validOperation = ERROR_CONTENT_2;
									JpaUtil.rollbackDatabaseTransaction();
									rollbackDeletedPaths();
								}
							}
						}
					}else{ //this case is for an ingestion on empty country, only tries to store the target structure 
						state = 7;
						this.insertedInstitutions = new ArrayList<ArchivalInstitution>();
						JpaUtil.beginDatabaseTransaction();
						state = 8;
						insertChildren(archivalInstitutions,null,useddbb); //insert institution
						JpaUtil.commitDatabaseTransaction();
						state = 9;
					}
				}
			}catch(Exception e){
				validOperation = ERROR;
				rollbackDeletedPaths();
				log.error("Some excepton comparing new AL structure with old AL structure. state: "+state + " " + APEnetUtilities.generateThrowableLog(e));
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
	 * <p>
	 * Method which removes directories from "repo" folder.
	 * </p>
	 * 
	 * @throws IOException - See {@link FileUtils}.deleteDirectory Apache documentation for more details 
	 */
	private void removePathsToBeDeleted() throws IOException {
		if(this.pathsToBeDeleted!=null){
			Iterator<String> itPaths = this.pathsToBeDeleted.iterator();
			String subDir = APEnetUtilities.getConfig().getRepoDirPath();
			while(itPaths.hasNext()){
				String path = itPaths.next();
				if (path != null && path.length() > 0) {
					log.debug("Delete operation was ok, deleting _old directory...");
					FileUtils.deleteDirectory(new File(subDir+path));
					log.debug("Done!! Finished.");
				}
			}
		}
	}

	/**
	 * <p>
	 * Method which rollback (move to the original path)
	 * a file marked to be deleted.
	 * </p>
	 * <p>
	 * Uses global pathsToBeDeleted and move into "repo" dir path to 
	 * "delete" (move/rename with '_old') directories.
	 * </p>
	 * @throws IOException - See {@link FileUtils}.moveDirectory Apache documentation for more details 
	 */
	private void rollbackDeletedPaths() throws IOException {
		if(this.pathsToBeDeleted!=null){
			Iterator<String> itPaths = this.pathsToBeDeleted.iterator();
			String subDir = APEnetUtilities.getConfig().getRepoDirPath();
			while(itPaths.hasNext()){
				String path = itPaths.next();
				if (path != null && path.length() > 0) {
					log.debug("Rollback detected, reverting _old to original path...");
					FileUtils.moveDirectory(new File(subDir+path),new File(subDir+path.substring(0,path.length()-"_old".length())));
					log.debug("Revert done!");
				}
			}
		}
	}

	/**
	 * <p>
	 * Method which deletes institution which are not used.
	 * </p>
	 * <p>
	 * Returns if an institution is removed.
	 * </p>
	 * @return boolean -> error
	 */
	private boolean deleteSimpleUnusedGroups() {
		boolean error = false;
		log.debug("Begin delete process for old institutions.");
		List<ArchivalInstitution> excludedInstitutions = new ArrayList<ArchivalInstitution>();
		excludedInstitutions.addAll(this.updatedInstitutions);
		excludedInstitutions.addAll(this.insertedInstitutions);
		//get all institutions to be deleted from ddbb
		List<ArchivalInstitution> institutionsToBeDeleted = this.aIDAO.getArchivalInstitutionsByCountryIdUnless(SecurityContext.get().getCountryId(),excludedInstitutions, false);

		// Order the groups to be ale to delete them correctly.
		institutionsToBeDeleted = this.orderGroups(institutionsToBeDeleted);
		log.debug("Institutions to be deleted: "+institutionsToBeDeleted.size());
		Iterator<ArchivalInstitution> deleteIt = institutionsToBeDeleted.iterator();
		while(!error && deleteIt.hasNext()){
			ArchivalInstitution targetToBeDeleted = deleteIt.next();
			if(!targetToBeDeleted.isContainSearchableItems() && targetToBeDeleted.isGroup()){
				log.debug("Deleting institution: "+targetToBeDeleted.getInternalAlId() + " " + targetToBeDeleted.getAiname() + " " + targetToBeDeleted.getAiId());
				Set<AiAlternativeName> alternativeNames = targetToBeDeleted.getAiAlternativeNames();
				if(alternativeNames!=null && alternativeNames.size()>0){
					log.debug("Deleting alternative names...");
					Iterator<AiAlternativeName> itAN = alternativeNames.iterator();
					while(itAN.hasNext()){
						this.aIANDAO.deleteSimple(itAN.next()); //removes each alternative name
					}
				}
				String oldPath = targetToBeDeleted.getEagPath();
				String path = ArchivalLandscapeUtils.deleteContent(targetToBeDeleted);
				if(path!=null || oldPath==null){
					//this.aIDAO.deleteSimple(targetToBeDeleted); //delete unused institution
					this.deletedInstitutions.add(targetToBeDeleted);
					if(this.pathsToBeDeleted==null){ this.pathsToBeDeleted = new HashSet<String>();}
					this.pathsToBeDeleted.add(path);
					log.debug("Deleted institution with aiId: "+targetToBeDeleted.getAiId());
				}
			}
		}
		return error;
	}

	/**
	 * <p>
	 * Method to order the structure of the groups.
	 * </p>
	 *
	 * @param archivalInstitutionList - List<{@link ArchivalInstitution}> source unsorted
	 * @return ArrayList<{@link ArchivalInstitution}> -> Sorted list of institutionsToBeDeleted.
	 */
	private List<ArchivalInstitution> orderGroups(List<ArchivalInstitution> archivalInstitutionList) {
		Set<ArchivalInstitution> institutionsToBeDeleted = new LinkedHashSet<ArchivalInstitution>();
		// Recover each group in the list.
		if (archivalInstitutionList != null && !archivalInstitutionList.isEmpty()) {
			// First time, recover the root groups (groups without parents).
			List<ArchivalInstitution> parentAIList = new ArrayList<ArchivalInstitution>();
			Iterator<ArchivalInstitution> aiForParentAIIt = archivalInstitutionList.iterator();
			while (aiForParentAIIt.hasNext()) {
				ArchivalInstitution aiForParent = aiForParentAIIt.next();
				if (aiForParent.isGroup() && aiForParent.getParent() == null) {
					parentAIList.add(aiForParent);
				}
			}

			// If parentAIList has elements.
			if (parentAIList != null && !parentAIList.isEmpty()) {
				// For each parent, recover children and check if exist it in the list.
				Iterator<ArchivalInstitution> aiParentAIIt = parentAIList.iterator();
				while (aiParentAIIt.hasNext()) {
					ArchivalInstitution currentAI = aiParentAIIt.next();
					institutionsToBeDeleted.addAll(ArchivalLandscapeUtils.orderChildsGroups(currentAI, archivalInstitutionList));
					institutionsToBeDeleted.add(currentAI);
				}
			} else {
				Iterator<ArchivalInstitution> aiForChildAIIt = archivalInstitutionList.iterator();
				while (aiForChildAIIt.hasNext()) {
					ArchivalInstitution aiForChild = aiForChildAIIt.next();

					while (aiForChild.getParent() != null) {
						aiForChild = aiForChild.getParent();
					}
					
					if (aiForChild.isGroup() && aiForChild.getParent() == null) {
						institutionsToBeDeleted.addAll(ArchivalLandscapeUtils.orderChildsGroups(aiForChild, archivalInstitutionList));
					}
				}
			}
		}
		return new ArrayList<ArchivalInstitution>(institutionsToBeDeleted);
	}

	/**
	 * <p>
	 * Detects and checks if some institution exists into system which algorithm
	 * could overwrite. When it's detected an overwrite check it's launched.
	 * </p>
	 * <p>
	 * Returns if an override and/or normal process is possible.
	 * </p>
	 * 
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @return Structs.STATE
	 */
	private String checkIfSomeInstitutionIsIngestedAndHasContentIndexed(Collection<ArchivalInstitution> archivalInstitutions) {
		String valid = SUCCESS;
//		boolean exit = false;
		List<ArchivalInstitution> archivalInstitutionsPlainList = ArchivalLandscapeUtils.parseCollectionToPlainList(archivalInstitutions); //parse archivalInstitutions structure to plain identifiers list structure
		if(this.totalInstitutions!=null && this.totalInstitutions.size()>0){
			Iterator<ArchivalInstitution> itTotalInstitutions = this.totalInstitutions.iterator(); //DDBB ingested institutions
			//first looper, total institutions ingested into DDBB
			while(itTotalInstitutions.hasNext()){
				boolean institutionExists = false;
				ArchivalInstitution currentIngestedInstitution = itTotalInstitutions.next();
				//second loop, new institutions into plain format
				Iterator<ArchivalInstitution> itAIPlain = archivalInstitutionsPlainList.iterator();
				while(!institutionExists && itAIPlain.hasNext()){
					//compare first looped institution with second looped institution.
					ArchivalInstitution plainInstitution = itAIPlain.next();
					if(currentIngestedInstitution.getInternalAlId().equals(plainInstitution.getInternalAlId())){ //detected
						institutionExists=true;//not needed seek into other branches
						//then check if ingested institution has content indexed
						if(currentIngestedInstitution.isContainSearchableItems()){
							boolean error = false;
							log.debug("The current institution (" + currentIngestedInstitution.getAiname() + ") has content indexed.");
							// Checks if the name is changed.
							if (!currentIngestedInstitution.getAiname().equals(plainInstitution.getAiname())) {
								log.debug("The current institution (" + currentIngestedInstitution.getAiname() + ") hasn't the same name as the institution to ingest (" + plainInstitution.getAiname() + ").");
								error = true;
							}

							// Checks if the element changed its position inside AL hierarchy.
							String pathToCurrentIngestedInstitution = ArchivalLandscapeUtils.buildParentsNode(currentIngestedInstitution);
							String pathToPlainInstitution = ArchivalLandscapeUtils.buildParentsNode(plainInstitution);
							if (!pathToCurrentIngestedInstitution.equals(pathToPlainInstitution)) {
								log.debug("The path for the current institution (" + pathToCurrentIngestedInstitution + ") hasn't the same name as the path for the institution to ingest (" + pathToPlainInstitution + ").");
								error = true;
							}


							if (error) {
								valid = ERROR_CONTENT;
								log.debug("Creating list of elements affected.");
								if (currentIngestedInstitution.isGroup()) {
									// Recover all child institutions that have content indexed.
									List<ArchivalInstitution> archivalInstitutionList = this.recoverChildInstitutions(currentIngestedInstitution);
									// Add the current element to the display list.
									archivalInstitutionList.add(currentIngestedInstitution);
									Collections.reverse(archivalInstitutionList);
									Set<ArchivalInstitution> institutionNamesSet = new LinkedHashSet<ArchivalInstitution>(archivalInstitutionList);
									if (institutionNamesSet != null && !institutionNamesSet.isEmpty()) {
										Iterator<ArchivalInstitution> institutionNamesIt = institutionNamesSet.iterator();
										while (institutionNamesIt.hasNext()) {
											ArchivalInstitution archivalInstitution = institutionNamesIt.next();
											if (!archivalInstitution.isGroup()) {
												this.addInstitutionsWithContent(archivalInstitution.getAiname());
											}
										}
									}
								} else {
									this.addInstitutionsWithContent(currentIngestedInstitution.getAiname());
								}
							}
						}
					}
				}
			}
		}

		return valid;
	}

	/**
	 * <p>
	 * Method to recover all child institutions that have content indexed.
	 * </p>
	 * @param archivalInstitution - {@link ArchivalInstitution} current archival institution to process (source).
	 * @return List<{@link ArchivalInstitution}> -> List of archival institution with searching content/published.
	 */
	private List<ArchivalInstitution> recoverChildInstitutions(ArchivalInstitution archivalInstitution) {
		log.debug("Recover elements with content indexed for: " + archivalInstitution.getAiname());
		List<ArchivalInstitution> archivalInstitutionList = new ArrayList<ArchivalInstitution>();
		if (this.aIDAO == null) {
			this.aIDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		}
		List<ArchivalInstitution> aiList = this.aIDAO.getArchivalInstitutionsWithSearchableItems(archivalInstitution.getCountryId(), archivalInstitution.getAiId());

		if (aiList != null && !aiList.isEmpty()) {
			for (int i = 0; i< aiList.size(); i++) {
				ArchivalInstitution ai = aiList.get(i);
				if (ai.isGroup()) {
					archivalInstitutionList.addAll(recoverChildInstitutions(ai));
				}
			}
		}

		// Add the current elements.
		archivalInstitutionList.addAll(aiList);
		return archivalInstitutionList;
	}

	/**
	 * <p>
	 * Insert not updated institutions.
	 * </p>
	 * <p>
	 * Iterates through a global List<{@link ArchivalInstitution}> (updatedInstitutions)
	 * to check if an institution is into parameter. 
	 * </p>
	 * <p> 
	 * If it's found is called recursively or discarded.
	 * If it's not found this institution is inserted.
	 * </p>
	 * <p>
	 * This method could be called recursively by himself.
	 * <p/>
	 * <p>
	 * It should be the rest of the param Collection<{@link ArchivalInstitution}> not updated.
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> sources
	 * @param parent - {@link ArchivalInstitution} parent
	 * @return String -> 'stdout' or {@link DashboardAPEnetException} detailed message
	 */
	private String insertNotUpdatedInstitutions(Collection<ArchivalInstitution> archivalInstitutions,ArchivalInstitution parent,boolean useddbb) {
		String strOut = null;
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
						found = true; //flag for next logic
					}
				}
				if(!found){ //error code to control if an error has happen, if not found launch logic to insert institution
					log.debug("Institution to be inserted: "+targetToBeInserted.getInternalAlId());
					if(parent!=null){
						targetToBeInserted.setParent(this.groupsInsertedIntoDDBB.get(parent.getInternalAlId()));
					}
					try{
						insertInstitution(targetToBeInserted,useddbb); //it's the recurse method, so it's not needed call to himself (insertNotUpdatedInstitutions)
					}catch(DashboardAPEnetException e){
						log.debug("Institution without lang: "+e.getMessage());
						strOut= e.getMessage();
						return strOut;
					}
				}else if(targetToBeInserted.isGroup() && targetToBeInserted.getChildArchivalInstitutions()!=null && targetToBeInserted.getChildArchivalInstitutions().size()>0){ //additional check children
					strOut=insertNotUpdatedInstitutions(targetToBeInserted.getChildArchivalInstitutions(),targetToBeInserted,useddbb);
				}
			}
		}
		return strOut;
	}
	/**
	 * <p>
	 * Method used to delete all institutions not updated or inserted.
	 * </p>
	 * <p>
	 * It runs a DDBB query to get all current institutions less the 
	 * sum of the updatedInstitutions and the insertedInstitutions.
	 * </p>
	 * <p>
	 * It just delete all alternative names not used of each unused institutions.
	 * </p>
	 * 
	 * @return boolean -> error detected
	 */
	private boolean deleteSimpleUnusedInstitutions() {
		boolean error = false;
		log.debug("Begin delete process for old institutions.");
		List<ArchivalInstitution> excludedInstitutions = new ArrayList<ArchivalInstitution>();
		excludedInstitutions.addAll(this.updatedInstitutions);
		excludedInstitutions.addAll(this.insertedInstitutions);
		//get all institutions to be deleted from ddbb
		List<ArchivalInstitution> institutionsToBeDeleted = this.aIDAO.getArchivalInstitutionsByCountryIdUnless(SecurityContext.get().getCountryId(),excludedInstitutions, false);
		log.debug("Institutions to be deleted: "+institutionsToBeDeleted.size());
		Iterator<ArchivalInstitution> deleteIt = institutionsToBeDeleted.iterator();
		while(!error && deleteIt.hasNext()){
			ArchivalInstitution targetToBeDeleted = deleteIt.next();
			if(!targetToBeDeleted.isContainSearchableItems() && !targetToBeDeleted.isGroup()){
				log.debug("Deleting institution: "+targetToBeDeleted.getInternalAlId() + " " + targetToBeDeleted.getAiname() + " " + targetToBeDeleted.getAiId());
				Set<AiAlternativeName> alternativeNames = targetToBeDeleted.getAiAlternativeNames();
				if(alternativeNames!=null && alternativeNames.size()>0){
					log.debug("Deleting alternative names...");
					Iterator<AiAlternativeName> itAN = alternativeNames.iterator();
					while(itAN.hasNext()){
						this.aIANDAO.deleteSimple(itAN.next()); //removes each alternative name
					}
				}
				String oldPath = targetToBeDeleted.getEagPath();
				String path = ArchivalLandscapeUtils.deleteContent(targetToBeDeleted);
				if(path!=null || oldPath==null){
					if(this.pathsToBeDeleted==null){ this.pathsToBeDeleted = new HashSet<String>();}
					this.pathsToBeDeleted.add(path);
					//this.aIDAO.deleteSimple(targetToBeDeleted); //delete unused institution
					this.deletedInstitutions.add(targetToBeDeleted);
					log.debug("Deleted institution with aiId: "+targetToBeDeleted.getAiId());
				}else{
					log.debug("NOT Deleted institution with aiId: "+targetToBeDeleted.getAiId()+" by content not deletable.");
					error = true;
					this.addInstitutionsWithContentNotPublished(targetToBeDeleted.getAiname());
				}
			}else if(!targetToBeDeleted.isGroup()){
				if(this.institutionsWithContentNotPublished==null){
					this.institutionsWithContentNotPublished = new HashSet<String>();
				}
				this.institutionsWithContentNotPublished.add(targetToBeDeleted.getAiname());
				log.debug("Detected institution with content indexed, so it could not be deleted. Marked operation like invalid.");
				error = true; //flag used to check an error for rollback
			}
		}
		return error;
	}

	/**
	 * <p>
	 * Checks if it's needed some delete operation.
	 * </p>
	 * <p>
	 * Loop into parameter List<{@link ArchivalInstitution}> currentIngestedArchivalInstitutions and compare
	 * this list with internal detected updateInstitutions. If an institution is found it market it and deletes 
	 * from updated list (this.updatedInstitutions).
	 * </p>
	 * 
	 * @param List<{@link ArchivalInstitution}> -> currentIngestedArchivalInstitutions, used to seek into internal updatedInstitutions
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
	/**
	 * <p>
	 * This method is recursive, when it's used it has 2 management cases.
	 * </p>
	 * <p>
	 * On the one hand is used when first time is called. It tries to get all parent institutions
	 * and next run into targets from parents to children.
	 * </p>
	 * <p>
	 * On the other hand there is the recursive method, institutionUpdate, to fill all targets.
	 * </p>
	 * <p>
	 * It's needed to be called each time by one institution (sibling).
	 * </p>
	 * 
	 * @param target - {@link ArchivalInstitution} seeker
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @param useddbb - boolean flag
	 * @return boolean -> some institution has been updated
	 */
	private boolean checkAndUpdateArchivalInstitutions(ArchivalInstitution target,Collection<ArchivalInstitution> archivalInstitutions, boolean useddbb) {
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
					found = checkParents(parentInstitution,archivalInstitutions,useddbb);
				}
			}else{ //search institution into all archivalInstitutions-tree
				found = institutionUpdate(archivalInstitutions,target,useddbb); //process to check recursively
			}
		}catch(Exception e){
			log.error("Some bad happened trying to update institution",e);
		}
		return found;
	}
	
	/**
	 * <p>
	 * Update the current node with the new information.
	 * </p>
	 * <p>
	 * Institutions - Alternative Names are being updating.
	 * </p>
	 * <p>
	 * If institution has children they are not being updated.
	 * </p>
	 * 
	 * @param oldDDBBInstitution - {@link ArchivalInstitution} source
	 * @param updatedInstitution - {@link ArchivalInstitution} updated
	 * @return boolean -> state
	 */
	protected boolean replaceNode(ArchivalInstitution oldDDBBInstitution,ArchivalInstitution updatedInstitution) {
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
//		oldDDBBInstitution.setPartnerId(SecurityContext.get().getPartnerId());
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
							oldAlternativeName.setPrimaryName(newAlternativeName.getPrimaryName()!=null?newAlternativeName.getPrimaryName():false);
							newANs.remove(newAlternativeName);
							this.aIANDAO.updateSimple(oldAlternativeName);
						}
					}
					if(!found){
						this.aIANDAO.deleteSimple(oldAlternativeName);
					}
				}
			}
			if(newANs!=null && !newANs.isEmpty()){
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
		if(oldDDBBInstitution.isGroup()){
			this.groupsInsertedIntoDDBB.put(oldDDBBInstitution.getInternalAlId(),oldDDBBInstitution);
		}
		this.updatedInstitutions.add(oldDDBBInstitution);
		return state;
	}
	
	/**
	 * <p>
	 * Recursive function to check root parents and all his children when
	 * target parent institution has other institutions pending of.
	 * </p>
	 * <p>
	 * Returns boolean, if target institution is updated or not.
	 * </p>
	 * 
	 * @param parentInstitutions - List<{@link ArchivalInstitution}> parents
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> sources
	 * @param useddbb - boolean flag
	 * @return boolean -> state
	 */
	private boolean checkParents(List<ArchivalInstitution> parentInstitutions,Collection<ArchivalInstitution> archivalInstitutions, boolean useddbb) {
		boolean found = false;
		Iterator<ArchivalInstitution> itParentInstitution = parentInstitutions.iterator();
		int correction = 0;
		while(itParentInstitution.hasNext()){
			ArchivalInstitution currentParent = itParentInstitution.next();
			if(currentParent!=null){
				log.debug("Checking parent: "+currentParent.getAiname());
				//check parent
				currentParent.setAlorder(currentParent.getAlorder()-correction);
				found = institutionUpdate(archivalInstitutions,currentParent,useddbb); //process to check recursively
				log.debug("Check done, found: "+found);
				//check child structure
				if(currentParent.isGroup()){
					//check each children first
					Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(currentParent.getChildArchivalInstitutions());
					if(children!=null && children.size()>0){
						log.debug("Checking children with size: "+children.size());
						checkParents(new ArrayList<ArchivalInstitution>(children),archivalInstitutions,useddbb);
					}
				}
			}else{
				correction++;
			}
		}
		return found;
	}
	
	/**
	 * <p>
	 * This function update an institution when it's found.
	 * </p>
	 * <p>
	 * <p>
	 * When it's found List<{@link ArchivalInstitution}> this.deletedInstitutions is updated
	 * and the current founded institution is appended to this list.
	 * </p>
	 * <p>
	 * It returns a boolean if the target Archival Institution is found or not.
	 * </p>
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> sources
	 * @param target - {@link ArchivalInstitution} seeker
	 * @return boolean -> institution updated was found
	 */
	private boolean institutionUpdate(Collection<ArchivalInstitution> archivalInstitutions, ArchivalInstitution target,boolean useddbb) {
		boolean found = false;
		Iterator<ArchivalInstitution> archivalInstitutionsIt = archivalInstitutions.iterator();
		while(!found && archivalInstitutionsIt.hasNext()){
			ArchivalInstitution archivalInstitution = archivalInstitutionsIt.next();
			if(archivalInstitution.getInternalAlId()!=null && archivalInstitution.getInternalAlId().equals(target.getInternalAlId())){ //found
				log.debug("Found institution equal: "+target.getAiname()+". Replacing node...");
				found = true;
				this.deletedInstitutions.add(target); //not needed for search afterwards
				if(useddbb){
					replaceNode(target,archivalInstitution); //replace node
				}
				log.debug("Replace done for institution: "+target.getAiname());
			}else if(archivalInstitution.isGroup()){//not found
				Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(archivalInstitution.getChildArchivalInstitutions());
				if(children!=null && children.size()>0){
					log.debug("Checking children with size: "+children.size());
					found = institutionUpdate(children,target,useddbb); //search into his children (not target, only archivalInstitutions provided).
				}
			}else{
				log.debug("Institution not found. Checked institution: "+archivalInstitution.getAiname());
			}
		}
		return found;
	}

	/**
	 * <p>
	 * Deletes a simple institution (this method doesn't 
	 * take into account count children) and his alternative names.
	 * </p>
	 * 
	 * @param possibleDeletedInstitution -> {@link ArchivalInstitution} target
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
			Set<ArchivalInstitution> possibleChildren = new LinkedHashSet<ArchivalInstitution>(possibleDeletedInstitution.getChildArchivalInstitutions());
			if(possibleChildren!=null && possibleChildren.size()>0){
				log.debug("Checking children for delete, parent: "+possibleDeletedInstitution.getAiname());
				checkDeleteArchivalInstitutions(new ArrayList<ArchivalInstitution>(possibleChildren));
			}
		}
		String oldPath = possibleDeletedInstitution.getEagPath();
		String path = ArchivalLandscapeUtils.deleteContent(possibleDeletedInstitution);
		if(path!=null || oldPath==null){
			if(this.pathsToBeDeleted==null){ this.pathsToBeDeleted = new HashSet<String>();}
			this.pathsToBeDeleted.add(path);
			log.debug("Deleted institution: "+possibleDeletedInstitution.getAiname());
			this.deletedInstitutions.add(possibleDeletedInstitution);
		}else{
			log.debug("Not deleted institution: "+possibleDeletedInstitution.getAiname()+" by content ingested not deletable.");
		}
	}

	/**
	 * <p>
	 * Recursive function to ingest an Archival Institution to DDBB and his children.
	 * </p>
	 * <p>
	 * It loops into archivalInstitution param and next go each by one.
	 * </p>
	 * <p>
	 * If target institution is a group (SERIES) it ingests itself and next
	 * tries to ingests his children.
	 * </p>
	 * <p>
	 * If target case is not a group (FILE) it ingest itself by 
	 * insertInstitution method.
	 * </p> 
	 * 
	 * @param archivalInstitutions - Collection<{@link ArchivalInstitution}> source
	 * @param parent - {@link ArchivalInstitution} seeker
	 * @throws {@link DashboardAPEnetException}
	 */
	private void insertChildren(Collection<ArchivalInstitution> archivalInstitutions,ArchivalInstitution parent,boolean useddbb) throws DashboardAPEnetException {
		Iterator<ArchivalInstitution> aiIt = archivalInstitutions.iterator();
		List<ArchivalInstitution> institutionsToBeInserted = new ArrayList<ArchivalInstitution>();
		while(aiIt.hasNext()){
			ArchivalInstitution currentInstitution = aiIt.next();
			currentInstitution.setParent(parent);
			if(parent!=null){
				currentInstitution.setParentAiId(parent.getAiId()); //fix for hibernate map
			}
			if(currentInstitution.isGroup()){
				Set<ArchivalInstitution> institutionChildren = new LinkedHashSet<ArchivalInstitution>(currentInstitution.getChildArchivalInstitutions());
				if(institutionChildren!=null && institutionChildren.size()>0){
					ArchivalInstitution childParent = this.aIDAO.insertSimple(currentInstitution);
					Set<AiAlternativeName> alternativeNames = childParent.getAiAlternativeNames();
					if(alternativeNames!=null){
						Iterator<AiAlternativeName> alternativeNamesIt = alternativeNames.iterator();
						while(alternativeNamesIt.hasNext()){
							AiAlternativeName alternativeName = alternativeNamesIt.next();
							alternativeName.setArchivalInstitution(childParent);
							alternativeName.setPrimaryName(alternativeName.getPrimaryName()!=null?alternativeName.getPrimaryName():false);
							this.aIANDAO.insertSimple(alternativeName);
							log.debug("Inserted alternative name: "+alternativeName.getAiAName());
						}
					}
					this.insertedInstitutions.add(currentInstitution);
					log.debug("Inserted parent "+currentInstitution.getInternalAlId()+" with id:"+childParent.getAiId()+" which contain children");
					insertChildren(institutionChildren,childParent,useddbb);
				}else{
					insertInstitution(currentInstitution,useddbb);
				}
			}else{
				insertInstitution(currentInstitution,useddbb);
			}
		}
		if(institutionsToBeInserted.size()>0){
			log.debug("Inserted institution children with size: "+institutionsToBeInserted.size()+".");
		}
	}
	/**
	 * <p>
	 * Recursive function to ingest an Archival Institution.
	 * </p>
	 * <p>
	 * All the target institutions will be provided from this method.
	 * </p>
	 * <p>
	 * This method is used to ingest all institutions each by one with
	 * a simple method (needs an open transaction first and next a commit).
	 * </p>
	 * <p>
	 * Function aggregates institution to global this.groupsInsertedIntoDDBB,
	 * which it's used onto other methods to be used to put a parent.
	 * </p>
	 * <p> These parents are stored into a Map<String-InternalAlId,{@link ArchivalInstitution}-parent>
	 * </p>
	 * <p>
	 * It's called by insertChildren institution.
	 * </p>
	 * 
	 * @param currentInstitution - {@link ArchivalInstitution} seeker
	 * @param useddbb - boolean flag
	 * @return {@link ArchivalInstitution} -> institution inserted
	 */
	private ArchivalInstitution insertInstitution(ArchivalInstitution currentInstitution,boolean useddbb) throws DashboardAPEnetException {
		String internalAlId = currentInstitution.getInternalAlId();
		if(internalAlId==null || internalAlId.isEmpty()){
			internalAlId = ArchivalLandscapeEditor.getNewinternalIdentifier();
			currentInstitution.setInternalAlId(internalAlId);
		}
		//loop file and check if lang is not null 
		Set<AiAlternativeName> aiANSet = currentInstitution.getAiAlternativeNames();
		Iterator<AiAlternativeName> aiANIt = aiANSet.iterator();
		while (aiANIt.hasNext()) {
			AiAlternativeName aiAlternativeName = aiANIt.next();
			if (aiAlternativeName.getLang() == null) {
				String strErr = aiAlternativeName.getAiAName();
				throw new DashboardAPEnetException(strErr, new NullPointerException());
			}
		}
		ArchivalInstitution institution = currentInstitution;
		if(useddbb){
			institution = this.aIDAO.insertSimple(currentInstitution);
		}
		if(institution!=null && institution.isGroup()){
			if (this.groupsInsertedIntoDDBB == null) {
				this.groupsInsertedIntoDDBB = new HashMap<String,ArchivalInstitution>();
			}
			this.groupsInsertedIntoDDBB.put(institution.getInternalAlId(),institution);
		}
		//alternative names logic
		if(useddbb){
			Set<AiAlternativeName> alternativeNames = currentInstitution.getAiAlternativeNames();
			if(alternativeNames!=null){
				Iterator<AiAlternativeName> alternativeNamesIt = alternativeNames.iterator();
				while(alternativeNamesIt.hasNext()){
					AiAlternativeName alternativeName = alternativeNamesIt.next();
					alternativeName.setArchivalInstitution(institution);
					alternativeName.setPrimaryName(alternativeName.getPrimaryName()!=null?alternativeName.getPrimaryName():false);
					this.aIANDAO.insertSimple(alternativeName);
					log.debug("Inserted alternative name: "+alternativeName.getAiAName());
				}
			}
		}
		if(currentInstitution.isGroup()){
			Set<ArchivalInstitution> childrenToInsert = new LinkedHashSet<ArchivalInstitution>(currentInstitution.getChildArchivalInstitutions());
			if(childrenToInsert!=null && childrenToInsert.size()>0){
				Iterator<ArchivalInstitution> itInstitutionChildren = childrenToInsert.iterator();
				while(itInstitutionChildren.hasNext()){
					ArchivalInstitution childInstitutionToInsert = itInstitutionChildren.next();
					childInstitutionToInsert.setParent(institution);
					childInstitutionToInsert.setParentAiId(institution.getAiId());
					log.debug("Trying to insert child institution");
					//1. check if institution exists, to be updated
					ArchivalInstitution child = this.aIDAO.getArchivalInstitutionByInternalAlId(childInstitutionToInsert.getInternalAlId(),SecurityContext.get().getCountryId());
					if(child!=null){
						//1.2 update parent for this institution
						child.setParent(currentInstitution);
						child.setParentAiId(currentInstitution.getParentAiId());
					}else{
						//2. if not exists, create
						insertInstitution(childInstitutionToInsert,useddbb);
					}
				}
			}
		}
		this.insertedInstitutions.add(currentInstitution);
		log.debug("Inserted institution: "+currentInstitution.getAiname());
		return currentInstitution;
	}

	/**
	 * <p>
	 * It extracts and returns file institutions.
	 * </p>
	 * <p>
	 * This method read file and returns a Set<{@link ArchivalInstitution}>.
	 * </p> 
	 * <p>
	 * It calls to createXMLStreamReader and next use getXMLArchivalInstitutionLevel 
	 * to read xml content and format it into the target set.
	 * </p>
	 * 
	 * @param archivalInstitutionFile - File source
	 * @param checkLang - boolean flag
	 * @return Set<{@link ArchivalInstitution}> -> file institutions / null
	 */
	public Set<ArchivalInstitution> getInstitutionsByALFile(File archivalInstitutionFile,boolean checkLang) {
		Set<ArchivalInstitution> archivalInstitutions = null;
		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			XMLStreamReader r = factory.createXMLStreamReader(new FileReader(archivalInstitutionFile));
			archivalInstitutions = getXMLArchivalInstitutionLevel(r,checkLang);
		} catch (FileNotFoundException e) {
			log.error("File not found :: "+archivalInstitutionFile.getAbsolutePath() + APEnetUtilities.generateThrowableLog(e));
		} catch(WstxUnexpectedCharException e) {
			log.error("Unexpected character into xml: ",e);
			this.setInvalidChars(true);
		} catch (XMLStreamException e) {
			log.error("Archival Landscape reading exception: " + APEnetUtilities.generateThrowableLog(e));
		} catch (WstxLazyException e){
			log.error("Unexpected character into xml: ",e);
			this.setInvalidChars(true);
		} catch (Exception e){
			log.error("Exception: " + APEnetUtilities.generateThrowableLog(e));
		}
		return archivalInstitutions;
	}
	
	/**
	 * <p>
	 * Method which obtain and return country institution children
	 * readed from file by SAX.
	 * </p>
	 * <p>
	 * Uses a LinkedHashSet to store the targets archival institutions.
	 * </p>
	 * <p>
	 * It should be called by getInstitutionsByALFile method.
	 * </p>
	 * <p>
	 * It returns an Set<{@link ArchivalInstitution}> with the content of 
	 * the xml level given in the target param.
	 * </p>
	 * 
	 * @throws {@link XMLStreamException}
	 * @param r - {@link XMLStreamReader} reader
	 * @param checkLang - boolean flag
	 * @return Set<{@link ArchivalInstitution}> -> reader institutions / null
	 */
	private Set<ArchivalInstitution> getXMLArchivalInstitutionLevel(XMLStreamReader r,boolean checkLang) throws XMLStreamException{
		ArchivalInstitution archivalInstitution = null;
		Set<ArchivalInstitution> archivalInstitutions = new LinkedHashSet<ArchivalInstitution>();
		String level = "";
		String id = "";
		String alternativeNameText = null;
		String alternativeLangText = null;
		boolean validXML = true;
		boolean continueLoop = false;
		Integer event = null;
		boolean openLevel = false;
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
						if(openLevel && level!=null && level.equals(ArchivalLandscapeUtils.SERIES)){
							Set<ArchivalInstitution> children = getXMLArchivalInstitutionLevelChildren(r,archivalInstitution,checkLang);
							if(children!=null){
								archivalInstitution.setChildArchivalInstitutions(new LinkedList<ArchivalInstitution>(children));
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
							for (int i = 0; i < r.getAttributeCount(); i++) {
								if(r.getAttributeLocalName(i)!=null && r.getAttributeLocalName(i).trim().equals("id")){
									id = r.getAttributeValue(i).trim();
								}else if (r.getAttributeLocalName(i).trim().equals(ArchivalLandscapeUtils.LEVEL)) {
									level = r.getAttributeValue(i).trim();
								}
							}
							if(!level.equals(ArchivalLandscapeUtils.FONDS)){
								if(!openLevel){
									openLevel = true;
									archivalInstitution.setAlorder(archivalInstitutions.size());
									archivalInstitution.setGroup(level.equals(ArchivalLandscapeUtils.SERIES));
									archivalInstitution.setInternalAlId((id!=null && id.length()>0)?id:"");
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
						if(!level.equals(ArchivalLandscapeUtils.FONDS)){ //fond level mustn't be used like an institution, it's a country
							archivalInstitutions.add(archivalInstitution); //first level
							openLevel = false;
						}
					}else if(r.getLocalName().trim().equals("unittitle") && archivalInstitution!=null){
						if(archivalInstitution.getAiname()==null || archivalInstitution.getAiname().trim().length()==0){
							archivalInstitution.setAiname(alternativeNameText);
						}
						Set<AiAlternativeName> alternativeNames = archivalInstitution.getAiAlternativeNames();
						boolean primaryName = false; //flag which set if it's the first language
						if(alternativeNames==null){
							alternativeNames = new HashSet<AiAlternativeName>();
						}
						if(alternativeNames.size()==0){
							primaryName = true;
						}
						if(archivalInstitution.getAiname()==null){
							archivalInstitution.setAiname(alternativeNameText);
						}
						AiAlternativeName alternativeName = new AiAlternativeName();
						alternativeName.setAiAName(alternativeNameText);
						alternativeName.setPrimaryName(primaryName);
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
						}else if(checkLang){
							log.error("Bad xml detected, reason: not lang for alternative name");
							this.setAiArchivalInstitutionName(alternativeNameText);
							this.errors = new ArrayList<String>();
							String message2 = getText("updateErrorFormatAL.error.unittitle.lang2");
							if(message2!=null && message2.length()>0){
								if(message2.contains("<")){
									message2 = message2.replace("<","&#60;");
								}if(message2.contains(">")){
									message2 = message2.replace(">","&#62;");
								}if(message2.contains("@")){
									message2 = message2.replace("@","&#64;");
								}
							}
							String message = "<p>&nbsp;</p>"+getText("updateErrorFormatAL.errors")  
									+"<p>&nbsp;</p>"+getText("updateErrorFormatAL.error.unittitle.lang1")
									+"<p>&nbsp;</p>"+alternativeNameText
									+ "<p>&nbsp;</p>"+ message2;
							
							this.errors.add(message);
							validXML = false;
						}
						alternativeNameText = "";
						alternativeLangText = "";
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(!r.isWhiteSpace() && alternativeNameText!=null && archivalInstitution!=null){
						alternativeNameText += r.getText().trim()
						    .replaceAll("[\\s+&&[^\\n]]+"," ")//1. reduce all non-newline whitespaces to a unique space
						    .replaceAll("(?m)^\\s+|\\s$","")//2. remove spaces from start or end of the lines
						    .replaceAll("\\n+"," ");//3. remove all newlines, compress it in a unique line
					}
			}
		}
		return (validXML)?archivalInstitutions:null;
	}
	
	/**
	 * <p>
	 * Method which get all the series children, it's used to obtain recursive
	 * institution series children.
	 * </p> 
	 * 
	 * @throws {@link XMLStreamException}
	 * @param r - {@link XMLStreamReader} reader
	 * @param aiParent - {@link ArchivalInstitution} parent
	 * @param checkLang - boolean flag
	 * @return LinkedHashSet<{@link ArchivalInstitution}> -> level institutions / null
	 */
	private Set<ArchivalInstitution> getXMLArchivalInstitutionLevelChildren(XMLStreamReader r, ArchivalInstitution aiParent,boolean checkLang) throws XMLStreamException {
		Set<ArchivalInstitution> archivalInstitutions = new LinkedHashSet<ArchivalInstitution>();
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
							id = level = "";
							for (int i = 0; i < r.getAttributeCount(); i++) {
								if(r.getAttributeLocalName(i)!=null && r.getAttributeLocalName(i).trim().equals("id")){
									id = r.getAttributeValue(i).trim();
								}else if (r.getAttributeLocalName(i).trim().equals(ArchivalLandscapeUtils.LEVEL)) {
									level = r.getAttributeValue(i).trim();
								}
							}
							archivalInstitution.setInternalAlId(id);
							archivalInstitution.setGroup((level!=null && level.equals(ArchivalLandscapeUtils.SERIES)));
							archivalInstitution.setAlorder(archivalInstitutions.size());
							archivalInstitution.setParent(aiParent);
						}else if(level!=null && level.equals(ArchivalLandscapeUtils.SERIES)){
							Set<ArchivalInstitution> children = getXMLArchivalInstitutionLevelChildren(r,archivalInstitution,checkLang);
							if(children!=null){
								log.debug("Children has been added with size: "+children.size()+" to institution with id: "+id);
								archivalInstitution.setChildArchivalInstitutions(new LinkedList<ArchivalInstitution>(children));
								update = true; //continue
							}else{
								log.debug("Detected invalid xml from getXMLArchivalInstitutionLevelChildren function");
								validXML = false;
							}
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
						if(!level.equals(ArchivalLandscapeUtils.FONDS)){ //fond level haven't be used like an institution, it's a country
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
						boolean primaryName = false;
						if(alternativeNames.size()==0){
							primaryName = true;
						}
						AiAlternativeName alternativeName = new AiAlternativeName();
						alternativeName.setAiAName(unittitle);
						alternativeName.setPrimaryName(primaryName);
						Lang lang = null;
						List<Lang> languages = DAOFactory.instance().getLangDAO().findAll();
						for(int i=0;lang==null && i<languages.size();i++){
							String tempLanguage = languages.get(i).getIsoname();
							if(tempLanguage.equalsIgnoreCase(uLang)){
								lang = languages.get(i);
							}
						}
						if(lang!=null){
							alternativeName.setLang(lang);
							alternativeNames.add(alternativeName);
							archivalInstitution.setAiAlternativeNames(alternativeNames);
							archivalInstitution.setCountry(this.country);
							archivalInstitution.setCountryId(this.country.getId()); //fix for current bad Hibernate mapping
						}else if(checkLang){
							log.error("ERROR: bad xml, invalid unittitle, reason: lang null or not found in server");
							this.setAiArchivalInstitutionName(unittitle);
							this.errors = new ArrayList<String>();
							String message2 = getText("updateErrorFormatAL.error.unittitle.lang2");
							if(message2!=null && message2.length()>0){
								if(message2.contains("<")){
									message2 = message2.replace("<","&#60;");
								}if(message2.contains(">")){
									message2 = message2.replace(">","&#62;");
								}if(message2.contains("@")){
									message2 = message2.replace("@","&#64;");
								}
							}
							String message = "<p>&nbsp;</p>"+getText("updateErrorFormatAL.errors")  
									+"<p>&nbsp;</p>"+getText("updateErrorFormatAL.error.unittitle.lang1")
									+"<p>&nbsp;</p>"+unittitle
									+ "<p>&nbsp;</p>"+ message2;
							
							this.errors.add(message);
							validXML = false;
						}
						unittitle = "";
						uLang = "";
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(!r.isWhiteSpace() && unittitle!=null && archivalInstitution!=null){
						unittitle += r.getText().trim().replaceAll("[\\s+&&[^\\n]]+"," ")//1. reduce all non-newline whitespaces to a unique space
						    .replaceAll("(?m)^\\s+|\\s$","")//2. remove spaces from start or end of the lines
						    .replaceAll("\\n+"," ");//3. remove all newlines, compress it in a unique line
					}
			}
		}
		return (validXML)?archivalInstitutions:null;
	}

	/**
	 * <p>
	 * Method which launches the download archival-landscape action.
	 * </p>
	 * <p>
	 * It calls to {@link ContentUtils}.download function. See {@link ContentUtils} documentation to get more extended explanation.
	 * </p>
	 * 
	 * @return Struct.STATE
	 */
	public String download(){
		ByteArrayOutputStream xml = ArchivalLandscapeUtils.buildXMlFromDDBB(getSecurityContext().getCountryName());
		if(xml!=null){
			try {
				String fileName = SecurityContext.get().getCountryIsoname().toUpperCase()+AL_FILE_NAME;
				ContentUtils.download(getServletRequest(), getServletResponse(), new ByteArrayInputStream(xml.toByteArray()), fileName, ContentUtils.MIME_TYPE_APPLICATION_XML);
			} catch (IOException e) {
				log.error("Exception trying to download file with ContentUtils.downloadXML"  + APEnetUtilities.generateThrowableLog(e));
			}
			return SUCCESS;
		}
		return ERROR;
	}
} 
