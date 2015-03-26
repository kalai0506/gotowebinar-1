package com.citrix.g2w.webdriver.pages.managewebinar;

import junit.framework.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
/**
 * 
 * @author kapilmahalavat
 *
 */
public class PanelistPage extends BasePage{
    /**
     * Web Element for Panelist Name.
     */
    @FindBy(id = "newPanelists_0.name")
    private WebElement panelistName;
    /**
     * Web Element for Panelinst Email.
     */
    @FindBy(id = "newPanelists_0.email")
    private WebElement panelistEmail;
    /**
     * Web Element use to save Panelist Info.
     */
    @FindBy(id = "panelist.save")
    private WebElement savePanelist;
    
    
    /**
     * Constructor to initialize web driver object and verify the page URL.
     * 
     * @param webDriver
     *            (web driver)
     */
    public PanelistPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl()
                .contains("/panelist/edit.tmpl"));
        PageFactory.initElements(this.driver, this);
    }
    
    public ManageWebinarPage addPanelist(String panelistName,String panelistemailAddress) {
        this.panelistName.sendKeys(panelistName);
        this.panelistEmail.sendKeys(panelistemailAddress);
        this.savePanelist.click();
        return new ManageWebinarPage(this.driver);
    }
}
