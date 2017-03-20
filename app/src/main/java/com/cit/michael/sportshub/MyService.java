package com.cit.michael.sportshub;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cit.michael.sportshub.model.Friendship;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestRelationship;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cit.michael.sportshub.Constants.ACCEPTED;

/**
 * Created by micha on 20/03/2017.
 */

public class MyService extends IntentService {
    public static final String ACTION1 = "ACTION1";
    public static final String ACTION2 = "ACTION2";
    NetworkService service;
    private FirebaseAuth auth;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public MyService() {
        super("Update Friendship");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        final String action = intent.getAction();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Toast.makeText(this, "Now connected.", Toast.LENGTH_SHORT).show();
        if (ACTION1.equals(action)) {
            service = RestClient.getSportsHubApiClient();
            auth = FirebaseAuth.getInstance();
            String user_id = intent.getStringExtra("user_id");
            Friendship friendship = new Friendship(auth.getCurrentUser().getUid(), user_id, ACCEPTED, auth.getCurrentUser().getUid());
            service.friendRequestResponse(friendship).enqueue(new Callback<RestRelationship>() {
                @Override
                public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                    //Toast.makeText(getApplicationContext(), "Now friends with " +  listFriends.get(position).getUserFullName(), Toast.LENGTH_SHORT).show();
                    //listFriends.get(position).setStatus(ACCEPTED);
                    //mAdapter.notifyItemChanged(position, listFriends.get(position));
                }

                @Override
                public void onFailure(Call<RestRelationship> call, Throwable t) {

                }
            });
        } else if (ACTION2.equals(action)) {
            // do some other stuff...
        } else {
            throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}