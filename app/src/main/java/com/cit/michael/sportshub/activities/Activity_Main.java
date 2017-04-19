package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.Latest_Events_Adapter;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.location.AppLocationManager;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Subscription;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestArrayList;
import com.cit.michael.sportshub.rest.model.RestEvent;
import com.cit.michael.sportshub.rest.model.RestLocation;
import com.cit.michael.sportshub.rest.model.RestSubscription;
import com.cit.michael.sportshub.rest.model.RestUsers;
import com.cit.michael.sportshub.ui.EventOptionsFragment;
import com.facebook.FacebookSdk;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cit.michael.sportshub.Constants.ACTION_FRAG_MAIN;
import static com.cit.michael.sportshub.Constants.SP_DISTANCE;
import static com.cit.michael.sportshub.Constants.SP_SETTINGS;

/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;*/

public class Activity_Main extends AppCompatActivity implements Fragment_Profile.OnFragmentInteractionListener, Frag_Group.OnFragmentInteractionListener, Fragment_Friends_List.OnFragmentInteractionListener,
        Fragment_Chat_List.OnFragmentInteractionListener {

    /*    @BindView(com.cit.michael.sportshub.R.id.btnOrganizeEvent) Button btnOrganizeEvent;
        @BindView(R.id.btnNearby) Button btnNearby;
        @BindView(R.id.btnMyEvents) Button btnMyEvents;
        @BindView(R.id.btnRecentEvents) Button btnRecentEvents;
        @BindView(R.id.btnChat) Button btnChat;*/
    @BindView(R.id.floatAddEvent)
    FloatingActionButton floatAddEvent;
    @BindView(R.id.floatSearchEvent)
    FloatingActionButton floatSearchEvent;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private FirebaseAuth auth;
    String newUser;
    NetworkService service;
    FirebaseInstanceId mFirebaseInstanceId;
    User user;
    public static boolean chat_active = false;
    private GoogleMap mMap;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.fragment_main);
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        FacebookSdk.sdkInitialize(getApplicationContext());
/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        mFirebaseInstanceId = FirebaseInstanceId.getInstance();


        String newUser = getIntent().getStringExtra("user");

        if (newUser.equals("new")) {
            addUserToDatabase();
        }

        auth.getCurrentUser().getUid();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1, false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    private void addUserToDatabase() {
        User newUser = new User(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getPhotoUrl().toString(), null, null, mFirebaseInstanceId.getToken().toString());

        service.addUser(newUser).enqueue(new Callback<RestUsers>() {
            @Override
            public void onResponse(Call<RestUsers> call, Response<RestUsers> response) {

            }

            @Override
            public void onFailure(Call<RestUsers> call, Throwable t) {

            }
        });
        //auth.getCurrentUser().
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.logOut) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //user logged out
                            Log.d("AUTH", "User Logged Out");
                            finish();
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment /*implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener*/
            //GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener

    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private GoogleMap mMap;
        NetworkService service;
        private FirebaseAuth auth;
        List<Subscription> listOfSubs;
        public List<com.cit.michael.sportshub.model.Location> listOfLocations;
        List<Event> latestEvents;
        private RecyclerView recyclerView;
        private Latest_Events_Adapter mAdapter;
        private SwitchCompat switchCompat;
        SharedPreferences prefs;
        Boolean scChecked;
        List<String> options;
        LocationRequest mLocationRequest;
        GoogleApiClient mGoogleApiClient;
        Location mCurrentLocation;
        private static final long INTERVAL = 1000 * 10;
        private static final long FATEST_INTERVAL = 1000 * 5;
        private static final String TAG = "MainActivityFrag";
        AppLocationManager locationManager;
        AppLocationManager appLocationManager;

        private static final String ARG_SECTION_NUMBER = "section_number";


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


          /*  createLocationRequest();
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .enableAutoManage(getActivity(), 0 *//* clientId *//*, this)
                        .build();
            }


            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
*/

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            auth = FirebaseAuth.getInstance();
            service = RestClient.getSportsHubApiClient();
            locationManager = new AppLocationManager(getContext());
            listOfSubs = new ArrayList<Subscription>();
            latestEvents = new ArrayList<Event>();
            listOfLocations = new ArrayList<com.cit.michael.sportshub.model.Location>();
            switchCompat = (SwitchCompat) rootView.findViewById(R.id.scSortEvents);
            prefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
            scChecked = prefs.getBoolean("scMain", false);
            switchCompat.setChecked(scChecked);
            appLocationManager = new AppLocationManager(getContext());
            if(appLocationManager.canGetLocation() && appLocationManager == null){
                appLocationManager.startLocation();
            }
            else{
                //appLocationManager.showSettingsAlert();

            }
            mCurrentLocation = appLocationManager.getLocation();




            if(mCurrentLocation != null) {
                Log.d("GPS mCurrentMain lat2", " " + mCurrentLocation.getLatitude());
                Log.d("GPS mCurrenMain long2", " " + mCurrentLocation.getLongitude());
            }


            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("scMain", isChecked);
                    editor.commit();
                    scChecked = isChecked;
                    getLatestEvents();
                }
            });

            ButterKnife.bind(this, rootView);
            loadSubData();
            getLatestEvents();

            Log.d("AUTH123", "  loadSubData();");

            recyclerView = (RecyclerView) rootView.findViewById(R.id.rvLatestEvents);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //FragmentManager fm = getFragmentManager();

                            options = new ArrayList<>();
                            //options.add("Join Event");
                            options.add("View Event");
                            options.add("View Map");
                            FragmentTransaction fm = ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                            EventOptionsFragment editNameDialogFragment = new EventOptionsFragment(latestEvents.get(position), getContext(), options, ACTION_FRAG_MAIN);
                            editNameDialogFragment.show(fm, "aaa");

                        }


                    })
            );


            return rootView;
        }

        private void createLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }


        @OnClick(R.id.floatAddEvent)
        public void submit(View rootView) {
            // TODO submit data to server...
            Intent intent = new Intent(rootView.getContext(), Activity_Organize_Event.class);
            startActivity(intent);
        }

        //@OnClick(R.id.btnNearby)
        @OnClick(R.id.floatSearchEvent)
        public void search(View rootView) {
            // TODO submit data to server...
            Intent intent = new Intent(rootView.getContext(), Activity_Search_Events.class);
            startActivity(intent);

        }

        public void loadSubData() {

            service.getSubscribedSport(auth.getCurrentUser().getUid())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RestSubscription>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(RestSubscription restSub) {
                            listOfSubs = restSub.getSubscription();
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
                            listOfLocations = restLocation.getLocation();
                        }
                    });
        }

        private void getLatestEvents() {
            ArrayList<String> subIDs = new ArrayList<String>();
            for (int i = 0; i < listOfSubs.size(); i++) {
                subIDs.add(listOfSubs.get(i).getSportID().toString());
            }

            RestArrayList list = new RestArrayList(subIDs);

            if (!scChecked) {
                service.getLatestEventsByCreated(list).enqueue(new Callback<RestEvent>() {
                    @Override
                    public void onResponse(Call<RestEvent> call, Response<RestEvent> response) {
                        Log.w("TESTEVENT", "JSON: " + new Gson().toJson(response));
                        latestEvents = response.body().getEvent();
                        Log.d("AUTH123", "latestEvents size: " + latestEvents.size());
                        displayLatestEvents();
                    }

                    @Override
                    public void onFailure(Call<RestEvent> call, Throwable t) {

                    }
                });
            } else {
                service.getLatestEventsBySoon(list).enqueue(new Callback<RestEvent>() {
                    @Override
                    public void onResponse(Call<RestEvent> call, Response<RestEvent> response) {
                        latestEvents = response.body().getEvent();
                        Log.d("AUTH123", "latestEvents size: " + latestEvents.size());
                        displayLatestEvents();
                    }

                    @Override
                    public void onFailure(Call<RestEvent> call, Throwable t) {

                    }
                });
            }
        }

        private void displayLatestEvents() {
            Location locationA = new Location("current location");
            Location locationB = new Location("event location");
            int maxDistance = prefs.getInt(SP_DISTANCE, 50);
            prefs = getActivity().getSharedPreferences(SP_SETTINGS, Context.MODE_PRIVATE);
            if (mCurrentLocation != null) {
                Log.e(TAG, "onConnected - NOT NULL ...............: ");
                locationA.setLatitude(mCurrentLocation.getLatitude());
                locationA.setLongitude(mCurrentLocation.getLongitude());
                if (!listOfLocations.isEmpty()) {
                    for (int i = 0; i < listOfLocations.size(); i++) {
                        for (int j = 0; j < latestEvents.size(); j++) {
                            if (latestEvents.get(j).getLocationId().equals(listOfLocations.get(i).getLocationId())) {
                                locationB.setLatitude(listOfLocations.get(i).getLatitude());
                                locationB.setLongitude(listOfLocations.get(i).getLongitude());
                                latestEvents.get(j).setDistance_to(locationA.distanceTo(locationB) / 1000);
                                Log.e("GPS", "onConnected - FOUND ...............: " + locationA.distanceTo(locationB) / 1000 + "KM");
                                if(latestEvents.get(j).getDistance_to() >= maxDistance) {
                                    latestEvents.remove(j);

                                }
                            }
                        }
                    }
                }
            } else {
                Log.e(TAG, "onConnected - NULL ...............: ");
            }

            mAdapter = new Latest_Events_Adapter(latestEvents);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }

/*
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.e(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
            //displayLatestEvents();
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.e(TAG, "Connection failed: " + connectionResult.toString());
        }

*//*        @Override
        public void onLocationChanged(Location location) {

            mCurrentLocation = location;
            Toast.makeText(getContext(), "Location Change....", Toast.LENGTH_SHORT).show();
            displayLatestEvents();
            Log.e(TAG, "Location: " + "long: " + mCurrentLocation.getLongitude() + " lat: " + mCurrentLocation.getLatitude());


        }*//*



        public void onStart() {
            mGoogleApiClient.connect();
            super.onStart();
        }

        public void onStop() {
            mGoogleApiClient.disconnect();
            super.onStop();
        }*/
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return Frag_Group.newInstance(null,null);
            }

            if(position == 2){
                //return Frag_Profile.newInstance(null,null);
                return Fragment_Profile.newInstance(auth.getCurrentUser().getUid(),null);
            }
            if(position == 3){
                //return Frag_Profile.newInstance(null,null);
                return Fragment_Friends_List.newInstance();
            }
            if(position == 4){
                //return Frag_Profile.newInstance(null,null);
                return Fragment_Chat_List.newInstance(null,null);
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Groups";
                case 1:
                    return "Home";
                case 2:
                    return "Profile";
                case 3:
                    return "Friends";
                case 4:
                    return "Chat";
            }
            return null;
        }
    }


}