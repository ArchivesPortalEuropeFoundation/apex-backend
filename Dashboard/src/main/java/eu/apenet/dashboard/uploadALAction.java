 package eu.apenet.dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.archivallandscape.ChangeAlIdentifiers;
import eu.apenet.dashboard.manual.ManualHTTPUploader;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;

 /**
 * User: Jara Alvarez
 * Date: Sep 20th, 2010
 *
 */

public class uploadALAction extends ActionSupport implements Preparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ManualHTTPUploader uploader_http;
	private List<String> filesNotUploaded;	//This attribute contains all the files not uploaded because of they are already in the Dashboard (tmp directory) or their format is not allowed
    private List<String> filesUploaded;		//This attribute contains all the files uploaded
    private String httpFileFileName; 		//The uploaded file name
    private File httpFile;					//The uploaded file
    private String httpFileContentType;		//The content type of the file uploaded
	private Boolean Overwrite = false;
	private final static Logger LOGGER = Logger.getLogger(uploadALAction.class);
	private List<FindingAid> fasDeleted = new ArrayList<FindingAid>();
	private List<HoldingsGuide> hgsDeleted = new ArrayList<HoldingsGuide>();
	private String fileName;
	private Long fileSize; //It's used to store the file download size
	private Boolean resultAL=null;
	private List<ArchivalInstitution> archivalInstitutionsToDelete= new ArrayList<ArchivalInstitution>();
    private List<ArchivalInstitution> archivalInstitutionsToInsert= new ArrayList<ArchivalInstitution>();
    private List<AiAlternativeName> archivalInstitutionsNameNotChanged;
    private List<AiAlternativeName> archivalInstitutionsNameChanged;
	private List<ArchivalInstitution> archivalInstitutionsParentNotChanged;
	private List<ArchivalInstitution> archivalInstitutionsParentChanged;
    private List<ArchivalInstitution> institutionList = new ArrayList<ArchivalInstitution>();
    
    private Country country = new Country();
	private List<String> identifier = new ArrayList<String>();
	private String identifierOld;
	private List<Integer> aiid;	    
    
	private Boolean checking= false;
	private Boolean changeidentifierchance = false;
	private Boolean cancel = false;
	
	
	public List<ArchivalInstitution> getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(List<ArchivalInstitution> institutionList) {
		this.institutionList = institutionList;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<String> getIdentifier() {
		return identifier;
	}

	public void setIdentifier(List<String> identifier) {
		this.identifier = identifier;
	}

	public String getIdentifierOld() {
		return identifierOld;
	}

	public void setIdentifierOld(String identifierOld) {
		this.identifierOld = identifierOld;
	}

	public List<Integer> getAiid() {
		return aiid;
	}

	public void setAiid(List<Integer> aiid) {
		this.aiid = aiid;
	}

	public List<FindingAid> getFasDeleted() {
		return fasDeleted;
	}

	public void setFasDeleted(List<FindingAid> fasDeleted) {
		this.fasDeleted = fasDeleted;
	}

	public List<HoldingsGuide> getHgsDeleted() {
		return hgsDeleted;
	}

	public void setHgsDeleted(List<HoldingsGuide> hgsDeleted) {
		this.hgsDeleted = hgsDeleted;
	}
		
	public List<ArchivalInstitution> getArchivalInstitutionsToDelete() {
		return archivalInstitutionsToDelete;
	}

	public void setArchivalInstitutionsToDelete(
			List<ArchivalInstitution> archivalInstitutionsToDelete) {
		this.archivalInstitutionsToDelete = archivalInstitutionsToDelete;
	}
	private InputStream inputStream;	
	

	public Boolean getOverwrite() {
		return Overwrite;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setOverwrite(Boolean overwrite) {
		this.Overwrite = overwrite;
	}

	public ManualHTTPUploader getUploader_http() {
		return uploader_http;
	}

	public void setUploader_http(ManualHTTPUploader uploader_http) {
		this.uploader_http = uploader_http;
	}

	public List<String> getFilesNotUploaded() {
		return filesNotUploaded;
	}

	public void setFilesNotUploaded(List<String> filesNotUploaded) {
		this.filesNotUploaded = filesNotUploaded;
	}

	public List<String> getFilesUploaded() {
		return filesUploaded;
	}

	public void setFilesUploaded(List<String> filesUploaded) {
		this.filesUploaded = filesUploaded;
	}

	public String getHttpFileFileName() {
		return httpFileFileName;
	}

	public void setHttpFileFileName(String httpFileFileName) {
		this.httpFileFileName = httpFileFileName;
	}

	public File getHttpFile() {
		return httpFile;
	}

	public void setHttpFile(File httpFile) {
		this.httpFile = httpFile;
	}

	public String getHttpFileContentType() {
		return httpFileContentType;
	}

	public void setHttpFileContentType(String httpFileContentType) {
		this.httpFileContentType = httpFileContentType;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	private List<Breadcrumb> breadcrumbRoute;
	
	
	public List<Breadcrumb> getBreadcrumbRoute(){
		return this.breadcrumbRoute;
	}
	
	public Long getFileSize() {
		return fileSize;
	}
	
	public List<ArchivalInstitution> getArchivalInstitutionsToInsert() {
		return archivalInstitutionsToInsert;
	}

	public void setArchivalInstitutionsToInsert(
			List<ArchivalInstitution> archivalInstitutionsToInsert) {
		this.archivalInstitutionsToInsert = archivalInstitutionsToInsert;
	}

	public Boolean getResultAL() {
		return resultAL;
	}

	public void setResultAL(Boolean resultAL) {
		this.resultAL = resultAL;
	}

	public Boolean getChecking() {
		return checking;
	}

	public void setChecking(Boolean checking) {
		this.checking = checking;
	}

	public Boolean getChangeidentifierchance() {
		return changeidentifierchance;
	}

	public void setChangeidentifierchance(Boolean changeidentifierchance) {
		this.changeidentifierchance = changeidentifierchance;
	}

	public List<AiAlternativeName> getArchivalInstitutionsNameNotChanged() {
		return archivalInstitutionsNameNotChanged;
	}

	public void setArchivalInstitutionsNameNotChanged(
			List<AiAlternativeName> archivalInstitutionsNameNotChanged) {
		this.archivalInstitutionsNameNotChanged = archivalInstitutionsNameNotChanged;
	}

	public Boolean getCancel() {
		return cancel;
	}

	public void setCancel(Boolean cancel) {
		this.cancel = cancel;
	}

	public List<AiAlternativeName> getArchivalInstitutionsNameChanged() {
		return archivalInstitutionsNameChanged;
	}

	public void setArchivalInstitutionsNameChanged(
			List<AiAlternativeName> archivalInstitutionsNameChanged) {
		this.archivalInstitutionsNameChanged = archivalInstitutionsNameChanged;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsParentNotChanged() {
		return archivalInstitutionsParentNotChanged;
	}

	public void setArchivalInstitutionsParentNotChanged(
			List<ArchivalInstitution> archivalInstitutionsParentNotChanged) {
		this.archivalInstitutionsParentNotChanged = archivalInstitutionsParentNotChanged;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsParentChanged() {
		return archivalInstitutionsParentChanged;
	}

	public void setArchivalInstitutionsParentChanged(
			List<ArchivalInstitution> archivalInstitutionsParentChanged) {
		this.archivalInstitutionsParentChanged = archivalInstitutionsParentChanged;
	}

	@Override
	public void validate() {
		super.validate();
		buildBreadcrumb();
	}

	private void buildBreadcrumb() {
		this.breadcrumbRoute = new ArrayList<Breadcrumb>();
		Breadcrumb breadcrumb = new Breadcrumb("index.action",getText("breadcrumb.section.dashboard"));
		this.breadcrumbRoute.add(breadcrumb);
		breadcrumb = new Breadcrumb(null,getText("breadcrumb.section.uploadAL"));
		this.breadcrumbRoute.add(breadcrumb);
	}

	public void prepare() throws Exception {
		
		CountryDAO countryDao = DAOFactory.instance().getCountryDAO();
		this.setCountry(countryDao.findById(SecurityContext.get().getCountryId()));
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		
		this.setInstitutionList(aiDao.getGroupsAndArchivalInstitutionsByCountryId(this.getCountry().getId(),"alorder", true ));
	}
	
	public String execute() throws Exception{

		return SUCCESS;		
	}
	
	   public String httpUploadAL(){
		   			 
	    	String result=null;
	    	String uploadType = "AL";
	    	String uploadMethod = "HTTP";
	    	ArchivalLandscape a = new ArchivalLandscape();
	    	String format;
	    	String readResult="";
	    	
	    	if (this.getHttpFileFileName()!=null)
	    	{
	    		format = this.getHttpFileFileName().substring(this.getHttpFileFileName().lastIndexOf(".") + 1).toLowerCase();
	    		
	    		//Check if the file is correct.
	    		readResult = a.read(this.getHttpFile(),"upload");
	    		if (readResult.equals("ERROR"))
	    			result = INPUT;
	    		else
	    		{
		    		//Upload it
		    		uploader_http = new ManualHTTPUploader(uploadMethod);
		    		result = uploader_http.uploadFile(uploadType, this.getHttpFileFileName(), this.getHttpFile(), format, null, uploadMethod);
			    
				    if (result.equals("success")) {
				    	result = ERROR; 
				    	try {
				    		File tempDir = new File (a.getmyPath(a.getmyCountry()) + "temp" + APEnetUtilities.FILESEPARATOR + this.getHttpFileFileName());
				    		if (!(tempDir.exists()))				    			
				    			tempDir.mkdir();
							FileUtils.copyFile(this.getHttpFile(), tempDir);
							LOGGER.info("The new file has been copied to the temp directory. ");
							this.setOverwrite(true);
							this.setResultAL(true);
							result = httpoverwriteAL();
							
						} catch (Exception e) {
							result = INPUT;
							LOGGER.error("Error in http uploading first step when temp directory creation. ", e);
						}
				    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
				    	this.filesUploaded = this.uploader_http.getFilesUploaded();
				    	a.storeOperation("Upload al");
				    }
				    else if (result.equals("error")) {
				    	result = ERROR;
				    }				    
				    else {
				    	result = INPUT;
				    }			        
	    		}
	    		return result;
		    
	    	//There's nothing to upload	    		        
	    	}else
	    	{
	    		addActionMessage(getText("label.uploadfile"));
	    		return "none";
	    	}
	   }
	  
	   //Overwrite or not the file selected to upload
	   public String httpoverwriteAL(){		   
	   
		   ArchivalLandscape a = new ArchivalLandscape();
		   String result=null;
		   File tmpDir = new File (a.getmyPath(a.getmyCountry()) + "temp" + APEnetUtilities.FILESEPARATOR);
		   this.filesNotUploaded = new ArrayList<String>();
		   this.filesUploaded = new ArrayList<String>();
		   this.archivalInstitutionsNameNotChanged= new ArrayList<AiAlternativeName>();
		   this.archivalInstitutionsNameChanged= new ArrayList<AiAlternativeName>();
		   this.archivalInstitutionsParentNotChanged= new ArrayList<ArchivalInstitution>();
		   this.archivalInstitutionsParentChanged = new ArrayList<ArchivalInstitution>();
		   boolean ddbbResult=true;	
		   Boolean execute=null;
		   
		   if (this.getOverwrite())
		   {
			   try {
				   
				   if (this.resultAL == null)
				   {   
				       if(a.checkIdentifiers(new File(a.getmyPath(a.getmyCountry()) + "temp" + APEnetUtilities.FILESEPARATOR+this.getHttpFileFileName())))
				       {
					       HibernateUtil.beginDatabaseTransaction();			       
							
					       uploader_http = new ManualHTTPUploader("AL");			 
						   String pathCountry = a.getmyPath(a.getmyCountry());	
						   execute = false;
						   result = uploader_http.overWriteFile(this.httpFile, this.getHttpFileFileName(), pathCountry, execute);	
						   if (result.equals("error"))
							   ddbbResult = false;
						   else
						   {
							   this.filesUploaded = this.uploader_http.getFilesUploaded();
							   addActionMessage(getText("al.message.filetoupload") + this.filesUploaded);
							   int changes = -1;
						  
							   if (uploader_http.getArchivalInstitutionsNameNotChanged().size()>0)
							   {
								   this.setArchivalInstitutionsNameNotChanged(uploader_http.getArchivalInstitutionsNameNotChanged());								   
								   changes++;
								   this.setCancel(true);
							   }
							   else if (uploader_http.getArchivalInstitutionsParentNotChanged().size()>0){
								   this.setArchivalInstitutionsParentNotChanged(uploader_http.getArchivalInstitutionsParentNotChanged());
								   changes++;
								   this.setCancel(true);
							   }
							   else
							   {  								   
								   if (uploader_http.getArchivalInstitutionsToDelete().size()>0)
								   {									   
									   this.setArchivalInstitutionsToDelete(uploader_http.getArchivalInstitutionsToDelete());
									   addActionMessage(getText("al.message.warningintstutionstodelete.files"));
									   addActionMessage(getText("al.message.warningintstutionstodelete.manager"));
									   this.setChangeidentifierchance(true);
									   changes++;
								   }
								   if (uploader_http.getArchivalInstitutionsToInsert().size()>0)
								   {
									   this.setArchivalInstitutionsToInsert(uploader_http.getArchivalInstitutionsToInsert());
									   changes++;
								   }
								   if (uploader_http.getArchivalInstitutionsNameChanged().size()>0)
								   {
									   this.setArchivalInstitutionsNameChanged(uploader_http.getArchivalInstitutionsNameChanged());
									   changes++;
								   }
								   if (uploader_http.getArchivalInstitutionsParentChanged().size()>0)
								   {
									   this.setArchivalInstitutionsParentChanged(uploader_http.getArchivalInstitutionsParentChanged());
									   changes++;
								   }
							   }
							   if (changes==-1)
								   addActionMessage(getText("al.message.error.noInstitutions"));
							   result = INPUT;
							   ddbbResult = false;
						   }
				       }else
				       {
				    	   result= ERROR;
				    	   addActionMessage(getText("al.message.error.equalidentifiers"));
				       }
				   }else if (this.resultAL)
				   {
					   execute = true;
					   //Set the eadid in the xml if it has not it
				       if (!a.checkEadid(new File(a.getmyPath(a.getmyCountry()) + "temp" + APEnetUtilities.FILESEPARATOR+this.getHttpFileFileName())))
				       {   
				    	   //The eadid has NOT been changed successfully		    	   
				    	   LOGGER.error("uploadALAction: There was a problem when the EADID was assigned to your file");
				       }else 
				    	   LOGGER.debug("uploadALAction: The EADID in the archival landscape of the country " + a.getmyCountry() + " is correct");
					   
				       if(a.checkIdentifiers(new File(a.getmyPath(a.getmyCountry()) + "temp" + APEnetUtilities.FILESEPARATOR+this.getHttpFileFileName())))
				       {
					       HibernateUtil.beginDatabaseTransaction();			       
							
					       uploader_http = new ManualHTTPUploader("AL");			 
						   String pathCountry = a.getmyPath(a.getmyCountry());						   
						   result = uploader_http.overWriteFile(this.httpFile, this.getHttpFileFileName(), pathCountry, execute);	
						   if (result.equals("error"))
							   ddbbResult = false;
						   else
						   {	
							   this.filesUploaded = this.uploader_http.getFilesUploaded();		
							   a.storeOperation("Overwrite al");
							   
							   //The final commits
							   HibernateUtil.commitDatabaseTransaction();							  
						   }
				       }else
				       {
				    	   result= ERROR;
				    	   addActionMessage(getText("al.message.error.equalidentifiers"));
				       }					   
				   }
				} catch (Exception e) {
					result=ERROR;
					ddbbResult=false;
					LOGGER.error("The new archival landscape of " + a.getmyCountry() + " could not be overwrited requested by upload AL process", e);
				}finally{

					String pathCountryAL = a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL.xml";					
					String pathCountryALOld = a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL_old.xml";		
					String tmpDirectory =a.getmyPath(a.getmyCountry()) + "temp" + APEnetUtilities.FILESEPARATOR + this.getHttpFileFileName();
					
					if (!ddbbResult)
					{
						Boolean rollbackFiles = false;
						if (this.resultAL == null)
							LOGGER.debug("Rollbacking the transaction process, the user has to check data first");
						else						   
							LOGGER.error("Some operation was not correct in overwriting the AL of the " + a.getmyCountry()+ ". Rollbacking the whole transaction process");
						
						try {
							this.setArchivalInstitutionsToDelete(uploader_http.getArchivalInstitutionsToDelete());
							HibernateUtil.rollbackDatabaseTransaction();
							HibernateUtil.closeDatabaseSession();
							
							//To rollback the repository changes.
							//The AL file for this country created in overwriting process is deleted and put the older one
							File newFile = new File(pathCountryAL);
							if (this.resultAL == null)
							{
								File tempDirectory = new File(tmpDirectory);
								tempDirectory.mkdir();
								FileUtils.copyFile(newFile, tempDirectory);	
							}		
							
							Thread.sleep(500);
							
							Boolean resultDelete = newFile.delete();
							if (!resultDelete)
								FileUtils.forceDelete(newFile);
							
							File oldFile = new File(pathCountryALOld);
							oldFile.renameTo(new File(pathCountryAL));
							
							//The general AL file created need to be changed again updating with the former version
							a.changeAL();
							
							//The repository files renamed to _old must be changed to the former name for rollbacking
							for (int i=0; i< this.archivalInstitutionsToDelete.size();i++) {
								//Rename the files in tmp folder
								String tmpDirOldPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId() + "_old";
								String tmpDirPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId();
								File  tmpDirFile = new File (tmpDirOldPath);
								if (tmpDirFile.exists()) {
									LOGGER.debug("Renaming the tmp folder from the repository related the institution: " + this.archivalInstitutionsToDelete.get(i));
									tmpDirFile.renameTo(new File(tmpDirPath));		
								}
											
								//Rename the files in Country Repo folder
								String repoDirOldPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId() + "_old";
								String repoDirPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId();
								File repoDirFile = new File(repoDirOldPath);
								if (repoDirFile.exists()){
								
									LOGGER.debug("Renaming the repo folder from the repository related to the institution: " + this.archivalInstitutionsToDelete.get(i));
									repoDirFile.renameTo(new File(repoDirPath));
								}
							}

							
						} catch (Exception e) {
							LOGGER.error("FATAL ERROR. The rollback of index or in repository could not be done successfully. A manually review of the AL of: " + a.getmyCountry() + " must be done");
						}
						if (result != INPUT)
							addActionMessage(getText("al.message.error.overwrite"));
					}else
					{	
						try{
							this.setArchivalInstitutionsToDelete(uploader_http.getArchivalInstitutionsToDelete());
							//Delete the temporary file country AL created in overwriting
							File oldFile = new File(pathCountryALOld);
							oldFile.delete();
							//Delete the repository files							
							for (int i=0; i< this.archivalInstitutionsToDelete.size();i++)
							{
								//Delete the files in tmp folder
								String tmpDirOldPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId() + "_old";
								File  tmpDirFile = new File (tmpDirOldPath);
								if (tmpDirFile.exists()) {
									LOGGER.debug("Renaming the tmp folder from the repository related the institution: " + this.archivalInstitutionsToDelete.get(i));
									FileUtils.forceDelete(tmpDirFile);		
								}
											
								//Rename the files in Country Repo folder
								String repoDirOldPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId() + "_old";
								File repoDirFile = new File(repoDirOldPath);
								if (repoDirFile.exists()){
								
									LOGGER.debug("Renaming the repo folder from the repository related to the institution: " + this.archivalInstitutionsToDelete.get(i));
									FileUtils.forceDelete(repoDirFile);
								}
							}
							
						}catch(Exception e){
							LOGGER.error("The temporary file" + pathCountryALOld + " or the ones related to the institutions: " + this.archivalInstitutionsToDelete + " could not have been deleted. It should remove it manually", e);
						}						
					}
				}
		       
		       return result;
		   } else
			{
			   try {
				   FileUtils.deleteDirectory(tmpDir);
				   if (this.getHttpFileFileName()!=null)
					   this.filesNotUploaded.add(this.getHttpFileFileName());
			   } catch (IOException e) {
				   LOGGER.error("The files in " + tmpDir + "could not be deleted");
			   }
			   return "error";
			}
	   }
	   
	   public String downloadAL(){
		   
		  ArchivalLandscape al = new ArchivalLandscape();
   		   		   
			try {
				String pathCountry = al.getmyPath(al.getmyCountry());
	            
	            //if the xml of this country does not exist, error            
	            File[] files = new File(pathCountry).listFiles();    	
	        	if (files.length==0) 
	        	{ 
	        		return "error";
	        	}
				else
				{
					File tempFile = new File(pathCountry + al.getmyCountry() + "AL.xml");
					this.inputStream = new FileInputStream(tempFile);
					this.httpFileFileName = tempFile.getName();
					this.fileName = al.getmyCountry() + "AL.xml";
	                this.fileSize = tempFile.length();
				}
			} catch (Exception e){
				LOGGER.error(e.getMessage());
			}
			al.storeOperation("Download al");
			return al.download(inputStream);		   
	   }



////    public String downloadAL_fromDB() {
////        ArchivalLandscape al = new ArchivalLandscape();
////        try {
////            StringWriter eadArchilvaLandscapeWriter = new StringWriter();
////            CreateArchivalLandscapeEad createArchivalLandscapeEad = new CreateArchivalLandscapeEad(eadArchilvaLandscapeWriter);
////
////            CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
////            Country country = countryDAO.findById(al.getCountryId());
////            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
////
////            createArchivalLandscapeEad.createEadContentData("Archives Portal Europe - Archival Landscape", "AL-"+country.getIsoname(), country.getIsoname(), null);
////
////            createArchivalLandscapeEad.addInsideEad(country);
////            List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsByCountryId(country.getId());
////            for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
////                if(archivalInstitution.getParentAiId() == null) {
////                    createArchivalLandscapeEad.addInsideEad(archivalInstitution);
////                    recurenceLoop(createArchivalLandscapeEad, archivalInstitution);
////                    createArchivalLandscapeEad.writeEndElement(); //close each C element for each main archival institution
////                }
////            }
////            createArchivalLandscapeEad.writeEndElement(); //close each C element for each country
////
////            createArchivalLandscapeEad.closeEndFile();
////            eadArchilvaLandscapeWriter.close();
////
////            inputStream = (IOUtils.toInputStream(eadArchilvaLandscapeWriter.toString()));
////            fileName = "AL.xml";
////
////            al.storeOperation("Download al");
////            return al.download(inputStream);
////        } catch (Exception e) {
////            LOGGER.error("Error downloading the local AL EAD for " + country.getCname(), e);
////            return ERROR;
////        }
////    }
//
//    public void recurenceLoop(CreateArchivalLandscapeEad createArchivalLandscapeEad, ArchivalInstitution archivalInstitution) throws XMLStreamException {
//        List<ArchivalInstitution> archivalInstitutionChildren = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionsByParentAiId(archivalInstitution.getAiId());
//        for(ArchivalInstitution archivalInstitutionChild : archivalInstitutionChildren) {
//            createArchivalLandscapeEad.addInsideEad(archivalInstitutionChild);
//            recurenceLoop(createArchivalLandscapeEad, archivalInstitutionChild);
//            createArchivalLandscapeEad.writeEndElement();
//        }
//    }
//
//

	   public String changeAlIdFromUpload()   {
			String result = null;
			ChangeAlIdentifiers cAlId = new ChangeAlIdentifiers();
			ArchivalLandscape al = new ArchivalLandscape();
			
			if ((this.getIdentifier()!= null)) 
			{
				try{
					for (int i=0; i<this.getIdentifier().size();i++)
					{
						HibernateUtil.beginDatabaseTransaction();
						ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
						ArchivalInstitution ai = new ArchivalInstitution();
						
						ai = aiDao.findById(this.getAiid().get(i));	
						this.setIdentifierOld(ai.getInternalAlId());				
						if (!(this.getIdentifier().get(i).trim().equals(this.getIdentifierOld().trim())))
						{
							//Checking of the unique identifiers
							String available = cAlId.checkIdentifierAvailability(this.getInstitutionList(), this.getIdentifier().get(i), ai);
							if (!(available.equals("success")))
							{
								addActionMessage(ai.getAiname() + ":   " + getText("al.message.changeIdentifier.alreadyUsed"));
								this.setChecking(true);
								result= INPUT;
							}else
							{
								String ddbbChanged = cAlId.changeIdentifierinDDBB(ai, this.getIdentifier().get(i));
								if (!ddbbChanged.equals("success"))
								{
									addActionMessage(ai.getAiname() + ":   " + getText("al.message.changeIdentifier.error"));
									this.setChecking(true);
									result = ERROR;
								}
								else{
									String path = al.getmyPath(al.getmyCountry()) + al.getmyCountry() + "AL.xml";
									
									String resultChangeXml = cAlId.changeIdentifierinAlNode(this.getIdentifierOld(),this.getIdentifier().get(i), path);				
									if (resultChangeXml.equals(SUCCESS)){				
										al.changeAL();
										result = SUCCESS;
										addActionMessage(ai.getAiname() + ":   " + getText("al.message.changeIdentifier.identifierChanged"));
										HibernateUtil.commitDatabaseTransaction();
										HibernateUtil.closeDatabaseSession();										
										this.setChecking(true);
									}
									else
									{
										addActionMessage(ai.getAiname() + ":   " + getText("al.message.changeIdentifier.error"));
										this.setChecking(true);
										result = ERROR;
									}									
								}
							}
						}
						else
						{
							addActionMessage(ai.getAiname() + ":   " + getText("al.message.changeIdentifier.identifierEqual"));
							this.setChecking(true);
							result = INPUT;	
						}											
					}//End-for					
					
				}catch(Exception e){
					LOGGER.error(e.getMessage());
					addActionMessage(getText(this.getIdentifier()+ ":   " + "al.message.changeIdentifier.error"));
					result = ERROR;				
				}finally{
					if (result.equals(ERROR)){
						try{
								LOGGER.debug("Rollbacking the changing AL identifiers process");
			
								HibernateUtil.rollbackDatabaseTransaction();
								HibernateUtil.closeDatabaseSession();
							
								for (int i=0; i<this.getIdentifier().size();i++)
								{
									//Rollback the changing of the xml files
									String path = al.getmyPath(al.getmyCountry()) + "tmp/" + al.getmyCountry() + "AL.xml";
									String resultChangeXml = cAlId.changeIdentifierinAlNode(this.getIdentifier().get(1),this.getIdentifierOld(),path);
									if (resultChangeXml.equals(SUCCESS))
										al.changeAL();
									else
										LOGGER.warn("The changing identifier " + this.getIdentifierOld() + " into " + this.getIdentifier().get(i) + " in the storeIdentifier() rollback could not be done properly. Please review them manually.");
								}
						}
						catch(Exception e){						
							LOGGER.error("Error in rollbacking the changing archival landscape identifiers process. Please review manually.");
							LOGGER.error(e.getMessage());
							LOGGER.error(e.getStackTrace());
						}
					}
				}
			
			}else
				result = INPUT;		
			
			return result;
		}
}