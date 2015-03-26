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

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.By;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.ScheduleAWebinarPage;
import com.citrix.g2w.webdriver.pages.managewebinar.ManageRegistrationSettingsPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationConfirmationPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

/**
 * @author: Joel Thames
 * @since: 4/21/2014
 */
public class RegistrationConfirmationPageWebDriverTests extends
BaseWebDriverTest {

    /**
     * Method used to run tests for different locale.
     * 
     * @return Object (two dimensional Array Object)
     */
    @DataProvider
    public Object[][] locales() {
        return new Object[][] { { "en_US" }, { "it_IT" }, { "es_ES" },
                { "fr_FR" }, { "de_DE" } };
    }
    
    @BeforeMethod
    public void testSetup(){
        this.locale =
                StringUtils.parseLocaleString(this.propertyUtil.getProperty("environment.locale"));
    }

    /**
     * <ol>
     * Test Registration is successful for Single Webinar
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a Single webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Submit an email address</li>
     * <li>Verify the confirmation page</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.CORPORATE, Groups.REGISTRATION }, description = "RegistrationConfirmationPage: Register for a Single Webinar sucessful")
    public void verifySuccessfulRegistrationOfSingleWebinar() {
        String firstName = "reg";
        String lastName = "single";
        String email = "reg-single@jedix.com";

        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), firstName, lastName,
                email);
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());
        Assert.assertEquals(registrationConfirmationPage.getWebinarName(),
                this.webinarName);
        Assert.assertTrue(registrationConfirmationPage.isJoinLinkExists());
        System.out.println("registration successful");
        Assert.assertTrue(registrationConfirmationPage.isContactEmailExists());
        Assert.assertTrue(registrationConfirmationPage.isCalendarLinkExists());
        }

    /**
     * <ol>
     * Test Registration is successful for Sequence Webinar
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a Sequence webinar with name
     * </li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Submit an email address</li>
     * <li>Verify the confirmation page:Title,Webinar Name,Webinar Times,Join
     * Link,Calender Link,Contact Email</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationConfirmationPage: Register for a Sequence Webinar sucessful")
    public void verifySuccessfulRegistrationOfSequenceWebinar() {
        long duration = 3600000;
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        DateTime startDate = new DateTime(
                DateTimeZone.forID("America/Los_Angeles")).plusWeeks(3);
        DateTime endDate = startDate.plusDays(3);
        DateTime endTime = endDate.plus(duration);

        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL,
                        ScheduleAWebinarPage.Frequency.DAILY,
                        ScheduleAWebinarPage.WebinarType.SEQUENCE, startDate,
                        endDate, endTime);

        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(
                registrationUrl, this.getWebDriver());
        registrationPage.clickWebinarsSizeLink();

        List<String> expectedWebinarTimes = convertDateAndTimeToList(startDate,
                endTime, "daily");
        List<String> actualWebinarTimes = registrationPage
                .getRecurringWebinarTimesList();
        Assert.assertEquals(expectedWebinarTimes, actualWebinarTimes);
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                registrationUrl, registrantfirstName, registrantlastName,
                registrantemailId);
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());
        registrationConfirmationPage.clickWebinarsSizeLink();
        // Get Webinar times on Confirmation Page
        actualWebinarTimes = registrationConfirmationPage
                .getRecurringWebinarTimesList();
        Assert.assertEquals(actualWebinarTimes, expectedWebinarTimes);
        Assert.assertEquals(registrationConfirmationPage.getWebinarName(),
                this.webinarName);
        Assert.assertTrue(registrationConfirmationPage.isJoinLinkExists());
        Assert.assertTrue(registrationConfirmationPage.isContactEmailExists());
        Assert.assertTrue(registrationConfirmationPage.isCalendarLinkExists());
    }

    /**
     * <ol>
     * Test Registration is successful for Series Webinar
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a Series webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Submit an email address</li>
     * <li>Verify the confirmation page:Title,Webinar Name,Join Link,Calender
     * Link,Contact Email</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationConfirmationPage: Register for a Series Webinar sucessful")
    public void verifySuccessfulRegistrationOfSERIESWebinar() {
        long duration = 3600000;
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        DateTime startDate = new DateTime(
                DateTimeZone.forID("America/Los_Angeles")).plusWeeks(3);
        DateTime endDate = startDate.plusDays(5);
        DateTime endTime = endDate.plus(duration);

        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL,
                        ScheduleAWebinarPage.Frequency.DAILY,
                        ScheduleAWebinarPage.WebinarType.SERIES, startDate,
                        endDate, endTime);

        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(
                registrationUrl, this.getWebDriver());

        List<String> expectedWebinarTimes = convertDateAndTimeToList(startDate,
                endTime, "daily");
        List<String> actualWebinarTimes = registrationPage
                .getDateAndTimeAsListforDifferentAttendees();
        Assert.assertEquals(expectedWebinarTimes, actualWebinarTimes);
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                registrationUrl, registrantfirstName, registrantlastName,
                registrantemailId);
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());
        Assert.assertEquals(registrationConfirmationPage.getWebinarName(),
                this.webinarName);
        Assert.assertTrue(registrationConfirmationPage.isJoinLinkExists());
        Assert.assertTrue(registrationConfirmationPage.isContactEmailExists());
        Assert.assertTrue(registrationConfirmationPage.isCalendarLinkExists());
    }

    /**
     * <ol>
     * Test to verify duplicate registration of email addresses display alert
     * message
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Submit valid details</li>
     * <li>Verify the confirmation page</li>
     * <li>Resubmit registration with same upper case email address</li>
     * <li>Verify that alert message indicate duplicate email is shown</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationPage: Register attendee twice with different case email (confirm duplicate)")
    public void verifyAlreadyRegisteredMessageDisplayedWhenEnteringADuplicateRegistrationEmailWithDifferentCase() {
        String firstName = "test";
        String lastName = "user";
        String email = "test-user@jedix.com";

        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String registrationurl = manageWebinarPage.getRegistrationURL();
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                registrationurl, firstName, lastName, email);
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());

        // Submit form with uppercase email address and validate alert message
        RegistrationConfirmationPage duplicateRegistrationConfirmationPage = setupRegistrationConfirmationPage(
                registrationurl, firstName + "Duplicate", lastName
                + "Duplicate", email.toUpperCase());

        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                duplicateRegistrationConfirmationPage.getTitle());
        String registrationConfirmationurl = this.webDriver.getCurrentUrl();
        Assert.assertTrue((registrationConfirmationurl
                .contains("duplicate=true")));
    }

    /**
     * <ol>
     * Test to verify spaces before and after an email address is allowed
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Get the attendee registration URL from the manage webinar page</li>
     * <li>Submit an email address with spaces before and after the email
     * address</li>
     * <li>Verify the confirmation page</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationPage: Register with spaces before and after the reg email")
    public void verifySuccessfulSubmitOfFormWhenValidEmailWithBeforeAndAfterSpaces() {
        String firstName = "test";
        String lastName = "user";
        String email = "  test-user@jedix.com  ";

        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), firstName, lastName,
                email);
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());
    }

    /**
     * <ol>
     * Test to verify the basic registration is valid
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Click on the registration link</li>
     * <li>Register User and Verify Confirmation Page.</li>
     * <li>Re-Register the same User again and verify the Registration
     * Confirmation URL</li>
     * </ol>
     */
    @Test(groups = { Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationConfirmationPage: Verify Registration Confirmation URL for already registered user")
    public void verifyRegistrationConfirmationForAlreadyRegisteredUser() {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");

        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String registrationURL = manageWebinarPage.getRegistrationURL();
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());
        registrationConfirmationPage = setupRegistrationConfirmationPage(
                registrationURL, registrantfirstName, registrantlastName,
                registrantemailId);
        String registrationConfirmationurl = this.webDriver.getCurrentUrl();
        Assert.assertTrue((registrationConfirmationurl
                .contains("duplicate=true")));
    }

    /**
     * <ol>
     * Test to verify the Organizer Approval required Message on registration
     * confirmation page
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to the manage Registration settings page</li>
     * <li>set manual approval required for webinar</li>
     * <li>Verify the approval required text on Confirmation Page.</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "RegistrationConfirmationPage: approval required")
    public void verifyOrganizerApprovalRequiredToRegister() {

        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        String approvalRequired = this.messages.getMessage(
                "registrationConfirmation.message.approvalRequired", null,
                this.locale);
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        ManageRegistrationSettingsPage manageRegistrationSettingsPage = manageWebinarPage
                .goToManageRegistrationSettingsPage();
        manageRegistrationSettingsPage.enableManualApprovalOfRegistrants();
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        registrationConfirmationPage.findPresenceOfElement(
                By.xpath("//*[contains(text(),'" + approvalRequired + "')]"),
                10);
    }

    /**
     * <ol>
     * Test to verify the Contact Email on Registration Confirmation Page
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Change Contact email by going changing replyToEmail for given webinar
     * </li>
     * <li>Verify the new contact email on Registration Confirmation Page.</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "Registration Confirmation Page - Reply-To Email: verify changes on registration confirmation page")
    public void verifyReplyToEmailOnRegistrationConfirmationPage() {
        String emailID = "jt@dodgers.com";
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        manageWebinarPage.changeReplyToEmail(emailID);
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        Assert.assertEquals(registrationConfirmationPage.getContactEmail(),
                emailID);
    }

    /**
     * <ol>
     * Verify Registration Confirmation Page with different locale
     * <li>Create a personal account based on locale</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Get the attendee registration URL from the manage webinar page and
     * submit the registration details</li>
     * <li>Verify the Registration confirmation page title in different locale.</li>
     * </ol>
     * 
     * @param locale
     *            (locale value)
     */
    @Test(dataProvider = "locales", groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.LOCALE,
            Groups.REGISTRATION }, description = "Registration Confirmation Page - using different locales, verify confirmation and registration pages appear in that language")
    public void verifyRegistrationAndConfirmationPageWithDifferentLocales(
            final String locale) {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");

        this.logger
        .log("Verify user can create account with different locales and schedule a Webinar");

        // Override locale object based on locale value
        this.locale = StringUtils.parseLocaleString(locale);
        // Create Personal account in given locale
        ManageWebinarPage manageWebinarPage = createPersonalAccountLoginAndScheduleWebinar(locale);
        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(
                registrationUrl, this.getWebDriver());

        Assert.assertEquals(this.messages.getMessage("registration.title",
                null, this.locale), registrationPage.getTitle());
        Assert.assertEquals(this.messages.getMessage(
                "registrationFieldType.email", null, this.locale),
                registrationPage.getAttendeeEmailIdLabel());
        Assert.assertTrue(registrationPage.getAttendeeEmailId().isEmpty());
        Assert.assertEquals(this.messages.getMessage(
                "registrationFieldType.givenname", null, this.locale),
                registrationPage.getAttendeeFirstNameLabel());
        Assert.assertTrue(registrationPage.getAttendeeFirstName().isEmpty());
        Assert.assertEquals(this.messages.getMessage(
                "registrationFieldType.surname", null, this.locale),
                registrationPage.getAttendeeLastNameLabel());
        Assert.assertTrue(registrationPage.getAttendeeLastName().isEmpty());
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                registrationUrl, registrantfirstName, registrantlastName,
                registrantemailId);
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());
    }

    /**
     * <ol>
     * verify Direct registrants to your own confirmation page
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Direct registrants to your own confirmation page instead of
     * GoToWebinar confirmation page</li>submit the registration details
     * <li>Verify the registrant is on Custom Confirmation Page.</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "Registration Confirmation Page - Direct registrants to Custom Confirmation page")
    public void verifyRegistrantIsOnCustomConfirmationPage() {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        String URL = "finance.yahoo.com";
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        ManageRegistrationSettingsPage manageRegistrationSettingsPage = manageWebinarPage
                .goToManageRegistrationSettingsPage();
        manageRegistrationSettingsPage.setOwnConfirmationPage(URL);
        // Get the attendee registration URL
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(
                registrationUrl, this.getWebDriver());
        // Submit the form and validate confirmation
        RegistrantDetails registrantDetails = createRegistrantDetails(
                registrantfirstName, registrantlastName, registrantemailId);
        registrationPage.clickToRegisterAttendee(registrantDetails);
        Assert.assertTrue(this.getWebDriver().getCurrentUrl().contains(URL));
    }
    
    
    /**
     * <ol>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "Registration Confirmation Page - Reply-To Email: verify changes on registration confirmation page")
    public void verifyIcsCalendarForSingleWebinar() {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        Assert.assertTrue(registrationConfirmationPage.isCalendarLinkExists());
        String calendarUrl = registrationConfirmationPage.getCalendarUrl();
        Assert.assertEquals(serviceUrl+"/icsCalendar.tmpl?webinar=" + webinarKey + 
                                            "&user=" + registrationConfirmationPage.getRegistrantID(), calendarUrl);
    }
    
    /**
     * <ol>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "Registration Confirmation Page - Reply-To Email: verify changes on registration confirmation page")
    public void verifyIcsCalendarForSeriesWebinar() {
        long duration = 3600000;
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        DateTime startDate = new DateTime(
                DateTimeZone.forID("America/Los_Angeles")).plusWeeks(3);
        DateTime endDate = startDate.plusDays(5);
        DateTime endTime = endDate.plus(duration);

        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL,
                        ScheduleAWebinarPage.Frequency.DAILY,
                        ScheduleAWebinarPage.WebinarType.SERIES, startDate,
                        endDate, endTime);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        Assert.assertTrue(registrationConfirmationPage.isCalendarLinkExists());
        String calendarUrl = registrationConfirmationPage.getCalendarUrl();
        Assert.assertEquals(serviceUrl+"/icsCalendar.tmpl?webinar=" + webinarKey + 
                                            "&user=" + registrationConfirmationPage.getRegistrantID(), calendarUrl);
    }
    
    /**
     * <ol>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRATION }, description = "Registration Confirmation Page - Reply-To Email: verify changes on registration confirmation page")
    public void verifyIcsCalendarForSequenceWebinar() {
        long duration = 3600000;
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        DateTime startDate = new DateTime(
                DateTimeZone.forID("America/Los_Angeles")).plusWeeks(3);
        DateTime endDate = startDate.plusDays(5);
        DateTime endTime = endDate.plus(duration);

        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL,
                        ScheduleAWebinarPage.Frequency.DAILY,
                        ScheduleAWebinarPage.WebinarType.SEQUENCE, startDate,
                        endDate, endTime);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        RegistrationConfirmationPage registrationConfirmationPage = setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        Assert.assertTrue(registrationConfirmationPage.isCalendarLinkExists());
        String calendarUrl = registrationConfirmationPage.getCalendarUrl();
        Assert.assertEquals(serviceUrl+"/icsCalendar.tmpl?webinar=" + webinarKey + 
                                            "&user=" + registrationConfirmationPage.getRegistrantID(), calendarUrl);
    }


    /**
     * test to verify registration submits successfully when all fields
     * are required by organizer
     * <ol>
     * <li>create a personal account, login and schedule a webinar</li>
     * <li>go to registration page and click question tab</li>
     * <li>use toggle to set all fields as required</li>
     * <li>register attendee with all data fields filled out</li>
     * <li>verify on submitting registration form with all fields required it will
     * successfully redirect to registration confirmation page</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRANT_CONFIRMATION })
    public void verifyRegistrationSuccessWhenAllRegistrationFieldsAreRequiredByOrganizer() {

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

        // register attendee with all required fields filled out
        RegistrantDetails registrantDetails = new RegistrantDetails();
        registrantDetails.setAddress("123 Street Avenue");
        registrantDetails.setCity("Trumbull");
        registrantDetails.setState("CT");
        registrantDetails.setIndustry("Accounting");
        registrantDetails.setJobTitle("Supervisor");
        registrantDetails.setNumberOfEmployees("51-100");
        registrantDetails.setOrganization("Acme Corporation");
        registrantDetails.setPurchasingRole("Not involved");
        registrantDetails.setPhone("203-261-0505");
        registrantDetails.setPurchasingTimeframe("No timeframe");
        registrantDetails.setQuestionAndComments("How many breaks will there be?");
        registrantDetails.setCountry("US");
        registrantDetails.setZipCode("06611");
        // verify on submitting registration form with all fields required
        // user successfully redirects to registration confirmation page

        RegistrationConfirmationPage registrationConfirmationPage = registrationPage
                .registerAttendeeDetailsWithConfirmation(registrantDetails);
        
        // verify Registration Confirmation page title
        Assert.assertEquals(this.messages.getMessage(
                "registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());
    }

    /**
     * Test to verify registration submitted successfully without entering
     * optional data when all fields are specified as optional by organizer
     * <ol>
     * <li>create a personal account, login and schedule a webinar</li>
     * <li>Go to Registration and Payment page and click Question Tab</li>
     * <li>Mark All Registration fields="true" & Required="false"</li>
     * <li>Register Attendee without data in fields excepts Name & Email</li>
     * <li>Verify on submitting Registration form without data in optional
     * fields successfully redirect to Registration Confirmation Page</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REGISTRANT_CONFIRMATION })
    public void verifyRegistrationSucesssWhenOptionalFieldsAreLeftBlank() {

        this.logger.log("Verify registration submitted successfully without entering optional"
                + " data when all fields are specified as optional by organizer");

        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);

        // get registration url displayed on the page
        String registrationUrl = manageWebinarPage.getRegistrationURL();

        // go to registration page
        ManageRegistrationSettingsPage manageRegistrationSettingsPage = manageWebinarPage
                .goToManageRegistrationSettingsPage();

        // go to Questions section
        manageRegistrationSettingsPage.gotoQuestions();
        
        // use 'select all' toggle for all fields to be optional
        this.logger.log("set toggle fields checkbox for all optional registration fields");
        manageRegistrationSettingsPage
        .setSelectAllOptionalRegistrantFields();

        // Save registrant fields
        manageRegistrationSettingsPage.saveRegistrantFields();

        // Go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.webDriver);

        // Register attendee only with first name, last name & email address
        RegistrantDetails registrantDetails = new RegistrantDetails();

        // Verify on submitting Registration form without data in optional
        // fields successfully redirect to Registration Confirmation Page
        this.logger.log("Verify on submitting Registration form without data in "
                + "optional fields successfully redirect to Registration Confirmation Page");
        RegistrationConfirmationPage registrationConfirmationPage = registrationPage
                .registerAttendeeDetailsWithConfirmation(registrantDetails);

        // verify Registration Confirmation page title
        
        Assert.assertEquals(this.messages.getMessage("registrationConfirmation.title", null, this.locale),
                registrationConfirmationPage.getTitle());
    }

}
