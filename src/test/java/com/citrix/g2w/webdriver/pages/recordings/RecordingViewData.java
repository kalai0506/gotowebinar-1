/*
 * Copyright (c) 1998-2014 Citrix Online LLC
 * All Rights Reserved Worldwide.
 *
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO CITRIX ONLINE
 * AND CONSTITUTES A VALUABLE TRADE SECRET.  Any unauthorized use,
 * reproduction, modification, or disclosure of this program is
 * strictly prohibited.  Any use of this program by an authorized
 * licensee is strictly subject to the terms and conditions,
 * including confidentiality obligations, set forth in the applicable
 * License and Co-Branding Agreement between Citrix Online LLC and
 * the licensee.
 */

package com.citrix.g2w.webdriver.pages.recordings;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

/**
 * @author sudip
 * @since: 4/29/14
 */
public class RecordingViewData {

    private String name;

    private String email;

    private DateTime viewTime;

    public RecordingViewData(String name, String email, String date, String time, String pattern, Locale locale) {
        date = date.trim();
        time = time.trim();
        pattern = pattern.trim();
        this.name = name;
        this.email = email;

        // Ignoring time zone data for tests since currently no good way to parse timezone unambiguously
        this.viewTime = DateTime.parse(date + " " + time.substring(0, time.lastIndexOf(" ")),
                DateTimeFormat.forPattern(pattern.substring(0, pattern.lastIndexOf(" "))).withLocale(locale));
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public DateTime getViewTime() {
        return viewTime;
    }

}
