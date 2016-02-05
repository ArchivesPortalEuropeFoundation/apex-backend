/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest.opendata;

import com.google.gson.Gson;
import eu.archivesportaleurope.dashboard.regressiontest.opendata.pojo.EadResponseSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import eu.archivesportaleurope.dashboard.test.utils.ScreenshotHelper;
import eu.archivesportaleurope.dashboard.test.utils.SolrUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author kaisar
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EnableOpenDataTest {

    private static String baseUrl;
    private static WebDriver driver;
    private static ScreenshotHelper screenshotHelper;
    private static final Properties properties = new Properties();
    private static HttpClient client;
    private static Gson gson;
    private static Logger logger = Logger.getLogger(EnableOpenDataTest.class.getName());

    public EnableOpenDataTest() {
    }

    @Rule
    public ErrorCollector errCollector = new ErrorCollector();
    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpClass() throws IOException, InterruptedException {
        logger.info("::: Setting up test environment :::");
        InputStream is = ClassLoader.getSystemResourceAsStream("config.properties");
        properties.load(is);
        baseUrl = properties.getProperty("baseUrl", "https://development.archivesportaleurope.net/Dashboard/");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
        screenshotHelper = new ScreenshotHelper();
        client = HttpClients.createDefault();
        gson = new Gson();
        logger.info("::: Removing Solr index :::");
        SolrUtils.getSolrUtil().clearAllCore();
    }

    @AfterClass
    public static void tearDownClass() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Home")));
        if (driver.getPageSource().contains("Switch back to")) {
            driver.findElement(By.partialLinkText("Switch back to")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout"))).click();
        } else {
            driver.findElement(By.partialLinkText("Logout")).click();
        }

        driver.quit();
        logger.info("::: Removing Solr index :::");
        SolrUtils.getSolrUtil().clearAllCore();
    }

    @Before
    public void openBrowser() throws IOException {

    }

    @After
    public void saveScreenshotAndCloseBrowser() throws IOException {
    }

    @Test
    public void testALoginWithAdmin() throws IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("login_label_login")));
        driver.findElement(By.id("username")).sendKeys(properties.getProperty("adminUserName", "Kaisar.Ali@nationaalarchief.nl"));
        driver.findElement(By.id("login_password")).sendKeys(properties.getProperty("adminPassword", "test2010"));
        driver.findElement(By.id("login_dropOtherSession")).click();
        element.click();
        Assert.assertEquals("DASHBOARD", driver.getTitle());
        captureScreen(name.getMethodName() + "_AdminLogin");
    }

    @Test
    public void testBCreateCountry() throws InterruptedException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement userManagementLink = wait.until(ExpectedConditions
                .elementToBeClickable(By.partialLinkText("User management")));
        userManagementLink.click();

        driver.navigate().back();
        WebElement createCountryLink = wait.until(ExpectedConditions
                .elementToBeClickable(By.partialLinkText("Create country")));
        createCountryLink.click();

        WebElement createCountry = wait.until(ExpectedConditions.elementToBeClickable(By.id("accept")));
        driver.findElement(By.id("storeCountry_englishCountryName"))
                .sendKeys(properties.getProperty("testCountryName", "TESTCOUNTRY"));
        driver.findElement(By.id("storeCountry_isoCountryName"))
                .sendKeys(properties.getProperty("testCountryISO", "TC"));
        createCountry.click();
        Thread.sleep(5000);
        try {
            if ("Country already stored in the system".equals(driver.findElement(By.id("storeCountry_")).findElement(By.tagName("li"))
                    .findElement(By.tagName("span")).getText())) {
                userManagementLink = wait.until(ExpectedConditions
                        .elementToBeClickable(By.partialLinkText("User management")));
                userManagementLink.click();
                Assert.assertNotNull(driver.findElement(By.partialLinkText(properties
                        .getProperty("testCountryName", "TESTCOUNTRY"))));
                Assert.fail("Country Already Created");
            }
        } catch (NoSuchElementException ex) {
            Logger.getLogger(EnableOpenDataTest.class.getName()).log(Level.FINE, null, ex);
        }
        userManagementLink = wait.until(ExpectedConditions
                .elementToBeClickable(By.partialLinkText("User management")));
        userManagementLink.click();
        wait.until(ExpectedConditions
                .elementToBeClickable(By.id("dashboard")));
        Assert.assertNotNull(driver.findElement(By.partialLinkText(properties
                .getProperty("testCountryName", "TESTCOUNTRY"))));
    }

    @Test
    public void testCCreateCountryManger() throws InterruptedException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement createCountrymanagerButton;
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(properties.getProperty("testCountryName", "TESTCOUNTRY"))));
            createCountrymanagerButton = driver.findElement(By
                    .partialLinkText(properties.getProperty("testCountryName", "TESTCOUNTRY")))
                    .findElement(By.xpath("../..")).findElement(By.id("displayCreateCountryManager_createCountryManager"));
            createCountrymanagerButton = wait.until(ExpectedConditions.elementToBeClickable(createCountrymanagerButton));
            createCountrymanagerButton.click();
            driver.findElement(By.id("createCountryManager_firstName"))
                    .sendKeys(properties.getProperty("countryManagerFirstName", "test"));
            driver.findElement(By.id("createCountryManager_lastName"))
                    .sendKeys(properties.getProperty("countryManagerLastName", "test"));
            driver.findElement(By.id("createCountryManager_email"))
                    .sendKeys(properties.getProperty("countryManagerEmail", "test@test.TC"));
            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By
                    .id("createCountryManager_okButton")));
            okButton.click();

            WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Home")));
            if (driver.getPageSource().contains("An Internal Server Error occurred")) {
                homeLink.click();
                wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("User management"))).click();
            }

            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(properties.getProperty("countryManagerEmail", "test@test.TC"))));

            Assert.assertEquals(properties.getProperty("countryManagerEmail", "test@test.TC"),
                    driver.findElement(By.partialLinkText(properties.getProperty("countryManagerEmail", "test@test.TC"))).getText());
        } catch (NoSuchElementException e) {
            Assert.fail("Country manager already exists, so : " + e.getMessage());
        }
    }

    @Test
    public void testDCreateArchivalInstitute() {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement changeToThisAccount, editArchivalLandscape, countryNameInTree,
                aiNameTextBox, addToTheList;
        try {
            changeToThisAccount = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By
                    .partialLinkText(properties.getProperty("testCountryName", "TESTCOUNTRY")))
                    .findElement(By.xpath("../..")).findElement(By.id("changeToCountryManager"))));
            changeToThisAccount.click();
            editArchivalLandscape = wait.until(ExpectedConditions.elementToBeClickable(driver
                    .findElement(By.partialLinkText("Edit archival landscape"))));
            editArchivalLandscape.click();
            Thread.sleep(5000);
            countryNameInTree = wait.until(ExpectedConditions.elementToBeClickable(driver
                    .findElement(By.partialLinkText(properties.getProperty("testCountryName", "TESTCOUNTRY").toLowerCase()))));
            countryNameInTree.click();

            Thread.sleep(5000);

            aiNameTextBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("textAL")));
            aiNameTextBox.sendKeys(properties.getProperty("aiName", "testAi"));
            addToTheList = wait.until(ExpectedConditions.elementToBeClickable(By.id("addDiv")));
            addToTheList.click();

            Thread.sleep(5000);

            Assert.assertEquals(properties.getProperty("aiName", "testAi"), driver
                    .findElement(By.partialLinkText(properties.getProperty("aiName", "testAi"))).getText());
        } catch (NoSuchElementException nEx) {

        } catch (InterruptedException ex) {
            Logger.getLogger(EnableOpenDataTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testDUploadEAG() throws InterruptedException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement manageContentLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Manage content")));
        manageContentLink.click();
        Select aiSelector = new Select(driver.findElement(By.id("Ai_selected")));
        aiSelector.selectByVisibleText(properties.getProperty("aiName", "TestArchivalInstitution"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("selectArchive_0"))).click();

        Assert.assertTrue(driver.getPageSource().contains("This is your first time to enter your dashboard. "
                + "It is mandatory to create a new EAG file or to upload an existing one in order to ingest "
                + "content into the Archives Portal Europe."));

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Upload EAG file"))).click();

        WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("uploadowneag_label_upload")));

        driver.findElement(By.id("uploadowneag_httpFile")).sendKeys(ClassLoader.getSystemResource("TC-00000000372.xml").getPath());
        uploadButton.click();
        Assert.assertTrue(driver.getPageSource().contains("Your EAG file has been uploaded correctly"));
    }

    @Test
    public void testEUploadFindingAid() throws InterruptedException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Upload content"))).click();
        WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("uploadButton")));
        driver.findElement(By.id("httpFile")).sendKeys(ClassLoader.getSystemResource("NL-HaNA_4.VTH.ead.xml").getPath());
        uploadButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkexistingfiles_label_accept"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
        Assert.assertTrue(driver.getPageSource().contains("4.VTH"));
    }

    @Test
    public void testFConvertValidatePublish() throws InterruptedException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement batchActionButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
        driver.findElement(By.id("check_1")).click();
        new Select(driver.findElement(By.id("batchSelectedAction"))).selectByValue("convert_validate_publish");
        batchActionButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
        Assert.assertTrue(driver.getPageSource().contains("Number of your files in the queue: 1, Queue size: 1"));
        while (!driver.getPageSource().contains("Queue size: 0")) {
            Thread.sleep(5000);
            driver.navigate().refresh();
        }
        Assert.assertTrue(driver.getPageSource().contains("Queue size: 0"));
    }

    @Test
    public void testGNoDataFromSolr() throws IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        HttpPost post = new HttpPost(properties.getProperty("apiBaseUrl", "http://localhost:9090/ApeApi/services/") + "search/ead");
        String data = "{\n"
                + "  \"query\": \"*\",\n"
                + "  \"count\": 0,\n"
                + "  \"start\": 0\n"
                + "}";
        post.setEntity(new StringEntity(data, ContentType.create("application/json", "UTF-8")));
        HttpResponse response = client.execute(post);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        EadResponseSet eads = gson.fromJson(IOUtils.toString(response.getEntity().getContent()), EadResponseSet.class);
        System.out.println("!!!!!!!!!!!" + eads.getTotalResults());
        Assert.assertEquals(0, eads.getTotalResults());
    }

    @Test
    public void testHEnableOpenData() throws InterruptedException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(properties.getProperty("aiName", "testAi")))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Manage open data"))).click();
        WebElement checkBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("enableOpenData")));
        Assert.assertTrue(driver.getPageSource().contains("Open data flag is disabled."));

        checkBox.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("submit"))).click();
        Thread.sleep(1000);

        Alert jsAlert = driver.switchTo().alert();
        jsAlert.accept();

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Switch back to"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(properties
                .getProperty("queueManagementLinkText", "Queue management")))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Home")));
        String aiName = driver.findElements(By.tagName("table")).get(3)
                .findElements(By.tagName("tr")).get(1)
                .findElements(By.tagName("td")).get(0).getText();
        String aiNameInQueueItemList = driver.findElements(By.tagName("table")).get(4)
                .findElements(By.tagName("tr")).get(1)
                .findElements(By.tagName("td")).get(2).getText();

        Assert.assertEquals(properties.getProperty("aiName", "testAi"), aiName);
        Assert.assertEquals(properties.getProperty("aiName", "testAi"), aiNameInQueueItemList);
    }

//    @Test
    public void testAEnableOpenDataFromInsManagerAccount() throws InterruptedException, IOException {
        Thread.sleep(1000);
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("login_label_login")));

            driver.findElement(By.id("username")).sendKeys(properties.getProperty("insManagerUserName", "k.ali@cimsolutions.nl"));
            driver.findElement(By.id("login_password")).sendKeys(properties.getProperty("insManagerPassword", "Test@2010"));
            driver.findElement(By.id("login_dropOtherSession")).click();
            element.click();
            //driver.findElement(By.id("login_label_login")).click();
            Thread.sleep(5000);

            captureScreen(name.getMethodName() + "_login");

            Select aiSelector = new Select(driver.findElement(By.id("Ai_selected")));
            aiSelector.selectByVisibleText(properties.getProperty("aiName", "TestArchivalInstitution"));
            driver.findElement(By.id("selectArchive_0")).click();

            Thread.sleep(5000);

            captureScreen(name.getMethodName() + "_home");

            driver.findElement(By.linkText(properties.getProperty("openDataLinkText", "Manage open data for API"))).click();

            Thread.sleep(5000);

            captureScreen(name.getMethodName() + "_openData");

            driver.findElement(By.id("enableOpenData")).click();
            Thread.sleep(1000);
            driver.findElement(By.id("submit")).click();
            Thread.sleep(1000);
            Alert jsAlert = driver.switchTo().alert();
            jsAlert.accept();

            Thread.sleep(5000);
            captureScreen(name.getMethodName() + "_changeOpenDataFlag");
            Thread.sleep(100);
            driver.findElement(By.linkText("Logout")).click();
            Thread.sleep(500);

            driver.findElement(By.id("username")).sendKeys(properties.getProperty("adminUserName", "Kaisar.Ali@nationaalarchief.nl"));
            driver.findElement(By.id("login_password")).sendKeys(properties.getProperty("adminPassword", "test2010"));
            driver.findElement(By.id("login_dropOtherSession")).click();
            driver.findElement(By.id("login_label_login")).click();
            Thread.sleep(1000);
            driver.findElement(By.linkText(properties.getProperty("queueManagementLinkText", "Queue management"))).click();
            Thread.sleep(1000);

            String aiName = driver.findElements(By.tagName("table")).get(3)
                    .findElements(By.tagName("tr")).get(1)
                    .findElements(By.tagName("td")).get(0).getText();
            String aiNameInQueueItemList = driver.findElements(By.tagName("table")).get(4)
                    .findElements(By.tagName("tr")).get(1)
                    .findElements(By.tagName("td")).get(2).getText();

            Assert.assertEquals(properties.getProperty("aiName", "TestArchivalInstitution"), aiName);
            Assert.assertEquals(properties.getProperty("aiName", "TestArchivalInstitution"), aiNameInQueueItemList);

        } catch (NoSuchElementException ex) {
            fail("No Such Element " + ex.getMessage());
        }
        try {
            Thread.sleep(1000);
        } catch (Throwable t) {
            fail("Expected AI was not found in the Queue!!!");
            errCollector.addError(t);
        }
    }

    private void captureScreen(String name) throws IOException {
        screenshotHelper.saveScreenshot("openData_" + name + ".png", driver);
        System.out.println("Screenshot succesfully made! openData_" + name + ".png");
    }

}
