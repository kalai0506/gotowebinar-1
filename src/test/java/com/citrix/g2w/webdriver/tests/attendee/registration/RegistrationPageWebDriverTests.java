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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.citrix.g2w.webdriver.pages.MyAccountPage;
import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.ScheduleAWebinarPage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.managewebinar.ManageRegistrationSettingsPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationConfirmationPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

import static org.testng.Assert.assertEqualsNoOrder;

/**
 * @author: Sudip Pulapura
 * @since: 11/12/13
 */
public class RegistrationPageWebDriverTests extends BaseWebDriverTest {
    
    /**
     * <ol>
     * Test to verify the basic registration is valid
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Click on the registration link</li>
     * <li>Verify the title, email, first name and last name labels</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationPage: verify page")
    public void verifyTitleAndExpectedFieldsArePresentOnPage() {

        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);

        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl,
                    this.getWebDriver());

        Assert.assertEquals(this.messages.getMessage("registration.title", null, this.locale), registrationPage.getTitle());
        Assert.assertEquals(this.messages.getMessage("registrationFieldType.email", null, this.locale), registrationPage.getAttendeeEmailIdLabel());
        Assert.assertTrue(registrationPage.getAttendeeEmailId().isEmpty());
        Assert.assertEquals(this.messages.getMessage("registrationFieldType.givenname", null, this.locale), registrationPage.getAttendeeFirstNameLabel());
        Assert.assertTrue(registrationPage.getAttendeeFirstName().isEmpty());
        Assert.assertEquals(this.messages.getMessage("registrationFieldType.surname", null, this.locale), registrationPage.getAttendeeLastNameLabel());
        Assert.assertTrue(registrationPage.getAttendeeLastName().isEmpty());
    }

    /**
     * <ol>
     * Test to verify registration page errors when the form is incomplete.
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Don't fill some of the fields which is mandatory and submit.</li>
     * <li>Verify the page error(s)</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationPage: Submit without parameters for mandatory fields")
    public void verifyFieldRequiredMessageWhenSubmitFormWithoutMandatoryParameters() {

        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);

        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl,
                this.getWebDriver());

        // Submit the form and validate error messages
        registrationPage = registrationPage.registerAttendeeDetails(null);

        // Get the error messages listed in registration validation page
        List<String> actualErrorMessages = registrationPage.getErrorMessages();

        // List to get error messages from message resource bundle
        List<String> expectedErrorMessages = new ArrayList<String>();
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.givenName[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.surname[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.email[not.blank]", null,
                this.locale));

        this.logger.log("Verify error messages listed in registration page");
        // Verify the error messages appeared in registration page
        assertEqualsNoOrder(expectedErrorMessages.toArray(), actualErrorMessages.toArray());
    }

    /**
     * <ol>
     * Test to verify registration page errors when the form is incomplete.
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>go to registration page and click question tab</li>
     * <li>use toggle to set all fields as required</li>
     * <li>Don't fill some of the fields which is mandatory and submit.</li>
     * <li>Verify the page error(s) for each field set as Required</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationPage: Submit without parameters configured as required")
    public void verifyFieldRequiredMessageWhenSubmitFormWithoutRequiredParameters() {

        // create a personal account, login, schedule a webinar and go to
        // manage webinar page
 
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);

        // get registration url displayed on the page
        String registrationUrl = manageWebinarPage.getRegistrationURL();

        // go to registration page
        ManageRegistrationSettingsPage manageRegistrationSettingsPage = manageWebinarPage
                .goToManageRegistrationSettingsPage();

        // go to questions section
        manageRegistrationSettingsPage.gotoQuestions();
        
        // use 'select all' toggle for all fields to be required
        this.logger.log("set toggle required checkbox for all registration fields");
        manageRegistrationSettingsPage
        .setSelectAllRequiredRegistrantFields();
        
        // save registrant fields
        manageRegistrationSettingsPage.saveRegistrantFields();

        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.webDriver);

        // Submit the form and validate error messages
	    registrationPage = registrationPage.registerAttendeeDetails(null);

        // Get the error messages listed in registration validation page
        List<String> actualErrorMessages = registrationPage.getErrorMessages();

        // List to get error messages from message resource bundle
        List<String> expectedErrorMessages = new ArrayList<String>();
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.givenName[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.surname[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.email[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.address[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.city[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.state[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.country[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.zip[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.industry[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.phone[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.organization[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.jobTitle[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.purchasingRole[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.purchasingTimeFrame[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.numberOfEmployees[not.blank]", null,
                this.locale));
        expectedErrorMessages.add(this.messages.getMessage("Registration.RegistrantInfo.comments[not.blank]", null,
                this.locale));
        

        this.logger.log("Verify error messages listed in registration page");
        // Verify the error messages appeared in registration page
        assertEqualsNoOrder(expectedErrorMessages.toArray(), actualErrorMessages.toArray());
        }
    
        
    /**
     * <ol>
     * Test to verify error is shown on invalid email address
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Submit an with an invalid email address</li>
     * <li>Verify the error message</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "Registration page: Verify Error Message when attempting to submit an invalid email address")
    public void verifyErrorMessageWhenFormSubmittedWithInvalidEmail() {
        String firstName = "test";
        String lastName = "user";
        String email = "test-user";

        this.logger.log("Verify invalid email address");
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        RegistrationPage registrationPage = setupRegistrationPageWithErrors(manageWebinarPage.getRegistrationURL(), firstName, lastName, email);
        // Get the error messages listed in registration validation page

        List<String> actualErrorMessages = registrationPage.getErrorMessages();

        // List to get error messages from message resource bundle
        List<String> expectedErrorMessages = new ArrayList<String>();
        expectedErrorMessages.add(this.messages.getMessage("typeMismatch.javax.mail.internet.InternetAddress", new Object[]{"email address"}, this.locale));

        this.logger.log("Verify error messages listed in registration page");
        // Verify the error messages appeared in registration page
        assertEqualsNoOrder(expectedErrorMessages.toArray(), actualErrorMessages.toArray());
    }

    /**
     * <ol>
     * Test to verify registration page popup has correct listing of webinar times
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Verify webinar times on registration page</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "Registration page - Recurring Series: verify Registration page has pop-up box listing all available dates")
    public void verifyWebinarTimesDisplayedForEachSessionForARecurringWebinar() {
        long duration = 3600000;

        DateTime startDate = new DateTime(DateTimeZone.forID("America/Los_Angeles")).plusWeeks(3);
        DateTime endDate = startDate.plusDays(1);
        DateTime endTime = endDate.plus(duration);

        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL, ScheduleAWebinarPage.Frequency.DAILY,
                ScheduleAWebinarPage.WebinarType.SERIES, startDate, endDate, endTime);

        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl,
                this.getWebDriver());

        List<String> expectedWebinarTimes = convertDateAndTimeToList(startDate, endTime, "daily");
        List<String> actualWebinarTimes = registrationPage.getDateAndTimeAsListforDifferentAttendees();
        Assert.assertEquals(expectedWebinarTimes, actualWebinarTimes);
    }

    /**
     * <ol>
     * Test to verify registration page popup has correct listing of webinar times
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Verify webinar times on registration page</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "Registration page - Recurring Sequence: verify Registration page has pop-up box listing all available dates")
    public void verifyWebinarTimesDisplayedForEachSessionForASequenceWebinar() {
        long duration = 3600000;

        DateTime startDate = new DateTime(DateTimeZone.forID("America/Los_Angeles")).plusWeeks(3);
        DateTime endDate = startDate.plusDays(3);
        DateTime endTime = endDate.plus(duration);


        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL, ScheduleAWebinarPage.Frequency.DAILY,
                ScheduleAWebinarPage.WebinarType.SEQUENCE, startDate, endDate, endTime);

        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl,
                this.getWebDriver());
        registrationPage.clickWebinarsSizeLink();

        List<String> expectedWebinarTimes = convertDateAndTimeToList(startDate, endTime, "daily");
        List<String> actualWebinarTimes = registrationPage.getRecurringWebinarTimesList();
        Assert.assertEquals(actualWebinarTimes, expectedWebinarTimes);
    }
    
    /**
     * <ol>
     * Test to verify the registration & confirmation page in different locale
     * <li>Create a personal account</li>
     * <li>Change Primary Language from English to German.</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Click on the registration link</li>
     * <li>Verify the title, email, first name and last name labels after change
     * in Primary language.</li>
     * <li>Verify Registration Confirmation page after change in Primary
     * language.
     * </ol>
     */

    @Test(groups = { Groups.ATTENDEE_APP,Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationPage: When using a different language with G2W Global, registration and confirmation pages always appear in that language")
    public void verifyRegistrationPageInDifferentLocale() {

        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");

        // Create a personal account, login, change Primary Language & schedule
        // a webinar with changed language.

        MyWebinarsPage myWebinarPage = this
                .createAccountLoginAndGoToMyWebinar(Groups.PERSONAL);        
        //GoTo My Account page and change Locale and then Schedule Webinar.
        
        MyAccountPage myAccountPage = myWebinarPage.gotoMyAccountPage();  
        myAccountPage.editPrimaryLanguage(Locale.GERMAN.getLanguage());
        myAccountPage.gotoWebinarPage();
        ScheduleAWebinarPage scheduleWebinarPage = myWebinarPage
                .gotoScheduleAWebinarPage();

        ManageWebinarPage manageWebinarPage = scheduleWebinar(scheduleWebinarPage);
        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(
                registrationUrl, this.getWebDriver());
        Assert.assertEquals(this.messages.getMessage("registration.title",
                null, Locale.GERMAN), registrationPage.getTitle());
        Assert.assertEquals(this.messages.getMessage(
                "registrationFieldType.email", null, Locale.GERMAN),
                registrationPage.getAttendeeEmailIdLabel());

        Assert.assertTrue(registrationPage.getAttendeeEmailId().isEmpty());
        Assert.assertEquals(this.messages.getMessage(
                "registrationFieldType.givenname", null, Locale.GERMAN),
                registrationPage.getAttendeeFirstNameLabel());
        Assert.assertTrue(registrationPage.getAttendeeFirstName().isEmpty());
        Assert.assertEquals(this.messages.getMessage(
                "registrationFieldType.surname", null, Locale.GERMAN),
                registrationPage.getAttendeeLastNameLabel());
        Assert.assertTrue(registrationPage.getAttendeeLastName().isEmpty());
        RegistrantDetails registrantDetails = createRegistrantDetails(
                registrantfirstName, registrantlastName, registrantemailId);
        RegistrationConfirmationPage registrationConfirmationPage = registrationPage
                .registerAttendeeDetailsWithConfirmation(registrantDetails);
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, Locale.GERMAN),
                registrationConfirmationPage.getTitle());

    }

    /**
     * <ol>
     * Test to verify the basic registration is valid
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Age Webinar</li>
     * <li>Click on the registration link</li>
     * <li>Verify Webinar Over Title is shown when user Register for webinar
     * which is over.</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationPage: verify Registration flow When User tries to Register for Webinar which is Over.")
    public void verifyRegistrationWhenWebinarIsOver() {
        String webinarOverMessage = this.messages.getMessage(
                "webinarOver.message", null, this.locale);
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        Long webinarId = manageWebinarPage.getWebinarId();
        DateTime webinarDateTime = manageWebinarPage.getDateTime();
        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        DateTime agedTime = webinarDateTime.minusDays(1);
        Long hrsToAge = (webinarDateTime.getMillis() - agedTime.getMillis()) / 3600000;
        // Age webinar
        ageWebinar(manageWebinarPage, webinarKey, webinarId, hrsToAge);
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(
                registrationUrl, this.getWebDriver());
        Assert.assertEquals(this.messages.getMessage("webinarOver.title", null,
                this.locale), registrationPage.getTitle());
        registrationPage.findPresenceOfElement(
                By.xpath("//*[contains(text(),'" + webinarOverMessage + "')]"),
                10);
    }
    private RegistrationPage setupRegistrationPageWithErrors(String registrationUrl, String firstName, String lastName, String email) {
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.getWebDriver());

        // Submit the form and validate confirmation
        RegistrantDetails registrantDetails = createRegistrantDetails(firstName, lastName, email);
        return registrationPage.registerAttendeeDetails(registrantDetails);
    }
}
