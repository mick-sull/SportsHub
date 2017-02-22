package com.cit.michael.sportshub.rest.model;

import com.cit.michael.sportshub.model.Friendship;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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


    @SerializedName("Friendship")
    @Expose
    private List<Friendship> friendships = null;

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

    public List<Friendship> getFriendship() {
        return friendships;
    }

    public void getFriendships(List<Friendship> friendships) {
        this.friendships = friendships;
    }


}
