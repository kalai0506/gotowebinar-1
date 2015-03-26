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

package com.citrix.g2w.webdriver.tests.attendee.emails;

import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationCanceledPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationConfirmationPage;
import com.citrix.g2w.webdriver.pages.emails.ZimbraEmail;

import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

import java.lang.String;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;



public class RegistrationConfirmationEmailWebDriverTests extends BaseWebDriverTest {

    private static final String CANCEL_LINK_REGEX = "http[^\\s]*/cancel/[0-9]+/[0-9]+";

    /**
     *  Test to verify an attendee has the option to cancel their registration by clicking on the cancel link in registration confirmation email
     *  Steps:
     *  1. Schedule Webinar
     *  2. Register an attendee
     *  3. Verify Email
     *  4. Click on cancel registration link in email
     *  5. Verify text "You're Registered!"
     *  6. Verify the popup dialog element exists by clicking on the cancel registration link on the page
     */

    @Test(timeOut=300000, groups = { Groups.EMAILS, Groups.ATTENDEE_APP, Groups.REGISTRATION_CANCELLED }, description="Emails - RegisterConfirmationEmail - An attendee has the option to cancel their registration by clicking on the cancel link")
    public void verifyAttendeeHasOptionToCancelRegistrationByClickingOnCancelLink() {

        this.logger.log("Emails - RegistrationConfirmationEmail: An attendee has the option to cancel their registration by clicking on the cancel link");

        RegistrantDetails registrantDetails = createWebinarAndRegisterAnAttendee();
        String cancelLink = getRegistrationCancellationLinkFromEmail(registrantDetails);
        RegistrationConfirmationPage registrationCancelPage = openCancelRegistrationPage(cancelLink);
        // Verify the popup dialog element exists by clicking on the cancel registration link on the cancel page
        Assert.assertNotNull(registrationCancelPage.findPresenceOfElement(By.id("cancel-dialog"), 5));
    }

    /**
     *  Test to verify an attendee can cancel their registration after clicking on the cancel link in registration confirmation email
     *  Steps:
     *  1. Schedule Webinar
     *  2. Register an attendee
     *  3. Verify Email
     *  4. Click on cancel registration link in email
     *  5. Verify text "You're Registered!"
     *  6. Click Yes to confirm registration cancellation
     *  7. Verify text "Registration Cancelled"
     *  8. Verify no attendee is registered now
     */

   @Test(timeOut=300000, groups = { Groups.EMAILS, Groups.ATTENDEE_APP, Groups.REGISTRATION_CANCELLED }, description="Emails - RegisterConfirmationEmail - Verify an attendee can cancel their registration after clicking on the cancel link")
    public void verifyAttendeeCanCancelRegistrationAfterClickingOnCancelLink() {
        this.logger.log("Emails - RegistrationConfirmationEmail: An attendee can cancel their registration after clicking on the cancel link");
        RegistrantDetails registrantDetails = createWebinarAndRegisterAnAttendee();
        String cancelLink = getRegistrationCancellationLinkFromEmail(registrantDetails);
        RegistrationConfirmationPage registrationCancelLink = openCancelRegistrationPage(cancelLink);
        registrationCancelLink.clickOnYesInPopup();
        RegistrationCanceledPage registrationCanceledPage = new RegistrationCanceledPage(this.webDriver);
        String myWebinarsUrl = getMyWebinarsPageUrl();
        Assert.assertEquals(registrationCanceledPage.getRegistrationCancelHeader(), this.messages.getMessage("registrationCancelConfirmation.title", null, this.locale));
        MyWebinarsPage myWebinarsPage = new MyWebinarsPage(myWebinarsUrl, this.webDriver);
        // Checking no attendee is registered now
        Assert.assertEquals(myWebinarsPage.getRegistrantCount(), "0");
    }

    /**
     *  Test to verify an attendee can choose not to cancel their registration after clicking on the cancel link in registration confirmation email
     *  Steps:
     *  1. Schedule Webinar
     *  2. Register an attendee
     *  3. Verify Email
     *  4. Click on cancel registration link in email
     *  5. Verify text "You're Registered!"
     *  6. Click No to decline registration cancellation
     *  7. Verify text "You're Registered!"
     *  8. Verify 1 attendee has registered
     */

  @Test(timeOut=300000, groups = { Groups.EMAILS, Groups.ATTENDEE_APP, Groups.REGISTRATION_CANCELLED }, description="Emails - RegisterConfirmationEmail - Verify an attendee can choose not to cancel their registration after clicking on the cancel link")
    public void verifyAttendeeCanChooseNotToCancelRegistrationAfterClickingOnCancelLink() {
        this.logger.log("Emails - RegistrationConfirmationEmail: An attendee can choose not to cancel their registration after clicking on the cancel link");
        RegistrantDetails registrantDetails = createWebinarAndRegisterAnAttendee();
        String cancelLink = getRegistrationCancellationLinkFromEmail(registrantDetails);
        RegistrationConfirmationPage registrationCancelLink = openCancelRegistrationPage(cancelLink);
        registrationCancelLink.clickOnNoInPopup();
        String myWebinarsUrl = getMyWebinarsPageUrl();
        Assert.assertEquals(registrationCancelLink.getConfirmationMessage(), this.messages.getMessage("registrationConfirmation.title", null, this.locale));
        MyWebinarsPage myWebinarsPage = new MyWebinarsPage(myWebinarsUrl, this.webDriver);
        // Checking if 1 attendee is still registered
        Assert.assertEquals(myWebinarsPage.getRegistrantCount(), "1");
    }

    /**
     * Method to create a webinar and register an attendee
     * @return registrantDetails
     */

    private RegistrantDetails createWebinarAndRegisterAnAttendee() {
        // create a personal account, login, schedule a webinar, go to manage page, register an attendee
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.webDriver);
        RegistrantDetails registrantDetails = new RegistrantDetails();
        registrationPage.registerAttendeeDetailsWithConfirmation(registrantDetails);
        return registrantDetails;
    }

    /**
     *  Method to login to the jedix webclient and retrieve the registration cancellation link from the body of the registration confirmation email
     *  @return cancelLink
     */

    private String getRegistrationCancellationLinkFromEmail(RegistrantDetails registrantDetails) {
        String actualEmailBody = "";
        String cancelLink = "";

        String mailSubject = this.messages.getMessage("email.settings.confirmation.default.subject", new String[] { this.webinarName }, this.locale);
        String fromAddress = this.propertyUtil.getProperty("customercare.email");
        ZimbraEmail zimbraEmail = new ZimbraEmail(registrantDetails.getEmailAddress());
        zimbraEmail.login();

        // check whether the registration confirmation mail is present in the inbox
        Assert.assertNotNull(zimbraEmail.getEmail(mailSubject, null, fromAddress));
        Message registrationConfirmationMessage = zimbraEmail.getEmail(mailSubject, null,
                fromAddress);

        try {
            Multipart multiPart = (Multipart) registrationConfirmationMessage.getContent();
            BodyPart bodyPart = multiPart.getBodyPart(0);
            actualEmailBody = (String) bodyPart.getContent();
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // Extract the cancel registration link from the email body
        Pattern p = Pattern.compile(CANCEL_LINK_REGEX);
        Matcher m = p.matcher(actualEmailBody);
        if(m.find()) {
            cancelLink = m.group(0);
        } else {
            Assert.fail("Cancel Registration link not found in the email.");
        }
        zimbraEmail.logout();
        return cancelLink;
    }

    /**
     * Method to open Cancel Registration Page
     * @return registrationCancelPage
     */

    private RegistrationConfirmationPage openCancelRegistrationPage(String cancelLink) {
        // Open the cancel registration link from the email
        RegistrationConfirmationPage registrationCancelPage = new RegistrationConfirmationPage(cancelLink, this.webDriver);
        // Verify the text "You're Registered!"
        Assert.assertEquals(registrationCancelPage.getConfirmationMessage(), this.messages.getMessage("registrationConfirmation.title", null, this.locale));

        return registrationCancelPage;
    }

    /**
     * Method to return the url of the MyWebinars page
     */

     private String getMyWebinarsPageUrl() {
        return this.serviceUrl + "/webinars.tmpl";
    }
}

