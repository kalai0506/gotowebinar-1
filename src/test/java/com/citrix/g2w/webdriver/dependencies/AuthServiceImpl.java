
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

import com.citrix.g2w.webdriver.PropertyUtil;
import com.citrix.g2w.webdriver.TestLogger;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**

 */
public class AuthServiceImpl implements AuthService {
    /**
     * instance variable for authentication service url.
     */
    @Value("${authService.restUrl}")
    private String authSvcUrl;

    /**
     * instance variable object for http headers.
     */
    HttpHeaders authSvcHeaders;

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
     * instance variable for queue service url.
     */
    @Value("${queueService.restUrl}")
    private String queueSvcUrl;

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
    public AuthServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();

        this.authSvcHeaders = new HttpHeaders();
        this.authSvcHeaders.add("Content-Type", "application/json");
        this.authSvcHeaders.add("Accept", "application/json");
    }

    /**
     * Method to get auth token from auth service
     *
     * @param userId
     *            (user id)
     * @param password
     *            (user password)
     */
    @Override
    public String getAuthToken(final String userId, final String password) {
        HttpEntity httpEntity = new HttpEntity("{\"username\":\"" + userId + "\",\"password\":\"" + password + "\"}",
                authSvcHeaders);
        String result =
                this.restTemplate.exchange(this.authSvcUrl + "/tokens", HttpMethod.POST, httpEntity, String.class).getBody();
        try {
            JSONObject json = new JSONObject(result);
            Thread.sleep(2000);
            return json.getString("token");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
