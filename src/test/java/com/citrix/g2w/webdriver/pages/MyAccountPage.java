package com.citrix.g2w.webdriver.pages;

import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MyAccountPage extends BasePage {

	/**
	 * Web element used to changePersonalInfo page link.
	 */
	@FindBy(xpath = "//a[@href='changePersonalInfo.tmpl']")
	WebElement changePersonalInfoLink;

	/**
	 * Webelement for User firstname.
	 */
	@FindBy(id = "user.firstName")
	private WebElement FirstName;

	/**
	 * Webelement for User lastname.
	 */
	@FindBy(id = "user.lastName")
	private WebElement LastName;

	/**
	 * Web element for PrimaryLanguage.
	 */
	@FindBy(xpath = "//a[@id='user-locale_trig']")
	private WebElement PrimaryLanguage;

	/**
	 * Web element for Timezone.
	 */
	@FindBy(id = "user-timezone_trig")
	private WebElement TimeZone;

	/**
	 * Web element for twitter.
	 */
	@FindBy(id = "user.twitter")
	private WebElement TwitterUsername;

	/**
	 * Web element for SaveChanges button.
	 */
	@FindBy(xpath = "//div[@class='btn-bg']/input")
	WebElement saveChangesButton;

	/**
	 * Constructor to initialize web driver object and verify the page URL.
	 * 
	 * @param webDriver
	 *            (web driver)
	 */
	public MyAccountPage(final WebDriver webDriver) {
		this.driver = webDriver;
		Assert.assertTrue(this.driver.getCurrentUrl()
		        .contains("myAccount.tmpl"));
		PageFactory.initElements(this.driver, this);
	}

	public MyAccountPage editPrimaryLanguage(String language) {
		this.changePersonalInfoLink.click();
		this.PrimaryLanguage.click();
		WebElement selectPrimaryLanguage = driver
		        .findElement(By
		                .xpath("//div[@id='user-locale__menu']/ul/li[contains(@title, language)]"));
		selectPrimaryLanguage.click();
		this.saveChangesButton.click();
		return new MyAccountPage(this.driver);
	}

}
