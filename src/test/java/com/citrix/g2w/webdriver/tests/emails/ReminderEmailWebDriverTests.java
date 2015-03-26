package com.citrix.g2w.webdriver.tests.emails;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Value;

import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.emails.EmailDuration;
import com.citrix.g2w.webdriver.pages.emails.ReminderEmailPage;
import com.citrix.g2w.webdriver.pages.managewebinar.PanelistPage;
import com.citrix.g2w.webdriver.tests.BaseWebDriverTest;

/**
 * 
 * @author kapilmahalavat
 * 
 */

public class ReminderEmailWebDriverTests extends BaseWebDriverTest {
    @Value("${g2w.url}/webinars.tmpl")
    private String myWebinarsPageURL;

    /**
     * Method used to run tests for different Account State.
     * 
     * @return Object (two dimensional Array Object)
     */
    @DataProvider
    public Object[][] accountState() {
        return new Object[][] { { "suspended" }, { "unlicensed" } };
    }

    /**
     * Test to verify that No Reminder emails are being sent for Cancelled
     * Webinar.
     * <ol>
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Register attendee for the webinar</li>
     * <li>After Registration Cancel the Webinar.</li>
     * <li>Invoke the mbean to trigger Reminder mail</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Reminder Email doesn't exist for cancelled Webinar.</li>
     * </ol>
     */

    @Test(timeOut=300000, groups = { Groups.PERSONAL, Groups.REMINDER_EMAIL, Groups.EMAILS }, description = "Emails - ReminderEmail: Verify No Reminder Emails are being sent for cancelled Webinar.")
    public void verifyNoReminderEmailForCancelledWebinar() {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        ReminderEmailPage reminderEmailPage = manageWebinarPage
                .clickReminderEmail();
        reminderEmailPage.setReminderEmail(EmailDuration.ONE_HOUR);
        setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        // Cancel the webinar.
        MyWebinarsPage myWebinarsPage = new MyWebinarsPage(myWebinarsPageURL,
                this.getWebDriver());
        myWebinarsPage.goToManageWebinarPageOnWebinarKey(webinarKey);
        manageWebinarPage.cancelWebinar(true);
        // trigger Reminder email
        String emailSubject = triggerEmail(webinarKey, EmailDuration.ONE_HOUR);
        // Verify No Reminder email for Cancelled Webinar.
        if (isEmailReceievedForWebinar(registrantemailId, emailSubject)) {
            Assert.fail("Registrants are getting Reminder Email for Cancelled Webinar.");
        }
    }

    /**
     * Test to verify that No Reminder emails are being sent when user is
     * suspended.
     * <ol>
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Register attendee for the webinar</li>
     * <li>After Registration suspend the user</li>
     * <li>Invoke the mbean to trigger Reminder mail</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Reminder Email doesn't exist for suspended user.</li>
     * </ol>
     */

    @Test(timeOut=300000, dataProvider = "accountState", groups = { Groups.PERSONAL,
            Groups.REMINDER_EMAIL, Groups.EMAILS }, description = "Emails - ReminderEmail: Verify No Reminder Emails are being sent from suspended/unlicensed user.")
    public void verifyNoReminderEmailFromPersonalAccount(final String accountState) {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        ReminderEmailPage reminderEmailPage = manageWebinarPage
                .clickReminderEmail();
        reminderEmailPage.setReminderEmail(EmailDuration.ONE_HOUR);
        setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);

        if (accountState.equals("suspended")) {
            // Suspend User
            suspendUser(this.personalAccount.getUserKey());
        }
        if (accountState.equals("unlicensed")) {
            // Unlicense User
            unLicenseUser(this.personalAccount.getUserKey(),
                    this.personalAccount.getLicenseKey());
        }
        // trigger Reminder email
        String emailSubject = triggerEmail(webinarKey, EmailDuration.ONE_HOUR);
        // Verify No Reminder email for Suspended webinar.
        if (isEmailReceievedForWebinar(registrantemailId, emailSubject)) {
            Assert.fail("Registrants are getting Webinar's Reminder Email from Suspended User.");
        }
    }

    /**
     * Test to verify that Reminder emails are being sent when user is
     * un-suspended.
     * <ol>
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Register attendee & Add Panelist to the webinar</li>
     * <li>After Registration suspend the user</li>
     * <li>Invoke the mbean to trigger Reminder mail</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Reminder Email doesn't exist from suspended user.</li>
     * <li>un-suspend the user</li>
     * <li>Invoke the mbean to trigger Reminder mail</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Reminder Email exist when user is unsuspended user.</li>
     * </ol>
     */

    @Test(timeOut=300000, groups = { Groups.PERSONAL, Groups.REMINDER_EMAIL, Groups.EMAILS }, description = "Emails - ReminderEmail: Verify Reminder Emails are being sent for un-suspended user.")
    public void verifyReminderEmailWhenUserIsUnsuspended() {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        String panelistName = this.propertyUtil.getProperty("panelist.name");
        String panelistEmailId = this.propertyUtil
                .getProperty("panelist.emailId");
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        PanelistPage panelistpage = manageWebinarPage.gotoPanelistPage();
        panelistpage.addPanelist(panelistName, panelistEmailId);
        ReminderEmailPage reminderEmailPage = manageWebinarPage
                .clickReminderEmail();
        // Set Reminder email to one Hour.
        reminderEmailPage.setReminderEmail(EmailDuration.ONE_HOUR);
        setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        // Suspend User
        suspendUser(this.personalAccount.getUserKey());
        // trigger Reminder email
        String emailSubject = triggerEmail(webinarKey, EmailDuration.ONE_HOUR);
        if (isEmailReceievedForWebinar(registrantemailId, emailSubject)) {
            Assert.fail("Registrants are getting Webinar's Reminder Email from Suspended User.");
        }
        if (isEmailReceievedForWebinar(panelistEmailId, emailSubject)) {
            Assert.fail("Panelists are getting Webinar's Reminder Email from Suspended User.");
        }
        // Un-Suspend User
        unSuspendUser(this.personalAccount.getUserKey());
        // trigger Reminder email
        emailSubject = triggerEmail(webinarKey, EmailDuration.ONE_HOUR);
        // Verify Reminder email for un-suspended user.
        if (!(isEmailReceievedForWebinar(registrantemailId, emailSubject))) {
            Assert.fail("Registrants are not getting Webinar's Reminder Email when user is un-suspended.");
        }
        if (!(isEmailReceievedForWebinar(panelistEmailId, emailSubject))) {
            Assert.fail("Panelists are getting Webinar's Reminder Email from Suspended User.");
        }

    }

    /**
     * Test to verify that No Reminder emails are being sent to Panelist when
     * user's account is suspended/unlicensed.
     * <ol>
     * <li>Create a personal account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Register attendee for the webinar</li>
     * <li>Add Panelist to the webinar</li>
     * <li>After Registration suspend/unlicense the user</li>
     * <li>Invoke the mbean to trigger Reminder mail</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Reminder Email doesn't exist for suspended/unlicensed user.</li>
     * </ol>
     */

    @Test(timeOut=300000, dataProvider = "accountState", groups = { Groups.PERSONAL,
            Groups.REMINDER_EMAIL, Groups.EMAILS }, description = "Emails - ReminderEmail: Verify No Reminder Emails are being sent to Panelist from suspended/unlicensed user.")
    public void verifyNoReminderEmailToPanelist(final String accountState) {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        String panelistName = this.propertyUtil.getProperty("panelist.name");
        String panelistEmailId = this.propertyUtil
                .getProperty("panelist.emailId");
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.PERSONAL);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        PanelistPage panelistpage = manageWebinarPage.gotoPanelistPage();
        panelistpage.addPanelist(panelistName, panelistEmailId);
        ReminderEmailPage reminderEmailPage = manageWebinarPage
                .clickReminderEmail();
        // Set Reminder email to one Hour.
        reminderEmailPage.setReminderEmail(EmailDuration.ONE_HOUR);

        setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);
        if (accountState.contains("suspended")) {
            // Suspend User
            suspendUser(this.personalAccount.getUserKey());
        }
        if (accountState.contains("unlicensed")) {
            // Unlicense User
            unLicenseUser(this.personalAccount.getUserKey(),
                    this.personalAccount.getLicenseKey());
        }
        // trigger Reminder email
        String emailSubject = triggerEmail(webinarKey, EmailDuration.ONE_HOUR);
        // Verify No Reminder email for Unlicense user.
        if (isEmailReceievedForWebinar(registrantemailId, emailSubject)) {
            Assert.fail("Registrants are getting Webinar's Reminder Email from "
                    + accountType + " User.");
        }

        if (isEmailReceievedForWebinar(panelistEmailId, emailSubject)) {
            Assert.fail("Panelists are getting Webinar's Reminder Email from "
                    + accountType + " User.");
        }
    }

    /**
     * Test to verify that No Reminder emails are being sent when user is
     * suspended.
     * <ol>
     * <li>Create a Corporate account</li>
     * <li>Go to schedule webinar page and schedule a webinar with name</li>
     * <li>Register attendee for the webinar</li>
     * <li>After Registration suspend the user</li>
     * <li>Invoke the mbean to trigger Reminder mail</li>
     * <li>Login to Zimbra mail with registrant email id</li>
     * <li>Verify Reminder Email doesn't exist for suspended user.</li>
     * </ol>
     */

    @Test(timeOut=300000, dataProvider = "accountState", groups = { Groups.PERSONAL,
            Groups.REMINDER_EMAIL, Groups.EMAILS }, description = "Emails - ReminderEmail: Verify No Reminder Emails are being sent from suspended/unlicensed user.")
    public void verifyNoReminderEmailFromCorporateAccount(
            final String accountState) {
        String registrantfirstName = this.propertyUtil
                .getProperty("registrant.firstName");
        String registrantlastName = this.propertyUtil
                .getProperty("registrant.lastName");
        String registrantemailId = this.propertyUtil
                .getProperty("registrant.emailId");
        String panelistName = this.propertyUtil.getProperty("panelist.name");
        String panelistEmailId = this.propertyUtil
                .getProperty("panelist.emailId");
        // Create a personal account, login, schedule a webinar and go to
        // manage webinar page
        ManageWebinarPage manageWebinarPage = this
                .createAccountLoginAndScheduleWebinar(Groups.CORPORATE);
        Long webinarKey = manageWebinarPage.getWebinarKey();
        PanelistPage panelistpage = manageWebinarPage.gotoPanelistPage();
        panelistpage.addPanelist(panelistName, panelistEmailId);
        ReminderEmailPage reminderEmailPage = manageWebinarPage
                .clickReminderEmail();
        reminderEmailPage.setReminderEmail(EmailDuration.ONE_HOUR);
        setupRegistrationConfirmationPage(
                manageWebinarPage.getRegistrationURL(), registrantfirstName,
                registrantlastName, registrantemailId);

        if (accountState.equals("suspended")) {
            // Suspend User
            suspendUser(this.corpAccount.getAdminUserKey());
        }
        if (accountState.equals("unlicensed")) {
            // Unlicense User
            unLicenseUser(this.corpAccount.getAdminUserKey(),
                    this.corpAccount.getLicenseKey());
        }
        // trigger Reminder email
        String emailSubject = triggerEmail(webinarKey, EmailDuration.ONE_HOUR);
        // Verify No Reminder email for Suspended webinar.
        if (isEmailReceievedForWebinar(registrantemailId, emailSubject)) {
            Assert.fail("Registrants are getting Webinar's Reminder Email from Suspended User.");
        }
        if (isEmailReceievedForWebinar(panelistEmailId, emailSubject)) {
            Assert.fail("Panelists are getting Webinar's Reminder Email from Suspended User.");
        }
    }

    private String triggerEmail(Long webinarKey, EmailDuration emailduration) {
        triggerReminderEmailForWebinar(webinarKey);
        String reminderEmailSubject = messages.getMessage(
                "email.settings.reminder.default.subject",
                new String[] { webinarName }, locale);
        String emailSubject = reminderEmailSubject.replace("[time]", messages
                .getMessage(
                        "email.settings.reminder." + emailduration.getKey(),
                        null, locale));
        return emailSubject;

    }
}
