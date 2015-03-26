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
package com.citrix.g2w.webdriver.pages.emails;

import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.RecipientStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import com.citrix.g2w.webdriver.pages.BasePage;
import com.google.common.collect.Lists;

/**
 * @author adas
 * Reused from g2t webdriver codebase
 * 
 * @author: Vaishnavi
 * @since: Jan 7, 2014
 */
public class ZimbraEmail extends BasePage {

    /**
     * instance variable for email id.
     */
    private String email;
    /**
     * instance variable for email Store.
     */
    private Store store;
    /**
     * instance variable for user id.
     */
    private String userId;

    /**
     * Constructor to instantiate instance variables.
     * 
     * @param email
     *            (email)
     */
    public ZimbraEmail(final String email) {
        this.email = email;
        String emailSuffix = this.propertyUtil.getProperty("test.g2w.mail.Suffix");
        this.userId = this.email.substring(0, this.email.indexOf("-")) + "@" + emailSuffix;
    }

    /**
     * Method used to delete an email based on email from address, to address
     * and subject.
     * 
     * @param fromEmailAddress
     *            (from email address)
     * @param toEmailAddress
     *            (to email address)
     * @param emailSubject
     *            (email subject)
     * @return boolean (value to confirm the message deletion)
     * @author Prabhu Selvakumar
     * @since Jan 21, 2014
     */

    public boolean deleteEmail(final String fromEmailAddress, final String toEmailAddress,
            final String emailSubject) {
        boolean messageStatus = false;
        try {
            Message inboxMessage = this.getEmail(emailSubject, toEmailAddress, fromEmailAddress);
            if (inboxMessage != null) {
                Folder inbox = this.store.getFolder("INBOX");
                inbox.open(Folder.READ_WRITE);
                inboxMessage.setFlag(Flags.Flag.DELETED, true);
                messageStatus = true;
                inbox.close(true);
            }
            return messageStatus;
        } catch (MessagingException mex) {
            return messageStatus;
        }
    }

    /**
     * Method to empty inbox.
     * 
     * @return isInboxEmpty
     */
    public boolean emptyInbox() {
        boolean isInboxEmpty = false;
        try {
            Folder inbox = this.store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            Message[] inboxMessages = inbox.getMessages();
            this.logger.log("Message count in Zimbra inbox before Clean up: "
                    + inbox.getMessageCount());
            if (inboxMessages != null) {
                for (Message inboxMessage : inboxMessages) {
                    inboxMessage.setFlag(Flags.Flag.DELETED, true);
                    isInboxEmpty = true;
                }
                inbox.close(true);
                this.logger.log("Message count in Zimbra inbox After Clean up: "
                        + inbox.getMessageCount());
            }
        } catch (MessagingException mex) {
            this.logger.log(mex.getMessage());
            isInboxEmpty = false;
        }
        this.logger.log("Is Zimbra Inbox Empty: " + isInboxEmpty);
        return isInboxEmpty;
    }
    
    /**
     * Method to check Zimbra Mail Inbox for a particular subject, from address
     * and to address.
     * 
     * @param subject
     *            (subject)
     * @param toEmailAddress
     *            (to email address)
     * @param fromEmailAddress
     *            (from email address)
     * @return Message
     */
    public Message getEmail(final String subject, final String toEmailAddress,
            final String fromEmailAddress) {
        Message searchMessage = null;
        int retryCount = Integer
                .parseInt(this.propertyUtil.getProperty("test.g2w.mail.retryCount"));
        int retrySleep = Integer
                .parseInt(this.propertyUtil.getProperty("test.g2w.mail.retrySleep"));
        searchMessage = this.getEmail(subject, toEmailAddress, fromEmailAddress, retryCount,
                retrySleep);
        return searchMessage;
    }

    /**
     * Method to check Zimbra Mail Inbox for a particular subject, from address
     * and to address with a specific retry count and retry sleep time.
     * 
     * @param subject
     *            (subject)
     * @param toEmailAddress
     *            (to email address)
     * @param fromEmailAddress
     *            (from email address)
     * @param retryCount
     *            (retry count)
     * @param retrySleep
     *            (retry sleep - in seconds)
     * @return Message
     */
    public Message getEmail(final String subject, final String toEmailAddress,
            final String fromEmailAddress, final int retryCount, final int retrySleep) {
        Message searchMessage = null;

        try {
            // Open the inbox
            Folder inbox = this.store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            for (int i = 0; i < retryCount; i++) {
                
                List<SearchTerm> searchTerms = Lists.newArrayList();
                // Search using from address
                if (fromEmailAddress != null) {
                    FromStringTerm fromSearch = new FromStringTerm(fromEmailAddress);
                    searchTerms.add(fromSearch);
                } 

                // Search using to address
                if (toEmailAddress != null) {
                    RecipientStringTerm toSearch = new RecipientStringTerm(RecipientType.TO,
                            toEmailAddress);
                    searchTerms.add(toSearch);
                } 

                // Search using subject
                if (subject != null) {
                    SubjectTerm subjectSearch = new SubjectTerm(subject);
                    searchTerms.add(subjectSearch);
                }
                
                Message[] messages = inbox.search(new AndTerm(searchTerms.toArray(new SearchTerm[0])));

                if (messages != null && messages.length == 1) {
                    this.logger.log("Mail with subject : \"" + subject + "\", from address : \""
                            + fromEmailAddress + "\" and to address : \"" + toEmailAddress
                            + "\" has been found");
                    searchMessage = messages[0];
                    return searchMessage;
                } else {
                    try {
                        Thread.sleep(retrySleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        this.logger.log("Mail with subject : \"" + subject + "\", from address : \""
                + fromEmailAddress + "\" and to address : \"" + toEmailAddress
                + "\" has not been found");
        return searchMessage;
    }

    /**
     * Method to login into Zimbra Mail Server.
     */
    public void login() {
        Properties props = new Properties();
        String serverType = this.propertyUtil.getProperty("test.g2w.mail.serverType");
        String mailServer = this.propertyUtil.getProperty("test.g2w.mail.server");
        String password = this.propertyUtil.getProperty("test.g2w.mail.password");
        props.setProperty("mail.store.protocol", serverType);
        try {
            Session session = Session.getInstance(props, null);
            this.store = session.getStore();
            this.store.connect(mailServer, this.userId, password);
            this.logger.log("Logged into Zimbra mail as user : " + this.userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to logout and set class variables to null.
     */
    public void logout() {
        try {
            this.store.close();
            this.email = null;
            this.userId = null;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
