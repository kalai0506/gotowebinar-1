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
package com.citrix.g2w.webdriver.pages.registration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 */
public class RegistrationCanceledPage extends BasePage {
    /**
     * Web element to get registration cancel header.
     */
    @FindBy(xpath = "//div[@id='content']/h1")
    private WebElement registrationCancelHeader;

    /**
     * Web element to get registration cancel message.
     */
    @FindBy(xpath = "//div[@id='content']/p")
    private WebElement registrationCancelMessage;

    /**
     * Constructor to initialize instance variables and verify current page url.
     *
     * @param webDriver
     *            (web driver)
     */
    public RegistrationCanceledPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("registration/cancel.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method to get registration cancel message.
     *
     * @return registrationCancelMsg(registration cancelled message)
     */
    public String getRegistrationCancelMessage() {
        String registrationCancelMsg = this.registrationCancelMessage.getText();
        this.logger.log("Registration canceled message:" + registrationCancelMsg);
        return registrationCancelMsg;
    }

    /**
     * Method to get registration cancel header.
     *
     * @return registrationCancelMsg(registration cancelled message)
     */
    public String getRegistrationCancelHeader() {
        String registrationCancelHeaderText = this.registrationCancelHeader.getText();
        this.logger.log("Registration canceled message:" + registrationCancelHeader);
        return registrationCancelHeaderText;
    }
}
