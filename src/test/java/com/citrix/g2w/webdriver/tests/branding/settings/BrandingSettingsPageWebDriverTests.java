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
package com.citrix.g2w.webdriver.tests.branding.settings;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.ScheduleAWebinarPage;
import com.citrix.g2w.webdriver.pages.SettingsPage;
import com.citrix.g2w.webdriver.pages.audio.AllConferenceCallNumbersPage;
import com.citrix.g2w.webdriver.pages.branding.settings.ManageBrandingSettingsPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationConfirmationPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.pages.webinar.AddAnotherSessionPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

public class BrandingSettingsPageWebDriverTests extends BaseWebDriverTest {
    
    /**
     * instance variable for attendee url.
     */
    @Value("${attendee.Url}")
    private String attendeeUrl ;

    @DataProvider(name = "themeData")
    public static Object[][] themeData() 
    {
        return new Object[][] 
        	 {
                { "Coal", "background-color:#636363;", "background: linear-gradient(#636363, #393939) fixed;" ,"border-color: #393939;", "#registration h1, #registrationConfirmation h1, #confCallNumbers h1 {color:#393939;}"},
                { "Forest", "background-color:#6B9570;", "background: linear-gradient(#6B9570, #49694F) fixed;" ,"border-color: #42632F;", "#registration h1, #registrationConfirmation h1, #confCallNumbers h1 {color:#41632E;}"},
                { "Ruby", "background-color:#652b2b;", "background: linear-gradient(#652b2b, #3e2727) fixed;" ,"border-color: #400000;", "#registration h1, #registrationConfirmation h1, #confCallNumbers h1 {color:#3d2524;}" },
                { "Ocean", "background-color:#5b7eaf;", "background: linear-gradient(#5b7eaf, #314769) fixed;" ,"border-color: #314868;", "#registration h1, #registrationConfirmation h1, #confCallNumbers h1 {color:#314668;}" },
                { "Custom", "background-color:#eeeee7;", "background: linear-gradient(#eeeee7, #eeeee7) fixed;" ,"border-color: #cccccc;", "#registration h1, #registrationConfirmation h1, #confCallNumbers h1 {color:#114c7f;}"}
               
               };
      }
    
    /**
     * Test to Verify When  Organizer Sets Custom Theme  then The Branding
     * Reflect The Custom theme.
     * <ol>
     * <li>Create personal account, login , schedule a webinar.</li>
     * <li>Go to branding settings page .</li>
     * <li>Select theme colors and click on "Save".</li>
     * <li>Verify custom branding colors on Registration, confirmation pages .</li>
     * </ol>
     */

    @Test(groups = {Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.BRANDING, Groups.BRANDING_THEME }, dataProvider = "themeData")
    public void verifyWhenOrganizerSelectsThemeRegistrationPagesHaveThemes(String themeName, String backGroundColor,String linearGradient, String borderColor, String headerFontColor) 
    {

        this.logger.log("Verify When Personal Organizer Clicks On The Default Page Colors"  + "The Branding Reverts To Default Colors");

        // Create personal account, login and go to settings page
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String registrationUrl = manageWebinarPage.getRegistrationURL();

        // Go to branding settings page
        ManageBrandingSettingsPage brandingSettingsPage = manageWebinarPage.goToManageBrandingSettingsPage();

        // Set branding colors and click on theme
        this.logger.log("Select branding theme " + themeName);
        brandingSettingsPage.selectBrandingTheme(themeName);

        this.logger.log("Registration Url" + registrationUrl);
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.getWebDriver());
        this.logger.log("theme URL" + registrationPage.getThemeUrl());
        this.webDriver.get(registrationPage.getThemeUrl());
        verifyThemeColors(backGroundColor, linearGradient, borderColor, headerFontColor);
        registrationPage = new RegistrationPage(registrationUrl, this.getWebDriver());
        RegistrantDetails registrantDetails = new RegistrantDetails();
        RegistrationConfirmationPage registrationConfirmationPage = registrationPage.registerAttendeeDetailsWithConfirmation(registrantDetails);
        this.webDriver.get(registrationConfirmationPage.getThemeUrl());
        verifyThemeColors(backGroundColor, linearGradient, borderColor, headerFontColor);
    }

    /**
     * Test to Verify When  Organizer Sets default Theme  then no theme gets applied
     * <ol>
     * <li>Create personal account, login , schedule a webinar.</li>
     * <li>Verify no theme on Registration, confirmation pages .</li>
     * </ol>
     */

    @Test(groups = {Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.BRANDING, Groups.BRANDING_THEME })
    public void verifyWhenOrganizerSelectsDefaultThemeRegistrationPagesDontHaveThemes() 
    {

        this.logger.log("Verify When Personal Organizer Clicks On The Default Page Colors " + "The Branding Reverts To Default Colors");

        // Create personal account, login and go to settings page
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String registrationUrl = manageWebinarPage.getRegistrationURL();

        this.logger.log("Registration Url" + registrationUrl);
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.getWebDriver());
        Assert.assertNull(registrationPage.getThemeUrl());
    }

    /**
     * Test to Verify When  Organizer Uploads logo and customImage,   they  show up in
     * Registration and Conformation page
     * <ol>
     * <li>Create personal account, login , schedule a webinar.</li>
     * <li>Go to branding settings page .</li>
     * <li>Upload logo and custom theme image, save.</li>
     * <li>Verify images  shows on Registration, confirmation pages .</li>
     * </ol>
     */
   
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.BRANDING, Groups.BRANDING_THEME })
    public void verifyLogoComesUpInRegistrationAndConfirmationPage()
    {

        this.logger.log("Verify When Personal Organizer Sets Custom Background Colors " + "Then The Branding Reflect The Custom Colors");

        // Create personal account, login and go to settings page
        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        String manageWebinarPageUrl =  this.getWebDriver().getCurrentUrl();

        String registrationUrl = manageWebinarPage.getRegistrationURL();

        // Go to branding settings page
        ManageBrandingSettingsPage brandingSettingsPage = manageWebinarPage.goToManageBrandingSettingsPage();

        uploadLogoAndCustomImage(brandingSettingsPage);
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.getWebDriver());
        verifyLogoAndCustomImageInRegistrationPage();
        RegistrantDetails registrantDetails = new RegistrantDetails();
        registrationPage.registerAttendeeDetailsWithConfirmation(registrantDetails);
        verifyLogoInConfirmationPage();
        deleteLogoAndCustomImage(manageWebinarPageUrl);

    }

   
    
    /**
     * Test to verify themes in audio all conference numbers page
     * reflect the selected theme.
     * <ol>
     * <li>Create corp account, login ,update audio settings to allow different country calls, schedule a webinar.</li>
     * <li>Go to branding settings page .</li>
     * <li>Select theme colors and click on "Save".</li>
     * <li> Register for webinar and get conference call url from registration Confirmation page </li>
     * <li> Go to conference call page and verify theme </li>
     * <li> Go to panelist conference call page and verify theme </li> 
     * <li> .</li>
     * </ol>
     */
     
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.EMAILS, Groups.AUDIO_SETTINGS, Groups.BRANDING_THEME},   dataProvider = "themeData")
    public void verifyThemesinAllConferenceNumbersPage(String themeName, String backGroundColor,String linearGradient, String borderColor, String headerFontColor) 
    {

        MyWebinarsPage myWebinarsPage = this.createAccountLoginAndGoToMyWebinar(Groups.CORPORATE);

        // Navigate to settings page.
        SettingsPage settingsPage = myWebinarsPage.gotoSettingsPage();

        // Get the country name from the properties file.
        List<String> countryNames = new ArrayList<String>();
        countryNames.add(this.messages.getMessage("countryEnum.gb", null, this.locale));
        countryNames.add(this.messages.getMessage("countryEnum.us", null, this.locale));

        // Update the settings in settings page.
        settingsPage.updateAudioSettings(true, true, countryNames, false, null);

        // Save settings.
        settingsPage = settingsPage.saveSettings();

        ManageWebinarPage manageWebinarPage = this.scheduleWebinar(myWebinarsPage);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        

        // Go to branding settings page
        ManageBrandingSettingsPage brandingSettingsPage = manageWebinarPage.goToManageBrandingSettingsPage();

        // Set branding colors and click on theme
        this.logger.log("Select branding theme " + themeName);
        brandingSettingsPage.selectBrandingTheme(themeName);
        String attendeeAudioURL = attendeeUrl + "/audio/" + webinarKey;        
        this.logger.log("audio url" +  attendeeAudioURL);
      //Go To panelist all conference numbers page
        AllConferenceCallNumbersPage attendeeConferencePage = new AllConferenceCallNumbersPage(attendeeAudioURL,this.getWebDriver());
        getWebDriver().navigate().to(attendeeConferencePage.getThemeUrl());
        this.logger.logWithScreenShot("Navigating to Audio Page", getWebDriver());
        this.logger.log("audio url" +  attendeeAudioURL);
        verifyThemeColors(backGroundColor, linearGradient, borderColor, headerFontColor);
        
        String panelistAudioPageUrl = attendeeUrl + "/paudio/" + webinarKey;
      //Go To panelist all conference numbers page
        AllConferenceCallNumbersPage panelistConferencePage = new AllConferenceCallNumbersPage(panelistAudioPageUrl, this.getWebDriver());
        getWebDriver().navigate().to(panelistConferencePage.getThemeUrl());
        this.logger.logWithScreenShot("Navigating to Audio Page", getWebDriver());
        verifyThemeColors(backGroundColor, linearGradient, borderColor, headerFontColor);
      
    }
    
    /**
     * Test to verify themes for a  series webinar 
     * 
     * <ol>
     * <li>Create  account, login  schedule a series webinar.</li>
     * <li> Add another session to series webinar </li>
     * <li>Go to branding settings page .</li>
     * <li>Select theme  and click on "Save".</li>
     * <li> upload logo and image </li>
     * <li> Register for last added webinar in the series  </li>
     * <li> Verify Logo, theme in registration page and confirmation page </li> 
     * <li> .</li>
     * </ol>
     */
    
    @Test(groups = { Groups.ATTENDEE_APP, Groups.PERSONAL, Groups.EMAILS, Groups.AUDIO_SETTINGS, Groups.BRANDING_THEME, Groups.SERIES},   dataProvider = "themeData")
    public void verifyThemesForSeriesWebinar(String themeName, String backGroundColor,String linearGradient, String borderColor, String headerFontColor)
    {
        long duration = 3600000;
        DateTime startDate = new DateTime(DateTimeZone.forID("America/Los_Angeles")).plusWeeks(3);
        DateTime endDate = startDate.plusDays(1);
        DateTime endTime = endDate.plus(duration);

        ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL,ScheduleAWebinarPage.Frequency.DAILY, ScheduleAWebinarPage.WebinarType.SERIES, startDate, endDate, endTime);
        String manageWebinarPageUrl =  this.getWebDriver().getCurrentUrl();
        AddAnotherSessionPage addAnotherSession =  manageWebinarPage.gotoAddAnotherSessionPage();
        manageWebinarPage  = addAnotherSession.addAnotherSession();
        String registrationUrl = manageWebinarPage.getRegistrationURL();
      
        // Go to branding settings page
        ManageBrandingSettingsPage brandingSettingsPage = manageWebinarPage.goToManageBrandingSettingsPage();
      
        // Set branding colors and click on theme
        this.logger.log("Select branding theme " + themeName);
        brandingSettingsPage.selectBrandingTheme(themeName);
        uploadLogoAndCustomImage(brandingSettingsPage);
       
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(registrationUrl, this.getWebDriver());
        verifyLogoAndCustomImageInRegistrationPage();
        String registrationPageThemeUrl = registrationPage.getThemeUrl();
        RegistrantDetails registrantDetails = new RegistrantDetails();
        // Register for the newly added webinar session
        RegistrationConfirmationPage confirmationPage = registrationPage.registerForSeriesWebinarWithConfirmation(registrantDetails, 2);
        String confirmationPageThemeUrl = confirmationPage.getThemeUrl();
        verifyLogoInConfirmationPage();
        this.webDriver.get(registrationPageThemeUrl);
        verifyThemeColors(backGroundColor, linearGradient, borderColor, headerFontColor);
        this.webDriver.get(confirmationPageThemeUrl);
        verifyThemeColors(backGroundColor, linearGradient, borderColor, headerFontColor);
        deleteLogoAndCustomImage(manageWebinarPageUrl);
     
     }
    
    private void deleteLogoAndCustomImage(String manageWebinarPageUrl)
    {
        ManageWebinarPage manageWebinarPage;
        ManageBrandingSettingsPage brandingSettingsPage;
        this.webDriver.get(manageWebinarPageUrl);
        manageWebinarPage = new ManageWebinarPage(this.webDriver);
        brandingSettingsPage = manageWebinarPage.goToManageBrandingSettingsPage();
        brandingSettingsPage.deleteLogo();
        brandingSettingsPage.deleteCustomImage();
    }
    
    private void verifyLogoInConfirmationPage()
    {
        String confirmationPageLogoXpath = "//img[@class='img-responsive']";        
        Assert.assertNotNull(this.webDriver.findElement(By.xpath(confirmationPageLogoXpath)));
    }

    private void verifyLogoAndCustomImageInRegistrationPage() 
    {       
        String logoXpath = "//img[@class='img-responsive' and @alt='" + this.webinarName + "']";
        Assert.assertNotNull(this.webDriver.findElement(By.xpath(logoXpath)));
        String customThemeImageXpath = "//img[@class='customImage' and @alt='" + this.webinarName + "']";
        Assert.assertNotNull(this.webDriver.findElement(By.xpath(customThemeImageXpath)));
       
    }

    private void uploadLogoAndCustomImage(ManageBrandingSettingsPage brandingSettingsPage)
    {
        // Branding header logo name
        String logoImageName = "400x200_logo.jpg";
        String customThemeImageName = "200x200_customImage.jpg";

        // Upload the branding header logo
        brandingSettingsPage.uploadLogo(logoImageName);
        // Upload the custom theme image
        brandingSettingsPage.uploadCustomThemeImage(customThemeImageName);
    }

    private void verifyThemeColors(String backGroundColor, String linearGradient, String borderColor,String headerFontColor) 
    {
        Assert.assertTrue(this.webDriver.getPageSource().contains(backGroundColor),"Expected Value for backGroundColor " + backGroundColor);
        Assert.assertTrue(this.webDriver.getPageSource().contains(linearGradient),"Expected Value for linearGradient  " + linearGradient);
        Assert.assertTrue(this.webDriver.getPageSource().contains(borderColor), "Expected Value for borderColor " + borderColor);
        Assert.assertTrue(this.webDriver.getPageSource().contains(headerFontColor),"Expected Value for headerFontColor " + headerFontColor);
    }

}
