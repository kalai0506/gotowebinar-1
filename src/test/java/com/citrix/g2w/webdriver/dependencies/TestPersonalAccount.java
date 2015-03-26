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

 */
public class TestPersonalAccount {
    /**
     * instance variable for account key.
     */
    private Long accountKey;
    /**
     * instance variable for email.
     */
    private String email;
    /**
     * instance variable for password.
     */
    private String password;
    /**
     * instance variable for user key.
     */
    private Long userKey;
    /**
     * instance variable for first name
     */
    private String firstName;
    /**
     * instance variable for last name
     */
    private String lastName;
    
    /**
     * instance variable for license key
     */
    private String licenseKey;

    /**
     * default constructor for TestPersonalAccount.
     */
    public TestPersonalAccount() {

    }

    /**
     * Constructor to initialize instance variables of the class.
     * @param accountKey
     *            (account key)
     * @param userKey
     *            (user key)
     * @param email
     *            (email)
     * @param password
     *            (password)
     * @param firstName
     *            (firstName)
     * @param lastName
     *            (lastName)           
     */
    public TestPersonalAccount(final Long accountKey, final Long userKey, final String email,
            final String password, final String firstName, final String lastName,final String licenseKey) {
        this.accountKey = accountKey;
        this.userKey = userKey;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.licenseKey = licenseKey;
    }

    /**
     * Method to get account key.
     * @return accountKey
     */
    public Long getAccountKey() {
        return this.accountKey;
    }

    /**
     * Method to get email.
     * @return email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Method to get first name.
     * @return firstName
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Method to get last name.
     * @return lastName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Method to get password.
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Method to get user key.
     * @return userKey
     */
    public Long getUserKey() {
        return this.userKey;
    }
    
    /**
     * @return the licenseKey
     */
    public String getLicenseKey() {
        return this.licenseKey;
    }

    /**
     * Method to set account key.
     * @param accountKey
     *            (account key)
     */
    public void setAccountKey(final Long accountKey) {
        this.accountKey = accountKey;
    }

    /**
     * Method to set email.
     * @param email
     *            (email)
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Method to set first name.
     * @param firstName
     *            (firstName)
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Method to set last name.
     * @param lastName
     *            (lastName)
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Method to set password.
     * @param password
     *            (password)
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Method to set user key.
     * @param userKey
     *            (user key)
     */
    public void setUserKey(final Long userKey) {
        this.userKey = userKey;
    }

    /**
     * Method overriding toString() method.
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Account key: " + this.accountKey);
        builder.append(", User key: " + this.userKey);
        builder.append(", Email: " + this.email);
        builder.append(", FirstName: " + this.firstName);
        builder.append(", LastName: " + this.lastName);
        return builder.toString();
    }
}
