package com.citrix.g2w.webdriver.tests.api;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.api.SessionsRestApi;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.survey.AnswerSurveyQuestionPage;
import com.citrix.g2w.webdriver.pages.survey.CreateSurveyPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;
import com.citrix.g2w.webdriver.util.ExcelDataReader;

/**
 * Tests for session api
 * @author ankitag1
 *
 */
public class SessionApiTests extends BaseWebDriverTest {

    private Object[][] surveyQuestions;
    private Object[][] surveyAnswers;
    
    private ExcelDataReader excelDataReader = new ExcelDataReader();

    @Autowired
    private SessionsRestApi sessionsApi;
    
    public SessionApiTests() throws IOException {
        surveyQuestions = excelDataReader.getSeleniumDataArray(
                "src/test/resources/tests/data/reports/SurveyQuestions.xls", "");
        surveyAnswers = excelDataReader.getSeleniumDataArray(
                "src/test/resources/tests/data/reports/SurveyAnswers.xls", "");
    }

    /**
     * <ol>
     * 
     * API - SessionsAPITests: getSessionsForSetOfOrganizers works fine when attendees submit multiple surveys
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to Survey Page & Create Survey for Webinar</li>
     * <li>Register 10 Attendees for Webinar</li>
     * <li>Start, add one more attendee and then ends the Webinar</li>
     * <li>take survey twice</li>
     * <li>Verify Getsessions Api</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.API}, 
            description = "API - SessionsAPITests: getSessionsForSetOfOrganizers works fine when attendees submit multiple surveys")
    public void verifyGetSessionForOrganizersWithMultipleSurveys() {
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String regUrl = manageWebinarPage.getRegistrationURL();
        Long webinarID = manageWebinarPage.getWebinarId();
        Long webinarKey = manageWebinarPage.getWebinarKey();
        String webinarName = manageWebinarPage.getWebinarName();
        DateTime dateTime = manageWebinarPage.getDateTime();
        CreateSurveyPage createSurveyPage = manageWebinarPage.gotoSurveyPage();
        createSurveyPage.addSurveyToWebinar(surveyQuestions);
        startAddAttendeeAndEndWebinar(regUrl, webinarID, dateTime, 1);
        String exitPopUpUrl = this.session.getSurveyExitPopUpUrl();
        
        //take survey twice
        AnswerSurveyQuestionPage answerSurveyQuestion1 = new AnswerSurveyQuestionPage(
                exitPopUpUrl, this.getWebDriver());
        answerSurveyQuestion1.takeSurvey(surveyAnswers);
        AnswerSurveyQuestionPage answerSurveyQuestion2 = new AnswerSurveyQuestionPage(
                exitPopUpUrl, this.getWebDriver());
        answerSurveyQuestion2.takeSurvey(surveyAnswers);
        
        String token = this.authService.getAuthToken(this.getEmail(), this.getPassword());
        Long userKey = this.personalAccount.getUserKey();
        
        ResponseEntity<String> sessions = sessionsApi.getSessionsForSetOfOrganizers(token, Lists.newArrayList(userKey), null, null, "attendees,attendeeinterestratings");
        Assert.assertNotNull(sessions);
        Assert.assertEquals(sessions.getStatusCode().value(), HttpStatus.SC_OK);
        String body = sessions.getBody();
        logger.log("Reponse Body: " + body);
        Assert.assertTrue(body.contains("\"webinarID\":\"" + webinarID + "\""));
        Assert.assertTrue(body.contains("\"webinarKey\":\"" + webinarKey + "\""));
        Assert.assertTrue(body.contains("\"webinarName\":\"" + webinarName + "\""));
        
    }

}
