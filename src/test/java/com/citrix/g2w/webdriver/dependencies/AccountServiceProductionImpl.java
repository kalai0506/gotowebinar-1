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

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import com.citrix.g2w.webdriver.TestLogger;

/**
 * @author: 
 * @since: 
 */
public class AccountServiceProductionImpl implements AccountService {
    /**
     * instance variable for corporate account key.
     */
    @Value("${g2w.corp.accountKey}")
    private Long corpAccountKey;
    /**
     * instance variable for corporate admin and organizer.
     */
    @Value("${g2w.corp.adminAndOrganizer}")
    private String corpAdminAndOrganizer;
    /**
     * instance variable for corporate admin only.
     */
    @Value("${g2w.corp.adminOnly}")
    private String corpAdminOnly;
    /**
     * instance variable for corporate organizer1.
     */
    @Value("${g2w.corp.org1}")
    private String corpOrg1;
    /**
     * instance variable for corporate organizer2.
     */
    @Value("${g2w.corp.org2}")
    private String corpOrg2;
    /**
     * instance object variable for logger.
     */
    TestLogger logger = new TestLogger();
    /**
     * instance object variable for object mapper.
     */
    ObjectMapper objectMapper = new ObjectMapper();
    /**
     * instance variable for personal account.
     */
    @Value("${g2w.personal.user}")
    private String personalAccount;

    /**
     * Read corporate account from properties file and return
     * TestCorporateAccount object.
     * 
     * @param seats
     *            number of seats in account
     * @param isAdminOrganizer
     *            flag to make admin organizer
     * @return TestCorporateAccount
     */
    @Override
    public TestCorporateAccount createCorporateAccount(final int seats,
            final boolean isAdminOrganizer) {
        Map<String, Object> adminInfo;
        TestCorporateAccount account;
        try {
            if (isAdminOrganizer) {
                adminInfo = this.objectMapper.readValue(this.corpAdminAndOrganizer, Map.class);
            } else {
                adminInfo = this.objectMapper.readValue(this.corpAdminOnly, Map.class);
            }

            Map<String, Object> org1 = this.objectMapper.readValue(this.corpOrg1, Map.class);
            Map<String, Object> org2 = this.objectMapper.readValue(this.corpOrg2, Map.class);
            account =
                    new TestCorporateAccount(this.corpAccountKey, adminInfo.get("adminEmail")
                            .toString(), adminInfo.get("adminPassword").toString(),
                            (Long) adminInfo.get("adminUserKey"), adminInfo.get("adminFirstName")
                            .toString(), adminInfo.get("adminLastName").toString(),"");

            account.addOrganizer(org1.get("firstName").toString(), org1.get("lastName").toString(),
                    org1.get("email").toString(), org1.get("password").toString(), (Long) org1
                    .get("userKey"));

            account.addOrganizer(org2.get("firstName").toString(), org2.get("lastName").toString(),
                    org2.get("email").toString(), org2.get("password").toString(), (Long) org2
                    .get("userKey"));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return account;
    }

    /**
     * Read corporate account from properties file and return
     * TestCorporateAccount object.
     * 
     * @param seats
     *            number of seats in account
     * @param isAdminOrganizer
     *            flag to make admin organizer
     * @param tier
     *            attendee limit
     * @return TestCorporateAccount
     */
    @Override
    public TestCorporateAccount createCorporateAccount(final int seats,
            final boolean isAdminOrganizer, final int tier) {
        Map<String, Object> adminInfo;
        TestCorporateAccount account;
        try {
            if (isAdminOrganizer) {
                adminInfo = this.objectMapper.readValue(this.corpAdminAndOrganizer, Map.class);
            } else {
                adminInfo = this.objectMapper.readValue(this.corpAdminOnly, Map.class);
            }

            Map<String, Object> org1 = this.objectMapper.readValue(this.corpOrg1, Map.class);
            Map<String, Object> org2 = this.objectMapper.readValue(this.corpOrg2, Map.class);
            account =
                    new TestCorporateAccount(this.corpAccountKey, adminInfo.get("adminEmail")
                            .toString(), adminInfo.get("adminPassword").toString(),
                            (Long) adminInfo.get("adminUserKey"), adminInfo.get("adminFirstName")
                            .toString(), adminInfo.get("adminLastName").toString(),"");

            account.addOrganizer(org1.get("firstName").toString(), org1.get("lastName").toString(),
                    org1.get("email").toString(), org1.get("password").toString(), (Long) org1
                    .get("userKey"));

            account.addOrganizer(org2.get("firstName").toString(), org2.get("lastName").toString(),
                    org2.get("email").toString(), org2.get("password").toString(), (Long) org2
                    .get("userKey"));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return account;
    }

    /**
     * Read personal account from properties file and return TestPersonalAccount
     * object.
     * 
     * @return TestPersonalAccount
     */
    @Override
    public TestPersonalAccount createPersonalAccount() {
        try {
            return this.objectMapper.readValue(this.personalAccount, TestPersonalAccount.class);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Read personal account from properties file and return
     * TestPersonalAccount. object. just override the method and localization
     * wont work here.
     * 
     * @param locale
     *            (user locale)
     * @return TestPersonalAccount
     */
    @Override
    public TestPersonalAccount createPersonalAccount(final String locale) {
        try {
            return this.objectMapper.readValue(this.personalAccount, TestPersonalAccount.class);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Method to set content sharing for an account.
     * 
     * @param accountKey
     *            (user account key)
     * @param enable
     *            (user enable)
     */
    @Override
    public void setContentSharingForAccount(final Long accountKey, final boolean enable) {
        this.logger.log("Content sharing for an account cannot be set on stage or live.");
    }

    /**
     * Method to set isRecordingv2Enabled flag for an account.
     * 
     * @param accountKey
     *            (user account key)
     * @param enable
     *            (enable or disable isRecordingv2Enabled flag)
     */
    @Override
    public void setRecordingv2AttributeForAccount(final Long accountKey, final boolean enable) {
        this.logger.log("isRecordingv2Enabled for an account cannot be set on stage or live.");
    }

    /**
     * Method to suspend user.
     * 
     * @param userKey
     *            (user key)
     */
    @Override
    public void suspendUser(final Long userKey) {
        this.logger.log("suspend user for an account cannot be set on stage or live.");
    }
    
    /**
     * Method to unlicense a user.
     * 
     * @param userKey
     *            (user key)
     * @param licenseKey
     *            (license key)
     */
    @Override
    public void unLicenseUser(final Long userKey, final String licenseKey) {
        this.logger.log("unlicense user for an account cannot be set on stage or live.");
    }

    /**
     * Method to un suspend user.
     * 
     * @param userKey
     *            (user key)
     */
    @Override
    public void unSuspendUser(final Long userKey) {
        this.logger.log("unsuspend user for an account cannot be set on stage or live.");
    }
}
