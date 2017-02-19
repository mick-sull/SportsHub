package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cit.michael.sportshub.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class Activity_Login extends AppCompatActivity {

    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    Button btnLogOut;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        auth = FirebaseAuth.getInstance();



        if (auth.getCurrentUser() != null) {
            //user has already signed in
            Intent intent = new Intent(this,Activity_Main.class);
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
                                    //new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build())
                            .build(),
                    RC_SIGN_IN);
            /*startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)//originally false
                    //.setLogo(R.mipmap.sportshub)
                    //.setTheme(R.style.LoginTheme)
                    .setProviders(
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.EMAIL_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER)
                    .build(), RC_SIGN_IN);
*/

        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void logout() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //user logged out
                        Log.d("AUTH", "User Logged Out");
                        finish();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            Log.d("ACTIVITY LOGIN EST", auth.getCurrentUser().getEmail());
            Intent intent = new Intent(this,Activity_Main.class);
            intent.putExtra("user", "new");
            startActivity(intent);
            finish();
        }
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
  /*      if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                startActivity(SignedInActivity.createIntent(this, response));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == AddressConstants.ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response);*/


/*        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                //user logged in
                auth.getCurrentUser().updateEmail(auth.getCurrentUser().getEmail());
                Log.d("ACTIVITY LOGIN EST", auth.getCurrentUser().getEmail());
                Intent intent = new Intent(this,Activity_Main.class);
                intent.putExtra("user", "new");
                startActivity(intent);
                finish();
            } else {
                //user not authenticatd
                Log.d("ACTIVITY LOGIN EST", "not authenticatd");
            }*/
        }
    }

