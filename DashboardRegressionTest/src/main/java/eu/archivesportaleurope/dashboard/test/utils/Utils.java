package eu.archivesportaleurope.dashboard.test.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

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

    public Connection getConnection() throws SQLException {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "rozdbAdmin");
        connectionProps.put("password", "R0zDBAdm1n");

        conn = DriverManager.getConnection("jdbc:postgresql://172.16.1.105:5432/roz-db", connectionProps);

        System.out.println("Connected to database");
        return conn;
    }

    public String[] getStatementsFromFile(String fileLocation) throws IOException, SQLException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(fileLocation).getFile());
        String stringSqlFromFile = new String(Files.readAllBytes(file.toPath())); // read entire file content into one string
        System.out.println("File read!");
        String[] statements = stringSqlFromFile.split(";"); // create an array with individual SQL statements
        System.out.println("Split into statements!");
        return statements;
    }

    public void executeSqlScript(String[] statements, Connection dbCon) throws SQLException {
        Statement myStatement = dbCon.createStatement(); // create a Statement object connected to the database connection
        System.out.println(statements.length);
        int number = statements.length; // determine the number of statements to be executed first

        for (int i = 0; i < number; i++) { // loop through the array to execute each individual statement
            String stringSql = statements[i] + ";";
            System.out.println(stringSql);
            try {
                int result = myStatement.executeUpdate(stringSql);
                System.out.println(result + " records affected");
            } catch (SQLException e) {
                System.out.println(e.toString());
            }

        } // end for loop

    } //

}
