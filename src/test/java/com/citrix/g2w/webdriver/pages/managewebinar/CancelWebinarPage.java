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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 * @author: meredith tully
 * @since: May 10, 2014
 */
public class CancelWebinarPage extends BasePage {
    /**
     * Web element used to get banner error messages.
     */
    @FindBy(xpath = "//div[@class='message-banner error-banner']/div/p")
    private WebElement errorMessage;
    /**
     * Web element used to click on confirm cancel button.
     */
    @FindBy(id = "confirmCancel")
    private WebElement confirmCancelWebinar;
    /**
     * Constructor to initialize web driver and verify current page URL.
     * @param webDriver
     *            (Web Driver object)
     */
    public CancelWebinarPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("cancelWebinar"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method to confirm cancel webinar.
     */
    public void cancelWebinar() {
        this.confirmCancelWebinar.click();
        this.logger.logWithScreenShot("After clicking on confirm cancel webinar:", this.driver);
    }


    /**
     * Method to get reconfirm webinar cancellation error message and enter
     * @return errorMessages
     */
    public List<String> getReconfirmWebinarCancellationErrorMessage() {
        List<String> errorMessages = this
                .getListOfTitlesForElementsWithGivenXPath("//div[@class='message"
                        + "-banner error-banner']/div/p");
        this.logger.log("reconfirm webinar cancellation message :"
                + errorMessages);
        return errorMessages;
    }
}
