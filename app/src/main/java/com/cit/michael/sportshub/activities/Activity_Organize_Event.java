package com.cit.michael.sportshub.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cit.michael.sportshub.NetworkConnectionUtil;
import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Group;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.Sport;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestEvent;
import com.cit.michael.sportshub.rest.model.RestGroup;
import com.cit.michael.sportshub.rest.model.RestLocation;
import com.cit.michael.sportshub.rest.model.RestSport;
import com.cit.michael.sportshub.rest.model.RestUsers;
import com.cit.michael.sportshub.ui.AddLocationFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cit.michael.sportshub.Constants.ACTION_EDIT_EVENT;

public class Activity_Organize_Event extends AppCompatActivity  implements Validator.ValidationListener, AddLocationFragment.AddLocationtDialogListener {

    @Nullable
    private static TextView set_date, set_time;// selectSport, addNewSport;
    NetworkService service;

    @NotEmpty
    @BindView(R.id.txtSelectSport) TextView selectSport ;
    @BindView(R.id.btnAddSport) ImageView addNewSport;
    @NotEmpty
    @BindView(R.id.txtDisplayLocation) TextView txtDisplayLocation;
    @BindView(R.id.txtSearchLocation) ImageView selectLocation;
    @BindView(R.id.imgDate) ImageView imgDate;
    @BindView(R.id.imgTime) ImageView imgTime;
    @NotEmpty
    @BindView(R.id.txtEventName) TextView txtEventName;

    @NotEmpty
    @Max(1000)
    @Min(1)
    @BindView(R.id.txtEventDuration) TextView txtEventDuration;
    @NotEmpty
    @Max(1000)
    @Min(1)
    @BindView(R.id.txtNumSpaces) TextView txtNumSpaces;
    @NotEmpty
    @BindView(R.id.btnCreate) TextView btnCreate;
    @NotEmpty
    @Max(1000)
    @Min(0)
    @BindView(R.id.txtEventCost) TextView txtEventCost;
    @BindView(R.id.rgEventGender) RadioGroup rgEventGender;
    @BindView(R.id.cbPublicGame) CheckBox cbPublicGame;
    @BindView(R.id.txtInviteFriends) TextView txtInviteFriends;
    @BindView(R.id.ivInviteFriends) ImageView ivInviteFriends;

    @BindView(R.id.txtInviteGroup) TextView txtInviteGroup;
    @BindView(R.id.ivInviteGroups) ImageView ivInviteGroups;


    //Dates selected
    public static int sYear;
    public static int sMonth;
    public static int sDay;
    public static String dbDate;
    private Subscription subscription_sports;
    public List<Sport> listOfSports;
    public List<Location> listOfLocations;
    public List<User> listOfFriends;
    ArrayList<String> userTokens;
    private List<CharSequence> listOfNames;
    private FirebaseAuth auth;
    public Validator validator;
    public int selectedLocationId = 0;
    public int selectedSportId = 0;
    NetworkConnectionUtil networkConnectionUtil;
    public int finishDownloading;
    ProgressDialog progress;
    private ArrayList<User> selectedUsers;
    private ArrayList<Group> selectedGroup;
    String ACTION;
    Event event;
    private List<Group> listOfGroups;

/*    @BindView(R.id.txtEventDate) TextView set_date;
    @BindView(R.id.txtEventTime) TextView set_time;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        listOfLocations = new ArrayList<Location>();
        listOfSports = new ArrayList<Sport>();
        listOfGroups = new ArrayList<Group>();
        listOfFriends = new ArrayList<User>();
        listOfNames = new ArrayList<CharSequence>();
        selectedUsers = new ArrayList<User>();
        selectedGroup = new ArrayList<Group>();
        setContentView(R.layout.activity_organize_event);
        set_date = (TextView) findViewById(R.id.txtEventDate);
        set_time = (TextView) findViewById(R.id.txtEventTime);
        validator = new Validator(this);
        validator.setValidationListener(this);
        networkConnectionUtil = new NetworkConnectionUtil();
        ButterKnife.bind(this);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
// To dismiss the dialog
        progress.dismiss();
        finishDownloading = 2;
        getData();
        getGroups();
        ACTION = "";
        if( getIntent().getExtras() != null)
        {
            event = getIntent().getParcelableExtra("event");
            ACTION = getIntent().getStringExtra("action");
            handleIntent();
        }


    }

    private void handleIntent() {
        txtEventName.setText(event.getEventName());

        txtEventDuration.setText(event.getDuration().toString());
        if(event.getGender().equals("Male")){
            rgEventGender.check(R.id.rbMale);
        }
        else if(event.getGender().equals("Female")){
            rgEventGender.check(R.id.rbFemale);
        }
        else if(event.getGender().equals("Mixed")){
            rgEventGender.check(R.id.rbMixed);
        }

        set_date.setText(event.getEventDate());
        set_time.setText(event.getEventTime());


        txtNumSpaces.setText(event.getNoSpace().toString());
        txtEventCost.setText(event.getCost().toString());
        if(event.getPublicGame() == 1){
            cbPublicGame.setChecked(true);
        }
        else{
            cbPublicGame.setChecked(false);
        }

        if(ACTION.equals(ACTION_EDIT_EVENT)){
            btnCreate.setText("UPDATE");
            txtEventCost.setFocusable(false);
            txtInviteFriends.setFocusable(false);
            txtNumSpaces.setFocusable(false);
            txtEventDuration.setFocusable(false);
            txtDisplayLocation.setClickable(false);
            txtEventName.setFocusable(false);
            selectLocation.setClickable(false);
            set_date.setClickable(false);
            set_time.setClickable(false);
            addNewSport.setClickable(false);
            selectSport.setClickable(false);
            selectSport.setClickable(false);
            imgTime.setClickable(false);
            imgDate.setClickable(false);
            for (int i = 0; i < rgEventGender.getChildCount(); i++) {
                rgEventGender.getChildAt(i).setEnabled(false);
            }

        }


    }


    @OnClick(R.id.txtSelectSport)
    public void selectSport(View view) {
        // TODO submit data to server...
        displayDialogBox();
    }
    @OnClick({R.id.imgTime, R.id.txtEventTime})
    public void selectTime(View view) {
        // TODO submit data to server...
        btnTime(view);
    }

    @OnClick({R.id.imgDate, R.id.txtEventDate})
    public void selectDate(View view) {
        // TODO submit data to server...
        btnDate(view);
    }

    @OnClick(R.id.btnAddSport)
    public void addSport(View view) {
        // TODO submit data to server...
        addNewSport();
    }

    @OnClick({R.id.txtSearchLocation, R.id.txtDisplayLocation})
    public void selectLocation(View view) {
        // TODO submit data to server...
        dialogChooseLocation();
    }
    @OnClick(R.id.btnCreate)
    public void createEvent(View view) {
        // TODO submit data to server...
        validator.validate();
    }

    @OnClick({R.id.ivInviteFriends, R.id.txtInviteFriends})
    public void inviteFriends(View view) {
        // TODO submit data to server...
        displayFriendsList(listOfFriends);
    }

    @OnClick({R.id.ivInviteGroups, R.id.txtInviteGroup})
    public void inviteGroup(View view) {
        // TODO submit data to server...
        displayGroupList(listOfGroups);
    }

    private void displayGroupList(final List<Group> listOfGroups) {
        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
        //final DBHelper dbHelper = new DBHelper(this);
        String[] userFullNames = new String[listOfGroups.size()];
        final boolean[] selectedItems = new boolean[listOfGroups.size()];
        for(int i = 0 ; i < userFullNames.length ; i++){
            userFullNames[i] = listOfGroups.get(i).getGroupName();
            selectedItems[i] = false;
            for(int j = 0 ; j < selectedGroup.size() ; j++){
                if(selectedGroup.get(j).getGroupId() == listOfGroups.get(i).getGroupId()){
                    selectedItems[i] = true;
                    break;
                }
            }
        }
        alerBuilder.setMultiChoiceItems(userFullNames,selectedItems,new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                Log.e("CheckStatus",String.valueOf(b));
                //selectedUsers.add(listOfFriends.get(i).getUserToken().toString());
                selectedGroup.add(listOfGroups.get(i));
            }
        }).setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ii) {
                selectedGroup.clear();
                for(int i = 0 ; i < selectedItems.length ; i++) {
                    if(selectedItems[i]) {
                        selectedGroup.add(listOfGroups.get(i));
                    }
                }
                if(selectedGroup.size() >1){
                    txtInviteGroup.setText(selectedGroup.get(0).getGroupName() + " and " + (selectedGroup.size() -1) + " others");
                }
                else if(selectedGroup.size()==1){
                    txtInviteGroup.setText(selectedGroup.get(0).getGroupName());
                }
                else{
                    //do nothing
                    //txtInviteFriends.setText(selectedUsers.get(0).getUserFullName());
                }
            }
        }).setCancelable(false).setTitle("Select Groups").create().show();
    }

    private void displayFriendsList(final List<User> listOfNames) {

        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
        //final DBHelper dbHelper = new DBHelper(this);
        String[] userFullNames = new String[listOfNames.size()];
        final boolean[] selectedItems = new boolean[listOfNames.size()];
        for(int i = 0 ; i < userFullNames.length ; i++){
            userFullNames[i] = listOfNames.get(i).getUserFullName();
            selectedItems[i] = false;
            for(int j = 0 ; j < selectedUsers.size() ; j++){
                if(selectedUsers.get(j).getUserId() == listOfNames.get(i).getUserId()){
                    selectedItems[i] = true;
                    break;
                }
            }
        }
        alerBuilder.setMultiChoiceItems(userFullNames,selectedItems,new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                Log.e("CheckStatus",String.valueOf(b));
                //selectedUsers.add(listOfFriends.get(i).getUserToken().toString());
                selectedUsers.add(listOfFriends.get(i));
            }
        }).setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ii) {
                selectedUsers.clear();
                for(int i = 0 ; i < selectedItems.length ; i++) {
                    if(selectedItems[i]) {
                        selectedUsers.add(listOfFriends.get(i));
                    }
                }
                if(selectedUsers.size() >1){
                    txtInviteFriends.setText(selectedUsers.get(0).getUserFullName() + " and " + (selectedUsers.size() -1) + " others");
                }
                else if(selectedUsers.size()==1){
                    txtInviteFriends.setText(selectedUsers.get(0).getUserFullName());
                }
                else{
                    //do nothing
                    //txtInviteFriends.setText(selectedUsers.get(0).getUserFullName());
                }
            }
        }).setCancelable(false).setTitle("Select friends").create().show();
        
    }




    private void dialogChooseLocation() {

        InputMethodManager imm;// = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
//        Log.d("ERR", "List of location size: " + listOfLocations.size());
        if(listOfLocations.isEmpty()){
            //Toast.makeText(Activity_Organize_Event.this, "Error: Couldnt not retrieve data...", Toast.LENGTH_SHORT).show();
            getData();
        }
        else {
            adapter.add("Add New Location");
            for (int i = 0; i < listOfLocations.size(); i++) {
                adapter.add(listOfLocations.get(i).getLocationName());
            }




            AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));
            builder.setTitle("Locations");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if(item ==0){
                        FragmentManager fm = getFragmentManager();
                        AddLocationFragment editNameDialogFragment = new AddLocationFragment(getApplicationContext());
                        editNameDialogFragment.show(fm, "abc");
                    }
                    else{
                        txtDisplayLocation.setText(listOfLocations.get(item-1).getLocationName());
                        selectedLocationId = listOfLocations.get(item-1).getLocationId();
                    }

                }
            });
            builder.show();
        }
    }


    private void addNewSport() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.fragment_add_sport, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.txtAddSport);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Add new sport: ")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String taskEntered = String.valueOf((userInput.getText()));
                                Sport newSport = new Sport(null,String.valueOf(userInput.getText()),auth.getCurrentUser().getUid() );
                                //service = RestClient.getSportsHubApiClient();
                                service.createSport(newSport).enqueue(new Callback<RestSport>() {
                                    @Override
                                    public void onResponse(Call<RestSport> call, Response<RestSport> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<RestSport> call, Throwable t) {

                                    }
                                });

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // show it
        alertDialog.show();
    }




    private void displayDialogBox() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        if(listOfSports.isEmpty()){
            //Toast.makeText(Activity_Organize_Event.this, "Error: Couldnt not retrieve data...", Toast.LENGTH_SHORT).show();
            getData();
        }
        else{
            for(int i = 0; i< listOfSports.size(); i++){
                adapter.add(listOfSports.get(i).getSportName());
            }
            AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));
            builder.setTitle("Sports");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    selectSport.setText(listOfSports.get(item).getSportName());
                    selectedSportId = listOfSports.get(item).getSportId();
                }
            });
            builder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Event");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void btnDate(View v) {

        DialogFragment newFragment = new DatePickerFragment();
        // newFragment.show(getSupportFragmentManager(), "datePicker");
        newFragment.show(getFragmentManager(), "Date Picker");
    }


    public void btnTime(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "TimePicker");

    }



    private void getData() {
        service.getSport()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestSport>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestSport restSport) {

                        listOfSports = restSport.getSport();
                        if( getIntent().getExtras() != null)
                        {
                            for(int i = 0; i < listOfSports.size(); i++){
                                if(listOfSports.get(i).getSportId().equals(event.getSportId())){
                                    selectSport.setText(listOfSports.get(i).getSportName());
                                    selectedSportId = listOfSports.get(i).getSportId();
                                }
                            }
                        }
                    }

                });

        service.getLocation()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestLocation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestLocation restLocation) {
                        if (restLocation.getLocation().isEmpty()) {
                            Toast.makeText(Activity_Organize_Event.this, "Error: Couldnt retrieve data...", Toast.LENGTH_SHORT).show();
                        } else {
                            listOfLocations = restLocation.getLocation();
                            for(int i = 0; i < listOfLocations.size();i++){
                                if(listOfLocations.get(i).getLocationId().equals(event.getLocationId())){
                                    txtDisplayLocation.setText(listOfLocations.get(i).getLocationName());
                                    selectedLocationId = listOfLocations.get(i).getLocationId();
                                }
                            }
                        }
                    }
                });

        service.getUserFriends(auth.getCurrentUser().getUid())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestUsers>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestUsers restUsers) {
                        listOfFriends = restUsers.getUser();
                    }
                });


    }

    public void getGroups(){
        service.getGroups(auth.getCurrentUser().getUid()).enqueue(new Callback<RestGroup>() {
            @Override
            public void onResponse(Call<RestGroup> call, Response<RestGroup> response) {
                listOfGroups = response.body().getGroup();
            }

            @Override
            public void onFailure(Call<RestGroup> call, Throwable t) {

            }
        });
    }
    @Override
    public void onValidationSucceeded() {

        int publicGame = 0;
        if(cbPublicGame.isChecked()){
            publicGame = 1;
        }

        if(ACTION.equals(ACTION_EDIT_EVENT)){
            event.setPublicGame(publicGame);
            service.updateEvent(event).enqueue(new Callback<RestEvent>() {
                @Override
                public void onResponse(Call<RestEvent> call, Response<RestEvent> response) {
                    Toast.makeText(Activity_Organize_Event.this, "" + response.body().getMessage() , Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<RestEvent> call, Throwable t) {

                }
            });
        }
        else{

        String eventGender = "";
        switch(rgEventGender.getCheckedRadioButtonId()){
            case R.id.rbMale:
                eventGender = "Male";
                break;
            case R.id.rbFemale:
                eventGender = "Female";
                break;
            case R.id.rbMixed:
                eventGender = "Mixed";
                break;
        }

        if(!selectedUsers.isEmpty()){
            userTokens = new ArrayList<String>();
            for(int i = 0; i< selectedUsers.size(); i++){
                userTokens.add(selectedUsers.get(i).getUserToken().toString());
            }
        }

        Event newEvent = new Event(null,selectedLocationId,auth.getCurrentUser().getUid(),dbDate,set_time.getText().toString(), selectedSportId,
                Integer.parseInt(txtNumSpaces.getText().toString()),
                Integer.parseInt(txtNumSpaces.getText().toString()), Integer.parseInt(txtEventDuration.getText().toString()), eventGender, Double.parseDouble(txtEventCost.getText().toString()) ,publicGame, txtEventName.getText().toString(), userTokens);

        service.createEvent(newEvent).enqueue(new Callback<RestEvent>(){
            @Override
            public void onResponse(Call<RestEvent> call, Response<RestEvent> response) {
                Toast.makeText(Activity_Organize_Event.this, "" + response.body().getMessage() , Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<RestEvent> call, Throwable t) {

            }
        });
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void dialogListener(Location location) {
        listOfLocations.add(location);
        LinearLayout myLayout = (LinearLayout)findViewById(R.id.layout_OrganizeSport);
        myLayout.requestFocus();
        dialogChooseLocation();
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String displayDate;
            if(monthOfYear<9){
                if(dayOfMonth<10){
                    dbDate = year + "-" + "0" +(monthOfYear + 1) + "-" +  "0" + dayOfMonth;
                    displayDate = "0" + dayOfMonth + "-" + "0" +(monthOfYear + 1) + "-" + year;
                }
                else {
                    dbDate =year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    displayDate = dayOfMonth + "-" + "0" + (monthOfYear + 1) + "-" + year;
                }
            }else{

                dbDate =year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                displayDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" +year;
            }
            set_date.setText(displayDate);
            sYear = year;
            sMonth = monthOfYear;
            sDay = dayOfMonth;
        }
    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Time t = new Time(hourOfDay, minute, 0);//seconds by default set to zero
            Format formatter;
            formatter = new SimpleDateFormat("HH:mm");
            String time = formatter.format(t);
            set_time.setText(time);
        }
    }
}
