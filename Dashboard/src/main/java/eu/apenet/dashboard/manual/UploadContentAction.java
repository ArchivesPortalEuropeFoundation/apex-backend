package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ctc.wstx.exc.WstxParsingException;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.utils.ZipManager;
import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.dao.UploadMethodDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.Ingestionprofile;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Yoann Moranville Date: Sep 2, 2010
 *
 * @author Yoann Moranville
 */
public class UploadContentAction extends AbstractInstitutionAction {

    private static final long serialVersionUID = -1321327143496576377L;
    private final Logger log = Logger.getLogger(getClass());

    private List<String> uploadType;
    private final static String HTTP = "HTTP";
    private final static String FTP = "FTP";

    private Set<SelectItem> ingestionprofiles = new LinkedHashSet<SelectItem>();
    private String ingestionprofile;

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
     * Prepare the Arrays for the radio button and the profilelist
     */
    public UploadContentAction() {
        super();
        uploadType = new ArrayList<String>();
        uploadType.add(HTTP);
        uploadType.add(FTP);
    }

    /**
     * Provides the default option for the radio button
     *
     * @return The default Radio button option
     */
    public String getUploadTypeChoice() {
        return HTTP;
    }

    public List<String> getUploadType() {
        return uploadType;
    }

    public void setUploadType(List<String> uploadType) {
        this.uploadType = uploadType;
    }

    public Set<SelectItem> getIngestionprofiles() {
        return ingestionprofiles;
    }

    public void setIngestionprofiles(Set<SelectItem> ingestionprofiles) {
        this.ingestionprofiles = ingestionprofiles;
    }

    public String getIngestionprofile() {
        return ingestionprofile;
    }

    public void setIngestionprofile(String ingestionprofile) {
        this.ingestionprofile = ingestionprofile;
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
        return ftpPort + "";
    }

    public void setFtpPort(String ftpPort) {
        try {
            this.ftpPort = Integer.parseInt(ftpPort);
        } catch (Exception e) {
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

    @Override
    public String execute() throws Exception {
        initializeProfileList();
        return SUCCESS;
    }

    public String connectFTP() {
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
            if (client != null) {
                session.put("ftpClient", client);
                initializeProfileList();
                return SUCCESS;
            } else {
                addActionMessage(getText("uploadContent.errUser"));
                return ERROR;
            }
        } catch (IOException ioe) {
            log.error("Could not connect to FTP server '" + ftpUrl + "'.", ioe);
            addActionMessage(getText("uploadContent.errFTP"));
            addActionError(ioe.getMessage());
            return ERROR;
        }
    }

    /**
     * AJAX call from the JSP page. Retrieves the data (files and directories) from the FTP server
     *
     * @return A JSON token containing the files and directories to be added to the tree view in the JSP
     */
    public String retrieveFtpData() {
        initializeProfileList();
        String UTF8 = "utf-8";
        try {
            getServletRequest().setCharacterEncoding(UTF8);
            getServletResponse().setCharacterEncoding(UTF8);
            getServletResponse().setContentType("application/json");
            Writer writer = new OutputStreamWriter(getServletResponse().getOutputStream(), UTF8);

            Map<String, Object> session = ActionContext.getContext().getSession();
            if (uploader_ftp == null) {
                uploader_ftp = (ManualFTPEADUploader) session.get("ftpUploader");
            }
            if (client == null) {
                client = (FTPClient) session.get("ftpClient");
            }

            String parentName = getServletRequest().getParameter("parentName");
            log.info("ParentName: " + parentName);

            List<FTPFile> listFiles = uploader_ftp.getFTPFiles(client, parentName);
            String s = createJSONString(listFiles, parentName);

            writer.append(s);
            writer.close();
        } catch (Exception e) {
            log.error("ERROR", e);
            addActionMessage(getText("uploadContent.errFTP"));
            return ERROR;
        }
        return null;
    }

    /**
     * Saves the select files from the FTP server to APEnet server
     *
     * @return The code for the Struts2 dispatcher
     */
    public String saveFtpFiles() {
    	String result = "";
        log.info("Save the FTP files");
        Integer aiId = getAiId();
        filesUploaded = new ArrayList<String>();
        filesNotUploaded = new ArrayList<String>();

        Map<String, Object> session = ActionContext.getContext().getSession();
        if (uploader_ftp == null) {
            uploader_ftp = (ManualFTPEADUploader) session.get("ftpUploader");
        }
        if (client == null) {
            client = (FTPClient) session.get("ftpClient");
        }
        if (filesToUpload == null) {
        	return "noFile";
        }
        for (String filePathOrig : filesToUpload) {
            String sourceFilePath = filePathOrig.substring(filePathOrig.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1);
            String filePath = APEnetUtilities.convertToFilename(sourceFilePath);
            try {
                String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
                uploader_ftp.getFile(client, sourceFilePath,filePath, aiId);
                if (fileType.equals("xml")) {
                	String status = createDBentry(filePath, "FTP");
                    if ("redirect".equalsIgnoreCase(status)) {
                    	result = status;
                    }
                } else if (fileType.equals("zip")) {
                    String filename = filePath.substring(0, filePath.lastIndexOf("."));
                    log.info("Filename: " + filename);
                    log.info("FilePath: " + filePath);
                    String storeFilePath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR;
                    String unzipZipPath = storeFilePath + "tmp" + APEnetUtilities.FILESEPARATOR;

                    log.info("storeFilePath: " + storeFilePath);
                    log.info("unzipZipPath: " + unzipZipPath);
                    File unzipZipDirFile = new File(unzipZipPath);
                    if (!unzipZipDirFile.exists()) {
                        unzipZipDirFile.mkdir();
                    }

                    ZipManager zipManager = new ZipManager(unzipZipPath);
                    zipManager.unzip(storeFilePath + filename + ".zip");
                    String[] files = unzipZipDirFile.list(new SuffixFileFilter("xml"));

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
                        	String status = createDBentry(file, "FTP", srcFile, destFile);
                            if ("redirect".equalsIgnoreCase(status)) {
                            	result = status;
                            }
                        }
                    }
                    try {
                        FileUtils.forceDelete(unzipZipDirFile);
                        FileUtils.forceDelete(new File(storeFilePath + filename + ".zip"));
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            } catch (IOException e) {
                filesNotUploaded.add(filePath);
                try {
                    if (uploader_ftp != null && client != null) {
                        uploader_ftp.disconnectFTPServer(client);
                    }
                } catch (IOException ioe) {
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

        if (result == null || result.isEmpty()) {
        	return Action.SUCCESS;
        } else {
        	return result;
        }
    }

    /**
     * Will create an entry to the database for the uploaded file;
     * if file is processed with profile, will directly add it to queue
     * or ask the user if a manual action is needed.
     *
     * @param filePath The current location of the saved file
     * @param uploadMethodString Depending if the file has been harvest or downloaded from an FTP server
     */
    private String createDBentry(String filePath, String uploadMethodString) {
        return createDBentry(filePath, uploadMethodString, null, null);
    }

    private String createDBentry(String filePath, String uploadMethodString, File srcFile, File destFile) {
    	String result = "profile";
        try {
            // Moving the file
            // Insert file uploaded into up_file table
            JpaUtil.beginDatabaseTransaction();

            UpFile upFile = new UpFile();
            UploadMethodDAO uploadMethodDao = DAOFactory.instance().getUploadMethodDAO();
            UploadMethod uploadMethod = uploadMethodDao.getUploadMethodByMethod(uploadMethodString);
            upFile.setUploadMethod(uploadMethod);
            upFile.setAiId(getAiId());
            upFile.setFileType(FileType.XML);
            upFile.setPath(APEnetUtilities.FILESEPARATOR + getAiId() + APEnetUtilities.FILESEPARATOR);
            upFile.setFilename(filePath);

            DAOFactory.instance().getUpFileDAO().insertSimple(upFile);

            // If method deals with files from a ZIP, move these from temporary location as given in method header
            if(srcFile != null && destFile != null){
                log.info("Moving file " + srcFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
                FileUtils.moveFile(srcFile, destFile);
            }
            
            if(StringUtils.isNotEmpty(filePath)){
            	this.filesUploaded.add(filePath);
            }

            JpaUtil.commitDatabaseTransaction();

            //If profile was added to upload, directly add file to queue in order to avoid delays for user
            if (ingestionprofile != null && !ingestionprofile.isEmpty()) {
                IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
                Ingestionprofile profile = profileDAO.findById(Long.parseLong(ingestionprofile));
                if (profile != null) {
                    // Check if the if the action for existing
                	// files is "ASK".
                	if (profile.getExistAction().isAsk()) {
                		// Call method to check if the file already exists.
                		return this.checkExistence(upFile, profile);
                	} else if (profile.getNoeadidAction().isAddLater()) {
                		// Call method to check if the file has not ID.
                		return this.checkIdExistence(upFile, profile);
                	} else {
                        processWithProfile(upFile, profile);
                	}
                }
            }
        } catch (Exception e) {
            log.error("Error inserting the file " + filePath + " in up_table (the user was uploading this file to the Dashboard) or error storing the file in temporal up repository [Database and FileSystem Rollback]. Error: " + e.getMessage());
            JpaUtil.rollbackDatabaseTransaction();
            // FileSystem Rollback //todo: For Inteco: Shouldn't it be destFile to be erased if it exists?
            if (srcFile.exists()) {
                try {
                    FileUtils.forceDelete(srcFile);
                } catch (IOException e1) {
                    log.error("It was not possible to remove the file " + srcFile.getAbsolutePath() + ". Error: " + e1.getMessage());
                }
            }
        }

        return result;
    }

    /**
     * Creates the JSON string to be displayed in the web page for FTP Client
     *
     * @param files A list of FTPFiles that contain all needed information to create a JSON string
     * @return The JSON string containing data to be displayed
     */
    private String createJSONString(List<FTPFile> files, String parentName) {
        JSONArray jsonArray = new JSONArray();
        try {
            if (files.size() == 0) {
                return null;
            }
            for (FTPFile file : files) {
                JSONObject obj = new JSONObject();
                obj.put("title", file.getName());
                if (file.isDirectory()) {
                    obj.put("isFolder", true);
                    obj.put("isLazy", true);
                    obj.put("hideCheckbox", true);
                }
                String userDir = uploader_ftp.getUserDir();
                if (StringUtils.contains(parentName, userDir)) {
                    obj.put("name", parentName + "/" + file.getName());
                } else {
                    obj.put("name", userDir + parentName + "/" + file.getName());
                }
                jsonArray.put(obj);
            }
        } catch (JSONException e) {
            log.error("Error", e);
        }
        return jsonArray.toString();
    }

    /**
     * Creates a path for the name of the tree objects, used to retrieve tree structure
     *
     * @param directory The directory path of the file
     * @param name The file name of the file
     * @return A string containing the path of the file in the tree structure
     */
    private static String createLink(String directory, String name) {
        if (directory.endsWith("/")) {
            return directory + name;
        }
        return directory + "/" + name;
    }

    /**
     * Upload a File using HTTP protocol
     *
     * @return The code used by Struts2 dispatcher
     */
    public String httpUpload() {

        //The user is executing Upload Content, so uploadType will be EAD
        String result = null;
        String uploadType = "EAD";
        String uploadMethod = "HTTP";
        String format = null;
        //IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        Ingestionprofile profile = null;
        if (ingestionprofile != null && !ingestionprofile.isEmpty()) {
            IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
            profile = profileDAO.findById(Long.parseLong(ingestionprofile));
        }
        try {
            Integer aiId = getAiId();
            if (this.getHttpFile() != null) {
                //The user has selected a file to upload
                format = this.getHttpFileFileName().substring(this.getHttpFileFileName().lastIndexOf(".") + 1).toLowerCase();
                uploader_http = new ManualHTTPUploader(uploadMethod);
                log.info("Starting uploadFile process for '" + this.getHttpFileFileName() + "' file name.");
                if (profile != null) {
                    result = uploader_http.uploadFile(uploadType, this.getHttpFileFileName(), this.getHttpFile(), format.toLowerCase(), aiId, uploadMethod, profile);
                } else {
                    result = uploader_http.uploadFile(uploadType, this.getHttpFileFileName(), this.getHttpFile(), format.toLowerCase(), aiId, uploadMethod);
                }
                if (result.equals("success")) {
                    this.filesNotUploaded = this.uploader_http.getFilesNotUploaded();
                    this.filesUploaded = this.uploader_http.getFilesUploaded();
                    if (filesNotUploaded.isEmpty() && filesUploaded.size() > 0) {
                        result = "redirect";
                    } else {
                        result = SUCCESS;
                    }
                } else if (result.equals("profile")) {
                    this.filesUploaded = this.uploader_http.getFilesUploaded();
                    result = "profile";
                } else if (result.equals("error")) {
                    result = ERROR;
                } else {
                    result = INPUT;
                }
            } else {
                //The user has not selected a file to upload
                result = "reload";
            }
            return result;
        } catch (Exception e) {
            log.error("ERROR trying to upload a file", e);
            addActionMessage(getText("uploadContent.errHTTP"));
        }
        return ERROR;
    }

    private static Properties retrieveProperties(Ingestionprofile ingestionprofile) {
        Properties properties = new Properties();
        properties.setProperty(UpFile.XML_TYPE, ingestionprofile.getFileType() + "");
        properties.setProperty(UpFile.NO_EADID_ACTION, ingestionprofile.getNoeadidAction().getId() + "");
        properties.setProperty(UpFile.EXIST_ACTION, ingestionprofile.getExistAction().getId() + "");
        properties.setProperty(UpFile.DAO_TYPE, ingestionprofile.getDaoType().getId() + "");
        properties.setProperty(UpFile.DAO_TYPE_CHECK, ingestionprofile.getDaoTypeFromFile() + "");
        properties.setProperty(UpFile.UPLOAD_ACTION, ingestionprofile.getUploadAction().getId() + "");
        properties.setProperty(UpFile.CONVERSION_TYPE, ingestionprofile.getEuropeanaConversionType() + "");
        properties.setProperty(UpFile.DATA_PROVIDER, ingestionprofile.getEuropeanaDataProvider() + "");
        properties.setProperty(UpFile.DATA_PROVIDER_CHECK, ingestionprofile.getEuropeanaDataProviderFromFile() + "");
        properties.setProperty(UpFile.EUROPEANA_DAO_TYPE, ingestionprofile.getEuropeanaDaoType() + "");
        properties.setProperty(UpFile.EUROPEANA_DAO_TYPE_CHECK, ingestionprofile.getEuropeanaDaoTypeFromFile() + "");
        properties.setProperty(UpFile.LANGUAGES, ingestionprofile.getEuropeanaLanguages() + "");
        properties.setProperty(UpFile.LANGUAGE_CHECK, ingestionprofile.getEuropeanaLanguagesFromFile() + "");
        properties.setProperty(UpFile.LICENSE, ingestionprofile.getEuropeanaLicense() + "");
        properties.setProperty(UpFile.LICENSE_DETAILS, ingestionprofile.getEuropeanaLicenseDetails() + "");
        properties.setProperty(UpFile.LICENSE_ADD_INFO, ingestionprofile.getEuropeanaAddRights() + "");
        properties.setProperty(UpFile.INHERIT_FILE_CHECK, ingestionprofile.getEuropeanaInheritElementsCheck()+"");
        properties.setProperty(UpFile.INHERIT_FILE, ingestionprofile.getEuropeanaInheritElements()+"");
        properties.setProperty(UpFile.INHERIT_ORIGINATION_CHECK, ingestionprofile.getEuropeanaInheritOriginCheck()+"");
        properties.setProperty(UpFile.INHERIT_ORIGINATION, ingestionprofile.getEuropeanaInheritOrigin()+"");
        return properties;
    }

    private void processWithProfile(UpFile upFile, Ingestionprofile profile) {
        Properties properties = retrieveProperties(profile);
        try {
            if(profile.getFileType() == 2){
                EacCpfService.useProfileAction(upFile, properties);
            } else {
                EadService.useProfileAction(upFile, properties);
            }
        } catch (Exception ex) {
            LOG.error("Failed when adding the new up files into the queue", ex);
        }
    }

    private void initializeProfileList() {
        ingestionprofiles.add(new SelectItem("", "---" + getText("ingestionprofiles.chooseprofile") + "---"));
        IngestionprofileDAO profileDAO = DAOFactory.instance().getIngestionprofileDAO();
        List<Ingestionprofile> queryResult = profileDAO.getIngestionprofiles(getAiId());
        if (queryResult != null && !queryResult.isEmpty()) {
            for (Ingestionprofile entry : queryResult) {
                ingestionprofiles.add(new SelectItem(Long.toString(entry.getId()), entry.getNameProfile()));
            }
        }
    }

    /**
     * Method to check if the uploaded file has the same id as other in the
     * system for the same institution.
     * 
     * @param upFile Uploaded file.
     * @param profile Profile to use.
     *
     * @return Action result.
     */
    private String checkExistence(UpFile upFile, Ingestionprofile profile) {
    	String result = "";
		String identifier = "";
        try {
			XmlType xmlType = XmlType.getType(profile.getFileType());

            String upFilePath = upFile.getPath() + upFile.getFilename();

			if (profile.getFileType() == 2) {
				identifier = ExistingFilesChecker.extractAttributeFromXML(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFilePath, "eac-cpf/control/recordId", null, true, true).trim();
			} else {
				identifier = ExistingFilesChecker.extractAttributeFromXML(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFilePath, "eadheader/eadid", null, true, false).trim();
			}

			if (StringUtils.isNotEmpty(identifier) && !"empty".equals(identifier) && !"error".equals(identifier)) {
				if (profile.getFileType() == 2) {
					EacCpf eacCpf = DAOFactory.instance().getEacCpfDAO().getEacCpfByIdentifier(upFile.getArchivalInstitution().getRepositorycode(), identifier);
					if (eacCpf != null) {
						result = "redirect";
					}
				} else {
					Ead ead = DAOFactory.instance().getEadDAO().getEadByEadid(xmlType.getEadClazz(), upFile.getAiId(), identifier);
					if (ead != null) {
						result = "redirect";
					}
				}
			}
		} catch (WstxParsingException e) {
            log.error("File was not correct XML, cause: " + e.getMessage());
		}

        if (result == null || result.isEmpty()) {
    		processWithProfile(upFile, profile);
    		return "profile";
        } else {
        	if (upFile != null) {
            	try {
                    JpaUtil.beginDatabaseTransaction();
        			upFile.setPreferences(writeProperties(retrieveProperties(profile)));
                    JpaUtil.commitDatabaseTransaction();
                } catch (Exception ex) {
                	log.error("Failed when adding the new up files into the upFile", ex);
                	JpaUtil.rollbackDatabaseTransaction();
                }
        	}
        	return result;
        }
    }

    /**
     * Method to check if the uploaded file has no id.
     * 
     * @param upFile Uploaded file.
     * @param profile Profile to use.
     *
     * @return Action result.
     */
    private String checkIdExistence(UpFile upFile, Ingestionprofile profile) {
    	String result = "";
		String identifier = "";
        try {

            String upFilePath = upFile.getPath() + upFile.getFilename();

			if (profile.getFileType() == 2) {
				identifier = ExistingFilesChecker.extractAttributeFromXML(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFilePath, "eac-cpf/control/recordId", null, true, true).trim();
			} else {
				identifier = ExistingFilesChecker.extractAttributeFromXML(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFilePath, "eadheader/eadid", null, true, false).trim();
			}

			if (StringUtils.isEmpty(identifier) || "empty".equals(identifier) || "error".equals(identifier)) {
				result = "redirect";
			}
		} catch (WstxParsingException e) {
            log.error("File was not correct XML, cause: " + e.getMessage());
		}

        if (result == null || result.isEmpty()) {
    		processWithProfile(upFile, profile);
    		return "profile";
        } else {
        	if (upFile != null) {
            	try {
                    JpaUtil.beginDatabaseTransaction();
        			upFile.setPreferences(writeProperties(retrieveProperties(profile)));
                    JpaUtil.commitDatabaseTransaction();
                } catch (Exception ex) {
                	log.error("Failed when adding the new up files into the upFile", ex);
                	JpaUtil.rollbackDatabaseTransaction();
                }
        	}
        	return result;
        }
    }

    /**
     * Method to parse the Properties passed to String value.
     *
     * @param properties The Properties to parse.
     *
     * @return The String representing the Properties.
     *
     * @throws IOException Exception while managing StringWriter.
     */
	private static String writeProperties(Properties properties) throws IOException {
		StringWriter stringWriter = new StringWriter();
		properties.store(stringWriter, "");
		String result = stringWriter.toString();
		stringWriter.flush();
		stringWriter.close();
		return result;
	}
}
