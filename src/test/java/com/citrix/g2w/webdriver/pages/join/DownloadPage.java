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
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 * @author: adas
 */
public class DownloadPage extends BasePage {

    /**
     * Constructor to initialize web driver object and verify the page URL.
     * @param webDriver (web driver object to initialize)
     */
    public DownloadPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("launch.html"));
        PageFactory.initElements(this.driver, this);
        this.logger.logWithScreenShot("Launch GoTo Webinar Page", this.driver);
    }

    /**
     * Method to get Launch goto webinar page title.
     * @return title
     */
    public String getTitleFromDownloadPage() {
        String title = this.driver.getTitle();
        this.logger.log("Get the title from download page: " + title);
        return title;
    }
}
