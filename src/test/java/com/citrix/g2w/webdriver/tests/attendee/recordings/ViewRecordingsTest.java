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
import com.citrix.g2w.webdriver.PropertyUtil;
import com.citrix.g2w.webdriver.pages.recordings.MyRecordingsPage;
import com.citrix.g2w.webdriver.pages.recordings.RegisterForRecordingPage;
import com.citrix.g2w.webdriver.pages.recordings.ViewRecordingPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Verify the view recording page functionality
 *
 * @author spulapura
 * @since: 04/23/2014
 */
public class ViewRecordingsTest extends BaseWebDriverTest {

    private static final String VALID_RECORDING_FILE = "recording.wmv";

    private static final String TEST_FIRST_NAME = "test";

    private static final String TEST_LAST_NAME = "user";

    private static final String TEST_EMAIL = "test-user@jedix.com";

    @Value("${g2w.url}/recordings.tmpl")
    private String myRecordingsPageURL;

    @Value("${attendee.Url}/recording/")
    private String recordingsRegistrationBaseURL;

    /**
     * <ol>
     * Test to verify that the proper codec instructions link is displayed
     * <li>Go to the register for recording page</li>
     * <li>Submit registration details and submit to go to the View Recording page</li>
     * <li>Verify the codec link</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description = "RegisterForRecordingsPage: Registration - Verify that some instructions are given about Codec (or having trouble)")
    public void verifyCodecInstructions() {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();

        RegisterForRecordingPage registerForRecordingPage = new RegisterForRecordingPage(recordingsRegistrationBaseURL + recordingId, this.getWebDriver());
        ViewRecordingPage viewRecordingPage = registerForRecordingPage.submit(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL);
        String expectedCodecLinkText = this.messages.getMessage("recordingView.codec.instructions.link", null, this.locale);
        Assert.assertEquals(expectedCodecLinkText, viewRecordingPage.getCodecLink());
        cleanup();
    }

    /**
     * <ol>
     * Test to verify that the proper codec instructions link is displayed
     * <li>Go to the register for recording page</li>
     * <li>Submit registration details and submit to go to the View Recording page</li>
     * <li>Verify that clicking the View Recording button launches the recording</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description = "ViewRecordings: Verify that clicking the View Recording button launches the recording")
    public void verifyPlayRecording() throws Exception {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();

        RegisterForRecordingPage registerForRecordingPage = new RegisterForRecordingPage(recordingsRegistrationBaseURL + recordingId, this.getWebDriver());
        ViewRecordingPage viewRecordingPage = registerForRecordingPage.submit(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL);
        viewRecordingPage.viewRecording();

        Assert.assertTrue(this.webDriver.getCurrentUrl().contains("akamai"));
        cleanup();
    }

}
