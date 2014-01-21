package eu.apenet.dashboard.archivallandscape;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
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

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
	private static final String COUNTRYCODE = "countrycode";
	
	private static final String EADID = "eadid";
	
	private static final String AL_FILE_NAME = "AL.xml";
	
	private static final String LEVEL = "level";
	private static final String FONDS = "fonds";
	private static final String SERIES = "series";
	private static final String FILE = "file";

	// Error when an institution has content published.
	private static final String ERROR_CONTENT = "errorContent";
	private static final String ERROR_CONTENT_2 = "errorContent2";

	private static final String AL_GLOBAL_UNITTITLE = "European countries";

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

	private Set<String> institutionsWithContent;

	private List<String> warnings_ead;

	// Variable for the name of the institution without lang.
	private String aiArchivalInstitutionName;

	private Set<String> institutionsWithContentNotPublished;

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
		return aiArchivalInstitutionName;
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

	public String upload() throws SAXException, APEnetException{
		String state = ERROR;
		try {
			state = checkUnzipAndUpload(this.httpFile,this.httpFileFileName,true);
		} catch (IOException e) {
			log.error(e);
		} 
		return state;
	}
	private String checkUnzipAndUpload(File httpFile, String httpFileFileName,boolean execute) throws IOException, SAXException, APEnetException {
		if(this.httpFile!=null && ((this.httpFileFileName!=null && execute) || (!execute))){
			String path = this.httpFile.getAbsolutePath();
			String format = this.httpFileFileName.substring(this.httpFileFileName.lastIndexOf(".") + 1).toLowerCase();
			if(format.equalsIgnoreCase("xml")){
				if(execute){
					return ingestArchivalLandscapeXML();
				}else{
					return displayBeforeAfterTrees();
				}
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
							if(execute){
								return ingestArchivalLandscapeXML();
							}else{
								return displayBeforeAfterTrees();
							}
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
	/**
	 * Called into parseCollectionToPlainList method.
	 * Functionality is very similar, but the different is the argument parent.
	 * 
	 * Parent is used to get all children and call to himself by recursive-way if needed.
	 * 
	 * @param parent
	 * @return List<ArchivalInstition> plainArchivalInstitutions
	 */
	private List<ArchivalInstitution> checkChild(ArchivalInstitution parent){
		List<ArchivalInstitution> archivalInstitutionList = new ArrayList<ArchivalInstitution>();
		log.debug("children check for parent: "+parent.getAiname());
		Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(parent.getChildArchivalInstitutions());
		Iterator<ArchivalInstitution> itChildren = children.iterator();
		while(itChildren.hasNext()){
			ArchivalInstitution institution = itChildren.next();
			if(institution.isGroup()){
				log.debug("Group: "+institution.getAiname()+" parent: "+parent.getAiname());
				if(institution.getChildArchivalInstitutions()!=null && institution.getChildArchivalInstitutions().size()>0){
					archivalInstitutionList.addAll(checkChild(institution)); //recursive call
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
	 * @throws APEnetException 
	 * @throws SAXException 
	 */
	private String ingestArchivalLandscapeXML() throws SAXException, APEnetException {
		String state = ERROR;
		Boolean firstState = ArchivalLandscape.checkIdentifiers(this.httpFile);
		
		if (firstState==null){
			validateUploadedAL(this.httpFile);
			state="errorIdentifier";
			Iterator<String> it = this.warnings_ead.iterator();
			while(it.hasNext()){
				addActionMessage(it.next());
			}
		}else{
			String countryCode = getXMLEadidCountrycode(this.httpFile);
			if(countryCode!=null && countryCode.equalsIgnoreCase(SecurityContext.get().getCountryIsoname())){
//				if(this.country==null){
					this.country = DAOFactory.instance().getCountryDAO().getCountryByCname(SecurityContext.get().getCountryName());
//				}
				Set<ArchivalInstitution> archivalInstitutions = getInstitutionsByALFile(this.httpFile);
				if(archivalInstitutions!=null){
					try{
						state = checkAndUpdateFromToDDBB(archivalInstitutions);
					}catch(Exception e){
						log.error("Exception checking institutions with ddbb to be replaced",e);
					}
				}
			}
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
	
	public String displayTrees(){
		String state = ERROR;
		try {
			state = checkUnzipAndUpload(this.httpFile,this.httpFileFileName,false);
		} catch (IOException e) {
			log.error(e);
		} catch (SAXException e) {
			log.error(e);
		} catch (APEnetException e) {
			log.error(e);
		} 
		return state;
	} 
	
	private String displayBeforeAfterTrees() throws IOException{
		Writer writer = null;
		try{
			log.debug("Building tree for Archival Institution");
			getServletRequest().setCharacterEncoding(UTF8);
			getServletResponse().setCharacterEncoding(UTF8);
			getServletResponse().setContentType("application/json");
			writer = new OutputStreamWriter(getServletResponse().getOutputStream(),UTF8);
			//begin json part
			writer.append("{");
			Set<ArchivalInstitution> archivalInstitutions = getInstitutionsByALFile(this.httpFile);
			if(archivalInstitutions!=null){
				writer.append("\"newtree\":");
				writer.append(parseArchivalInstitutionsToJSON(archivalInstitutions));
				//oldtree part, only if there are a valid new tree
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
				List<ArchivalInstitution> countryArchivalInstitutions = new LinkedList<ArchivalInstitution>(aiDao.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),true));
				if(countryArchivalInstitutions!=null){
					writer.append(",\"oldtree\":");
					writer.append(parseArchivalInstitutionsToJSON(countryArchivalInstitutions));
				}
				//now put texts values ("question","yes","no",..)
				writer.append(",\"question\":");
				writer.append("\""+getText("al.message.areyousureyouwanttocontinue")+"\"");
				writer.append(",\"yes\":");
				writer.append("\""+getText("content.message.yes")+"\"");
				writer.append(",\"no\":");
				writer.append("\""+getText("content.message.no")+"\"");
				writer.append(",\"oldtreeMessage\":");
				writer.append("\""+getText("al.message.oldtree")+"\"");
				writer.append(",\"newtreeMessage\":");
				writer.append("\""+getText("al.message.newtree")+"\"");
				writer.append(",\"status\":");
				writer.append("\""+getText("al.message.previewisbeingdisplayed")+"\"");//Preview is being displayed bellow
			}else{
				writer.append("\"error\":");
				writer.append("\""+getText("al.message.error.badarchivallandscapedetected")+"\"");//Preview is being displayed bellow
			}
			writer.append("}");
			//end json part
			writer.flush();
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}finally{
			if(writer!=null){
				writer.close();
			}
		}
		return SUCCESS;
	}

	private StringBuilder parseArchivalInstitutionsToJSON(Collection<ArchivalInstitution> archivalInstitutions) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		Iterator<ArchivalInstitution> itInstitutions = archivalInstitutions.iterator();
		while(itInstitutions.hasNext()){
			ArchivalInstitution institution = itInstitutions.next();
			json.append(buildArchivalInstitutionJSON(institution));
			if(itInstitutions.hasNext()){
				json.append(",");
			}
		}
		json.append("]");
		return json;
	}

	private StringBuilder buildArchivalInstitutionJSON(ArchivalInstitution institution) {
		StringBuilder json = new StringBuilder(); //{ "title": "Node 1", "key": "k1", "isLazy": true }
		json.append("{");
		String institutionName = institution.getAiname();
		if(institutionName!=null && institutionName.length()>0){
			if(institutionName.contains("\"")){
				institutionName = institutionName.replace("\"","'");
			}
			if(institutionName.contains(",")){
				institutionName.replace(",","%2C");
			}
			if(institutionName.contains("{")){
				institutionName.replace("{","%7B");
			}
			if(institutionName.contains("}")){
				institutionName.replace("}","%7D");
			}
			try {
				URLEncoder.encode(institutionName,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("Could not be parsed by URLEncode.encode function: "+e.getMessage());
			}
		}
		json.append("\"title\":\""+institutionName+"\",");
		json.append("\"key\":\""+institution.getInternalAlId()+"\"");
		if(institution.isGroup()){
			json.append(",\"isLazy\":\"true\"");
			json.append(",\"isFolder\":\"true\"");
			List<ArchivalInstitution> children = new LinkedList<ArchivalInstitution>(institution.getChildArchivalInstitutions());
			if(children!=null && children.size()>0){
				json.append(",\"children\":[");
				Iterator<ArchivalInstitution> childrenIt = children.iterator();
				while(childrenIt.hasNext()){
					ArchivalInstitution child = childrenIt.next();
					json.append(buildArchivalInstitutionJSON(child));
					if(childrenIt.hasNext()){
						json.append(",");
					}
				}
				json.append("]");
			}
		}
		json.append("}");
		return json;
	}

	/**
	 * Checks and work with an archival_institution structure.
	 * Tries to insert, update and delete institutions.
	 * 
	 * @param archivalInstitutions
	 * @return validOperation
	 */
	private String checkAndUpdateFromToDDBB(Collection<ArchivalInstitution> archivalInstitutions) {
		String validOperation = SUCCESS; //flag used to rollback the process when some rule is wrong
		Integer state = 0;
		if(archivalInstitutions!=null){
			try{
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
					if(validOperation.equalsIgnoreCase(SUCCESS)){
						JpaUtil.beginDatabaseTransaction();
						//when valid operation it's able to manage all ingested institutions/groups
						this.deletedInstitutions = new ArrayList<ArchivalInstitution>();
						
						this.totalInstitutions = this.aIDAO.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),true);
						
						checkAndUpdateArchivalInstitutions(null,archivalInstitutions); //new check
						log.debug("Updated process has been finished successfull");
						state = 3;

						log.debug("Inserting process for not updated institutions");
						//do an insert for rest file institutions
						String aiName = insertNotUpdatedInstitutions(archivalInstitutions,null);
						 if (aiName != null){
							 // this institution has any lang error and trans may be closed
							 state = 6;
							 log.debug("Institution has not lang");
							 validOperation = "errorLang";//LANG_ERROR
							 JpaUtil.rollbackDatabaseTransaction();
							 this.setAiArchivalInstitutionName(aiName);
							 return validOperation;
						 }
						log.debug("Done insert process!");
						state = 4;
//						//now delete all institutions which has not been updated (old institutions are not being processed)
						//institutions to be deleted = totalDDBBinstitution - (institutionsUpdated + institutionsDeleted)
						this.deletedInstitutions = new ArrayList<ArchivalInstitution>(); //clean
						boolean error = deleteSimpleUnusedInstitutions();
						if(!error){
							error = deleteSimpleUnusedGroups();
							state = 5;
							JpaUtil.commitDatabaseTransaction();
							//finally remove deleted files from ddbb if they existed
							state = 10;
						}else{
							state = 6;
							log.debug("Invalid operation detected. There could be content into some institution.");
							validOperation = ERROR_CONTENT_2;
							JpaUtil.rollbackDatabaseTransaction();
						}
					}
				}else{ //this case is for an ingestion on empty country, only tries to store the target structure 
					state = 7;
					this.insertedInstitutions = new ArrayList<ArchivalInstitution>();
					JpaUtil.beginDatabaseTransaction();
					state = 8;
					insertChildren(archivalInstitutions,null); //insert institution
					JpaUtil.commitDatabaseTransaction();
					state = 9;
				}
			}catch(Exception e){
				validOperation = ERROR;
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
	private boolean deleteSimpleUnusedGroups() {
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
				if(ArchivalLandscape.deleteContent(targetToBeDeleted)){
					//this.aIDAO.deleteSimple(targetToBeDeleted); //delete unused institution
					this.deletedInstitutions.add(targetToBeDeleted);
					log.debug("Deleted institution with aiId: "+targetToBeDeleted.getAiId());
				}
			}
		}
		return error;
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
		List<ArchivalInstitution> archivalInstitutionsPlainList = parseCollectionToPlainList(archivalInstitutions); //parse archivalInstitutions structure to plain identifiers list structure
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
							String pathToCurrentIngestedInstitution = buildParentsNode(currentIngestedInstitution);
							String pathToPlainInstitution = buildParentsNode(plainInstitution);
							if (!pathToCurrentIngestedInstitution.equals(pathToPlainInstitution)) {
								log.debug("The path for the current institution (" + pathToCurrentIngestedInstitution + ") hasn't the same name as the path for the institution to ingest (" + pathToPlainInstitution + ").");
								error = true;
							}

							// Checks if the element changed its position inside the group.
							if (currentIngestedInstitution.getAlorder() != plainInstitution.getAlorder()) {
								log.debug("The brothers for the current institution (" + currentIngestedInstitution.getAiname() + ") hasn't the same brothers for the institution to ingest (" + plainInstitution.getAiname() + ").");
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
	 * Method to construct the path from the current element to the root level.
	 *
	 * @param parentArchivalInstitution Element to construct the path.
	 * @return Path from the current element to root level.
	 */
	private String buildParentsNode(ArchivalInstitution parentArchivalInstitution) {
		StringBuffer parents = new StringBuffer();
		if(parentArchivalInstitution!=null){
			log.debug("Building hierarchy for current element: " + parentArchivalInstitution.getAiname());
			parents.append(parentArchivalInstitution.getInternalAlId());
			while(parentArchivalInstitution.getParentAiId()!=null){
				parentArchivalInstitution = parentArchivalInstitution.getParent();
				parents.append(",");
				parents.append(parentArchivalInstitution.getInternalAlId());
			}
			parents.append(",");
		}
		parents.append(SecurityContext.get().getCountryId());
		return parents.toString();
	}

	/**
	 * Insert not updated institutions. It should be the rest of the 
	 * param Collection<ArchivalInstitution> not updated.
	 * @param archivalInstitutions
	 */
	private String insertNotUpdatedInstitutions(Collection<ArchivalInstitution> archivalInstitutions,ArchivalInstitution parent) {
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
						insertInstitution(targetToBeInserted); //it's the recurse method, so it's not needed call to himself (insertNotUpdatedInstitutions)
					}catch(DashboardAPEnetException e){
						log.debug("Institution without lang: "+e.getMessage());
						strOut= e.getMessage();
						return strOut;
					}
				}else if(targetToBeInserted.isGroup() && targetToBeInserted.getChildArchivalInstitutions()!=null && targetToBeInserted.getChildArchivalInstitutions().size()>0){ //additional check children
					strOut=insertNotUpdatedInstitutions(targetToBeInserted.getChildArchivalInstitutions(),targetToBeInserted);
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
				if(ArchivalLandscape.deleteContent(targetToBeDeleted)){
					//this.aIDAO.deleteSimple(targetToBeDeleted); //delete unused institution
					this.deletedInstitutions.add(targetToBeDeleted);
					log.debug("Deleted institution with aiId: "+targetToBeDeleted.getAiId());
				}else{
					log.debug("NOT Deleted institution with aiId: "+targetToBeDeleted.getAiId()+" by content not deletable.");
					error = true;
					this.addInstitutionsWithContentNotPublished(targetToBeDeleted.getAiname());
				}
			}else if(!targetToBeDeleted.isGroup()){
				log.debug("Detected institution with content indexed, so it could not be deleted. Marked operation like invalid.");
				error = true; //flag used to check an error for rollback
			}
		}
		return error;
	}

	/**
	 * Parse a Collection<ArchivalInstitutions> currentStructure(Group1,Institution2,Group3,Institution4):
	 * 
	 * 1. - Group1
	 * 1.1 - Group1.1
	 * 1.1.1 - Institution1.1.1
	 * 2. - Institution2
	 * 3. - Group3
	 * 3.1 - Institution3.1
	 * 4. Institution4
	 * 
	 * to a plain List<ArchivalInstitution> allInstitutions:
	 * 
	 * (Group1,Group1.1,Institution1.1.1,Institution2,Group3,Institution3.1,Institution4).
	 * 
	 * @param archivalInstitutions
	 * @return List<ArchivalInstitution>
	 */
	private List<ArchivalInstitution> parseCollectionToPlainList(Collection<ArchivalInstitution> archivalInstitutions) {
		List<ArchivalInstitution> archivalInstitutionsPlainList = new ArrayList<ArchivalInstitution>();
		if(archivalInstitutions!=null){
			Iterator<ArchivalInstitution> it = archivalInstitutions.iterator();
			while(it.hasNext()){
				ArchivalInstitution institution = it.next();
				if(institution.isGroup()){
					log.debug("Group: "+institution.getAiname());
					if(institution.getChildArchivalInstitutions()!=null){
						archivalInstitutionsPlainList.addAll(checkChild(institution)); //call to get plain children
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
	/**
	 * This method is recursive, when it's used it has 2 management cases.
	 * 
	 * On the one hand is used when first time is called. It tries to get all parent institutions
	 * and next run into targets from parents to children.
	 * On the other hand there is the recursive method, institutionUpdate, to fill all targets.
	 * It's needed to be called each time by one institution (sibbling).
	 * 
	 * @param target
	 * @param archivalInstitutions
	 * @return boolean (found or not)
	 */
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
		int correction = 0;
		while(itParentInstitution.hasNext()){
			ArchivalInstitution currentParent = itParentInstitution.next();
			if(currentParent!=null){
				log.debug("Checking parent: "+currentParent.getAiname());
				//check parent
				currentParent.setAlorder(currentParent.getAlorder()-correction);
				found = institutionUpdate(archivalInstitutions,currentParent); //process to check recursively
				log.debug("Check done, found: "+found);
				//check child structure
				if(currentParent.isGroup()){
					//check each children first
					Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(currentParent.getChildArchivalInstitutions());
					if(children!=null && children.size()>0){
						log.debug("Checking children with size: "+children.size());
						checkParents(new ArrayList<ArchivalInstitution>(children),archivalInstitutions);
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
	private boolean institutionUpdate(Collection<ArchivalInstitution> archivalInstitutions, ArchivalInstitution target) {
		boolean found = false;
		Iterator<ArchivalInstitution> archivalInstitutionsIt = archivalInstitutions.iterator();
		while(!found && archivalInstitutionsIt.hasNext()){
			ArchivalInstitution archivalInstitution = archivalInstitutionsIt.next();
			if(archivalInstitution.getInternalAlId()!=null && archivalInstitution.getInternalAlId().equals(target.getInternalAlId())){ //found
				log.debug("Found institution equal: "+target.getAiname()+". Replacing node...");
				found = true;
				this.deletedInstitutions.add(target); //not needed for search afterwards
				replaceNode(target,archivalInstitution); //replace node
				log.debug("Replace done for institution: "+target.getAiname());
			}else if(archivalInstitution.isGroup()){//not found
				Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(archivalInstitution.getChildArchivalInstitutions());
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
	private boolean replaceNode(ArchivalInstitution oldDDBBInstitution,ArchivalInstitution updatedInstitution) {
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
		if(ArchivalLandscape.deleteContent(possibleDeletedInstitution)){
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
	private void insertChildren(Collection<ArchivalInstitution> archivalInstitutions,ArchivalInstitution parent) throws DashboardAPEnetException {
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
					insertChildren(institutionChildren,childParent);
				}else{
					insertInstitution(currentInstitution);
				}
			}else{
				insertInstitution(currentInstitution);
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
	private ArchivalInstitution insertInstitution(ArchivalInstitution currentInstitution) throws DashboardAPEnetException {
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
		
		ArchivalInstitution institution = this.aIDAO.insertSimple(currentInstitution);
		if(institution!=null && institution.isGroup()){
			if (this.groupsInsertedIntoDDBB == null) {
				this.groupsInsertedIntoDDBB = new HashMap<String,ArchivalInstitution>();
			}
			this.groupsInsertedIntoDDBB.put(institution.getInternalAlId(),institution);
		}
		//alternative names logic
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
						insertInstitution(childInstitutionToInsert);
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
	private Set<ArchivalInstitution> getInstitutionsByALFile(File archivalInstitutionFile) {
		Set<ArchivalInstitution> archivalInstitutions = null;
		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			XMLStreamReader r = factory.createXMLStreamReader(new FileReader(archivalInstitutionFile));
			archivalInstitutions = getXMLArchivalInstitutionLevel(r);
		} catch (FileNotFoundException e) {
			log.error("File not found :: "+archivalInstitutionFile.getAbsolutePath() + APEnetUtilities.generateThrowableLog(e));
		} catch (XMLStreamException e) {
			log.error("Archival Landscape reading exception: " + APEnetUtilities.generateThrowableLog(e));
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
	private Set<ArchivalInstitution> getXMLArchivalInstitutionLevel(XMLStreamReader r) throws XMLStreamException{
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
						if(openLevel && level!=null && level.equals(SERIES)){
							Set<ArchivalInstitution> children = getXMLArchivalInstitutionLevelChildren(r,archivalInstitution);
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
	 * Open a file, read it and get attribute "countrycode" from
	 * eadid node.
	 * 
	 * @param httpFile
	 * @return countryCode-String
	 */
	private String getXMLEadidCountrycode(File httpFile){
		String response = "";
		XMLStreamReader r = null;
		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			r = factory.createXMLStreamReader(new FileReader(httpFile));
			//start reading file
			boolean exit = false;
			boolean found = false;
			while(!exit && r.hasNext()){
				int event = r.next();
				switch (event) {
					case XMLStreamConstants.START_ELEMENT:
						//check if tag equals eadid
						if(r.getLocalName().equalsIgnoreCase(EADID)){
							//read attribute countrycode and put it into response
							int count = r.getAttributeCount();
							for(int i=0;!exit && i<count;i++){
								String attributeName = r.getAttributeLocalName(i);
								if(attributeName!=null && attributeName.equalsIgnoreCase(COUNTRYCODE)){
									response = r.getAttributeValue(i);
									exit = true;
								}
							}
						}
						break;
					case XMLStreamConstants.END_ELEMENT:
						if(found){
							exit = true;
						}
						break;
				}
			}
			//end reading file
		} catch (FileNotFoundException e) {
			log.error("File not found reading eadid :: "+httpFile.getAbsolutePath(), e.getCause());
		} catch (XMLStreamException e) {
			log.error("Archival Landscape exception reading eadid: ", e);
		} catch (Exception e){
			log.error("Exception reading eadid: ", e);
		} finally {
			if(r!=null){
				try {
					r.close();
				} catch (XMLStreamException e) {
					log.error("Archival Landscape reading exception", e.getCause());
				}
			}
		}
		return response;
	}
	
	/**
	 * Method which get all the series children, it's used to obtain recursive
	 * institution series children. 
	 * @throws XMLStreamException
	 */
	private Set<ArchivalInstitution> getXMLArchivalInstitutionLevelChildren(XMLStreamReader r, ArchivalInstitution aiParent) throws XMLStreamException {
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
							archivalInstitution.setChildArchivalInstitutions(new LinkedList<ArchivalInstitution>(children));
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
//		EadCreator eadCreator = null;
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
						eadContent.append("\n\t\t\t<did>");
							eadContent.append("\n\t\t\t\t<unittitle");
							eadContent.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
							eadContent.append(" type=\"eng\">");
							eadContent.append(AL_GLOBAL_UNITTITLE);
							eadContent.append("</unittitle>");
						eadContent.append("\n\t\t\t</did>");
						eadContent.append(openDsc());
							eadContent.append(buildCLevel(countryName,0)); //each archival institution is a C level
						eadContent.append(closeDsc());
					eadContent.append(closeArchDesc());
					
				eadContent.append(closeEadNode());
				outputStream.write(eadContent.toString().getBytes());
				eadContent = null;
			}
		} catch (Exception e){
			log.error("Unknown error into buildXMLFromDDBB",e);
		}finally {
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("Unknown error into buildXMLFromDDBB",e);
				}
			}
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
			cLevel.append(buildDidNode(null,alternativeNames,tabs));
		}
		ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> listCountryArchivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsByCountryIdForAL(country.getId(),true);
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
			Map<String,String> mainAlternativeName = new HashMap<String,String>();
			Iterator<AiAlternativeName> itAlternativeNames = aiAlternativeNames.iterator();
			while(itAlternativeNames.hasNext()){
				AiAlternativeName alternativeName = itAlternativeNames.next();
				if(!alternativeName.getPrimaryName()){
					alternativeNames.put(alternativeName.getLang().getIsoname(),alternativeName.getAiAName());
				}else{
					mainAlternativeName.put(alternativeName.getLang().getIsoname(),alternativeName.getAiAName());
				}
			}
			cLevel.append(buildDidNode(alternativeNames,mainAlternativeName,tabs));
		}
		Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(archivalInstitution.getChildArchivalInstitutions());
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

	private StringBuilder buildDidNode(Map<String, String> alternativeNames, Map<String, String> mainAlternativeName, String tabs) {
		StringBuilder didNode = new StringBuilder();
		//first make main alternative name, which it's the institution name
		if(mainAlternativeName!=null){
			didNode.append("\n"+tabs+"\t");
			didNode.append("<did>");
			Iterator<String> keyIterator = mainAlternativeName.keySet().iterator(); //should be only one
			while(keyIterator.hasNext()){ //this should only have one unique
				String key = keyIterator.next();
				didNode.append("\n"+tabs+"\t\t");
				didNode.append("<unittitle");
				didNode.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
				didNode.append(" type=\""+key.toLowerCase()+"\">");
				didNode.append(mainAlternativeName.get(key));
				didNode.append("</unittitle>");
			}
			if(alternativeNames!=null && alternativeNames.size()>0){
//				didNode.append("\n"+tabs+"\t");
//				didNode.append("<did>");
				//next make all alternative names
				Iterator<String> alternativeNamesIterator = alternativeNames.keySet().iterator();
				while(alternativeNamesIterator.hasNext()){
					String key = alternativeNamesIterator.next();
					didNode.append("\n"+tabs+"\t\t");
					didNode.append("<unittitle");
					didNode.append(" encodinganalog=\""+AL_GLOBAL_ENCODINGANALOG+"\"");
					didNode.append(" type=\""+key.toLowerCase()+"\">");
					didNode.append(alternativeNames.get(key));
					didNode.append("</unittitle>");
				}
//				didNode.append("\n"+tabs+"\t");
//				didNode.append("</did>");
			}
			didNode.append("\n"+tabs+"\t");
			didNode.append("</did>");
		}
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
		String countryCode = SecurityContext.get().getCountryIsoname();
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
