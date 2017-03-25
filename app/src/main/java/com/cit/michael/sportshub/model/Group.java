package com.cit.michael.sportshub.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by micha on 23/03/2017.
 */

public class Group {

    @SerializedName("group_id")
    @Expose
    private Integer groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;

    @SerializedName("friends_invited")
    @Expose
    @Nullable
    private ArrayList<String> friends_tokens;

    public Group(Integer groupId, String groupName, ArrayList<String> friends_tokens) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.friends_tokens = friends_tokens;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Nullable
    public ArrayList<String> getFriends_tokens() {
        return friends_tokens;
    }

    public void setFriends_tokens(@Nullable ArrayList<String> friends_tokens) {
        this.friends_tokens = friends_tokens;
    }
}