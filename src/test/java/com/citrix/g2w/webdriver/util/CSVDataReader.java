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
package com.citrix.g2w.webdriver.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.testng.collections.Lists;

/**
 * 
 */
public class CSVDataReader implements SeleniumDataProvider {

    private static String cvsSplitBy = ",";

    /**
     * Method to read the csv file.
     * 
     * @param filePath
     *            (file path)
     * @throws IOException 
     */
    public Object[][] getSeleniumDataArray(final String filePath, final String sheetName) throws IOException {
        List<List> testReport = readFile(filePath);
        return toArray(testReport);
    }

    private static Object[][] toArray(List<List> testReport) {
        Object[][] report = new Object[testReport.size()][];
        int i = 0;
        for(List row : testReport) {
            int j = 0;
            report[i] = new Object[row.size()];
            for(Object cell : row) {
                report[i][j] = cell;
                j++;
            }
            i++;
        }
        return report;
    }
    
    /**
     * Method to read the file.
     * 
     * @param filePath
     *            (file path)
     * @throws IOException 
     */
    public List<List> readFile(final String filePath) throws IOException {
        String line = "";
        BufferedReader br = null;
        List<List> testReport = Lists.newArrayList();
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                List<String> rowData = Lists.newArrayList();
                String[] splitLineArray = line.split(cvsSplitBy);
                String append = "";
                boolean found = false;
                for (String splitVal : splitLineArray) {
                    if (splitVal.startsWith("\"") || found) {
                        String currentSplit = splitVal;
                        if(!found) {
                            currentSplit = currentSplit.substring(1);
                        }
                        append = append + currentSplit;
                        found = true;
                    }
                    if (splitVal.endsWith("\"")) {
                        splitVal = append.substring(0, append.length() - 1);
                        append = "";
                        found = false;
                    } else if (StringUtils.isNotEmpty(append)) {
                        append += cvsSplitBy;
                    }
                    if (!found) {
                        rowData.add(splitVal);
                    }
                }
                testReport.add(rowData);
            }
        } finally {
            if(br != null) {
                br.close();
            }
        }
        return testReport;
    }
}
