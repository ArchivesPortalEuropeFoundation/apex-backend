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
import eu.archivesportaleurope.dashboard.test.utils.Utils;
import java.awt.AWTException;
import java.net.URISyntaxException;
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
import org.openqa.selenium.TimeoutException;
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
    private static final int MAX_WAIT = 360000;
    private static final int MAX_TIMEOUT = 120;
    private static WebDriver driver;
    private static ScreenshotHelper screenshotHelper;
    private static final Properties properties = new Properties();
    private static HttpClient client;
    private static Gson gson;
    private static final Logger logger = Logger.getLogger(EnableOpenDataTest.class.getName());

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
        baseUrl = properties.getProperty("baseUrl", "https://test.archivesportaleurope.net/Dashboard/");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
        screenshotHelper = new ScreenshotHelper();
        client = HttpClients.createDefault();
        gson = new Gson();
//        logger.info("::: Removing Solr index :::");
//        SolrUtils.getSolrUtil().clearAllCore();
    }

    @AfterClass
    public static void tearDownClass() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Home")));
        if (driver.getPageSource().contains("Switch back to")) {
            driver.findElement(By.partialLinkText("Switch back to")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout"))).click();
        } else {
            driver.findElement(By.partialLinkText("Logout")).click();
        }

        driver.quit();
    }

    @Before
    public void openBrowser() throws IOException {

    }

    @After
    public void saveScreenshotAndCloseBrowser() throws IOException, InterruptedException {
        captureScreen(name.getMethodName());
    }

    @Test
    public void testALoginWithAdmin() throws IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        try {
            WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("login_label_login")));
            driver.findElement(By.id("username")).sendKeys(properties.getProperty("adminUserName", "Kaisar.Ali@nationaalarchief.nl"));
            driver.findElement(By.id("login_password")).sendKeys(properties.getProperty("adminPassword", "test2010"));
            driver.findElement(By.id("login_dropOtherSession")).click();
            element.click();
            Assert.assertEquals("DASHBOARD", driver.getTitle());
            Thread.sleep(1);
            captureScreen(name.getMethodName() + "_AdminLogin");
            logger.info("::: Logged in successful :::");
        } catch (NoSuchElementException | TimeoutException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        } catch (InterruptedException ex) {
            logger.log(Level.INFO, "Thread sleep exception.");
        }
    }

    @Test
    public void testBCreateCountry() throws IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        try {
            WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
            WebElement userManagementLink = wait.until(ExpectedConditions
                    .elementToBeClickable(By.partialLinkText("User management")));
            userManagementLink.click();
            captureScreen(name.getMethodName() + "_User_Management");

            driver.navigate().back();
            WebElement createCountryLink = wait.until(ExpectedConditions
                    .elementToBeClickable(By.partialLinkText("Create country")));
            createCountryLink.click();
            captureScreen(name.getMethodName() + "_create_country");

            WebElement createCountry = wait.until(ExpectedConditions.elementToBeClickable(By.id("accept")));
            driver.findElement(By.id("storeCountry_englishCountryName"))
                    .sendKeys(properties.getProperty("testCountryName", "TESTCOUNTRY"));
            driver.findElement(By.id("storeCountry_isoCountryName"))
                    .sendKeys(properties.getProperty("testCountryISO", "TC"));
            createCountry.click();
            Thread.sleep(5000);

            captureScreen(name.getMethodName() + "_after_create_country");

            userManagementLink = wait.until(ExpectedConditions
                    .elementToBeClickable(By.partialLinkText("User management")));
            userManagementLink.click();
            wait.until(ExpectedConditions
                    .elementToBeClickable(By.id("dashboard")));
            Assert.assertNotNull(driver.findElement(By.partialLinkText(properties
                    .getProperty("testCountryName", "TESTCOUNTRY"))));

            logger.info("::: Country created successfully :::");
        } catch (NoSuchElementException | TimeoutException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        } catch (InterruptedException ex) {
            logger.log(Level.INFO, "Thread sleep exception.");
        }
    }

    @Test
    public void testCCreateCountryManger() throws IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        WebElement createCountrymanagerButton;
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(properties.getProperty("testCountryName", "TESTCOUNTRY"))));
            createCountrymanagerButton = driver.findElement(By
                    .partialLinkText(properties.getProperty("testCountryName", "TESTCOUNTRY")))
                    .findElement(By.xpath("../..")).findElement(By.id("displayCreateCountryManager_createCountryManager"));
            createCountrymanagerButton = wait.until(ExpectedConditions.elementToBeClickable(createCountrymanagerButton));
            captureScreen(name.getMethodName() + "_create_country_manager_page");
            createCountrymanagerButton.click();
            Thread.sleep(1);
            captureScreen(name.getMethodName() + "_create_country_manager");
            driver.findElement(By.id("createCountryManager_firstName"))
                    .sendKeys(properties.getProperty("countryManagerFirstName", "test"));
            driver.findElement(By.id("createCountryManager_lastName"))
                    .sendKeys(properties.getProperty("countryManagerLastName", "test"));
            driver.findElement(By.id("createCountryManager_email"))
                    .sendKeys(properties.getProperty("countryManagerEmail", "test@test.TC"));
            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By
                    .id("createCountryManager_okButton")));
            okButton.click();
            captureScreen(name.getMethodName() + "_after_create_country_manager");

            WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Home")));
            if (driver.getPageSource().contains("An Internal Server Error occurred")) {
                logger.info("::: Internal server error occurred due to mail server not configured :::");
                homeLink.click();
                wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("User management"))).click();
            }

            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(properties.getProperty("countryManagerEmail", "test@test.TC"))));

            Assert.assertEquals(properties.getProperty("countryManagerEmail", "test@test.TC"),
                    driver.findElement(By.partialLinkText(properties.getProperty("countryManagerEmail", "test@test.TC"))).getText());

            logger.info("::: Country manager created successfully :::");
        } catch (NoSuchElementException | TimeoutException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        } catch (InterruptedException ex) {
            logger.log(Level.INFO, "Thread sleep exception.");
        }
    }

    @Test
    public void testDCreateArchivalInstitute() throws IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        WebElement changeToThisAccount, editArchivalLandscape, countryNameInTree,
                aiNameTextBox, addToTheList;
        try {
            changeToThisAccount = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By
                    .partialLinkText(properties.getProperty("testCountryName", "TESTCOUNTRY")))
                    .findElement(By.xpath("../..")).findElement(By.id("changeToCountryManager"))));
            changeToThisAccount.click();
            captureScreen(name.getMethodName() + "_create_test_AI");
            editArchivalLandscape = wait.until(ExpectedConditions.elementToBeClickable(driver
                    .findElement(By.partialLinkText("Edit archival landscape"))));
            editArchivalLandscape.click();
            captureScreen(name.getMethodName() + "_AI_landscape");
            Thread.sleep(5000);
            countryNameInTree = wait.until(ExpectedConditions.elementToBeClickable(driver
                    .findElement(By.partialLinkText(properties.getProperty("testCountryName", "TESTCOUNTRY").toLowerCase()))));
            countryNameInTree.click();
            captureScreen(name.getMethodName() + "_create_AI_landscape_page");

            Thread.sleep(5000);

            aiNameTextBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("textAL")));
            aiNameTextBox.sendKeys(properties.getProperty("aiName", "testAi"));
            addToTheList = wait.until(ExpectedConditions.elementToBeClickable(By.id("addDiv")));
            addToTheList.click();

            Thread.sleep(5000);
            captureScreen(name.getMethodName() + "after_AI_landscape_created");

            Assert.assertEquals(properties.getProperty("aiName", "testAi"), driver
                    .findElement(By.partialLinkText(properties.getProperty("aiName", "testAi"))).getText());

            logger.info("::: Archival Institute created successfully :::");
        } catch (NoSuchElementException | TimeoutException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        } catch (InterruptedException ex) {
            logger.log(Level.INFO, "Thread sleep exception.");
        }
    }

    @Test
    public void testDUploadEAG() throws InterruptedException, AWTException, IOException, URISyntaxException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        try {
            WebElement manageContentLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Manage content")));
            manageContentLink.click();
            WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("selectArchive_0")));
            Select aiSelector = new Select(driver.findElement(By.id("Ai_selected")));
            aiSelector.selectByVisibleText(properties.getProperty("aiName", "TestArchivalInstitution"));
            goButton.click();

            Assert.assertTrue(driver.getPageSource().contains("This is your first time to enter your dashboard. "
                    + "It is mandatory to create a new EAG file or to upload an existing one in order to ingest "
                    + "content into the Archives Portal Europe."));

            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Upload EAG file"))).click();

            WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("uploadowneag_label_upload")));
            Utils.uploadFile(driver, "uploadowneag_httpFile", "TC-00000000372.xml");
            captureScreen("EAG_file_select");
            uploadButton.click();
            Thread.sleep(5000);
            captureScreen("EAG Upload");
            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("modify EAG file")));
            Assert.assertTrue(driver.getPageSource().contains("Your EAG file has been uploaded correctly"));
            logger.info("::: EAG for test AI uploaded successfully :::");
        } catch (NoSuchElementException | TimeoutException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        } catch (InterruptedException ex) {
            logger.log(Level.INFO, "Thread sleep exception.");
        } finally {
            logger.info("EAG upload method done.");
        }
    }

    @Test
    public void testEUploadFindingAid() throws InterruptedException, AWTException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Upload content"))).click();
            WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("uploadButton")));
            captureScreen(name.getMethodName() + "_upload_content");
            Utils.uploadFile(driver, "httpFile", "NL-HaNA_4.VTH.ead.xml");
            //driver.findElement(By.id("httpFile")).sendKeys(ClassLoader.getSystemResource("NL-HaNA_4.VTH.ead.xml").getPath());
            uploadButton.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("checkexistingfiles_label_accept"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            captureScreen(name.getMethodName() + "_content_is_uploaded");
            Assert.assertTrue(driver.getPageSource().contains("4.VTH"));
            logger.info("::: Finding aid Uploaded :::");
        } catch (NoSuchElementException | TimeoutException | IOException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        } catch (URISyntaxException ex) {
            Logger.getLogger(EnableOpenDataTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testFConvertValidatePublish() throws InterruptedException, IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        try {
            WebElement batchActionButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            driver.findElement(By.id("check_1")).click();
            captureScreen(name.getMethodName() + "_convert_validate_publish");
            new Select(driver.findElement(By.id("batchSelectedAction"))).selectByValue("convert_validate_publish");
            batchActionButton.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            Assert.assertTrue(driver.getPageSource().contains("Number of your files in the queue: 1, Queue size: 1"));
            int waited = 0;
            captureScreen(name.getMethodName() + "_convert_validate_publish_ongoing");
            logger.info("::: Waiting for the file to be converted/validated/published :::");
            while (!driver.getPageSource().contains("Queue size: 0") && waited <= MAX_WAIT) {
                Thread.sleep(5000);
                waited += 5000;
                driver.navigate().refresh();
                logger.log(Level.INFO, "::: Still waiting : Waited for :  {0} :::", waited);
            }
            captureScreen(name.getMethodName() + "_convert_validate_publish_done");
            Assert.assertTrue(driver.getPageSource().contains("Queue size: 0"));
            logger.info("::: File is converted/validated/published :::");
        } catch (NoSuchElementException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        }
    }

    @Test
    public void testGNoDataFromSolr() throws IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        EadResponseSet eads = this.searchAllEad();
        Assert.assertEquals(0, eads.getTotalResults());
    }

    @Test
    public void testHEnableOpenData() throws InterruptedException, IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText(properties.getProperty("aiName", "testAi")))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Manage open data"))).click();
            captureScreen(name.getMethodName() + "_openData_page_disabled");
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

            captureScreen(name.getMethodName() + "_openData_queue_management");

            Assert.assertEquals(properties.getProperty("aiName", "testAi"), aiName);
            Assert.assertEquals(properties.getProperty("aiName", "testAi"), aiNameInQueueItemList);
            int waited = 0;
            logger.info("::: waiting for openData to be enabled :::");
            while (driver.getPageSource().contains(properties.getProperty("aiName", "testAi")) && waited <= MAX_WAIT) {
                Thread.sleep(5000);
                waited += 5000;
                driver.navigate().refresh();
                logger.log(Level.INFO, "::: Still waiting : Waited for :  {0} :::", waited);
            }
            logger.log(Level.INFO, "::: Enable openData is done!!! Solr reindex has been completed :::");
            logger.info("::: openData enabling is done :::");
        } catch (NoSuchElementException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        }
    }

    @Test
    public void testISearchDataFromSolr() throws IOException, InterruptedException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());

        EadResponseSet eads = this.searchAllEad();
        Assert.assertNotEquals(0, eads.getTotalResults());
    }

    @Test
    public void testJUploadAndPublishFindingAidWithOpenDataEnabled() throws InterruptedException, IOException, URISyntaxException, AWTException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());

        long numberOfSearchResultBeforePublishingNewDoc = searchAllEad().getTotalResults();

        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("User management"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("changeToCountryManager_changeToCountryManager"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Manage content"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("selectArchive_0"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Upload content"))).click();
            WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("uploadButton")));
            //driver.findElement(By.id("httpFile")).sendKeys(ClassLoader.getSystemResource("NL-HaNA_4.VTHR.ead.xml").getPath());
            Utils.uploadFile(driver, "httpFile", "NL-HaNA_4.VTHR.ead.xml");
            uploadButton.click();
            captureScreen(name.getMethodName() + "_upload_FA_openData_true");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("checkexistingfiles_label_accept"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            Assert.assertTrue(driver.getPageSource().contains("4.VTHR"));
            WebElement batchActionButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            driver.findElement(By.id("check_2")).click();
            captureScreen(name.getMethodName() + "_convert_validate_publish_FA_openData_true");
            new Select(driver.findElement(By.id("batchSelectedAction"))).selectByValue("convert_validate_publish");
            batchActionButton.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            Assert.assertTrue(driver.getPageSource().contains("Number of your files in the queue: 1, Queue size: 1"));
            int waited = 0;
            captureScreen(name.getMethodName() + "_upload_FA_openData_true_ongoing");
            logger.info("::: waiting for the finding aid to be converted/validated/published :::");
            while (!driver.getPageSource().contains("Queue size: 0") && waited <= MAX_WAIT) {
                Thread.sleep(5000);
                waited += 5000;
                driver.navigate().refresh();
                logger.log(Level.INFO, "::: Still waiting : Waited for :  {0} :::", waited);
            }
            Thread.sleep(5000);
            Assert.assertTrue(searchAllEad().getTotalResults() > numberOfSearchResultBeforePublishingNewDoc);
            logger.info("::: convert/validate/publish is successful :::");
            captureScreen(name.getMethodName() + "_upload_FA_openData_true_done");
        } catch (NoSuchElementException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        }
    }

    @Test
    public void testKDisableOpenData() throws InterruptedException, IOException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());
        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText(properties.getProperty("aiName", "testAi")))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Manage open data"))).click();
            captureScreen(name.getMethodName() + "_openData_disable");
            WebElement checkBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("enableOpenData")));
            Assert.assertTrue(driver.getPageSource().contains("Open data flag is enabled."));

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
            captureScreen(name.getMethodName() + "_openData_disable_queue_management");

            Assert.assertEquals(properties.getProperty("aiName", "testAi"), aiName);
            Assert.assertEquals(properties.getProperty("aiName", "testAi"), aiNameInQueueItemList);
            int waited = 0;
            logger.info("::: Waiting for the openData to be disabled :::");
            while (driver.getPageSource().contains(properties.getProperty("aiName", "testAi")) && waited <= MAX_WAIT) {
                Thread.sleep(5000);
                waited += 5000;
                driver.navigate().refresh();
                logger.log(Level.INFO, "::: Still waiting : Waited for :  {0} :::", waited);
            }
            captureScreen(name.getMethodName() + "_openData_disable_queue_done");
            logger.log(Level.INFO, "::: Disable openData is done!!! Solr reindex has been completed :::");
            Assert.assertEquals(0, searchAllEad().getTotalResults());
        } catch (NoSuchElementException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        }
    }

    @Test
    public void testLUploadAndPublishFindingAidWithOpenDataDisabled() throws InterruptedException, IOException, URISyntaxException, AWTException {
        logger.log(Level.INFO, "::: Executing Method {0} :::", name.getMethodName());

        WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("User management"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("changeToCountryManager_changeToCountryManager"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Manage content"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("selectArchive_0"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Upload content"))).click();
            WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("uploadButton")));
            Utils.uploadFile(driver, "httpFile", "NL-HaNA_3.01.01.ead.xml");
            uploadButton.click();
            captureScreen(name.getMethodName() + "_upload_FA_openData_false");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("checkexistingfiles_label_accept"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            Assert.assertTrue(driver.getPageSource().contains("3.01.01"));
            WebElement batchActionButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            driver.findElement(By.id("check_3")).click();
            new Select(driver.findElement(By.id("batchSelectedAction"))).selectByValue("convert_validate_publish");
            batchActionButton.click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("batchActionButton")));
            captureScreen(name.getMethodName() + "_upload_FA_openData_false_ongoing");
            Assert.assertTrue(driver.getPageSource().contains("Number of your files in the queue: 1, Queue size: 1"));
            int waited = 0;
            logger.info("::: waiting for the Finding aid to be converted/validated/publishd :::");
            while (!driver.getPageSource().contains("Queue size: 0") && waited <= MAX_WAIT) {
                Thread.sleep(5000);
                waited += 5000;
                driver.navigate().refresh();
                logger.log(Level.INFO, "::: Still waiting : Waited for :  {0} :::", waited);
            }
            Thread.sleep(5000);
            captureScreen(name.getMethodName() + "_upload_FA_openData_false_done");
            Assert.assertEquals(0, searchAllEad().getTotalResults());
            logger.info("::: Finding aid converted/validated/publishd :::");
        } catch (NoSuchElementException nEx) {
            logger.log(Level.WARNING, "::: Failed due to Element not found : {0}", nEx.getMessage());
            Assert.fail("Element not found: " + nEx.getMessage());
        }
    }

    private EadResponseSet searchAllEad() throws IOException {
        HttpPost post = new HttpPost(properties.getProperty("apiBaseUrl", "http://localhost:9090/ApeApi/services/") + "search/ead");
        String data = "{\n"
                + "  \"query\": \"*\",\n"
                + "  \"count\": 0,\n"
                + "  \"startIndex\": 0\n"
                + "}";
        post.setHeader("APIkey", "myApiKeyXXXX123456789");
        post.setEntity(new StringEntity(data, ContentType.create("application/vnd.ape-v1+json", "UTF-8")));
        HttpResponse response = client.execute(post);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        EadResponseSet eads = gson.fromJson(IOUtils.toString(response.getEntity().getContent()), EadResponseSet.class);
        return eads;
    }

    private void captureScreen(String name) throws IOException, InterruptedException {
        screenshotHelper.saveScreenshot(name, driver);
    }

}
