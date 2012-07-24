package eu.apenet.dashboard.manual;

import eu.apenet.commons.utils.APEnetUtilities;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ManualFTPEADUploader extends ManualUploader {

    private final Logger log = Logger.getLogger(getClass());
    
	private String user;
	private String password;
	private String serverUrl;
	private Integer serverPort;

    public ManualFTPEADUploader(String user, String password, String serverUrl, Integer serverPort){
        this.user = user;
        this.password = password;
        this.serverUrl = serverUrl.replace("ftp://", "");
        if(serverPort != null)
            this.serverPort = serverPort;
        else
            this.serverPort = 21;
    }

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

/**
	@Override
	public Boolean upload() {
		return null;
	}
**/

    /**
     * Creates the connection (FTPClient) from the server to the remote FTP server
     * @return An FTPClient which contains the open connection with the FTP server (or null if can't connect)
     * @throws IOException If the connection is problematic 
     */
	public FTPClient establishConnection() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(this.serverUrl, this.serverPort);

        if(this.user != null)
            ftpClient.login(this.user, this.password);

        int reply = ftpClient.getReplyCode();

        ftpClient.enterRemotePassiveMode();
        ftpClient.enterLocalPassiveMode();

        if(FTPReply.isPositiveCompletion(reply)){
            log.debug("Connected to " + serverUrl + " server: " + ftpClient.getReplyString());
            return ftpClient;
        }
        ftpClient.disconnect();
        log.error("Connection refused by " + serverUrl + " server");
        return null;
	}

    /**
     * Download a file from the FTP server to the local server.
     * @param ftpClient An FTPClient containing the open connection
     * @param pathFile The path of the file to download within the FTP server's context
     * @param archivalInstitutionId The id used to create the full path for the saved file
     * @return The File that has been downloaded
     * @throws IOException If the connection is problematic
     */
	public File getFile(FTPClient ftpClient, String pathFile, int archivalInstitutionId) throws IOException{
		log.info("Downloading: " + pathFile);
        String file = pathFile.split("/")[pathFile.split("/").length - 1];
        log.debug("File is: " + file);
        String dir = pathFile.replace("/"+file, "");
        dir = dir.equals("")?"/":dir;
        log.debug("Dir is: " + dir);
        ftpClient.changeWorkingDirectory(dir);

        File dirToSave = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId);
        if(!dirToSave.exists())
            dirToSave.mkdir();
        File fileToSave = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + pathFile);

        OutputStream os = new FileOutputStream(fileToSave);
        if(ftpClient.retrieveFile(file, os))
            return fileToSave;
        return null;
	}

    /**
     * Disconnect the connection of the FTP server (Logout if necessary)
     * @param ftpClient FTPClient holding the open connection
     * @return A success boolean
     * @throws IOException If the connection is problematic
     */
    public boolean disconnectFTPServer(FTPClient ftpClient) throws IOException {
        boolean success = ftpClient.logout();
        if(ftpClient.isConnected())
            ftpClient.disconnect();
        return success;
    }

    /**
     * Reconnects to the FTP server
     * @return An FTPClient holding the open connection
     * @throws IOException If the connection is problematic
     */
    public FTPClient reconnectFTPServer() throws IOException {
        if(this.serverUrl == null)
            return null;
        return establishConnection();
    }

    /**
     * Get a list of FTPFile from the server containing XML files and directories
     * @param ftpClient An FTPClient holding the open connection
     * @param folder The path of the folder to retrieve files from
     * @return A list of FTPFile containing XML files and directories
     * @throws IOException If the connection is problematic
     */
    public List<FTPFile> getFTPFiles(FTPClient ftpClient, String folder) throws IOException {
        log.debug("Getting files");

        if(folder!=null)
            ftpClient.changeWorkingDirectory("/" + folder);
        
        log.debug("Working dir: " + ftpClient.printWorkingDirectory());

        FTPFile[] ftpFiles = ftpClient.listFiles();
        List<FTPFile> ftpFileList = new ArrayList<FTPFile>();

        for(FTPFile ftpFile : ftpFiles){
            if(ftpFile.getName().endsWith(".xml") || ftpFile.getName().endsWith(".zip") || (ftpFile.isDirectory() && !ftpFile.getName().equals(".") && !ftpFile.getName().equals("..")))
                ftpFileList.add(ftpFile);
        }

        log.debug("File list from server retrieved: " + ftpFileList.size() + " files");
        log.debug("Returning file names");
        return ftpFileList;
    }
	
	private Boolean checkFormat(){
		return null;
	}
	
}
