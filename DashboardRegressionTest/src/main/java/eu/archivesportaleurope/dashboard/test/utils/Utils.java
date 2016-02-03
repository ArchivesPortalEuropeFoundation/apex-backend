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
import java.nio.file.Files;

public class Utils {

    public List<String[]> getFileData(String fileLocation) throws IOException {
        CSVReader reader = null;
        FileReader freader;
        freader = new FileReader(new File(Thread.currentThread().getContextClassLoader().getResource(fileLocation).getFile()));
        reader = new CSVReader(freader, ';');
        List<String[]> listAllTestData = reader.readAll();
        freader.close();
        reader.close();
        return listAllTestData;

    }

    public String getElementXPath(WebDriver driver, WebElement element) {
        return (String) ((JavascriptExecutor) driver).executeScript("gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();", element);
    }

    public String[] getStatementsFromFile(String fileLocation) throws IOException, SQLException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(fileLocation).getFile());
        String stringSqlFromFile = new String(Files.readAllBytes(file.toPath())); // read entire file content into one string
        System.out.println("File read!");
        String[] statements = stringSqlFromFile.split(";"); // create an array with individual SQL statements
        System.out.println("Split into statements!");
        return statements;
    }
}
