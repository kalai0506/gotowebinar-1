package com.citrix.g2w.webdriver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Read the excel files
 * 
 * @author ankitag1
 * 
 */
public class ExcelDataReader implements SeleniumDataProvider {

    /**
     * parse the excel and return the data in selenium data provider format (2d array)
     * 
     * @param xlFilePath
     * @return
     * @throws IOException
     */
    @Override
    public Object[][] getSeleniumDataArray(String xlFilePath,
            String sheetName) throws IOException {
        Object[][] tableArray = null;
        InputStream st = null;
        try {
            st = new FileInputStream(new File(xlFilePath));;
            HSSFWorkbook wb = new HSSFWorkbook(st);
            HSSFSheet sheet;
            if (StringUtils.isEmpty(sheetName)) {
                // read only from first sheet
                sheet = wb.getSheetAt(0);
            } else {
                sheet = wb.getSheet(sheetName);
            }
            tableArray = new Object[sheet.getLastRowNum()][];
            Iterator<Row> rowIterator = sheet.iterator();

            // skip the header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            int ci = 0, cj = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                // For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                tableArray[ci] = new Object[row.getLastCellNum()];
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    tableArray[ci][cj] = getValue(cell);
                    cj++;
                }
                ci++;
                cj = 0;
            }
        } finally {
            if (st != null)
                st.close();
        }

        return tableArray;
    }

    private Object getValue(Cell cell) {
        Object value = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                value = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_ERROR:
                value = cell.getErrorCellValue();
                break;
        }
        return value;
    }
    
    /**
     * Method to read the file.
     * 
     * @param filePath
     *            (file to read)
     * @return testReport
     * @throws IOException 
     */
    public Map<Integer, List> readFile(final String filePath) throws IOException {
        Map<Integer, List> workSheetsContent = new HashMap<Integer, List>();
        List<List> report = null;
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(filePath));
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
                            rowData.add(getValue(cellObj));
                        }
                        report.add(rowData);
                    }
                }
                workSheetsContent.put(count, report);
            }

        } finally {
            if( file != null) {
                file.close();
            }
        }
        return workSheetsContent;
    }

}
