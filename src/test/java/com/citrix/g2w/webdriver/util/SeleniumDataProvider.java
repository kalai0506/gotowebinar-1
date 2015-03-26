package com.citrix.g2w.webdriver.util;

import java.io.IOException;

/**
 * read data as selenium data provider
 * @author citrix
 *
 */
public interface SeleniumDataProvider {

    Object[][] getSeleniumDataArray(String xlFilePath, String sheetName)
            throws IOException;

}
