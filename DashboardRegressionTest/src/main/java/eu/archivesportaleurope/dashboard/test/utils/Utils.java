package eu.archivesportaleurope.dashboard.test.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.opencsv.CSVReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.logging.Logger;
import org.openqa.selenium.By;

public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    public static List<String[]> getFileData(String fileLocation) throws IOException {
        CSVReader reader = null;
        FileReader freader;
        freader = new FileReader(new File(Thread.currentThread().getContextClassLoader().getResource(fileLocation).getFile()));
        reader = new CSVReader(freader, ';');
        List<String[]> listAllTestData = reader.readAll();
        freader.close();
        reader.close();
        return listAllTestData;

    }

    public static String getElementXPath(WebDriver driver, WebElement element) {
        return (String) ((JavascriptExecutor) driver).executeScript("gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();", element);
    }

    public static String[] getStatementsFromFile(String fileLocation) throws IOException, SQLException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(fileLocation).getFile());
        String stringSqlFromFile = new String(Files.readAllBytes(file.toPath())); // read entire file content into one string
        System.out.println("File read!");
        String[] statements = stringSqlFromFile.split(";"); // create an array with individual SQL statements
        System.out.println("Split into statements!");
        return statements;
    }
//
    public static void uploadFile(WebDriver driver, String elementName, String fileName) throws URISyntaxException, IOException, InterruptedException {
        WebElement fileBrowser = driver.findElement(By.id(elementName));
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            logger.info("Windows file upload: "+fileName);
            fileBrowser.click();
            File file = new File(new URI(ClassLoader.getSystemResource(fileName).toExternalForm()));
            String path = file.getAbsolutePath();
            File script = new File(new URI(ClassLoader.getSystemResource("selenium/windows/uploadScript.exe").toExternalForm()));
            String scriptPath = script.getAbsolutePath();
            Runtime.getRuntime().exec(scriptPath+ " " + path);
            driver.switchTo().activeElement();
        } else {
            logger.info("Unix file upload "+fileName);
            fileBrowser.sendKeys(ClassLoader.getSystemResource(fileName).getPath());
        }
        Thread.sleep(7000);
    }
}
