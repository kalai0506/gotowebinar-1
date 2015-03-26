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

/**
 * @author: 
 * @since: 
 */
public interface AccountService {
    /**
     * Method to create corporate account.
     *
     * @param seats
     *            (seats)
     * @param isAdminOrganizer
     *            (is admin organizer)
     * @return TestCorporateAccount
     */
    TestCorporateAccount createCorporateAccount(final int seats, final boolean isAdminOrganizer);

    /**
     * Method to create corporate account.
     *
     * @param seats
     *            (seats)
     * @param isAdminOrganizer
     *            (is admin organizer)
     * @param tier
     *             (attendee limit)
     * @return TestCorporateAccount
     */
    TestCorporateAccount createCorporateAccount(final int seats, final boolean isAdminOrganizer,
            final int tier);

    /**
     * Method to create personal account.
     *
     * @return TestPersonalAccount
     */
    TestPersonalAccount createPersonalAccount();

    /**
     * Method to create personal account based on locale.
     *
     * @param locale
     *            (user locale)
     * @return TestPersonalAccount
     */
    TestPersonalAccount createPersonalAccount(final String locale);

    /**
     * Method to enable or disable content sharing for an account.
     *
     * @param accountKey
     *            (user account key)
     * @param enable
     *            (enable)
     */
    void setContentSharingForAccount(final Long accountKey, final boolean enable);

    /**
     * Method to enable or disable isRecordingv2Enabled flag for an account.
     * 
     * @param accountKey
     *            (user account key)
     * @param enable
     *            (enable or disable isRecordingv2Enabled flag)
     */
    void setRecordingv2AttributeForAccount(final Long accountKey, final boolean enable);

    /**
     * Method to suspend user.
     * 
     * @param userKey
     *            (user key)
     */
    void suspendUser(final Long userKey);

    /**
     * Method to un suspend user.
     * 
     * @param userKey
     *            (user key)
     */
    void unSuspendUser(final Long userKey);
    
    /**
     * Method to unlicense a user.
     * 
     * @param userKey
     *            (user key)
     * @param licenseKey
     *            (license key)
     */
    void unLicenseUser(final Long userKey, final String licenseKey);
}
