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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 *
 */
public class RegistrationConfirmationPage extends BasePage {

    /**
     * Web element for attendee join url.
     */
    @FindBy(id = "joinURL")
    private WebElement attendeeJoinUrl;
    
    /**
     * Web element for attendee calendar url.
     */
    @FindBy(id = "calendarUrl")
    private WebElement attendeeCalendarUrl;
    /**
     * Web element for cancel registration.
     */
    @FindBy(id = "cancelRegistrationLink")
    private WebElement cancelRegistration;
    /**
     * Web element for cancel registration confirmation.
     */
    @FindBy(id = "cancelRegConfirm")
    private WebElement confirmCancelRegistration;
    /**
     * Web element for content message.
     */
    @FindBy(xpath = "//div[@id='content']//h2")
    private WebElement contentMessage;
    private final String dateTimeForSameAttendeesXpath = "//ol[@id='webinar-times']//li";
    /**
     * Web element for header message.
     */
    @FindBy(xpath = "//div[@id='header']//h1")
    private WebElement headerMessage;
    /**
     * By element for materials link.
     */
    private final By materialsLink = By.id("materials");
    /**
     * WebElement for RegistrantID.
     */
    @FindBy(xpath = "//div[@id='RegistrantID_hdn']")
    private WebElement registrantID;
    /**
     * WebElement for RegistrantKey.
     */
    @FindBy(xpath = "//div[@id='RegistrantKey']")
    private WebElement registrantKey;
    /**
     * Web element for webinar date and time.
     */
    @FindBy(xpath = "//ol[@id='webinar-times']/li")
    private WebElement webinarDateAndTime;
    /**
     * Web element for webinar name.
     */
    @FindBy(xpath = "//div[@id='content']//h2")
    private WebElement webinarName;
    /**
     * instance variable used to hold error messages.
     */
        
    /**
     * Web element for contactEmail on registration confirmation.
     */
    @FindBy(id = "contactEmail")
    private WebElement contactEmail;
          
    private final String registrationPageAlertMessages =
            "//div[contains(@class,'alert-info')]/p";
        
    /**
     * Web element representing link indicating number of webinars in sequence
     */
    @FindBy(xpath = "//a[@class='recurring-sessions']")
    private WebElement recurringWebinarsSizeLink;
    
    /**
     * Web element representing webinar times for sequence webinar
     */
    @FindBy(xpath = "//ol[@class='trainingTimes']/li")
    private List<WebElement> recurringWebinarTimesList;

    /**
     * Web element for cancel registration link
     */
    @FindBy(xpath="//a[@data-target='#cancel-dialog']")
    private WebElement cancelRegistrationLink;

    /**
     * Url for theme
     */
     private By themeUrlBy = By.xpath("//link[contains(@href, 'theme')]");

    /**
     * Initialize constructor with registration URL and web driver object.
     * 
     * @param registrationCancelURL
     *            (attendee registration page URl)
     * @param webDriver
     *            (web driver object)
     */
    public RegistrationConfirmationPage(final String registrationCancelURL,
            final WebDriver webDriver) {
        this.driver = webDriver;
        if (!registrationCancelURL.equalsIgnoreCase("")) {
            this.driver.get(registrationCancelURL);
            this.logger.logWithScreenShot("Registration confirmation Page: ", this.driver);
        }
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/cancel"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Constructor to initialize web driver object and verify current page URL.
     * 
     * @param webDriver
     *            (object to initialize)
     */
    public RegistrationConfirmationPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("confirmation.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

   

    

    /**
     * Method to get the attendee join URL.
     * 
     * @return (attendee join URL)
     */
    public String getAttendeeJoinURL() {
        return this.attendeeJoinUrl.getAttribute("href");
    }

    /**
     * Method to return registration page content message.
     * 
     * @return (content message)
     */
    public String getContentMessage() {
        return this.contentMessage.getText();
    }

    /**
     * Method to store date and time values into list for same attendees.
     * 
     * @return dateAndTimeList (list of date and times in registration page)
     * @author Ravi Kant Soni
     * @since feb/20/2014
     */
    public List<String> getDateAndTimeAsListforSameAttendees() {
        this.logger.logWithScreenShot("List of Date & Time on Registration Page for Same Attendee",
                this.driver);
        List<String> dateAndTimeList = this
                .getListOfTitlesForElementsWithGivenXPath(this.dateTimeForSameAttendeesXpath);
        return dateAndTimeList;
    }

    /**
     * Method to return registration page header message.
     * 
     * @return (header message)
     */
    public String getHeaderMessage() {
        return this.headerMessage.getText();
    }

    /**
     * Method to get Registrant ID.
     * 
     * @return registrantID
     * @author Ravi Kant Soni
     * @since 12/16/2013
     */
    public String getRegistrantID() {
        String regUrl = this.getAttendeeJoinURL();
        String[] tokens = regUrl.split("/");

        String registrantId = tokens[tokens.length - 1];
        this.logger.log("Registrant Id:" + registrantId);

        return registrantId;
    }

    /**
     * Method to get Registrant Key.
     * 
     * @return registrantKey
     * @author Ravi Kant Soni
     * @since 12/16/2013
     */
    public String getRegistrantKey() {
        return this.registrantKey.getText();
    }

    /**
     * Method to return webinar date and time in registration confirmation
     * page.
     * 
     * @return (webinar date and time)
     */
    public String getWebinarDateAndTime() {
        return this.webinarDateAndTime.getText();
    }

    /**
     * Method to return webinar name in registration confirmation page.
     * 
     * @return (webinar name)
     */
    public String getWebinarName() {
        return this.webinarName.getText();
    }
    
    public String getThemeUrl(){
        WebElement themeUrlElement = driver.findElement(themeUrlBy);
         if(themeUrlElement != null){
             this.logger.log("ThemeUrlElement" + themeUrlElement);
             return themeUrlElement.getAttribute("href");
         }
          return null;
            
    }


    /**
     * Method to check is material link exists.
     * 
     * @return isMaterialLinkExists
     */
    public boolean isMaterialLinkExists() {
        boolean isMaterialLinkExists = true;
        try {
            this.findVisibleElement(this.materialsLink);
        } catch (Exception e) {
            isMaterialLinkExists = false;
        }
        this.logger.log("Is material link exists: " + isMaterialLinkExists);
        return isMaterialLinkExists;
    }

    /**
     * Method to fetch alert message fields.
     *
     * @return (registration page alert messages)
     */
    public List<String> getAlertMessages() {
        return this.getListOfTitlesForElementsWithGivenXPath(this.registrationPageAlertMessages);
    }
    
    /**
     * Method to return Contact Email in registration confirmation page.
     * 
     * @return (contact email)
     */
    public String getContactEmail() {
        return this.contactEmail.getText();
    }
    
    /**
     * Method to check is Contact Email exists.
     * 
     * @return isContactEmailExists
     */
    public boolean isContactEmailExists() {
        boolean isContactEmailExists = true;
        try {
            this.contactEmail.isDisplayed();
        } catch (Exception e) {
            isContactEmailExists = false;
        }
        this.logger.log("Is contact email exists: " + isContactEmailExists);
        return isContactEmailExists;
    }

    /**
     * Method to check is Join Link exists.
     * 
     * @return isJoinLinkExists
     */
    public boolean isJoinLinkExists() {
        boolean isJoinLinkExists = true;
        try {
            this.attendeeJoinUrl.isDisplayed();
        } catch (Exception e) {
            isJoinLinkExists = false;
        }
        this.logger.log("Is join link exists: " + isJoinLinkExists);
        return isJoinLinkExists;
    }

    /**
     * Method to check is Calendar Link exists.
     * 
     * @return isCalendarLinkExists
     */
    public boolean isCalendarLinkExists() {
        boolean isCalendarLinkExists = true;
        try {
            this.attendeeCalendarUrl.isDisplayed();
        } catch (Exception e) {
            isCalendarLinkExists = false;
        }
        this.logger.log("Is material link exists: " + isCalendarLinkExists);
        return isCalendarLinkExists;
    }
    
    /**
     * Method to return Calendar Link .
     * 
     * @return calendarUrl
     */
    public String getCalendarUrl() {
        return  this.attendeeCalendarUrl.findElement(By.tagName("a")).getAttribute("href");
    }
    

    /**
     * click on The Webinar Meets times.
     */

    public void clickWebinarsSizeLink() {
        this.recurringWebinarsSizeLink.click();
    }

    /**
     * Method to get Sequence Webinar Times occurrence.
     * 
     * @return
     */
    public List<String> getRecurringWebinarTimesList() {
        List<String> webinarTimeStrs = new ArrayList<String>();
        for (WebElement webinarTime : recurringWebinarTimesList) {
            webinarTimeStrs.add(webinarTime.getText().trim());
        }
        return webinarTimeStrs;
    }

    /**
     * Method to return cancel registration link element
     *
     * @return (cancellation link)
     */
    public WebElement getCancelRegistrationLink() {
        return this.cancelRegistrationLink;
    }

    /**
     * Method to return registration page confirmation message.
     *
     * @return (confirmation message)
     */
    public String getConfirmationMessage() {
        return findClickableElement(By.xpath("//div[@id='content']//h1")).getText();
    }


    /**
     * Method to click on the Yes option on the cancel registration confirmation popup
     */

    public void clickOnYesInPopup() {
        findClickableElement(By.xpath("//a[@class='btn btn-primary']")).click();
    }

    /**
     * Method to click on the No option on the cancel registration confirmation popup
     */

    public void clickOnNoInPopup() {
        findVisibleElement(By.xpath("//a[@class='btn']")).click();
    }
     
}
