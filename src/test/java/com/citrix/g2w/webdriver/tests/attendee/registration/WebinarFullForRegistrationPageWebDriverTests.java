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
import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.managewebinar.ManageRegistrationSettingsPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.pages.registration.WebinarFullForRegistrationPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;



public class WebinarFullForRegistrationPageWebDriverTests extends BaseWebDriverTest {

    /**
     * Test to verify that Webinar Full Page is served when joining a webinar
     * when registration limit has exceeded.
     * <ol>
     * <li>Create a personal account.</li>
     * <li>Go to schedule webinar page and schedule a webinar with name,
     * description and date.</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Set new attendee limit for the webinar as 1</li>
     * <li>Go to attendee registration page and fill user details to register
     * for webinar resulting in a confirmation page</li>
     * <li>Try to register an user again</li>
     * <li>Verify if it automatically redirects to Webinar Full Page</li>
     * <li>Verify the page message and the webinar name displayed</li>
     * </ol>
     */
    @Test(groups = { Groups.PERSONAL, Groups.REGISTRATION, Groups.REGISTRANT_DENIED })
    public void verifyWebinarFullPageIsServedWhenRegisteringForWebinarWithRegistrationLimitExceeded() {

        this.logger.log("Registration Full - Registrant Limit for Trial: verify WebinarFull page is displayed when registrant count limit is reached");

        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);

        registerForWebinarAndVerifyWebinarFullPage(manageWebinarPage);
    }
    
    /**
     * Test to verify that Webinar Full Page is served when joining a webinar
     * when registration limit has exceeded.
     * <ol>
     * <li>Create a Corp account.</li>
     * <li>Go to schedule webinar page and schedule a webinar with name,
     * description and date.</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Set new attendee limit for the webinar as 1</li>
     * <li>Go to attendee registration page and fill user details to register
     * for webinar resulting in a confirmation page</li>
     * <li>Try to register an user again</li>
     * <li>Verify if it automatically redirects to Webinar Full Page</li>
     * <li>Verify the page message and the webinar name displayed</li>
     * </ol>
     */
    @Test(groups = { Groups.CORPORATE, Groups.REGISTRATION, Groups.REGISTRANT_DENIED })
    public void verifyWebinarFullPageIsServedWhenRegisteringForWebinarWithRegistrationLimitExceededForCorpCustomer() {
        // Create a corporare account, login, schedule a webinar and go to
        // manage webinar page
        
        this.logger.log("Registration Full - Registrant Limit for Corp: verify WebinarFull page is displayed when registrant count limit is reached");
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.CORPORATE);
        registerForWebinarAndVerifyWebinarFullPage(manageWebinarPage);
        
    }

    private void registerForWebinarAndVerifyWebinarFullPage(ManageWebinarPage manageWebinarPage) {
        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();

        // Edit registration settings
        ManageRegistrationSettingsPage manageRegistrationSettingsPage = manageWebinarPage.goToManageRegistrationSettingsPage();

        // Set Registration Limit to 1 and save
        manageRegistrationSettingsPage.setRegistrationLimit("1");
        manageRegistrationSettingsPage.saveRegistration();

        // Register an attendee
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.webDriver);
        RegistrantDetails registrantDetails = new RegistrantDetails();
        registrationPage.registerAttendeeDetailsWithConfirmation(registrantDetails);
        
        // Register 2nd attendee
        RegistrationPage  secondAttendeeRegPage = new RegistrationPage(registrationUrl, this.webDriver);
        RegistrantDetails secondRegistrantDetails = new RegistrantDetails();
        WebinarFullForRegistrationPage webinarFullPage = secondAttendeeRegPage.registerAttendeeGoesToWebinarFullForRegistrationPage(secondRegistrantDetails);

        // Verify correct message is displaying in the webinar full page
        this.logger.log("Verify the page message is : "
                + this.messages.getMessage("trainingFull.title", null, this.locale));
        Assert.assertEquals(webinarFullPage.getPageMessage(),
                this.messages.getMessage("trainingFull.title", null, this.locale));

        // Verify webinar name displayed on the page
        this.logger.log("Verify the webinar name displayed is : " + this.webinarName);
        Assert.assertEquals(this.webinarName, webinarFullPage.getWebinarName());
        manageWebinarPage.close();
        manageRegistrationSettingsPage.close();
        registrationPage.close();
        webinarFullPage.close();
   }
       

}
