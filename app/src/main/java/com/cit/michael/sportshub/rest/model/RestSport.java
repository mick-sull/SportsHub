package com.cit.michael.sportshub.rest.model;

import com.cit.michael.sportshub.model.Sport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by micha on 17/01/2017.
 */

public class RestSport implements Observer {

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

    @Override
    public void update(Observable observable, Object data) {

    }
}
