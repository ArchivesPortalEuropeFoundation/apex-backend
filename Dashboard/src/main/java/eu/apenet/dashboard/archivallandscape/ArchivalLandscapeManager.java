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
 * Class which supports download and upload actions
 * for Archival Landscape. It's separated for the rest
 * of the Archival Landscape Edition because these operations
 * only support the last Archival Landscape export/import 
 * operations (download and upload), which are based on DDBB 
 * and not in any File storage. 
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
	// Error when an institution has duplicated identifiers.
	private static final String ERROR_DUPLICATE_IDENTIFIERS = "errorDuplicateIdentifiers";
	private static final String ERROR_NAMES_CHANGED = "changedNames";
	// Error when the name of te institution hasn't language.
	private static final String ERROR_LANG = "errorLang";
	private static final String INVALID = "invalid";

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

	/**
	 * Upload main action
	 * @return Struts.STATE
	 * @throws SAXException
	 * @throws APEnetException
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
	 * Main launcher of logic.
	 * Unzip and call checks for ingest process.
	 * @return Struts.STATE
	 * @throws IOException
	 * @throws SAXException
	 * @throws APEnetException
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
	 * Checks if there is needed an overwrite, if not continues 
	 * with the normal process (old new implementation).
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
	 * Cancel (clean) action.
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

	private void fillMainFiles() {
		this.httpFileFileName = SecurityContext.get().getCountryIsoname().toUpperCase() +AL_FILE_NAME;
		this.httpFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + 
			File.separatorChar +SecurityContext.get().getCountryIsoname().toUpperCase(),
			this.httpFileFileName);
	}
	private void cleanMainFiles(){
		if (httpFile != null){
			try {
			File parentDirectory = httpFile.getParentFile();
			ContentUtils.deleteFile(httpFile, false);
			if (parentDirectory.list().length == 0){
				ContentUtils.deleteFile(parentDirectory, false);
			}
			}catch (IOException e){}
		}
	}

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
	 * Launch all validations.
	 * 
	 * @return boolean.RIGHT
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
	 * Checks if the AL uploaded is for the current country.
	 *
	 * @return boolean
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
	 * Checks two structures, indexed institutions with target structure.
	 * The first structure is plain (probably obtained by DDBB query)
	 * The second structure is in a tree way (probably obtained by a file parsed to an structured no plain) 
	 * @param indexedInstitutions
	 * @param archivalFileInstitutions
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
	 * Check for checkIndexedInstitutions - recursive. 
	 * It should be called by checkIndexedInstitutions.
	 * 
	 * @param childArchivalInstitutions
	 * @param indexedInstitution
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
	 * Action which displays the report from temporal file 
	 * while it's not being uploaded.
	 * @return Struts.STATE
	 */
	public String reportAction(){
		if(this.httpFile==null){
			fillMainFiles();
		}
		Collection<ArchivalInstitution> archivalInstitutions = getInstitutionsByALFile(this.httpFile,false);
		if(archivalInstitutions!=null){
			if(!institutionNamesHaveChanged(archivalInstitutions)){
				return displayReport(archivalInstitutions);
			}else{
				return ERROR_NAMES_CHANGED;
			}
		}else if (this.isInvalidChars()) {
			return ERROR_INVALID_CHARS;
		}
		return ERROR;
	}
	/**
	 * Compare, if possible identify an institution. 
	 * When method is able to detect that exits two institutions with the same identifier, it 
	 * compares the name, and if some institution detected has different name is rejected.
	 * 
	 * @param archivalInstitutions
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
					if(!archivalInstitutionsComparable.getAiname().equals(targetInstitution.getAiname())){
						state = false;
					}
				}
			}
		}
		return !state;
	}

	/**
	 * Action which displays three list, inserts, updates and deleted
	 * @param archivalInstitutions
	 * @return String <=> (Action.INPUT)
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

		return INPUT;
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
	/**
	 * Main function called to ingest all content.
	 * 
	 * This function is explited in two parts:
	 * 	1) Read target file and returns his archival-institutions structure
	 * 	2) Checks if previews structure is possible to insert/update into 
	 * 		current system and make ingestion/update logic.
	 * 
	 * @return Structs2-RESPONSE
	 * @throws APEnetException 
	 * @throws SAXException 
	 */
	public String ingestArchivalLandscapeXML() throws SAXException, APEnetException {
		String state = ERROR;
		try {
		
			if (this.httpFile == null) {
				this.fillMainFiles();
			}
			Boolean firstState = ArchivalLandscape.checkIdentifiers(this.httpFile);
			
			if (firstState==null){
				validateUploadedAL(this.httpFile);
				state=ERROR_IDENTIFIERS;
				Iterator<String> it = this.warnings_ead.iterator();
				while(it.hasNext()){
					addActionMessage(it.next());
				}
			} else if (!firstState) {
				this.setDuplicateIdentifiers(ArchivalLandscape.getDuplicateIdentifiers());
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
	 * Checks and work with an archival_institution structure.
	 * Tries to insert, update and delete institutions.
	 * 
	 * @param archivalInstitutions
	 * @return validOperation
	 * @throws IOException 
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
					this.totalInstitutions = this.aIDAO.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),false);
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
							
							this.totalInstitutions = this.aIDAO.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),true);
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
				String path = ArchivalLandscape.deleteContent(targetToBeDeleted);
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
	 * Method to order the structure of the groups.
	 *
	 * @param archivalInstitutionList List of groups to order.
	 * @return Order list.
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
	 * Detects and checks if some institution exists into system which algorithm
	 * could overwrite. When it's detected an overwrite check it's launched.
	 * Returns if an override and/or normal process is possible.
	 * 
	 * @param archivalInstitutions
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
	 * Method to recover all child institutions that have content indexed.
	 *
	 * @param archivalInstitution Current archival institution to process.
	 * @return List of archival institution with content published.
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
	 * Insert not updated institutions. It should be the rest of the 
	 * param Collection<ArchivalInstitution> not updated.
	 * @param archivalInstitutions
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
	 * Method used to delete all institutions not updated or inserted.
	 * It runs a DDBB query to get all current institutions less the 
	 * sum of the updatedInstitutions and the insertedInstitutions.
	 * 
	 * It just delete all alternative names not used of each unused institutions.
	 * 
	 * @return boolean (error situation)
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
				String path = ArchivalLandscape.deleteContent(targetToBeDeleted);
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
	/**
	 * This method is recursive, when it's used it has 2 management cases.
	 * 
	 * On the one hand is used when first time is called. It tries to get all parent institutions
	 * and next run into targets from parents to children.
	 * On the other hand there is the recursive method, institutionUpdate, to fill all targets.
	 * It's needed to be called each time by one institution (sibling).
	 * 
	 * @param target
	 * @param archivalInstitutions
	 * @param useddbb 
	 * @return boolean (found or not)
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
	 * Update the current node with the new information.
	 * Institutions - Alternative Names are being updating.
	 * If institution has children they are not being updated.
	 * 
	 * @param oldDDBBInstitution
	 * @param updatedInstitution
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
	 * Recursive function to check root parents and all his children when
	 * target parent institution has other institutions pending of.
	 * 
	 * Returns boolean, if target institution is updated or not.
	 * 
	 * @param parentInstitutions
	 * @param archivalInstitutions
	 * @param useddbb 
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
	 * This function update an institution when it's found.
	 * When it's found List<ArchivalInstitution> this.deletedInstitutions is updated
	 * and the current founded institution is appended to this list.
	 * 
	 * It returns a boolean if the target Archival Institution is found or not.
	 * 
	 * @param archivalInstitutions
	 * @param target
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
	 * Deletes a simple institution (this method doesn't 
	 * take into account count children) and his alternative names.
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
			Set<ArchivalInstitution> possibleChildren = new LinkedHashSet<ArchivalInstitution>(possibleDeletedInstitution.getChildArchivalInstitutions());
			if(possibleChildren!=null && possibleChildren.size()>0){
				log.debug("Checking children for delete, parent: "+possibleDeletedInstitution.getAiname());
				checkDeleteArchivalInstitutions(new ArrayList<ArchivalInstitution>(possibleChildren));
			}
		}
		String oldPath = possibleDeletedInstitution.getEagPath();
		String path = ArchivalLandscape.deleteContent(possibleDeletedInstitution);
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
	 * Recursive function to ingest an Archival Institution to DDBB and his children.
	 * 
	 * It loops into archivalInstitution param and next go each by one.
	 * If target institution is a group (SERIES) it ingests itself and next
	 * tries to ingests his children.
	 * If target case is not a group (FILE) it ingest itself by 
	 * insertInstitution method. 
	 * 
	 * @param archivalInstitutions
	 * @param parent
	 * @throws DashboardAPEnetException 
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
	 * Recursive function to ingest an Archival Institution.
	 * All the target institutions will be provided from this method.
	 * This method is used to ingest all institutions each by one with
	 * a simple method (needs an open transaction first and next a commit).
	 * 
	 * Function aggregates institution to global this.groupsInsertedIntoDDBB,
	 * which it's used onto other methods to be used to put a parent. These 
	 * parents are stored into a Map<String-InternalAlId,ArchivalInstitution-parent>
	 * 
	 * It's called by insertChildren institution.
	 * 
	 * @param currentInstitution
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
	 * It extracts and returns file institutions. 
	 * 
	 * @param archivalInstitutionFile
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
	 * Method which obtain and return country institution children
	 * readed from file by SAX.
	 * 
	 * @throws XMLStreamException
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
	 * Method which get all the series children, it's used to obtain recursive
	 * institution series children. 
	 * @throws XMLStreamException
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
