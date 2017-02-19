package com.cit.michael.sportshub.rest.model;

/**
 * Created by micha on 19/01/2017.
 */


import com.cit.michael.sportshub.model.Event;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestEvent {

    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Event")
    @Expose
    private List<Event> event = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public RestEvent() {
    }

    /**
     *
     * @param message
     * @param error
     * @param event
     */
    public RestEvent(Boolean error, String message, List<Event> event) {
        super();
        this.error = error;
        this.message = message;
        this.event = event;
    }

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

    public List<Event> getEvent() {
        return event;
    }

    public void setEvent(List<Event> event) {
        this.event = event;
    }

}