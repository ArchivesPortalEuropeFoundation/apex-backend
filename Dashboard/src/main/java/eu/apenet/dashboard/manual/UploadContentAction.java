package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.utils.ZipManager;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.FileTypeDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.dao.UploadMethodDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;

/**
 * User: Yoann Moranville
 * Date: Sep 2, 2010
 *
 * @author Yoann Moranville
 */
public class UploadContentAction extends AbstractInstitutionAction implements ServletRequestAware, ServletResponseAware {
 
	private static final long serialVersionUID = -1321327143496576377L;
	private final Logger log = Logger.getLogger(getClass());

    private HttpServletResponse response;
    private HttpServletRequest request;

    private List<String> uploadType;
    private final static String HTTP = "HTTP";
    private final static String FTP = "FTP";
    private final static String OAI = "OAI-PMH";
    

    private Integer sessionId; 
    
    private List<String> oaiType;

    private String ftpUrl;
    private String ftpUser;
    private String ftpPwd;
    private int ftpPort;

    private String oaiUrl;
    private String oaiSet;
    private String oaiMetadata;
    private String oaiToken;

    private String[] filesToUpload;

    private ManualHTTPUploader uploader_http;

	private ManualFTPEADUploader uploader_ftp;
    private ManualOAIPMHEADUploader uploader_oai;
    private FTPClient client;
    
    private String httpFileFileName; 		//The uploaded file name
    private File httpFile;					//The uploaded file
    private String httpFileContentType;		//The content type of the file uploaded

    private List<String> filesNotUploaded;	//This attribute contains all the files not uploaded because of they are already in the Dashboard (tmp directory) or their format is not allowed
    private List<String> filesUploaded;		//This attribute contains all the files uploaded

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.uploadcontent"));
	}
  
	


	/**
     * Prepare the Array for the radio button
     */
    public UploadContentAction(){
        uploadType = new ArrayList<String>();
        uploadType.add(HTTP);
        uploadType.add(FTP);
        uploadType.add(OAI);

        oaiType = new ArrayList<String>();
        oaiType.add(AjaxControllerAbstractAction.FI_TYPE);
        oaiType.add(AjaxControllerAbstractAction.PT_TYPE);
    }

    /**
     * Provides the default option for the radio button
     * @return The default Radio button option
     */
    public String getUploadTypeChoice(){
        return HTTP;
    }

    public List<String> getUploadType(){
        return uploadType;
    }
    public void setUploadType(List<String> uploadType){
        this.uploadType = uploadType;
    }

    public List<String> getOaiType() {
        return oaiType;
    }

    public void setOaiType(List<String> oaiType) {
        this.oaiType = oaiType;
    }

    public String getFtpUrl() {
        return ftpUrl;
    }

    public void setFtpUrl(String ftpUrl) {
        this.ftpUrl = ftpUrl;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPwd() {
        return ftpPwd;
    }

    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd;
    }

    public String getFtpPort() {
        return ftpPort+"";
    }

    public void setFtpPort(String ftpPort) {
        try {
            this.ftpPort = Integer.parseInt(ftpPort);
        } catch (Exception e){
            this.ftpPort = 21;
        }
    }

    public String getOaiUrl() {
        return oaiUrl;
    }

    public void setOaiUrl(String oaiUrl) {
        this.oaiUrl = oaiUrl;
    }

    public String getOaiSet() {
        return oaiSet;
    }

    public void setOaiSet(String oaiSet) {
        this.oaiSet = oaiSet;
    }

    public String getOaiMetadata() {
        return oaiMetadata;
    }

    public void setOaiMetadata(String oaiMetadata) {
        this.oaiMetadata = oaiMetadata;
    }

    public String getOaiToken() {
        return oaiToken;
    }

    public void setOaiToken(String oaiToken) {
        this.oaiToken = oaiToken;
    }

    public String[] getFilesToUpload() {
        return filesToUpload;
    }

    public void setFilesToUpload(String[] filesToUpload) {
        this.filesToUpload = filesToUpload;
    }

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


    
    public String connectFTP(){
        log.debug(ftpUrl + " - " + ftpPort + " - " + ftpUser + " - " + ftpPwd);
        Map<String, Object> session = ActionContext.getContext().getSession();
        session.put("ftpUrl", ftpUrl);
        session.put("ftpUser", ftpUser);
        session.put("ftpPwd", ftpPwd);
        session.put("ftpPort", ftpPort);
        uploader_ftp = new ManualFTPEADUploader(ftpUser, ftpPwd, ftpUrl, ftpPort);
        session.put("ftpUploader", uploader_ftp);
        try {
            client = uploader_ftp.establishConnection();
            session.put("ftpClient", client);
            return SUCCESS;
        } catch (IOException ioe){
            log.error("Could not connect to FTP server '" + ftpUrl + "'.", ioe);
            return ERROR;
        }
    }

    /**
     * Harvest the data from a source and to a temporary file
     * @return The code used by Struts2 dispatcher
     */
    public String harvestData(){
        filesUploaded = new ArrayList<String>();
        filesNotUploaded = new ArrayList<String>();
        log.trace(oaiUrl + " - " + oaiSet + " - " + oaiMetadata);
        uploader_oai = new ManualOAIPMHEADUploader(oaiUrl, oaiMetadata, oaiSet, null, null);
        //Start harvest here:
        String filename = "harvest_" + oaiSet + ".xml";
        File harvestResult = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + filename);
        String token = "";
        try {
            OutputStream out = new FileOutputStream(harvestResult);
            token = uploader_oai.harvestBegin(out);
            while(token != null)
                token = uploader_oai.harvesting(out, token);
	    	filesUploaded.add(filename);
            createDBentry(filename, "OAI-PMH");
            return SUCCESS;
        } catch (Exception e) {
            filesNotUploaded.add(filename);
            log.error("Harvest failed, last token was: " + token);
            return ERROR;
        }
    }


    /**
     * AJAX call from the JSP page. Retrieves the data (files and directories) from the FTP server
     * @return A JSON token containing the files and directories to be added to the tree view in the JSP
     */
    public String retrieveFtpData(){
        String UTF8 = "utf-8";
        try {
            request.setCharacterEncoding(UTF8);
            response.setCharacterEncoding(UTF8);
            response.setContentType("application/json");
            Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);

            Map<String, Object> session = ActionContext.getContext().getSession();
            if(uploader_ftp == null)
                uploader_ftp = (ManualFTPEADUploader) session.get("ftpUploader");
            if(client == null)
                client = (FTPClient) session.get("ftpClient");

            String parentName = request.getParameter("parentName");
            log.debug("ParentName: " + parentName);

            List<FTPFile> listFiles = uploader_ftp.getFTPFiles(client, parentName);
            String s = createJSONString(listFiles, parentName);

            writer.append(s);
            writer.close();
        } catch (Exception e){
            log.error("ERROR", e);
        }
        return null;
    }

    /**
     * Saves the select files from the FTP server to APEnet server
     * @return The code for the Struts2 dispatcher
     */
    public String saveFtpFiles(){
        log.info("Save the FTP files");
        Integer aiId = getAiId();
        filesUploaded = new ArrayList<String>();
        filesNotUploaded = new ArrayList<String>();

        Map<String, Object> session = ActionContext.getContext().getSession();
        if(uploader_ftp == null)
            uploader_ftp = (ManualFTPEADUploader) session.get("ftpUploader");
        if(client == null)
            client = (FTPClient) session.get("ftpClient");
        for(String filePath : filesToUpload) {
            try {
                filePath = filePath.substring(filePath.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1);
                String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
                uploader_ftp.getFile(client, filePath, aiId);
                if(fileType.equals("xml")){
                    filesUploaded.add(filePath);
                    createDBentry(filePath, "FTP");
                }
                else if(fileType.equals("zip")){
                    String filename = filePath.substring(0, filePath.lastIndexOf("."));
                    log.info("Filename: " + filename);
                    log.info("FilePath: " + filePath);
                    String storeFilePath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR;
                    String unzipZipPath = storeFilePath + "tmp" + APEnetUtilities.FILESEPARATOR;

                    log.info("storeFilePath: " + storeFilePath);
                    log.info("unzipZipPath: " + unzipZipPath);
                    File unzipZipDirFile = new File(unzipZipPath);
                    if(!unzipZipDirFile.exists())
                        unzipZipDirFile.mkdir();

                    ZipManager zipManager = new ZipManager(unzipZipPath);
                    zipManager.unzip(storeFilePath + filename + ".zip");
                    String[] files = unzipZipDirFile.list( new SuffixFileFilter("xml"));

                    /****/
                    for (String file : files) {
                        File srcFile = new File(unzipZipPath + file);
                        File destFile = new File(storeFilePath + file);
                        if (destFile.exists()) {
                            filesNotUploaded.add(file);
                            try {
                                FileUtils.forceDelete(srcFile);
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        } else {
                            try {
                                FileUtils.copyFile(srcFile, destFile);
                                FileUtils.forceDelete(srcFile);
                                filesUploaded.add(file);

                                createDBentry(file, "FTP");

                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        }
                    }
                    /****/

                }
            } catch (IOException e){
                filesNotUploaded.add(filePath);
                try {
                    if(uploader_ftp != null && client != null)
                        uploader_ftp.disconnectFTPServer(client);
                } catch (IOException ioe){
                    log.error(ioe);
                    return NONE;
                }
                log.error(e);
                return NONE;
            }
        }
        try {
            uploader_ftp.disconnectFTPServer(client);
        } catch (IOException e) {
            log.error(e);
            return NONE;
        }
        return SUCCESS;
    }

    /**
     * Will create an entry to the database for the uploaded file
     * @param filePath The current location of the saved file
     * @param uploadMethodString Depending if the file has been harvest or downloaded from an FTP server
     */
    private void createDBentry(String filePath, String uploadMethodString){
    	Integer aiId = getAiId();
        UpFile upFile = new UpFile();

        UpFileDAO upFileDao = DAOFactory.instance().getUpFileDAO();
        upFile.setPath(APEnetUtilities.FILESEPARATOR + "up" + APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR + filePath);

        UploadMethodDAO uploadMethodDao = DAOFactory.instance().getUploadMethodDAO();
        UploadMethod uploadMethod = uploadMethodDao.getUploadMethodByMethod(uploadMethodString);
        upFile.setUploadMethod(uploadMethod);

        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aiId);
        upFile.setArchivalInstitution(archivalInstitution);

        FileTypeDAO fileTypeDao = DAOFactory.instance().getFileTypeDAO();
        FileType fileType = fileTypeDao.getFileTypeByType("xml");
        upFile.setFileType(fileType);

        upFile.setFname(filePath);
        upFileDao.store(upFile);
    }

    /**
     * Creates the JSON string to be displayed in the web page for FTP Client
     * @param files A list of FTPFiles that contain all needed information to create a JSON string
     * @return The JSON string containing data to be displayed
     */
    private String createJSONString(List<FTPFile> files, String parentName){
        JSONArray jsonArray = new JSONArray();
        try {
            if(files.size() == 0){
                return null;
            }
            for(FTPFile file : files){
                JSONObject obj = new JSONObject();
                obj.put("title", file.getName());
                if(file.isDirectory()){
                    obj.put("isFolder", true);
                    obj.put("isLazy", true);
                    obj.put("hideCheckbox", true);
                }
                obj.put("name", parentName + "/" + file.getName());
                jsonArray.put(obj);
            }
        } catch (JSONException e){
            log.error("Error", e);
        }
        log.info(jsonArray.toString());
        return jsonArray.toString();
    }

    /**
     * Creates a path for the name of the tree objects, used to retrieve tree structure
     * @param directory The directory path of the file
     * @param name The file name of the file
     * @return A string containing the path of the file in the tree structure 
     */
    private static String createLink(String directory, String name){
        if(directory.endsWith("/"))
            return directory + name;
        return directory + "/" + name;
    }
    
    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }
    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    /**
     * Upload a File using HTTP protocol
     * @return The code used by Struts2 dispatcher
     */
    public String httpUpload(){

    	//The user is executing Upload Content, so uploadType will be EAD
    	String result = null;
    	String uploadType = "EAD";
    	String uploadMethod = "HTTP";
    	String format = null;
    	try{
    		Integer aiId = getAiId();
        	if (this.getHttpFile()!=null){
        		//The user has selected a file to upload
        		format = this.getHttpFileFileName().substring(this.getHttpFileFileName().lastIndexOf(".") + 1).toLowerCase();
                uploader_http = new ManualHTTPUploader(uploadMethod);
                log.info("Starting uploadFile process for '"+this.getHttpFileFileName()+"' file name.");
        	    result = uploader_http.uploadFile(uploadType, this.getHttpFileFileName(), this.getHttpFile(), format.toLowerCase(), aiId, uploadMethod);
        	    if (result.equals("success")) {
        	    	this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
        	    	this.filesUploaded = this.uploader_http.getFilesUploaded();
        	    	result = SUCCESS;
        	    }
        	    else if (result.equals("error")) {
        	    	result = ERROR;
        	    }
        	    else {
        	    	result = INPUT;
        	    }
        	}
        	else {
        		//The user has not selected a file to upload
        		result = "reload";
        	}
            return result;
    	}catch(Exception e){
    		log.error("ERROR trying to upload a file",e);
    	}
    	return ERROR;
    }
    
}
