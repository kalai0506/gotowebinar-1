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
import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.recordings.MyRecordingsPage;
import com.citrix.g2w.webdriver.pages.recordings.RecordingViewData;
import com.citrix.g2w.webdriver.pages.recordings.RecordingViewsPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;
import com.citrix.g2w.webdriver.util.ExcelDataReader;

import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Verify the recording views page
 *
 * @author sudip
 * @since: 4/29/14
 */
public class RecordingViewsTest extends BaseWebDriverTest {

    private static final String VALID_RECORDING_FILE = "recording.wmv";

    private static final String VALID_RECORDING_FILE2 = "recording2.wmv";

    private static final String INVALID_RECORDING_FILE = "unsupported-format.avi";

    private static final String TEST_FIRST_NAME = "test";

    private static final String TEST_LAST_NAME = "user";

    private static final String TEST_EMAIL = "test-user@jedix.com";

    @Value("${g2w.url}/recordings.tmpl")
    private String myRecordingsPageURL;

    @Value("${attendee.Url}/recording/")
    private String recordingsRegistrationBaseURL;

    /**
     * <ol>
     * Test to verify recording name appears at top of page.
     * <li>Create a personal account, upload a recording and logout</li>
     * <li>Register for recording</li>
     * <li>Login and go to RecordingViews page</li>
     * <li>Verify recording name appears at top</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description="RecordingViews: Verify that recording name appears at top")
    public void verifyRecordingNameIsDisplayed() {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String email = getEmail();
        String password = getPassword();

        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();
        registerAndViewRecording(recordingsRegistrationBaseURL + recordingId, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL);

        MyWebinarsPage myWebinarPage = loginG2W(email, password);
        myRecordingsPage = myWebinarPage.gotoMyRecordingsPage();
        RecordingViewsPage recordingViewsPage = myRecordingsPage.gotoRecordingViewPage(recordingId);

        Assert.assertTrue(recordingViewsPage.getHeading().contains(VALID_RECORDING_FILE));
        myRecordingsPage.logOut();
        cleanup();
    }

    /**
     * <ol>
     * Test to verify that Views # on My Recordings MATCHES EXACTLY to the number of names on the Recordings Views List
     * <li>Create a personal account, upload a recording and logout</li>
     * <li>Register for recording with multiple users</li>
     * <li>Login and go to My Recordings page</li>
     * <li>Store number of views for the recording</li>
     * <li>Go to RecordingViews page</li>
     * <li>Verify that number of views on My Recordings page matches number of entries in the views table</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description="RecordingViews: Verify that Views # on My Recordings MATCHES EXACTLY to the number of names on the Recordings Views List")
    public void verifyNumberOfViewsMatchesNumberOfRowsInDisplayedTable() {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String email = getEmail();
        String password = getPassword();
        int numRegistrants = 5;

        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();

        for(int i=0; i<numRegistrants; ++i) {
            registerAndViewRecording(recordingsRegistrationBaseURL + recordingId, TEST_FIRST_NAME + i, TEST_LAST_NAME + i, i + TEST_EMAIL);
        }

        MyWebinarsPage myWebinarPage = loginG2W(email, password);
        myRecordingsPage = myWebinarPage.gotoMyRecordingsPage();
        int numberOfViews = myRecordingsPage.getNumberOfViews(recordingId);
        RecordingViewsPage recordingViewsPage = myRecordingsPage.gotoRecordingViewPage(recordingId);

        Assert.assertEquals(numberOfViews, recordingViewsPage.getListOfRecordingViews().size());
        recordingViewsPage.logOut();
        cleanup();
    }

    /**
     * <ol>
     * Test to verify that the Recording's Views List shows all recording registrant names sorted by registration date, most recent at top
     * <li>Create a personal account, upload a recording and logout</li>
     * <li>Register for recording with multiple users</li>
     * <li>Login and go to My Recordings page</li>
     * <li>Go to RecordingViews page for recording</li>
     * <li>Verify that the entries are sorted by registration date, most recent on top</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description="RecordingViews: Verify that the Recording's Views List shows all recording registrant names sorted by registration date, most recent at top")
    public void verifyViewsListSortedByDate() {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String email = getEmail();
        String password = getPassword();

        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();

        int numRegistrants = 5;
        for(int i=0; i<numRegistrants; ++i) {
            registerAndViewRecording(recordingsRegistrationBaseURL + recordingId, TEST_FIRST_NAME + i, TEST_LAST_NAME + i, i + TEST_EMAIL);
        }

        MyWebinarsPage myWebinarPage = loginG2W(email, password);
        myRecordingsPage = myWebinarPage.gotoMyRecordingsPage();
        RecordingViewsPage recordingViewsPage = myRecordingsPage.gotoRecordingViewPage(recordingId);
        List<RecordingViewData> recordingViewDataList = recordingViewsPage.getListOfRecordingViews();
        List<RecordingViewData> actual = new ArrayList<RecordingViewData>(recordingViewDataList);

        // Descending order
        Collections.sort(recordingViewDataList, new Comparator<RecordingViewData>() {
            @Override
            public int compare(RecordingViewData o1, RecordingViewData o2) {
                return o2.getViewTime().compareTo(o1.getViewTime());
            }
        });

        Assert.assertEquals(recordingViewDataList, actual);
        recordingViewsPage.logOut();
        cleanup();
    }

    /**
     * <ol>
     * Test to verify that 0 Views is not a hyperlink. All other #s hyperlink to the Recordings Views List
     * <li>Create a personal account, upload two recordings and logout</li>
     * <li>Register for one recording with multiple users</li>
     * <li>Login and go to My Recordings page</li>
     * <li>Verify that zero views is not a link, while the number of views for the second recording is a link</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description="RecordingViews: Verify that 0 Views is not a hyperlink. All other #s hyperlink to the Recordings Views List")
    public void verifyZeroViewsIsNotLink() {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String email = getEmail();
        String password = getPassword();
        myRecordingsPage.uploadFile(this.getAbsoluteFilePath("Recordings" + File.separator + VALID_RECORDING_FILE2));
        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();

        int numRegistrants = 5;
        for(int i=0; i<numRegistrants; ++i) {
            registerAndViewRecording(recordingsRegistrationBaseURL + recordingId, TEST_FIRST_NAME + i, TEST_LAST_NAME + i, i + TEST_EMAIL);
        }

        MyWebinarsPage myWebinarPage = loginG2W(email, password);
        myWebinarPage.gotoMyRecordingsPage();

        // Make sure the count of 0 isn't a link by asserting the text of li is 0
        Assert.assertNotNull(this.webDriver.findElement(By.xpath("//div[@class='table-data']//ul[@class='table-data-row']/li[text()=0]")));
        Assert.assertNotNull(this.webDriver.findElement(By.xpath("//div[@class='table-data']//ul[@class='table-data-row']/li/a[text()=5]")));
        myWebinarPage.logOut();
        cleanup();
    }

    /**
     * <ol>
     * Test to verify that upload with correct mime type works
     * <li>Create a personal account, upload recording</li>
     * <li>Verify that delete link with recordingId is present</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description="RecordingViews: Verify upload with correct mime type")
    public void verifyUploadWithCorrectMimeType() {
        createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        Assert.assertNotNull(this.webDriver.findElement(By.xpath("//a[@data-webinarname='" + VALID_RECORDING_FILE + "']")));
    }

    /**
     * <ol>
     * Test to verify that upload with wrong mime type throws error
     * <li>Create a personal account, upload recording with wrong mime type</li>
     * <li>Verify error message</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description="RecordingViews: Verify Upload with wrong mime type and extension")
    public void verifyUploadFileWrongFileType() {
        MyWebinarsPage myWebinarPage = createAccountLoginAndGoToMyWebinar(Groups.PERSONAL);

        // Go to My Recordings page.
        MyRecordingsPage myRecordingsPage = myWebinarPage.gotoMyRecordingsPage();
        myRecordingsPage.uploadFileExpectError(this.getAbsoluteFilePath("Recordings" + File.separator + INVALID_RECORDING_FILE));
        Assert.assertEquals(this.messages.getMessage("archive.fileForm.error.file.mimeType", null,
                this.locale), this.webDriver.findElement(By.xpath("//p[@class='inline-error']")).getText());
        myRecordingsPage.logOut();
    }

    /**
     * <ol>
     * Test to verify that excel report is generated properly
     * <li>Create a personal account, upload recording with wrong mime type</li>
     * <li>Verify error message</li>
     * </ol>
     * @throws IOException
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.RECORDINGS, Groups.HTML_UNIT_FAILURE }, description="RecordingViews: Verify that the list can be exported to Excel ")
    public void verifyViewListCanBeExportedToExcel() throws IOException {
        MyRecordingsPage myRecordingsPage = createAccountAndUploadRecording(Groups.PERSONAL, VALID_RECORDING_FILE);
        String email = getEmail();
        String password = getPassword();

        String recordingId = myRecordingsPage.getRecordingIds().get(0);
        myRecordingsPage.logOut();
        for(int i=0; i<2; ++i) {
            registerAndViewRecording(recordingsRegistrationBaseURL + recordingId, TEST_FIRST_NAME + i, TEST_LAST_NAME + i, i + TEST_EMAIL);
        }

        MyWebinarsPage myWebinarPage = loginG2W(email, password);
        myRecordingsPage = myWebinarPage.gotoMyRecordingsPage();
        RecordingViewsPage recordingViewsPage = myRecordingsPage.gotoRecordingViewPage(recordingId);
        String downloadedFile = recordingViewsPage.downloadFile();
        Map<Integer, List> sheets = new ExcelDataReader().readFile(downloadedFile);
        List<Object> rows = sheets.get(0);

        List<String> heading = (List<String>)rows.get(0);
        Assert.assertEquals(heading.get(1), VALID_RECORDING_FILE);

        // Verify first user details matches
        verifyUserDetailsMatches((List<String>)rows.get(8), TEST_FIRST_NAME + "0", TEST_LAST_NAME + "0", "0" + TEST_EMAIL);

        // Verify second user details matches
        verifyUserDetailsMatches((List<String>)rows.get(7), TEST_FIRST_NAME + "1", TEST_LAST_NAME + "1", "1" + TEST_EMAIL);
        recordingViewsPage.logOut();
        cleanup();
    }

    private void verifyUserDetailsMatches(List<String> userDetails, String expectedFirstName, String expectedLastName, String expectedEmail) {
        String firstName = userDetails.get(1);
        String lastName = userDetails.get(2);
        String email = userDetails.get(3);

        Assert.assertEquals(firstName, expectedFirstName);
        Assert.assertEquals(lastName, expectedLastName);
        Assert.assertEquals(email, expectedEmail);
    }

}
