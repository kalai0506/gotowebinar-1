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
package com.citrix.g2w.webdriver.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 * 
 */

public class ReadCSVReport extends BasePage {

    String cvsSplitBy = ",";
    private final List<List> testReport = new ArrayList<List>();

    /**
     * Method to read the file.
     * 
     * @param filePath
     *            (file path)
     */
    public List<List> readFile(final String filePath) {
        this.logger.log("Absolute file path:" + filePath);
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                List rowData = new ArrayList();
                String[] splitLineArray = line.split(this.cvsSplitBy);
                String append = "";
                boolean found = false;
                for (String splitVal : splitLineArray) {
                    if (splitVal.startsWith("\"") || found) {
                        append = append + splitVal;
                        found = true;
                    }
                    if (splitVal.endsWith("\"")) {
                        splitVal = append;
                        append = "";
                        found = false;
                    }
                    if (!found) {
                        rowData.add(splitVal);
                    }
                }
                this.testReport.add(rowData);
            }
        } catch (Exception e) {
            String errorMessage = "Could not find the file:" + filePath;
            throw new RuntimeException(errorMessage);
        }
        return this.testReport;
    }
}
