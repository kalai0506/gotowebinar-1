/*
 * Copyright (c) 1998-2013 Citrix Online LLC
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

package com.citrix.g2w.webdriver.tests;

import com.beust.jcommander.internal.Maps;
import com.citrix.g2w.webdriver.BaseTest;
import com.citrix.g2w.webdriver.CustomHtmlUnitDriver;
import com.citrix.g2w.webdriver.Groups;
import com.citrix.g2w.webdriver.dependencies.CorporateAccountProperties;
import com.citrix.g2w.webdriver.dependencies.RegistrantDetails;
import com.citrix.g2w.webdriver.dependencies.TestCorporateAccount;
import com.citrix.g2w.webdriver.dependencies.TestPersonalAccount;
import com.citrix.g2w.webdriver.pages.BasePage;
import com.citrix.g2w.webdriver.pages.LoginPage;
import com.citrix.g2w.webdriver.pages.ManageWebinarPage;
import com.citrix.g2w.webdriver.pages.MyWebinarsPage;
import com.citrix.g2w.webdriver.pages.ScheduleAWebinarPage;
import com.citrix.g2w.webdriver.pages.SystemStatusPage;
import com.citrix.g2w.webdriver.pages.emails.ZimbraEmail;
import com.citrix.g2w.webdriver.pages.managewebinar.ManageFollowUpEmailPage;
import com.citrix.g2w.webdriver.pages.recordings.MyRecordingsPage;
import com.citrix.g2w.webdriver.pages.recordings.RegisterForRecordingPage;
import com.citrix.g2w.webdriver.pages.recordings.ViewRecordingPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationConfirmationPage;
import com.citrix.g2w.webdriver.pages.registration.RegistrationPage;
import com.citrix.g2w.webdriver.util.G2WUtility;
import com.citrix.g2w.webdriver.util.InvokeMBean;
import com.gargoylesoftware.htmlunit.BrowserVersion;

import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;

/**
 * @author: Ravikanth Konda
 * @since: 10/16/13
 */
public abstract class BaseWebDriverTest extends BaseTest {

    /**
     * instance variable to set user account type.
     */
    protected String accountType;

    /**
     * instance object variable for user corporate account.
     */
    protected TestCorporateAccount corpAccount;
    /**
     * instance object variable for Corporate Account Properties.
     */
    protected CorporateAccountProperties corporateAccountProperties;

    /**
     * instance object variable for G2WUtils.
     */
    protected G2WUtility g2wUtils;
    /**
     * flag for LinkedIn test case.
     */
    protected boolean linkedInFlag = false;
    /**
     * instance object variable for personal account.
     */
    protected TestPersonalAccount personalAccount;
    /**
     * instance variable to set webinar description.
     */
    protected String webinarDescription;
    /**
     * instance variable to set webinar name.
     */
    protected String webinarName;
    /**
     * instance web driver object used to hold driver object.
     */
    protected WebDriver webDriver;
    
    protected Boolean closeWebDriverAtEndOfMethod = true;
    /**
     * instance variable store resource location.
     */
    protected final String RESOURCE_LOCATION = "src/test/resources/tests/data/";

    /**
     * default constructor for BaseWebDriverTest to initialize instance
     * variables.
     */
    public BaseWebDriverTest() {
        this.corporateAccountProperties = new CorporateAccountProperties();
        this.g2wUtils = new G2WUtility();
    }

    /**
     * After method that will be executed at the end of webdriver test run
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethodWebDriverTest() {
        // Close driver if not null
        if (closeWebDriverAtEndOfMethod) {
            cleanUpWebDriver();
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanUpWebDriver() {
        try {
            if (this.webDriver != null) {
                this.webDriver.quit();
                this.webDriver = null;
            }

        } catch (Exception e) {
            // Driver maybe already closed. Ignore.
        }
    }

    public String getEmail() {
        if(personalAccount != null) {
            return personalAccount.getEmail();
        }
        return null;
    }

    public String getPassword() {
        if(personalAccount != null) {
            return personalAccount.getPassword();
        }
        return null;
    }

    /**
     * Method used to create account, login and schedule a webinar with default
     * webinar details.
     * 
     * @param accountType
     *            (Type of account either personal or corporate)
     * @return manageWebinarPage object
     */
    public ManageWebinarPage createAccountLoginAndScheduleWebinar(
            final String accountType) {
        MyWebinarsPage myWebinarPage = this
                .createAccountLoginAndGoToMyWebinar(accountType);
        ManageWebinarPage manageWebinarPage = this.scheduleWebinar(myWebinarPage);
        return manageWebinarPage;
    }

    /**
     * Method used to create Personal account in given locale, login and
     * schedule a webinar with default webinar details.
     * 
     * @param locale String
     *
     * @return manageWebinarPage object
     */
    public ManageWebinarPage createPersonalAccountLoginAndScheduleWebinar(
            final String locale) {
        TestPersonalAccount account = this.accountService
                .createPersonalAccount(locale);
        LoginPage loginPage = new LoginPage(this.serviceUrl,
                this.getWebDriver(), true);
        MyWebinarsPage myWebinarsPage = loginPage.login(account.getEmail(),
                account.getPassword());
        ManageWebinarPage manageWebinarPage = this
                .scheduleWebinar(myWebinarsPage);
        return manageWebinarPage;
    }

    /**
     * Method used to create account, login and schedule a webinar with default
     * webinar details.
     * 
     * @param accountType
     *            (Type of account either personal or corporate)
     * @return manageWebinarPage object
     */
    public ManageWebinarPage createAccountLoginAndScheduleWebinar(
            final String accountType,
            final ScheduleAWebinarPage.Frequency frequency,
            final ScheduleAWebinarPage.WebinarType webinarType,
            final DateTime startDate, final DateTime endDate,
            final DateTime endTime) {
        MyWebinarsPage myWebinarPage = this
                .createAccountLoginAndGoToMyWebinar(accountType);
        ManageWebinarPage manageWebinarPage = this.scheduleWebinar(
                myWebinarPage, frequency, webinarType, startDate, endDate,
                endTime);
        return manageWebinarPage;
    }

    /**
     * Method used to create account, login and go to my webinar page.
     * 
     * @param accountType
     *            (Type of account either personal or corporate)
     * @return myWebinarsPage
     */
    public MyWebinarsPage createAccountLoginAndGoToMyWebinar(
            final String accountType) {
        this.setAccountType(accountType);
        this.createUserAccount();
        this.logger.log("created account");
        MyWebinarsPage myWebinarsPage = this.loginG2WWithRetryLogic();
        return myWebinarsPage;
    }

    /**
     * Method used to create account, login and upload a recording
     *
     * @param accountType (Type of account either personal or corporate)
     * @param recordingFile Recording file to be uploaded
     * @return myWebinarsPage
     */
    public MyRecordingsPage createAccountAndUploadRecording(final String accountType, final String recordingFile) {
        this.setAccountType(accountType);
        this.createUserAccount();
        this.logger.log("Created account " + this.personalAccount.getEmail());
        MyWebinarsPage myWebinarsPage = this.loginG2WWithRetryLogic();
        MyRecordingsPage myRecordingsPage = myWebinarsPage.gotoMyRecordingsPage();
        myRecordingsPage.uploadFile(getAbsoluteFilePath("Recordings" + File.separator + recordingFile));
        return myRecordingsPage;
    }

    /**
     * Method used to register to view a recording
     *
     * @param registrationUrl Registration url for the recording
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     */
    public void registerAndViewRecording(String registrationUrl, String firstName, String lastName, String email) {
        RegisterForRecordingPage registerForRecordingPage = new RegisterForRecordingPage(registrationUrl, this.getWebDriver());
        ViewRecordingPage viewRecordingPage = registerForRecordingPage.submit(firstName, lastName, email);
        viewRecordingPage.viewRecording();
    }

    /**
     * Method used to schedule a webinar with default details.
     *
     * @param basePage
     *            (basePage)
     * @return manageWebinarPage object
     */
    public ManageWebinarPage scheduleWebinar(final BasePage basePage) {
    	        ScheduleAWebinarPage scheduleAWebinarPage = basePage
                .gotoScheduleWebinarPage();
        this.webinarName = "Webinars - " + this.getUniqueString();
        this.webinarDescription = "Webinars Description - "
                + this.getUniqueString();
        ManageWebinarPage manageWebinarPage = scheduleAWebinarPage
                .scheduleWebinar(this.webinarName, this.webinarDescription);
        return manageWebinarPage;
    }

    /**
     * Method used to schedule a webinar with default details.
     * 
     * @param basePage
     *            (basePage)
     * @return manageWebinarPage object
     */
    public ManageWebinarPage scheduleWebinar(final BasePage basePage,
            final ScheduleAWebinarPage.Frequency frequency,
            final ScheduleAWebinarPage.WebinarType webinarType,
            final DateTime startDate, final DateTime endDate,
            final DateTime endTime) {
        ScheduleAWebinarPage scheduleAWebinarPage = basePage
                .gotoScheduleWebinarPage();
        this.webinarName = "Webinars - " + this.getUniqueString();
        this.webinarDescription = "Webinars Description - "
                + this.getUniqueString();
        ManageWebinarPage manageWebinarPage = scheduleAWebinarPage
                .scheduleWebinar(this.webinarName, this.webinarDescription,
                        frequency, webinarType, startDate, endDate, endTime);
        return manageWebinarPage;
    }

    /**
     * This is used to cleanup a single recording for a user. The user is expected to be in a logged out state.
     */
    public void cleanup() {
        MyWebinarsPage myWebinarPage = loginG2W(getEmail(), getPassword());
        myWebinarPage.gotoMyRecordingsPage();
        List<WebElement> removeLinks = this.webDriver.findElements(By.xpath("//a[contains(@href,'#removeRecording')]"));
        while(removeLinks.size() != 0) {
            removeLinks.get(0).click();
            // Not checking for success or failure
            WebElement confirmButton = this.webDriver.findElement(By.xpath("//a[@id='removeButton']"));
            confirmButton.click();
            removeLinks = this.webDriver.findElements(By.xpath("//a[contains(@href,'#removeRecording')]"));
        }

        logger.logWithScreenShot("Cleanup - Deleted recording", this.webDriver);
    }

    /**
     * Method used to create either personal or corporate account.
     */
    public void createUserAccount() {
        if (this.accountType.equalsIgnoreCase(Groups.PERSONAL)) {
            this.personalAccount = this.accountService.createPersonalAccount();
        } else if (this.accountType.equalsIgnoreCase(Groups.CORPORATE)) {
            this.corpAccount = this.accountService.createCorporateAccount(
                    this.corporateAccountProperties.getSeats(),
                    this.corporateAccountProperties.isAdminOrganiser());
            this.accountService.setContentSharingForAccount(
                    this.corpAccount.getAccountKey(),
                    this.corporateAccountProperties.isContentSharingEnabled());
            this.accountService.setRecordingv2AttributeForAccount(
                    this.corpAccount.getAccountKey(),
                    this.corporateAccountProperties.isRecordingv2Enabled());
        }
        //wait for account to be created
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return this.accountType;
    }

    /**
     * Method to get connection to the current running server according to the
     * server settings available in the properties file.
     * 
     * @return Connection
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            if (this.dbType.equals("oracle")) {
                conn = DriverManager.getConnection("jdbc:" + this.dbType
                        + ":thin:@" + this.dbName + ":" + this.dbPort + "/"
                        + this.dbInstance, this.dbUser, this.dbPassword);
            }
            this.logger.log("Connected to Database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Method to get connection for Historical Schema to the current running
     * server according to the server settings available in the properties file.
     * 
     * @return Connection
     */
    public Connection getConnectionForHistoricalSchema() {
        Connection conn = null;
        try {
            if (this.dbType.equals("oracle")) {
                conn = DriverManager
                        .getConnection("jdbc:" + this.dbType + ":thin:@"
                                + this.dbName + ":" + this.dbPort + "/"
                                + this.dbInstance, this.dbHistUser,
                                this.dbHistPassword);
            }
            this.logger.log("Connected to Database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Method to get connection for Quartz Schema to the current running server
     * according to the server settings available in the properties file.
     * 
     * @return Connection
     */
    public Connection getConnectionForQuartzSchema() {
        Connection conn = null;
        try {
            if (this.dbType.equals("oracle")) {
                conn = DriverManager.getConnection("jdbc:" + this.dbType
                        + ":thin:@" + this.dbName + ":" + this.dbPort + "/"
                        + this.dbInstance, this.dbQuartzUser,
                        this.dbQuartzPassword);
            }
            this.logger.log("Connected to Database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Method to format webinar id.
     * <ol>
     * <li>Input: 123456789</li>
     * <li>Output: 123-456-789</li>
     * </ol>
     * 
     * @param webinarId
     *            (webinar id)
     * @return formattedWebinarId
     */
    public String getFormattedWebinarId(final Long webinarId) {
        String formattedWebinarId = Long.toString(webinarId);
        formattedWebinarId = formattedWebinarId.substring(0, 3) + "-"
                + formattedWebinarId.substring(3, 6) + "-"
                + formattedWebinarId.substring(6, formattedWebinarId.length());
        this.logger.log("Webinar ID after formatted: " + formattedWebinarId);
        return formattedWebinarId;
    }

    /**
     * Method to get server id of active node.
     * 
     * @return serverId
     */
    public String getServerIdOfActiveNode() {
        // get system-status page
        SystemStatusPage systemStatusPage = new SystemStatusPage(
                this.getWebDriver(), this.testG2wHost);

        // Get displayed details and process Server ID
        String data = this.getWebDriver().getPageSource();
        Pattern serverData = Pattern.compile("Server=[^ \n]+");
        Matcher m = serverData.matcher(data);
        String serverId = "";
        if (m.find()) {
            serverId = m.group();
            serverId = serverId.replaceAll("Server=", "");
        } else {
            serverId = this.rmiHost;
        }
        return serverId;
    }

    /**
     * Method to create new WebDriver.
     * 
     * @return (driver object depends on input)
     */
    protected WebDriver getWebDriver() {
        // Not handling any concurrency issues since expect single thread to run
        // TODO: Web driver creation for every page seems weird, should be a
        // webdriver per test. Move out web driver instantiation
        if (this.webDriver != null) {
            return this.webDriver;
        }
        String driver = this.propertyUtil.getProperty("driver");
        if ((driver != null) && (driver.equalsIgnoreCase("firefox"))) {
            this.webDriver = new FirefoxDriver();

        } 
        else if(driver.equalsIgnoreCase("remotewebdriver")){
        	//Hardcoding to  firefox  
        	DesiredCapabilities capability = DesiredCapabilities.firefox();
        	try {
				this.webDriver = new RemoteWebDriver(new URL("http://"+this.propertyUtil.getProperty("seleniumGridHost")+":4444/wd/hub"), capability);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
        }
        else if (driver.equalsIgnoreCase("ghostdriver")) {
            // String path1 =
            // this.getClass().getResource("/test/phantomjs").getPath();
            String path = "D://Projects//WebinarProject//lib//phantomjs/ .exe";

            // Command line arguments to phantomjs
            ArrayList<String> cliArgsCap = new ArrayList<String>();
            cliArgsCap.add("--web-security=false");
            cliArgsCap.add("--ssl-protocol=any");
            cliArgsCap.add("--ignore-ssl-errors=true");

            DesiredCapabilities dCaps = new DesiredCapabilities();

            dCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                    cliArgsCap);
            dCaps.setCapability(
                    PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                    path);
            dCaps.setJavascriptEnabled(true);

            this.webDriver = new PhantomJSDriver(dCaps);
            this.webDriver.manage().timeouts()
            .implicitlyWait(10, TimeUnit.SECONDS);
        } else {
            this.webDriver = new CustomHtmlUnitDriver(true, false);
            this.webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        return this.webDriver;
    }

    /**
     * Method to create new WebDriver by setting desired browser language.
     * 
     * @param browserLanguage
     *            (browser language)
     * @return (driver object depends on input)
     */
    protected WebDriver getWebDriverBySettingDesiredBrowserLanguage(
            final String browserLanguage) {
        String driver = this.propertyUtil.getProperty("driver");
        if ((driver != null) && (driver.equalsIgnoreCase("firefox"))) {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("intl.accept_languages", browserLanguage);
            return new FirefoxDriver(profile);
        } else {
            BrowserVersion browserVersion = new BrowserVersion("null", "null",
                    "null", 0);
            browserVersion.setBrowserLanguage(browserLanguage);
            return new HtmlUnitDriver(browserVersion);
        }
    }

    /**
     * Method used to create my webinar page object based on personal or
     * corporate account using retry logic to avoid my webinar page intermittent
     * error.
     * 
     * @return myWebinarsPage object
     */
    public MyWebinarsPage loginG2WWithRetryLogic() {
        MyWebinarsPage myWebinarsPage = null;
        int count = 0;
        int waitTime = 8000;
        while (count < 2) {
            try {
                myWebinarsPage = this.loginG2W(count > 0);
                break;
            } catch (Throwable e) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                count++;
                waitTime += 2000 * count;
            }
        }
        return myWebinarsPage;
    }

    /**
     * Method used to create my webinar page object based on personal or
     * corporate account.
     * 
     * @return myWebinarsPage object
     */
    public MyWebinarsPage loginG2W(boolean retry) {
        this.logger.log("trying to login");
        MyWebinarsPage myWebinarsPage = null;
        // TODO: Not clear on purpose of this flag, what happens if linkedInFlag
        // is true, we dont create web driver?
        // if (!this.linkedInFlag) {
        // this.webDriver = this.getWebDriver();
        // }
        LoginPage loginPage = new LoginPage(this.serviceUrl,
        this.getWebDriver(), !retry);
        System.out.println(serviceUrl);
   
        if (this.accountType.equalsIgnoreCase(Groups.PERSONAL)) {
            myWebinarsPage = loginPage.login(this.personalAccount.getEmail(),
                    this.personalAccount.getPassword());
        } else {
            myWebinarsPage = loginPage.login(this.corpAccount.getAdminEmail(),
                    this.corpAccount.getAdminPassword());
        }
        return myWebinarsPage;
    }

    /**
     * Method used to create my webinar page object based on personal or
     * corporate account.
     * 
     * @param email
     *            (email)
     * @param password
     *            (password)
     * @return MyWebinarsPage
     */
    public MyWebinarsPage loginG2W(final String email, final String password) {
        MyWebinarsPage myWebinarsPage;
        LoginPage loginPage = new LoginPage(this.serviceUrl,
                this.getWebDriver(), true);
        myWebinarsPage = loginPage.login(email, password);
        return myWebinarsPage;
    }

    /**
     * @param accountType
     *            the accountType to set
     */
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    /**
     * Method to start, Add an Attendee and End webinar.
     * 
     * @param authenticationToken
     *            (User authentication token)
     * @param userKey
     *            (user key)
     * @param webinarId
     *            (webinar id)
     * @param webinarStartTime
     *            (webinar start time)
     * @param registrantDetails
     *            (registrant details)
     * @param webinarDuration
     *            (webinar duration in millis)
     */
    public void startAddAttendeeAndEndWebinar(final String authenticationToken,
            final Long userKey, final Long webinarId,
            final DateTime webinarStartTime,
            final Map<Long, RegistrantDetails> registrantDetails,
            final Long webinarDuration) {
        try {
            // Start the webinar based on webinar id to join attendee.
            this.logger
            .log("Start the webinar having webinar id :" + webinarId);

            this.session.startWebinar(authenticationToken, userKey, webinarId,
                    webinarStartTime);

            if (MapUtils.isNotEmpty(registrantDetails)) {
                // Add attendee to the webinar
                for (Entry<Long, RegistrantDetails> registrant : registrantDetails
                        .entrySet()) {
                    Long registrantId = registrant.getKey();
                    RegistrantDetails registrantDetail = registrant.getValue();
                    if (registrantDetail != null) {
                        this.logger.log("Join a registered attendee : "
                                + registrantDetail.getFirstName() + " "
                                + registrantDetail.getLastName());
                        this.session.addAttendee(webinarId, registrantId,
                                registrantDetail.getEmailAddress(),
                                registrantDetail.getFirstName() + " "
                                        + registrantDetail.getLastName(), null,
                                        null);
                    }
                }
            }

            // wait for webinarDuration before ending the meeting
            if (webinarDuration > 5000L) {
                Thread.sleep(webinarDuration);
            } else {
                Thread.sleep(5000L);
            }

            // End the webinar
            this.session.endWebinar(webinarId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to format start and end date as list.
     * 
     * @param startDate
     *            (start date)
     * @param expectedEndDate
     *            (expected end date)
     * @param frequency
     *            (webinar frequency)
     * @return dateTimeList (date and time as list)
     */
    public List<String> convertDateAndTimeToList(DateTime startDate,
            DateTime expectedEndDate, final String frequency) {
        List<String> dateTimeList = new LinkedList<String>();
        DateTime endDate = startDate.plusHours(1);

        int startDayOfMonth = startDate.getDayOfMonth();
        int endDayOfMonth = endDate.getDayOfMonth();

        DateTimeFormatter startDateFormat = DateTimeFormat
                .forPattern(this.messages.getMessage(
                        "date.format.mask.long.startAndEndDate.short.start",
                        null, this.locale));
        DateTimeFormatter endDateFormat;
        if (startDayOfMonth == endDayOfMonth) {
            endDateFormat = DateTimeFormat.forPattern(this.messages.getMessage(
                    "date.format.mask.long.startAndEndDate.short.end", null,
                    this.locale));
        } else {
            endDateFormat = DateTimeFormat.forPattern(this.messages.getMessage(
                    "date.format.mask.long.startAndEndDate.long.end", null,
                    this.locale));
        }
        String formattedStartDate;
        String formattedEndDate;

        while (startDate.isBefore(expectedEndDate)) {
            formattedStartDate = startDateFormat.withLocale(this.locale).print(
                    startDate);
            formattedEndDate = endDateFormat.withLocale(this.locale).print(
                    endDate);
            String expectedText = formattedStartDate + " - " + formattedEndDate;
            dateTimeList.add(expectedText);
            if (frequency.equalsIgnoreCase("daily")) {
                startDate = startDate.plusDays(1);
            } else if (frequency.equalsIgnoreCase("weekly")) {
                startDate = startDate.plusWeeks(1);
            } else if (frequency.equalsIgnoreCase("monthly")) {
                startDate = startDate.plusMonths(1);
            }
            endDate = startDate.plusHours(1);
        }
        return dateTimeList;
    }

    public void startAddAttendeeAndEndWebinar(String regUrl, Long webinarID,
            DateTime dateTime, int numberOfAttendees) {
        String authenticationToken = this.authService.getAuthToken(
                this.personalAccount.getEmail(),
                this.personalAccount.getPassword());
        Map<Long, RegistrantDetails> attendeeMap = Maps.newHashMap();
        for (int i = 0; i < numberOfAttendees; i++) {
            String attendeeFirstName = "attendeeFName" + i;
            String attendeeLastName = "attendeeLName" + i;
            String attendeeEmailId = "attendee-email" + i + "@nodeliver.com";

            // register attendee
            RegistrantDetails attendee = new RegistrantDetails();
            attendee.setFirstName(attendeeFirstName);
            attendee.setLastName(attendeeLastName);
            attendee.setEmailAddress(attendeeEmailId);
            RegistrationPage registrationPage = new RegistrationPage(regUrl,
                    webDriver);
            RegistrationConfirmationPage confirmationPage = registrationPage
                    .registerAttendeeDetailsWithConfirmation(attendee);
            attendeeMap.put(Long.valueOf(confirmationPage.getRegistrantID()),
                    attendee);
        }
        // Start, add attendee(s) and end the webinar
        this.startAddAttendeeAndEndWebinar(authenticationToken,
                this.personalAccount.getUserKey(), webinarID, dateTime,
                attendeeMap, 3000L);
    }

    /**
     * Method to get absolute file path.
     *
     * @param fileName
     *            // * Name Of the file.
     * @return The absolute pathname string denoting the same file.
     */
    public String getAbsoluteFilePath(final String fileName) {
        File file = new File(this.RESOURCE_LOCATION + fileName);
        return file.getAbsolutePath();
    }
    
    /**
     * Method to age Webinar.
     * @param manageWebinarPage (manage webinar page)
     * @param webinarKey (webinar key)
     * @param webinarId  (webinar id)
     * @param hrsToAge   (hours to age)
     */
    public void ageWebinar(BasePage basePage, Long webinarKey, long webinarId, Long hrsToAge) {
        // Age the webinar by specified time in hrsToAge
        logger.log("Aging the webinar by " + hrsToAge + " hours");
        try {
            // Aging webinar in Active Schema
            Connection connection = getConnection();
            basePage.ageWebinarTimesInActiveSchema(connection, hrsToAge, webinarKey, webinarId);
            connection.close();

            // Aging webinar in Historic Schema
            Connection connectionHist = getConnectionForHistoricalSchema();
            basePage.ageWebinarTimesInHistoricSchema(connectionHist, hrsToAge, webinarKey, webinarId);
            connectionHist.close();
        
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log("Error in aging the webinar times");
        }
    }

    /**
     * Method to check if expected email content is present or not.
     * @param messageObject (message object)
     * @param listOfExpectedDetailsInMailBody (expected email body list)
     * @return expectedEmailContentPresent
     */
    public boolean isExpectedContentExistsInEmail(String messageBody,
    						final List<String> listOfExpectedDetailsInMailBody) {
    	
        boolean expectedEmailContentPresent = false;
        // Verify the actual email content with expected email content
        for (String expectedMailBody : listOfExpectedDetailsInMailBody) {
            if (messageBody.contains(expectedMailBody)) {
                logger.log("Email body contains : " + listOfExpectedDetailsInMailBody);
                expectedEmailContentPresent = true;
            } else {
                logger.log("Email body does not contain: " + expectedMailBody);
                expectedEmailContentPresent = false;
                break;
            }
        }
        return expectedEmailContentPresent;
    }   
    
    public RegistrationConfirmationPage setupRegistrationConfirmationPage(
            String registrationUrl, String firstName, String lastName,
            String email) {
        // go to attendee registration page
        RegistrationPage registrationPage = new RegistrationPage(
                registrationUrl, this.getWebDriver());

        // Submit the form and validate confirmation
        RegistrantDetails registrantDetails = createRegistrantDetails(
                firstName, lastName, email);
        return registrationPage
                .registerAttendeeDetailsWithConfirmation(registrantDetails);
    }

    public RegistrantDetails createRegistrantDetails(String firstName,
            String lastName, String email) {
        return new RegistrantDetails(firstName, lastName, email);
    }
    
    /**
     * Trigger follow up email for the webinar
     * 
     * @param webinarKey
     */
    public void triggerFollowUpEmailForWebinar(Long webinarKey) {
        // get server id of active node to access m beans
        String serverId = getServerIdOfActiveNode();

        // Trigger follow up email
        logger.log("Trigger follow up email");
        InvokeMBean invokeMBean = new InvokeMBean();
        invokeMBean.triggerFollowUpEmail(rmiAdminUserName, rmiPassword,
                serverId, rmiPort, webinarKey);
    }

    /**
     * Trigger Reminder email for the webinar
     * 
     * @param webinarKey
     */
    public void triggerReminderEmailForWebinar(Long webinarKey) {
        // get server id of active node to access m beans
        String serverId = getServerIdOfActiveNode();

        // Trigger Reminder email
        logger.log("Trigger Reminder email");
        InvokeMBean invokeMBean = new InvokeMBean();
        invokeMBean.triggerReminderEmail(rmiAdminUserName, rmiPassword,
                serverId, rmiPort, webinarKey);
    }

    /**
     * Method to suspend user.
     * 
     * @param userKey
     */

    public void suspendUser(Long userKey) {
        this.accountService.suspendUser(userKey);
    }

    /**
     * Method to suspend user.
     * 
     * @param userKey
     */

    public void unSuspendUser(Long userKey) {
        this.accountService.unSuspendUser(userKey);
    }

    /**
     * Method to un license user.
     * 
     * @param userKey
     * @param licenseKey
     */

    public void unLicenseUser(Long userKey, String licenseKey) {
        this.accountService.unLicenseUser(userKey, licenseKey);
    }

    /**
     * Method to verify that email receieved for registrant.
     * 
     * @param userEmail
     *            (registrant email)
     * @param mailSubject
     *            (expected subject line)
     */
    public boolean isEmailReceievedForWebinar(String userEmail,
            String mailSubject) {
        // login to user email account
        ZimbraEmail zimbraEmail = new ZimbraEmail(userEmail);
        zimbraEmail.login();
        // search the inbox for mail with expected subject, to and from address
        String fromAddress = propertyUtil.getProperty("customercare.email");
        Message message = zimbraEmail.getEmail(mailSubject, userEmail,
                fromAddress);
        // verify if mail has been received and contains expected contents
        logger.log("Verify if mail has been received");
        zimbraEmail.logout();
        if (message != null) {            
            return true;
        } else
            return false;
    }

    
    /**
     * Setup follow up email for attendees and absentees for the webinar
     * 
     * @param manageWebinarPage
     */
    public void setUpFollowUpEmailForWebinar(ManageWebinarPage manageWebinarPage) {

        String daysOffsetSelectionMessage = messages.getMessage(
                "manageFollowUpEmail.followUpEmailOffset.day_1", null, locale);

        // Navigate to Manage Follow up Email settings for absentee and setup
        // follow up email
        ManageFollowUpEmailPage manageFollowUpEmailPageForAbsentees = manageWebinarPage
                .goToFollowUpEmailPageForAbsentees();
        manageWebinarPage = manageFollowUpEmailPageForAbsentees
                .setFollowUpEmailForWebinar(daysOffsetSelectionMessage);

        // Navigate to Manage Follow up Email settings for attendee by clicking
        // on edit follow up email link
        ManageFollowUpEmailPage manageFollowUpEmailPageForAttendees = manageWebinarPage
                .goToFollowUpEmailPageForAttendee();
        manageWebinarPage = manageFollowUpEmailPageForAttendees
                .setFollowUpEmailForWebinar(daysOffsetSelectionMessage);
    }


    public void setCloseWebDriverAtEndOfMethod(Boolean closeWebDriverAtEndOfMethod) {
        this.closeWebDriverAtEndOfMethod = closeWebDriverAtEndOfMethod;
    }

}
