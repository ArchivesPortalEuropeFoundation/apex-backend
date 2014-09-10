package eu.apenet.dashboard.manual;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
@Ignore
public class ManualFTPEADUploaderTest {
    private final Integer PORT = 7654;
    private final String user = "apenetftp";
    private final String pwd = "apenetftp";
    private final String serverUrl = "80.38.128.242";
    private final String serverUrlInternal = "192.168.1.32";
    private final String folderPath = "/dir1";
    private final String filePath = "/dir1/file2.xml";

    private ManualFTPEADUploader ftpUploader = null;
    private FTPClient ftpClient = null;

    @Before
    public void A_createUploader(){
        System.out.println("create");
        ftpUploader = new ManualFTPEADUploader(user, pwd, serverUrl, PORT);
        Assert.assertNotNull(ftpUploader);
        try {
            ftpClient = ftpUploader.establishConnection();
            Assert.assertNotNull("FTPClient is not null", ftpClient);
        } catch (IOException e){
            //First after the exception, we will try to connect using the internal network address
            try{
                ftpUploader = new ManualFTPEADUploader(user, pwd, serverUrlInternal, null);
                ftpClient = ftpUploader.establishConnection();
                Assert.assertNotNull("FTPClient is not null", ftpClient);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }
    }

    @Test
    public void B_reconnectFtp(){
        System.out.println("reconnect");
        E_disconnectFtp();
        try {
            ftpClient = ftpUploader.reconnectFTPServer();
            Assert.assertNotNull(ftpClient);
        } catch (IOException e){
            Assert.fail();
        }
    }

    @Test
    public void C_getListFiles(){
        System.out.println("get list");
        try {
            List<FTPFile> listFiles = ftpUploader.getFTPFiles(ftpClient, folderPath);
            Assert.assertEquals(listFiles.size(), 4);
        } catch (IOException e){
            Assert.fail();
        }
    }

    @Test
    public void D_downloadFile(){
        System.out.println("get file");
        try {
            File file = ftpUploader.getFile(ftpClient, filePath, filePath, 1);
            Assert.assertNotNull(file);
        } catch (IOException e){
            Assert.fail();
        }
    }

    @After
    public void E_disconnectFtp(){
        System.out.println("disconnect");
        boolean success = false;
        try {
            if(ftpClient == null)
                throw new IOException("ftpClient not initialized");
            success = ftpUploader.disconnectFTPServer(ftpClient);
            Assert.assertTrue(success);
        } catch(IOException e){
            Assert.assertFalse(success);
        }
    }
}