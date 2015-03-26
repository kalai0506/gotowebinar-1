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
package com.citrix.g2w.webdriver.pages.managewebinar;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;

/**
 * @author: adas
 */
public class ManageFollowUpEmailPage extends BasePage {

    /**
     * Web Element for enabling follow up email copy to organizer.
     */
    @FindBy(id = "toBeSent")
    private WebElement followUpEmailToBeSent;

    /**
     * By element for send copy of follow up email.
     */
    private final By sendFollowUpEmail = By.id("email.settings.button.save");
    /**
     * Web Element for follow up email to be sent.
     */
    @FindBy(id = "notifyTrainer")
    private WebElement followUpEmailSendCopy;

    /**
     * Constructor to initialize web driver object and verify current page URL.
     * 
     * @param webDriver
     *            (object to initialize)
     */
    public ManageFollowUpEmailPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/email/"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Send follow up email to attendees.
     * 
     * @param sendFollowUpEmailType
     *            (follow up email type)
     * @return ManageWebinarPage
     */
    public ManageWebinarPage sendFollowUpEmailForWebinarWithRecording(
            final String sendFollowUpEmailType, String recordingFileId) {
        this.setCheckBox(this.followUpEmailToBeSent, true);
        this.setCheckBox(this.followUpEmailSendCopy, true);
        Select daysAfterDropDown = new Select(this.driver.findElement(By
                .id("daysAfter")));
        daysAfterDropDown.selectByVisibleText(sendFollowUpEmailType.trim());
        attachRecording(recordingFileId);
        this.logger.logWithScreenShot("After Select follow up email type",
                this.driver);
        this.findVisibleElement(this.sendFollowUpEmail).click();
        this.logger.logWithScreenShot(
                "After clicking on Save Follow Up Settings", this.driver);
        return new ManageWebinarPage(this.driver);
    }

    /**
     * Send follow up email to attendees.
     * 
     * @param sendFollowUpEmailType
     *            (follow up email type)
     * @return ManageWebinarPage
     */
    public ManageWebinarPage setFollowUpEmailForWebinar(
            final String sendFollowUpEmailType) {
        if (!(this.followUpEmailToBeSent.isSelected())) {
            this.setCheckBox(this.followUpEmailToBeSent, true);
            this.setCheckBox(this.followUpEmailSendCopy, true);
            Select daysAfterDropDown = new Select(this.driver.findElement(By
                    .id("daysAfter")));
            daysAfterDropDown.selectByVisibleText(sendFollowUpEmailType.trim());
            this.logger.logWithScreenShot("After Select follow up email type",
                    this.driver);
        }
        this.findVisibleElement(this.sendFollowUpEmail).click();
        this.logger.logWithScreenShot(
                "After clicking on Save Follow Up Settings", this.driver);
        return new ManageWebinarPage(this.driver);
    }

    /**
     * Method used to select a recording attachment for follow up email
     * 
     * @param recordingFileId
     *            Id of recording file
     */
    public void attachRecording(String recordingFileId) {
        if (StringUtils.isNotBlank(recordingFileId)) {
            WebElement selectRecordingLink = this.driver.findElement(By
                    .id("selectRecordingButton"));
            selectRecordingLink.click();
            WebElement recordingFileSelection = this.driver.findElement(By
                    .id("offlineWebinarKey_" + recordingFileId));
            recordingFileSelection.click();
            WebElement attachRecordingButton = this.driver
                    .findElement(By
                            .xpath("//div[@id='attachRecording']/div[1]/div[1]/button"));
            attachRecordingButton.click();
            logger.logWithScreenShot("attached file", this.driver);
        }
    }
}
