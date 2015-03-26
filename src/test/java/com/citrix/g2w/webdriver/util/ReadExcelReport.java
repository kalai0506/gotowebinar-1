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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 * 
 */

public class ReadExcelReport extends BasePage {

    /**
     * Instance map variable used to map work sheets content.
     */
    private final Map<Integer, List> workSheetsContent = new HashMap<Integer, List>();

    /**
     * Method to get the cell value and convert to String.
     * 
     * @param cell
     * @return String
     */
    private String getValue(Cell cell) {

        switch (cell.getCellType()) {

        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue() + "";

        case Cell.CELL_TYPE_NUMERIC:
            return ((int) cell.getNumericCellValue()) + "";

        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();

        case Cell.CELL_TYPE_BLANK:
            return "";

        default:
            return "";
        }
    }

    /**
     * Method to read the file.
     * 
     * @param filePath
     *            (file to read)
     * @return testReport
     */
    public Map<Integer, List> readFile(final String filePath) {
        this.logger.log("Absolute file path:" + filePath);
        List<List> report = null;
        try {
            FileInputStream file = new FileInputStream(new File(filePath));

            // Get the workbook instance for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            // Get the worksheet count
            int workSheetCount = workbook.getNumberOfSheets();

            for (int count = 0; count < workSheetCount; count++) {
                // Get first sheet from the workbook
                HSSFSheet sheet = workbook.getSheetAt(count);

                Row rowObj = null;
                List rowData = new ArrayList();
                report = new ArrayList<List>();
                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    rowObj = sheet.getRow(rowNum);
                    rowData = new ArrayList();
                    if (rowObj != null) {
                        Iterator<Cell> rowCellIterator = rowObj.cellIterator();
                        while (rowCellIterator.hasNext()) {
                            Cell cellObj = rowCellIterator.next();
                            rowData.add(this.getValue(cellObj));
                        }
                        report.add(rowData);
                    }
                }
                this.workSheetsContent.put(count, report);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error while reading file : " + filePath;
            this.logger.log(errorMessage);
            this.logger.log(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return this.workSheetsContent;
    }
}
