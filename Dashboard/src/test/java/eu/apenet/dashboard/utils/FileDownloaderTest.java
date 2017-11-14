/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mahbub
 */
public class FileDownloaderTest {
    
    public FileDownloaderTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getURLFileExtension method, of class FileDownloader.
     */
    @Test
    public void testGetURLFileExtension() throws MalformedURLException {
        System.out.println("getURLFileExtension");
//        System.out.println("File ext: "+fileName.get());
        URL url = new URL("https://localhost:8443/docs/RUNNING.txt?gsdg");
        String expResult = "txt";
        Optional<String> result = FileDownloader.getURLFileExtension(url);
        assertEquals(expResult, result.get());
    }

    /**
     * Test of getURLFileName method, of class FileDownloader.
     */
    @Test
    public void testGetURLFileName() throws MalformedURLException {
        System.out.println("getURLFileName");
        URL url = new URL("https://localhost:8443/docs/RUNNING.txt?gsdg");
        String expResult = "RUNNING.txt";
        Optional<String> result = FileDownloader.getURLFileName(url);
        assertEquals(expResult, result.get());
    }
    
    /**
     * Test of getURLFileName method, of class FileDownloader.
     */
    @Test(expected = NullPointerException.class)
    public void testGetURLFileNameNullUrl() throws MalformedURLException {
        System.out.println("testGetURLFileNameNullUrl");
        URL url = null;
        String expResult = "RUNNING.txt";
        Optional<String> result = FileDownloader.getURLFileName(url);
        assertEquals(expResult, result.get());
    }

    /**
     * Test of downloadFile method, of class FileDownloader.
     */
//    @Test
    public void testDownloadFile() throws Exception {
        System.out.println("downloadFile");
        String fileURL = "";
        String targetDirectory = "";
        Path expResult = null;
        Path result = FileDownloader.downloadFile(fileURL, targetDirectory);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
