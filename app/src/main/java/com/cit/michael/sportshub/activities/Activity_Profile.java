package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.Profile_Events_Attended_Adapter;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.chat.ui.Activity_Chat;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Friendship;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestProfile;
import com.cit.michael.sportshub.rest.model.RestRelationship;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cit.michael.sportshub.Constants.ACCEPTED;
import static com.cit.michael.sportshub.Constants.PENDING_REQUEST;
import static com.cit.michael.sportshub.Constants.UNFRIEND;

public class Activity_Profile extends AppCompatActivity {
//public class Activity_Profile extends Fragment {
    private RecyclerView recyclerView;
    private Profile_Events_Attended_Adapter mAdapter;
    @BindView(R.id.profilePictureView1) ImageView profilePictureView;
    @BindView(R.id.txtOrganizedProfile) TextView txtOrganizedProfile;
    @BindView(R.id.txtPlayedProfile) TextView txtPlayedProfile;
    @BindView(R.id.txtReliability) TextView txtReliability;
    @BindView(R.id.txtUsernameProfile) TextView txtUsernameProfile;
    @BindView(R.id.lblPerviousEvents) TextView lblPerviousEvents;



    Context ctx;
    NetworkService service;
    List<Event> listEvents;
    List<Location> listLocaton;
    List<User>  user;
    ActionBar actionBar;
    private FirebaseAuth auth;
    private Menu menu;
    FirebaseInstanceId mFirebaseInstanceId;
    List <Friendship>listOfFriendships;
    int friendship;
    LinearLayout lSwitchCompat;
    Boolean currentlyFriends = false;
    public int friendshipFoundIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__profile);
        service = RestClient.getSportsHubApiClient();
        mFirebaseInstanceId = FirebaseInstanceId.getInstance();
        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        lSwitchCompat = (LinearLayout) findViewById(R.id.lSwitchCompat);
        lSwitchCompat.setVisibility(View.GONE);

/*
        View namebar = findViewById(R.id.lSwitchCompat);
        ((ViewGroup) namebar.getParent()).removeView(namebar);*/


        listEvents = new ArrayList<Event>();
        listLocaton = new ArrayList<Location>();
        user = new ArrayList<User>();
        recyclerView = (RecyclerView) findViewById(R.id.profile_recycler_view);
        Log.d("MyFirebaseMsgService ", "Profile: " +  user_id);
        ButterKnife.bind(this);

        listOfFriendships = new ArrayList<Friendship>();
        friendship= 0;




        service.getUserDetails(user_id).enqueue(new Callback<RestProfile>() {
            @Override
            public void onResponse(Call<RestProfile> call, Response<RestProfile> response) {
                Log.w("TEST123 ACTIVITY", "JSON: " + new Gson().toJson(response));
                
                listEvents = response.body().getEvent();
                listLocaton = response.body().getLocation();
                user = response.body().getUser();
                Toast.makeText(getApplicationContext(), "Activity Profile...", Toast.LENGTH_SHORT).show();
                if(listEvents.isEmpty()){
                    //Toast.makeText(ctx, "No Previous Events...", Toast.LENGTH_SHORT).show();
                    lblPerviousEvents.setText("NO PREVIOUS EVENTS");
                }
                else{
                    displayEvents();


                }
                displayUserInfo();

            }

            @Override
            public void onFailure(Call<RestProfile> call, Throwable t) {

            }

        });


        service.getFriendshipStatus(auth.getCurrentUser().getUid()).enqueue(new Callback<RestRelationship>() {
            @Override
            public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                listOfFriendships = response.body().getFriendship();
                //Log.d(TAG, "listOfFriendships size " + listOfFriendships.size());

                if(!listOfFriendships.isEmpty()) {
                    Log.d("ABC123", "!listOfFriendships.isEmpty()");
                    for (int i = 0; i < listOfFriendships.size(); i++) {
                         Log.d("ABC123", "FOR LOOP: 1: " + listOfFriendships.get(i).getUserId() + " - " +auth.getCurrentUser().getUid()  + " 2: " +  listOfFriendships.get(i).getUser_two_id() + " - " + auth.getCurrentUser().getUid());
                        if (listOfFriendships.get(i).getUserId().equals(auth.getCurrentUser().getUid()) || listOfFriendships.get(i).getUser_two_id().equals(auth.getCurrentUser().getUid())) {
                            Log.d("ABC123", "Friendship found at index " + i);
                            friendshipFoundIndex = i;
                            if(listOfFriendships.get(i).getStatus() == ACCEPTED){
                                Log.d("ABC123", "Friendship ACCEPTED " + i);
                                updateMenuTitles(ACCEPTED);
                                 friendship = ACCEPTED;
                                currentlyFriends = true;
                                 invalidateOptionsMenu();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RestRelationship> call, Throwable t) {

            }
        });


        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(ctx, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
/*                        // TODO Handle item click
                        // changeActivity(position);
                        Intent intent = new Intent(Activity_Search_Results.this,Activity_Event_Details.class);
                        //Intent intent = new Intent(Activity_Search_Results.this,MapsActivity.class);
                        intent.putExtra("eventSelected", arrayEvents.get(position).getEventId());
                        startActivity(intent);*/
                    }


                })
        );
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_viewing_user_profile, menu);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.home){
            Toast.makeText(getApplicationContext(), "Finish Activity", Toast.LENGTH_SHORT).show();
            NavUtils.navigateUpFromSameTask(this);
        }

        if(!user.get(0).getUserId().equals(auth.getCurrentUser().getUid())) {
            if (id == R.id.addFriend) {
                //Toast.makeText(getApplicationContext(), "addFriend", Toast.LENGTH_SHORT).show();
                //validator.validate();
                doFriendships();
            }

            if (id == R.id.messageUser) {
                Toast.makeText(getApplicationContext(), "messageUser", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,Activity_Chat.class);
                //intent.putParcelableArrayListExtra("searchResults", (ArrayList<? extends Parcelable>) user);
                intent.putExtra("receivingUser",  user.get(0));
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void updateMenuTitles(int ACTION) {

        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.addFriend);

        if (friendship == ACCEPTED) {
            menuItem.setTitle("Unfriend");
        } else {
            menuItem.setTitle("Add Friend");
        }
        return super.onPrepareOptionsMenu(menu);
    }


    private void displayUserInfo() {
        String uri = user.get(0).getUserProfileUrl();
        Picasso.with(getApplicationContext())
                .load(uri)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(400, 400)
                .transform(new CircleTransform())
                .into(profilePictureView);

        int firstSpace = user.get(0).getUserFullName().indexOf(" "); // detect the first space character
        if(firstSpace >0) {
            String firstName = user.get(0).getUserFullName().toString().substring(0, firstSpace);  // get everything upto the first space character
            actionBar.setTitle(firstName + "'s Profile");
        }
        else{
            actionBar.setTitle(user.get(0).getUserFullName() + "'s Profile");
        }


        if(user.get(0).getUserId().equals(auth.getCurrentUser().getUid())){
            menu.removeItem(R.id.addFriend);
            menu.removeItem(R.id.messageUser);
            Toast.makeText(getApplicationContext(), "addFriend", Toast.LENGTH_SHORT).show();
        }
        else {

        }

        invalidateOptionsMenu();

        txtUsernameProfile.setText(user.get(0).getUserFullName());
        txtOrganizedProfile.setText(user.get(0).getOrganized().toString());
        txtPlayedProfile.setText(user.get(0).getAttendances().toString());

        double reliability = (double)100-(((double)user.get(0).getFailedAttendances()/((double)user.get(0).getFailedAttendances()+(double)user.get(0).getAttendances())*100));
        txtReliability.setText(Integer.toString((int) reliability) + "%");
    }

    private void displayEvents() {

        mAdapter = new Profile_Events_Attended_Adapter(listEvents, listLocaton);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }
    private void doFriendships() {
        if (!currentlyFriends) {
            Friendship friendship = new Friendship(auth.getCurrentUser().getUid(), user.get(0).getUserId(), PENDING_REQUEST, auth.getCurrentUser().getUid());
            //Log.d(TAG, "User Selected:  " + user.getUserFullName() + " ID: " + user.getUserId());
            if (friendshipFoundIndex < 0) {
                service.sendFriendRequest(friendship).enqueue(new Callback<RestRelationship>() {
                    @Override
                    public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                        Toast.makeText(ctx, "Friend request sent...", Toast.LENGTH_SHORT).show();
                       // Log.d(TAG, "Friendship created.friendshipFoundIndex: " + friendshipFoundIndex);
                    }

                    @Override
                    public void onFailure(Call<RestRelationship> call, Throwable t) {

                    }
                });
            } else {//There is a friendship found between these two users.
                //Log.d(TAG, "There is a friendship found between these two users");
                if (auth.getCurrentUser().getUid().equals(listOfFriendships.get(friendshipFoundIndex).getAction_user_id())) {
                    //Dont do anything as this user has already sent the request.
                    Toast.makeText(ctx, "Friend request already sent...", Toast.LENGTH_SHORT).show();
                } else {
                   // Log.d(TAG, "Updating Friendship");
                    listOfFriendships.get(friendshipFoundIndex).setAction_user_id(auth.getCurrentUser().getUid());
                    listOfFriendships.get(friendshipFoundIndex).setStatus(ACCEPTED);
                    service.friendRequestResponse(listOfFriendships.get(friendshipFoundIndex)).enqueue(new Callback<RestRelationship>() {
                        @Override
                        public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                            Toast.makeText(ctx, "Now friends with " + user.get(0).getUserFullName(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ctx, "Unfriended " + user.get(0).getUserFullName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<RestRelationship> call, Throwable t) {

                }
            });
        }
    }
}
