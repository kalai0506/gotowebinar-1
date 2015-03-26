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
package com.citrix.g2w.webdriver.flows;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;

import com.citrix.comm.rpc.RpcResponse;
import com.citrix.g2w.webdriver.clients.CommService;
import com.citrix.g2w.webdriver.clients.Endpoint;

/**

 */
public class Session {

    /**
     * Inner Participant class to store participant details.
     */
    private class Participant {
        /**
         * Instance variable for email.
         */
        private final String email;
        /**
         * Instance DateTime object variable for join time.
         */
        private final DateTime joinTime;
        /**
         * Instance DateTime object variable for leave time.
         */
        private final DateTime leaveTime;
        /**
         * Instance variable for name.
         */
        private final String name;
        /**
         * Instance variable for participantId.
         */
        private final int participantId;

        /**
         * Constructor to initialize instance variables.
         * 
         * @param participantId
         *            (participant id)
         * @param email
         *            (email)
         * @param name
         *            (name)
         * @param joinTime
         *            (attendee join time)
         * @param leaveTime
         *            (attendee leave time)
         */
        public Participant(final int participantId, final String email, final String name,
                final DateTime joinTime, final DateTime leaveTime) {
            this.participantId = participantId;
            this.name = name;
            this.email = email;
            this.joinTime = joinTime;
            this.leaveTime = leaveTime;
        }

        /**
         * Method to get email id.
         * 
         * @return email
         */
        private String getEmail() {
            return this.email;
        }

        /**
         * Method to get join time.
         * 
         * @return joinTime
         */
        private DateTime getJoinTime() {
            return this.joinTime;
        }

        /**
         * Method to get participant leave time.
         * 
         * @return leaveTime
         */
        private DateTime getLeaveTime() {
            return this.leaveTime;
        }

        /**
         * Method to get name.
         * 
         * @return name
         */
        private String getName() {
            return this.name;
        }

        /**
         * Method to get participant id.
         * 
         * @return participantId
         */
        private int getParticipantId() {
            return this.participantId;
        }
    }

    /**
     * Inner WebinarSession class to store webinar session details.
     */
    private class WebinarSession {
        private final String delegationToken;
        private DateTime endTime;
        private int participantId = 0;
        private final Set<Participant> participants = new HashSet<Participant>();
        private final Long sessionId;
        private final DateTime startTime;

        /**
         * Constructor to initialize instance variables.
         * 
         * @param sessionId
         *            (session id)
         * @param delegationToken
         *            (delegation token)
         * @param startTime
         *            (start time)
         */
        public WebinarSession(final Long sessionId, final String delegationToken,
                final DateTime startTime) {
            this.sessionId = sessionId;
            this.delegationToken = delegationToken;
            this.startTime = startTime;
        }

        /**
         * Method to add attendee and get participant id.
         * 
         * @param email
         *            (email)
         * @param name
         *            (name)
         * @param joinTime
         *            (join time)
         * @param leaveTime
         *            (leave time)
         * @return participantId
         */
        public int addAttendee(String email, String name, DateTime joinTime, DateTime leaveTime) {
            this.participantId = this.participantId + 1;
            this.participants.add(new Participant(this.participantId, email, name, joinTime,
                    leaveTime));
            return this.participantId;
        }

        /**
         * Method to get delegation token.
         * 
         * @return delegationToken
         */
        private String getDelegationToken() {
            return this.delegationToken;
        }

        /**
         * Method to get participants.
         * 
         * @return participants
         */
        private Set<Participant> getParticipants() {
            return this.participants;
        }

        /**
         * Method to get session id.
         * 
         * @return sessionId
         */
        private Long getSessionId() {
            return this.sessionId;
        }

        /**
         * Method to get start time.
         * 
         * @return startTime
         */
        private DateTime getStartTime() {
            return this.startTime;
        }
    }

    /**
     * Instance variable for audioProblemURL.
     */
    private String audioProblemURL;
    /**
     * Instance variable for broker id.
     */
    @Value("${commService.systemId}")
    private String brokerId;
    /**
     * Instance object variable for CommService.
     */
    private CommService commService;
    /**
     * Instance variable for egwHost.
     */
    @Value("${g2w.egwHost}")
    private String egwHost;
    /**
     * Instance variable for egwPort.
     */
    @Value("${g2w.egwPort}")
    private int egwPort;
    /**
     * Instance object variable for EndPoint.
     */
    private Endpoint endpoint;
    /**
     * Instance variable for exitPopUpUrl.
     */
    private String exitPopUpUrl;
    /**
     * Instance list object variable for queue service server list.
     */
    @Value("${queueService.serverList}")
    private String queueServiceServerList;
    /**
     * Instance map object variable to store webinar sessions.
     */
    private final Map<Long, WebinarSession> sessions = new HashMap<Long, WebinarSession>();

    /**
     * Method to add attendee to a session
     * 
     * @param webinarId
     *            Webinar Id
     * @param registrationId
     *            Registration ID of the attendee
     * @param email
     *            Attendee email
     * @param name
     *            Attendee name
     * @param joinTime
     *            Attendee join time
     * @param leaveTime
     *            Attendee leave time
     */
    public void addAttendee(final Long webinarId, final Long registrationId, final String email,
            final String name, DateTime joinTime, DateTime leaveTime) {
        RpcResponse rpcResponse = this.endpoint.joinWebinar(null, registrationId, webinarId);
        try {
            this.exitPopUpUrl = rpcResponse.getParams().getString("ExitPopUpUrl");
        } catch (Exception e) {
            // no operation
        }

        if (joinTime == null) {
            joinTime = DateTime.now();
        }
        WebinarSession session = this.sessions.get(webinarId);
        int participantId = session.addAttendee(email, name, joinTime, leaveTime);
        this.endpoint.addAttendeeInfo(webinarId, participantId, email, name);
    }

    /**
     * Method to end webinar
     * 
     * @param webinarId
     *            Webinar Id
     */
    public void endWebinar(final Long webinarId) {
        this.endWebinar(webinarId, DateTime.now());
    }

    /**
     * Method to end webinar
     * 
     * @param webinarId
     *            Webinar Id
     * @param endTime
     *            Fake end time to use in session outcome event
     */
    public void endWebinar(final Long webinarId, final DateTime endTime) {
        WebinarSession webinarSession = this.sessions.get(webinarId);
        this.endpoint.endWebinar(webinarId, webinarSession.getDelegationToken(), null);
        if (!webinarSession.getParticipants().isEmpty()) {
            this.commService.createSession(webinarSession.getSessionId(), webinarSession
                    .getStartTime().getMillis(), endTime.getMillis());
            for (Participant participant : webinarSession.getParticipants()) {
                DateTime leaveTime = (participant.getLeaveTime() != null) ? participant
                        .getLeaveTime() : endTime;
                        this.commService.addParticipantToSession(webinarSession.getSessionId(),
                                participant.getParticipantId(), participant.joinTime.getMillis(),
                                leaveTime.getMillis());
            }

            this.commService.sendSessionOutcomeEvent(webinarSession.getSessionId());
        }
    }

    /**
     * Method to get archiving parameters.
     * 
     * @param bat
     *            (BAT/Auth Token of the user)
     * @return RpcResponse
     */
    public RpcResponse getArchivingParameters(final String bat) {
        RpcResponse rpcResponse = this.endpoint.getArchivingParameters(bat);
        return rpcResponse;
    }

    /**
     * Method to get Audio Problem URL.
     * 
     * @return audioProblemURL
     */
    public String getAudioProblemURL() {
        if (this.audioProblemURL != null && this.audioProblemURL.length() > 0) {
            return this.audioProblemURL;
        } else {
            return null;
        }
    }

    /**
     * Method to get survey exit pop up Url.
     * 
     * @return exitPopUpUrl (survey test pop up url after the end of
     *         attendee)
     */
    public String getSurveyExitPopUpUrl() {
        return this.exitPopUpUrl;
    }

    /**
     * Method used to get material management parameters.
     * 
     * @param bat
     *            (BAT/Auth Token of the user)
     * @param webinarKey
     *            (webinar key)
     * @return rpcResponse
     */
    public RpcResponse getMaterailManagementParameters(final String bat, final Long webinarKey) {
        RpcResponse rpcResponse = this.endpoint.getMaterailManagementParameters(bat, webinarKey);
        return rpcResponse;

    }

    /**
     * Method used to get material management url.
     * 
     * @param bat
     *            (BAT/Auth Token of the user)
     * @param webinarKey
     *            (webinar key)
     * @return material management url
     */
    public String getMaterialManagementUrl(final String bat, final Long webinarKey) {
        RpcResponse rpcResponse = this.endpoint.getMaterailManagementParameters(bat, webinarKey);
        return rpcResponse.getParams().getString("ManagementURL");
    }

    /**
     * Method used to get upload url.
     * 
     * @param bat
     *            (BAT/Auth Token of the user)
     * @return upload url
     */
    public String getUploadUrl(final String bat) {
        RpcResponse rpcResponse = this.endpoint.getArchivingParameters(bat);
        return rpcResponse.getParams().getString("UploadURL");
    }

    /**
     * Method to initialize instance variables.
     */
    @PostConstruct
    public void init() {
        this.endpoint = new Endpoint(this.egwHost, this.egwPort);
        this.commService = new CommService(this.queueServiceServerList, this.brokerId);
    }

    /**
     * Method to start webinar
     * 
     * @param bat
     *            BAT/Auth Token of the user
     * @param userKey
     *            User Key
     * @param webinarId
     *            Webinar Id
     */
    public void startWebinar(final String bat, final Long userKey, final Long webinarId) {
        this.startWebinar(bat, userKey, webinarId, DateTime.now());
    }

    /**
     * Method to start webinar
     * 
     * @param bat
     *            BAT/Auth Token of the user
     * @param userKey
     *            User Key
     * @param webinarId
     *            Webinar Id
     * @param startTime
     *            Fake start time to use in session outcome event
     */
    public void startWebinar(final String bat, final Long userKey, final Long webinarId,
            final DateTime startTime) {
        RpcResponse startResponse = this.endpoint.joinWebinar(bat, userKey, webinarId);
        Long sessionId = startResponse.getParams().getLong("SessionId");
        String dt = startResponse.getParams().getString("DT");
        this.audioProblemURL = startResponse.getParams().getString("AudioProblemURL");
        this.sessions.put(webinarId, new WebinarSession(sessionId, dt, startTime));
    }
}
