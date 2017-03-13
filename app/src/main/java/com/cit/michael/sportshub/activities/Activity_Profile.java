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
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.Profile_Events_Attended_Adapter;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestProfile;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__profile);
        service = RestClient.getSportsHubApiClient();
        mFirebaseInstanceId = FirebaseInstanceId.getInstance();
        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");

        listEvents = new ArrayList<Event>();
        listLocaton = new ArrayList<Location>();
        user = new ArrayList<User>();
        recyclerView = (RecyclerView) findViewById(R.id.profile_recycler_view);
        Log.d("TEST123", "Activity PROFILE");
        ButterKnife.bind(this);



        service.getUserDetails(user_id).enqueue(new Callback<RestProfile>() {
            @Override
            public void onResponse(Call<RestProfile> call, Response<RestProfile> response) {
                //Log.w("TEST123", "JSON: " + new Gson().toJson(response));
                
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
                Toast.makeText(getApplicationContext(), "addFriend", Toast.LENGTH_SHORT).show();
                //validator.validate();
            }

            if (id == R.id.messageUser) {
                Toast.makeText(getApplicationContext(), "messageUser", Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Event Created", Toast.LENGTH_SHORT).show();
                //validator.validate();
            }
        }
        return super.onOptionsItemSelected(item);
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
}
