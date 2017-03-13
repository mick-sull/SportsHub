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

    //Notifcations
    public final static String NOTIFCATION_ACTIVITY = "open_activity";
    public final static String NOTIFCATION_CHAT = "Activity_Chat.class";
    public final static String NOTIFCATION_USER_ID = "sender_id";

    //Check if chat activity is opened.
    public final static String CHAT_ACTIVITY_OPEN = "chatActivityActive";

    //Shared Pref
    public final static String APP_INFO = "appInfo";
}
