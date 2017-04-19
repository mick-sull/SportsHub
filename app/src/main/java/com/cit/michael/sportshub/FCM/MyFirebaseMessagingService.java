package com.cit.michael.sportshub.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cit.michael.sportshub.MyService;
import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.activities.Activity_Event_Details;
import com.cit.michael.sportshub.activities.Activity_Profile;
import com.cit.michael.sportshub.chat.ui.Activity_Chat;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestProfile;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cit.michael.sportshub.Constants.NOTIFCATION_ACTIVITY;
import static com.cit.michael.sportshub.Constants.NOTIFCATION_CHAT;
import static com.cit.michael.sportshub.Constants.NOTIFCATION_EVENT_ID;
import static com.cit.michael.sportshub.Constants.NOTIFCATION_EVENT_REQUEST;
import static com.cit.michael.sportshub.Constants.NOTIFCATION_FRIEND_REQUEST;
import static com.cit.michael.sportshub.Constants.NOTIFCATION_GROUP_ID;
import static com.cit.michael.sportshub.Constants.NOTIFCATION_GROUP_REQUEST;
import static com.cit.michael.sportshub.Constants.NOTIFCATION_TYPE;
import static com.cit.michael.sportshub.Constants.NOTIFCATION_USER_ID;
import static com.cit.michael.sportshub.Constants.SP_NOTIFICATIONS;
import static com.cit.michael.sportshub.Constants.SP_SETTINGS;
import static com.cit.michael.sportshub.activities.Activity_Main.chat_active;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    NetworkService service;
    SharedPreferences prefs;
    //SharedPreferences sp = getSharedPreferences(APP_INFO, MODE_PRIVATE);
   /* SharedPreferences sharedPreferences =this.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
    Boolean isChatActivityOpen = sharedPreferences.getBoolean(CHAT_ACTIVITY_OPEN, false);*/
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        prefs = getSharedPreferences(SP_SETTINGS, Context.MODE_PRIVATE);
        if(prefs.getBoolean(SP_NOTIFICATIONS, true)) {
            service = RestClient.getSportsHubApiClient();
            Log.d("CHATISSUE", "NOTIFCATION_ACTIVITY1" + remoteMessage.getData().get(NOTIFCATION_ACTIVITY + "is equal to " + NOTIFCATION_CHAT));
            // if(remoteMessage.getData().get(NOTIFCATION_ACTIVITY).equals(NOTIFCATION_CHAT) && !chat_active){
            if (remoteMessage.getData().get(NOTIFCATION_TYPE).equals(NOTIFCATION_CHAT) && !chat_active) {
                sendChatNotification(remoteMessage);
            } else if (remoteMessage.getData().get(NOTIFCATION_TYPE).equals(NOTIFCATION_FRIEND_REQUEST)) {
                sendFriendRequestNotifcations(remoteMessage);
            } else if (remoteMessage.getData().get(NOTIFCATION_TYPE).equals(NOTIFCATION_EVENT_REQUEST)) {
                Log.d("CHATISSUE", "Event ID: " + remoteMessage.getData().get(NOTIFCATION_EVENT_ID));
                sendEventNotifcation(remoteMessage);
            } else if (remoteMessage.getData().get(NOTIFCATION_TYPE).equals(NOTIFCATION_GROUP_REQUEST)) {
                Log.d("CHATISSUE", "Event ID: " + remoteMessage.getData().get(NOTIFCATION_EVENT_ID));
                sendGroupInvite(remoteMessage);
            } else {
                Log.d(TAG, "Activity: " + remoteMessage.getData().get(NOTIFCATION_ACTIVITY) + " is not equal to " + NOTIFCATION_CHAT);
            }


            // TODO(developer): Handle FCM messages here.
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendChatNotification method below.
    }

    private void sendGroupInvite(RemoteMessage remoteMessage) {
        Intent indentAcceptGroupRequest = new Intent(getApplicationContext(), MyService.class);
        indentAcceptGroupRequest.setAction(MyService.ACCECP_GROUP_INVITE);
        //indentAcceptGroupRequest.putExtra("user_id", remoteMessage.getData().get(NOTIFCATION_USER_ID));
        indentAcceptGroupRequest.putExtra("group_id", remoteMessage.getData().get(NOTIFCATION_GROUP_ID));
        indentAcceptGroupRequest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent piAcceptFriendRequest = PendingIntent.getService(getApplicationContext(), 0, indentAcceptGroupRequest, /*PendingIntent.FLAG_UPDATE_CURRENT*/ PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.logo_googleg_color_18dp)
                .setContentTitle("SportsHub")
                .setContentText(/*user.getUserFullName() +": " +*/ remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                //.setContentIntent(pendingIntentReqProfile);
                .addAction(R.drawable.ic_check_black_24dp,"Accept",piAcceptFriendRequest);



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }

    private void sendEventNotifcation(RemoteMessage remoteMessage) {
        Intent indentEventReq = new Intent(getApplicationContext(), Activity_Event_Details.class);
        Log.d("CHATISSUE2", "Event ID: " + (Integer.parseInt(remoteMessage.getData().get(NOTIFCATION_EVENT_ID).toString())));


        indentEventReq.putExtra("eventSelected",  (Integer.parseInt(remoteMessage.getData().get(NOTIFCATION_EVENT_ID).toString())));




        indentEventReq.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentEventRequest = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, indentEventReq,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.logo_googleg_color_18dp)
                .setContentTitle("SportsHub")
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                //.setContentIntent(pendingIntentReqProfile);
                //.setContentIntent(pendingIntentEventRequest)
                .addAction(R.mipmap.date_icon,"View Event",pendingIntentEventRequest);



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private void sendFriendRequestNotifcations(final RemoteMessage remoteMessage) {
        service.getUser(remoteMessage.getData().get(NOTIFCATION_USER_ID)).enqueue(new Callback<RestProfile>() {
            @Override
            public void onResponse(Call<RestProfile> call, Response<RestProfile> response) {
                Log.d(TAG, "Getting user: " + remoteMessage.getData().get(NOTIFCATION_USER_ID));
                Intent indentReqProfile = new Intent(getApplicationContext(), Activity_Profile.class);
                User user = response.body().getUser().get(0);
                indentReqProfile.putExtra("user_id", remoteMessage.getData().get(NOTIFCATION_USER_ID));
                Log.d(TAG, "USER: " + user.getUserFullName());
                indentReqProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntentReqProfile = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, indentReqProfile,
                        PendingIntent.FLAG_ONE_SHOT);


                Intent indentAcceptFriendRequest = new Intent(getApplicationContext(), MyService.class);
                indentAcceptFriendRequest.setAction(MyService.ACTION1);
                indentAcceptFriendRequest.putExtra("user_id", remoteMessage.getData().get(NOTIFCATION_USER_ID));
                indentAcceptFriendRequest.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent piAcceptFriendRequest = PendingIntent.getService(getApplicationContext(), 0, indentAcceptFriendRequest, /*PendingIntent.FLAG_UPDATE_CURRENT*/ PendingIntent.FLAG_ONE_SHOT);




                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.logo_googleg_color_18dp)
                        .setContentTitle("SportsHub")
                        .setContentText(/*user.getUserFullName() +": " +*/ remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        //.setContentIntent(pendingIntentReqProfile);
                        .addAction(R.drawable.ic_person_outline_black_24dp,"View Profile",pendingIntentReqProfile)
                        .addAction(R.drawable.ic_check_black_24dp,"Accept",piAcceptFriendRequest);



                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

            }

            @Override
            public void onFailure(Call<RestProfile> call, Throwable t) {
                Log.d(TAG, "Getting user: " + " failed to get user error: " + t.toString());
            }
        });
    }

    public void confirmFriendRequest(){

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message body received.
     */
    private void sendChatNotification(final RemoteMessage remoteMessage) {
        Log.d("CHATISSUE", "NOTIFCATION_ACTIVITY2"  + remoteMessage.getData().get(NOTIFCATION_ACTIVITY));

       // if(remoteMessage.getData().get(NOTIFCATION_ACTIVITY).equals(NOTIFCATION_CHAT)){
            Log.d("CHATISSUE", "FIREBASE CREATING NOTIFICAITON" );
            service.getUser(remoteMessage.getData().get(NOTIFCATION_USER_ID)).enqueue(new Callback<RestProfile>() {
                @Override
                public void onResponse(Call<RestProfile> call, Response<RestProfile> response) {
                    Log.d(TAG, "Getting user: " + remoteMessage.getData().get(NOTIFCATION_USER_ID));
                    Intent intent = new Intent(getApplicationContext(), Activity_Chat.class);
                    User user = response.body().getUser().get(0);
                    intent.putExtra("receivingUser", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                            PendingIntent.FLAG_ONE_SHOT);

                    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.logo_googleg_color_18dp)
                            .setContentTitle("SportsHub")
                            .setContentText(user.getUserFullName() +": " + remoteMessage.getData().get("message"))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

                }

                @Override
                public void onFailure(Call<RestProfile> call, Throwable t) {
                    Log.d(TAG, "Getting user: " + " failed to get user error: " + t.toString());
                }
            });


    }

}
