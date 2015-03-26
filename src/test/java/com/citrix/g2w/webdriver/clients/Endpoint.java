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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.citrix.comm.VersionNotSupportedException;
import com.citrix.comm.rpc.MContainer;
import com.citrix.comm.rpc.MContainerDeserializerImpl;
import com.citrix.comm.rpc.MContainerSerializerImpl;
import com.citrix.comm.rpc.ProcedureImpl;
import com.citrix.comm.rpc.RpcPacket;
import com.citrix.comm.rpc.RpcRequest;
import com.citrix.comm.rpc.RpcRequestHandler;
import com.citrix.comm.rpc.RpcRequestImpl;
import com.citrix.comm.rpc.RpcResponse;
import com.citrix.comm.rpc.RpcResponseHandler;
import com.citrix.comm.rpc.SerializationException;
import com.citrix.comm.stack.egw.CommConfigImpl;
import com.citrix.comm.stack.egw.RpcCommStackFactoryImpl;
import com.citrix.comm.stack.egw.RpcCommStackImpl;
import com.citrix.util.ThreadPoolBeanImpl;

/**
 * @author: 
 * @since: 
 */
public class Endpoint {
    public static class DummyRpcRequestHandler implements RpcRequestHandler {
        @Override
        public void execute(RpcRequest request, RpcResponseHandler handler) throws IOException,
        SerializationException {
            // 
        }
    }

    /**
     * Instance object variable for RpcCommStackFactoryImpl.
     */
    protected static RpcCommStackFactoryImpl rpcCommStackFactory;
    /**
     * Static block to initialize default values.
     */
    static {
        MContainerDeserializerImpl mContainerDeserializer = new MContainerDeserializerImpl();
        mContainerDeserializer.setPoolSize(25);
        mContainerDeserializer.afterPropertiesSet();

        ThreadPoolBeanImpl threadPoolBean = new ThreadPoolBeanImpl();
        threadPoolBean.setCorePoolSize(20);
        threadPoolBean.setMaximumPoolSize(20);
        threadPoolBean.afterPropertiesSet();

        CommConfigImpl commConfig = new CommConfigImpl();
        commConfig.setKeepaliveReceiveTimeout(60000);
        commConfig.setKeepaliveSendTimeout(5000);
        commConfig.setSendAckInterval(5);
        commConfig.setSendWindow(4);

        rpcCommStackFactory = new RpcCommStackFactoryImpl();
        rpcCommStackFactory.setRpcRequestHandler(new DummyRpcRequestHandler());
        rpcCommStackFactory.setMContainerDeserializer(mContainerDeserializer);
        rpcCommStackFactory.setMContainerSerializer(new MContainerSerializerImpl());
        rpcCommStackFactory.setThreadPool(threadPoolBean);
        rpcCommStackFactory
        .setKeepAliveScheduledExecutorService(new ScheduledThreadPoolExecutor(5));
        rpcCommStackFactory.setCommConfig(commConfig);
    }

    /**
     * Method to get protocol version.
     * 
     * @param is
     *            (input stream)
     * @return version (protocol version)
     * @throws IOException
     */
    protected static int getProtocolVersion(final InputStream is) throws IOException {
        int version = 1;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null && line.length() > 0) {
            String[] split = line.split(":");
            if (split.length == 2 && split[0].trim().equals("Protocol-Version")) {
                version = Integer.parseInt(split[1].trim());
            }
        }
        return version;
    }

    /**
     * Instance object variable for RpcCommStackImpl.
     */
    protected RpcCommStackImpl rpcCommStack;
    /**
     * instance variable for version.
     */
    protected int version;

    /**
     * Constructor for Endpoint with hostname and rpc port
     * 
     * @param egwHost
     *            VIP of the broker
     * @param egwPort
     *            RPC port
     */
    public Endpoint(final String egwHost, final int egwPort) {
        try {
            Socket socket = new Socket(egwHost, egwPort);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.print("GET /EGW_Hello?supported_versions=1&supported_versions=2&egw_id=Egw"
                    + Math.random() + " HTTP/1.0\r\n\r\n");
            printWriter.flush();
            this.version = getProtocolVersion(socket.getInputStream());
            this.rpcCommStack = (RpcCommStackImpl) rpcCommStackFactory.getEgwRpcCommStack(
                    "G2WRPCTest_" + System.currentTimeMillis(), this.version);
            this.rpcCommStack.setIgnoreSourceAddress(true);
            this.rpcCommStack.connect(socket);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (VersionNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to add attendee info the broker
     * 
     * @param webinarId
     *            Webinar Id
     * @param participantId
     *            Participant Id
     * @param email
     *            Attendee email
     * @param name
     *            Attendee name
     * @return RpcResponse
     */
    public RpcResponse addAttendeeInfo(final Long webinarId, final int participantId,
            final String email, final String name) {
        MContainer mContainer = new MContainer();
        mContainer.put("MeetingId", webinarId);
        mContainer.put("ParticipantId", participantId);
        mContainer.put("Email", email);
        mContainer.put("Name", name);
        return this.makeRpcRequest(new ProcedureImpl("WebinarAttendeeRPC", 1, "addAttendeeInfo"),
                mContainer);
    }

    /**
     * Method to end webinar
     * 
     * @param webinarId
     *            Webinar Id
     * @param delegationToken
     *            Delegation token obtained on start webinar
     * @param reason
     *            Reason
     * @return RpcResponse
     */
    public RpcResponse endWebinar(final Long webinarId, final String delegationToken,
            final String reason) {
        MContainer mContainer = new MContainer();
        mContainer.put("AMID", webinarId);
        mContainer.put("DT", delegationToken);
        mContainer.put("Reason", reason);
        return this.makeRpcRequest(new ProcedureImpl("WebinarRPC", 1, "endWebinar"), mContainer);
    }

    /**
     * Method to get archiving parameters.
     * 
     * @param bat
     *            (BAT/Auth Token of the user)
     * @return rpcResponse
     * 
     * @author Prabhu Selvakumar
     * @since Feb 26, 2014
     */
    public RpcResponse getArchivingParameters(final String bat) {
        MContainer mContainer = new MContainer();
        mContainer.put("BAT", bat);
        ProcedureImpl procedure = new ProcedureImpl("ArchivingRPC", 1, "getArchivingParameters");
        RpcResponse rpcResponse = this.makeRpcRequest(procedure, mContainer);
        return rpcResponse;
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
        MContainer mContainer = new MContainer();
        mContainer.put("BAT", bat);
        mContainer.put("WebinarKey", webinarKey);
        ProcedureImpl procedure = new ProcedureImpl("MaterialRPC", 1, "getMaterialManagementURL");
        RpcResponse rpcResponse = this.makeRpcRequest(procedure, mContainer);
        return rpcResponse;
    }

    /**
     * Method to join webinar
     * 
     * @param bat
     *            Bat/Auth token if the user is organizer
     * @param userKey
     *            user key if organizer, registration id if attendee
     * @param webinarId
     *            webinar Id
     * @return RpcResponse
     */
    public RpcResponse joinWebinar(final String bat, final Long userKey, final Long webinarId) {
        MContainer mContainer = new MContainer();
        mContainer.put("BAT", bat);
        mContainer.put("UserID", userKey);
        mContainer.put("MeetingId", webinarId);
        mContainer.put("ExitPopUpUrl", "");
        mContainer.put("MachineId", "12345");
        mContainer.put("WebinarMode", "Start");
        return this.makeRpcRequest(new ProcedureImpl("WebinarRPC", 1, "joinWebinar"), mContainer);
    }

    /**
     * Method to make rpc request to mock egw.
     * 
     * @param procedure
     *            (ProcedureImpl object)
     * @param mContainer
     *            (MContainer object)
     * @return rpcResponse
     */
    private RpcResponse makeRpcRequest(final ProcedureImpl procedure, final MContainer mContainer) {
        try {
            mContainer.put("commProtocolVersion", 16);
            RpcRequestImpl rpcRequest = new RpcRequestImpl(mContainer, this.rpcCommStack.getId());
            rpcRequest.getRpcPacket().getHeader()
            .put(RpcPacket.MN_SOURCE_ADDRES, "" + System.currentTimeMillis());
            rpcRequest.setProtocolVersion(this.version);
            rpcRequest.setSourceIP(InetAddress.getLocalHost().getHostAddress());
            rpcRequest.setRequestRoute(new MContainer());
            rpcRequest.setProcedure(procedure);
            rpcRequest.setMessageId(1);
            rpcRequest.setSourceIP("192.233.12.54");
            RpcResponse rpcResponse = this.rpcCommStack.send(rpcRequest, 50000);
            return rpcResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
