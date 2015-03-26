package com.citrix.g2w.webdriver.pages.managewebinar;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 * @author Venkatesh.Aarelly
 *
 */
public class EditTitleandDescriptionPage extends BasePage 
{
	/**
	 * Web element for webinarName.
	 */
	@FindBy(id = "webinarNameInfo.name")
	private WebElement webinarName;
	/**
	 * Web element for webinar description.
	 */
	@FindBy(id = "webinarNameInfo.description")
	private WebElement webinarDescription;

	/**
	 * Web element for save button.
	 */
	@FindBy(id = "editWebinar.button.save")
	private WebElement saveButton;

	/**
	 * Web element for trainingName.
	 */
	@FindBy(id = "trainingName")
	private WebElement trainingName;

	/**
	 * Web element for trainingDesc.
	 */
	@FindBy(id = "trainingDesc")
	private WebElement trainingDesc;

	/**
	 * Web element for trainingDesc.
	 */
	@FindBy(xpath = "//p[@class='inline-error']")
	private WebElement titleMissingError;
	
	
	
	/**
	 * Constructor to initialize web driver object and verify current page URL.
	 * 
	 * @param webDriver
	 *            (object to initialize)
	 */
	public EditTitleandDescriptionPage(WebDriver webDriver)
	{
		this.driver = webDriver;
		Assert.assertTrue(this.driver.getCurrentUrl().contains("edit.tmpl"));
		PageFactory.initElements(this.driver, this);

	}

	/**
	 * Method to edit the schedule webinar page.
	 * 
	 * @param webinarName
	 * @return (ManageWebinarSchedulePageSettings object)
	 */

	public void editWebinarDetials(String editWebinarName)
	{
		this.logger.log("Edit the Webinar Title and Description");
		this.webinarName.sendKeys(editWebinarName);
		this.saveButton.click();
		this.logger.logWithScreenShot("Nagivating to ManageWebinarPage",
				this.driver);
	}

	/**
	 * Get the training description 
	 * 
	 * @return trainingDescription of Webinar
	 */

	public String getWebinarDesc()
	{
		String trainingDesc = this.trainingDesc.getText();
		return trainingDesc;
	}

	
	/**
	 * Get the webinar Name
	 * 
	 * @return webinarName of  Webinar
	 */

	public String getWebinarName() 
	{
		String trainingDesc = this.webinarName.getText();
		return trainingDesc;
	}
	
	
	/**
	 * Get the training name
	 * 
	 * @return trainingName of  Webinar
	 */

	public String getTrainingName() 
	{
		String trainingDesc = this.trainingName.getText();
		return trainingDesc;
	}
	
	
	/**
	 * Get the Title error message
	 * 
	 * @return errorname of  Webinar title
	 */

	public String getTitleMissingError() 
	{
		String trainingDesc = this.titleMissingError.getText();
		return trainingDesc;
	}
	
}
