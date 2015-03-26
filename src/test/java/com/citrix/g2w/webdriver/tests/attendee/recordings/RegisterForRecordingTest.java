/*
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

package com.citrix.g2w.webdriver.tests.attendee.recordings;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.pages.recordings.MyRecordingsPage;
import com.citrix.g2w.webdriver.pages.recordings.RegisterForRecordingPage;
import com.citrix.g2w.webdriver.pages.recordings.ViewRecordingPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEqualsNoOrder;

/**
 * Verify the register for recording page functionality
 *
 * @author spulapura
 * @since: 04/23/2014
 */
public class RegisterForRecordingTest extends BaseWebDriverTest {

    private static final String VALID_RECORDING_FILE = "recording.wmv";

    private static final String TEST_FIRST_NAME = "test";

    private static final String TEST_LAST_NAME = "user";

    private static final String TEST_EMAIL = "test-user@jedix.com";

    private static final String TEST_INVALID_EMAIL = "test-user    ";

    @Value("${g2w.url}/recordings.tmpl")
    private String myRecordingsPageURL;

    @Value("${attendee.Url}/recording/")
    private String recordingsRegistrationBaseURL;

    /**
     * <ol>
     * Test to verify that the proper codec instructions link is displayed
     * <li>Create account, upload recording and go to registration page</li>
     * <li>Verify the firstName, lastName and email fields are present</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description = "RegisterForRecordingsPage: Registration - Verify that clicking on Recording Registration URL asked for first, last and email. All required fields.")
    public void verifyRecordingsPageHasRequiredFields() {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();

        RegisterForRecordingPage registerForRecordingPage = new RegisterForRecordingPage(recordingsRegistrationBaseURL + recordingId, this.getWebDriver());

        Assert.assertTrue(registerForRecordingPage.getRegistrantEmail().isEmpty());
        Assert.assertTrue(registerForRecordingPage.getRegistrantGivenName().isEmpty());
        Assert.assertTrue(registerForRecordingPage.getRegistrantSurname().isEmpty());
        cleanup();
    }

    /**
     * <ol>
     * Test to verify that error message is displayed when required fields not submitted
     * <li>Create account, upload recording and go to registration page</li>
     * <li>Submit registration details without filling up fields</li>
     * <li>Verify that the error messages are displayed</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description = "RegisterForRecordingsPage: Registration - Verify that error message is displayed when required fields not submitted.")
    public void verifyFieldRequiredMessageWhenSubmitFormWithoutParameters() {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();

        RegisterForRecordingPage registerForRecordingPage = new RegisterForRecordingPage(recordingsRegistrationBaseURL + recordingId, this.getWebDriver());
        registerForRecordingPage.register();

        List<String> actualErrorMessages = registerForRecordingPage.getErrorMessages();

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
        cleanup();
    }

    /**
     * <ol>
     * Test to verify that they are taken to View Recording page after entering first name, and last name, and email address (validate syntax not that it actually works)
     * <li>Create account, upload recording and go to registration page</li>
     * <li>Submit registration details and submit to go to the View Recording page</li>
     * <li>Verify the title and instructions for viewing the recording</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description = "RegisterForRecordingsPage: Registration - Verify that they are taken to View Recording page after entering first name, and last name, and email address (validate syntax not that it actually works)")
    public void verifySuccessfulSubmitOfForm() {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();

        RegisterForRecordingPage registerForRecordingPage = new RegisterForRecordingPage(recordingsRegistrationBaseURL + recordingId, this.getWebDriver());
        ViewRecordingPage viewRecordingPage = registerForRecordingPage.submit(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL);
        Assert.assertEquals(this.messages.getMessage("recordingView.title", null, this.locale), viewRecordingPage.getTitle());
        Assert.assertEquals(this.messages.getMessage("recordingView.instructions", null, this.locale), viewRecordingPage.getInstructions());
        cleanup();
    }
}
