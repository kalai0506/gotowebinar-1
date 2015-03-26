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

package com.citrix.g2w.webdriver.pages.reports;



import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.util.CSVDataReader;
import com.citrix.g2w.webdriver.util.ExcelDataReader;
import com.citrix.g2w.webdriver.util.FileDownloader;


/**
 * @author ankitag1
 */
public class GenerateReportsPage extends BasePage {

    public enum ReportType {
        Registration, Attendee, Performance,Recording,Survey
    };    
    
    private ExcelDataReader excelDataReader = new ExcelDataReader();
    private CSVDataReader csvDataReader = new CSVDataReader();
    
    /**
     * Webelement for Registration Report.
     */
    @FindBy(id = "reportType_REGISTRATION")
    private WebElement reportType_REGISTRATION;
    /**
     * Webelement for Attendee Report.
     */
    @FindBy(id = "reportType_ATTENDEE")
    private WebElement reportType_ATTENDEE;
    /**
     * Webelement for Performance Report.
     */
    @FindBy(id = "reportType_PERFORMANCE")
    private WebElement reportType_PERFORMANCE;
    /**
     * Webelement for Recording Report.
     */
    @FindBy(id = "reportType_RECORDING")
    private WebElement reportType_RECORDING;
    /**
     * Webelement for Survey Report.
     */
    @FindBy(id = "reportType_SURVEY")
    private WebElement reportType_SURVEY;
    
    /**
     * Webelement for Survey Report.
     */
    @FindBy(id = "dateRangeType_DEFAULT")
    private WebElement dateRangeType_DEFAULT;
    
    /**
     * Webelement for Survey Report.
     */
    @FindBy(id = "dateRangeType_CUSTOM")
    private WebElement dateRangeType_CUSTOM;
    
    /**
     * Webelement for Survey Report.
     */
    @FindBy(id = "dateRange_trig")
    private WebElement dateRange_trig;
    
    /**
     * Webelement for Survey Report.
     */
    @FindBy(id = "showWebinarsButton")
    private WebElement showWebinarsButton;
    
    /**
     * By element used to generate reports.
     */ 
    @FindBy(id = "reportGenerationFormSubmit")
    private WebElement reportGenerationBtn;

    
    /**
     * Web element used to generate the report in CSV format.
     */
    @FindBy(id = "reportFormat_CSV")
    private WebElement reportFormatCSV;
    /**
     * Web element used to generate the report in xls format.
     */
    @FindBy(id = "reportFormat_XLS")
    private WebElement reportFormatXLS;
    
    /**
     * Constructor to initialize instance variables and verify if we are on the
     * Generate Reports page.
     * 
     * @param webDriver
     *            (web driver)
     */

    public GenerateReportsPage(final String reportUrl, final WebDriver webDriver) {
        this.driver = webDriver;
        this.driver.get(reportUrl);
        Assert.assertTrue(webDriver.getCurrentUrl().contains("generateReports.tmpl"));
        PageFactory.initElements(this.driver, this);
    }
    
    /**
     * Constructor to initialize instance variables and verify if we are on the
     * Generate Reports page.
     * 
     * @param webDriver
     *            (web driver)
     */

    public GenerateReportsPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(webDriver.getCurrentUrl().contains("generateReports.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method to navigate to Report Page.
     * 
     */
    public void generateReport(String reportType, String dateRange) {
        String webinarNameXpath = "//input[@class='choose-report']";
        int retryAttempts = 3;

        switch (ReportType.valueOf(reportType)) {
        case Registration:
            this.reportType_REGISTRATION.click();
        case Attendee:
            this.reportType_ATTENDEE.click();
        case Performance:
            this.reportType_PERFORMANCE.click();
        case Recording:
            this.reportType_RECORDING.click();
        case Survey:
            this.reportType_SURVEY.click();

        }
        this.dateRange_trig.click();
        WebElement chooseDateRange = driver
                .findElement(By
                        .xpath("//div[@id='dateRange__menu']/ul/li[contains(@title, dateRange)]"));
        chooseDateRange.click();        
        while (retryAttempts >= 0) {
            try {
                if (this.findVisibleElement(By.xpath(webinarNameXpath)) != null) {
                    break;
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                retryAttempts--;
            }
            this.showWebinarsButton.click();
        }        
        this.logger.logWithScreenShot("After navigating to Survey Report Page",
                this.driver);
    }
    
    /**
     * Method to select report format.
     * 
     * @param reportFormat
     *            (report format type)
     * 
     */
    public void selectReportFormat(final String reportFormat) {
        this.logger.log("Report Format:" + reportFormat);
        if (reportFormat != null && reportFormat.equalsIgnoreCase(this.REPORT_FORMAT_CSV)) {
            this.reportFormatCSV.click();
        } else {
            this.reportFormatXLS.click();
        }
    }
    
    /**
     * Method to generate and download the report.
     * 
     * @return downloadedFileAbsoluteLocation (Downloaded file absolute
     *         location)
     */
    public String clickOnGenerateReport() {
        FileDownloader downloadAttendeeFile = new FileDownloader(this.driver);
        this.reportGenerationBtn.click();
        String downloadedFileAbsoluteLocation = null;
        try {
            downloadedFileAbsoluteLocation = downloadAttendeeFile.downloader(this.DOWNLOAD_PATH);
        } catch (Exception e) {
            String errorMessage = "Exception while report generation:" + e.getMessage();
            this.logger.log(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        this.logger.log("Downloaded file absolute location :" + downloadedFileAbsoluteLocation);
        return downloadedFileAbsoluteLocation;
    }
    
    /**
     * Method to select Attendee training, report type and generate report.
     * 
     * @param webinarName
     *            (webinar name)
     * @param reportFormat
     *            (report Format)
     * @return attendeeReportList
     * @throws IOException 
     * 
     */
    public Object[][] selectWebinarAndGenerateReport(final String webinarName,
            final String reportFormat) throws IOException {
        String webinarNameXpath = "//input[@class='choose-report']";
        this.findClickableElement(By.xpath(webinarNameXpath)).click();

        // Select report type
        this.selectReportFormat(reportFormat);

        this.logger.logWithScreenShot("After Selecting Webinar and report type ",
                this.driver);

        // download attendee report
        String downloadedFileAbsoluteLocation = this.clickOnGenerateReport();
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ie) {
            logger.logWithScreenShot(ie.getMessage(), this.driver);
        }
        if("XLS".equals(reportFormat)) {
            return excelDataReader.getSeleniumDataArray(downloadedFileAbsoluteLocation, "");
        } else if("CSV".equals(reportFormat)) {
            return csvDataReader.getSeleniumDataArray(downloadedFileAbsoluteLocation, "");
        } 
        throw new IllegalArgumentException("Report Format Not supported: " + reportFormat);
    }
}
