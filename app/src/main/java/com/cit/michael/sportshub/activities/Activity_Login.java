package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.location.AppLocationManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class Activity_Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    Button btnLogOut;
    Context ctx;
    private FirebaseAuth auth;
    AppLocationManager appLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        auth = FirebaseAuth.getInstance();
        Log.d("GPS ACTLOG ", "CALLED");
        appLocationManager = new AppLocationManager(this);
        if(appLocationManager.canGetLocation()){
            appLocationManager.startLocation();
        }
        else{
            appLocationManager.showSettingsAlert();

        }


        if (auth.getCurrentUser() != null) {
            //user has already signed in
            Intent intent = new Intent(this, Activity_Main.class);
            intent.putExtra("user", "current");
            startActivity(intent);
            finish();

            Log.d("ACTIVITY LOGIN EST", auth.getCurrentUser().getEmail());
        } else {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.LoginTheme)
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            Log.d("ACTIVITY LOGIN EST", auth.getCurrentUser().getEmail());
            Intent intent = new Intent(this, Activity_Main.class);
            intent.putExtra("user", "new");
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

