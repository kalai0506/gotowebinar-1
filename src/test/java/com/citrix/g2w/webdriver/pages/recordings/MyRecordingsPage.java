/*
 * Copyright (c) 1998-2014 Citrix Online LLC
 * All Rights Reserved Worldwide.
 *
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO CITRIX ONLINE
 * AND CONSTITUTES A VALUABLE TRADE SECRET.  Any unauthorized use,
 * reproduction, modification, or disclosure of this program is
 * strictly prohibited.  Any use of this program by an authorized
 * licensee is strictly subject to the terms and conditions,
 * including confidentiality obligations, set forth in the applicable
 * License and Co-Branding Agreement between Citrix Online LLC and
 * the licensee.
 */

package com.citrix.g2w.webdriver.pages.recordings;

import com.citrix.g2w.webdriver.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sudip
 * @since: 4/29/14
 */
public class MyRecordingsPage extends BasePage {

    @FindBy(id="fileUpload")
    private WebElement fileUploadInput;


    @FindBy(xpath="//form/a[@class='symphony']")
    private WebElement uploadFileButton;

    @FindBy(xpath="//div[@class='table-data']//ul[@class='table-data-row']/li")
    private List<WebElement> viewsTableRows;

    @FindBy(xpath="//a[@href='#removeRecording']")
    private List<WebElement> removeRecordingLinks;

    /**
     * Constructor to initialize instance variables and verify if we are on the
     * My Recordings page.
     *
     * @param webDriver (web driver)
     */
    public MyRecordingsPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(webDriver.getCurrentUrl().contains("recordings.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    public MyRecordingsPage(String url, final WebDriver webDriver) {
        this.driver = webDriver;
        this.driver.get(url);
        Assert.assertTrue(webDriver.getCurrentUrl().contains("recordings.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method used to upload a file
     *
     * @param recordingFilePath Name of recording file
     */
    public void uploadFile(String recordingFilePath) {
        String js = "arguments[0].style.visibility = 'visible'; arguments[0].style.height = '1px'; arguments[0].style.width = '1px'; arguments[0].style.opacity = 1";
        ((JavascriptExecutor) this.driver).executeScript(js, fileUploadInput);
        fileUploadInput.sendKeys(recordingFilePath);

        By cancelUploadXpath = By.xpath("//a[contains(@class,'cancel-upload')]");

        // wait till the recordings uploaded
        // TODO: Find better way of detecting upload complete
        boolean isCancelUploadExists = true;
        while (isCancelUploadExists) {
            try {
                // find cancel upload dialog to check the file
                // upload progress
                this.findVisibleElement(cancelUploadXpath, 5);
                continue;
            } catch (Exception e) {
                isCancelUploadExists = false;
            }
        }

        logger.logWithScreenShot("Uploaded file", this.driver);
    }
    /**
     * Method used to upload a file expecting error
     *
     * @param recordingFile Name of file
     */
    public void uploadFileExpectError(String recordingFilePath) {
        String js = "arguments[0].style.visibility = 'visible'; arguments[0].style.height = '1px'; arguments[0].style.width = '1px'; arguments[0].style.opacity = 1";
        ((JavascriptExecutor) this.driver).executeScript(js, fileUploadInput);
        fileUploadInput.sendKeys(recordingFilePath);

        logger.logWithScreenShot("Upload file failure", this.driver);
    }

    /**
     * Navigate to the recordingViews page for a given recording
     * @param recordingId Recording Id
     * @return Recording Views Page
     */
    public RecordingViewsPage gotoRecordingViewPage(String recordingId) {
        WebElement recordingViewsLink = this.driver.findElement(By.xpath("//ul[@class='table-data-row']/li/a[contains(@href,'" + recordingId + "')]"));
        recordingViewsLink.click();
        return new RecordingViewsPage(this.driver);
    }

    /**
     * Get the number of views for a recording
     *
     * @param recordingId Recording Id
     * @return Number of views
     */
    public int getNumberOfViews(String recordingId) {
        WebElement recordingViewsLink = this.driver.findElement(By.xpath("//ul[@class='table-data-row']/li/a[contains(@href,'" + recordingId + "')]"));
        return Integer.parseInt(recordingViewsLink.getText());
    }

    /**
     * Get list of recording ids
     *
     * @return List
     */
    public List<String> getRecordingIds() {
        List<String> recordingUrls = new ArrayList<String>();
        for(WebElement recordingUrlLink : removeRecordingLinks) {
            recordingUrls.add(recordingUrlLink.getAttribute("data-webinarKey"));
        }
        return recordingUrls;
    }
}
