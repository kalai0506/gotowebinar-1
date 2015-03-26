package com.citrix.g2w.webdriver.tests.managewebinar;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.ScheduleSimilarWebinarPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

public class ScheduleSimilarWebnarPageWebdriverTest extends BaseWebDriverTest {
	/**
	 * * @author saraswathi.venkatreddy
	 * <ol>
	 * Test schedule similar webnar
	 * <li>Create a personal account</li>
	 * <li>login to G2W with created credentials<li>
	 * <li>Go to schedule webinar page and schedule a Single webinar with name</li>
	 * <li>Go to the manage webinar page</li>
	 * <li>click on the similarschedule webnar link
	 * </li>
	 * <li>Verify the confirnmation page:schedule similar webnar.</li>
	 * <li>Change the Title text and submit the details</li>
	 * 
	 * <li>Verify the confirmation page:Save changes message,Webinar Title</li>
	 * </ol>
	 */

	@Test(groups = { Groups.PERSONAL, Groups.REGISTRATION,
			Groups.MANAGE_WEBINAR }, description = "schedule similar webnar)")
	public void verifyOnScheduleASimilarWebinarPage() {
		ManageWebinarPage manageWebinarPage = this
				.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
		Assert.assertEquals(this.messages.getMessage("schedule.success", null, this.locale),manageWebinarPage.getWebnarSuccessMsg());
//		   ManageWebinarPage manageWebinarPage = this.scheduleWebinar(myWebinarPage);
		ScheduleSimilarWebinarPage scheduleSimilarWebinarPage=manageWebinarPage.getScheduleSimilarWebnar();	
		System.out.println("am in schedule similar webnar page");
		 manageWebinarPage=scheduleSimilarWebinarPage.scheduleWebinar("javaspring","jasdwsfdfgfbh");
		 Assert.assertEquals(this.messages.getMessage("copyTraining.success", null, this.locale),manageWebinarPage.getWebnarCopiedSuccessMsg());
	}
	/**
	 * @author Saraswathi.venkatreddy
	 * <ol>
	 * test title and descriptiond can be edited while scheduling similar webnar
	 * <li>create a personel account</li>
	 * <li>login to G2W with created credentials</li>
	 * <li>Go to schedule webinar page and schedule a Single webinar with name and description</li>
	 * <li> go to the manage webnar page </li>
	 * <li> verify that manage webnar page with url"/manageWebinar.tmpl" </li>
	 * <li> click on schedule webnar link</li>
	 * <li> verify the schedule similar webnarpage by url""</li>
	 * <li>Verify the confirnmation page:schedule similar webnar.</li>
	 * <li>Change the Title text and submit the details</li>
	 * <li> verify webnar name and description are edited>/li>
	 * <verify assertion msg "webnar copied"</li>
     */
	@Test(groups = { Groups.PERSONAL, Groups.REGISTRATION,
			Groups.MANAGE_WEBINAR }, description = "schedule similar webnar)")	
	public void  VerifyTitleAndDescriptionCanbeEdited()
	{
		ManageWebinarPage manageWebinarPage = this
				.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
	    Assert.assertEquals(
				this.messages.getMessage("schedule.success", null, this.locale),
				manageWebinarPage.getWebnarSuccessMsg());
		 ScheduleSimilarWebinarPage scheduleSimilarWebinarPage=manageWebinarPage.getScheduleSimilarWebnar();	
		 manageWebinarPage=scheduleSimilarWebinarPage.getScheduleSimilarWebnarEditTitleAndDescription("Webtest Webinar-1329959235-76418547 name can be edited after copy","Webtest Webinar description can be edited");
		 Assert.assertEquals(this.messages.getMessage("copyTraining.success2", null, this.locale),manageWebinarPage.getWebnarCopiedSuccessMsg());
	}
	/**
	 * @author Saraswathi.venkatreddy
	 * <ol>
	 * test title and descriptiond can be edited while scheduling similar webnar
	 * <li>create a personel account</li>
	 * <li>login to G2W with created credentials</li>
	 * <li>Go to schedule webinar page and schedule a Single webinar with name and description</li>
	 * <li> go to the manage webnar page </li>
	 * <li> verify that manage webnar page with url"/manageWebinar.tmpl" </li>
	 * <li> click on schedule webnar link</li>
	 * <li> verify the schedule similar webnarpage by url""</li>
	 * <li>Verify the confirnmation page:schedule similar webnar.</li>
	 * <li>Change the Title text and submit the details</li>
	 * <li> verify webnar name and description are edited>/li>
	 * <verify assertion msg "webnar copied"</li>
	 * <li>goto my webnars page</li>
	 * <li>verify assertion mywebnars page url<li>
	 * <li> verify assertion copied webnar existance in mywebnars page</li>
     */
	@Test(groups = { Groups.PERSONAL, Groups.REGISTRATION,
			Groups.MANAGE_WEBINAR }, description = "schedule similar webnar)")	
	public void verifyThatMyWebnarpageShowsNewlyCopiedWebnar()
	{
		 ManageWebinarPage manageWebinarPage = this.createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
	     Assert.assertEquals(this.messages.getMessage("schedule.success", null, this.locale),manageWebinarPage.getWebnarSuccessMsg());
		 ScheduleSimilarWebinarPage scheduleSimilarWebinarPage=manageWebinarPage.getScheduleSimilarWebnar();	
		 manageWebinarPage=scheduleSimilarWebinarPage.scheduleWebinar("Webtest Webinar-1329959235-76418547 name can be edited after copy","Webtest Webinar description can be edited");
	     // Assert.assertEquals(this.messages.getMessage("copyTraining.success2", null, this.locale),manageWebinarPage.getWebnarCopiedSuccessMsg());
		 System.out.println("before gng to mywebnar page");
		 MyWebinarsPage myWebinarsPage=manageWebinarPage.getMyWebinarsPage();
		 System.out.println("in my webinar page");
		 myWebinarsPage.verifyCopiedWebnarExistance("Webtest Webinar-1329959235-76418547 name can be edited after copy");
		 
	}
	
}

