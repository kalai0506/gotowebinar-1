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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import com.citrix.g2w.webdriver.PropertyUtil;
import com.citrix.g2w.webdriver.TestLogger;
import com.citrix.g2w.webdriver.pages.recordings.MyRecordingsPage;
import com.citrix.g2w.webdriver.pages.reports.GenerateReportsPage;

/**

 */
public abstract class BasePage {

    /**
     * web element used to get catalogs page link.
     */
    @FindBy(xpath = "//a[@href='/webinar/catalogs.tmpl']")
    private WebElement catalogsPageLink;
    /**
     * instance variable store default timeout value.
     */
    protected final int DEFAULT_TIMEOUT = 30;
    /**
     * instance variable store download path value.
     */
    protected final String DOWNLOAD_PATH = "src/test/resources/tests/data";
    /**
     * instance object variable store driver details.
     */
    protected WebDriver driver;
    /**
     * web element used to store generate reports page link.
     */
    @FindBy(xpath = "//a[@href='/reporting/generateReports.tmpl']")
    private WebElement generateReportsPageLink;
    /**
     * By element for header title of a page.
     */
    private final By headerTitle = By.xpath("//div/h1");
   
    /**
     * instance object variable store locale details.
     */
    protected Locale locale;
    /**
     * instance object variable for logger.
     */
    protected TestLogger logger = new TestLogger();
    /**
     * instance object variable for resource bundle message source.
     */
    protected ResourceBundleMessageSource messages;
    /**
     * instance object variable for broker resource bundle message source.
     */
    protected ResourceBundleMessageSource brokerMessages;
    /**
     * web element used to store my recordings page link.
     */
    @FindBy(xpath = "//a[contains(@href,'/recordings.tmpl')]")
    private WebElement myRecordingsPageLink;
    /**
     * web element used to store my webinar page link.
     */
    @FindBy(xpath = "//a[contains(@href,'/webinars.tmpl')]")
    private WebElement myWebinarPageLink;
    /**
     * instance object variable for property util.
     */
    protected PropertyUtil propertyUtil;
    /**
     * instance variable store report format for csv.
     */
    protected final String REPORT_FORMAT_CSV = "CSV";
    /**
     * instance variable store report formate for xls.
     */
    protected final String REPORT_FORMAT_XLS = "XLS";
    /**
     * instance variable store resource location.
     */
    protected final String RESOURCE_LOCATION = "src/test/resources/tests/data/";
    /**
     * web element used to store schedule webinar page link.
     */
    @FindBy(xpath = "//a[@href='/schedule.tmpl']")
    WebElement scheduleWebinarPageLink ;
    //@FindBy(xpath = "//a[contains(@href,'/schedule.tmpl')]")
    //WebElement scheduleWebinarPageLink;

    /**
     * web element for GoToWebinar page link.
     */
    @FindBy(xpath = "//a[@id='g2wLink']")
    WebElement gotoWebinarPageLink;

    /**
     * web element for myAccount page link.
     */
    @FindBy(xpath = "//li[@id='account']/a")
    WebElement myAccountPageLink;

    /**
     * web element used to store settings page link.
     */
    @FindBy(xpath = "//a[@href='/settings.tmpl']")
    WebElement settingsPageLink;

    /**
     * instance variable to get webinar key.
     */
    protected String webinarKey;

    /**
     * Constructor to initialize property utils and localized message objects.
     */
    public BasePage() {
        this.propertyUtil = new PropertyUtil();
        this.locale =
                StringUtils.parseLocaleString(this.propertyUtil.getProperty("environment.locale"));
        this.messages = new ResourceBundleMessageSource();
        this.messages.setBasenames(new String[]{"messages/attendee/messages", "messages/broker/messages-g2w"});
        this.brokerMessages = new ResourceBundleMessageSource();
        this.brokerMessages.setBasenames(new String[]{"messages/broker/messages-g2w"});
    }

    /**
     * Method to age webinar timings by subtracting the milli seconds given as a
     * parameter.
     *
     * @param connection
     *            (Connection object)
     * @param subtractMillis
     *            (milli seconds to be subtracted)
     */
    public void ageWebinar(final Connection connection, final long subtractMillis) {
        this.logger.log("Aging the webinar by milli seconds : " + subtractMillis);
        try {
            PreparedStatement stmt = null;
            String query =
                    "update webinartimes set startdate = startdate - " + subtractMillis
                    + ", enddate = enddate - " + subtractMillis + " where webinarKey = "
                    + this.webinarKey;
            try {
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Method : Navigate to Generate report page.
     *
     * @return GenerateReportsPage Object
     */
    public GenerateReportsPage gotoGenerateReportsPage() {
        this.generateReportsPageLink.click();
        this.logger.logWithScreenShot("Navigate to Generate Report Page", this.driver);
        return new GenerateReportsPage(this.driver);
    }

    /**
     * Method to age webinar timings by subtracting the milli seconds given as a
     * parameter for a particular webinar.
     *
     * @param connection
     *            (Connection object)
     * @param subtractMillis
     *            (milli seconds to be subtracted)
     * @param webinarKey
     *            (webinar key)
     */
    public void ageWebinar(final Connection connection, final long subtractMillis,
            final Long webinarKey) {
        this.logger.log("Aging the webinar by milli seconds : " + subtractMillis);
        try {
            PreparedStatement stmt = null;
            String query =
                    "update webinartimes set startdate = startdate - " + subtractMillis
                    + ", enddate = enddate - " + subtractMillis + " where webinarKey = "
                    + webinarKey;
            try {
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to age webinar timings after running session by subtracting the
     * milli seconds given as a parameter. This is done in Active schema.
     * <ol>
     * Tables updated are :
     * <li>WEBINARTIMES</li>
     * <li>MEETINGS</li>
     * <li>MEETINGTIMES</li>
     * <li>MEETINGSESSIONINFO</li>
     * <li>COMMSESSIONS</li>
     * </ol>
     *
     * @param connection
     *            (Connection object)
     * @param hrsToAge
     *            (hrs to age)
     * @param webinarKey
     *            (webinar Key)
     * @param webinarId
     *            (webinar id)
     */
    public void ageWebinarTimesInActiveSchema(final Connection connection, final long hrsToAge,
            final Long webinarKey, final Long webinarId) {
        this.logger.log("Aging the webinar after running the session by : " + -(hrsToAge) + "hrs");
        Long milliSecondsToAge = hrsToAge * 3600000;
        try {

            PreparedStatement stmt = null;
            String query = "";

            try {

                this.logger.log("Update the WebinarTimes in Active DB to age the webinar times");
                query =
                        "update webinartimes set startdate = startdate - " + milliSecondsToAge
                        + ", enddate = enddate - " + milliSecondsToAge
                        + " where webinarKey = " + webinarKey;
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

                // wait for 5 sec to reflect the txn's
                Thread.sleep(5000L);

                this.logger.log("Update time fields in MEETINGS in Active DB");
                stmt = null;
                query =
                        "update meetings set scheduledstarttime = scheduledstarttime - "
                                + milliSecondsToAge + ", scheduledendtime = scheduledendtime - "
                                + milliSecondsToAge + ", lastusetime = lastusetime -"
                                + milliSecondsToAge + ", expirationtime = expirationtime - "
                                + milliSecondsToAge + " where amid = " + webinarId;
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

                // wait for 5 sec to reflect the txn's
                Thread.sleep(5000L);

                this.logger.log("Update time fields in MEETINGTIMES in Active DB");
                stmt = null;
                query =
                        "update meetingtimes mt "
                                + "set mt.scheduledstarttime = mt.scheduledstarttime - "
                                + milliSecondsToAge
                                + ", mt.scheduledendtime = mt.scheduledendtime - "
                                + milliSecondsToAge
                                + " where exists (select 1 from meetings m where m.amid = "
                                + webinarId + " AND m.meetingkey = mt.meetingkey)";
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

                // wait for 5 sec to reflect the txn's
                Thread.sleep(5000L);

                this.logger.log("Update time fields in MEETINGSESSIONINFO in Active DB");
                stmt = null;
                query =
                        "update meetingsessioninfo ms set ms.starttime = ms.starttime - "
                                + milliSecondsToAge
                        + ", ms.endtime = ms.endtime - "
                        + milliSecondsToAge
                                + " where exists (select 1 from meetings m where m.amid = "
                                + webinarId + " AND m.meetingkey = ms.meetingkey)";
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

                // wait for 5 sec to reflect the txn's
                Thread.sleep(5000L);

                this.logger.log("Update time fields in COMMSESSIONS in Active DB");
                stmt = null;
                query =
                        "update commsessions cs set cs.createtime =  cs.createtime - "
                                + milliSecondsToAge
                                + ", cs.deletetime =  cs.deletetime - "
                                + milliSecondsToAge
                                + " where exists (select 1 from meetings m, meetingsessioninfo ms "
                                + "where m.amid = "
                                + webinarId
                                + " AND m.meetingkey = ms.meetingkey and ms.sessionkey = cs.sessionkey)";
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to age webinar timings after running session by subtracting the
     * milli seconds given as a parameter. This is done in Historical schema.
     * <ol>
     * Tables updated are :
     * <li>WEBINARTIMEEVENTS</li>
     * <li>ORGANIZERSESSIONEVENTS</li>
     * <li>WEBINARSESSIONENDS</li>
     * <li>WEBINARSESSIONSTARTS</li>
     * </ol>
     *
     * @param connection
     *            (Connection object)
     * @param hrsToAge
     *            (hrs to age)
     * @param webinarKey
     *            (webinar Key)
     * @param webinarId
     *            (webinar id)
     */
    public void ageWebinarTimesInHistoricSchema(final Connection connection, final long hrsToAge,
            final Long webinarKey, final Long webinarId) {
        this.logger.log("Aging the webinar after running the session by : " + -(hrsToAge) + "hrs");
        Long milliSecondsToAge = hrsToAge * 3600000;
        try {

            PreparedStatement stmt = null;
            String query = "";

            try {

                this.logger.log("Update the WebinarTimeEvents in Historic DB");
                query =
                        "update WEBINARTIMEEVENTS set startdate = startdate - "
                                + milliSecondsToAge + ", enddate = enddate - " + milliSecondsToAge
                                + " where webinarKey = " + webinarKey;
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

                // wait for 5 sec to reflect the txn's
                Thread.sleep(5000L);

                this.logger.log("Update time fields in ORGANIZERSESSIONEVENTS in Historic DB");
                stmt = null;
                query =
                        "update ORGANIZERSESSIONEVENTS "
                                + "set sessionstarttime = sessionstarttime - " + milliSecondsToAge
                                + ", sessionendtime = sessionendtime - " + milliSecondsToAge
                                + " where amid = " + webinarId;
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

                // wait for 5 sec to reflect the txn's
                Thread.sleep(5000L);

                this.logger.log("Update time fields in WebinarSessionEnds in Historic DB");
                stmt = null;
                query =
                        "update webinarsessionends set sessionendtime = sessionendtime - "
                                + milliSecondsToAge + " where amid = " + webinarId;
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

                // wait for 5 sec to reflect the txn's
                Thread.sleep(5000L);

                this.logger.log("Update time fields in WebinarSessionStarts in Historic DB");
                stmt = null;
                query =
                        "update webinarsessionstarts ts "
                                + "set ts.sessionstarttime = ts.sessionstarttime - "
                                + milliSecondsToAge
                                + " where exists (select 1 from ORGANIZERSESSIONEVENTS te where te.amid = "
                                + webinarId + " AND te.sessionkey = ts.sessionkey)";
                stmt = connection.prepareStatement(query);
                stmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to change time zone on registration page.
     *
     * @param timeZone
     *            (time zone name)
     */
    public void changeTimeZone(final String timeZone) {
        // click time zone button
        this.findVisibleElement(By.id("show-tz-btn")).click();
        this.logger.logWithScreenShot("time zone light box:", this.driver);
        Select dropdown = new Select(this.driver.findElement(By.id("timezone")));
        dropdown.selectByValue(timeZone);
        // click set timeZone
        this.findVisibleElement(By.id("timezone-modal-set")).click();
        // sleep for making changes available to page
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to clear element and enter text.
     *
     * @param elements
     *            (web element to clear)
     * @param text
     *            (value to send)
     */
    public void clearAndType(final WebElement elements, final String text) {
        elements.clear();
        elements.sendKeys(text);
    }

    /**
     * Method to close page
     */
    public void close() {
        this.driver.quit();
    }

    /**
     * Method used to compute date time text to desired date time text.
     * <ol>
     * <li>Input Format: Feb 02, 2014 11:00 PM PST</li>
     * <li>Output Format : Thu, Jan 9, 2014 11:00 PM - Fri, Jan 10, 2014 12:00
     * AM PST</li>
     * </ol>
     *
     * @param dateTimeText
     *            (date and time in text format)
     * @return expectedDateTimeText
     */
    public String computeDateTimeForRecurringWebinar(final String dateTimeText) {

        // Convert date time string to date time object and get the start & end
        // day of month.
        String timeZone = this.propertyUtil.getProperty("environment.timezone");
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date startDate =
                this.convertDateTextToObject(this.messages.getMessage(
                        "manageWebinar.date.format.individualSession.start", null, this.locale),
                        dateTimeText, timeZone);
        c.setTime(startDate);
        String startDayOfMonth =
                this.convertDateObjectToText(this.messages.getMessage(
                        "date.format.registrant.start.long.formatted", null, this.locale), c
                        .getTime(), timeZone);
        c.add(Calendar.HOUR_OF_DAY, 1);
        Date endDate = c.getTime();
        String endDayOfMonth =
                this.convertDateObjectToText(this.messages.getMessage(
                        "date.format.registrant.start.long.formatted", null, this.locale), endDate,
                        timeZone);

        // Convert start date time object to desired string format.
        String formattedStartDate =
                this.convertDateObjectToText(this.messages.getMessage(
                        "manageWebinar.date.format.startAndEndDate.start", null, this.locale),
                        startDate, timeZone);

        // condition to check whether end date fall in same day or another day
        // and pick date format accordingly and convert end date to string
        // format
        String formattedEndDate = "";
        if (Integer.parseInt(startDayOfMonth) != Integer.parseInt(endDayOfMonth)) {
            formattedEndDate =
                    this.convertDateObjectToText(this.messages.getMessage(
                            "manageWebinar.date.format.startAndEndDate.diffDay.end", null,
                            this.locale), endDate, timeZone);
        } else {
            formattedEndDate =
                    this.convertDateObjectToText(this.messages.getMessage(
                            "manageWebinar.date.format.startAndEndDate.sameDay.end", null,
                            this.locale), endDate, timeZone);
        }

        // Get the expected date and time in string format.
        String expectedDateTimeText = formattedStartDate.trim() + " - " + formattedEndDate;
        return expectedDateTimeText;
    }

    /**
     * Method used to convert date time text to desired date time text along
     * with specified time zone.
     * <ol>
     * <li>Input Format: Fri, Jan 10, 2014 9:00 PM - 10:00 PM PST</li>
     * <li>Input Format : Thu, Jan 9, 2014 11:00 PM - Fri, Jan 10, 2014 12:00 AM
     * PST</li>
     * <li>Input Time Zone Format: US/Pacific (for pacific region)</li>
     * <li>Output Format: Friday, January 10, 2014 12:00 AM - 1:00 AM PST</li>
     * <li>Output Format: Thursday, January 9, 2014 11:00 PM - Friday, January
     * 10, 2014 12:00 AM PST</li>
     * </ol>
     *
     * @param dateTimeText
     *            (date and time in text format)
     * @param toTimeZone
     *            (conversion time zone id)
     * @return expectedDateTimeText
     */
    public String computeWebinarDurationInTimeZone(final String dateTimeText,
            final String toTimeZone) {

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(toTimeZone));
        int timeZonePosition = dateTimeText.lastIndexOf(" ");
        String timeZoneText = dateTimeText.substring(timeZonePosition + 1);
        String[] dateArray = dateTimeText.split("-");
        String startDateTimeText =
                this.convertDateTextBasedOnPattern(this.messages.getMessage(
                        "date.format.registrant.start.long", null, this.locale), dateArray[0]
                                .trim()
                                + " " + timeZoneText.trim(), toTimeZone);
        String endDateTimeText = "";
        if (dateArray[1].length() <= 13) {
            endDateTimeText =
                    this.convertDateTextBasedOnPattern(this.messages.getMessage(
                            "date.format.registrant.start.short", null, this.locale),
                            startDateTimeText, toTimeZone);
            endDateTimeText = endDateTimeText + " " + dateArray[1].trim();
        } else {
            endDateTimeText = dateArray[1].trim();
        }
        Date startDate =
                this.convertDateTextToObject(this.messages.getMessage(
                        "date.format.registrant.start.long", null, this.locale), startDateTimeText,
                        toTimeZone);
        c.setTime(startDate);
        String startDayOfMonth =
                this.convertDateObjectToText(this.messages.getMessage(
                        "date.format.registrant.start.long.formatted", null, this.locale), c
                        .getTime(), toTimeZone);
        Date endDate =
                this.convertDateTextToObject(this.messages.getMessage(
                        "date.format.registrant.start.long", null, this.locale), endDateTimeText,
                        toTimeZone);
        String endDayOfMonth = "";
        if (startDate.after(endDate)) {
            c.setTime(endDate);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endDayOfMonth =
                    this.convertDateObjectToText(this.messages.getMessage(
                            "date.format.registrant.start.long.formatted", null, this.locale), c
                            .getTime(), toTimeZone);
        } else {
            c.setTime(endDate);
            endDayOfMonth =
                    this.convertDateObjectToText(this.messages.getMessage(
                            "date.format.registrant.start.long.formatted", null, this.locale), c
                            .getTime(), toTimeZone);
        }
        String formattedStartDate =
                this.convertDateObjectToText(this.messages.getMessage(
                        "date.format.registrant.start.long", null, this.locale), startDate,
                        toTimeZone);
        String formattedEndDate = "";
        if ((Integer.parseInt(startDayOfMonth) != Integer.parseInt(endDayOfMonth))) {
            formattedEndDate =
                    this.convertDateObjectToText(this.messages.getMessage(
                            "date.format.registrant.end.diffDay", null, this.locale), endDate,
                            toTimeZone);
        } else {
            formattedEndDate =
                    this.convertDateObjectToText(this.messages.getMessage(
                            "date.format.registrant.end.sameDay", null, this.locale), endDate,
                            toTimeZone);
        }

        // Get the expected date and time in string format.
        String expectedDateTimeText =
                formattedStartDate.substring(0, formattedStartDate.lastIndexOf(" ")).trim() + " - "
                        + formattedEndDate;
        return expectedDateTimeText;
    }

    /**
     * Method used to convert date object to string date format.
     *
     * @param pattern  (The pattern of the date time parameter)
     * @param dateTime (Date and time format to format)
     * @param timeZone (time zone)
     * @return formattedDate (formatted date in string)
     */
    public String convertDateObjectToText(final String pattern, final Date dateTime,
            final String timeZone) {
        String formattedDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, this.locale);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        try {
            formattedDate = sdf.format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    /**
     * Method to convert date text based on pattern.
     *
     * @param pattern
     *            (The pattern of the date time parameter)
     * @param dateTimeString
     *            (Date and time format in String)
     * @param timeZone
     *            (time zone)
     * @return formatted date based on pattern
     */
    public String convertDateTextBasedOnPattern(final String pattern, final String dateTimeString,
            final String timeZone) {
        Date date = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, this.locale);
        try {
            date = sdf.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTimeZone(TimeZone.getTimeZone(timeZone));
        c.setTime(date);
        return sdf.format(c.getTime());
    }

    /**
     * Method used to convert string date format to date object.
     *
     * @param pattern
     *            (The pattern of the date time string parameter)
     * @param dateTimeString
     *            (Date and time format in String)
     * @param timeZone
     *            (time zone)
     * @return Date (date object)
     */
    public Date convertDateTextToObject(final String pattern, final String dateTimeString,
            final String timeZone) {
        Date date = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, this.locale);
        try {
            date = sdf.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTimeZone(TimeZone.getTimeZone(timeZone));
        c.setTime(date);
        return c.getTime();
    }

    /**
     * Method used to convert date time object to string date format.
     *
     * @param pattern  (The pattern of the date time parameter)
     * @param dateTime (Date and time format to format)
     * @return formattedDate (formatted date in string)
     */
    public String convertDateTimeObjectToText(final String pattern, final DateTime dateTime) {
        DateTimeFormatter dateFormat = DateTimeFormat.forPattern(pattern);
        String formattedDate = dateFormat.withLocale(this.locale).print(dateTime);
        return formattedDate;
    }

    /**
     * Method used to convert string date format to date time object.
     *
     * @param pattern
     *            (The pattern of the date time string parameter)
     * @param dateTimeString
     *            (Date and time format in String)
     * @return dateTime (date time object)
     */
    public DateTime convertDateTimeTextToObject(final String pattern, final String dateTimeString) {
        DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
        DateTime dateTime = format.withLocale(this.locale).parseDateTime(dateTimeString);
        return dateTime;
    }

    /**
     * Method to find clickable element.
     *
     * @param by
     *            (by element)
     * @return (clickable element)
     */
    public WebElement findClickableElement(final By by) {
        WebElement clickableElement;
        try {
            clickableElement =
                    (new WebDriverWait(this.driver, this.DEFAULT_TIMEOUT)).until(ExpectedConditions
                            .elementToBeClickable(by));
        } catch (Throwable cause) {
            String errorMessage = "Could not find clickable element: " + by.toString();
            this.logger.logWithScreenShot("findClickableElement failed.", this.driver);
            throw new RuntimeException(errorMessage);
        }
        return clickableElement;
    }

    /**
     * Method to find presence of the element in page.
     * 
     * @param by
     *            (by element to be visible in page)
     * @param timeoutInSeconds
     *            (timeout value in seconds)
     * @return presenceOfElement (Web element object of the presence element)
     */
    public WebElement findPresenceOfElement(final By by, final int timeoutInSeconds) {
        WebElement presenceOfElement;
        try {
            presenceOfElement = (new WebDriverWait(this.driver, timeoutInSeconds))
                    .until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            String errorMessage = "Could not find presence of element: " + by.toString();
            this.logger.logWithScreenShot("Could not find presence of element: " + by.toString(), this.driver);
            throw new RuntimeException(errorMessage);
        }
        return presenceOfElement;
    }

    /**
     * Method to find presence of the elements in page.
     * 
     * @param by
     *            (by element to be visible in page)
     * @param timeoutInSeconds
     *            (timeout value in seconds)
     * @return presenceOfElement (Web element object of the presence element)
     */
    public List<WebElement> findPresenceOfElements(final By by, final int timeoutInSeconds) {
        List<WebElement> presenceOfElements;
        try {
        	presenceOfElements = (new WebDriverWait(this.driver, timeoutInSeconds))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        } catch (Exception e) {
            String errorMessage = "Could not find presence of elements: " + by.toString();
            this.logger.logWithScreenShot("Could not find presence of elements: ", this.driver);
            throw new RuntimeException(errorMessage);
        }
        return presenceOfElements;
    }
    
    /**
     * Method to find presence of inner text for the elements in page.
     * Can be used when elements vissibiliy is changed dynamically to make sure
     * that inner text is vissible.
     * @param by
     * @param timeoutInSeconds
     * @return presenceOfElements
     */
    public List<WebElement> findPresenceOfInnerTextForElements(final By by, final int timeoutInSeconds) {
        List<WebElement> presenceOfElements;
        final int waitForElement = timeoutInSeconds/5;
        try {
        	presenceOfElements = (new WebDriverWait(this.driver, timeoutInSeconds))
                    .until(new ExpectedCondition<List<WebElement>>() {
                        @Override
                        public List<WebElement> apply(WebDriver driver) {
                            try {
                              List<WebElement> elements = findPresenceOfElements(by, waitForElement);
                              for(WebElement element : elements) {
                              	if(!StringUtils.hasText(element.getText())) {
                              	    return null;
                              	}
                              }                          
                              return elements.size() > 0 ? elements : null;
                            } catch (Throwable e) {
                                return null;
                            }
                        }					
                    });
        } catch (Exception e) {
            this.logger.logWithScreenShot("Could not find presence of elements: " + by.toString(), this.driver);
            throw new RuntimeException(e);
        }
        return presenceOfElements;
    }
    
    
    /**
     * Method to find visible element.
     *
     * @param by
     *            (by element)
     * @return visibleElement (visible element)
     */
    public WebElement findVisibleElement(final By by) {
        WebElement visibleElement;
        try {
            visibleElement =
                    (new WebDriverWait(this.driver, this.DEFAULT_TIMEOUT)).until(ExpectedConditions
                            .visibilityOfElementLocated(by));
        } catch (Throwable cause) {
            String errorMessage = "Could not find visible element: " + by.toString();
            this.logger.logWithScreenShot("findVisibleElement failed.", this.driver);
            throw new RuntimeException(errorMessage);
        }
        return visibleElement;
    }

    /**
     * Method to find visible element in the page.
     * 
     * @param by
     *            (by element to be visible in page)
     * @param timeoutInSeconds
     *            (timeout value in seconds)
     * @return visibleElement (Web element object of the visible element)
     */
    public WebElement findVisibleElement(final By by, final int timeoutInSeconds) {
        WebElement visibleElement;
        try {
            visibleElement = (new WebDriverWait(this.driver, timeoutInSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable clause) {
            String errorMessage = "Could not find visible element: " + by.toString();
            this.logger.logWithScreenShot("Could not find visible element: ", this.driver);
            throw new RuntimeException(errorMessage);
        }
        return visibleElement;
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
     * Method to get aged date time.
     *
     * @param dateTime
     *            (date time)
     * @return agedDateTime
     */
    public DateTime getAgedDateTime(final DateTime dateTime) {
        DateTimeZone californiaTimeZone =
                DateTimeZone.forID(this.propertyUtil.getProperty("environment.timezone"));
        DateTime agedDateTime = this.getGMTDateTime(dateTime);
        agedDateTime = new DateTime(agedDateTime, californiaTimeZone);
        return agedDateTime;
    }

    /**
     * Method to get aged date time.
     *
     * @param numberOfDays
     *            (number of days to age)
     * @return agedDateTime
     */
    public DateTime getAgedDateTime(final int numberOfDays) {
        DateTimeZone californiaTimeZone =
                DateTimeZone.forID(this.propertyUtil.getProperty("environment.timezone"));
        DateTime agedDateTime = this.getGMTDateTime().plusDays(numberOfDays);
        agedDateTime = new DateTime(agedDateTime, californiaTimeZone);
        return agedDateTime;
    }

    /**
     * Method used to get current date and time.
     *
     * @return currentDate (current date and time)
     */
    public DateTime getDateTime() {
        DateTimeZone californiaTimeZone =
                DateTimeZone.forID(this.propertyUtil.getProperty("environment.timezone"));
        DateTime currentDate = new DateTime(this.getGMTDateTime(), californiaTimeZone);
        return currentDate;
    }

    /**
     * Method to create date and time object based on input. By default 1 hour
     * will be added if hrs parameter is 0.
     *
     * @param days
     *            (create date and time object based on number of days)
     * @param hrs
     *            (create date and time object based on number of hours)
     * @param mins
     *            (create date and time object based on number of minutes)
     * @return startDate (return date and time object based on the parameters)
     */
    public DateTime getDateTime(final int days, final int hrs, final int mins) {
        DateTime startDate = this.getDateTime();
        if (startDate.getMinuteOfHour() < 60) {
            startDate =
                    this.getAgedDateTime(startDate.plusDays(days)).plusHours(hrs + 1)
                    .withMinuteOfHour(mins).withSecondOfMinute(0).withMillisOfSecond(0);
        }
        return startDate;
    }

    /**
     * Method used to get expected date format based on the date time text in
     * manage webinar page.
     * <ol>
     * <li>Input Format: Fri, Jan 10, 2014 9:00 PM - 10:00 PM PST</li>
     * <li>Input Format : Thu, Jan 9, 2014 11:00 PM - Fri, Jan 10, 2014 12:00 AM
     * PST</li>
     * <li>Output Format: Friday, January 10, 2014 12:00 AM - 1:00 AM PST</li>
     * <li>Output Format: Thursday, January 9, 2014 11:00 PM - Friday, January
     * 10, 2014 12:00 AM PST</li>
     * </ol>
     *
     * @param dateTimeText
     *            (date and time in text format)
     * @return expectedDateTimeText
     */
    public String getDateTimeAsText(final String dateTimeText) {

        int timeZonePosition = dateTimeText.lastIndexOf(" ");
        String timeZoneText = dateTimeText.substring(timeZonePosition + 1);

        // Split date time string to construct desired format.
        String[] dateTimeArray = dateTimeText.split("-");

        // Convert date time string to date time object and get the start day
        // of month.
        String timeZone = this.propertyUtil.getProperty("environment.timezone");
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date startDate =
                this.convertDateTextToObject(this.messages
                        .getMessage("manageWebinar.date.format.startAndEndDate.short.start", null,
                                this.locale), dateTimeArray[0].trim() + " " + timeZoneText,
                                timeZone);
        c.setTime(startDate);
        String startDayOfMonth =
                this.convertDateObjectToText(this.messages.getMessage(
                        "date.format.registrant.start.long.formatted", null, this.locale), c
                        .getTime(), timeZone);
        Date endDate = null;
        String endDayOfMonth = startDayOfMonth;
        if (dateTimeArray[1].trim().length() > 12) {
            endDate =
                    this.convertDateTextToObject(this.messages.getMessage(
                            "manageWebinar.date.format.startAndEndDate.diffDay.end", null,
                            this.locale), dateTimeArray[1].trim(), timeZone);
            endDayOfMonth =
                    this.convertDateObjectToText(this.messages.getMessage(
                            "date.format.registrant.start.long.formatted", null, this.locale),
                            endDate, timeZone);
        }

        // Convert start date time object to desired string format.
        String formattedStartDate =
                this.convertDateObjectToText(
                        this.messages.getMessage(
                                "manageWebinar.date.format.startAndEndDate.long.start", null,
                                this.locale), startDate, timeZone);

        // condition to check whether end date fall in same day or another day
        // and pick date format accordingly and convert end date to string
        // format
        String formattedEndDate = dateTimeArray[1].trim();
        if (Integer.parseInt(startDayOfMonth) != Integer.parseInt(endDayOfMonth)) {
            formattedEndDate =
                    this.convertDateObjectToText(this.messages.getMessage(
                            "manageWebinar.date.format.startAndEndDate.long.diffDay.end", null,
                            this.locale), endDate, timeZone);
        }

        // Get the expected date and time in string format.
        String expectedDateTimeText = formattedStartDate + " - " + formattedEndDate;
        return expectedDateTimeText;
    }

    /**
     * Method to return web driver object.
     *
     * @return (driver object)
     */
    public WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Method to get GMT date time.
     *
     * @return gmtDateTime
     */
    public DateTime getGMTDateTime() {
        DateTimeZone gmtTimeZone = DateTimeZone.forID("GMT");
        DateTime gmtDateTime = new DateTime(gmtTimeZone);
        return gmtDateTime;
    }

    /**
     * Method to get GMT date time.
     *
     * @param dateTime
     *            (date time to convert to GMT date time)
     * @return gmtDateTime
     */
    public DateTime getGMTDateTime(final DateTime dateTime) {
        DateTimeZone gmtTimeZone = DateTimeZone.forID("GMT");
        DateTime gmtDateTime = new DateTime(dateTime, gmtTimeZone);
        return gmtDateTime;
    }

    /**
     * Method to get header title for page.
     *
     * @return headerTitle
     */
    public String getHeaderTitleForPage() {
        String headerTitle = this.driver.findElement(this.headerTitle).getText();
        this.logger.log("Current page header title is : " + headerTitle);
        return headerTitle;
    }

    /**
     * Method to get list of time zones
     *
     * @return timeZonesList
     */
    public List<String> getListOfTimeZones() {
        List<String> timeZonesList = new LinkedList<String>();
        timeZonesList.add("Pacific/Samoa");
        timeZonesList.add("America/Anchorage");
        timeZonesList.add("America/Los_Angeles");
        timeZonesList.add("America/New_York");
        timeZonesList.add("America/Bogota");
        timeZonesList.add("America/Indianapolis");
        timeZonesList.add("America/Caracas");
        timeZonesList.add("America/Halifax");
        /* commented due to failure in bamboo */
        // timeZonesList.add("America/Santiago");
        timeZonesList.add("America/St_Johns");
        timeZonesList.add("America/Sao_Paulo");
        timeZonesList.add("America/Buenos_Aires");
        timeZonesList.add("Atlantic/Azores");
        timeZonesList.add("Atlantic/Cape_Verde");
        /* Commented out due to DST for 2014 but g2w shows no DST */
        // timeZonesList.add("Africa/Casablanca");
        timeZonesList.add("Europe/London");
        timeZonesList.add("Africa/Malabo");
        timeZonesList.add("Europe/Brussels");
        timeZonesList.add("Europe/Amsterdam");
        timeZonesList.add("Europe/Prague");
        timeZonesList.add("Europe/Warsaw");
        timeZonesList.add("Asia/Jerusalem");
        timeZonesList.add("Europe/Bucharest");
        timeZonesList.add("Europe/Athens");
        timeZonesList.add("Africa/Harare");
        /* Commented due to no DST but shows DST in g2w */
        // timeZonesList.add("Africa/Cairo");
        timeZonesList.add("Europe/Helsinki");
        timeZonesList.add("Asia/Baghdad");
        timeZonesList.add("Asia/Kuwait");
        timeZonesList.add("Africa/Nairobi");
        timeZonesList.add("Asia/Tehran");
        timeZonesList.add("Asia/Tbilisi");
        timeZonesList.add("Asia/Muscat");
        timeZonesList.add("Asia/Kabul");
        /* Commented due to no DST but shows DST in g2w */
        // timeZonesList.add("Asia/Karachi");
        timeZonesList.add("Asia/Calcutta");
        timeZonesList.add("Asia/Katmandu");
        timeZonesList.add("Asia/Rangoon");
        timeZonesList.add("Asia/Jakarta");
        timeZonesList.add("Asia/Bangkok");
        timeZonesList.add("Asia/Taipei");
        timeZonesList.add("Asia/Singapore");
        timeZonesList.add("Asia/Shanghai");
        timeZonesList.add("Asia/Seoul");
        timeZonesList.add("Asia/Tokyo");
        timeZonesList.add("Pacific/Guam");
        timeZonesList.add("Pacific/Auckland");
        timeZonesList.add("Pacific/Fiji");
        timeZonesList.add("Pacific/Tongatapu");
        timeZonesList.add("Asia/Colombo");
        /* commented due to failure in bamboo */
        // timeZonesList.add("America/Chicago");
        timeZonesList.add("America/Mexico_City");
        timeZonesList.add("America/Phoenix");
        /* Commented out due to DST for 2014 but g2w shows no DST */
        // timeZonesList.add("America/Denver");
        timeZonesList.add("Pacific/Honolulu");
        return timeZonesList;
    }

    /**
     * Method to return list of titles/get text for the given xpath.
     *
     * @param xpath
     *            (xpath of elements)
     * @return List<String> list of titles of WebElements
     */
    public List<String> getListOfTitlesForElementsWithGivenXPath(final String xpath) {
        this.logger.log("To get list of existing elements having xpath : " + xpath);
        List<String> listOfWebElementTitle = new ArrayList<String>();
        List<WebElement> listOfElements = (new WebDriverWait(this.driver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
        for (WebElement element : listOfElements) {
            if (element.getText() != null && element.getText().length() > 0) {
                listOfWebElementTitle.add(element.getText());
            }
        }
        return listOfWebElementTitle;
    }

    /**
     * Method to get the Locale.
     *
     * @return Locale
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * Method to get the ResourceBundleMessageSource.
     *
     * @return ResourceBundleMessageSource
     */
    public ResourceBundleMessageSource getResourceBundleMessage() {
        return this.messages;
    }

    /**
     * Method to get the tile.
     *
     * @return title
     */
    public String getTitle() {
        String title = this.driver.getTitle();
        this.logger.log("Title of the page is : " + title);
        return title;
    }

    /**
     * Method used to create unique string.
     *
     * @return unique String
     */
    public String getUniqueString() {
        return new Date().getTime() + "-" + Thread.currentThread().getId();
    }

    /**
     * Method : Navigate to Schedule a Webinar page.
     *
     * @return ScheduleAWebinarPage Object
     */
    public ScheduleAWebinarPage gotoScheduleWebinarPage() {
    	
    	System.out.println("gotoschedulewebnarpage method");
    	
driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

        this.scheduleWebinarPageLink.click();
        this.logger.logWithScreenShot("Navigate to Schedule A Webinar Page", this.driver);
        return new ScheduleAWebinarPage(this.driver);
    }

    /**
     * Method : Navigate to My Account Page.
     *
     * @return MyAccountPage Object
     */
    public MyAccountPage gotoMyAccountPage() {
        this.myAccountPageLink.click();
        this.logger.logWithScreenShot("Navigate to My Account Page",
                this.driver);
        return new MyAccountPage(this.driver);
    }

    /**
     * Method : Navigate to GoToWebinar page.
     *
     */
    public void gotoWebinarPage() {
        this.gotoWebinarPageLink.click();
        this.logger.logWithScreenShot("Navigate to GoToWebinar Page",
                this.driver);

    }
    
    /**
     * Method : Navigate to Settings page.
     * 
     * @return SettingsPage Object
     */
    public SettingsPage gotoSettingsPage() {
        this.settingsPageLink.click();
        this.logger.logWithScreenShot("Navigate to Settings Page", this.driver);
        return new SettingsPage(this.driver);
    }

    /**
     * Method : Navigate to MyWebinars page.
     *
     */
    public MyWebinarsPage gotoMyWebinarsPage() {
        this.myWebinarPageLink.click();
        this.logger.logWithScreenShot("Navigate to GoToWebinar Page",
                this.driver);
        return new MyWebinarsPage(this.driver);
    }    
    /**
     * Specifies the amount of time the driver should wait.
     *
     * @param timeOut
     *            The amount of time to wait.
     * @param unit
     *            The unit of measure for time.
     */
    public void implicitlyWait(int timeOut, TimeUnit unit) {
        timeOut = (timeOut != 0 ? timeOut : this.DEFAULT_TIMEOUT);
        this.driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
    }

    /**
     * Method to logout of current account.
     */
    public void logOut() {
        this.findClickableElement(By.xpath("//a[@href='/j_spring_security_logout']")).click();
        // Wait for login form to show up, don't do anything with returned value
        this.findVisibleElement(By.id("loginForm"), this.DEFAULT_TIMEOUT);
    }

    /**
     * Method to navigate back.
     */
    public void navigateBack() {
        this.logger.log("Navigate back");
        this.driver.navigate().back();
        this.logger.logWithScreenShot("After navigating back", this.driver);
    }

    /**
     * Method to refresh page.
     */
    public void refreshPage() {
        this.logger.log("Refresh the page");
        this.driver.navigate().refresh();
    }

    /**
     * Method to select an option in drop down.
     *
     * @param optionValue
     *            (option value)
     * @param optionIndex
     *            (option index)
     * @param dropDownElemId
     *            (drop down element id to be selected)
     */
    public void selectDropDown(final String optionValue, final int optionIndex,
            final String dropDownElemId) {
        this.logger.log("Select option :" + optionValue);
        String driverType = this.propertyUtil.getProperty("driver");
        if (driverType.equalsIgnoreCase("firefox") || driverType.equalsIgnoreCase("ghostdriver")) {
            Select dropDown = new Select(this.driver.findElement(By.id(dropDownElemId)));
            dropDown.selectByValue(optionValue);
        } else {
            this.driver.findElement(By.id(dropDownElemId + "_trig")).click();
            String xpath = "//div[@id='" + dropDownElemId + "__menu']//li[" + optionIndex + "]";
            this.findVisibleElement(By.xpath(xpath)).click();
        }
        this.logger.logWithScreenShot("After selecting option", this.driver);
    }

    /**
     * Method to set the check box values.
     *
     * @param checkBoxElement
     *            (element of check box)
     * @param check
     *            (boolean to select check box)
     */
    public void setCheckBox(final WebElement checkBoxElement, final boolean check) {
        boolean isChecked = checkBoxElement.isSelected();
        if ((isChecked && !check) || (!isChecked && check)) {
            checkBoxElement.click();
        }
    }

    /**
     * Method to set the date value.
     *
     * @param inputId
     *            (id of an element)
     * @param date
     *            (date object)
     */
    public void setDate(final String inputId, final DateTime date) {
        DateTimeFormatter fmt =
                DateTimeFormat.forPattern(this.messages.getMessage(
                        "date.format.generateReport.startAndEnd", null, this.locale));
        ((JavascriptExecutor) this.driver).executeScript("document.getElementById('" + inputId
                + "').value = '" + fmt.withLocale(this.locale).print(date) + "'");
    }

    /**
     * Method used to set start and end dates for custom report generation.
     * 
     * @param startDate
     *            (Start date for custom report)
     * @param endDate
     *            (End date for custom report)
     */
    public void setDateRangeForReportGeneration(final DateTime startDate, final DateTime endDate) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(this.messages.getMessage(
                "date.format.generateReport.startAndEnd", null, this.locale));
        if (startDate != null) {
            ((JavascriptExecutor) this.driver)
            .executeScript("document.getElementById('from-date').value = '"
                    + fmt.withLocale(this.locale).print(startDate) + "'");
        }
        if (endDate != null) {
            ((JavascriptExecutor) this.driver)
            .executeScript("document.getElementById('to-date').value = '"
                    + fmt.withLocale(this.locale).print(endDate) + "'");
        }
    }

    /**
     * Method to switch window based on url.
     *
     * @param url
     *            (url of window to be switched to)
     */
    public void switchWindowBasedOnUrl(final String url) {
        for (String winHandle : this.driver.getWindowHandles()) {
            this.driver.switchTo().window(winHandle);
            if (this.driver.getCurrentUrl().contains(url)) {
                break;
            }
        }
        this.logger.logWithScreenShot("After switching window based on url : " + url, this.driver);
    }

    /**
     * Method to navigate to My Recordings page
     *
     * @return MyRecordingsPage (my recordings page object)
     */
    public MyRecordingsPage gotoMyRecordingsPage() {
        this.myRecordingsPageLink.click();
        this.logger.logWithScreenShot("Navigating to my recordings page", this.driver);
        return new MyRecordingsPage(this.driver);
    }
}
