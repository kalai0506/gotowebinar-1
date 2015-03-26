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

package com.citrix.g2w.webdriver.pages.registration;



import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.join.DownloadPage;

import edu.emory.mathcs.backport.java.util.Arrays;

/**

 */
public class RegistrationPage extends BasePage {

    /**
     * web element used to hold registrant address.
     */
    @FindBy(id = "registrant.address")
    private WebElement attendeeAddress;
    /**
     * web element used to hold registrant address label.
     */
    @FindBy(id = "registrant.addressLabel")
    private WebElement attendeeAddressLabel;
    /**
     * web element used to hold registrant city.
     */
    @FindBy(id = "registrant.city")
    private WebElement attendeeCity;
    /**
     * web element used to hold registrant city label.
     */
    @FindBy(id = "registrant.cityLabel")
    private WebElement attendeeCityLabel;
    /**
     * web element used to hold registrant country.
     */
    @FindBy(id = "registrant.country")
    private WebElement attendeeCountry;
    /**
     * web element used to hold registrant country label
     */
    @FindBy(id = "registrant.countryLabel")
    private WebElement attendeeCountryLabel;
    /**
     * web element used to hold registrant email id.
     */
    @FindBy(id = "registrant.email")
    private WebElement attendeeEmailId;
    /**
     * web element used to hold registrant email id label.
     */
    @FindBy(id = "registrant.emailLabel")
    private WebElement attendeeEmailIdLabel;
    /**
     * web element used to hold registrant first name.
     */
    @FindBy(id = "registrant.givenName")
    private WebElement attendeeFirstName;
    /**
     * web element used to hold registrant first name label.
     */
    @FindBy(id = "registrant.givenNameLabel")
    private WebElement attendeeFirstNameLabel;
    /**
     * web element used to hold registrant job title.
     */
    @FindBy(id = "registrant.jobTitle")
    private WebElement attendeeJobTitle;
    /**
     * web element used to hold registrant last name.
     */
    @FindBy(id = "registrant.surname")
    private WebElement attendeeLastName;
    /**
     * web element used to hold registrant last name label.
     */
    @FindBy(id = "registrant.surnameLabel")
    private WebElement attendeeLastNameLabel;
    /**
     * web element used to hold registrant organization.
     */
    @FindBy(id = "registrant.organization")
    private WebElement attendeeOrganization;
    /**
     * web element used to hold registrant phone.
     */
    @FindBy(id = "registrant.phone")
    private WebElement attendeePhone;
    /**
     * web element used to hold registrant state.
     */
    @FindBy(id = "registrant.state")
    private WebElement attendeeState;
    /**
     * web element used to hold submit button.
     */
    @FindBy(id = "registration.submit.button")
    private WebElement attendeeSubmit;
    /**
     * web element used to hold registrant zip code.
     */
    @FindBy(id = "registrant.zip")
    private WebElement attendeeZipCode;
    
   /**
    * Url for theme
    */
    private By themeUrlBy = By.xpath("//link[contains(@href, 'theme')]");
    
    /**
     * web element to hold purchasing timeframe.
     */
    @FindBy(id = "registrant.purchasingTimeFrame")
    private WebElement attendeePurchasingTimeFrame;
    /**
     * web element used to hold registrant purchasing role button.
     */
    @FindBy(id = "registrant.purchasingRole")
    private WebElement attendeePurchasingRole;
    /**
     * web element used to hold registrant number of employees.
     */
    @FindBy(id = "registrant.numberOfEmployees")
    private WebElement attendeeNumberOfEmployees;   
    /**
     * web element used to hold industry.
     */
    @FindBy(id = "registrant.industry")
    private WebElement attendeeIndustry;
    /**
     * web element used to hold questions & comments
     */
    @FindBy(id = "registrant.comments")
    private WebElement attendeeComments;
    /**
     * instance variable used to hold date and time for different attendees.
     */
    private final String dateTimeForDifferentAttendeesXpath = "//select[@id='times_1']//option";
    /**
     * instance variable used to hold date and time for same attendees.
     */
    private final String dateTimeForSameAttendeesXpath = "//ol[@id='webinar-times']//li";
    /**
     * instance variable used to hold error messages.
     */
    private final String registrationPageErrorMessages =
            "//form[@id='registrationForm']//div[contains(@class,'has-error')]//span[@class='help-block']";
    /**
     * web element used to hold registrant webinar date.
     */
    @FindBy(xpath = "//ol[@id='webinar-times']/li")
    private WebElement webinarDate;
    /**
     * by element used to hold registrant webinar description.
     */
    private final By webinarDescription = By
            .xpath("//form[@id='registrationForm']/p[@class='flush-top']");
    /**
     * web element used to hold registrant webinar name.
     */
    @FindBy(xpath = "//div[@class='page-header']//h1")
    private WebElement webinarName;
    /**
     * Web element used to get discount message.
     */
    @FindBy(xpath = "//div[@class='infoNotice']//p")
    private WebElement discountMessage;
    /**
     * Web element used to get price and link text.
     */
    @FindBy(xpath = "//div[@id='priceBox']/p")
    private WebElement priceInfo;

    /**
     * web element used to get message when attendee registers twice.
     */
    @FindBy(xpath = "//div[@id='content']/div[1]/p[1]")
    private WebElement messageWhenRegisteredTwice;

    /**
     * web element used to get 'enter registration information' text.
     */
    @FindBy(xpath = "//div[@id='studentInformation']/h2")
    private WebElement registrationInformation;

    /**
     * Web element representing webinar times for series webinar
     */
    @FindBy(xpath = "//select[@id='times_1']")
    private WebElement recurringWebinarTimesSelect;
    /**
     * Web element representing text for a recurring webinar
     */
    @FindBy(xpath = "//div[@id='recurringTrainingTimesBox']/div[1]")
    private WebElement recurringWebinarSelectionText;
    
    /**
     * Web element for save.
     */
    @FindBy(id = "times_1")
    private WebElement seriesWebinarDropDown;

    /**
     * Web element for Short Answer.
     */
    @FindBy(id = "customRegistrationSubmission.answers_1.text")
    private WebElement shortAnswer;    
    
    /**
     * Web element for Multiple Choice Answer.
     */
     @FindBy(id = "customRegistrationSubmission.answers_0.selectedOptions")
    private WebElement multipleChoiceAnswer;
    
    /**
     * initialize constructor with registration URL and web driver object.
     * 
     * @param registrationURL
     *            (attendee registration page URl)
     * @param webDriver
     *            (web driver object)
     */
    public RegistrationPage(final String registrationURL, final WebDriver webDriver) {
        this.driver = webDriver;
        if (!registrationURL.equalsIgnoreCase("")) {
            this.driver.get(registrationURL);
            this.logger.logWithScreenShot("Registration Page: ", this.driver);
        }
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/r"));
        PageFactory.initElements(this.driver, this);
    }

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/r"));
        PageFactory.initElements(this.driver, this);
	}

	/**
     * Method to register attendee with success.
     * 
     * @param registrantDetails
     *            (registrant details object)
     * @return (attendee registration confirmation page object)
     */
    public RegistrationConfirmationPage registerAttendeeDetailsWithConfirmation(
            final RegistrantDetails registrantDetails) {
        if (registrantDetails != null) {
        	this.setRegistrationFieldValues(registrantDetails);        
        }
        this.logger.logWithScreenShot("Registrant information:", this.driver);
        this.attendeeSubmit.click();
        this.logger.logWithScreenShot("Registrant Confirmation Page:", this.driver);
        return new RegistrationConfirmationPage(this.driver);
    }
    
    /**
     * Method to register attendee for a Series.
     * 
     * @param registrantDetails
     *            (registrant details object)
     * @return (attendee registration confirmation page object)
     */
    public RegistrationConfirmationPage registerForSeriesWebinarWithConfirmation(
            final RegistrantDetails registrantDetails, int webinarIndexInDropdown) {
        if (registrantDetails != null) {
            this.setRegistrationFieldValues(registrantDetails);
        }
        this.logger.logWithScreenShot("Registrant information:", this.driver);
        Select select = new Select(seriesWebinarDropDown);        
        this.logger.logWithScreenShot("After click on dropdown", this.driver);
        select.selectByIndex(webinarIndexInDropdown);
        this.attendeeSubmit.click();
        this.logger.logWithScreenShot("Registrant Confirmation Page:", this.driver);
        return new RegistrationConfirmationPage(this.driver);
    }

    
    /**
     * Method to register attendee to Webinar Full.
     * 
     * @param registrantDetails
     *            (registrant details object)
     * @return (attendee registration confirmation page object)
     */
    public WebinarFullForRegistrationPage registerAttendeeGoesToWebinarFullForRegistrationPage(
            final RegistrantDetails registrantDetails) {
        if (registrantDetails != null) {
            this.setRegistrationFieldValues(registrantDetails);
        }
        this.logger.logWithScreenShot("Registrant information:", this.driver);
        this.attendeeSubmit.click();
        this.logger.logWithScreenShot("Registrant Confirmation Page:", this.driver);
        return new WebinarFullForRegistrationPage(this.driver);
    }

    /**
     * Method to make the attendee join a webinar in progress from registration page.
     * 
     * @param registrantDetails
     *            (registrant details object)
     * @return (downloadPage g2w download page)
     */
    public DownloadPage joinAttendeeToWebinarInProgress(final RegistrantDetails registrantDetails) {
        this.attendeeFirstName.sendKeys(registrantDetails.getFirstName());
        this.attendeeLastName.sendKeys(registrantDetails.getLastName());
        this.logger.logWithScreenShot("Registrant information:", this.driver);
        this.attendeeSubmit.click();
        return new DownloadPage(this.driver);
    }

    /**
     * close a single browser window.
     * 
     * @author Ravi Kant Soni
     * @since 12/17/2013
     */
    public void closePage() {
        this.driver.close();
    }

    /**
     * Method to store date and time values into list for different attendees.
     * 
     * @return dateAndTimeList (list of date and times in registration page)
     */
    public List<String> getDateAndTimeAsListforDifferentAttendees() {
        List<String> dateAndTimeList =
                this.getListOfTitlesForElementsWithGivenXPath(this.dateTimeForDifferentAttendeesXpath);
        return dateAndTimeList;
    }

    /**
     * Method to store date and time values into list for same attendees.
     * 
     * @return dateAndTimeList (list of date and times in registration page)
     */
    public List<String> getDateAndTimeAsListforSameAttendees() {
        this.logger.logWithScreenShot("List of Date & Time on Registration Page for Same Attendee",
                this.driver);
        List<String> dateAndTimeList =
                this.getListOfTitlesForElementsWithGivenXPath(this.dateTimeForSameAttendeesXpath);
        return dateAndTimeList;
    }

    /**
     * Method to get webinar date and time for recurring series webinar.
     * 
     * @return listOfDateAndTimes
     */
    public List<String> getDateAndTimeForRecurringSeriesWebinar() {
        List<String> listOfDateAndTimes = null;
        listOfDateAndTimes =
                this.getListOfTitlesForElementsWithGivenXPath("//select[@name='webinar']//option");
        this.logger.log("Webinar date and times: " + listOfDateAndTimes);
        return listOfDateAndTimes;
    }


    /**
    * Method to get the text 'Enter your Registration information'.
    * @return message
    */
    public String getEnterRegistrationInformationText() {
        String message = this.registrationInformation.getText();
        this.logger.log("Registration information text :" + message);
        return message;
    }

    /**
     * Method to fetch error message fields.
     * 
     * @return (registration page error messages)
     */
    public List<String> getErrorMessages() {
        List<String> errorMessages =
                this.getListOfTitlesForElementsWithGivenXPath(this.registrationPageErrorMessages);
        return errorMessages;
    }

    /**
    * Method to get message when attendee registers twice.
    * @return messageWhenRegisteredTwice
    */
    public String getMessageWhenAttendeeRegistersTwice() {
        String messageWhenRegisteredTwice = this.messageWhenRegisteredTwice.getText();
        this.logger.log("Message when attendee registers twice : " + messageWhenRegisteredTwice);
        return messageWhenRegisteredTwice;
    }

    /**
     * Method to return all optional questions as list.
     * 
     * @return (optional question label name list)
     */
    public List<String> getOptionalQuestions() {
        List<String> optionalQuestionLableNameList = new ArrayList<String>();
        try {
            List<String> requiredFieldsList =
                    Arrays.asList(new String[] {
                            "//div[@id='studentInformation']//div[not(contains(@class,'required'))]//label",
                            "//div[@id='customQuestions']//div[not(contains(@class,'required'))]//label",
                            "//div[not(contains(@class,'required')) and (@id='comments')]//label"});
            for (String fieldsXpath : requiredFieldsList) {
                List<WebElement> optionalQuestionLableNameWebElements =
                        this.driver.findElements(By.xpath(fieldsXpath));
                for (WebElement webElement : optionalQuestionLableNameWebElements) {
                    optionalQuestionLableNameList.add(webElement.getText());
                }
            }
            return optionalQuestionLableNameList;
        } catch (Exception e) {
            return optionalQuestionLableNameList;
        }
    }
    
    public String getThemeUrl(){
        try {
            WebElement themeUrlElement = driver.findElement(themeUrlBy);
            if (themeUrlElement != null) {
                this.logger.log("ThemeUrlElement" + themeUrlElement);
                return themeUrlElement.getAttribute("href");
            }
        } catch (NoSuchElementException nse) {
            this.logger.log("NoSuchElementException ");
        }
        return null;

    }

    /**
     * Method to retrieve webinar description.
     * 
     * @return (webinar description)
     */
    public String getRegistraionPageWebinarDescription() {
        return this.driver.findElement(this.webinarDescription).getText();
    }

    /**
     * Method to retrieve webinar date.
     * 
     * @return (webinar date)
     */
    public String getRegistrationPageWebinarDate() {
        return this.webinarDate.getText();
    }

    /**
     * Method to retrieve webinar name.
     * 
     * @return (webinar name)
     */
    public String getRegistrationPageWebinarName() {
        return this.webinarName.getText();
    }

    /**
     * Method to return all required questions as list.
     * 
     * @return (required question label name list)
     */
    public List<String> getRequiredQuestions() {
        List<String> requiredQuestionLableNameList = new ArrayList<String>();
        try {
            List<String> requiredFieldsList =
                    Arrays.asList(new String[]{
                            "//div[@id='studentInformation']//div[contains(@class,'required')]//label",
                            "//div[@id='customQuestions']//div[contains(@class,'required')]//label",
                            "//div[contains(@class,'required') and (@id='comments')]//label"});
            for (String fieldsXpath : requiredFieldsList) {
                List<WebElement> requiredQuestionLableNameWebElements =
                        this.driver.findElements(By.xpath(fieldsXpath));
                for (WebElement webElement : requiredQuestionLableNameWebElements) {
                    requiredQuestionLableNameList.add(webElement.getText());
                }
            }
            return requiredQuestionLableNameList;
        } catch (Exception e) {
            return requiredQuestionLableNameList;
        }
    }


    /**
     * Overloaded method to register attendee information.
     * 
     * @param registrantDetails
     *            (registrant details object)
     * @return (attendee registration page object)
     */
    public RegistrationPage registerAttendeeDetails(final RegistrantDetails registrantDetails) {
        if (registrantDetails != null) {
            this.setRegistrationFieldValues(registrantDetails);
        }
        this.attendeeSubmit.click();
        this.logger.logWithScreenShot(
                "Registration Page Errors when Registrant did not fill mandatory fields:",
                this.driver);
        return new RegistrationPage("", this.driver);
    }
  

    /**
     * Method to set values for comments text area.
     * 
     * @param answer
     *            (answer for the question)
     * @param required
     *            (set answer or not)
     */
    public void setCommentsAnswer(final String answer, final boolean required) {
        if (answer != null && required) {
            this.findVisibleElement(By.id("registrant.comments")).sendKeys(answer);
        }
    }

    /**
     * method to set values for registration fields.
     * 
     * @param registrantDetails
     *            (registrant details object)
     */
    private void setRegistrationFieldValues(final RegistrantDetails registrantDetails) {
        if (registrantDetails.getFirstName() != null) {
            this.attendeeFirstName.sendKeys(registrantDetails.getFirstName());
        }
        if (registrantDetails.getLastName() != null) {
            this.attendeeLastName.sendKeys(registrantDetails.getLastName());
        }
        if (registrantDetails.getEmailAddress() != null) {
            this.attendeeEmailId.sendKeys(registrantDetails.getEmailAddress());
        }
        if (registrantDetails.getAddress() != null) {
            this.attendeeAddress.sendKeys(registrantDetails.getAddress());
        }
        if (registrantDetails.getCity() != null) {
            this.attendeeCity.sendKeys(registrantDetails.getCity());
        }
        if (registrantDetails.getState() != null) {
            Select state = new Select(this.attendeeState);
            state.selectByValue(registrantDetails.getState());
        }
        if (registrantDetails.getZipCode() != null) {
            this.attendeeZipCode.sendKeys(registrantDetails.getZipCode());
        }
        if (registrantDetails.getCountry() != null) {
            Select country = new Select(this.attendeeCountry);
            country.selectByValue(registrantDetails.getCountry());
        }
        if (registrantDetails.getPhone() != null) {
            this.attendeePhone.sendKeys(registrantDetails.getPhone());
        }
        if (registrantDetails.getJobTitle() != null) {
            this.attendeeJobTitle.sendKeys(registrantDetails.getJobTitle());
        }
        if (registrantDetails.getOrganization() != null) {
            this.attendeeOrganization.sendKeys(registrantDetails.getOrganization());
        }
        if (registrantDetails.getPurchasingTimeframe() != null) {
            Select purchasingTimeframe = new Select(this.attendeePurchasingTimeFrame);
            purchasingTimeframe.selectByValue(registrantDetails.getPurchasingTimeframe());
        }
        if (registrantDetails.getPurchasingRole() != null) {
            Select purchasingRole = new Select(this.attendeePurchasingRole);
            purchasingRole.selectByValue(registrantDetails.getPurchasingRole());
        }
        if (registrantDetails.getNumberOfEmployees() != null) {
            Select numberOfEmployees = new Select(this.attendeeNumberOfEmployees);
            numberOfEmployees.selectByValue(registrantDetails.getNumberOfEmployees());
        }
        if (registrantDetails.getIndustry() != null) {
            Select industry = new Select(this.attendeeIndustry);
            industry.selectByValue(registrantDetails.getIndustry());
        }    
        if (registrantDetails.getQuestionAndComments() !=null) {
        	this.attendeeComments.sendKeys(registrantDetails.getQuestionAndComments());
        }
    }

    /**
     * Method to set values for Multiple Choice answer.
     * 
     * @param question
     *            (short answer question)
     * @param answer
     *            (answer for the question)
     * @param required
     *            (set answer or not)
     */
    public void setMultipleAnswerQuestion(final String question, final String answer,
            final boolean required) {
        if (answer != null && required) {
            this.findVisibleElement(By.xpath("//label[text()='" + question + "']/..//textarea"))
                    .sendKeys(answer);
        }
    }    
    
    
    /**
     * Method to set values for Short question answer.
     * 
     * @param question
     *            (short answer question)
     * @param answer
     *            (answer for the question)
     * @param required
     *            (set answer or not)
     */
    public void setShortQuestionAnswer(final String question, final String answer,
            final boolean required) {
        if (answer != null && required) {
            this.findVisibleElement(By.xpath("//label[text()='" + question + "']/..//textarea"))
                    .sendKeys(answer);
        }
    }

    public String getAttendeeEmailId() {
        return attendeeEmailId.getAttribute("value");
    }

    public String getAttendeeEmailIdLabel() {
        return attendeeEmailIdLabel.getText();
    }

    public String getAttendeeFirstName() {
        return attendeeFirstName.getText();
    }

    public String getAttendeeFirstNameLabel() {
        return attendeeFirstNameLabel.getText();
    }

    public String getAttendeeLastName() {
        return attendeeLastName.getText();
    }

    public String getAttendeeLastNameLabel() {
        return attendeeLastNameLabel.getText();
    }

    public List<String> getRecurringWebinarTimesList() {
        List<String> webinarTimeStrs = new ArrayList<String>();
        List<WebElement> recurringWebinarTimesList = null;
        try {
            recurringWebinarTimesList = findPresenceOfInnerTextForElements(By.xpath("//ol[@id='training-times']//li"), 30);
        } catch (Exception e) {
            clickWebinarsSizeLink();
            recurringWebinarTimesList = findPresenceOfInnerTextForElements(By.xpath("//ol[@id='training-times']//li"), 30);
        }
        for(WebElement webinarTime : recurringWebinarTimesList) {
           webinarTimeStrs.add(webinarTime.getText().trim());
        }
        return webinarTimeStrs;
    }

    public void clickWebinarsSizeLink() {
    	WebElement recurringWebinarsSizeLink = findClickableElement(By.xpath("//a[@class='recurring-sessions']"));
        recurringWebinarsSizeLink.click();
        this.logger.logWithScreenShot("Clicked the link to expand webinar timings.",this.driver);
    }

    /**
     * Click Register button to Register Attendee on Registration Page after
     * setting up the registrantdetails
     * 
     * @param registrantDetails
     */
    public void clickToRegisterAttendee(
            final RegistrantDetails registrantDetails) {
        if (registrantDetails != null) {
            this.setRegistrationFieldValues(registrantDetails);
        }
        this.attendeeSubmit.click();
        this.logger
        .logWithScreenShot(
                "Registration Page Errors when Registrant did not fill mandatory fields:",
                this.driver);
    }
    
    /**
     * Gets the session selection message for a recurring webinar
     * @return message Message string for a recurring webinar in registration page
     */
	public String getRecurringWebinarSelectionText() {
		String message = recurringWebinarSelectionText.getText();
        this.logger.log("Selection message for recurring webinar :" + message);
        return message;		
	}
	
    /**
     * Gets the selected webinar key details for a recurring webinar serires
     * @return selectedSession selected webinar key 
     */
	public String getSelectedRecurringWebinarKey() {
	    Select select = new Select(recurringWebinarTimesSelect);
	    String selectedSession = select.getFirstSelectedOption().getAttribute("value");
        this.logger.log("Selected webinar key in registration page :" + selectedSession);
        return selectedSession;		
	}	
	
    /**
     * Method to register for webinar or recording which is not available.
     * 
     * @param registrantDetails
     *            (registrant details object)
     * @return (attendee registration  page object)
     */
    public WebinarNotAvailablePage registerForCancelledWebinarShowsWebinarNotAvailablePage(
            final RegistrantDetails registrantDetails) {
        if (registrantDetails != null) {
            this.setRegistrationFieldValues(registrantDetails);
        }
        this.logger.logWithScreenShot("Registrant information:", this.driver);
        this.attendeeSubmit.click();
        this.logger.logWithScreenShot("Registrant Webinar Not Available Page:", this.driver);
        return new WebinarNotAvailablePage(this.driver);
    }
    
    /**
     * Method to register with mandatory fields and some required fields blank.
     * 
     * @param registrantDetails
     *            (registrant details object)
     * @return (attendee registration  page object)
     */
    public RegistrationPage registerWithCustomRequiredFieldsEmpty(
            final RegistrantDetails registrantDetails) {
        if (registrantDetails != null) {
            this.setRegistrationFieldValues(registrantDetails);
        }
        this.logger.logWithScreenShot("Registrant information:", this.driver);
        this.attendeeSubmit.click();
        this.logger.logWithScreenShot("Registration Page", this.driver);
        return new RegistrationPage(this.driver);
    }    
}
