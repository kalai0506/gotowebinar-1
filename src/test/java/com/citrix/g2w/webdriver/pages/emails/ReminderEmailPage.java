package com.citrix.g2w.webdriver.pages.emails;

import junit.framework.Assert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;

public class ReminderEmailPage extends BasePage {

    /**
     * Web element set to oneHourReminder.
     */
    @FindBy(id = "timePeriodCheckBox-0")
    WebElement oneHourReminder;

    /**
     * Web element set to oneHourReminder.
     */
    @FindBy(id = "timePeriodCheckBox-1")
    WebElement oneDayReminder;

    /**
     * Web element set to oneHourReminder.
     */
    @FindBy(id = "timePeriodCheckBox-2")
    WebElement oneWeekReminder;

    /**
     * Webelement for Reminder Email Save Button.
     */
    @FindBy(id = "email.settings.button.save")
    private WebElement emailSaveButton;

    /**
     * Webelement for Reminder Email Save Button.
     */
    @FindBy(id = "message")
    private WebElement emailMessage;

    /**
     * Webelement for email Subject.
     */
    @FindBy(id = "subject")
    private WebElement emailSubject;

    /**
     * Webelement for notifyTrainer checkbox.
     */
    @FindBy(id = "notifyTrainer")
    private WebElement notifyTrainer;

    /**
     * Constructor to initialize web driver object and verify the page URL.
     * 
     * @param webDriver
     *            (web driver)
     */
    public ReminderEmailPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(this.driver.getCurrentUrl().contains("reminder.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    public ManageWebinarPage setReminderEmail(EmailDuration reminder) {
        
        switch (reminder) {
        case ONE_HOUR:
            if (this.oneHourReminder.isDisplayed()
                    && (!(this.oneHourReminder.isSelected()))) {
                this.oneHourReminder.click();
            }
            break;
        case ONE_DAY:
            if (this.oneDayReminder.isDisplayed()
                    && (!(this.oneDayReminder.isSelected()))) {
                this.oneDayReminder.click();
                break;
            }
        case ONE_WEEK:
            if (this.oneWeekReminder.isDisplayed()
                    && (!(this.oneWeekReminder.isSelected()))) {
                this.oneWeekReminder.click();
            }
            break;
        }
        this.emailSaveButton.click();
        this.logger.logWithScreenShot("After click on Save", this.driver);
        return new ManageWebinarPage(this.driver);
    }

}
