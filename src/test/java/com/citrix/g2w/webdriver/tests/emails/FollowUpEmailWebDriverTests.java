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
package com.citrix.g2w.webdriver.tests.emails;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.NotAvailablePage;
import com.citrix.g2w.webdriver.pages.emails.ZimbraEmail;
import com.citrix.g2w.webdriver.pages.managewebinar.ManageFollowUpEmailPage;
import com.citrix.g2w.webdriver.pages.recordings.MyRecordingsPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationConfirmationPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

/**
 * @author: adas
 */
public class FollowUpEmailWebDriverTests extends BaseWebDriverTest {

    private ManageWebinarPage manageWebinarPage;
    private static final String RECORDING_FILE = "recording.wmv";

    /**
     * Method used to run tests for different Account State.
     * 
     * @return Object (two dimensional Array Object)
     */
    @DataProvider
    public Object[][] accountState() {
        return new Object[][] { { "suspended" }, { "unlicensed" } };
    }

    /**
     * Test to verify that No Followup emails are being sent when user is
     * suspended/unlicensed.
     * <ol>
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Register attendee(s) for the webinar</li>
     * <li>Start webinar and few Registrant will join the webinar</li>
     * <li>Age the webinar by one day</li>
     * <li>Now suspend/unlicensed the user</li>
     * <li>Invoke the mbean to trigger Followup email</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Followup Email doesn't exist from suspended user/unlicensed
     * User.</li>
     * </ol>
     */
    @Test(timeOut=300000, dataProvider = "accountState", groups = { Groups.PERSONAL,
            Groups.FOLLOWUP_EMAIL, Groups.EMAILS }, description = "Emails - FollowUpEmail: Verify No followup emails are being sent to absentee and attendee")
    public void verifyNoFollowUpEmails(final String accountState) {
        // create account, login and schedule a webinar
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        Long webinarId = manageWebinarPage.getWebinarId();
        String webinarName = manageWebinarPage.getWebinarName();
        DateTime webinarDateTime = manageWebinarPage.getDateTime();
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        DateTime agedTime = webinarDateTime.minusDays(1);
        Long hrsToAge = (webinarDateTime.getMillis() - agedTime.getMillis()) / 3600000;

        // Setup follow up email for the webinar
        setUpFollowUpEmailForWebinar(manageWebinarPage);
        manageWebinarPage.logOut();
        // Register an attendee who will be missing the webinar (Absentee)
        RegistrantDetails absenteeDetails = registerAbsentee(registrationUrl);

        // Register an attendee who will be attending the webinar
        Map<Long, RegistrantDetails> attendeeMap = registerAttendee(registrationUrl);

        // Start and end the webinar with attendee
        startAddAttendeeAndEndWebinar(authService.getAuthToken(
                personalAccount.getEmail(), personalAccount.getPassword()),
                personalAccount.getUserKey(), webinarId, webinarDateTime,
                attendeeMap, 3000L);
        // age webinar by 1 day
        ageWebinar(manageWebinarPage, webinarKey, webinarId, hrsToAge);

        if (accountState.equals("suspended")) {
            // Suspend User
            suspendUser(this.personalAccount.getUserKey());
        }
        if (accountState.equals("unlicensed")) {
            // Unlicense User
            unLicenseUser(this.personalAccount.getUserKey(),
                    this.personalAccount.getLicenseKey());
        }
        // trigger follow up email
        triggerFollowUpEmailForWebinar(webinarKey);
        verifyFollowUpEmailForSuspendedUser(webinarName, webinarKey,
                absenteeDetails, attendeeMap);
    }

    /**
     * Test to verify Followup emails are being sent when user is Unsuspended.
     * <ol>
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Register attendee(s) for the webinar</li>
     * <li>Start webinar and few Registrant will join the webinar</li>
     * <li>Age the webinar by one day</li>
     * <li>Now suspend the user</li>
     * <li>Invoke the mbean to trigger Followup email</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Followup Email doesn't exist from suspended user/unlicensed
     * User.</li>
     * <li>un-suspend the user</li>
     * <li>Invoke the mbean to trigger Followup mail</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Followup Email exist from unsuspended user.</li>
     * </ol>
     */
    @Test(timeOut=300000, groups = { Groups.PERSONAL, Groups.FOLLOWUP_EMAIL, Groups.EMAILS }, description = "Emails - FollowUpEmail: Verify followup emails are being sent to absentee and attendee from Unsuspended user")
    public void verifyFollowUpEmailsForUnSuspendedUser() {
        // create account, login and schedule a webinar
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        Long webinarId = manageWebinarPage.getWebinarId();
        String webinarName = manageWebinarPage.getWebinarName();
        DateTime webinarDateTime = manageWebinarPage.getDateTime();
        String registrationUrl = manageWebinarPage.getRegistrationURL();
        DateTime agedTime = webinarDateTime.minusDays(1);
        Long hrsToAge = (webinarDateTime.getMillis() - agedTime.getMillis()) / 3600000;

        // Setup follow up email for the webinar
        setUpFollowUpEmailForWebinar(manageWebinarPage);
        manageWebinarPage.logOut();
        // Register an attendee who will be missing the webinar (Absentee)
        RegistrantDetails absenteeDetails = registerAbsentee(registrationUrl);

        // Register an attendee who will be attending the webinar
        Map<Long, RegistrantDetails> attendeeMap = registerAttendee(registrationUrl);

        // Start and end the webinar with attendee
        startAddAttendeeAndEndWebinar(authService.getAuthToken(
                personalAccount.getEmail(), personalAccount.getPassword()),
                personalAccount.getUserKey(), webinarId, webinarDateTime,
                attendeeMap, 3000L);
        // age webinar by 1 day
        ageWebinar(manageWebinarPage, webinarKey, webinarId, hrsToAge);

        // Suspend User
        suspendUser(this.personalAccount.getUserKey());

        // trigger follow up email
        triggerFollowUpEmailForWebinar(webinarKey);
        verifyFollowUpEmailForSuspendedUser(webinarName, webinarKey,
                absenteeDetails, attendeeMap);

        // Unsuspend User
        unSuspendUser(this.personalAccount.getUserKey());
        // trigger follow up email
        triggerFollowUpEmailForWebinar(webinarKey);
        // Verify follow up email for absentee
        verifyFollowUpEmailForUnSuspendedUser(webinarName, webinarKey,
                absenteeDetails, attendeeMap);
    }

    /**
     * Test to verify that 'Webinar Not Available' message is displayed when
     * attendee or absentee clicks on the recording links in the follow up email
     * after the recording has been deleted by the organizer.
     * <ol>
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Get webinar id and webinar key</li>
     * <li>Add a recording in the MyRecordings Page</li>
     * <li>Configure the follow up email for absentee with uploaded recording.</li>
     * <li>Register attendee for the webinar</li>
     * <li>Run the webinar with out attendees</li>
     * <li>Age the webinar by 24 H to enable follow up email</li>
     * <li>Invoke the mbean to trigger follow up mail</li>
     * <li>Deletes the uploaded recording which was configured in the follow up
     * email from organizers MyRecording page</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify the absentee gets the proper error message when the recording
     * is not available</li>
     * </ol>
     */
    @Test(timeOut=300000, groups = { Groups.PERSONAL, Groups.MANAGE_FOLLOWUP_EMAIL,
            Groups.EMAILS, Groups.RECORDINGS, Groups.ATTENDEE_APP,
            Groups.HTML_UNIT_FAILURE }, description = "Emails - FollowUpEmail: Delete recording after followup emails were sent to absentee and attendee")
    public void verifyDeleteRecordingAfterFollowUpEmailSentForWebinar() {

        // create account, login and schedule a webinar
        manageWebinarPage = createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        Long webinarId = manageWebinarPage.getWebinarId();
        String webinarName = manageWebinarPage.getWebinarName();
        DateTime webinarDateTime = manageWebinarPage.getDateTime();
        String registrationUrl = manageWebinarPage.getRegistrationURL();

        MyRecordingsPage myRecordingsPage = manageWebinarPage
                .gotoMyRecordingsPage();
        myRecordingsPage.uploadFile(getAbsoluteFilePath("Recordings"
                + File.separator + RECORDING_FILE));

        // Navigate again to my recordings page to verify the uploaded recording
        myRecordingsPage = myRecordingsPage.gotoMyRecordingsPage();
        String recordingId = myRecordingsPage.getRecordingIds().get(0);

        MyWebinarsPage myWebinarPage = myRecordingsPage.gotoMyWebinarsPage();
        manageWebinarPage = myWebinarPage
                .goToManageWebinarPageOnWebinarKey(webinarKey);
        // Setup follow up email for the webinar
        setUpFollowUpEmailWithRecordingForWebinar(recordingId);
        manageWebinarPage.logOut();

        // Register an attendee who will be missing the webinar (Absentee)
        RegistrantDetails absenteeDetails = new RegistrantDetails();
        RegistrationPage absenteeRegistrationPage = new RegistrationPage(
                registrationUrl, webDriver);
        absenteeRegistrationPage
        .registerAttendeeDetailsWithConfirmation(absenteeDetails);

        // Register an attendee who will be attending the webinar
        RegistrantDetails attendeeDetails = new RegistrantDetails();
        RegistrationPage attendeeRegistrationPage = new RegistrationPage(
                registrationUrl, webDriver);
        RegistrationConfirmationPage confirmationPage = attendeeRegistrationPage
                .registerAttendeeDetailsWithConfirmation(attendeeDetails);
        Map<Long, RegistrantDetails> attendeeMap = new HashMap<Long, RegistrantDetails>(
                2);
        attendeeMap.put(Long.valueOf(confirmationPage.getRegistrantID()),
                attendeeDetails);

        // Start and end the webinar with attendee
        startAddAttendeeAndEndWebinar(authService.getAuthToken(
                personalAccount.getEmail(), personalAccount.getPassword()),
                personalAccount.getUserKey(), webinarId, webinarDateTime,
                attendeeMap, 3000L);
        // age webinar by 1 day
        DateTime agedTime = webinarDateTime.minusDays(1);
        Long hrsToAge = (webinarDateTime.getMillis() - agedTime.getMillis()) / 3600000;
        ageWebinar(manageWebinarPage, webinarKey, webinarId, hrsToAge);

        // trigger follow up email
        triggerFollowUpEmailForWebinar(webinarKey);

        // delete the uploaded recording
        cleanup();

        // Verify the follow up email for absentee and verify the email details
        String absenteeEmailSubject = messages.getMessage(
                "email.settings.followUp.absentee.default.subject",
                new String[] { webinarName }, locale);
        List<String> expectedMailBody = new ArrayList<String>(1);
        expectedMailBody.add(messages.getMessage(
                "email.settings.followUp.absentee.default.body", null, locale));
        verifyFollowUpEmailForWebinar(absenteeDetails.getEmailAddress(),
                absenteeEmailSubject, expectedMailBody);
        expectedMailBody.clear();
        // Verify the follow up email for attendee and verify the email details

        String attendeeEmailSubject = messages.getMessage(
                "email.settings.followUp.default.subject",
                new String[] { webinarName }, locale);
        expectedMailBody.add(messages.getMessage(
                "email.settings.followUp.default.body", null, locale));
        verifyFollowUpEmailForWebinar(attendeeDetails.getEmailAddress(),
                attendeeEmailSubject, expectedMailBody);
    }

    /**
     * Setup follow up email for attendees and absentees for the webinar
     * 
     * @param recordingId
     *            recording to be sent in follow up email
     */
    private void setUpFollowUpEmailWithRecordingForWebinar(String recordingId) {

        String daysOffsetSelectionMessage = messages.getMessage(
                "manageFollowUpEmail.followUpEmailOffset.day_1", null, locale);

        // Navigate to Manage Follow up Email settings for absentee and setup
        // follow up email
        ManageFollowUpEmailPage manageFollowUpEmailPageForAbsentees = manageWebinarPage
                .goToFollowUpEmailPageForAbsentees();
        manageWebinarPage = manageFollowUpEmailPageForAbsentees
                .sendFollowUpEmailForWebinarWithRecording(
                        daysOffsetSelectionMessage, recordingId);

        // Navigate to Manage Follow up Email settings for attendee by clicking
        // on edit follow up email link
        ManageFollowUpEmailPage manageFollowUpEmailPageForAttendees = manageWebinarPage
                .goToFollowUpEmailPageForAttendee();
        manageWebinarPage = manageFollowUpEmailPageForAttendees
                .sendFollowUpEmailForWebinarWithRecording(
                        daysOffsetSelectionMessage, recordingId);
    }

    /**
     * Method to verify the email for registrat with expected values.
     * 
     * @param userEmail
     *            (registrant email)
     * @param mailSubject
     *            (expected subject line)
     * @param listOfExpectedDetailsInMailBody
     *            (expected body contents)
     */
    private void verifyFollowUpEmailForWebinar(String userEmail,
            String mailSubject, List<String> listOfExpectedDetailsInMailBody) {
        // login to user email account
        ZimbraEmail zimbraEmail = new ZimbraEmail(userEmail);
        zimbraEmail.login();

        // search the inbox for mail with expected subject, to and from address
        String fromAddress = propertyUtil.getProperty("customercare.email");
        Message message = zimbraEmail.getEmail(mailSubject, userEmail,
                fromAddress);

        // verify if mail has been received and contains expected contents
        logger.log("Verify if mail has been received");
        Assert.assertNotNull(message);
        // Get email message body.
        String actualEmailBody = "";
        try {
            Multipart multiPart = (Multipart) message.getContent();
            BodyPart bodyPart = multiPart.getBodyPart(0);
            actualEmailBody = (String) bodyPart.getContent();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.log("Exception in parsing content for mail : "
                    + ex.getMessage());
        }
        zimbraEmail.logout();
        // Verifies the email content
        Assert.assertNotNull(actualEmailBody);
        Assert.assertTrue(isExpectedContentExistsInEmail(actualEmailBody,
                listOfExpectedDetailsInMailBody));
        verifyWatchRecordingLink(actualEmailBody);
    }

    /**
     * Method to check the watch recording link the email. Verifies the
     * resulting not available page
     * 
     * @param messageObject
     *            (message object)
     */
    private void verifyWatchRecordingLink(String emailBody) {

        String viewRecordingLink = null;
        String emailSuffix = propertyUtil.getProperty("email.suffix");
        String viewRecordingURLRegEx = "http[\\w\\W]*/recording/viewRecording/[0-9]+/[0-9]+/[\\w\\W]*.@"
                + emailSuffix;

        // Extracts the view recording URL link from the email
        Pattern p = Pattern.compile(viewRecordingURLRegEx);
        Matcher m = p.matcher(emailBody);
        if (m.find()) {
            viewRecordingLink = m.group(0);
        }

        // Verifies the watch recording link in the email
        Assert.assertNotNull(viewRecordingLink);

        // Access the view recording link
        webDriver.get(viewRecordingLink);
        NotAvailablePage notAvailablePage = new NotAvailablePage(webDriver);
        String notAvailableTitle = messages.getMessage("notAvailable.title",
                null, locale);
        String notAvailableMessage = messages.getMessage(
                "notAvailable.message", null, locale);

        // Verifies not available page title and message
        Assert.assertEquals(notAvailablePage.getTitle(), notAvailableTitle);
        Assert.assertEquals(notAvailablePage.getPageMessage(),
                notAvailableMessage);
    }

    // Register an attendee who will be missing the webinar (Absentee)
    private RegistrantDetails registerAbsentee(String registrationUrl) {
        RegistrantDetails absenteeDetails = new RegistrantDetails();
        RegistrationPage absenteeRegistrationPage = new RegistrationPage(
                registrationUrl, webDriver);
        absenteeRegistrationPage
        .registerAttendeeDetailsWithConfirmation(absenteeDetails);
        return absenteeDetails;
    }

    // Register an attendee who will be attending the webinar
    private Map<Long, RegistrantDetails> registerAttendee(String registrationUrl) {

        RegistrantDetails attendeeDetails = new RegistrantDetails();
        RegistrationPage attendeeRegistrationPage = new RegistrationPage(
                registrationUrl, webDriver);
        RegistrationConfirmationPage confirmationPage = attendeeRegistrationPage
                .registerAttendeeDetailsWithConfirmation(attendeeDetails);
        Map<Long, RegistrantDetails> attendeeMap = new HashMap<Long, RegistrantDetails>(
                2);
        attendeeMap.put(Long.valueOf(confirmationPage.getRegistrantID()),
                attendeeDetails);
        return attendeeMap;
    }

    /**
     * Method to verify no Followup email in both attendee & absentee Email
     * Inbox.
     * 
     * @param webinarName
     * @param webinarKey
     * @param absenteeDetails
     * @param attendeeMap
     */
    private void verifyFollowUpEmailForSuspendedUser(String webinarName,
            Long webinarKey, RegistrantDetails absenteeDetails,
            Map<Long, RegistrantDetails> attendeeMap) {
        // Verify follow up email for absentee
        String absenteeEmailSubject = messages.getMessage(
                "email.settings.followUp.absentee.default.subject",
                new String[] { webinarName }, locale);
        if (isEmailReceievedForWebinar(absenteeDetails.getEmailAddress(),
                absenteeEmailSubject)) {
            Assert.fail("Registrants are getting Webinar's followup Email from suspended/unlicensed user.");
        }
        // Verify follow up email for attendee
        String attendeeEmailSubject = messages.getMessage(
                "email.settings.followUp.default.subject",
                new String[] { webinarName }, locale);
        for (Entry<Long, RegistrantDetails> entry : attendeeMap.entrySet()) {
            if (isEmailReceievedForWebinar(entry.getValue().getEmailAddress(),
                    attendeeEmailSubject)) {
                Assert.fail("Registrants are getting Webinar's followup Email from suspended/unlicensed user.");
            }

        }
    }

    /**
     * Method to verify followup email exists in both attendee & Absentee Email inbox.
     * 
     * @param webinarName
     * @param webinarKey
     * @param absenteeDetails
     * @param attendeeMap
     */

    private void verifyFollowUpEmailForUnSuspendedUser(String webinarName,
            Long webinarKey, RegistrantDetails absenteeDetails,
            Map<Long, RegistrantDetails> attendeeMap) {
        // Verify follow up email for absentee
        String absenteeEmailSubject = messages.getMessage(
                "email.settings.followUp.absentee.default.subject",
                new String[] { webinarName }, locale);
        if (!(isEmailReceievedForWebinar(absenteeDetails.getEmailAddress(),
                absenteeEmailSubject))) {
            Assert.fail("Registrants are not getting Webinar's followup Email from unsuspended user.");
        }
        // Verify follow up email for attendee
        String attendeeEmailSubject = messages.getMessage(
                "email.settings.followUp.default.subject",
                new String[] { webinarName }, locale);
        for (Entry<Long, RegistrantDetails> entry : attendeeMap.entrySet()) {
            if (!(isEmailReceievedForWebinar(
                    entry.getValue().getEmailAddress(), attendeeEmailSubject))) {
                Assert.fail("Registrants are not getting Webinar's followup Email from unsuspended user.");
            }

        }
    }

}
