package com.cit.michael.sportshub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 02/02/2017.
 */

public class Attendee {

    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("approved")
    @Expose
    private Integer approved;

    public Attendee(Integer eventId, String userId, Integer approved) {
        this.eventId = eventId;
        this.userId = userId;
        this.approved = approved;
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

}