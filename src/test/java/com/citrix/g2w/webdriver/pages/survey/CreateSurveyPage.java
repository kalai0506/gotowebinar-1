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
package com.citrix.g2w.webdriver.pages.survey;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.forms.SurveyForm;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author: ankit ag
 */
public class CreateSurveyPage extends SurveyForm {

    public enum questionType {
        MULTIPLE_CHOICE, MULTIPLE_ANSWER, SHORT_ANSWER
    };

    /**
     * Constructor to initialize instance variables and verify current page url.
     * 
     * @param webDriver
     *            (web driver)
     */
    public CreateSurveyPage(final WebDriver webDriver, final String editSurveyUrl) {
        super(webDriver);
        this.driver = webDriver;
        this.driver.get(editSurveyUrl);
        Assert.assertTrue(this.driver.getCurrentUrl().contains("survey/create.tmpl"));
        PageFactory.initElements(this.driver, this);
    }
    
    /**
     * Constructor to initialize instance variables and verify current page url.
     * 
     * @param webDriver
     *            (web driver)
     */
    public CreateSurveyPage(final WebDriver webDriver) {
        super(webDriver);
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("survey/create.tmpl"));
        PageFactory.initElements(this.driver, this);
    }
    
    
    @SuppressWarnings("unchecked")
    public void addSurveyToWebinar(Object[][]surveyQuestions) {
        //CreateSurveyPage createSurveyPage = manageWebinarPage.gotoSurveyPage();
        setTitle("Survey Test Title");

        int questionNo = 1;
        int totalQuestions = surveyQuestions.length;
        for(Object[] row : surveyQuestions) {
            //row[0] = question
            String question = String.valueOf(row[0]);
            //row[1] == questionType

            final String strOptions = String.valueOf(row[1]);
            switch (questionType.valueOf(strOptions)) {
                case MULTIPLE_CHOICE :
                    //row[2] == answers
                    List<String> multipleChoice = Arrays.asList(String.valueOf(row[2]).split(","));
                    addMultipleChoiceQuestionWithOneAnswer(question, multipleChoice);
                    break;
                case MULTIPLE_ANSWER :            
                    List<String> multipleAnswers = Arrays.asList(String.valueOf(row[2]).split(","));
                    addNewMultipleAnswersQuestionsToSurvey(question, multipleAnswers);
                    break;
                case SHORT_ANSWER : 
                    addShortAnswerQuestion(question);
                    break;
            }

            //if not on last question, add another question
            if(questionNo < totalQuestions) {
                clickOnNewQuestionButton();
            }
            questionNo++;
        }
        addSurveyToWebinar();
    }

}
