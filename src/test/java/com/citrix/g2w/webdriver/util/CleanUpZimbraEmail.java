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

import org.testng.annotations.Test;

import com.citrix.g2w.webdriver.pages.emails.ZimbraEmail;

/**
 * @author: adas
 */
public class CleanUpZimbraEmail {

    /**
     * Method to clean up zimbra email for organizer and attendee inbox.
     * <ol>
     * <li>Create unique Email id to login zimbra mail</li>
     * <li>Login zimbra mail with previously created account.</li>
     * <li>Empty the zimbra inbox.</li>
     * <li>Logout from zimbra mail.</li>
     * </ol>
     */
    @Test
    public void cleanUpZimbraEmail() {

        // clean  up email for organizer inbox
        G2WUtility g2wUtility = new G2WUtility();
        ZimbraEmail zimbraOrganizerEmail = new ZimbraEmail(g2wUtility.createUniqueEmail());
        zimbraOrganizerEmail.login();
        zimbraOrganizerEmail.emptyInbox();
        zimbraOrganizerEmail.logout();
        
        //clean up email for attendee inbox
        ZimbraEmail zimbraAttendeeEmail = new ZimbraEmail(g2wUtility.createUniqueEmail("g2w"));
        zimbraAttendeeEmail.login();
        zimbraAttendeeEmail.emptyInbox();
        zimbraAttendeeEmail.logout();

    }
}
