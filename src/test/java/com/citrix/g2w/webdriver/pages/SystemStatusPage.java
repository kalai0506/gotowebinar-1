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
import org.testng.Assert;

/**

 */
public class SystemStatusPage extends BasePage {

    /**
     * Constructor to initialize instance variables and verify if we are on the
     * correct page.
     * @param webDriver
     *            (web driver)
     * @param testG2wHost
     *            (test G2w host)
     */
    public SystemStatusPage(final WebDriver webDriver, final String testG2wHost) {
        String url = "https://" + testG2wHost + "/system-status";
        webDriver.get(url);
        Assert.assertTrue(webDriver.getCurrentUrl().contains("/system-status"));
        this.logger.logWithScreenShot("After opening system-status page having url :" + url,
                webDriver);
    }
}
