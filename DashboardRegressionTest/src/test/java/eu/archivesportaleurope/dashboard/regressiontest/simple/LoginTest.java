/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest.simple;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import eu.archivesportaleurope.dashboard.test.utils.ScreenshotHelper;

/**
 *
 * @author kaisar
 */
public class LoginTest {

    private static String baseUrl;
    private static WebDriver driver;
    private static ScreenshotHelper screenshotHelper;
    private Properties properties;

    @Rule
    public ErrorCollector errCollector = new ErrorCollector();

    public LoginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        properties = new Properties();
        InputStream is = ClassLoader.getSystemResourceAsStream("config.properties");
        properties.load(is);
        baseUrl = properties.getProperty("baseUrl", "https://development.archivesportaleurope.net/Dashboard/");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
        screenshotHelper = new ScreenshotHelper();
    }

    @After
    public void tearDown() throws IOException {
        screenshotHelper.saveScreenshot("login.png", driver);
        driver.quit();
    }

    @Test
    public void testLoginWithWrongCredentials() throws InterruptedException, IOException {
        WebElement element = null;
        Thread.sleep(5000);
        try {
            driver.findElement(By.id("username")).sendKeys("FOUT");
            driver.findElement(By.id("login_password")).sendKeys("FOUT");
            driver.findElement(By.id("login_label_login")).click();
            Thread.sleep(5000);
            element = driver.findElement(By.id("login_"));
            screenshotHelper.saveScreenshot("ErrorLogin.png", driver);
        } catch (NoSuchElementException ex) {
            fail("No Such Element " + ex.getAdditionalInformation());
        }
        try {
            assertEquals("Invalid email address or password: please try again", element.getText());
        } catch (Throwable t) {
            fail("Expected Text was : Invalid email address or password: please try again, But found : " + element.getText());
            errCollector.addError(t);
        }
    }

    @Test
    public void testLoginWithCorrectCredentials() throws InterruptedException, IOException {
        Thread.sleep(3000);
        try {
            driver.findElement(By.id("username")).sendKeys(properties.getProperty("adminUserName", "Kaisar.Ali@nationaalarchief.nl"));
            driver.findElement(By.id("login_password")).sendKeys(properties.getProperty("adminPassword", "test2010"));
            driver.findElement(By.id("login_dropOtherSession")).click();
            driver.findElement(By.id("login_label_login")).click();
            Thread.sleep(5000);
            screenshotHelper.saveScreenshot("CorrectLogin.png", driver);

        } catch (NoSuchElementException ex) {
            fail("No Such Element " + ex.getAdditionalInformation());
        }
        try {
            assertEquals("DASHBOARD", driver.getTitle());
            Thread.sleep(1000);
        } catch (Throwable t) {
            fail("Expected Text was : DASHBOARD, But found : " + driver.getTitle());
            errCollector.addError(t);
        }
    }

    @Test
    public void testLogout() throws InterruptedException {
        WebElement element = null;
        Thread.sleep(2000);
        try {
            driver.findElement(By.id("username")).sendKeys(properties.getProperty("adminUserName", "Kaisar.Ali@nationaalarchief.nl"));
            driver.findElement(By.id("login_password")).sendKeys(properties.getProperty("adminPassword", "test2010"));
            driver.findElement(By.id("login_dropOtherSession")).click();
            driver.findElement(By.id("login_label_login")).click();
            element = driver.findElement(By.id("userFeatures")).findElements(By.tagName("a")).get(0);
        } catch (NoSuchElementException ex) {
            fail("No Such Element " + ex.getAdditionalInformation());
        }
        try {
            assertEquals("Logout", element.getText());
            Thread.sleep(1000);
            element.click();
            Thread.sleep(2000);
            assertEquals("Login", driver.getTitle());
            Thread.sleep(1000);
        } catch (Throwable t) {
            fail("Expected Text was : Login, But found : " + driver.getTitle());
            errCollector.addError(t);
        }
    }

}
