package com.cit.michael.sportshub;

/**
 * Created by micha on 28/02/2017.
 */

public class Constants {

    //User-to-user relationships
    public final static int UNFRIEND = -1;
    public final static int PENDING_REQUEST = 0;
    public final static int ACCEPTED = 1;
    public final static int DECLINED = 2;
    public final static int BLOCKED = 3;
    public final static int CANCELLED = 4;


    //Notifcations
    public final static String NOTIFCATION_ACTIVITY = "open_activity";
    public final static String NOTIFCATION_TYPE = "type";
    public final static String NOTIFCATION_CHAT = "chat";
    public final static String NOTIFCATION_USER_ID = "sender_id";
    public final static String NOTIFCATION_GROUP_ID = "group_id";
    public final static String NOTIFCATION_FRIEND_REQUEST = "friend_request";
    public final static String NOTIFCATION_EVENT_REQUEST = "event_request";
    public final static String NOTIFCATION_EVENT_ID = "event_id";
    public final static String NOTIFCATION_GROUP_REQUEST = "group_request";

    //User_Friends_Adapter Actions
    public final static String ACTION_FRIENDS_LIST = "FriendsFrag";
    public final static String ACTION_GROUP_ADD_MEMBER = "groupAddMembers";
    public final static String ACTION_GROUP_REMOVE_MEMBER = "removeGroupMemeber";
    public final static int ACTION_GROUP_NOT_SELECTED = 0;
    public final static int ACTION_GROUP_SELECTED = 1;

    //USER_SETTINGS_SUBSCRIPTION
    public final static int SUBSCRIPTION_NOT_SELECTED = 1;
    public final static int SUBSCRIPTION_SELECTED = 0;

    //Check if chat activity is opened.
    public final static String CHAT_ACTIVITY_OPEN = "chatActivityActive";

    //Shared Pref
    public final static String APP_INFO = "appInfo";
}
