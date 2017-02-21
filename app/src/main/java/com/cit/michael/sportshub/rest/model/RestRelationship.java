package com.cit.michael.sportshub.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 21/02/2017.
 */

public class RestRelationship {
    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Message")
    @Expose
    private String message;

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


}
