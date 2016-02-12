/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.test.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author M.Mozadded
 */
public class ScreenshotHelper {

    private static int IMAGE_COUNT = 0;
    private static final Logger logger = Logger.getLogger(ScreenshotHelper.class.getName());

    public void saveScreenshot(String screenshotFileName, WebDriver driver) throws IOException, InterruptedException {
        Thread.sleep(10);
        IMAGE_COUNT++;
        String formatted = String.format("%04d", IMAGE_COUNT);
        screenshotFileName = "screenShot/" + formatted + screenshotFileName + ".png";
        logger.log(Level.INFO, "Screen captured : {0}", screenshotFileName);
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File(screenshotFileName));
    }
}
