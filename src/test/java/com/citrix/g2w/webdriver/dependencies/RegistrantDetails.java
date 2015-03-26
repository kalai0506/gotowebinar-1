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

import java.util.Date;

import com.citrix.g2w.webdriver.util.G2WUtility;

/**

 */
public class RegistrantDetails {

    /**
     * instance variable for address.
     */
    private String address;
    /**
     * instance variable for city.
     */
    private String city;
    /**
     * instance variable for country.
     */
    private String country;
    /**
     * instance variable for email address.
     */
    private String emailAddress;
    /**
     * instance variable for first name.
     */
    private String firstName;
    /**
     * instance variable for job title.
     */
    private String jobTitle;
    /**
     * instance variable for last name.
     */
    private String lastName;
    /**
     * instance variable for organization.
     */
    private String organization;
    /**
     * instance variable for phone.
     */
    private String phone;
    /**
     * instance variable for question and comments.
     */
    private String questionAndComments;
    /**
     * instance variable for state.
     */
    private String state;
    /**
     * instance variable for zipcode.
     */
    private String zipCode;
    /**
     * instance variable for purchasingTimeframe.
     */
    private String purchasingTimeframe;
    /**
     * instance variable purchasingRole.
     */
    private String purchasingRole;
    /**
     * instance variable for numberOfEmployees.
     */
    private String numberOfEmployees;
    /**
     * instance for variable industry.
     */
    private String industry;

    /**
     * Default constructor which creates object with default details.
     */
    public RegistrantDetails() {
        String uniqueString = new Date().getTime() + "-" + Thread.currentThread().getId();
        this.firstName = "George-" + uniqueString;
        this.lastName = "Washington-" + uniqueString;
        this.emailAddress = new G2WUtility().createUniqueEmail("g2w");
    }

    /**
     * Constructor to initialize registrant details with first name, last name
     * and email.
     * 
     * @param firstName
     *            (first name of the attendee)
     * @param lastName
     *            (last name of the attendee)
     * @param email
     *            (email address of the attendee)
     */
    public RegistrantDetails(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = email;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @return the jobTitle
     */
    public String getJobTitle() {
        return this.jobTitle;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @return the organization
     */
    public String getOrganization() {
        return this.organization;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * @return the questionAndComments
     */
    public String getQuestionAndComments() {
        return this.questionAndComments;
    }

    /**
     * @return the state
     */
    public String getState() {
        return this.state;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return this.zipCode;
    }

    /**
     * @return the purchasingTimeframe
     */
    public String getPurchasingTimeframe() {
        return this.purchasingTimeframe;
    }
    /**
     * @return the purchasingRole
     */
    public String getPurchasingRole() {
        return this.purchasingRole;
    }
    /**
     * @return the numberOfEmployees
     */
    public String getNumberOfEmployees() {
        return this.numberOfEmployees;
    }
    /**
     * @return the industry
     */
    public String getIndustry() {
        return this.industry;
    }
    /**
     * @param address
     *            the address to set
     */
    public void setAddress(final String address) {
        this.address = address;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * @param emailAddress
     *            the emailAddress to set
     */
    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param jobTitle
     *            the jobTitle to set
     */
    public void setJobTitle(final String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param organization
     *            the organization to set
     */
    public void setOrganization(final String organization) {
        this.organization = organization;
    }

    /**
     * @param phone
     *            the phone to set
     */
    public void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * @param questionAndComments
     *            the questionAndComments to set
     */
    public void setQuestionAndComments(final String questionAndComments) {
        this.questionAndComments = questionAndComments;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * @param zipCode
     *            the zipCode to set
     */
    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }
    
    /** @param purchasingRole
     *             the purchasingRole to set
     */
    public void setPurchasingRole(String purchasingRole) {
        this.purchasingRole = purchasingRole;
    }

    /**
     * @param purchasingTimeframe
     *            the purchasingTimeframe to set
     */
    public void setPurchasingTimeframe(final String purchasingTimeframe) {
        this.purchasingTimeframe = purchasingTimeframe;
    }

    /**
     * 
     * @param numberOfEmployees
     *             the numberOfEmployees to set
     */
    public void setNumberOfEmployees(String numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    /**
     * @param industry
     *             the industry to set
     */
    public void setIndustry(String industry) {
        this.industry = industry;
        
    }
}
