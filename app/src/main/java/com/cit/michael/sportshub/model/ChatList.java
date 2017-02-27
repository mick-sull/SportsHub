package com.cit.michael.sportshub.model;

/**
 * Created by micha on 25/02/2017.
 */

public class ChatList {


    private String userFullName;
    private String message;
    private String timstamp;
    private String imageUrl;
    private String chatID;


    public ChatList(String username, String message, String timstamp, String imageUrl) {
        this.userFullName = username;
        this.message = message;
        this.timstamp = timstamp;
        this.imageUrl = imageUrl;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUsername(String username) {
        this.userFullName = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimstamp() {
        return timstamp;
    }

    public void setTimstamp(String timstamp) {
        this.timstamp = timstamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }




}
