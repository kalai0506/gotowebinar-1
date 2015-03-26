/*
 * Copyright (c) 1998-2014 Citrix Online LLC
 * All Rights Reserved Worldwide.
 *
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO CITRIX ONLINE
 * AND CONSTITUTES A VALUABLE TRADE SECRET. Any unauthorized use,
 * reproduction, modification, or disclosure of this program is
 * strictly prohibited. Any use of this program by an authorized
 * licensee is strictly subject to the terms and conditions,
 * including confidentiality obligations, set forth in the applicable
 * License and Co-Branding Agreement between Citrix Online LLC and
 * the licensee.
 */

package com.citrix.g2w.webdriver;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

/**
 * @author: 
 * @since: 
 */
public class TestLogger {
    /**
     * class variable for screen shot directory.
     */
    private static File screenShotsDirectory;

    /**
     * Method used to get unique string.
     * 
     * @return unique string
     */
    public static String getUniqueString() {
        return new Date().getTime() + "-" + Thread.currentThread().getId();
    }

    /**
     * Constructor to initialize system property.
     */
    public TestLogger() {
        System.setProperty("org.uncommons.reportng.escape-output", "false");
    }

    /**
     * Method to log message to log file.
     * 
     * @param message
     *            (message to log)
     */
    public void log(final String message) {
        Reporter.log(message + "<br>");
    }

    /**
     * Method to log screenshot to log file.
     * 
     * @param webDriver
     *            (web driver object)
     * @param linkText
     *            (text to log)
     */
    public void logScreenShot(final WebDriver webDriver, final String linkText) {
        if (screenShotsDirectory != null) {
            File screenShotFile = new File(screenShotsDirectory, getUniqueString() + ".html");
            try {
                FileUtils.writeStringToFile(screenShotFile, webDriver.getPageSource());
                Reporter.log("<a href=\"" + screenShotFile.getName() + "\" target=\"_blank\">" + linkText + "</a><br>");
            } catch (IOException e) {
                Reporter.log("Unable to log screenshot");
            }
        } else {
            Reporter.log("Screenshots directory not set. Not logging screenshot");
        }
    }

    /**
     * Method to log message along with screenshot.
     * 
     * @param message
     *            (message to log)
     * @param webDriver
     *            (web driver object)
     */
    public void logWithScreenShot(final String message, final WebDriver webDriver) {
        StringBuilder logText = new StringBuilder(message);
        if (screenShotsDirectory != null) {
            File screenShotFile = new File(screenShotsDirectory, getUniqueString() + ".html");
            try {
                FileUtils.writeStringToFile(screenShotFile, webDriver.getPageSource());
                logText.append(" <a href=\"" + screenShotFile.getName() + "\" target=\"_blank\">Resulting Page</a>");
            } catch (IOException e) {
                logText.append(" Unable to log screenshot");
            }
        } else {
            logText.append(" <i>Screenshots directory not set. Not logging screenshot<i>");
        }

        logText.append("<br>");
        Reporter.log(logText.toString());
    }

    /**
     * Method used to set screen shot directory.
     * 
     * @param screenShotsDirectory
     *            (location of screen shot directory)
     */
    public void setScreenShotsDirectory(final File screenShotsDirectory) {
        TestLogger.screenShotsDirectory = screenShotsDirectory;
    }
}
