package com.citrix.g2w.webdriver.pages.webinar;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;



public class AddAnotherSessionPage extends BasePage {

            
    @FindBy(id = "addAnotherSession.button.save")
    private WebElement submit;
    
    /**
     * Constructor to initialize web driver object and verify current page URL.
     * 
     * @param webDriver
     *            (web driver)
     */

    public AddAnotherSessionPage (final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(webDriver.getCurrentUrl().contains("add.tmpl"));
        PageFactory.initElements(this.driver, this);
    }
    
    public ManageWebinarPage addAnotherSession(){
        this.submit.click();
        this.logger.logWithScreenShot("Scheduled another session " , this.driver);

        return new ManageWebinarPage(this.driver);
    }

}
