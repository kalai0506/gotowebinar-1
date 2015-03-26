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
public class CorporateAccountProperties {

    /**
     * instance variable used to set admin organizer flag.
     */
    private boolean isAdminOrganiser = true;
    /**
     * instance variable used to set content sharing flag.
     */
    private boolean isContentSharingEnabled = false;
    /**
     * instance variale used to set isRecordingv2Enabled flag.
     */
    private boolean isRecordingv2Enabled = false;
    /**
     * instance variable used to set number of co-organizers.
     */
    private int seats = 2;

    /**
     * @return the seats
     */
    public int getSeats() {
        return this.seats;
    }

    /**
     * @return the isAdminOrganiser
     */
    public boolean isAdminOrganiser() {
        return this.isAdminOrganiser;
    }

    /**
     * @return the isContentSharingEnabled
     */
    public boolean isContentSharingEnabled() {
        return this.isContentSharingEnabled;
    }

    /**
     * @return the isRecordingv2Enabled
     */
    public boolean isRecordingv2Enabled() {
        return this.isRecordingv2Enabled;
    }

    /**
     * @param isAdminOrganiser
     *            the isAdminOrganiser to set
     */
    public void setAdminOrganiser(final boolean isAdminOrganiser) {
        this.isAdminOrganiser = isAdminOrganiser;
    }

    /**
     * @param isContentSharingEnabled
     *            the isContentSharingEnabled to set
     */
    public void setContentSharingEnabled(final boolean isContentSharingEnabled) {
        this.isContentSharingEnabled = isContentSharingEnabled;
    }

    /**
     * @param isRecordingv2Enabled the isRecordingv2Enabled to set
     */
    public void setRecordingv2Enabled(final boolean isRecordingv2Enabled) {
        this.isRecordingv2Enabled = isRecordingv2Enabled;
    }

    /**
     * @param seats
     *            the seats to set
     */
    public void setSeats(final int seats) {
        this.seats = seats;
    }
}
