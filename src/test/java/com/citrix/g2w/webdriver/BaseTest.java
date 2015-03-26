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

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.StringUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;

import com.citrix.g2w.webdriver.dependencies.AccountService;
import com.citrix.g2w.webdriver.dependencies.AuthService;
import com.citrix.g2w.webdriver.flows.Session;

/**
 * saraswathireddy
 */
@ContextConfiguration(locations = "classpath:base-test.xml")
public abstract class BaseTest extends AbstractTestNGSpringContextTests implements InitializingBean
{
    /**
     * instance object variable for account service.
     */
    @Autowired
    protected AccountService accountService;
   
    /**
     * instance object variable for auth service.
     */
    @Autowired
    protected AuthService authService;
    /**
     * instance variable for db historical schema password.
     */
    @Value("${db.hist.password}")
    protected String dbHistPassword;
    /**
     * instance variable for db historical schema user.
     */
    @Value("${db.hist.user}")
    protected String dbHistUser;
    /**
     * instance variable for db instance.
     */
    @Value("${db.instance}")
    protected String dbInstance;
    /**
     * instance variable for db name.
     */
    @Value("${db.name}")
    protected String dbName;
    /**
     * instance variable for db password.
     */
    @Value("${db.password}")
    protected String dbPassword;
    /**
     * instance variable for db port.
     */
    @Value("${db.port}")
    protected String dbPort;
    /**
     * instance variable for db quartz schema password.
     */
    @Value("${db.quartz.password}")
    protected String dbQuartzPassword;
    /**
     * instance variable for db quartz schema user.
     */
    @Value("${db.quartz.user}")
    protected String dbQuartzUser;
    /**
     * instance variable for db type.
     */
    @Value("${db.type}")
    protected String dbType;
    /**
     * instance variable for db user.
     */
    @Value("${db.user}")
    protected String dbUser;
    /**
     * instance object variable for default properties.
     */
    protected Properties defaultProperties;
    /**
     * instance object variable for current locale.
     */
    protected Locale locale;
    /**
     * instance object variable for logger.
     */
    protected TestLogger logger = new TestLogger();
    /**
     * instance object variable for resource bundle message sources.
     */
    protected ResourceBundleMessageSource messages;
    /**
     * instance object variable for property util.
     */
    @Autowired
    protected PropertyUtil propertyUtil;
    /**
     * instance variable for rmi admin user name.
     */
    @Value("${rmi.adminUserName}")
    protected String rmiAdminUserName;
    /**
     * instance variable for rmi host.
     */
    @Value("${rmi.host}")
    protected String rmiHost;
    /**
     * instance variable for rmi password.
     */
    @Value("${rmi.password}")
    protected String rmiPassword;
    /**
     * instance variable for rmi port.
     */
    @Value("${rmi.port}")
    protected String rmiPort;
    /**
     * instance variable for g2w url.
     */
    @Value("${g2w.url}")
    protected String serviceUrl;
    /**
     * instance object for Endpoint
     */
    @Autowired
    protected Session session;
    /**
     * instance variable for g2w host.
     */
    @Value("${test.g2w.host}")
    protected String testG2wHost;

    /**
     * Method to initialize properties.
     */
    @Override
    public void afterPropertiesSet() {
        this.locale =
                StringUtils.parseLocaleString(this.propertyUtil.getProperty("environment.locale"));
        this.messages = new ResourceBundleMessageSource();
        this.messages.setBasenames(new String[]{"messages/attendee/messages", "messages/broker/messages-g2w",
                "messages/reports/messages-report"});
        this.logger.log("Service URL: " + this.serviceUrl);
    }

    /**
     * Method to set screenshot directory.
     * 
     * @param context
     *            (context)
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(final ITestContext context) {
        File outputDirectory = new File(context.getOutputDirectory());
        File screenShotsDirectory = new File(outputDirectory.getParentFile(), "html");
        if (!screenShotsDirectory.exists()) {
            screenShotsDirectory.mkdir();
        }
        this.logger.setScreenShotsDirectory(screenShotsDirectory);
    }

    /**
     * Method used to create unique string.
     * 
     * @return unique String
     */
    public String getUniqueString() {
        return new Date().getTime() + "-" + Thread.currentThread().getId();
    }
}
