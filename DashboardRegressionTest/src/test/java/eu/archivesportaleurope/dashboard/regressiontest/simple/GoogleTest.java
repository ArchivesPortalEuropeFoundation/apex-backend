/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest.simple;

import com.google.gson.Gson;
import eu.archivesportaleurope.dashboard.test.utils.DBManager;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import eu.archivesportaleurope.dashboard.test.utils.ScreenshotHelper;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author kaisar
 */
public class GoogleTest {

    private String baseUrl;
    private WebDriver driver;
    private ScreenshotHelper screenshotHelper;

    public GoogleTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        baseUrl = "http://www.google.com";
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
        screenshotHelper = new ScreenshotHelper();
    }

    @After
    public void tearDown() throws IOException {
        screenshotHelper.saveScreenshot("searchGoogle.png", driver);
        driver.quit();
    }

    //@Test
    public void test() {
        assertEquals("Check google Page title", "Google", driver.getTitle());

        WebElement searchField = driver.findElement(By.name("q"));
        searchField.sendKeys("archive portal europe");
        searchField.submit();
        assertTrue("The page title should start with the search string after the search.",
                (boolean) (new WebDriverWait(driver, 10)).until(new ExpectedCondition() {
                    @Override
                    public Boolean apply(Object f) {
                        WebDriver d = (WebDriver) f;
                        return d.getTitle().toLowerCase().startsWith("archive");
                    }

                }));
    }

    @Test
    public void testPost() throws MalformedURLException, IOException {

        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://development.archivesportaleurope.net/ApeApi/services/search/ead");

        String data = "{\n"
                + "  \"query\": \"*\",\n"
                + "  \"count\": 0,\n"
                + "  \"start\": 0\n"
                + "}";
        httpPost.setEntity(new StringEntity(data, ContentType.create("application/json", "UTF-8")));
        HttpResponse response = client.execute(httpPost);
        System.out.println(response.getStatusLine().getStatusCode());
        Gson gson = new Gson();
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream is = entity.getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
        }
    }

//    @Test
    public void testDB() throws SQLException, IOException {
        DBManager dbManager = DBManager.getInstance();
        dbManager.executeSqlScript(new String[]{""});
    }
}
