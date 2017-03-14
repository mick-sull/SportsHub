package com.cit.michael.sportshub.chat.model;

/**
 * Created by micha on 17/02/2017.
 */

public class Chat {
    private String sender;
    private String receiver;
    private String senderUid;
    private String receiverUid;
    private String message;
    private String timestamp;
    private String profilePictureUrl;
    private String receiverPictureUrl;



    public Chat() {}

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, String timestamp, String profilePictureUrl, String receiverPictureUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;
        this.profilePictureUrl = profilePictureUrl;
        this.receiverPictureUrl = receiverPictureUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getReceiverPictureUrl() {
        return receiverPictureUrl;
    }

    public void setReceiverPictureUrl(String receiverPictureUrl) {
        this.receiverPictureUrl = receiverPictureUrl;
    }
}
