/**
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

package com.citrix.g2w.webdriver.pages.registration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 * 
 * @author meredith tully
 * @since May 11, 2014
 *
 */

/**
 * Page object for the page shown to Registrant when webinar is not available (cancelled) 
 */

public class WebinarNotAvailablePage extends BasePage {

    /**
     * Web element for page title.
     */
    @FindBy(xpath = "//div[@id='content']/h1")
    private WebElement pageTitle;
    /**
     * Web element for page message.
     */
    @FindBy(id="notAvailable.message")
    private WebElement pageMessage;

    /**
     * initialize constructor with attendee join url and web driver object.
     * 
     * @param attendeeJoinURL
     *            (attendee joining URL)
     * @param webDriver
     *            (web driver object)
     */
    public WebinarNotAvailablePage(final String attendeeJoinURL,
            final WebDriver webDriver) {
        this.driver = webDriver;
        this.driver.get(attendeeJoinURL);
        Assert.assertTrue(webDriver.getCurrentUrl().contains(
                "notAvailable.tmpl"));
        PageFactory.initElements(this.driver, this);
        this.logger.logWithScreenShot("Webinar Not Available: ", this.driver);
    }
    
    
    public WebinarNotAvailablePage(
            final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(webDriver.getCurrentUrl().contains(
                "notAvailable.tmpl"));
        PageFactory.initElements(this.driver, this);
        this.logger.logWithScreenShot("Webinar Not Available Page: ", this.driver);
    }
    
    
    /**
     * Method to get message displayed on page.
     * 
     * @return String message
     */
    public String getPageTitle() {
        String message = this.pageTitle.getText();
        this.logger.log("Page title is : " + message);
        return message;
    }

    /**
     * Method to get message displayed on page.
     * 
     * @return String webinar name
     */
    public String getPageMessage() {
        String pageMessage = this.pageMessage.getText();
        this.logger.log("webinar Name is : " + pageMessage);
        return pageMessage;
    }
}
