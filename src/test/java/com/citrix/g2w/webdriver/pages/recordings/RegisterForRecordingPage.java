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

import java.util.ArrayList;
import java.util.List;

/**
 * @author spulapura
 * @since 04/23/2014
 */
public class RegisterForRecordingPage extends BasePage {

    private String recordingId;

    /**
     * First name for registrant
     */
    @FindBy(id="registrant.givenName")
    private WebElement registrantGivenName;

    /**
     * Last name for registrant
     */
    @FindBy(id="registrant.surname")
    private WebElement registrantSurname;

    /**
     * Last name for registrant
     */
    @FindBy(id="registrant.email")
    private WebElement registrantEmail;

    /**
     * Submit button
     */
    @FindBy(id="registration.submit.button")
    private WebElement submitButton;

    /**
     * Error messages
     */
    @FindBy(xpath="//div[contains(@class,'has-error')]//span[@class='help-block']")
    private List<WebElement> errorMessages;

    /**
     * Constructor to initialize web driver and verify current page URL.
     * @param webDriver
     *            (Web Driver object)
     */
    public RegisterForRecordingPage(final String recordingRegistrationURL, final WebDriver webDriver) {
        this.driver = webDriver;
        if (!recordingRegistrationURL.equalsIgnoreCase("")) {
            this.driver.get(recordingRegistrationURL);
            this.logger.logWithScreenShot("Recording Registration Page: ", this.driver);
        }
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/recording"));
        this.recordingId = recordingRegistrationURL.substring(recordingRegistrationURL.lastIndexOf("/") + 1);
        PageFactory.initElements(this.driver, this);
    }

    public String getRegistrantGivenName() {
        return registrantGivenName.getText();
    }

    public String getRegistrantSurname() {
        return registrantSurname.getText();
    }

    public String getRegistrantEmail() {
        return registrantEmail.getText();
    }

    public void register() {
        this.submitButton.click();
    }

    public List<String> getErrorMessages() {
        List<String> fieldErrorMessagesList = new ArrayList<String>();
        for(WebElement element : errorMessages) {
            fieldErrorMessagesList.add(element.getText());
        }
        return fieldErrorMessagesList;
    }

    public RegisterForRecordingPage submitExpectError(String firstName, String lastName, String email) {
        setFields(firstName, lastName, email);
        this.submitButton.click();
        this.logger.logWithScreenShot("Register for recording error page:", this.driver);
        return new RegisterForRecordingPage("", this.getDriver());
    }

    public ViewRecordingPage submit(String firstName, String lastName, String email) {
        setFields(firstName, lastName, email);
        this.submitButton.click();
        this.logger.logWithScreenShot("Register for recording error page:", this.driver);
        return new ViewRecordingPage(this.getDriver());
    }

    private void setFields(String firstName, String lastName, String email) {
        if(firstName != null) {
            this.registrantGivenName.sendKeys(firstName);
        }
        if(lastName != null) {
            this.registrantSurname.sendKeys(lastName);
        }
        if(email != null) {
            this.registrantEmail.sendKeys(email);
        }
    }
}
