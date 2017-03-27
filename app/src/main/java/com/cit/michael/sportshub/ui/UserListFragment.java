package com.cit.michael.sportshub.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.adapter.User_Friends_Adapter;
import com.cit.michael.sportshub.model.Group;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cit.michael.sportshub.Constants.ACTION_GROUP_ADD_MEMBER;
import static com.cit.michael.sportshub.Constants.ACTION_GROUP_NOT_SELECTED;
import static com.cit.michael.sportshub.Constants.ACTION_GROUP_REMOVE_MEMBER;
import static com.cit.michael.sportshub.Constants.ACTION_GROUP_SELECTED;


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
    private String action;
    ArrayList<User> selectedUsersInvFriends;
    @BindView(R.id.txtListUserDone)
    TextView txtDone;
    ArrayList<String> group_data;//this will either be user tokens or user_id of the members to remove.
    String groupID, groupName;
    Dialog dialog;


    public UserListFragment(Context ctx, ArrayList<User> listOfUsers, String action, String groupID, String groupName ) {
        this.ctx = ctx;
        this.action = action;
        this.listOfUsers = listOfUsers;
        this.groupID = groupID;
        this.groupName = groupName;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        selectedUsersInvFriends = new ArrayList<User>();
        group_data = new ArrayList<String>();

        //Set all user statuses to 0 (not selected),  1 = selected
        for(int i = 0; i < listOfUsers.size(); i++){
                listOfUsers.get(i).setStatus(ACTION_GROUP_NOT_SELECTED);
        }


        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.list_users);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        recyclerView = (RecyclerView) dialog.findViewById(R.id.rvDialogUserView);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ctx, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Store the indexes of the clicks
                        boolean found = false;
                        Log.d("UserListFragment", "1 : "  + selectedUsersInvFriends.size());
                        //if the the user has been selected already, unselect them.
                        for(int i = 0 ; i < selectedUsersInvFriends.size(); i++){
                            if(selectedUsersInvFriends.get(i).getUserId().equals(listOfUsers.get(position).getUserId())){
                                Log.d("UserListFragment", "2 found : "  + selectedUsersInvFriends.size());
                                selectedUsersInvFriends.remove(i);
                                found = true;
                                Log.d("UserListFragment", "3 found : "  + selectedUsersInvFriends.size());
                            }
                        }
                        if(!found){
                            listOfUsers.get(position).setStatus(ACTION_GROUP_SELECTED);
                            selectedUsersInvFriends.add(listOfUsers.get(position));
                        }
                        else{
                            listOfUsers.get(position).setStatus(ACTION_GROUP_NOT_SELECTED);
                        }
                        mAdapter.notifyItemChanged(position);
                    }
                })
        );

        mAdapter = new User_Friends_Adapter(listOfUsers, ctx, auth.getCurrentUser().getUid(), action);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ButterKnife.bind(this, dialog);

        return dialog;
    }

    @OnClick(R.id.txtListUserDone)
    public void doneText(){
        Log.d("UserListFragment", "@OnClick(R.id.txtListUserDone)");
        if(action.equals(ACTION_GROUP_ADD_MEMBER)){
            if(!selectedUsersInvFriends.isEmpty()){

                for(int i = 0; i< selectedUsersInvFriends.size(); i++){
                    group_data.add(selectedUsersInvFriends.get(i).getUserToken().toString());
                }
            }
            Group requestToGroup = new Group(Integer.parseInt(groupID),groupName, group_data, auth.getCurrentUser().getDisplayName());
            service.sendUsersGroupInvite(requestToGroup).enqueue(new Callback<RestGroup>() {
                @Override
                public void onResponse(Call<RestGroup> call, Response<RestGroup> response) {
                    Toast.makeText(ctx, response.message().toString(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<RestGroup> call, Throwable t) {
                    Toast.makeText(ctx, "Error...", Toast.LENGTH_SHORT).show();

                }

            });
            dialog.dismiss();
        }

        if(action.equals(ACTION_GROUP_REMOVE_MEMBER)){
            if(!selectedUsersInvFriends.isEmpty()){
                //reusing the group_data String arraylist. Prob should change the name
                group_data.clear();
                for(int i = 0; i< selectedUsersInvFriends.size(); i++){
                    group_data.add(selectedUsersInvFriends.get(i).getUserId().toString());
                }
                Group removeUsers = new Group(Integer.parseInt(groupID),groupName, group_data, auth.getCurrentUser().getDisplayName());
                service.removeUser(removeUsers).enqueue(new Callback<RestGroup>() {
                    @Override
                    public void onResponse(Call<RestGroup> call, Response<RestGroup> response) {
                        Toast.makeText(ctx, response.message().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<RestGroup> call, Throwable t) {
                        Toast.makeText(ctx, "Error...", Toast.LENGTH_SHORT).show();

                    }

                });
                dialog.dismiss();
            }
        }
    }
    @OnClick(R.id.txtListUserCancel)
    public void cancelRequest(){
        Log.d("UserListFragment", "@OnClick(R.id.txtListUserCancel)");
        dialog.dismiss();
    }


    public void onDismiss(DialogInterface dialogInterface)
    {
        //Toast.makeText(ctx, "ON_Dismiss", Toast.LENGTH_SHORT).show();

    }

    public interface DialogListener {
        void DialogListener(ArrayList<User> selectedUsers);
    }
}

