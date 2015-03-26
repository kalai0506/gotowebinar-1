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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.collections.Lists;

import com.citrix.g2w.webdriver.pages.BasePage;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author: Ankit Ag
 */
public class AnswerSurveyQuestionPage extends BasePage {

    public enum questionType {
        MULTIPLE_CHOICE, MULTIPLE_ANSWER, SHORT_ANSWER
    };

    /**
     * Web element used to submit the Survey test.
     */
    @FindBy(id = "surveySubmission.submit.button")
    private WebElement submitSurveyAnswer;

    /**
     * Constructor to initialize instance variables and verify if we are on the
     * Survey test Page.
     * 
     * @param SurveyTestUrl
     *            (Survey test URL)
     * @param webDriver
     *            (web driver)
     */
    public AnswerSurveyQuestionPage(final String surveyTestUrl,
            final WebDriver webDriver) {
        this.driver = webDriver;
        this.driver.get(surveyTestUrl);
        Assert.assertTrue(webDriver.getCurrentUrl().contains("/survey/"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method to enter short answer for Survey question .
     * 
     * @param SurveyQuestion
     *            (Survey question)
     * @param SurveyShortAnswer
     *            (Survey short answer)
     */
    public void enterShortAnswerForSurveyQuestion(
            final String surveyShortAnswer, final int questionNo) {
        this.clearAndType(
                this.findVisibleElement(By.id("surveySubmission.answers_"
                        + questionNo + ".text")), surveyShortAnswer);
        this.logger.logWithScreenShot("After Enter Short Answer: "
                + surveyShortAnswer + ", for the Survey question no: "
                + questionNo, this.driver);
    }

    /**
     * Method used to submit Survey answer.
     * 
     * @return SurveyAnswerSubmittedPage
     */
    public SurveyAnswerSubmittedPage submitSurveyAnswer() {
        this.submitSurveyAnswer.click();
        this.logger.logWithScreenShot("After Survey Answer Submitted",
                this.driver);
        return new SurveyAnswerSubmittedPage(this.driver);
    }

    /**
     * Method used to enter Survey answer for multiple choice question.
     * 
     * @return SurveyAnswerSubmittedPage
     */
    public void enterOptionForSurveyQuestion(int option, int questionNo) {
        this.findVisibleElement(
                By.id("surveySubmission.answers_" + questionNo
                        + ".selectedOptions_" + option)).click();
        this.logger.logWithScreenShot("After Enter Option: " + option
                + ", for the Survey question no: " + questionNo, this.driver);
    }

    /**
     * Method used to enter Survey answer for multiple answer questions.
     * 
     * @return SurveyAnswerSubmittedPage
     */
    public void enterMultipleOptionsForSurveyQuestion(List<String> options,
            int questionNo) {
        for (String option : options) {
            enterOptionForSurveyQuestion(Integer.valueOf(option), questionNo);
        }
    }

    @SuppressWarnings("unchecked")
    public SurveyAnswerSubmittedPage takeSurvey(Object[][] surveyAnswers) {

        for (Object[] row : surveyAnswers) {
            // skip the row, if answer not provided
            if (row.length < 3) {
                continue;
            }
            int questionNo = Integer.valueOf(String.valueOf(row[0]));
            String answer = String.valueOf(row[2]);
            final String strOptions = String.valueOf(row[1]);
            switch (questionType.valueOf(strOptions)) {
            case MULTIPLE_CHOICE:
                int option = Double.valueOf(answer).intValue();
                enterOptionForSurveyQuestion(option, questionNo);
                break;
            case MULTIPLE_ANSWER:
                String[] optionValues = answer.split(",");
                List<String> options;
                if (optionValues.length > 1) {
                    options = Arrays.asList(optionValues);
                } else {
                    int optionNo = Double.valueOf(optionValues[0]).intValue();
                    options = Lists.newArrayList(String.valueOf(optionNo));
                }
                enterMultipleOptionsForSurveyQuestion(options, questionNo);
                break;
            case SHORT_ANSWER:
                enterShortAnswerForSurveyQuestion(answer, questionNo);
                break;
            }
        }
        return submitSurveyAnswer();
    }
}
