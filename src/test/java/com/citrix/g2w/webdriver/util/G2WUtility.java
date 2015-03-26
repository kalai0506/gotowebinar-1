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

import java.util.Date;

import com.citrix.g2w.webdriver.PropertyUtil;

/**
 * @author: Vaishnavi
 * @since: Jan 8, 2014
 */
public class G2WUtility {

    /**
     * instance object variable for property util.
     */
    private final PropertyUtil propertyUtil;

    /**
     * Constructor to instantiate instance variables.
     */
    public G2WUtility() {
        this.propertyUtil = new PropertyUtil();
    }

    /**
     * Method used to create unique email id for a user.
     * 
     * @return eamilId
     */
    public String createUniqueEmail() {
        String prefix = this.propertyUtil.getProperty("email.prefix");
        String suffix = this.propertyUtil.getProperty("email.suffix");
        String uniqueString = new Date().getTime() + "-" + Thread.currentThread().getId();

        StringBuilder builder = new StringBuilder(prefix);
        builder.append("-").append(uniqueString);
        builder.append("@").append(suffix);

        return builder.toString();
    }

    /**
     * Method used to create unique email id for a user with a list of prefixes.
     * 
     * @param prefixToAdd
     *            (prefix to add)
     * @return eamilId
     */
    public String createUniqueEmail(final String prefixToAdd) {
        String prefix = this.propertyUtil.getProperty("email.prefix");
        String suffix = this.propertyUtil.getProperty("email.suffix");
        String uniqueString = new Date().getTime() + "-" + Thread.currentThread().getId();

        StringBuilder builder = new StringBuilder(prefixToAdd);
        builder.append(prefix);
        builder.append("-").append(uniqueString);
        builder.append("@").append(suffix);

        return builder.toString();
    }
}
