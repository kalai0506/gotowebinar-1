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
package com.citrix.g2w.webdriver.pages.branding.settings;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;



public class ManageBrandingSettingsPage extends BasePage {

   
    /**
     * Web element for preview.
     */
    @FindBy(css = ".preview")
    private WebElement preview;
    /**
     * Web element for save.
     */
    @FindBy(id = "brandingForms.submit")
    private WebElement save;
    
    /**
     * To select theme dropdown
     */
    private final By themeDropDown = By.xpath("//select['@name=theme.themeType']");
    
    @FindBy(xpath="//a[@class='remove' and @href='#deleteLogo']")
    private WebElement deleteLogo;
    
    
    @FindBy(xpath="//a[@class='remove' and @href='#delete-image']")
    private WebElement deleteCustomImage;
  

    /**
     * Constructor to initialize instance variables and verify the current page url.
     * 
     * @param webDriver (web driver)
     */
    public ManageBrandingSettingsPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(webDriver.getCurrentUrl().contains("branding/manage.tmpl"));
        PageFactory.initElements(this.driver, this);
        this.logger.logWithScreenShot("Branding settings page", this.driver);
    }

  
    /**
     * Method to click on preview.
     * 
     * @return BrandingPreviewPage
     */
    public BrandingPreviewPage clickOnPreview() {
        this.preview.click();
        this.logger.logWithScreenShot("After click on Preview", this.driver);
        return new BrandingPreviewPage(this.driver);
    }

    /**
     * Method to click on save.
     * 
     * @return BrandingSettingsPage
     */
    public ManageBrandingSettingsPage clickOnSave() {
        this.save.click();
        this.logger.logWithScreenShot("After click on Save", this.driver);
        return new ManageBrandingSettingsPage(this.driver);
    }
    
    public void selectBrandingTheme(String themeName){
        WebElement elementToSelect = driver.findElement(themeDropDown);
        //making drop down visible
        ((JavascriptExecutor) this.driver).executeScript("arguments[0].style.display='block';", elementToSelect);
        Select select = new Select(elementToSelect);  
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", elementToSelect);
        this.logger.logWithScreenShot("After click on dropdown", this.driver);
        select.selectByVisibleText(themeName);
        clickOnSave();
    }
    
    
    /**
     * Method used to upload logo.
     * 
     * @param logoFileName (logo file name)
     */
    public void uploadLogo(final String logoFileName) {   
        String fileToUpload = this.getAbsoluteFilePath("Logo" + File.separator + logoFileName);
        this.logger.log("location of the logoFile  :" + fileToUpload);
        this.findPresenceOfElement(By.id("logoImage"), this.DEFAULT_TIMEOUT).sendKeys(
                fileToUpload);
        clickOnSave();
    }
    
    public void uploadCustomThemeImage(final String customThemeImageFileName) {   
        String fileToUpload = this.getAbsoluteFilePath("Logo" + File.separator + customThemeImageFileName);
        this.logger.log("location of the themeImageFile :" + fileToUpload);
        this.findPresenceOfElement(By.id("customThemeImage"), this.DEFAULT_TIMEOUT).sendKeys(
                fileToUpload);
        clickOnSave();
    }
    
    
    public void deleteLogo() {
        deleteLogo.click();
        this.logger.logWithScreenShot("After deleting logo", this.driver);
    }
    
    public void deleteCustomImage() {
        deleteCustomImage.click();
        this.logger.logWithScreenShot("After deleting logo", this.driver);
    }
}
