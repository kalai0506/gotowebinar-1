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
package com.citrix.g2w.webdriver.pages.managewebinar;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.dependencies.WebinarRegistrationFields;
import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;

/**
 * Page object for Managing registration settings.
 * Sample url for that page : https://globaled1.gotowebinar.com/registration/edit.tmpl?webinar=
 *
 */
public class ManageRegistrationSettingsPage extends BasePage {

    
    /**
     * Web element for Toggle fields required.
     */
    @FindBy(id = "toggleRequired")
    private WebElement toggleRequired;

    /**
     * Web element for Toggle fields optional.
     */
    @FindBy(id = "toggleFields")
    private WebElement toggleFields;

   /**
     * By element for add another answer link.
     */
    private final By addAnotherAnswerLink = By.id("addAnotherAnswer");
    
    /**
     * Web element for address allowed.
     */
    @FindBy(id = "addressAllowed")
    private WebElement addressAllowed;

    /**
     * Web element for address required.
     */
    @FindBy(id = "addressRequired")
    private WebElement addressRequired;
    /**
     * By element for auto approval radio button.
     */
    @FindBy(id = "registrationSettings.approvalRequired_false")
    private WebElement  autoApprovalRadioButton ;
        
    /**
     * Web element for Direct Registrant to your Confirmation Page.
     */
    @FindBy(id = "registrationSettings.showConfirmationPage_false")
    private WebElement  selectOwnConfirmationPage ;
    
    /**
     * Web element for Direct Registrant to your Confirmation Page.
     */
    @FindBy(id = "registrationSettings.confirmationLink")
    private WebElement  confirmationLink ;
    
    /**
     * Web element for city allowed.
     */
    @FindBy(id = "cityAllowed")
    private WebElement cityAllowed;
    /**
     * Web element for city required.
     */
    @FindBy(id = "cityRequired")
    private WebElement cityRequired;
    /**
     * Web element used to send code value.
     */
    @FindBy(id = "offers_0.code")
    private WebElement code;
    /**
     * Web element for comments allowed.
     */
    @FindBy(id = "commentsAllowed")
    private WebElement commentsAllowed;
    /**
     * Web element for comments required.
     */
    @FindBy(id = "commentsRequired")
    private WebElement commentsRequired;
    /**
     * Web element for country allowed.
     */
    @FindBy(id = "countryAllowed")
    private WebElement countryAllowed;
    /**
     * Web element for country required.
     */
    @FindBy(id = "countryRequired")
    private WebElement countryRequired;
    /**
     * By element for create question button.
     */
    private final By createQuestionBtn = By.id("createQuestion");
    /**
     * Web element for job title allowed.
     */
    @FindBy(id = "jobTitleAllowed")
    private WebElement jobTitleAllowed;
    /**
     * Web element for job title required.
     */
    @FindBy(id = "jobTitleRequired")
    private WebElement jobTitleRequired;
    /**
     * Web element for manage webinar link.
     */
    @FindBy(xpath = "//a[contains(@href,'manageWebinar.tmpl')]")
    private WebElement manageWebinarLinkText;
    /**
     * By element for manual approval radio button.
     */
    @FindBy(id = "registrationSettings.approvalRequired_true")
    private WebElement manualApprovalRadioButton;
    /**
     * Web element for getting message when attendee limit set.
     */
    @FindBy(xpath = "//div[@class='banner-content']/p")
    private WebElement messageWhenAttendeeLimitSet;
    /**
     * By element for multiple answer option.
     */
    private final By multipleAnswerOption = By.xpath("//div[@id='questionTypes__menu']/ul//li[2]");
    /**
     * Web element used to send new price.
     */
    @FindBy(id = "offers_0.price.dollars")
    private WebElement newPrice;
    /**
     * Web element for new question button.
     */
    @FindBy(id = "newQuestionButton")
    private WebElement newQuestionBtn;
    /**
     * By element for new question text box.
     */
    private final By newQuestionTxtBox = By.id("newQuestionText");
    /**
     * By element for notify trainer
     */
    private final By notifyTrainer = By.id("notifyTrainer");
    /**
     * Web element for organization allowed.
     */
    @FindBy(id = "organizationAllowed")
    private WebElement organizationAllowed;
    /**
     * Web element for organization required.
     */
    @FindBy(id = "organizationRequired")
    private WebElement organizationRequired;
    /**
     * Web element for payments link.
     */
    @FindBy(id = "ui-id-3")
    private WebElement paymentsLink;
    /**
     * Web element for saving payments changes.
     */
    @FindBy(id = "payments.save")
    private WebElement paymentsSave;
    /**
     * Web element for phone allowed.
     */
    @FindBy(id = "phoneAllowed")
    private WebElement phoneAllowed;
    /**
     * Web element for phone required.
     */
    @FindBy(id = "phoneRequired")
    private WebElement phoneRequired;
    /**
     * Web element to set price.
     */
    @FindBy(id = "price.dollars")
    private WebElement price;
    /**
     * Web element to select currency.
     */
    @FindBy(xpath = "//a[@id='price_currency_trig']")
    private WebElement priceCurrency;
    /**
     * Web element for questions link.
     */
    @FindBy(id = "ui-id-2")
    private WebElement questionsLink;
    /**
     * By element for question type button.
     */
    private final By questionTypeBtn = By.xpath("//a[@id='questionTypes_trig']/span[1]");
    /**
     * Web element for registration limit.
     */
    @FindBy(id = "registrationSettings.registrationLimit")
    private WebElement registrationLimit;
    /**
     * Web element for saving registration changes.
     */
    @FindBy(id = "registration.settings.button.save")
    private WebElement registrationSave;
    /**
     * Web element to select price required.
     */
    @FindBy(id = "requirePayment_true")
    private WebElement requirePaymentTrue;
    /**
     * Web element for saving questions changes.
     */
    @FindBy(id = "registration.settings.custom.questions.button.save")
    private WebElement saveButton;
    /**
     * By element for short answer option.
     */
    private final By shortAnswerOption = By.xpath("//div[@id='questionTypes__menu']/ul//li[1]");
    /**
     * Web element for state allowed.
     */
    @FindBy(id = "stateAllowed")
    private WebElement stateAllowed;
    /**
     * Web element for state required.
     */
    @FindBy(id = "stateRequired")
    private WebElement stateRequired;
    /**
     * Web element for zip code allowed.
     */
    @FindBy(id = "zipAllowed")
    private WebElement zipAllowed;
    /**
     * Web element for zip code required.
     */
    @FindBy(id = "zipRequired")
    private WebElement zipRequired;

    /**
     * Constructor to initialize web driver object and verify current page URL.
     * 
     * @param webDriver
     *            (object to initialize)
     */
    public ManageRegistrationSettingsPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("edit.tmpl"));
        PageFactory.initElements(this.driver, this);
    }


    /**
     * Method to create new question.
     */
    public void createNewQuestion() {
        this.newQuestionBtn.click();
    }

    /**
     * Method used to create short answer question.
     * 
     * @param shortAnswerQuestion
     *            (registrant short answer question)
     * @param isCreate
     *            (create question or not)
     */
    public void createShortAnswerQuestion(final String shortAnswerQuestion, final boolean isCreate) {
        if (shortAnswerQuestion != null) {
            this.createNewQuestion();
            this.findVisibleElement(this.questionTypeBtn).click();
            this.findVisibleElement(this.shortAnswerOption).click();
            this.driver.findElement(this.newQuestionTxtBox).sendKeys(shortAnswerQuestion);
            if (isCreate) {
                this.findClickableElement(this.createQuestionBtn).click();
                this.logger.logWithScreenShot(
                        "After add Short Answer question to Custom Question List: "
                                + shortAnswerQuestion, this.driver);
            } else {
                this.logger.logWithScreenShot(
                        "After Setting Short Answer Question to New Question Text Box: "
                                + shortAnswerQuestion, this.driver);
            }
        }
    }

    /**
     * Method to set radio button for automatically approving the registrants.
     */
    public void enableAutomaticApprovalOfRegistrants() {
        this.logger.log("Enable auto approval of registrants");
        this.autoApprovalRadioButton.click();
        this.registrationSave.click();
        this.logger.logWithScreenShot("After clicking on enable auto approval radio button",
                this.driver);
    }

    /**
     * Method to set radio button for manually approving the registrants.
     */
    public void enableManualApprovalOfRegistrants() {
        this.logger.log("Enable manual approval of registrants");
        this.manualApprovalRadioButton.click();
        this.registrationSave.click();
        this.logger.logWithScreenShot("After clicking on enable manual approval radio button",
                this.driver);
    }

    /**
     * Method to get list of custom question.
     * 
     * @return listOfCustomQuestion (list of custom question)
     */
    public List<String> getListOfCustomQuestion() {
        List<String> listOfCustomQuestion = this
                .getListOfTitlesForElementsWithGivenXPath("//a[@href='#editQuestion']");
        this.logger.log("List of Custom Question : " + listOfCustomQuestion);
        return listOfCustomQuestion;
    }

    /**
     * Method to get message when attendee limit is set.
     * @return message
     */
    public String getMessageWhenAttendeeLimitSet() {
        String message = this.messageWhenAttendeeLimitSet.getText();
        this.logger.log("Message when attendee limit set :" + message);
        return message;
    }

    /**
     * Method to get registration limit for the webinar.
     * 
     * @return attendeeLimit
     */
    public String getRegistrationLimit() {
        String attendeeLimit = this.registrationLimit.getAttribute("value");
        this.logger.log("Registration Limit : " + attendeeLimit);
        return attendeeLimit;
    }

    /**
     * Method to go to manage webinar page.
     * 
     * @return (manage webinar page object)
     */
    public ManageWebinarPage gotoManageWebinarPage() {
        this.manageWebinarLinkText.click();
        this.logger.logWithScreenShot("Navigating to manage webinar page", this.driver);
        return new ManageWebinarPage(this.driver);
    }

  
    /**
     * Method to go to Questions section.
     */
    public void gotoQuestions() {
        this.questionsLink.click();
        this.logger.logWithScreenShot("Navigating To Questions Section", this.driver);
    }
   
  
    /**
     * Method to save registration, custom short and multiple choice fields.
     */
    public void saveRegistrantFields() {
        this.saveButton.click();
        this.logger.logWithScreenShot("Registration Fields after Selected ", this.driver);
    }

    /**
     * Method to save the changes made for registration.
     */
    public void saveRegistration() {
        this.logger.log("Click on Save to save registartion details");
        this.registrationSave.click();
    }

   
    /**
     * select mandatory check box fields.
     * 
     * @param question
     *            (id of short and multiple question)
     * @param required
     *            (flag to set required and not required fields for custom
     *            registration questions)
     */
    public void setMandatoryCheckBoxField(final String question, final boolean required) {
        String questionId = this.driver.findElement(
                By.xpath("//a[text()='" + question + "']/../..//input[@type='checkbox']"))
                .getAttribute("id");
        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        StringBuilder script = new StringBuilder();
        script.append("var element = document.getElementById('" + questionId + "');");
        script.append("element.checked='" + required + "';");
        WebElement checkBox = this.driver.findElement(By.id(questionId));
        if (this.driver instanceof HtmlUnitDriver) {
            if (required) {
                js.executeScript(script.toString());
                checkBox.click();
            }
        } else {
            if (required) {
                this.setCheckBox(this.driver.findElement(By.id(questionId)), required);
            }
        }
    }

    /**
     * Method to set multiple choice question.
     * 
     * @param question
     *            (multiple choice question)
     * @param answer
     *            (answer for a question)
     * @param numOfAnswers
     *            (number of answers for the question)
     */
    public void setMultipleChoiceQuestionAndAnswer(final String question, final String answer,
            final int numOfAnswers) {
        if (numOfAnswers != 0) {
            this.createNewQuestion();
            this.findVisibleElement(this.questionTypeBtn).click();
            this.findVisibleElement(this.multipleAnswerOption).click();
            this.driver.findElement(this.newQuestionTxtBox).sendKeys(question);
            for (int i = 1; i <= 2; i++) {
                this.driver.findElement(By.id("newAnswerText_" + i)).sendKeys(answer + "_" + i);
            }
            for (int i = 3; i <= numOfAnswers; i++) {
                this.findClickableElement(this.addAnotherAnswerLink).click();
                this.driver.findElement(By.id("newAnswerText_" + i)).sendKeys(answer + "_" + i);
            }
            this.findClickableElement(this.createQuestionBtn).click();
            this.logger.logWithScreenShot("Set Multiple Answer Question: " + question, this.driver);
        }
    }

    /**
     * Method to set notify organizer on Registration Notification Section.
     */
    public void setNotifyOrganizer() {
        this.setCheckBox(this.findVisibleElement(this.notifyTrainer), true);
        this.logger.logWithScreenShot(
                "After set Notify Trainer on Registration Notification Section", this.driver);
    }


    /**
     * Method to set a new registration limit for the webinar.
     * 
     * @param registrationLimit
     *            (registration limit)
     */
    public void setRegistrationLimit(final String registrationLimit) {
        this.registrationLimit.clear();
        this.registrationLimit.sendKeys(registrationLimit);
        this.logger.logWithScreenShot(
                "After changing the attendee limit to : " + registrationLimit, this.driver);
    }

    /**
     * Method to select required and not required registration fields.
     * 
     * @param webinarRegistrationFields
     *            (webinar registration fields object)
     */
    public void setRequiredAndNotRequiredRegistrantFields(
            final WebinarRegistrationFields webinarRegistrationFields) {
        if (webinarRegistrationFields != null) {
            if (webinarRegistrationFields.isIncludeAddress()) {
                this.setCheckBox(this.addressAllowed, true);
            }
            if (webinarRegistrationFields.isAddressRequired()) {
                this.setCheckBox(this.addressRequired, true);
            }
            if (webinarRegistrationFields.isIncludeCity()) {
                this.setCheckBox(this.cityAllowed, true);
            }
            if (webinarRegistrationFields.isCityRequired()) {
                this.setCheckBox(this.cityRequired, true);
            }
            if (webinarRegistrationFields.isIncludeState()) {
                this.setCheckBox(this.stateAllowed, true);
            }
            if (webinarRegistrationFields.isStateRequired()) {
                this.setCheckBox(this.stateRequired, true);
            }
            if (webinarRegistrationFields.isIncludeZipCode()) {
                this.setCheckBox(this.zipAllowed, true);
            }
            if (webinarRegistrationFields.isZipCodeRequired()) {
                this.setCheckBox(this.zipRequired, true);
            }
            if (webinarRegistrationFields.isIncludeCountry()) {
                this.setCheckBox(this.countryAllowed, true);
            }
            if (webinarRegistrationFields.isCountryRequired()) {
                this.setCheckBox(this.countryRequired, true);
            }
            if (webinarRegistrationFields.isIncludePhone()) {
                this.setCheckBox(this.phoneAllowed, true);
            }
            if (webinarRegistrationFields.isPhoneRequired()) {
                this.setCheckBox(this.phoneRequired, true);
            }
            if (webinarRegistrationFields.isIncludeOrganization()) {
                this.setCheckBox(this.organizationAllowed, true);
            }
            if (webinarRegistrationFields.isOrganizationRequired()) {
                this.setCheckBox(this.organizationRequired, true);
            }
            if (webinarRegistrationFields.isIncludeJobTitle()) {
                this.setCheckBox(this.jobTitleAllowed, true);
            }
            if (webinarRegistrationFields.isJobTitleRequired()) {
                this.setCheckBox(this.jobTitleRequired, true);
            }
            if (webinarRegistrationFields.isIncludeQuestionAndComments()) {
                this.setCheckBox(this.commentsAllowed, true);
            }
            if (webinarRegistrationFields.isQuestionAndCommentsRequired()) {
                this.setCheckBox(this.commentsRequired, true);
            }
        }
    }

    /**
     * Method used to set short Question.
     * 
     * @param question
     *            (registrant short answer question)
     */
    public void setShortAnswerQuestion(final String question) {
        if (question != null) {
            this.createNewQuestion();
            this.findVisibleElement(this.questionTypeBtn).click();
            this.findVisibleElement(this.shortAnswerOption).click();
            this.driver.findElement(this.newQuestionTxtBox).sendKeys(question);
            this.findClickableElement(this.createQuestionBtn).click();
            this.logger.logWithScreenShot("Set Short Answer Question: " + question, this.driver);
        }
    }
    
    /**
     * Method used to set the Registration Confirmation Page URL.
     * 
     * @param URL
     */
    public void setOwnConfirmationPage(final String URL) {
        this.selectOwnConfirmationPage.click();
        this.confirmationLink.sendKeys(URL);
        this.registrationSave.click();
    }

    /**
     * Method to select all fields as required.
     * 
     * @param toggleRequired
     *            (webinar registration fields object)
     */
    public void setSelectAllRequiredRegistrantFields() {
    	this.toggleRequired.click();

    }
    /**
     * Method to select all fields as optional.
     * 
     * @param toggleFields
     *            (webinar registration fields object)
     */
    public void setSelectAllOptionalRegistrantFields() {
    	this.toggleFields.click();

    }
}