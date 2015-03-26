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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * ViewRecordings page
 *
 * @author sudip
 * @since: 4/23/14
 */
public class ViewRecordingPage extends BasePage {

    /**
     * Submit button
     */
    @FindBy(id="submit")
    private WebElement submitButton;

    @FindBy(xpath="//div[@id='content']/p/a")
    private WebElement codecLink;

    /**
     * Thank you text, not worth adding id for this
     */
    @FindBy(xpath="//div[@id='content']/p[1]")
    private WebElement instructions;

    /**
     * Constructor to initialize web driver and verify current page URL.
     * @param webDriver
     *            (Web Driver object)
     */
    public ViewRecordingPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/recording"));
        PageFactory.initElements(this.driver, this);
    }

    public String getCodecLink() {
        return codecLink.getText();
    }

    public String getInstructions() {
        return instructions.getText();
    }

    public void viewRecording() {
        this.submitButton.click();
        this.driver.switchTo().alert().accept();
    }

}
