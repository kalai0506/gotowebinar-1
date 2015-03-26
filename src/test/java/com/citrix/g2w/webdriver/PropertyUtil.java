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

package com.citrix.g2w.webdriver;

import java.util.Properties;

/**
 *
 */
public class PropertyUtil {
    /**
     * instance object variable for properties.
     */
   // private final Properties properties = new Properties();
	private final Properties properties=new Properties();

    /**
     * constructor to initialize instance variables.
     */
    public PropertyUtil() {
        // First load the default.properties file
        try {
            this.properties.load(this.getClass().getClassLoader().getResourceAsStream("default.properties"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to load default.properties file: " + e.getCause());
        }

        // Load clientSettings if it exists. This will override any common properties defined in default.properties file
        try {
            this.properties.load(this.getClass().getClassLoader().getResourceAsStream("clientSettings.properties"));
        } catch (Exception e) {
            // Ignore the exception if clientSettings is not present
        }
    }

    /**
     * Method used to get value for particular property.
     * 
     * @param property
     *            (property)
     * @return property value
     */
    public String getProperty(final String property) {
        return this.properties.getProperty(property);
    }
}
