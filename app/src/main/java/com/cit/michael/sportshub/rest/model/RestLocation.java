package com.cit.michael.sportshub.rest.model;

/**
 * Created by micha on 18/01/2017.
 */

import com.cit.michael.sportshub.model.Location;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestLocation {

    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Location")
    @Expose
    private List<Location> location = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public RestLocation() {
    }

    /**
     *
     * @param message
     * @param error
     * @param location
     */
    public RestLocation(Boolean error, String message, List<Location> location) {
        super();
        this.error = error;
        this.message = message;
        this.location = location;
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

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

}