package com.cit.michael.sportshub.rest.model;

import com.cit.michael.sportshub.model.Sport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by micha on 17/01/2017.
 */

public class RestSport {

    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Sport")
    @Expose
    private List<Sport> sport = null;



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

    public List<Sport> getSport() {
        return sport;
    }

    public void setSport(List<Sport> sport) {
        this.sport = sport;
    }

}
