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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**

 */
public class ScheduleAWebinarPage extends BasePage {

    public static enum WebinarType {
        REGULAR, SERIES, SEQUENCE
    };

    public static enum Frequency {
        DAILY, WEEKLY, MONTHLY, CUSTOM
    };

    /**
     * By element used to add another session.
     */
    private final By addAnother = By.id("addAnother");
    /**
     * Web element for end time am.
     */
    private WebElement endTimeAm;
    /**
     * Web element for end time am pm button.
     */
    @FindBy(xpath = "//a[@id='webinarTimesForm_dateTimes_0_endAmPm_trig']/span[1]")
    private WebElement endTimeAmPmBtn;
    /**
     * Web element for end time pm.
     */
    private WebElement endTimePm;
    /**
     * Web element for frequency button.
     */
    @FindBy(xpath = "//a[@id='recurrenceForm_recurs_trig']/span[1]")
    private WebElement frequencyBtn;
    /**
     * By element for register for individual session.
     */
    private final By regForIndividualSession = By.id("recurrenceForm.differentAttendees_true");
    /**
     * By element for register once for all sessions.
     */
    private final By regOnceForAllSessions = By.id("recurrenceForm.differentAttendees_false");
    /**
     * Web element for start time am.
     */
    private WebElement startTimeAm;
    /**
     * Web element for start time am pm button.
     */
    @FindBy(xpath = "//a[@id='webinarTimesForm_dateTimes_0_startAmPm_trig']/span[1]")
    private WebElement startTimeAmPmBtn;
    /**
     * Web element for start time pm.
     */
    private WebElement startTimePm;
    /**
     * Web element for submit button.
     */
    @FindBy(id = "schedule.submit.button")
    private WebElement submit;
    /**
     * Web element for webinar description.
     */
    @FindBy(id = "description")
    private WebElement webinarDescription;
    /**
     * Web element for webinar name.
     */
    @FindBy(id = "name")
    private WebElement webinarName;
    /**
     * Web element for regular webinar type
     */
    @FindBy(xpath = "//a[@href='#recur-none']")
    private WebElement regularType;
    /**
     * Web element for series webinar type
     */
    @FindBy(xpath = "//a[@href='#recur-custom']")
    private WebElement seriesType;
    /**
     * Web element for sequence webinar type
     */
    @FindBy(xpath = "//a[@href='#recur-course']")
    private WebElement sequenceType;

    /**
     * Overloaded Constructor to initialize web driver object, invoke schedule
     * webinar page and verify current page URL.
     * 
     * @param scheduleWebinarPageUrl
     *            (schedule webinar page url)
     * @param webDriver
     *            (web driver)
     */

    public ScheduleAWebinarPage(final String scheduleWebinarPageUrl, final WebDriver webDriver) {
        this.driver = webDriver;
        this.driver.get(scheduleWebinarPageUrl);
        Assert.assertTrue(webDriver.getCurrentUrl().contains("/schedulewebinar?BAT="));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Constructor to initialize web driver object and verify current page URL.
     * 
     * @param webDriver
     *            (web driver)
     */

    public ScheduleAWebinarPage(final WebDriver webDriver) {
        this.driver = webDriver;
        Assert.assertTrue(webDriver.getCurrentUrl().contains("schedule.tmpl"));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Sets the base starting date
     * @param baseDate DateTime
     */
    private void setBaseDate(final DateTime baseDate) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(this.messages.getMessage(
                "date.format.mask.dayInWeekMonthDayYear", null, this.locale));
        ((JavascriptExecutor) this.driver)
                .executeScript("document.getElementById('webinarTimesForm.dateTimes_0.baseDate').value = '"
                        + fmt.withLocale(this.locale).print(baseDate) + "'");
    }

    /**
     * Sets the ending date
     * @param endDate DateTime
     */
    private void setEndDate(final DateTime endDate) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(this.messages.getMessage(
                "date.format.mask.dayInWeekMonthDayYear", null, this.locale));
        ((JavascriptExecutor) this.driver)
                .executeScript("document.getElementById('recurrenceForm.endDate').value = '"
                        + fmt.withLocale(this.locale).print(endDate) + "'");
    }

    /**
     * Sets the starting time
     * @param startTime DateTime
     */
    private void setStartTime(DateTime startTime) {
        this.setTextBoxValue(
                "webinarTimesForm.dateTimes_0.startTime",
                DateTimeFormat.forPattern(
                        this.messages.getMessage(
                                "date.format.mask.hourminute", null,
                                this.locale)).print(startTime));
    }

    /**
     * Sets the ending time
     * @param endTime DateTime
     */
    private void setEndTime(DateTime endTime) {
        this.setTextBoxValue(
                "webinarTimesForm.dateTimes_0.endTime",
                DateTimeFormat.forPattern(
                        this.messages.getMessage(
                                "date.format.mask.hourminute", null,
                                this.locale)).print(endTime));
    }

    /**
     * Sets the AM/PM field for startTime
     * @param dateTime DateTime
     */
    private void setStartTimeAmPm(DateTime dateTime) {
        if(isAM(dateTime)) {
            setStartTimeAm();
        } else {
            setStartTimePm();
        }
    }

    /**
     * Sets the AM/PM field for endTime
     * @param dateTime DateTime
     */
    private void setEndTimeAmPm(DateTime dateTime) {
        if(isAM(dateTime)) {
            setEndTimeAm();
        } else {
            setEndTimePm();
        }
    }

    /**
     * Sets the AM field for startTime
     */
    private void setStartTimeAm() {
        this.startTimeAmPmBtn.click();
        this.startTimeAm = this.driver
                .findElement(By
                        .xpath("//div[@id='webinarTimesForm_dateTimes_0_startAmPm__menu']/ul/li[1]"));
        this.startTimeAm.click();
    }

    /**
     * Sets the PM field for startTime
     */
    private void setStartTimePm() {
        this.startTimeAmPmBtn.click();
        this.startTimePm = this.driver
                .findElement(By
                        .xpath("//div[@id='webinarTimesForm_dateTimes_0_startAmPm__menu']/ul/li[2]"));
        this.startTimePm.click();
    }

    /**
     * Sets the AM field for endTime
     */
    private void setEndTimeAm() {
        this.endTimeAmPmBtn.click();
        this.endTimeAm = this.driver
                .findElement(By
                        .xpath("//div[@id='webinarTimesForm_dateTimes_0_endAmPm__menu']/ul/li[1]"));
        this.endTimeAm.click();
    }

    /**
     * Sets the PM field for endTime
     */
    private void setEndTimePm() {
        this.endTimeAmPmBtn.click();
        this.endTimePm = this.driver
                .findElement(By
                        .xpath("//div[@id='webinarTimesForm_dateTimes_0_endAmPm__menu']/ul/li[2]"));
        this.endTimePm.click();
    }

    /**
     * Sets the webinar description
     * @param description String
     */
    private void setWebinarDescription(final String description) {
        if (!(description == null || description.equalsIgnoreCase(""))) {
            this.webinarDescription.sendKeys(description);
        }
    }

    /**
     * Sets the webinar name
     * @param name String
     */
    private void setWebinarName(final String name) {
        this.webinarName.sendKeys(name);
    }

    /**
     * Sets the webinar type
     * @param webinarType WebinarType
     */
    private void setWebinarType(final WebinarType webinarType) {
        switch(webinarType) {
            case REGULAR: this.regularType.click(); break;
            case SERIES: this.seriesType.click(); break;
            case SEQUENCE: this.sequenceType.click();
        }
    }

    /**
     * Checks if 12 hour format is used
     * @return true if 12 hour format is used
     */
    private boolean is12HourFormatUsed() {
        return !Boolean.valueOf(this.messages.getMessage("locale.uses.24hourtime", null,
                this.locale));
    }

    private boolean isAM(DateTime date) {
        return DateTimeFormat.forPattern("a").print(date).equalsIgnoreCase("AM");
    }

    /**
     * Method to schedule webinar with name and description.
     *
     * @param name
     *            (webinar name)
     * @param description
     *            (webinar description)
     * @return (manage webinar object)
     */
    public ManageWebinarPage scheduleWebinar(final String name, final String description) {
        this.logger.log("Scheduling webinar with name: " + name + " and description: " + description);
        setWebinarName(name);
        setWebinarDescription(description);

        this.submit.click();
        this.logger.logWithScreenShot("Scheduled webinar with name: " + name, this.driver);

        return new ManageWebinarPage(this.driver);
    }

    /**
     * Method to schedule a webinar based on name, description, start and end date.
     * 
     * @param name
     *            (webinar name)
     * @param description
     *            (webinar description)
     * @param startDate
     *            (webinar start date)
     * @param endDate
     *            (webinar end date)
     * @return ManageWebinarPage (manage webinar page object).
     */
    public ManageWebinarPage scheduleWebinar(final String name, final String description, final DateTime startDate, final DateTime endDate) {
        this.logger.log("Scheduling webinar with name: " + name + " and description: " + description);
        setWebinarName(name);
        setWebinarDescription(description);

        if (startDate != null) {
            setBaseDate(startDate);
            setStartTime(startDate);
            if (is12HourFormatUsed()) {
                setStartTimeAmPm(startDate);
            }
        }
        if (endDate != null) {
            setEndTime(endDate);
            if (is12HourFormatUsed()) {
                if (isAM(endDate)) {
                    setEndTimeAm();
                } else {
                    setEndTimePm();
                }
            }
        }

        this.submit.click();
        this.logger.logWithScreenShot("Scheduled webinar with name: " + name, this.driver);
        return new ManageWebinarPage(this.driver);
    }

    /**
     * Method to schedule webinar with name, description, frequency, webinar
     * type, start and end date, and end time.
     * 
     * @param name
     *            (webinar name)
     * @param description
     *            (webinar description)
     * @param frequency
     *            (meeting occurrence type)
     * @param webinarType
     *            (single, series or sequence)
     * @param startDate
     *            (webinar start date)
     * @param endDate
     *            (webinar end date)
     * @param endTime
     *            (webinar end time)
     * @return ManageWebinarPage (manage webinar object)
     * 
     */
    public ManageWebinarPage scheduleWebinar(final String name, final String description,
            final Frequency frequency, final WebinarType webinarType, final DateTime startDate,
            final DateTime endDate, final DateTime endTime) {
        this.logger.log("Scheduling webinar with name: " + name + " and description: " + description);
        setWebinarName(name);
        setWebinarDescription(description);
        setWebinarType(webinarType);

        if (startDate != null) {
            setBaseDate(startDate);
            setStartTime(startDate);
            if (is12HourFormatUsed()) {
                setStartTimeAmPm(startDate);
            }
        }
        this.frequencyBtn.click();
        switch(frequency) {
            case DAILY: this.findVisibleElement(By.xpath("//div[@id='recurrenceForm_recurs__menu']/ul/li[1]")) .click();
                break;
            case WEEKLY: this.findVisibleElement(By.xpath("//div[@id='recurrenceForm_recurs__menu']/ul/li[2]"))
                    .click();
            case MONTHLY: this.findVisibleElement(By.xpath("//div[@id='recurrenceForm_recurs__menu']/ul/li[3]"))
                    .click(); break;
            case CUSTOM: this.findVisibleElement(By.xpath("//div[@id='recurrenceForm_recurs__menu']/ul/li[4]"))
                    .click();
                this.findVisibleElement(this.addAnother).click();
        }

        if (endTime != null) {
            setEndTime(endTime);
            if (is12HourFormatUsed()) {
                setEndTimeAmPm(endTime);
            }
        }

        if (endDate != null
                && !frequency.equals(Frequency.CUSTOM)) {
            setEndDate(endDate);
        }

        this.submit.click();
        this.logger.logWithScreenShot("Scheduled webinar with name: " + name, this.driver);
        return new ManageWebinarPage(this.driver);
    }

    /**
     * Method used to publish values for corresponding text box Id.
     * 
     * @param id
     *            (get id of text box to publish value)
     * @param value
     *            (publish value for corresponding ID)
     */
    private void setTextBoxValue(final String id, final String value) {
        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        StringBuilder script = new StringBuilder();
        script.append("var element = document.getElementById('" + id + "');");
        script.append("element.value='" + value + "';");
        WebElement textBox = this.driver.findElement(By.id(id));
        if (this.driver instanceof HtmlUnitDriver) {
            js.executeScript(script.toString());
            textBox.sendKeys("");
        } else {
            script.append("element.dispatchEvent(new Event('change'))");
            js.executeScript(script.toString());
        }
    }
}
