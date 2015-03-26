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
package com.citrix.g2w.webdriver.pages.audio;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;


public class AllConferenceCallNumbersPage extends BasePage {

    /**
     * instance variable used to get conference call number list.
     */
    private final String confCallNumbersList = "//div[@id='confCallNumbersList']//ul/li";
    
    /**
     * Url for theme
     */
     private By themeUrlBy = By.xpath("//link[contains(@href, 'theme')]");

    /**
     * Constructor to initialize web driver object and verify current page URL.
     * 
     * @param allConfCallNumbersPageUrl
     *            (all conference call numbers page url)
     * @param webDriver
     *            (object to initialize)
     */
    public AllConferenceCallNumbersPage(final String allConfCallNumbersPageUrl,
            final WebDriver webDriver) {
        this.driver = webDriver;
        this.driver.get(allConfCallNumbersPageUrl);
       
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/audio") || this.driver.getCurrentUrl().contains("/paudio"));
        PageFactory.initElements(this.driver, this);
        this.logger.logWithScreenShot("All Conference Call numbers Page", this.driver);
    }

    /**
     * Method to get list of all conference call details.
     * 
     * @return mainList
     */
    public List<List<String>> getListOfAllConferenceCallDetails() {
        List<List<String>> mainList = new ArrayList<List<String>>();
        List<String> subList = new ArrayList<String>();
        List<String> listOfElements = this
                .getListOfTitlesForElementsWithGivenXPath(this.confCallNumbersList);
        int i = 1;
        for (String elements : listOfElements) {
            subList.add(elements);
            if (i == 4) {
                mainList.add(subList);
                subList = new ArrayList<String>();
                i = 0;
            }
            i++;
        }
        return mainList;
    }
    
    public String getThemeUrl(){
        WebElement themeUrlElement = driver.findElement(themeUrlBy);
         if(themeUrlElement != null){
             this.logger.log("ThemeUrlElement" + themeUrlElement);
             return themeUrlElement.getAttribute("href");
         }
          return null;
            
    }

}
