package com.cit.michael.sportshub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 21/02/2017.
 */
public class Friendship {

    @SerializedName("user_id")
    @Expose
    private String userId;


    @SerializedName("user_two_id")
    @Expose
    private String user_two_id;


    @SerializedName("user_two_id")
    @Expose
    private int status;


    @SerializedName("action_user_id")
    @Expose
    private String  action_user_id;

    public Friendship(String userId, String user_two_id, int status, String action_user_id) {
        this.userId = userId;
        this.user_two_id = user_two_id;
        this.status = status;
        this.action_user_id = action_user_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser_two_id() {
        return user_two_id;
    }

    public void setUser_two_id(String user_two_id) {
        this.user_two_id = user_two_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAction_user_id() {
        return action_user_id;
    }

    public void setAction_user_id(String action_user_id) {
        this.action_user_id = action_user_id;
    }
}
