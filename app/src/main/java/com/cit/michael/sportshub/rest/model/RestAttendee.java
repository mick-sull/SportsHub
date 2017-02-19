package com.cit.michael.sportshub.rest.model;

import com.cit.michael.sportshub.model.Attendee;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 10/02/2017.
 */

public class RestAttendee {
    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Sport")
    @Expose
    private Attendee attendee = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendees) {
        this.attendee = attendees;
    }
}
