package com.cit.michael.sportshub.FCM;

import android.util.Log;

import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by micha on 22/02/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    NetworkService service;
    private FirebaseAuth auth;
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        service = RestClient.getSportsHubApiClient();
        auth = FirebaseAuth.getInstance();
/*        if(auth != null){
            User user = new User();
            user.setUserId(auth.getCurrentUser().getUid());
            user.setUserToken(token);
            service.updateUserToken(user).enqueue(new Callback<RestUsers>() {
                @Override
                public void onResponse(Call<RestUsers> call, Response<RestUsers> response) {
                    Log.d("Token Update:",  "Token updated" );

                }

                @Override
                public void onFailure(Call<RestUsers> call, Throwable t) {
                    Log.d("Token Update Fail: ", t.toString());

                }
            });
        }*/

    }

}
