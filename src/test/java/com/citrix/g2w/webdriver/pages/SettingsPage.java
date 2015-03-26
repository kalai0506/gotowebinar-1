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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;



public class SettingsPage extends BasePage {

    /**
     * instance variable used to get all country names.
     */
    private final String countryNamesXpath = "//div[@class='table-scroll']//ul//li[2]";
    /**
     * WebElement used to select the participants.
     */
    @FindBy(id = "modal-action-toll")
    private WebElement done;
    /**
     * Web element used to edit toll countries.
     */
    @FindBy(id = "editTollCountries")
    private WebElement editTollCountries;
   
    /**
     * Web element used to enable private audio.
     */
    @FindBy(id = "conferenceMode_PRIVATE")
    private WebElement privateAudio;
    /**
     * Web element used to enter private info.
     */
    @FindBy(id = "privateInfo")
    private WebElement privateInfo;
    /**
     * Web element used to save the audio settings.
     */
    @FindBy(id = "audio.settings.save")
    private WebElement saveSettings;
    /**
     * Web element used to enable toll.
     */
    @FindBy(id = "pstnSelected")
    private WebElement tollEnabled;
    /**
     * Web element used to enable voip.
     */
    @FindBy(id = "voipSelected")
    private WebElement voipSelected;

    /**
     * Constructor to initialize instance variables and verify if we are on the
     * Settings page.
     * 
     * @param webDriver
     *            (web driver)
     */
    public SettingsPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(webDriver.getCurrentUrl().contains("settings.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method to get all country names from light box.
     * 
     * @return allCountryNames
     */
    private List<String> getAllCountryNamesFromLightBox() {
        List<String> allCountryNames = this
                .getListOfTitlesForElementsWithGivenXPath(this.countryNamesXpath);
        return allCountryNames;
    }

  
    /**
     * Method to save settings.
     * 
     * @return SettingsPage
     */
    public SettingsPage saveSettings() {
        this.saveSettings.click();
        this.logger.logWithScreenShot("", this.driver);
        return this;
    }

    /**
     * Method to update the audio settings.
     * 
     * @param voip
     *            (select the voip checkbox)
     * @param toll
     *            (select the toll only)
     * @param tollCountries
     *            (toll countries)
     * @param privateAudio
     *            (select the private check box)
     * @param attendeePhoneNumber
     *            (Attendee phone number)
     */
    public void updateAudioSettings(final boolean voip, final boolean toll,
            final List<String> tollCountries, final boolean privateAudio,
            final String attendeePhoneNumber) {
        this.setCheckBox(this.voipSelected, voip);
        this.setCheckBox(this.tollEnabled, toll);

        if (tollCountries != null) {
            this.editTollCountries.click();
            List<String> allCountryNames = this.getAllCountryNamesFromLightBox();

            try {
                for (String countryName : allCountryNames) {
                    if (tollCountries.contains(countryName)) {
                        this.setCheckBox(
                                this.driver.findElement(By.xpath("//li/label[text()='"
                                        + countryName + "']/../..//input")), true);
                    } else {
                        this.setCheckBox(
                                this.driver.findElement(By.xpath("//li/label[text()='"
                                        + countryName + "']/../..//input")), false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.done.click();
            this.logger.logWithScreenShot("Select these countries: " + tollCountries, this.driver);
        }
        if (privateAudio && attendeePhoneNumber != null && attendeePhoneNumber.equals("")) {
            this.privateAudio.click();
            this.privateInfo.sendKeys(attendeePhoneNumber);
        }
        this.logger.logWithScreenShot("After updating audio settings", this.driver);
    }
}
