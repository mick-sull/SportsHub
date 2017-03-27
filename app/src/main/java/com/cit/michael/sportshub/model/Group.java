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

    @SerializedName("group_data")
    @Expose
    @Nullable
    private ArrayList<String> group_data;

    @SerializedName("fullUserName")
    @Expose
    @Nullable
    private String fullUserName;

    @SerializedName("active")
    @Expose
    @Nullable
    private int active;


    public Group(Integer groupId, String groupName, ArrayList<String> group_data, String fullUserName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.group_data = group_data;
        this.fullUserName = fullUserName;
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
    public ArrayList<String> getGroup_data() {
        return group_data;
    }

    public void setGroup_data(@Nullable ArrayList<String> friends_tokens) {
        this.group_data = friends_tokens;
    }
}