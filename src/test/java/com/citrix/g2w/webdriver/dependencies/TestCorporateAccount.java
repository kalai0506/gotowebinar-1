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

import java.util.ArrayList;
import java.util.List;

/**

 */
public class TestCorporateAccount {

    /**
     * inner class for an organizer.
     */
    public class Organizer {
        /**
         * instance variable for user email.
         */
        private final String email;
        /**
         * instance variable for user first name.
         */
        private final String firstName;
        /**
         * instance variable for user last name.
         */
        private final String lastName;
        /**
         * instance variable for user password.
         */
        private final String password;
        /**
         * instance variable for user key.
         */
        private final Long userKey;
        

        /**
         * organizer constructor to initialize instance variables.
         * 
         * @param firstName
         *            (user first name)
         * @param lastName
         *            (user last name)
         * @param email
         *            (user email)
         * @param password
         *            (user password)
         * @param userKey
         *            (user key)
         */
        public Organizer(final String firstName, final String lastName, final String email,
                final String password, final Long userKey) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.userKey = userKey;
            
        }

        /**
         * Method used to get user email id.
         * 
         * @return email
         */
        public String getEmail() {
            return this.email;
        }

        /**
         * Method used to get user first name.
         * 
         * @return firstName
         */
        public String getFirstName() {
            return this.firstName;
        }

        /**
         * Method used to get user last name.
         * 
         * @return lastName
         */
        public String getLastName() {
            return this.lastName;
        }

        /**
         * Method used to get user password.
         * 
         * @return password
         */
        public String getPassword() {
            return this.password;
        }

        /**
         * Method used to get user key.
         * 
         * @return userKey
         */
        public Long getUserKey() {
            return this.userKey;
        }
    }

    /**
     * instance variable for account key.
     */
    private final Long accountKey;
    /**
     * instance variable for admin email.
     */
    private final String adminEmail;
    /**
     * instance variable for admin first name.
     */
    private String adminFirstName;
    /**
     * instance variable for admin last name.
     */
    private String adminLastName;
    /**
     * instance variable for admin password.
     */
    private final String adminPassword;
    /**
     * instance variable for admin user key.
     */
    private final Long adminUserKey;
    /**
     * instance variable for license key.
     */
    private final String licenseKey;
    
    /**
     * instance list object variable for organizers.
     */
    
    private final List<Organizer> organizers = new ArrayList<Organizer>();

    /**
     * Test Corporate account constructor to initialize instance variable.
     * 
     * @param accountKey
     *            (account key)
     * @param adminEmail
     *            (admin email)
     * @param adminPassword
     *            (admin password)
     * @param adminUserKey
     *            (admin user key)
     * @param adminFirstName
     *            (admin first name)
     * @param adminLastName
     *            (admin last name)
     */
    public TestCorporateAccount(final Long accountKey, final String adminEmail,
            final String adminPassword, final Long adminUserKey, final String adminFirstName,
            final String adminLastName,final String licenseKey) {
        this.accountKey = accountKey;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.adminUserKey = adminUserKey;
        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
        this.licenseKey = licenseKey;
    }

    /**
     * Method to add organizer.
     * 
     * @param firstName
     *            (first name of organizer)
     * @param lastName
     *            (last name of organizer)
     * @param email
     *            (email address of organizer)
     * @param password
     *            (password of organizer)
     * @param userKey
     *            (user key of organizer)
     */
    public void addOrganizer(final String firstName, final String lastName, final String email,
            final String password, final Long userKey) {
        this.organizers.add(new Organizer(firstName, lastName, email, password, userKey));
    }

    /**
     * Method to get account key.
     * 
     * @return accountKey
     */
    public Long getAccountKey() {
        return this.accountKey;
    }

    /**
     * Method to get admin email.
     * 
     * @return adminEmail
     */
    public String getAdminEmail() {
        return this.adminEmail;
    }

    /**
     * Method to get admin first name.
     * 
     * @return adminFirstName
     */
    public String getAdminFirstName() {
        return this.adminFirstName;
    }

    /**
     * Method to get admin last name.
     * 
     * @return adminLastName
     */
    public String getAdminLastName() {
        return this.adminLastName;
    }

    /**
     * Method to get admin password.
     * 
     * @return adminPassword
     */
    public String getAdminPassword() {
        return this.adminPassword;
    }

    /**
     * Method to get admin user key.
     * 
     * @return adminUserKey
     */
    public Long getAdminUserKey() {
        return this.adminUserKey;
    }
    
    /**
     * Method to get license key.
     * 
     * @return the licenseKey
     */
    public String getLicenseKey() {
        return this.licenseKey;
    }

    /**
     * Method to get number of organizers.
     * 
     * @return organizers size
     */
    public int getNumOfOrganizers() {
        return this.organizers.size();
    }

    /**
     * Method to get organizer by index.
     * 
     * @param index
     *            (index of organizer required from list)
     * @return organizer (by index)
     */
    public Organizer getOrganizer(final int index) {
        return this.organizers.get(index);
    }

    /**
     * Method to set admin first name.
     * 
     * @param adminFirstName
     *            (admin first name)
     */
    public void setAdminFirstName(final String adminFirstName) {
        this.adminFirstName = adminFirstName;
    }

    /**
     * Method to set admin last name.
     * 
     * @param adminLastName
     *            (admin last name)
     */
    public void setAdminLastName(final String adminLastName) {
        this.adminLastName = adminLastName;
    }

    /**
     * Method overriding toString() method.
     * 
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Account key: " + this.accountKey);
        builder.append(", Admin: " + this.adminEmail);
        builder.append(", Organizers: ");
        for (Organizer organizer : this.organizers) {
            builder.append(organizer.getEmail() + "/" + organizer.getUserKey() + ",");
        }
        return builder.toString();
    }
}
