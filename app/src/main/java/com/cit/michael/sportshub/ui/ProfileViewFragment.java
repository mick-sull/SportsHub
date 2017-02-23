package com.cit.michael.sportshub.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.activities.Activity_Profile;
import com.cit.michael.sportshub.chat.ui.Activity_Chat;
import com.cit.michael.sportshub.model.Friendship;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestRelationship;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by micha on 14/02/2017.
 */

public class ProfileViewFragment extends DialogFragment {
    private ImageView profileImage,friendImage, chatImage;
    private ImageButton mMessage, mAdd, viewProfile;
    private String imageUrl, uID;
    private FirebaseAuth auth;
    NetworkService service;
    User user;
    List <Friendship>listOfFriendships;
    public int friendshipFoundIndex = -1;
    public String TAG = "ProfileDialog";
    Context ctx;
    Boolean currentlyFriends = false;
    final int APPROVED = 1;
    final int PENDING_REQUEST = 0;
    final int UNFRIEND = -1;



    /*    public ProfileViewFragment(String imageUrl, String uID ){
        this.imageUrl = imageUrl;
        this.uID = uID;
    }*/
    public ProfileViewFragment(User user, Context ctx ){
        //this.imageUrl = imageUrl;
        this.user = user;
        this.imageUrl = user.getUserProfileUrl();
        this.uID = user.getUserId();
        this.ctx = ctx;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.profile_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        profileImage = (ImageView)dialog.findViewById(R.id.imgDialogUserProfile);
        friendImage = (ImageView)dialog.findViewById(R.id.imgBtnAddUser);
        chatImage = (ImageView)dialog.findViewById(R.id.imgBtnMessage);
        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.img_circle_placeholder).resize(300,300).transform(new CircleTransform()).into(profileImage);

        listOfFriendships = new ArrayList<Friendship>();

        if(auth.getCurrentUser().getUid().equals(user.getUserId())){
            friendImage.setEnabled(false);
            friendImage.setImageDrawable(null);
            chatImage.setImageDrawable(null);
            chatImage.setEnabled(false);
        }

        service.getFriendshipStatus(auth.getCurrentUser().getUid()).enqueue(new Callback<RestRelationship>() {
            @Override
            public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                listOfFriendships = response.body().getFriendship();
                Log.d(TAG, "listOfFriendships size " + listOfFriendships.size());

                if(!listOfFriendships.isEmpty()) {
                    Log.d(TAG, "!listOfFriendships.isEmpty()");
                    for (int i = 0; i < listOfFriendships.size(); i++) {
                        Log.d(TAG, "FOR LOOP: 1: " + listOfFriendships.get(i).getUserId() + " - " + user.getUserId()  + " 2: " +  listOfFriendships.get(i).getUser_two_id() + " - " + user.getUserId());
                        if (listOfFriendships.get(i).getUserId().equals(user.getUserId()) || listOfFriendships.get(i).getUser_two_id().equals(user.getUserId())) {
                            friendshipFoundIndex = i;
                            Log.d(TAG, "Friendship found at index " + i);
                            if(listOfFriendships.get(i).getStatus() == APPROVED){
                                friendImage.setImageResource(R.drawable.ic_remove_friend);
                                currentlyFriends= true;

                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<RestRelationship> call, Throwable t) {

            }
        });


        dialog.findViewById(R.id.imgBtnMessage).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //Toast.makeText(getContext(), "Send Message to", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),Activity_Chat.class);
                //intent.putParcelableArrayListExtra("searchResults", (ArrayList<? extends Parcelable>) user);
                intent.putExtra("receivingUser",  user);
                startActivity(intent);
            }

        });

        dialog.findViewById(R.id.imgBtnViewProfile).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Toast.makeText(getContext(), "Opening Profile", Toast.LENGTH_SHORT).show();
                Log.d("TEST123", "View Profile");
                Intent intent = new Intent(getContext(), Activity_Profile.class);
                intent.putExtra("user_id", uID);
                startActivity(intent);

            }

        });

        dialog.findViewById(R.id.imgBtnAddUser).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //Toast.makeText(getContext(), "Friend Request Sent...", Toast.LENGTH_SHORT).show();
                doFriendships();

            }

        });
        
        return dialog;

    }

    private void doFriendships() {
        if (!currentlyFriends) {
            Friendship friendship = new Friendship(auth.getCurrentUser().getUid(), uID, PENDING_REQUEST, auth.getCurrentUser().getUid());
            Log.d(TAG, "User Selected:  " + user.getUserFullName() + " ID: " + user.getUserId());
            if (friendshipFoundIndex < 0) {
                service.sendFriendRequest(friendship).enqueue(new Callback<RestRelationship>() {
                    @Override
                    public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                        Toast.makeText(ctx, "Friend request sent...", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Friendship created.friendshipFoundIndex: " + friendshipFoundIndex);
                    }

                    @Override
                    public void onFailure(Call<RestRelationship> call, Throwable t) {

                    }
                });
            } else {//There is a friendship found between these two users.
                Log.d(TAG, "There is a friendship found between these two users");
                if (auth.getCurrentUser().getUid().equals(listOfFriendships.get(friendshipFoundIndex).getAction_user_id())) {
                    //Dont do anything as this user has already sent the request.
                    Toast.makeText(ctx, "Friend request already sent...", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Updating Friendship");
                    listOfFriendships.get(friendshipFoundIndex).setAction_user_id(auth.getCurrentUser().getUid());
                    listOfFriendships.get(friendshipFoundIndex).setStatus(APPROVED);
                    service.friendRequestResponse(listOfFriendships.get(friendshipFoundIndex)).enqueue(new Callback<RestRelationship>() {
                        @Override
                        public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                            Toast.makeText(ctx, "Now friends with " + user.getUserFullName(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<RestRelationship> call, Throwable t) {

                        }
                    });
                }
            }
        }
        else{//Unfriend user
            listOfFriendships.get(friendshipFoundIndex).setAction_user_id(auth.getCurrentUser().getUid());
            listOfFriendships.get(friendshipFoundIndex).setStatus(UNFRIEND);
            service.friendRequestResponse(listOfFriendships.get(friendshipFoundIndex)).enqueue(new Callback<RestRelationship>() {
                @Override
                public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                    Toast.makeText(ctx, "Unfriended " + user.getUserFullName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<RestRelationship> call, Throwable t) {

                }
            });
         }
    }

}



