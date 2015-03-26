/**
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

package com.citrix.g2w.webdriver.tests.tealium;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationConfirmationPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;
import com.citrix.g2w.webdriver.util.CSVDataReader;

/**
 * 
 * @author ankitag1, ksiva
 *
 */
public class TealiumWebdriverTests extends BaseWebDriverTest {

    private static Properties tealiumProperties;
    private static CSVDataReader csvDataReader = new CSVDataReader();
    private Long webinarKey;
    private String registrantId;

    @BeforeClass
    public void setUp() throws IOException {      
        tealiumProperties = new Properties();

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("src/test/resources/tealium.properties"));
            tealiumProperties.load(fileInputStream);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        // same webinar and webinarkey would be used for all the urls tested.
        // (in pageData.csv)
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);

        webinarKey = manageWebinarPage.getWebinarKey();
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), "tealiumf", "tealiuml", "tealiumtest@jedix.com");
        registrantId = registrationConfirmationPage.getRegistrantID();
        this.setCloseWebDriverAtEndOfMethod(false);
        this.logger.log("WebinarKey" + webinarKey);
    }

    @DataProvider(name = "pageData")
    public static Object[][] pageData() throws IOException {
        return csvDataReader.getSeleniumDataArray("src/test/resources/tests/data/tealium/pageData.csv", "");

    }

    @Test(groups = { Groups.TEALIUM}, description = "Tealium info for Pages", dataProvider = "pageData")
    public void verifyTealiumInfo(String pageUrl, String expectedPageId) {

        if (pageUrl.contains("WEBINARKEY")) {
            pageUrl = pageUrl.replace("WEBINARKEY", String.valueOf(webinarKey));
        }
        if (pageUrl.contains("REGISTRANTID")) {
            pageUrl = pageUrl.replace("REGISTRANTID", registrantId);
        }

        this.webDriver.get(this.serviceUrl + "/" + pageUrl);
        WebElement pageId = webDriver.findElement(By.xpath("//body[@id=\"" + expectedPageId + "\" ]"));
        Assert.assertNotNull(pageId);

        this.logger.logWithScreenShot("Page snapshot ", this.getWebDriver());
        this.webDriver.get(this.serviceUrl + "/tealium/" + expectedPageId);
        this.logger.logWithScreenShot("Tealium params for pageId ", this.getWebDriver());

        String template = (String) tealiumProperties.get(expectedPageId + ".template");
        String contentType = (String) tealiumProperties.get(expectedPageId + ".contentType");
        String subsection = (String) tealiumProperties.get(expectedPageId + ".subsection");
        String pageSource = this.webDriver.getPageSource();
        Assert.assertNotNull(pageSource);
        Assert.assertTrue(pageSource.contains("\"contentType\":\"" + contentType));
        Assert.assertTrue(pageSource.contains("\"template\":\"" + template));
        Assert.assertTrue(pageSource.contains("\"subsection\":\"" + subsection));

    }
        
}
