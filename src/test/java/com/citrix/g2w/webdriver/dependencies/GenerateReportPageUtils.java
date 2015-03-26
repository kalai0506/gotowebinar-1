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
package com.citrix.g2w.webdriver.dependencies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.citrix.g2w.webdriver.pages.BasePage;

/**

 */
public class GenerateReportPageUtils extends BasePage {

    /**
     * Method used to return date range as hash map.
     * 
     * @return dateMap
     */
    public HashMap<String, Integer> getDateRangeAsHashmap() {
        HashMap<String, Integer> dateMap = new LinkedHashMap<String, Integer>();
        dateMap.put(this.messages.getMessage("allDateRange.yesterday", null, this.locale), -1);
        dateMap.put(this.messages.getMessage("allDateRange.last_7_days", null, this.locale), -7);
        dateMap.put(this.messages.getMessage("allDateRange.last_30_days", null, this.locale), -30);
        dateMap.put(this.messages.getMessage("allDateRange.last_90_days", null, this.locale), -90);
        dateMap.put(this.messages.getMessage("allDateRange.last_180_days", null, this.locale), -180);
        dateMap.put(this.messages.getMessage("allDateRange.last_365_days", null, this.locale), -365);
        dateMap.put(this.messages.getMessage("allDateRange.today", null, this.locale), 0);
        dateMap.put(this.messages.getMessage("allDateRange.tomorrow", null, this.locale), 1);
        dateMap.put(this.messages.getMessage("allDateRange.next_7_days", null, this.locale), 7);
        dateMap.put(this.messages.getMessage("allDateRange.next_30_days", null, this.locale), 30);
        dateMap.put(this.messages.getMessage("allDateRange.next_90_days", null, this.locale), 90);
        dateMap.put(this.messages.getMessage("allDateRange.next_180_days", null, this.locale), 180);
        dateMap.put(this.messages.getMessage("allDateRange.next_365_days", null, this.locale), 365);
        return dateMap;
    }

    /**
     * Method used to return date range in list.
     * 
     * @return dateList (list contain date range).
     */

    public List<Integer> getDateRangeAsList() {
        List<Integer> dateList = new ArrayList<Integer>();
        dateList.add(-1);
        dateList.add(-7);
        dateList.add(-30);
        // dateList.add(-90);
        dateList.add(-180);
        dateList.add(-365);
        dateList.add(0);
        dateList.add(1);
        dateList.add(7);
        dateList.add(30);
        // dateList.add(90);
        dateList.add(180);
        dateList.add(365);
        return dateList;
    }

    /**
     * Method used to move scheduled webinar based on the days in the list.
     * 
     * @return numOfDays (number of days to move scheduled webinar).
     */
    public List<Integer> getListOfDaysToMoveScheduledWebinar() {
        List<Integer> numOfDays = new ArrayList<Integer>();
        numOfDays.add(-1);
        numOfDays.add(-7);
        numOfDays.add(-30);
        numOfDays.add(-60);
        numOfDays.add(-90);
        numOfDays.add(0);
        numOfDays.add(1);
        numOfDays.add(7);
        numOfDays.add(30);
        numOfDays.add(90);
        numOfDays.add(180);
        numOfDays.add(365);
        return numOfDays;
    }
}
