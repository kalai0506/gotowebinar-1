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
package com.citrix.g2w.webdriver.pages.forms;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;

/**
 * @author: ankitag1
 */

public class SurveyForm extends BasePage {

    /**
     * class variable for default answers count.
     */
    private final int DEFAULT_ANSWERS_COUNT = 3;
    
    /**
     * class variable for maximum allowed answers count.
     */
    private final int MAX_ANSWERS_COUNT = 8;
    /**
     * Web element for new answer text.
     */
    private WebElement newAnswerTextWebElement;
    /**
     * Web element for new Question button.
     */
    @FindBy(id = "newQuestionButton")
    private WebElement newQuestion;
    /**
     * Web element for new question text.
     */
    @FindBy(id = "newQuestionText")
    private WebElement newQuestionText;
    /**
     * Web element for save.
     */
    @FindBy(id = "save")
    protected WebElement save;

    /**
     * Web element for questionTypes.
     */
    @FindBy(id = "questionTypes_trig")
    protected WebElement questionTypes;

    /**
     * Web element for survey name.
     */
    @FindBy(id = "survey.name")
    protected WebElement surveyName;

    /**
     * Constructor to initialize WebDriver.
     * 
     * @param webDriver
     *            (web driver)
     */
    public SurveyForm(final WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method to Assign Survey to Webinar.
     */
    public void addSurveyToWebinar() {
        // click on save button
        this.save.click();
        this.logger.logWithScreenShot("Navigate to Survey page", this.driver);
    }

    /**
     * Method to Select the Multiple Choice with One Answer Set the inputs to
     * the fields.
     * 
     * @param question
     *            (question)
     * @param listOfanswers
     *            (list of answers)
     */
    public void addMultipleChoiceQuestionWithOneAnswer(final String question,
            final List<String> listOfanswers) {
        this.logger.log("Add Multiple Choic Question with Single answer: "
                + question);
        // click on questionType
        this.questionTypes.click();
        findVisibleElement(By.xpath("//div[@id='questionTypes__menu']/ul/li[contains(., 'Multiple Choice with One Answer')]"), 30);
        WebElement questionType = driver
                .findElement(By
                        .xpath("//div[@id='questionTypes__menu']/ul/li[contains(., 'Multiple Choice with One Answer')]"));
        questionType.click();
        this.enterQuestionsAndAnswers(question, listOfanswers);
        // click on create question
        this.findClickableElement(By.id("createQuestion")).click();
        this.logger.logWithScreenShot("Added question to Survey :" + question,
                this.driver);
    }

    /**
     * Method toSelect the Multiple Choice with multiple Answers Set the inputs
     * to the fields.
     * 
     * @param question
     *            (question)
     * @param listOfanswers
     *            (list of answers)
     */
    public void addNewMultipleAnswersQuestionsToSurvey(final String question,
            final List<String> listOfanswers) {
        this.logger.log("Add Multiple Choice with multiple answers question: "
                + question);
        // click on questionType
        this.questionTypes.click();
        findVisibleElement(By.xpath("//div[@id='questionTypes__menu']/ul/li[contains(., 'Multiple Choice with Multiple Answer')]"), 30);
        WebElement questionType = driver
                .findElement(By
                        .xpath("//div[@id='questionTypes__menu']/ul/li[contains(., 'Multiple Choice with Multiple Answers')]"));
        questionType.click();
        this.enterQuestionsAndAnswers(question, listOfanswers);
        // click on create question
        this.findClickableElement(By.id("createQuestion")).click();
        this.logger
        .logWithScreenShot("Added question to Survey :", this.driver);
    }

    /**
     * Method to Select the Rating on a scale Set the inputs to the fields.
     * 
     * @param question
     *            (question)
     */
    public void addRatingScaleQuestion(final String question) {
        this.logger.log("Add Rating Scale Question " + question);
        // click on questionType
        this.questionTypes.click();
        findVisibleElement(By.xpath("//div[@id='questionTypes__menu']/ul/li[contains(., 'Rate on a Scale of 1 to 5')]"), 30);
        WebElement questionType = driver
                .findElement(By
                        .xpath("//div[@id='questionTypes__menu']/ul/li[contains(., 'Rate on a Scale of 1 to 5')]"));
        questionType.click();

        if (question != null) {
            this.newQuestionText.sendKeys(question);
        }
        // click on create question
        this.findClickableElement(By.id("createQuestion")).click();
        this.logger
        .logWithScreenShot("Added question to Survey :", this.driver);
    }

    /**
     * Method to Select the short answer Set the inputs to the fields.
     * 
     * @param question
     *            (question)
     */
    public void addShortAnswerQuestion(final String question) {
        this.logger.log("Add Short Answer Question: " + question);
        // click on questionType
        this.questionTypes.click();
        findVisibleElement(By.xpath("//div[@id='questionTypes__menu']/ul/li[contains(., 'Short Answer')]"), 30);
        WebElement questionType = driver
                .findElement(By
                        .xpath("//div[@id='questionTypes__menu']/ul/li[contains(., 'Short Answer')]"));
        questionType.click();
        if (question != null) {
            this.newQuestionText.sendKeys(question);
        }
        // click on create question
        this.findClickableElement(By.id("createQuestion")).click();
        this.logger
        .logWithScreenShot("Added question to Survey :", this.driver);
    }

    /**
     * Method to click on new question button.
     */
    public void clickOnNewQuestionButton() {
        // click on new question button.
        this.newQuestion.click();
        this.logger.logWithScreenShot("Click on new question button",
                this.driver);
    }

    /**
     * Method to click on save.
     * 
     * @return ManageSurveysPage
     */
    public ManageWebinarPage clickOnSave() {
        // click on save button
        this.save.click();
        this.logger.logWithScreenShot("After clicking on save", this.driver);
        return new ManageWebinarPage(this.driver);
    }

    /**
     * Method to Enter questions and answers to Survey.
     * 
     * @param question
     *            (question)
     * @param listOfanswers
     *            (list of answers)
     */
    public void enterQuestionsAndAnswers(final String question,
            final List<String> listOfanswers) {
        this.logger.log("Add question :" + question + " and answers:"
                + listOfanswers + " to Survey");
        if (question != null && listOfanswers != null) {
            this.newQuestionText.clear();
            this.newQuestionText.sendKeys(question);

            int count = 1;
            String textFieldId = "";

            for (String answer : listOfanswers) {
                
                if (count > this.MAX_ANSWERS_COUNT)
                    break; 
                if (count > this.DEFAULT_ANSWERS_COUNT) {
                    this.driver.findElement(By.id("addAnotherAnswer")).click();
                }
                textFieldId = "newAnswerText_" + count;
                this.newAnswerTextWebElement = this.findVisibleElement(By
                        .id(textFieldId));
                this.newAnswerTextWebElement.clear();
                if (answer != null) {
                    this.newAnswerTextWebElement.sendKeys(answer);
                } else {
                    this.newAnswerTextWebElement.sendKeys("");
                }
                count++;
            }
        }
    }

    /**
     * Method to Set title and instruction for create Survey for Webinar.
     * 
     * @param surveyTitle
     *            (survey title)
     * @param surveyInstructions
     *            (survey instructions)
     */
    public void setTitle(final String surveyTitle) {
        this.logger.log("Set title as :" + surveyTitle);
        // Set inputs to elements
        if (surveyTitle != null) {
            this.surveyName.clear();
            this.surveyName.sendKeys(surveyTitle);
        }
    }
}
