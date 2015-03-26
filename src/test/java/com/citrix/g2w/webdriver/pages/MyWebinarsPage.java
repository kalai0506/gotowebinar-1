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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**

 */
public class MyWebinarsPage extends BasePage {

    /**
     * Web element used to get cancel webinar banner message.
     */
    @FindBy(xpath = "//div[@class='banner-content']//p")
    private WebElement cancelWebinarBannerMsg;
    /**
     * Instance variable used to get list of my webinar details.
     */
    private final String myWebinarDetailsList = "//div[@id='upcomingwebinar']//ul/li[position()<=4]";
    /**
     * Web element to find Past webinars Section.
     */
    @FindBy(xpath = "//a[@id='ui-id-2']")
    private WebElement pastWebinarsSection;
    /**
     * Web element for schedule a webinar button.
     */
    @FindBy(id = "scheduleWebinar")
    private WebElement scheduleAWebinarButton;
    /**
     * By element for whats new close button locator.
     */
    private final By whatsNewCloseButtonLocator = By.id("close-whats-new");

    /**
     * Web element for registrant count
     */
    @FindBy(xpath = "//div[@id='upcomingWebinar']//ul/li[@class='column-3']")
    private  WebElement registrantCount;

    /** saraswathi
     * webelement for copied webnar name
     */
    @FindBy(id ="webName")
    private WebElement copiedWebnarName;
    /*
     * web element for mywebinarspage link
     */
  
    /**
     * Return the registrant count as a string
     */
   public void verifyCopiedWebnarExistance(String webnarName)
   {
	   System.out.println("test existance of copied webnar name");
	   Assert.assertEquals(copiedWebnarName.getText(),webnarName);
	   System.out.println("copied webnar is existing");
	   
   }

    public String getRegistrantCount() {
        return this.registrantCount.getText().trim();
    }

    /**
     * initialize constructor with My webinar's page URL and web driver object.
     * @param mywebinarsPageURL
     *            (My webinar's page URL)
     * @param webDriver
     *            (web driver object)
     */
    public MyWebinarsPage(final String myWebinarsPageURL, final WebDriver webDriver) {
        this.driver = webDriver;
        this.driver.get(myWebinarsPageURL);
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/webinars.tmpl"));
        // Close What's New dialog if exists
        try {
            this.findClickableElement(this.whatsNewCloseButtonLocator).click();
        } catch (Exception e) {
            // What's New dialog not found. Ignore the error
        }
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Constructor to initialize web driver object and verify the page URL.
     * @param webDriver
     *            (web driver)
     *//*
    public MyWebinarsPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("webinars.tmpl"));
        // Close What's New dialog if exists
        //TODO g2w does not have what's new
       
        PageFactory.initElements(this.driver, this);
    }
*/
    /**
     * Constructor to initialize web driver object and verify the page URL.
     * @param webDriver
     *            (web driver)
     * @param isWahatsNewCloseButtonPresent
     */
    public MyWebinarsPage(final WebDriver webDriver, final boolean isWahatsNewCloseButtonPresent) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("webinars.tmpl"));
        // Close What's New dialog if exists
        if (isWahatsNewCloseButtonPresent) {
            try {
                this.findClickableElement(this.whatsNewCloseButtonLocator).click();
            } catch (Exception e) {
                // What's New dialog not found. Ignore the error
            }
        }
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Method to get cancel webinar banner message.
     * @return cancelwebinarMsg
     */
    public String getCancelWebinarBannerMessage() {
        String cancelWebinarMsg = this.cancelWebinarBannerMsg.getText();
        this.logger.log("cancel webinar banner message: " + cancelWebinarMsg);
        return cancelWebinarMsg;
    }

    /**
     * Method to get list of my webinar details.
     * 
     * @return mainList
     */
    public List<List<String>> getListOfMyWebinarDetails(){
        List<List<String>> mainList = new ArrayList<List<String>>();
        List<String> subList = new ArrayList<String>();
        List<String> listOfElements = this
                .getListOfTitlesForElementsWithGivenXPath(this.myWebinarDetailsList);
        int i = 1;
        for (String elements : listOfElements) {
            subList.add(elements.replaceAll("[\\t\\n\\r]", " "));
            if (i == 3) {
                mainList.add(subList);
                subList = new ArrayList<String>();
                i = 0;
            }
            i++;
        }
        return mainList;
    }

    /**
     * Method to go to manage webinar page based on webinar name.
     * 
     * @param webinarName
     *            (go to manage webinar page based on the name)
     * @return (manage webinar object based on name)
     */
    public ManageWebinarPage goToManageWebinarPageOnName(final String webinarName) {
        try {
            if (!(webinarName == null || webinarName.equalsIgnoreCase(""))) {
                this.driver.findElement(By.linkText(webinarName)).click();
            }
        } catch (Exception e) {
            this.logger.log("Webinar name should not be null or invalid Webinar Name: "
                    + e.getStackTrace());
        }
        this.logger.logWithScreenShot(
                "Navigating to Manage Webinar Page based on the Webinar name: " + webinarName,
                this.driver);
        return new ManageWebinarPage(this.driver);
    }

    /**
     * Method to go to manage Webinar page based on Webinar key.
     * @param webinarKey
     *            (webinar key)
     * @return ManageWebinarPage
     */
    public ManageWebinarPage goToManageWebinarPageOnWebinarKey(final Long webinarKey) {
        try {
            if (webinarKey != null) {
                String xpath = "//a[contains(@href,'webinar=" + webinarKey + "')]";
                this.findClickableElement(By.xpath(xpath)).click();
            }
        } catch (Exception e) {
            this.logger.log("webinar name should not be null or invalid webinar Name: "
                    + e.getStackTrace());
        }
        this.logger.logWithScreenShot(
                "Navigating to Manage webinar Page based on the webinar key : " + webinarKey,
                this.driver);
        return new ManageWebinarPage(this.driver);
    }

    /**
     * Method to go to Past webinar section.
     * @return (manage webinar object)
     */
    public MyWebinarsPage goToPastWebinars() {

        try {
            this.pastWebinarsSection.click();
        } catch (Exception e) {
            // Past webinar page not found. Ignore the error
            this.logger.log("past webinar section not found: " + e.getStackTrace());
        }
        this.logger.logWithScreenShot("Navigating to past webinar section", this.driver);
        return new MyWebinarsPage(this.driver);
    }

    /**
     * Method to click on Schedule a webinar button and go to schedule webinar
     * page.
     * @return ScheduleAwebinarPage (schedule webinar page object)
     */
    public ScheduleAWebinarPage gotoScheduleAWebinarPage() {
        this.scheduleAWebinarButton.click();
        this.logger.logWithScreenShot("Navigating to schedule a webinar page", this.driver);
        return new ScheduleAWebinarPage(this.driver);
    }
       /**@author saraswathi.venkatreddy Modifed
	    * Constructor to initialize web driver object and verify the page URL.
	    * @param webDriver
	    *            (web driver)
	    */
	   public MyWebinarsPage(final WebDriver webDriver) 
	   {
	       this.driver = webDriver;
	       Assert.assertTrue(this.driver.getCurrentUrl().contains("webinars.tmpl"));
	       // Close What's New dialog if exists
	       //TODO g2w does not have what's new
	       // Close Accept New dialog if exists
	       try {
	           this.findClickableElement(this.acceptButtonLocator).click();
	       } catch (Exception e) {
	           // AceptNew dialog not found. Ignore the error
	       }
	       PageFactory.initElements(this.driver, this);
	   }
	
		   private final By acceptButtonLocator = By.id("accept");

}
