/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest.opendata;

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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import eu.archivesportaleurope.dashboard.test.utils.ScreenshotHelper;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

    public EnableOpenDataTest() {
    }

    @Rule
    public ErrorCollector errCollector = new ErrorCollector();
    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUpClass() throws IOException, InterruptedException {
        InputStream is = ClassLoader.getSystemResourceAsStream("config.properties");
        properties.load(is);
        baseUrl = properties.getProperty("baseUrl", "https://development.archivesportaleurope.net/Dashboard/");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
        screenshotHelper = new ScreenshotHelper();
    }

    @AfterClass
    public static void tearDownClass() {
        driver.quit();
    }

    @Before
    public void openBrowser() throws IOException {

    }

    @After
    public void saveScreenshotAndCloseBrowser() throws IOException {
    }

    @Test
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
            fail("No Such Element " + ex.getAdditionalInformation());
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
