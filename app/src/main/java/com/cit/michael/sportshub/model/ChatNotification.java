package com.cit.michael.sportshub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 01/03/2017.
 */

public class ChatNotification {
    @SerializedName("userFullName")
    @Expose
    private String userFullName;


    @SerializedName("userProfileUrl")
    @Expose
    private String userProfileUrl;

    @SerializedName("userToken")
    @Expose
    private String userToken;

    @SerializedName("sender_id")
    @Expose
    private String sender_id;


    @SerializedName("message")
    @Expose
    private String message;

    public ChatNotification(String userFullName, String userProfileUrl, String userToken, String sender_id, String message) {
        this.userFullName = userFullName;
        this.userProfileUrl = userProfileUrl;
        this.userToken = userToken;
        this.sender_id = sender_id;
        this.message = message;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
