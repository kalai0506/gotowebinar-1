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
import com.citrix.g2w.webdriver.util.FileDownloader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sudip
 * @since: 4/29/14
 */
public class RecordingViewsPage extends BasePage {

    @FindBy(xpath="//div[@class='main']//h1")
    private WebElement heading;

    @FindBy(xpath="//div[@class='table-data']//ul[@class='table-data-row']/li")
    private List<WebElement> viewsTableRows;

    @FindBy(id="exportToExcel")
    private WebElement exportToExcelLink;

    public RecordingViewsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Get heading for the page
     *
     * @return heading String
     */
    public String getHeading() {
        return heading.getText();
    }

    /**
     * Get list of recording views
     *
     * @return List of recording views
     */
    public List<RecordingViewData> getListOfRecordingViews() {
        String localizedDateTimePattern = this.brokerMessages.getMessage("date.format.mask.daymonthyear", null, this.locale)
                + " " + this.brokerMessages.getMessage("date.format.mask.short.startAndEndDate.short.end", null, this.locale);
        List<RecordingViewData> recordingViewDataList = new ArrayList<RecordingViewData>();
        for(int i=0; i < viewsTableRows.size(); i+=4) {
            RecordingViewData recordingViewData = new RecordingViewData(viewsTableRows.get(i).getText(), viewsTableRows.get(i+1).getText(),
                    viewsTableRows.get(i+2).getText(), viewsTableRows.get(i+3).getText(), localizedDateTimePattern
                    , this.locale);
            recordingViewDataList.add(recordingViewData);
        }
        return recordingViewDataList;
    }

    /**
     * Download the excel report
     *
     * @return String absolute download path
     */
    public String downloadFile() {
        FileDownloader downloadAttendeeFile = new FileDownloader(this.driver);
        // Commented out until we have a good way to close the OS dialog
        // this.exportToExcelLink.click();
        String downloadedFileAbsoluteLocation;
        try {
            downloadedFileAbsoluteLocation = downloadAttendeeFile.downloader(exportToExcelLink.getAttribute("href"), this.DOWNLOAD_PATH);
        } catch (Exception e) {
            String errorMessage = "Exception while report generation:" + e.getMessage();
            this.logger.log(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        this.logger.log("Downloaded file absolute location :" + downloadedFileAbsoluteLocation);
        return downloadedFileAbsoluteLocation;
    }
}
