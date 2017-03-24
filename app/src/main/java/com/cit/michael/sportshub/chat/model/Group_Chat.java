package com.cit.michael.sportshub.chat.model;

/**
 * Created by micha on 23/03/2017.
 */

public class Group_Chat extends Chat {
    private String sender;
    private String senderUid;
    private String message;
    private String timestamp;
    private String profilePictureUrl;
    private String chatName;
    private String groupID;



    public Group_Chat() {}



    //public Group_Chat(String sender, String receiver, String senderUid, String receiverUid, String message, String timestamp, String profilePictureUrl, String receiverPictureUrl, String groupID) {
    public Group_Chat(String sender, String senderUid, String message, String timestamp, String profilePictureUrl, String chatName, String groupID) {
        this.sender = sender;
        //this.receiver = receiver;
        this.senderUid = senderUid;
        //this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;
        this.profilePictureUrl = profilePictureUrl;
        //this.receiverPictureUrl = receiverPictureUrl;
        this.chatName = chatName;
        this.groupID = groupID;
    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }




    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
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

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
