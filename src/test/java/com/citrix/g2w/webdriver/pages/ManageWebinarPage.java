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

package com.citrix.g2w.webdriver.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.branding.settings.ManageBrandingSettingsPage;
import com.citrix.g2w.webdriver.pages.emails.ReminderEmailPage;
import com.citrix.g2w.webdriver.pages.managewebinar.ManageFollowUpEmailPage;
import com.citrix.g2w.webdriver.pages.managewebinar.ManageRegistrationSettingsPage;
import com.citrix.g2w.webdriver.pages.managewebinar.PanelistPage;
import com.citrix.g2w.webdriver.pages.survey.CreateSurveyPage;
import com.citrix.g2w.webdriver.pages.webinar.AddAnotherSessionPage;
import com.citrix.g2w.webdriver.pages.ScheduleSimilarWebinarPage;



/**

 */
public class ManageWebinarPage extends BasePage {

    /**
     * By element used to get all audio conference call numbers.
     */
    private final By allAudioConfCallNumbers = By.id("ccnumURL");
    /**
     * By element for banner message.
     */
    private final By bannerMessage = By.xpath("//div[@class='banner-content']/p");
    /**
     * Web element used to get cancel webinar message from light box.
     */
    @FindBy(xpath = "//div[@id='cancel-webinar-container']/p[4]")
    private WebElement cancellingAPaidWebinarWithNoRegistrants;
    /**
     * Web element used to get cancel webinar message from light box.
     */
    @FindBy(xpath = "//div[@id='cancel-webinar-container']/p[2]/strong")
    private WebElement cancellingAPaidWebinarWithRegistrants;
    /**
     * Webelement to cancel publishing webinar.
     */
    @FindBy(xpath = "//label[@id='cancel']/input")
    private WebElement cancelPublishWebinar;
    /**
     * WebElement for cancel link in reply to email.
     */
    @FindBy(id = "replyToCancel")
    private WebElement cancelReplyToEmail;
    
    /**
     * WebElement to Edit Panelist for Webinar.
     */
    @FindBy(id = "editPanelist")
    private WebElement editPanelist;
    
    /**
     * By element used to cancel webinar.
     */
    private final By cancelWebinar = By.xpath("//a[@href='#cancelWebinarContainer']");
    /**
     * By element used to click yes while cancelling the webinar.
     */
    private final By cancelYes = By.id("confirmDelete");
    /**
     * By element used to get success message after creating catalog.
     */
    private final By catalogCreatedSuccessMessage = By
            .xpath("//div[@class='container-content']/div[2]/div[1]//p");
    /**
     * Web element used to confirm the cancellation.
     */
    @FindBy(id = "confirmCancel")
    private WebElement confirmCancel;
    /**
     * Web element used to confirm the continue cancellation.
     */
    @FindBy(xpath = " id('cancel-continue')/div/span")
    private WebElement continueCancelWebinar;
    /**
     * By element used to get copy webinar information details.
     */
    private final By copyWebinarInformation = By.id("webinar-copy-description");
    /**
     * Web element used to click copy webinar information link.
     */
    @FindBy(id = "main-copy-clipboard-link")
    private WebElement copyWebinarInformationLink;
    /**
     * Web element used to navigate to create catalog by link.
     */
    @FindBy(id = "createCatalogLink")
    private WebElement createCatalogLink;
    /**
     * Web element used to click on donot cancel webinar link.
     */
    @FindBy(xpath = "//div[@id='cancel-webinar-container']//a[3]")
    private WebElement doNotCancelWebinar;
    /**
     * Web element used to click editAudio webinar.
     */
    @FindBy(id = "editAudio")
    private WebElement editAudio;
    /**
     * Web element used to edit catalog.
     */
    @FindBy(id = "edit-catalog")
    private WebElement editCatalog;
    /**
     * By element to edit Co-Organizers.
     */
    private final By editCoTrainers = By.id("setCoOrgs");
    /**
     * Web element to edit webinar date and time.
     */
    @FindBy(id = "editDateTime")
    private WebElement editDateTime;
    /**
     * Web element to edit follow-up email.
     */
    @FindBy(id = "editFollowUp")
    private WebElement editFollowUp;
    /**
     * Web element to edit follow-up email for absentees.
     */
    @FindBy(id = "editFollowUpToAbsentees")
    private WebElement editFollowUpToAbsentees;    
    /**
     * Web element used to edit materials link.
     */
    @FindBy(id = "editMaterials")
    private WebElement editMaterialsLink;
    /**
     * Web element to edit webinar title.
     */
    @FindBy(id = "editNameDescription")
    private WebElement editNameAndDescription;
    /**
     * Web element used to edit polls.
     */
    @FindBy(id = "editPolls")
    private WebElement editPolls;
    /**
     * Web element used to edit registration settings link.
     */
    @FindBy(id = "editRegistrationSettings")
    private WebElement editRegistrationSettingsLink;
    
    /**
     * Web element used to edit registration settings link.
     */
    @FindBy(id = "editBranding")
    private WebElement editBrandingLink;
    
    /**
     * Web element to edit reminder email settings.
     */
    @FindBy(id = "editReminder")
    private WebElement editReminder;
    /**
     * By element used to edit reply to email.
     */
    
    @FindBy(id = "editReplyToAddress")
    private WebElement editReplyToEmail;
    /**
     * Web element to edit survey.
     */
    @FindBy(id = "editSurveys")
    private WebElement editSurvey;
    /**
     * Web element used to edit tests.
     */
    @FindBy(id = "editTests")
    private WebElement editTests;
    /**
     * By element for inline email error in reply to email form.
     */
    private final By emailErrorInReplyToEmailForm = By
            .xpath("//form[@id='reply-to-form']//div[@class='form-row'][2]//p");
    /**
     * Web element for email in reply to email on manage webinar page.
     */
    @FindBy(id = "replyToEmail")
    private WebElement emailInReplyToEmail;
    /**
     * By element for email field in reply to email form.
     */
    private final By emailInReplyToEmailForm = By
            .xpath("//form[@id='reply-to-form']//input[@id='email']");
    /**
     * Web element to get facebook cancel message.
     */
    @FindBy(xpath = "//div[@id='facebookDialog-cancel']/div/p")
    private WebElement faceBookCancelMsg;
    /**
     * Webelement to select facebook profile.
     */
    @FindBy(xpath = "//ul[@id='facebookSelectProfileOrPageContent']/li/a")
    private WebElement faceBookProfile;
    /**
     * Web element to get face book success msg.
     */
    @FindBy(xpath = "//div[@id='facebookDialog-success']/div/p")
    private WebElement faceBookSuccessMsg;
    /**
     * Web element used to filter catalogs.
     */
    @FindBy(id = "filter-catalogs")
    private WebElement filterCatalogs;
    
    /**
     * Web element to send canceled webinar notifications.
     */
    @FindBy(id = "cancellationNotice.submit")
    private WebElement cancellationNotice;
    
    /**
     * Web element used to check light box present.
     */
    @FindBy(id = "cancel-webinar-container")
    private WebElement lightBox;
    /**
     * web element used to click on linkedIn link.
     */
    @FindBy(id = "linkedInShare")
    private WebElement linkedInLink;
    /**
     * Web Element to click on manage registrants link.
     */
    @FindBy(id = "regMgmtLink")
    private WebElement manageRegistrants;
    /**
     * Web element used to get maximum attendee limit.
     */
    @FindBy(id = "wtRegMaxAttendees")
    private WebElement maxAttendeeLimit;
    /**
     * By element used to get error message for minimum character on name field
     * in edit reply to email form
     */
    private final By minCharNameErrorInReplyToEmail = By
            .xpath("//form[@id='reply-to-form']//p[@for='name']");
    /**
     * By element for inline name error in reply to email form.
     */
    private final By nameErrorInReplyToEmailForm = By
            .xpath("//form[@id='reply-to-form']//div[@class='form-row'][1]//p");
    /**
     * Web element for name in reply to email on manage webinar page.
     */
    @FindBy(id = "replyToName")
    private WebElement nameInReplyToEmail;
    /**
     * By element for name field in reply to email form.
     */
    private final By nameInReplyToEmailForm = By
            .xpath("//form[@id='reply-to-form']//input[@id='name']");
    /**
     * Web Element used to get survey "None" message.
     */
    @FindBy(xpath = "//div[@id='surveys']//span")
    private WebElement noSurveyMsg;
    /**
     * Web element used to get "No Materials" message.
     */
    @FindBy(xpath = "//div[@id='materials']/p")
    private WebElement noMaterialsMsg;
    /**
     * Web element used to get polls "None" message.
     */
    @FindBy(xpath = "//div[@id='polls']/p")
    private WebElement noPollsMsg;
    /**
     * Web element used to get "No Tests" message.
     */
    @FindBy(xpath = "//div[@id='tests']/p")
    private WebElement noTestsMsg;
    /**
     * By element used to share or cancel to face book.
     */
    private final By postToFaceBookForm = By.xpath("//iframe[@class='FB_UI_Dialog']");
    /**
     * Web element used to click on facebook link.
     */
    @FindBy(id = "facebookDialog")
    private WebElement postToFaceBookLink;
    // Static elements on the page
    /**
     * Web element used to click on twitter link.
     */
    @FindBy(id = "tweetRegistration")
    private WebElement postToTwitterLink;
    /**
     * Webelement to publish webinar.
     */
    @FindBy(xpath = "//label[@id='publish']/input")
    private WebElement publishWebinar;
    /**
     * Web element used to get to get registrants count.
     */
    @FindBy(xpath = "//div[@id='registration-management']//span[starts-with(@id,'reg_')]")
    private WebElement registrantsCount;
    /**
     * Web element used to get registration URL.
     */
    @FindBy(id = "registrationURL")
    private WebElement registrationUrl;
    /**
     * by element for Reset link in reply to email.
     */
    private final By replyToReset = By.xpath("//a[@id='reply-to-reset']");
    /**
     * By element used to save catalog.
     */
    private final By saveCatalog = By.id("catalog-modal-save");
    /**
     * WebElement for save button in reply to email.
     */
    @FindBy(id = "replyToSubmit")
    private WebElement saveReplyToEmail;
    /**
     * By element used to get scheduling organizer name.
     */
    private final By schedulingOrganizerName = By.id("so");
    /**
     * Web element used to send cancellation email.
     */
    @FindBy(id = "cancellation-email-submit")
    private WebElement sendCancellationEmail;
    /**
     * By element used to click on share link.
     */
    private final By shareLink = By.xpath("//a[@id='invite-button']/div");
    /**
     * Web element used to get webinar date and time.
     */
    @FindBy(xpath = "//div[@id='dateTime']/p")
    private WebElement webinarDateAndTime;
    /**
     * Web element used to get webinar description.
     */
    @FindBy(id = "webinarDesc")
    private WebElement webinarDesc;
    /**
     * Web element used to get webinar info(description).
     */
    @FindBy(id = "webinar-copy-description")
    private WebElement webinarInfo;
    /**
     * By element used to click on webinar information link.
     */
    private final By webinarInfoLink = By.id("share-copy-clipboard-link");
    /**
     * By element to get list of webinar keys for a recurring webinar.
     */
    private final By webinarKeysElement = By.xpath("//div[@id='dateTimeRecurring']//ul[@id]");
    /**
     * Web element used to get webinar name.
     */
    @FindBy(id = "trainingName")
    private WebElement webinarName;
    
    /**
     * Web element for Reply To Email ID.
     */
    @FindBy(id = "email")
    private WebElement replytoEmailID;

    /**
     * Web element for replyToSubmit Button.
     */
    @FindBy(id = "replyToSubmit")
    private WebElement replyToSubmitButton;
    
    @FindBy(id = "addSession")
    private  WebElement addAnotherSession ;   
    /** saraswathi
     * webelement for schedule webnar success msg
     */
   @FindBy(className = "banner-content")
 private WebElement scheduleWebnarSuccessMsg;    
   
   /**
    * webelement for schedulewebnar
    */
   
   @FindBy(id = "scheduleSimilar")
   private WebElement  schedulesimilarWebnar;
   /**
    * webelement for webnar copied success msg
    */
   @FindBy(className = "banner-content")
   private WebElement webnarCopiedSuccessMsg;
   /*
    * web element for toget mywebinars page
    */
   @FindBy(linkText = "My Webinars")
   private WebElement myWebinarsPage;
   /**
    * method for get webnar copied success msg
    */
   public String getWebnarCopiedSuccessMsg()
   {
	   return webnarCopiedSuccessMsg.getText();
   }
    /**
     * Constructor to initialize web driver and verify current page URL.
     * @param webDriver
     *            (Web Driver object)
     */
    public ManageWebinarPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("manageWebinar.tmpl"));
        String manageWebinarUrl = this.driver.getCurrentUrl();
        this.webinarKey = manageWebinarUrl.substring(manageWebinarUrl.indexOf("=") + 1);
        PageFactory.initElements(this.driver, this);
    }
    
    /**
     * Method to go to Manage registration settings page.
     * @return (registration page object)
     */
    public ManageRegistrationSettingsPage goToManageRegistrationSettingsPage() {
        this.editRegistrationSettingsLink.click();
        this.logger.logWithScreenShot("Navigating to Registration", this.driver);
        return new ManageRegistrationSettingsPage(this.driver);
    }
    
    /**
     * Method to go to Manage branding settings page.
     * @return (branding settings page object)
     */
    public ManageBrandingSettingsPage goToManageBrandingSettingsPage() {
        this.editBrandingLink.click();
        this.logger.logWithScreenShot("Navigating to Branding settings page", this.driver);
        return new ManageBrandingSettingsPage(this.driver);
    }
    
    /**
     * Method to go to Panelist page.
     * @return (panelist page object)
     */
    public PanelistPage gotoPanelistPage() {
        this.editPanelist.click();
        this.logger.logWithScreenShot("Navigating to Panelist page", this.driver);
        return new PanelistPage(this.driver);
    }

    /**
     * Method to go to follow up email page for attendee.
     * @return ManageFollowUpEmailPage
     */
    public ManageFollowUpEmailPage goToFollowUpEmailPageForAttendee() {
        this.editFollowUp.click();
        this.logger.logWithScreenShot("After Navigate to Edit follow Up Email Page for attendee", this.driver);
        return new ManageFollowUpEmailPage(this.driver);
    }

    /**
     * Method to go to follow up email page for absentees.
     * @return ManageFollowUpEmailPage
     */
    public ManageFollowUpEmailPage goToFollowUpEmailPageForAbsentees() {
        this.editFollowUpToAbsentees.click();
        this.logger.logWithScreenShot("After Navigate to Edit follow Up Email Page for absentees", this.driver);
        return new ManageFollowUpEmailPage(this.driver);
    }
    /**
     * Method used to get registrants count.
     * @return registrantsCount
     */
    public String getRegistrantsCount() {
        return this.registrantsCount.getText();
    }

    /**
     * Method to get Registration URL displayed on the page.
     * @return (return registration page URL)
     */
    public String getRegistrationURL() {
        return this.registrationUrl.getText();
    }



    /**
     * Method to get webinar description displayed on the page.
     * @return (return webinar description in the page)
     */
    public String getWebinarDescription() {
        return this.webinarDesc.getText();
    }

    /**
     * Get the webinar name
     * @return webinarName name of webinar
     */
 	public String getWebinarName() {
 		return this.webinarName.getText();
 	}   
 	
    /**
     * 
     * Method to changeReplyTo Email on Manage Webinar Page
     * @param emailID
     */
    
    public void changeReplyToEmail(String emailID){     
        this.editReplyToEmail.click();
        this.replytoEmailID.clear();        
        this.replytoEmailID.sendKeys(emailID);
        this.replyToSubmitButton.click();
    }   
    /** saraswathi
     * method to get webnarschedule success msg
     */
    public String getWebnarSuccessMsg()
    {
    	return this.scheduleWebnarSuccessMsg.getText();
    }
    /**
     * method to go schedulusimilar webnar page
     */
    public ScheduleSimilarWebinarPage getScheduleSimilarWebnar()
    {
    	schedulesimilarWebnar.click();
    	System.out.println("schedule semilar page");

		return new ScheduleSimilarWebinarPage(this.driver);

		
    }
    /**
     * Method to get Webinar id of the Webinar.
     * 
     * @return Webinar id
     */
    public Long getWebinarId() {
        WebElement element = this.driver.findElement(By
                .xpath("id('registrationURL')/following-sibling::span"));
        String webinarId = element.getText().replaceAll("-", "");
        this.logger.log("Webinar Id:" + webinarId);

        return Long.valueOf(webinarId);
    }
    
    /**
     * Method to get Webinar key of the Webinar.
     * 
     * @return Webinar key
     */
    public Long getWebinarKey() {
        String webinarKey = this.driver.getCurrentUrl().split("=")[1];

        this.logger.log("Webinar key:" + webinarKey);
        return Long.valueOf(webinarKey);
    }
    
 
    /**
     * Go to Survey page.
     * 
     * @return createSurveyPage
     */
    public CreateSurveyPage gotoSurveyPage() {
        this.editSurvey.click();
        this.logger.logWithScreenShot("Navigating to Survey Page", this.driver);
        return new CreateSurveyPage(this.driver);
    }
    
    /**
     * Go to Add another session page.
     * 
     * @return AddAnotherSessionPage
     */
    public AddAnotherSessionPage gotoAddAnotherSessionPage() {
        this.addAnotherSession.click();
        this.logger.logWithScreenShot("Navigating to addAnotherSession Page", this.driver);
        return new AddAnotherSessionPage(this.driver);
    }


    /**
     * Gets list of webinar keys for a recurring webinar.
     * @return listOfWebinargKeys list of the webinar key in the recurring webinar series
     */
    public List<Long> getListOfWebinarKeysForRecurringWebinar() {
        List<Long> listOfWebinarKeys = new ArrayList<Long>();
        List<WebElement> elements = this.driver.findElements(this.webinarKeysElement);
        for (WebElement element : elements) {
            String id = element.getAttribute("id");
            Pattern webinarKeyP = Pattern.compile("[0-9]+");
            Matcher m = webinarKeyP.matcher(id);
            if (m.find()) {
                id = m.group();
            }
            listOfWebinarKeys.add(Long.parseLong(id));
        }
        this.logger.log("The list of webinar keys for a recurring webinar are : " + listOfWebinarKeys);
        return listOfWebinarKeys;
    }
    
    public String getEditSurveyUrl() {
        return editSurvey.getAttribute("href");
    }
    
    /**
     * Gets a webinarID of a specific instance in a recurring webinar series.
     * @param sessionSequence
     * @return webinarID formatted webinar id from the page
     */
    public String getSpecificWebinarIDForRecurringWebinar(int sessionSequence){
    	WebElement webinarIDLinkElement  = this.driver.findElement(By.id("editDateTime_"+sessionSequence));
    	String webinarID = webinarIDLinkElement.getText();
    	this.logger.log("Selected webinar id : " + webinarID);
    	return webinarID;
    } 

    
    /**
     * Method to cancel webinar
     * 
     * @return MyWebinarPage object
     */
    public MyWebinarsPage cancelWebinar() {
        this.clickOnCancelWebinarLink();
        this.findClickableElement(this.cancelYes).click();
        this.logger.logWithScreenShot("After clicking confirm cancel webinar on light box:",
                this.driver);
        return new MyWebinarsPage(this.driver, false);
    }

    
    /**
     * Method to cancel webinar
     * 
     * @return MyWebinarPage object
     */
    public MyWebinarsPage cancelWebinar(boolean sendNotification) {
        this.clickOnCancelWebinarLink();
        this.findClickableElement(this.cancelYes).click();
        this.logger.logWithScreenShot("After clicking confirm cancel webinar on light box:",
                this.driver);
        if(sendNotification){
            this.cancellationNotice.click();
            this.logger.logWithScreenShot("After clicking on webinar cancellation Notice button",
                    this.driver);
        }
        return new MyWebinarsPage(this.driver, false);
    }
    /**
     * Method to click on cancel webinar link only
     * @return 
     */
    private void clickOnCancelWebinarLink() {
        this.findClickableElement(this.cancelWebinar).click();
        this.logger.logWithScreenShot("After clicking cancel webinar link:", this.driver);
    }

    public ReminderEmailPage clickReminderEmail(){
        this.editReminder.click();
        return new ReminderEmailPage(this.driver);
    }
    public MyWebinarsPage getMyWebinarsPage()
    {
    	myWebinarsPage.click();
		return new MyWebinarsPage(this.driver);

		
    }
    
  
}
