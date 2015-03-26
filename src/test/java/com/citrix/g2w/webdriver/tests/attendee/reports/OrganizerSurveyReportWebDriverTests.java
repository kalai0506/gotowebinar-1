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
package com.citrix.g2w.webdriver.tests.attendee.reports;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.pages.reports.GenerateReportsPage;
import com.citrix.g2w.webdriver.pages.survey.AnswerSurveyQuestionPage;
import com.citrix.g2w.webdriver.pages.survey.CreateSurveyPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;
import com.citrix.g2w.webdriver.util.ExcelDataReader;

/**
 * @author: Ankit Ag
 */
public class OrganizerSurveyReportWebDriverTests extends BaseWebDriverTest {

    @Value("${g2w.url}/reporting/generateReports.tmpl")
    private String generateReportrPageURL;

    private Object[][] surveyQuestions;
    private Object[][] surveyAnswers;
    
    private ExcelDataReader excelDataReader = new ExcelDataReader();

    public OrganizerSurveyReportWebDriverTests() throws IOException {
        surveyQuestions = excelDataReader.getSeleniumDataArray(
                "src/test/resources/tests/data/reports/SurveyQuestions.xls", "");
        surveyAnswers = excelDataReader.getSeleniumDataArray(
                "src/test/resources/tests/data/reports/SurveyAnswers.xls", "");
    }

    /**
     * <ol>
     * 
     * Test to verify No. of Respondants for Survey
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to Survey Page & Create Survey for Webinar</li>
     * <li>Register 10 Attendees for Webinar</li>
     * <li>Start, add one more attendee and then ends the Webinar</li>
     * <li>take survey</li>
     * <li>GoTo Generate Report page and then generate Survey Report</li>
     * <li>Verify no. of Respondant in Survey Report</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.REPORTS, Groups.SURVEY_REPORT }, description = "OrganizerSurveyReport: Verify Number of Respondants on page is correct")
    public void verifyNoOfRespondantsForSurvey() {
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String regUrl = manageWebinarPage.getRegistrationURL();
        Long webinarID = manageWebinarPage.getWebinarId();
        DateTime dateTime = manageWebinarPage.getDateTime();
        CreateSurveyPage createSurveyPage = manageWebinarPage.gotoSurveyPage();
        createSurveyPage.addSurveyToWebinar(surveyQuestions);
        addRegistrantsToWebinar(regUrl, 11);
        startAddAttendeeAndEndWebinar(regUrl, webinarID, dateTime, 1);
        String exitPopUpUrl = this.session.getSurveyExitPopUpUrl();
        AnswerSurveyQuestionPage answerSurveyQuestionPage = new AnswerSurveyQuestionPage(
                exitPopUpUrl, this.getWebDriver());
        answerSurveyQuestionPage.takeSurvey(surveyAnswers);
        GenerateReportsPage generateReportsPage = new GenerateReportsPage(
                generateReportrPageURL, this.webDriver);
        generateReportsPage.generateReport("Survey", "Today");
        String noOfRespondants = this.webDriver.findElement(
                By.xpath("//ul[@class='table-data-row']/li[last()]")).getText();
        Assert.assertEquals("1", noOfRespondants);
    }

    /**
     * <ol>
     * 
     * Test to verify Survey Report when No Attendee Joining the Session.
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to Survey Page & Create Survey for Webinar</li>
     * <li>Register 10 Attendees for Webinar</li>
     * <li>Start and ends the Webinar</li>
     * <li>take survey</li>
     * <li>GoTo Generate Report page and then generate Survey Report</li>
     * <li>Verify No Webinar message shown in Survey Report</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP,Groups.PERSONAL, Groups.REPORTS, Groups.SURVEY_REPORT }, description = "Reports - OrganizerSurveyReport: With no attendee joining session")
    public void verifySurveyReportWhenNoAttendeeJoiningtheSession() {
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        String noWebinars = this.messages.getMessage(
                "generateReport.noWebinars", null, this.locale);
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String regUrl = manageWebinarPage.getRegistrationURL();
        Long webinarID = manageWebinarPage.getWebinarId();
        DateTime dateTime = manageWebinarPage.getDateTime();
        CreateSurveyPage createSurveyPage = manageWebinarPage.gotoSurveyPage();
        createSurveyPage.addSurveyToWebinar(surveyQuestions);
        addRegistrantsToWebinar(regUrl, 11);
        startAddAttendeeAndEndWebinar(regUrl, webinarID, dateTime, 0);
        GenerateReportsPage generateReportsPage = new GenerateReportsPage(
                generateReportrPageURL, this.webDriver);
        generateReportsPage.generateReport("Survey", "Today");
        generateReportsPage.findPresenceOfElement(
                By.xpath("//*[contains(text(),'" + noWebinars + "')]"), 10);
    }
    
    /**
     * 
     * 
     * <ol>
     * 
     * Test to verify Survey Report after the Session.
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to Survey Page & Create Survey for Webinar</li>
     * <li>Register 10 Attendees for Webinar</li>
     * <li>Start, add one more attendee and then ends the Webinar</li>
     * <li>take survey</li>
     * <li>GoTo Generate Report page and then generate Survey Report</li>
     * <li>Verify Survey Report</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP,Groups.PERSONAL, Groups.REPORTS, Groups.SURVEY_REPORT }, description = "OrganizerSurveyReport: post session survey")
    public void verifySurveyReportPostSession() throws IOException {
     // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String regUrl = manageWebinarPage.getRegistrationURL();
        Long webinarID = manageWebinarPage.getWebinarId();
        String webinarName = manageWebinarPage.getWebinarName();
        DateTime dateTime = manageWebinarPage.getDateTime();
        CreateSurveyPage createSurveyPage = manageWebinarPage.gotoSurveyPage();
        createSurveyPage.addSurveyToWebinar(surveyQuestions);
        addRegistrantsToWebinar(regUrl, 11);
        startAddAttendeeAndEndWebinar(regUrl, webinarID, dateTime, 1);
        String exitPopUpUrl = this.session.getSurveyExitPopUpUrl();
        AnswerSurveyQuestionPage answerSurveyQuestionPage = new AnswerSurveyQuestionPage(
                exitPopUpUrl, this.getWebDriver());
        answerSurveyQuestionPage.takeSurvey(surveyAnswers);
        
        //verify xls report
        GenerateReportsPage generateReportsPage = new GenerateReportsPage(
                generateReportrPageURL, this.webDriver);
        generateReportsPage.generateReport("Survey", "Today");          
        Object[][] xlsReportContent = generateReportsPage
                 .selectWebinarAndGenerateReport(webinarName, "XLS");
        validateSurveyReport(xlsReportContent, 5);
        
        //verify csv report
        generateReportsPage = new GenerateReportsPage(
                 generateReportrPageURL, this.webDriver);
        generateReportsPage.generateReport("Survey", "Today");   
        Object[][] csvReportContent = generateReportsPage
                 .selectWebinarAndGenerateReport(webinarName, "CSV");
        validateSurveyReport(csvReportContent, 8);

    }

    /**
     * <ol>
     * 
     * Test to verify Survey Report after the Session, editing survey
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Go to Survey Page & Create Survey for Webinar</li>
     * <li>Register 10 Attendees for Webinar</li>
     * <li>Start, add one more attendee and then ends the Webinar</li>
     * <li>take survey</li>
     * <li>GoTo Generate Report page and then generate Survey Report</li>
     * <li>Verify Survey Report</li>
     * </ol>
     */
    @Test(groups = { Groups.ATTENDEE_APP,Groups.PERSONAL, Groups.REPORTS, Groups.SURVEY_REPORT }, description = "OrganizerSurveyReport: post session survey - verify answers show up after editing survey")
    public void verifySurveyReportPostSessionEditSurvey() throws IOException {
     // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String regUrl = manageWebinarPage.getRegistrationURL();
        Long webinarID = manageWebinarPage.getWebinarId();
        String webinarName = manageWebinarPage.getWebinarName();
        DateTime dateTime = manageWebinarPage.getDateTime();
        String editSurveyUrl = manageWebinarPage.getEditSurveyUrl();
        CreateSurveyPage createSurveyPage = manageWebinarPage.gotoSurveyPage();
        createSurveyPage.addSurveyToWebinar(surveyQuestions);
        addRegistrantsToWebinar(regUrl, 11);
        startAddAttendeeAndEndWebinar(regUrl, webinarID, dateTime, 1);
        String exitPopUpUrl = this.session.getSurveyExitPopUpUrl();
        
        //edit survey
        CreateSurveyPage editSurveyPage = new CreateSurveyPage(webDriver, editSurveyUrl);
        editSurveyPage.clickOnNewQuestionButton();
        editSurveyPage.addShortAnswerQuestion("Edit Survey Question");
        editSurveyPage.addSurveyToWebinar();
                
        AnswerSurveyQuestionPage answerSurveyQuestionPage = new AnswerSurveyQuestionPage(
                exitPopUpUrl, this.getWebDriver());
        answerSurveyQuestionPage.takeSurvey(surveyAnswers);
        
        
        //verify xls report
        GenerateReportsPage generateReportsPage = new GenerateReportsPage(
                generateReportrPageURL, this.webDriver);
        generateReportsPage.generateReport("Survey", "Today");          
        Object[][] xlsReportContent = generateReportsPage
                 .selectWebinarAndGenerateReport(webinarName, "XLS");
        validateSurveyReport(xlsReportContent, 5);
        
        //verify csv report
        generateReportsPage = new GenerateReportsPage(
                 generateReportrPageURL, this.webDriver);
        generateReportsPage.generateReport("Survey", "Today");   
        Object[][] csvReportContent = generateReportsPage
                 .selectWebinarAndGenerateReport(webinarName, "CSV");
        validateSurveyReport(csvReportContent, 8);

    }
    
    private void validateSurveyReport(Object[][] actualReport, int questionRowNum) {
        validateQuestions(actualReport[questionRowNum]);
        for(int i = questionRowNum + 1; i < actualReport.length; i++) {
            validateSurveyAnswers(actualReport[i]);
         }

    }
    
    private void validateQuestions(Object[] row) {
        Assert.assertEquals(row[0], this.messages.getMessage(
                "surveyReport.surname.column", null, this.locale));
        Assert.assertEquals(row[1], this.messages.getMessage(
                "surveyReport.givenName.column", null, this.locale));
        Assert.assertEquals(row[2], this.messages.getMessage(
                "surveyReport.email.column", null, this.locale));
        Assert.assertEquals(row[3], this.messages.getMessage(
                "surveyReport.submitted.column", null, this.locale));
        
        for(int i = 4; i < surveyQuestions.length + 4; i++) {
            Assert.assertEquals(row[i], surveyQuestions[i-4][0]);
        }
    }
    
    private void validateSurveyAnswers(Object[] row) {
        if(row == null) {
            return;
        }
        for(int i = 4; i < surveyAnswers.length + 4; i++) {
            int j = i-4;
            int questionNo = Integer.valueOf(String.valueOf(surveyAnswers[j][0]));
            String questionType = String.valueOf(surveyAnswers[j][1]);
            if("MULTIPLE_CHOICE".equals(questionType)) {
                String options = String.valueOf(surveyQuestions[questionNo][2]);
                int answer =  Double.valueOf(String.valueOf(surveyAnswers[j][2])).intValue();
                Assert.assertEquals(row[i], options.split(",")[answer]);
            } else if("MULTIPLE_ANSWER".equals(questionType)) {
                String options = String.valueOf(surveyQuestions[questionNo][2]);
                String[] selectedAnswers = String.valueOf(surveyAnswers[j][2]).split(",");
                List<String> selectedOptions = Lists.newArrayList(String.valueOf(row[i]).split(","));
                for(String selectedAnswer : selectedAnswers) {
                    int answer =  Double.valueOf(selectedAnswer).intValue();
                    Assert.assertTrue(selectedOptions.contains(options.split(",")[answer]));
                }
            } else if("SHORT_ANSWER".equals(questionType)) {
                if(surveyAnswers[j].length < 3) {
                    Assert.assertTrue(row.length <= i || row[i] == null);
                } else {
                    Assert.assertEquals(row[i], surveyAnswers[j][2]);
                }
            }
        }
    }

    private void addRegistrantsToWebinar(String regUrl, int count) {
        for (int i = 0; i < count; i++) {
            RegistrantDetails registrantDetails = new RegistrantDetails();
            registrantDetails.setFirstName("fName" + i);
            registrantDetails.setLastName("lName" + i);
            registrantDetails.setEmailAddress("email" + i + "@nodeliver.com");
            RegistrationPage registrationPage = new RegistrationPage(regUrl,
                    webDriver);
            registrationPage.registerAttendeeDetails(registrantDetails);
        }
    }
}
