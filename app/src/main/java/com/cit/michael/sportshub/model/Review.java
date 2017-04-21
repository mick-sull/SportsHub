package com.cit.michael.sportshub.model;

/**
 * Created by micha on 20/04/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("event_attendee_id")
    @Expose
    private Integer eventAttendeeId;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("approved")
    @Expose
    private Integer approved;
    @SerializedName("appearance")
    @Expose
    private Integer appearance;

    public Integer getEventAttendeeId() {
        return eventAttendeeId;
    }

    public void setEventAttendeeId(Integer eventAttendeeId) {
        this.eventAttendeeId = eventAttendeeId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getAppearance() {
        return appearance;
    }

    public void setAppearance(Integer appearance) {
        this.appearance = appearance;
    }
}