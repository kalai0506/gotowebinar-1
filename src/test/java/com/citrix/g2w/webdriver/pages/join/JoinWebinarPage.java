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

package com.citrix.g2w.webdriver.pages.join;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;

/**
 * Webinar join page for attendee
 * @author adas
 */
public class JoinWebinarPage extends BasePage {
    /**
     * web element used to hold registrant email id.
     */
    @FindBy(id = "email")
    private WebElement attendeeEmailId;
    /**
     * web element used to hold webinar id
     */
    @FindBy(id = "publicId")
    private WebElement webinarId;
    
    /**
     * web element used to hold submit button.
     */
    @FindBy(id = "submit")
    private WebElement joinContinue;
    
    /**
     * initialize constructor with joinPage URL and web driver object.
     * 
     * @param joinPageUrl (attendee join page URL)
     * @param webDriver (web driver object)
     */
    public JoinWebinarPage(final String joinPageURL, final WebDriver webDriver) {
        this.driver = webDriver;
        if (!joinPageURL.equalsIgnoreCase("")) {
            this.driver.get(joinPageURL);
            this.logger.logWithScreenShot("Join Page: ", this.driver);
        }
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/join"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Join a webinar from the join webinar page.
     * 
     * @param registrantEmail (attendee email)
     * @param webinarID (webinar id)
     */    
    public RegistrationPage joinWebinarForUnregisteredAttendee(String registrantEmail, String webinarID){
        setFields(registrantEmail,webinarID);
        this.joinContinue.submit();
        return new RegistrationPage("", this.driver);
    }
    
    /**
     * Utils methods to fill the join page form values
     * @param registrantEmail (attendee email)
     * @param webinarID (webinar id)
     */
    private void setFields(String registrantEmail, String webinarID){
    	if(webinarID != null){
    		this.webinarId.sendKeys(webinarID);
    	}
    	if(registrantEmail != null){
    		this.attendeeEmailId.sendKeys(registrantEmail);
    	}
    }
}
