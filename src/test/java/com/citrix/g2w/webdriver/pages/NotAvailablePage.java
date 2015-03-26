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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author: adas
 */
public class NotAvailablePage extends BasePage {
	/*
     * Web element used to get message in the page
     */
	@FindBy(xpath = "//div[@class='container']/div/div/p/span")
    private WebElement pageMessage;	
	
    /**
     * Constructor to initialize web driver object.
     * @param webDriver (web driver object to initialize)
     */
    public NotAvailablePage(final WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(this.driver, this);
        this.logger.logWithScreenShot("Launch Not Available Page", this.driver);
    }

 	/**
	 * Method to get page message from not available page
	 * @return message 
	 */
	public String getPageMessage() {
		String message = pageMessage.getText();
		this.logger.log("Getting the page message from not available page: " + message);
		return message;
	}
}
