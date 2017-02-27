package com.cit.michael.sportshub.model;

/**
 * Created by micha on 26/02/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Conversation {

    @SerializedName("chat_id")
    @Expose
    private String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

}