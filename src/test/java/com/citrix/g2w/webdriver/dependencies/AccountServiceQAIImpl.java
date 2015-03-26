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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.citrix.g2w.webdriver.PropertyUtil;
import com.citrix.g2w.webdriver.TestLogger;
import com.citrix.g2w.webdriver.util.G2WUtility;

/**
 * @author: 
 * @since: 
 */
public class AccountServiceQAIImpl implements AccountService {

    /**
     * instance variable object for http headers.
     */
    HttpHeaders accountSvcHeaders;

    /**
     * instance variable for account service url.
     */
    @Value("${accountService.restUrl}")
    private String accountSvcUrl;

    /**
     * instance object variable for G2WUtils.
     */
    private final G2WUtility g2wUtils;

    /**
     * instance object variable for logger.
     */
    TestLogger logger = new TestLogger();

    /**
     * instance object variable for object mapper.
     */
    ObjectMapper objectMapper;

    /**
     * instance object variable for property util.
     */
    @Autowired
    private PropertyUtil propertyUtil;

    /**
     * instance object variable for rest template.
     */
    RestTemplate restTemplate;
    /**
     * instance variable for service rest url.
     */
    @Value("${g2w.restUrl}")
    private String serviceRestUrl;

    /**
     * constructor to initialize instance variables.
     */
    public AccountServiceQAIImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.g2wUtils = new G2WUtility();

        this.accountSvcHeaders = new HttpHeaders();
        this.accountSvcHeaders.add("Content-Type", "application/json");
        this.accountSvcHeaders.add("Accept", "application/json");
        this.accountSvcHeaders.add("ClientName", "test_provisioner");
    }

    /**
     * Method used to add user to an account.
     * 
     * @param accountKey
     *            (user account key)
     * @param firstName
     *            (user first name)
     * @param lastName
     *            (user last name)
     * @param email
     *            (user email)
     * @param password
     *            (user password)
     * @param locale
     *            (user locale)
     * @param timezone
     *            (user timezone)
     * @return long
     */
    private Long addUserToAccount(final Long accountKey, final String firstName,
            final String lastName, final String email, final String password, final String locale,
            final String timezone) {
        String userKey;
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("email", email);
            userJson.put("firstName", firstName);
            userJson.put("lastName", lastName);
            userJson.put("password", password);
            userJson.put("locale", locale);
            userJson.put("timezone", timezone);

            HttpEntity httpEntity = new HttpEntity(userJson.toString(), this.accountSvcHeaders);
            userKey = this.restTemplate.exchange(
                    this.accountSvcUrl + "/accounts/" + accountKey + "/users", HttpMethod.POST,
                    httpEntity, String.class).getBody();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return Long.valueOf(userKey);
    }

    /**
     * Method used to create account and user.
     * 
     * @param plans
     *            (user plan)
     * @param email
     *            (user email)
     * @param password
     *            (user password)
     * @param firstName
     *            (user first name)
     * @param lastName
     *            (user last name)
     * @param locale
     *            (user locale)
     * @param timezone
     *            (user time zone)
     * @param isAdmin
     *            (is Admin)
     * @return accountInfo
     */
    private Map<String, Long> createAccountAndUserInAS(final String[] plans, final String email,
            final String password, final String firstName, final String lastName,
            final String locale, final String timezone, final boolean isAdmin) {

        Map<String, Long> accountInfo;

        JSONObject request = new JSONObject();

        try {

            JSONObject accountJson = new JSONObject();
            accountJson.put("name", "Webtest Account");
            accountJson.put("country", "US");

            JSONObject userJson = new JSONObject();
            userJson.put("email", email);
            userJson.put("firstName", firstName);
            userJson.put("lastName", lastName);
            userJson.put("externalAdmin", isAdmin);
            userJson.put("password", password);
            userJson.put("locale", locale);
            userJson.put("timezone", timezone);

            request.put("account", accountJson);
            request.put("user", userJson);
            request.put("plans", plans);

            HttpEntity httpEntity = new HttpEntity(request.toString(), this.accountSvcHeaders);
            String result = this.restTemplate.exchange(this.accountSvcUrl + "/accounts/users",
                    HttpMethod.POST, httpEntity, String.class).getBody();

            accountInfo = this.objectMapper.readValue(result, Map.class);
            
            return accountInfo;

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Method to create corporate account.
     * 
     * @param seats
     *            number of seats in the account
     * @param isAdminOrganizer
     *            flag to make admin organizer
     * @return TestCorporateAccount
     */
    @Override
    public TestCorporateAccount createCorporateAccount(final int seats,
            final boolean isAdminOrganizer) {
        TestCorporateAccount corporateAccount = this.createCorporateAccount(seats,
                isAdminOrganizer, 200);
        this.logger.log("Created corp account: " + corporateAccount);
        return corporateAccount;
    }

    /**
     * Method to create corporate account.
     * 
     * @param seats
     *            number of seats in the account
     * @param isAdminOrganizer
     *            flag to make admin organizer
     * @param tier
     *            number of max attendees
     * @return TestCorporateAccount
     */
    @Override
    public TestCorporateAccount createCorporateAccount(final int seats,
            final boolean isAdminOrganizer, final int tier) {
        String email = this.g2wUtils.createUniqueEmail();
        String password = this.propertyUtil.getProperty("default.password");
        String adminFirstName = this.propertyUtil.getProperty("admin.firstName");
        String adminLastName = this.propertyUtil.getProperty("admin.lastName");
        String locale = this.propertyUtil.getProperty("environment.locale");
        String timezone = this.propertyUtil.getProperty("environment.timezone");
        //TODO should be in common code
        // Create corporate account
        Map<String, Long> keys = this.createAccountAndUserInAS(new String[] { "g2wcorporateplan",
        "g2mcorporateplan" }, email, password, adminFirstName, adminLastName, locale,
        timezone, true);
        
        // Create external admin for account
        String extAdminLicenseKey = this.createLicenseForAccount(keys.get("accountkey"), true,
                "paid", new String[] { "ROLE_EXT_ADMIN", "ROLE_EXT_ADMIN_SUPER_USER" }, seats,
                "offline", tier);

        // License ext admin
        this.licenseUser(keys.get("userkey"), extAdminLicenseKey);

        // Create license for account
        String licenseKey = this
                .createLicenseForAccount(keys.get("accountkey"), true, "paid", new String[] {
                    "ROLE_G2W_ORGANIZER", "ROLE_G2M_ORGANIZER" }, seats, "offline", tier);
  


        // license admin if admin is organizer too
        int remainingSeats = seats;
        if (isAdminOrganizer) {
            this.licenseUser(keys.get("userkey"), licenseKey);
            remainingSeats = remainingSeats - 1;
        }

        TestCorporateAccount corporateAccount = new TestCorporateAccount(keys.get("accountkey"),
                email, password, keys.get("userkey"), adminFirstName, adminLastName,licenseKey);

        // Add organizers to account
        for (int i = 1; i <= remainingSeats; i++) {
            String firstName = this.propertyUtil.getProperty("corpOrganizer.firstName.prefix")
                    + String.valueOf(i);
            String lastName = this.propertyUtil.getProperty("corpOrganizer.lastName.prefix")
                    + String.valueOf(i);
            String userEmail = this.g2wUtils.createUniqueEmail();

            // Add user and license user
            Long userKey = this.addUserToAccount(keys.get("accountkey"), firstName, lastName,
                    userEmail, password, locale, timezone);
            this.licenseUser(Long.valueOf(userKey), licenseKey);

            corporateAccount.addOrganizer(firstName, lastName, userEmail, password, userKey);
        }

        this.logger.log("Created corp account: " + corporateAccount);
        return corporateAccount;
    }

    /**
     * Method used to create license for user account.
     * 
     * @param accountKey
     *            (user account key)
     * @param enabled
     *            (user enabled)
     * @param serviceType
     *            (user service type)
     * @param roles
     *            (user roles)
     * @param seats
     *            (user seats)
     * @param channel
     *            (user channel)
     * @return licenseKey
     */
    private String createLicenseForAccount(final Long accountKey, final boolean enabled,
            final String serviceType, final String[] roles, final int seats, final String channel,
            int tier) {
        String licenseKey;

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("enabled", enabled);
            requestJson.put("serviceType", serviceType);
            requestJson.put("description", "G2W License");
            requestJson.put("roles", roles);
            requestJson.put("seats", seats);
            requestJson.put("channel", channel);
            requestJson.put("tier", tier);

            HttpEntity entityLicense = new HttpEntity(requestJson.toString(),
                    this.accountSvcHeaders);
            licenseKey = this.restTemplate.exchange(
                    this.accountSvcUrl + "/accounts/" + accountKey + "/licenses", HttpMethod.POST,
                    entityLicense, String.class).getBody();

            // Set maxAttendees on license for G2W
            JSONObject entitlementRequestJson = new JSONObject();
            entitlementRequestJson.put("maxAttendees", tier);
            this.restTemplate
            .exchange(this.accountSvcUrl + "/licenses/" + licenseKey + "/products/g2w",
                    HttpMethod.PUT, new HttpEntity(entitlementRequestJson.toString(),
                            this.accountSvcHeaders), null);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return licenseKey;
    }

    /**
     * Method to create personal account.
     * 
     * @return TestPersonalAccount
     */
    @Override
    public TestPersonalAccount createPersonalAccount() {
        String email = this.g2wUtils.createUniqueEmail();
        String password = this.propertyUtil.getProperty("default.password");
        String firstName = this.propertyUtil.getProperty("registration.firstName");
        String lastName = this.propertyUtil.getProperty("registration.lastName");
        String locale = this.propertyUtil.getProperty("environment.locale");
        String timezone = this.propertyUtil.getProperty("environment.timezone");
       //TODO should be in common code
        Map<String, Long> keys = this.createAccountAndUserInAS(new String[] { "g2wpersonalplan",
        "g2mpersonalplan" }, email, password, firstName, lastName, locale, timezone, false);

        String licenseKey = this.createLicenseForAccount(keys.get("accountkey"), true, "trial",
                new String[] { "ROLE_G2M_ORGANIZER", "ROLE_G2W_ORGANIZER" }, 1, "online", 25);
        this.licenseUser(keys.get("userkey"), licenseKey);

        TestPersonalAccount personalAccount = new TestPersonalAccount(keys.get("accountkey"),
                keys.get("userkey"), email, password, firstName, lastName,licenseKey);
        this.logger.log("Created personal account: " + personalAccount);
        return personalAccount;
    }

    /**
     * Method to create personal account based on locale.
     * 
     * @param locale
     *            (create personal account depends on locale)
     * @return TestPersonalAccount
     */
    @Override
    public TestPersonalAccount createPersonalAccount(final String locale) {
        //TO DO this should be common across products. 
        String email = this.g2wUtils.createUniqueEmail();
        String password = this.propertyUtil.getProperty("default.password");
        String firstName = this.propertyUtil.getProperty("registration.firstName");
        String lastName = this.propertyUtil.getProperty("registration.lastName");
        String timezone = this.propertyUtil.getProperty("environment.timezone");

        Map<String, Long> keys = this.createAccountAndUserInAS(new String[] { "g2wpersonalplan",
        "g2mpersonalplan" }, email, password, firstName, lastName, locale, timezone, false);

        String licenseKey = this.createLicenseForAccount(keys.get("accountkey"), true, "trial",
                new String[] { "ROLE_G2M_ORGANIZER", "ROLE_G2W_ORGANIZER" }, 1, "online", 25);
        this.licenseUser(keys.get("userkey"), licenseKey);

        TestPersonalAccount personalAccount = new TestPersonalAccount(keys.get("accountkey"),
                keys.get("userkey"), email, password, firstName, lastName,licenseKey);
        this.logger.log("Created personal account: " + personalAccount);
        return personalAccount;
    }

    /**
     * Method used to license user.
     * 
     * @param userKey
     *            (user key)
     * @param licenseKey
     *            (license key)
     */
    private void licenseUser(final Long userKey, final String licenseKey) {
        try {
            // Add license to the user
            this.restTemplate.postForLocation(this.accountSvcUrl + "/licenses/" + licenseKey
                    + "/users/" + userKey, new HttpEntity(this.accountSvcHeaders));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    /**
     * Method used to get user.
     * 
     * @param userKey
     *            (user key)
     * @param licenseKey
     *            (license key)
     */
    private String getUser(final Long userKey) {
        try {
            return this.restTemplate.exchange(this.accountSvcUrl + "/users/" + userKey, HttpMethod.GET, 
                    new HttpEntity(this.accountSvcHeaders), String.class).getBody();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    

    /**
     * Method to enable or disable content sharing for an account.
     * 
     * @param accountKey
     *            (user account key)
     * @param enable
     *            (user enable)
     */
    @Override
    public void setContentSharingForAccount(final Long accountKey, final boolean enable) {
        HttpEntity httpEntity = new HttpEntity("{\"contentsharingenabled\":" + enable + "}",
                this.accountSvcHeaders);
        this.restTemplate.exchange(
                this.accountSvcUrl + "/accounts/" + accountKey + "/products/g2w", HttpMethod.PUT,
                httpEntity, null);
    }

    /**
     * Method to enable or disable isRecordingv2Enabled flag for an account.
     * 
     * @param accountKey
     *            (user account key)
     * @param enable
     *            (enable or disable isRecordingv2Enabled flag)
     */
    @Override
    public void setRecordingv2AttributeForAccount(final Long accountKey, final boolean enable) {
        HttpEntity httpEntity = new HttpEntity("{\"isRecordingv2Enabled\":" + enable + "}",
                this.accountSvcHeaders);
        this.restTemplate.exchange(
                this.accountSvcUrl + "/accounts/" + accountKey + "/products/g2w", HttpMethod.PUT,
                httpEntity, null);
    }

    /**
     * Method to suspend user.
     * 
     * @param userKey
     *            (user key)
     */
    @Override
    public void suspendUser(final Long userKey) {
        HttpEntity httpEntity = new HttpEntity("{\"status\":\"suspended\"}", this.accountSvcHeaders);
        this.restTemplate.exchange(this.accountSvcUrl + "/users/" + userKey,
                HttpMethod.PUT, httpEntity, null);
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
        try {
            // unlicense the user
            String licenseUri = this.accountSvcUrl + "/licenses/" + licenseKey + "/users/"
                    + userKey;
            this.restTemplate.exchange(licenseUri, HttpMethod.DELETE, new HttpEntity(
                    this.accountSvcHeaders), null);
        } catch (Exception t) {
            t.printStackTrace();
        }
    }

    /**
     * Method to un suspend user.
     * 
     * @param userKey
     *            (user key)
     */
    @Override
    public void unSuspendUser(final Long userKey) {
        HttpEntity httpEntity = new HttpEntity("{\"status\":null}", this.accountSvcHeaders);
        this.restTemplate.exchange(this.accountSvcUrl + "/users/" + userKey,
                HttpMethod.PUT, httpEntity, null);
    }
}
