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
package com.citrix.g2w.webdriver.clients;
import com.citrix.comm.SessionOutcomeEvent;
import com.citrix.queue.support.Cluster;
import com.citrix.queue.support.QueueServiceImpl;
import com.citrix.queue.support.RoundRobinProducer;
import com.google.common.collect.Lists;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 
 * @since: 
 */
public class CommService {
    private Long sessionId;
    private static RestTemplate restTemplate = new RestTemplate();
    private static QueueServiceImpl queueService = new QueueServiceImpl();
    private Long startTime;
    private Long endTime;
    private Cluster cluster = new Cluster();
    private String brokerSystemId;
    private List<SessionOutcomeEvent.ParticipantInfo> participantInfos = new ArrayList<SessionOutcomeEvent.ParticipantInfo>();
    private Map<Long, SessionOutcomeEvent> sessions = new HashMap<Long, SessionOutcomeEvent>();

    static {
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
        queueService.setRestTemplate(restTemplate);
    }

    /**
     * Constructor with server list and broker system id
     * @param serverList Comma-separated list of servers
     * @param brokerSystemId Broker system id
     */
    public CommService(String serverList, String brokerSystemId) {
        cluster.setServerList(serverList);
        this.brokerSystemId = brokerSystemId;
    }

    /**
     * Method to create session
     * @param sessionId sessionId
     * @param startTime start time of the session
     * @param endTime end time of the session
     */
    public void createSession(Long sessionId, Long startTime, Long endTime) {
        SessionOutcomeEvent event = new SessionOutcomeEvent();
        event.setSessionId(sessionId);
        if ( startTime != null ) {
            event.setStartTime(startTime);
        }
        if ( endTime != null ) {
            event.setEndTime(endTime);
        }

        sessions.put(sessionId, event);
    }

    /**
     * Method to add participant to session
     * @param sessionId Id of the session
     * @param participantId Id of the participant
     * @param joinTime Join time of the participant
     * @param leaveTime Leave time of the participant
     */
    public void addParticipantToSession(Long sessionId, int participantId, Long joinTime, Long leaveTime) {
        SessionOutcomeEvent event = sessions.get(sessionId);

        SessionOutcomeEvent.ParticipantInfo participantInfo = new SessionOutcomeEvent.ParticipantInfo();
        participantInfo.setId(participantId);
        participantInfo.setJoinTimestamp(joinTime);
        participantInfo.setLeaveTimestamp(leaveTime);

        List<SessionOutcomeEvent.ParticipantInfo> participantList;
        if (event.getParticipants() != null) {
            participantList = Lists.newArrayList(event.getParticipants());
        } else {
            participantList = new ArrayList<SessionOutcomeEvent.ParticipantInfo>();
        }

        participantList.add(participantInfo);
        event.setParticipants(participantList.toArray(new SessionOutcomeEvent.ParticipantInfo[participantList.size()]));

    }

    /**
     * Method to send session outcome event for a session
     * @param sessionId Id of the session
     */
    public void sendSessionOutcomeEvent(Long sessionId) {
        SessionOutcomeEvent event = sessions.get(sessionId);

        event.setTotalParticipants(event.getParticipants().length);
        event.setValidInteractions(event.getParticipants().length);
        event.setValidInteractionsAuthentication(event.getParticipants().length);
        event.setValidInteractionsScreenSharing(event.getParticipants().length);

        if (cluster.getServers() == null) {
            cluster.afterPropertiesSet();
        }

        List<Cluster> clusters = new ArrayList<Cluster>();
        clusters.add(cluster);

        RoundRobinProducer producer = new RoundRobinProducer();
        producer.setClusters(clusters);
        producer.setQueueService(queueService);
        producer.add("com.citrix.comm.SessionOutcome." + brokerSystemId +"+", 6000, event);
    }
}
