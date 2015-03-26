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
package com.citrix.g2w.webdriver.tests.attendee.registration;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.registration.WebinarNotAvailablePage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

/**
 * 
 * @author meredith tully
 * @since May 10, 2014
 *
 */
public class WebinarNotAvailablePageTests extends BaseWebDriverTest {
    /**
     * Test to verify that Webinar Not Available Page is served when registering 
     * for a webinar which has been cancelled.
     * <ol>
     * <li>Create a personal account, login and schedule a webinar.</li>
     * <li>Goto manage webinar page and get the attendee registration URL</li>
     * <li>Cancel the webinar.</li>
     * <li>Register an attendee.</li>
     * <li>Verify if it automatically redirects to Webinar Not Available page</li>
     * <li>Verify the page message and URL displayed</li>
     * </ol>
     */
	@Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.CANCEL_WEBINAR })
    public void webinarNotAvailablePageWhenRegisteringForCancelledWebinar() {

        this.logger.log("Test to verify Webinar Not Available appears if user registers "
                + "for a webinar which has been cancelled.");

        // Create a personal account, login and schedule a webinar
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);

        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        System.out.println(registrationUrl);
        
        // Cancel the webinar.
        manageWebinarPage.cancelWebinar();

        WebinarNotAvailablePage webinarNotAvailablePage = new WebinarNotAvailablePage(registrationUrl, webDriver);
        // Verify correct message is displaying in the webinar not available page
        this.logger.log("Verify the page message is : "
                + this.messages.getMessage("notAvailable.title", null, this.locale));
		Assert.assertEquals(webinarNotAvailablePage.getPageTitle(),
                this.messages.getMessage("notAvailable.title", null, this.locale));
    }

}
