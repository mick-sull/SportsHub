package com.cit.michael.sportshub.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.ExpandableHeightListView;
import com.cit.michael.sportshub.adapter.ProfileListView;
import com.cit.michael.sportshub.model.Attendee;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestAttendee;
import com.cit.michael.sportshub.rest.model.RestEventDetails;
import com.cit.michael.sportshub.ui.ProfileViewFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Event_Details extends AppCompatActivity  {

    private FirebaseAuth auth;
    public int selectedEventID;
    NetworkService service;
    public Location location;
    public Event event;
    public List<User> listUserAttending;
    public List<User> organizer;
    final String TAG = "EVENTDETAILS";
    private ExpandableHeightListView listview;
    private ProfileListView profileListView;
    private Boolean userAttending;
    ProgressDialog loading = null;




    @BindView(R.id.txtDisplayEventName) TextView txtEventName;
    @BindView(R.id.txtDisplayCost) TextView txtDisplayCost;
    @BindView(R.id.txtDisplayFreeSpace) TextView txtDisplayFreeSpace;
    @BindView(R.id.txtDisplayDate) TextView txtDisplayDate;
    @BindView(R.id.txtDisplayOrganizer) TextView txtDisplayOrganizer;
    @BindView(R.id.txtDisplayLocation) TextView txtDisplayLocation;
    @BindView(R.id.txtDisplayDuration) TextView txtDisplayDuration;
    @BindView(R.id.txtDisplayGender) TextView txtDisplayGender;
    @BindView(R.id.btnJoinLeave) TextView btnJoinLeave;
    //@BindView(R.id.progressBar1) ProgressBar spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__event__details);
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        selectedEventID = getIntent().getIntExtra("eventSelected", 0);
        Log.d("CHATISSUE3", "NOTIFCATION_ACTIVITY1"  + selectedEventID);
        Log.d("CHATISSUE4", "Event ID: " + getIntent().getIntExtra("eventSelected", 0));
        listUserAttending = new ArrayList<User>();
        organizer = new ArrayList<User>();
        load_event_data(selectedEventID);
        userAttending = false;
        ButterKnife.bind(this);

    }


    @OnClick(R.id.btnJoinLeave)
    public void submit(){

        if (event.getSpaceLeft() == 0 && !userAttending) {
            //changeButtonColor();
            Toast.makeText(this, "Event Full....", Toast.LENGTH_SHORT).show();
        }
        else{
            Attendee attendee = new Attendee(selectedEventID, auth.getCurrentUser().getUid(), null);
            if (userAttending) {
                //Attendee removeAttendee = new Attendee(selectedEventID, auth.getCurrentUser().getUid(), null);

                service.deleteAttendee(attendee).enqueue(new Callback<RestAttendee>() {
                    @Override
                    public void onResponse(Call<RestAttendee> call, Response<RestAttendee> response) {
                        //Toast.makeText(Activity_Event_Details.this, ""+ response.message(), Toast.LENGTH_SHORT).show();
                        userAttending = false;
                        changeButtonColor();
                        load_event_data(selectedEventID);
                    }

                    @Override
                    public void onFailure(Call<RestAttendee> call, Throwable t) {

                    }
                });
            } else {
                service.addAttendee(attendee).enqueue(new Callback<RestAttendee>() {
                    @Override
                    public void onResponse(Call<RestAttendee> call, Response<RestAttendee> response) {
                        //Toast.makeText(Activity_Event_Details.this, ""+ response.message(), Toast.LENGTH_SHORT).show();
                        userAttending = false;
                        changeButtonColor();
                        load_event_data(selectedEventID);

                    }

                    @Override
                    public void onFailure(Call<RestAttendee> call, Throwable t) {

                    }
                });
            }
        }
    }


    private void load_event_data(int selectedEventID) {

        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("Loading...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
        listUserAttending.clear();
        //listUserAttending= null;

        service.getEventDetails(selectedEventID).enqueue(new Callback<RestEventDetails>() {
        //service.getEventDetails().enqueue(new Callback<RestEventDetails>() {
            @Override
            public void onResponse(Call<RestEventDetails> call, Response<RestEventDetails> response) {

             //   if (response.isSuccessful()) {
                    Log.w(TAG, "JSON: " + new Gson().toJson(response));

                    if(response.body().getUser().isEmpty()){
                        Log.d(TAG, "getAttendee empty");
                    }
                    else {
                        listUserAttending = response.body().getUser();
                        Log.d(TAG, "listUserAttending not  empty size: " + response.body().getUser().size());
                    }
                    if(response.body().getOrganizer().isEmpty()){
                        Log.d(TAG, "getOrganizer empty");
                    }
                    else {
                        organizer = response.body().getOrganizer();
                        Log.d(TAG, "getOrganizer not  empty size: " + response.body().getOrganizer().size());
                    }

                    if(response.body().getLocation().isEmpty()){
                        Log.d(TAG, "getLocation empty");
                    }
                    else {
                        location = response.body().getLocation().get(0);
                        Log.d(TAG, "getLocation not  empty size: " + response.body().getLocation().size());
                    }

                    if(response.body().getEvent().isEmpty()){
                        Log.d(TAG, "getEvent empty");
                    }
                    else {
                        event = response.body().getEvent().get(0);
                        Log.d(TAG, "getEvent not  empty size: " + response.body().getEvent().size());
                    }
                if(!listUserAttending.isEmpty()) {

                    for (int i = 0; i < listUserAttending.size(); i++) {
                        if (auth.getCurrentUser().getUid().equals(listUserAttending.get(i).getUserId())) {
                            userAttending = true;
                            changeButtonColor();
                        }

                    }
                }
                Log.d("CHATISSUE2", "Organizer Size: " + organizer.size());
                Log.d("CHATISSUE2", "Attendee: " + listUserAttending.size());
                        displayInfo();

            }

            @Override
            public void onFailure(Call<RestEventDetails> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
            }
        });

    }

    //Display event information
    private void displayInfo() {

        txtEventName.setText(event.getEventName());
        txtDisplayCost.setText(event.getFormattedCost());
        txtDisplayFreeSpace.setText(event.getSpaceLeft() + " spaces");
        try {
            txtDisplayDate.setText(event.getFormattedDate() + " | " + event.getEventTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        txtDisplayOrganizer.setText(organizer.get(0).getUserFullName());
        txtDisplayDuration.setText(event.getDuration() + " mins");
        txtDisplayGender.setText(event.getGender());
        txtDisplayLocation.setText(location.getLocationName() + ", " + location.getAddress1() + ", "  + location.getAddress2());

            listview = (ExpandableHeightListView) findViewById(R.id.listview);
            profileListView = new ProfileListView(Activity_Event_Details.this, listUserAttending) {
            };

            listview.setAdapter(profileListView);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // WORKING
               FragmentManager fm = getSupportFragmentManager();
                //ProfileViewFragment editNameDialogFragment = new ProfileViewFragment(listUserAttending.get(position).getUserProfileUrl(), listUserAttending.get(position).getUserId());
                User user = listUserAttending.get(position);
                ProfileViewFragment editNameDialogFragment = new ProfileViewFragment(user, getApplicationContext());
                editNameDialogFragment.show(fm, "test");

/*                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View dialogLayout = inflater.inflate(R.layout.profile_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setView(dialogLayout);
*//*                TextView indtruction = (TextView)dialogLayout.findViewById(R.id.text_instruction);
                final EditText userInput = (EditText)dialogLayout.findViewById(R.id.user_input);*//*
                AlertDialog customAlertDialog = builder.create();
                customAlertDialog.show();*/


/*                Object o = prestListView.getItemAtPosition(position);
                prestationEco str=(prestationEco)o;//As you are using Default String Adapter
                Toast.makeText(getBaseContext(),str.getTitle(),Toast.LENGTH_SHORT).show();*/
            }
        });


        loading.dismiss();
        Log.d(TAG, "User Attending Value: " + userAttending.toString());

        changeButtonColor();


    }

    public void changeButtonColor(){
        if(event.getSpaceLeft() == 0 && userAttending){
            btnJoinLeave.setBackgroundColor(getResources().getColor(R.color.ProfileSecondary));
            btnJoinLeave.setText("LEAVE");

        }
        else if (event.getSpaceLeft() == 0 && !userAttending){
            btnJoinLeave.setBackgroundColor(getResources().getColor(R.color.primary_light));
            btnJoinLeave.setText("EVENT FULL");

        }
        else if(userAttending){
            btnJoinLeave.setBackgroundColor(getResources().getColor(R.color.ProfileSecondary));
            btnJoinLeave.setText("LEAVE");
            //userAttending = true;
        }
        else{
            btnJoinLeave.setBackgroundColor(getResources().getColor(R.color.colorblue));
            btnJoinLeave.setText("JOIN");
            //userAttending = false;
        }
    }

}
