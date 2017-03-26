package com.cit.michael.sportshub.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.adapter.User_Friends_Adapter;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class UserListFragment extends DialogFragment {
    private ImageView profileImage, friendImage, chatImage;
    private ImageButton mMessage, mAdd, viewProfile;
    private String imageUrl, uID;
    private FirebaseAuth auth;
    NetworkService service;
    User user;
    ArrayList<User> listOfUsers;
    public int friendshipFoundIndex = -1;
    public String TAG = "ProfileDialog";
    Context ctx;
    Boolean currentlyFriends = false;
    private RecyclerView recyclerView;
    private User_Friends_Adapter mAdapter;

/*    final int APPROVED = 1;
    final int PENDING_REQUEST = 0;
    final int UNFRIEND = -1;*/


    /*    public ProfileViewFragment(String imageUrl, String uID ){
        this.imageUrl = imageUrl;
        this.uID = uID;
    }*/
    public UserListFragment(/*User user,*/ Context ctx, ArrayList<User> listOfUsers) {
        //this.imageUrl = imageUrl;
        //this.user = user;
/*        this.imageUrl = user.getUserProfileUrl();
        this.uID = user.getUserId();*/
        this.ctx = ctx;
        this.listOfUsers = listOfUsers;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();


        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.list_users);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        recyclerView = (RecyclerView) dialog.findViewById(R.id.rvDialogUserView);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ctx, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Store the indexes of the clicks
                    }
                })
        );

        mAdapter = new User_Friends_Adapter(listOfUsers, ctx, auth.getCurrentUser().getUid());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return dialog;
    }
}

