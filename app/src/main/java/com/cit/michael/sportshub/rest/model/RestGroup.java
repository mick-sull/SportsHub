package com.cit.michael.sportshub.rest.model;

import android.support.annotation.Nullable;

import com.cit.michael.sportshub.model.Group;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by micha on 23/03/2017.
 */

public class RestGroup {

    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Message")
    @Expose
    private String message;

    @Nullable
    @SerializedName("Group")
    @Expose
    private List<Group> group = null;




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

    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
    }

}