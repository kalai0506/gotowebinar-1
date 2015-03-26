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

package com.citrix.g2w.webdriver.pages;

import static junit.framework.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**

 */
public class LoginPage extends BasePage {

    /**
     * Webelement for email address.
     */
    @FindBy(id = "emailAddress")
    private WebElement emailAddress;
    /**
     * Webelement for password.
     */
    @FindBy(id = "password")
    private WebElement password;
    /**
     * Webelement for submit.
     */
    @FindBy(id = "submit")
    private WebElement submit;

    /**
     * Constructor to initialize instance variables and verify if current page
     * is Login Page.
     * 
     * @param serviceUrl
     *            (service url)
     * @param webDriver
     *            (web driver)
     */
    public LoginPage(final String serviceUrl, final WebDriver webDriver, boolean validate) {
        this.driver = webDriver;
        this.driver.get(serviceUrl);
        this.logger.log("Service URL" + serviceUrl);
        try {
            findPresenceOfElement(By.xpath("//div[@id='loginForm']"), 30);
            assertTrue(
                    "Title does not contain Login text. Title of the page:" + this.driver.getTitle(),
                    this.driver.getTitle().contains("Login"));
        } catch (Throwable e) {
            if(validate) {
                throw new RuntimeException(e);
            }
        }
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method to login using given credentials.
     * 
     * @param emailAddress
     *            (email address)
     * @param password
     *            (password)
     * @return MyWebinarsPage
     */
    public MyWebinarsPage login(final String emailAddress, final String password) {
        this.emailAddress.sendKeys(emailAddress);
        this.password.sendKeys(password);
        this.submit.click();
        this.logger.log("On page: " + this.driver.getCurrentUrl());
        this.logger.logWithScreenShot("Logged in as " + emailAddress, this.driver);
        findPresenceOfElement(By.id("myWebinars"), 30);
        assertTrue(this.driver.getCurrentUrl().contains("webinars.tmpl"));
        return new MyWebinarsPage(this.driver);
    }
}
