package com.cit.michael.sportshub.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 30/03/2017.
 */

public class Subscription {

    @Nullable
    @SerializedName("subscriptionID")
    @Expose
    private String subscriptionID;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("sportID")
    @Expose
    private Integer sportID;
    @SerializedName("active")
    @Expose
    private Integer active;

    public Subscription(String subscriptionID, String userID, Integer sportID, Integer active) {
        this.subscriptionID = subscriptionID;
        this.userID = userID;
        this.sportID = sportID;
        this.active = active;
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getSportID() {
        return sportID;
    }

    public void setSportID(Integer sportID) {
        this.sportID = sportID;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}