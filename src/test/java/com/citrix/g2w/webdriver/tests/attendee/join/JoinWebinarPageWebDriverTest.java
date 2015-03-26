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
package com.citrix.g2w.webdriver.tests.attendee.join;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.ScheduleAWebinarPage;
import com.citrix.g2w.webdriver.pages.join.DownloadPage;
import com.citrix.g2w.webdriver.pages.join.JoinWebinarPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

/**
 * Test class for join webinar related cases.
 * @author: adas
 */
public class JoinWebinarPageWebDriverTest extends BaseWebDriverTest {
	@Value("${g2w.url}/join.tmpl")
	private String joinWebinarPageURL;
	
    /**
     * <ol>
     * Test to verify registration page selects the correct session if specific webinar session is selected for a series.
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar series with four sessions with name</li>
     * <li>Go to the manage webinar page</li>
     * <li>Go to the join webinar page as unregistered attendee and supply specific webinar instance details</li>
     * <li>Verify selected session in the resulting registration page for the attendee</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.JOIN }, description = "JoinAWebinar: (Recurring Series) verify from 'Join Webinar' page, correct session is selected")
    public void verifyJoinWebinarSelectsProperSessionKeyForARecurringWebinar() {
        long duration = 3600000;
        int sessionCount = 4 ;
        DateTime startDate = new DateTime(DateTimeZone.forID("America/Los_Angeles")).plusWeeks(3);
        DateTime endDate = startDate.plusDays(sessionCount - 1);
        DateTime endTime = endDate.plus(duration);
        
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL, ScheduleAWebinarPage.Frequency.DAILY,
                ScheduleAWebinarPage.WebinarType.SERIES, startDate, endDate, endTime);
        
        List<Long> listOfWebinarKeys = manageWebinarPage.getListOfWebinarKeysForRecurringWebinar();

        // Verify the session selection text appearing for recurring webinar in the registration page.        
        Assert.assertEquals(listOfWebinarKeys.size(), sessionCount);
        
        // select a specific webinar instance in the series for the attendee join 
        String selectedSessionKey = listOfWebinarKeys.get(1).toString();
        String webnarID = manageWebinarPage.getSpecificWebinarIDForRecurringWebinar(2);        
        manageWebinarPage.logOut();
        
        JoinWebinarPage joinWebinarPage = new JoinWebinarPage(joinWebinarPageURL, this.getWebDriver());
        RegistrantDetails registrantDetails = new RegistrantDetails();
        String registrantEmail = registrantDetails.getEmailAddress();
        RegistrationPage registrationPage = joinWebinarPage.joinWebinarForUnregisteredAttendee(registrantEmail , webnarID);

        // Verify the session selection text appearing for recurring webinar in the registration page.
        String message = messages.getMessage("registration.recurring.sessions", null,this.locale);
        String recurringWebinarMessage = registrationPage.getRecurringWebinarSelectionText();
        Assert.assertEquals(recurringWebinarMessage, message);

        // Verify the available session timings in the registration page with the actual recurring webinar timings.
        List<String> expectedWebinarTimes = convertDateAndTimeToList(startDate, endTime, "daily");
        List<String> actualWebinarTimes = registrationPage.getDateAndTimeAsListforDifferentAttendees();
        Assert.assertEquals(expectedWebinarTimes, actualWebinarTimes);

        // Verify the selected session key in the registration page with the actual selected key.
        String sessionKey = registrationPage.getSelectedRecurringWebinarKey();
        Assert.assertEquals(sessionKey,selectedSessionKey);
    }

    /**
     * <ol>
     * Test to verify unregistered user can register after submitting Join page and is taken to join flow
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Start the webinar session as organizer</li>
     * <li>Go to the join webinar page as unregistered attendee and supply webinar instance details</li>
     * <li>Attendee will be routed to join flow with launcher page downloading webinar</li>
     * <li>Verify download page is loaded for the attendee after joining webinar in progress</li>
     * </ol>
     */    
    @Test(groups = { Groups.PERSONAL, Groups.JOIN }, description="JoinAWebinar: Verify unregistered user can register after submitting Join page and is taken to join flow")
    public void verifyDownloadFlowIsInitiatedAfterSubmittingJoinPage() {
    	
    	ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL );
    	
    	Long webinarID = manageWebinarPage.getWebinarId();
        String webinarName = manageWebinarPage.getWebinarName();
        runWebinarSession(manageWebinarPage,personalAccount.getEmail(),personalAccount.getPassword());
        
        JoinWebinarPage joinWebinarPage = new JoinWebinarPage(joinWebinarPageURL, this.getWebDriver());
        RegistrantDetails registrantDetails = new RegistrantDetails();
        String registrantEmail = registrantDetails.getEmailAddress();
        RegistrationPage registrationPage = joinWebinarPage.joinWebinarForUnregisteredAttendee(registrantEmail , webinarID.toString());

        // Verify the pre-filled attendee email and webinar name with actual attendee email and webinar name
        Assert.assertEquals(registrantEmail, registrationPage.getAttendeeEmailId());
        Assert.assertEquals(webinarName, registrationPage.getRegistrationPageWebinarName());
        
        DownloadPage downloadPage = registrationPage.joinAttendeeToWebinarInProgress(registrantDetails);
        // Get title from download page.
        String downloadPageTitle = downloadPage.getTitleFromDownloadPage();
        // Verify title on Launch goto webinar page.
        Assert.assertEquals(downloadPageTitle, this.messages.getMessage("downloadPage.title", null, this.locale));
        
        // Close the webinar session and close the launcher page.
        downloadPage.close();
        this.session.endWebinar(webinarID);
    } 
    
    /**
     * Method to run webinar session.
     * 
     * @param manageWebinarPage (manage webinar page object)
     * @param organizerEmail (organizer Email)
     * @param organizerPassword (organizer password)
     */
    private void runWebinarSession(final ManageWebinarPage manageWebinarPage, 
    			final String organizerEmail, final String organizerPassword) {
        Long webinarId = manageWebinarPage.getWebinarId();
        // Get the user authentication token
        String authenticationToken = this.authService.getAuthToken(organizerEmail, organizerPassword);
        // Run the session
        this.session.startWebinar(authenticationToken, this.personalAccount.getUserKey(),webinarId);
    }    
}
