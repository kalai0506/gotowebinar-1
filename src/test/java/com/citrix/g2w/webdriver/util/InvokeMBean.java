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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.collections.CollectionUtils;

/**
*/
public class InvokeMBean {

    /**
     * Method to invoke m bean.
     * 
     * @param userName
     *            (user name)
     * @param password
     *            (password)
     * @param rmiHost
     *            (rmi host)
     * @param rmiPort
     *            (rmi port)
     * @param objectName
     *            (object name)
     * @param methodName
     *            (method name)
     * @param params
     *            (array of objects)
     * @param signature
     *            (array of params type)
     * @return mBeanResult 
     * 			  (list of string containing mbean result)
     */
    public List<String> invokeMBean(final String userName, final String password, final String rmiHost,
            final int rmiPort, final String objectName, final String methodName,
            final Object[] params, final String[] signature) {
        List<String> mBeanResult = new ArrayList<String>(2);
        try {
            String[] credentials = { userName, password };

            Map<String, String[]> env = new HashMap<String, String[]>();
            env.put("jmx.remote.credentials", credentials);

            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://" + rmiHost + ":" + rmiPort
                    + "/jndi/rmi://" + rmiHost + ":" + rmiPort + "/jmxrmi");

            JMXConnector connector = JMXConnectorFactory.connect(url, env);

            MBeanServerConnection connection = connector.getMBeanServerConnection();
            ObjectName destConfigName = new ObjectName(objectName);

            Object returnValue = connection.invoke(destConfigName, methodName, params, signature);

            if (returnValue != null) {
                if (returnValue instanceof Collection) {
                    Collection c = (Collection) returnValue;
                    if(CollectionUtils.isNotEmpty(c)){
                        for (Object val : c) {
                        	mBeanResult.add(val.toString());
                        }
                    }
                } else {
                	mBeanResult.add(returnValue.toString());
                }
            }
            connector.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mBeanResult;
    }

    /**
     * method to trigger follow up email.
     * 
     * @param rmiUserName
     *            (rmi user name)
     * @param rmiPassword
     *            (rmi password)
     * @param rmiHost
     *            (rmi host)
     * @param rmiPort
     *            (rmi port)
     * @param webinarKey
     *            (webinar key)
     */
    public void triggerFollowUpEmail(final String rmiUserName, final String rmiPassword,
            final String rmiHost, final String rmiPort, final Long webinarKey) {
        // get jobs with name
        List<String> mBeanOutput = this
                .invokeMBean(
                        rmiUserName,
                        rmiPassword,
                        rmiHost,
                        Integer.parseInt(rmiPort),
                        "quartz.scheduler.quartzSchedulerDynamicTasks:name=com.citrix.g2w.services.email.jobs.FollowUpEmailJob",
                        "getJobsWithName", new Object[] { String.valueOf(webinarKey) },
                        new String[] { "java.lang.String" });
        //trigger jobs
        if( CollectionUtils.isNotEmpty(mBeanOutput)){
        	for(String mBeanOutputValue: mBeanOutput){
                this.invokeMBean(
                        rmiUserName,
                        rmiPassword,
                        rmiHost,
                        Integer.parseInt(rmiPort),
                        "quartz.scheduler.quartzSchedulerDynamicTasks:name=com.citrix.g2w.services.email.jobs.FollowUpEmailJob",
                        "triggerJob", new Object[] { mBeanOutputValue },
                        new String[] { "java.lang.String" });
        	}
        }
    }
    
    /**
     * method to trigger Reminder email.
     * 
     * @param rmiUserName
     *            (rmi user name)
     * @param rmiPassword
     *            (rmi password)
     * @param rmiHost
     *            (rmi host)
     * @param rmiPort
     *            (rmi port)
     * @param webinarKey
     *            (webinar key)
     */
    public void triggerReminderEmail(final String rmiUserName, final String rmiPassword,
            final String rmiHost, final String rmiPort, final Long webinarKey) {
        // get jobs with name
        List<String> mBeanOutput = this
                .invokeMBean(
                        rmiUserName,
                        rmiPassword,
                        rmiHost,
                        Integer.parseInt(rmiPort),
                        "quartz.scheduler.quartzSchedulerDynamicTasks:name=com.citrix.g2w.services.email.jobs.ReminderEmailJob",
                        "getJobsWithName", new Object[] { String.valueOf(webinarKey) },
                        new String[] { "java.lang.String" });
        //trigger jobs
        if( CollectionUtils.isNotEmpty(mBeanOutput)){
            for(String mBeanOutputValue: mBeanOutput){
                this.invokeMBean(
                        rmiUserName,
                        rmiPassword,
                        rmiHost,
                        Integer.parseInt(rmiPort),
                        "quartz.scheduler.quartzSchedulerDynamicTasks:name=com.citrix.g2w.services.email.jobs.ReminderEmailJob",
                        "triggerJob", new Object[] { mBeanOutputValue },
                        new String[] { "java.lang.String" });
            }
        }
    }
}
