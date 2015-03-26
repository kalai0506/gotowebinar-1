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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * author saraswathi.venkatreddy
* webelement for copywebnar.
 */
public class ScheduleSimilarWebinarPage extends BasePage {

        @FindBy(id = "select2-drop-mask")
    private WebElement copyWebnar;
    /**
     * Web element for webinar title(name).
     */
    @FindBy(id = "name")
    private WebElement webinarName;
    /**
     * Web element for webinar description.
     */
    @FindBy(id = "description")
    private WebElement webinarDescription;
    /** 
     * web element for schedule button
     */
    
    @FindBy(id = "schedule.submit.button")
    		
    private WebElement schedule;		
    @FindBy(name= "name")
    private WebElement copiedName;
    
    

    /**
     * Overloaded Constructor to initialize web driver object, invoke schedule
     * webinar page and verify current page URL.
     * 
     * @param scheduleWebinarPageUrl
     *            (schedule webinar page url)
     * @param webDriver
     *            (web driver)
     */

    public ScheduleSimilarWebinarPage(final String scheduleWebinarPageUrl, final WebDriver webDriver) {
        this.driver = webDriver;
        this.driver.get(scheduleWebinarPageUrl);
        Assert.assertTrue(webDriver.getCurrentUrl().contains("/copy.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Constructor to initialize web driver object and verify current page URL.
     * 
     * @param webDriver
     *            (web driver)
     */

    public ScheduleSimilarWebinarPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(webDriver.getCurrentUrl().contains("copy.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Sets the base starting date
     * @param baseDate DateTime
     */
    private void setBaseDate(final DateTime baseDate) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(this.messages.getMessage(
                "date.format.mask.dayInWeekMonthDayYear", null, this.locale));
        ((JavascriptExecutor) this.driver)
                .executeScript("document.getElementById('webinarTimesForm.dateTimes_0.baseDate').value = '"
                        + fmt.withLocale(this.locale).print(baseDate) + "'");
    }

    /**
     * Method to schedule webinar with name and description.
     *
     * @param name
     *            (webinar name)
     * @param description
     *            (webinar description)
     * @return (manage webinar object)
     */
    public ManageWebinarPage scheduleWebinar(final String name, final String description) {
        this.logger.log("Scheduling webinar with name: " + name + " and description: " + description);
        setWebinarName(name);
        setWebinarDescription(description);
        this.schedule.click();
        this.logger.logWithScreenShot("Scheduled webinar with name: " + name, this.driver);

        return new ManageWebinarPage(this.driver);
    }
public ManageWebinarPage getScheduleSimilarWebnarEditTitleAndDescription(final String name, final String description)
{
	this.logger.log("Scheduling webinar with name: " + name + " and description: " + description);
    setWebinarName(name);
    setWebinarDescription(description);
	verifyWebnarName(name);


    verifyWebnarDescription(description);
    System.out.println("name and description can be edited");

    this.schedule.click();
    this.logger.logWithScreenShot("Scheduled webinar with name: " + name, this.driver);

    return new ManageWebinarPage(this.driver);

}

    /**
     * Sets the webinar description
     * @param description String
     */
    private void setWebinarDescription(final String description) {
        if (!(description == null || description.equalsIgnoreCase(""))) {
        	webinarDescription.clear();
            this.webinarDescription.sendKeys(description);
        }
    }

    /**
     * Sets the webinar name
     * @param name String
     */
    private void setWebinarName(final String name) {
    	webinarName.clear();
        this.webinarName.sendKeys(name);
    }
    /**
     * method to verify webnar can be edited
     * @throws  
     */
    private void verifyWebnarName(String name)
    {
    	//String Expected="Webtest Webinar-1329959235-76418547 name can be edited after copy";
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String webnarName=webinarName.getAttribute("value");
    	System.out.println("webinar name====="+webnarName);
    	Assert.assertEquals(webnarName,name);
    	
    }
    private void verifyWebnarDescription(String description) {

    	
    	String webnarDescription=webinarDescription.getAttribute("value");
    	System.out.println("webinar desc====="+webnarDescription);

    	Assert.assertEquals(webnarDescription,description);
    	System.out.println("desc successfully verified");
    	
    
    	
		
	}
    
    
}
