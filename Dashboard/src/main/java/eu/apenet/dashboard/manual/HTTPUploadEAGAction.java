package eu.apenet.dashboard.manual;

import java.io.File;
import java.util.List;

import eu.apenet.dashboard.AbstractInstitutionAction;

/**
 * User: Eloy García
 * Date: Oct 21, 2010
 *
 * @author Eloy García & Paul
 */
public class HTTPUploadEAGAction extends AbstractInstitutionAction {

	private static final long serialVersionUID = 5948857869592106742L;

	//Attributes
    private ManualHTTPUploader uploader_http;
    
    private String httpFileFileName; 		//The uploaded file name
    private File httpFile;					//The uploaded file
    private String httpFileContentType;		//The content type of the file uploaded

    private List<String> filesNotUploaded;	//This attribute contains all the files not uploaded because of they are already in the Dashboard (tmp directory) or their format is not allowed
    private List<String> filesUploaded;		//This attribute contains all the files uploaded

    private List<String> warnings_eag;
  
	//Getters and Setters
    public void setHttpFileFileName(String httpFileFileName) {
		this.httpFileFileName = httpFileFileName;
	}

	public String getHttpFileFileName() {
		return httpFileFileName;
	} 

	public void setHttpFile(File httpFile) {
		this.httpFile = httpFile;
	}

	public File getHttpFile() {
		return httpFile;
	}
	
	public void setHttpFileContentType(String httpFileContentType) {
		this.httpFileContentType = httpFileContentType;
	}

	public String getHttpFileContentType() {
		return httpFileContentType;
	}

    public List<String> getFilesNotUploaded() {
		return filesNotUploaded;
	}
    
    public List<String> getFilesUploaded() {
		return filesUploaded;
	}



    public List<String> getWarnings_eag(){
        return warnings_eag;
    }

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.eagupload"));
	}

	//Methods
    /**
     * Simple access to the upload page (no logic associated)
     * @return The code for the Struts2 dispatcher (Here only SUCCESS is returned)
     */
    @Override
    public String execute(){
    	
    	addActionMessage(getText("label.eag.eagfileselection"));
    	addActionMessage(getText("label.eag.warningeagfileuploading"));
       
    	return SUCCESS;
    }
	
	/**
     * Upload a File using HTTP protocol
     * @return The code used by Struts2 dispatcher
     */
    public String httpUpload(){

		//The ai_id is retrieved from the session
    	//This code has been moved to the prepare method
    	
    	//The user is uploading an EAG file, so uploadType will be EAG
    	String result = null;
    	String uploadType = "EAG";
    	String uploadMethod = "HTTP";
    	Integer archivalInstitutionId;
    	String format = null;
    	try{
    		Integer aiId = getAiId();
    		if (this.getHttpFile()!=null){
        		//The user has selected a file to upload
        		format = this.getHttpFileFileName().substring(this.getHttpFileFileName().lastIndexOf(".") + 1).toLowerCase();

                archivalInstitutionId = aiId;
                uploader_http = new ManualHTTPUploader(uploadMethod);
        	    result = uploader_http.uploadFile(uploadType, this.getHttpFileFileName(), this.getHttpFile(), format, archivalInstitutionId, uploadMethod);
        	    
        	    if (result.equals("success") || result.equals("success_with_url_warning")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
        	    	this.filesUploaded = this.uploader_http.getFilesUploaded();
        	    	addActionMessage(getText("label.eag.eagcorrectlyuploaded"));
        	    	if(result.equals("success_with_url_warning")){
        	    		addActionMessage(getText("label.eag.eagwithurlwarnings"));
        	    	}
        	    	result = SUCCESS;
        	    }
        	    else if (result.equals("success_noInformation")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.eagcorrectlyuploaded.noinformation.part1") + "'" + getText("label.ai.hg.information.content.default") + "' " + getText("label.eag.eagcorrectlyuploaded.noinformation.part2"));
        	    	addActionMessage(getText("label.eag.eagcorrectlyuploaded"));
                    result = SUCCESS;
        	    }
        	    else if (result.equals("error_eagalreadyuploaded")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.eagalreadyuploaded"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_formatnotallowed")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.one"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_eagnotstored")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.error"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_database")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.database"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_archivallandscape")){
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.three"));
                    result = ERROR;
        	    }
        	    else if (result.equals("error_eagnotvalidatednotconverted")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    warnings_eag = uploader_http.getWarnings_eag();
                    addActionMessage(getText("label.eag.uploadingerror.two"));
        	    	result = ERROR;
        	    }
        	    else if (result.equals("error_eagnotconverted")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.notconverted"));
        	    	result = ERROR;
        	    }
        	    else if (result.equals("error_parsing")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    addActionMessage(getText("label.eag.uploadingerror.parsing"));
        	    	result = ERROR;
        	    }
        	}
        	else {
        		//The user has not selected a file to upload
        		addActionMessage(getText("label.uploadfile"));
        		result = INPUT;
        	}
        	return result;
    	}catch(Exception e){
    		LOG.error("ERROR trying to upload a file ", e);
    	}
    	return ERROR;
    }
    
}
