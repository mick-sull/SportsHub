package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.Profile_Events_Attended_Adapter;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.Sport;
import com.cit.michael.sportshub.model.Subscription;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestProfile;
import com.cit.michael.sportshub.rest.model.RestSport;
import com.cit.michael.sportshub.rest.model.RestSubscription;
import com.cit.michael.sportshub.rest.model.RestUsers;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.cit.michael.sportshub.ui.EventOptionsFragment;
import com.cit.michael.sportshub.ui.SettingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cit.michael.sportshub.Constants.ACTION_FRAG_PROFILE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Profile extends Fragment implements SettingFragment.MyDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "userID";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = getTag();
    private RecyclerView recyclerView;
    private Profile_Events_Attended_Adapter mAdapter;
    @BindView(R.id.profilePictureView1)
    ImageView profilePictureView;
    @BindView(R.id.txtOrganizedProfile) TextView txtOrganizedProfile;
    @BindView(R.id.txtPlayedProfile) TextView txtPlayedProfile;
    @BindView(R.id.txtReliability) TextView txtReliability;
    @BindView(R.id.txtUsernameProfile) TextView txtUsernameProfile;
    @BindView(R.id.lblPerviousEvents) TextView lblPerviousEvents;
    //@BindView(R.id.imgBtnSetting) ImageView imgSettigns;

    //Context ctx;
    NetworkService service;
    private List<Event> listEventsOrganized;
    private List<Event> listEventsParticipated;
    List<Location> listLocatonParticipated;
    List<Location> listLocatonOrganized;
    List<User>  user;
    private FirebaseAuth auth;
    FirebaseInstanceId mFirebaseInstanceId;
    List<Sport> listOfALlSport;
    List<Subscription> listOfSubs;
    SettingFragment editNameDialogFragment;
    //ProfileViewFragment editNameDialogFragment;
    // TODO: Rename and change types of parameters
    private String userID;
    private String mParam2;
    private FloatingActionButton fbSettings;
    LinearLayout lSwitchCompat;
    private SwitchCompat switchCompat;
    SharedPreferences prefs;
    Boolean scChecked;
    List<String> options;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OnFragmentInteractionListener mListener;

    public Fragment_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userID Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Profile newInstance(String userID, String param2) {
        Fragment_Profile fragment = new Fragment_Profile();
        Bundle args = new Bundle();
        args.putString(USER_ID, userID);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(USER_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        listEventsParticipated = new ArrayList<Event>();
        listEventsOrganized = new ArrayList<Event>();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        mFirebaseInstanceId = FirebaseInstanceId.getInstance();
        fbSettings = (FloatingActionButton) view.findViewById(R.id.floatSettings);
        lSwitchCompat = (LinearLayout) view.findViewById(R.id.lSwitchCompat);
        lSwitchCompat.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshProfile);
        switchCompat = (SwitchCompat) view.findViewById(R.id.scSortProfileEvents);
        fbSettings.setVisibility(View.VISIBLE);
        listLocatonParticipated = new ArrayList<Location>();
        listLocatonOrganized = new ArrayList<Location>();
        user = new ArrayList<User>();
        listOfALlSport = new ArrayList<Sport>();
        listOfSubs = new ArrayList<Subscription>();
        prefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        scChecked = prefs.getBoolean("scProfile",false);
        switchCompat.setChecked(scChecked);
        loadEventData();
        loadSubData();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("scProfile", isChecked);
                editor.commit();
                scChecked = isChecked;
                if(scChecked){
                    displayEvents(listEventsOrganized, listLocatonOrganized);
                }
                else{
                    displayEvents(listEventsParticipated, listLocatonParticipated);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                loadEventData();
                loadSubData();
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        options = new ArrayList<>();
                        //options.add("Join Event");
                        options.add("View Event");
                        options.add("Recreate");

                        FragmentTransaction fm = ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                        EventOptionsFragment editNameDialogFragment;
                        if(scChecked) {
                            try {
                                if(listEventsOrganized.get(position).getTimeInMilliSeconds() > Calendar.getInstance().getTime().getTime()) {
                                    options.add("Edit Event");
                                }
                                else{
                                    options.add("Review Participants");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            editNameDialogFragment = new EventOptionsFragment(listEventsOrganized.get(position), getContext(),options, ACTION_FRAG_PROFILE );
                            editNameDialogFragment.show(fm,"aaa");
                        }
                        else{
                            Intent intent;
                            intent = new Intent(getContext(),Activity_Event_Details.class);
                            //Intent intent = new Intent(Activity_Search_Results.this,MapsActivity.class);
                            intent.putExtra("eventSelected", listEventsParticipated.get(position).getEventId());
                            startActivity(intent);
                        }

                    }


                })
        );
    }

    private void loadEventData() {
        service.getUserDetails(auth.getCurrentUser().getUid()).enqueue(new Callback<RestProfile>() {
            @Override
            public void onResponse(Call<RestProfile> call, Response<RestProfile> response) {
                if (!response.body().getError()) {
                    Log.w("TEST123 ACTIVITY", "JSON: " + new Gson().toJson(response));
                    listEventsParticipated = response.body().getEvent();
                    listLocatonParticipated = response.body().getLocation();
                    user = response.body().getUser();
                    if(!scChecked) {
                        if (listEventsParticipated.isEmpty()) {
                            lblPerviousEvents.setText("NO PREVIOUS EVENTS");
                            }
                        else {
                            displayEvents(listEventsParticipated, listLocatonParticipated);
                            }
                        }
                        else{
                            getOrganizedEvents();
                        }

                    if(user.isEmpty()){
                        //getUser();//This will be called if the user hasnt attended an event.
                        Toast.makeText(getContext(), "Cant load profile....", Toast.LENGTH_SHORT).show();
                    }else{
                        checkToken();
                        displayUserInfo();
                    }

                }
                else{
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestProfile> call, Throwable t) {

            }
        });
    }

    private void getOrganizedEvents() {
        service.getOrganizedEvents(auth.getCurrentUser().getUid()).enqueue(new Callback<RestProfile>() {
            @Override
            public void onResponse(Call<RestProfile> call, Response<RestProfile> response) {
                if(response.body().getEvent().isEmpty()){
                    lblPerviousEvents.setText("NO PREVIOUS EVENTS ORGANIZED");
                }
                else {
                    listEventsOrganized = response.body().getEvent();
                    listLocatonOrganized = response.body().getLocation();
                    displayEvents(listEventsOrganized, listLocatonOrganized);
                }
            }

            @Override
            public void onFailure(Call<RestProfile> call, Throwable t) {

            }
        });
    }


    private void loadSubData() {
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
                        listOfALlSport = restSport.getSport();
                    }

                });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity__profile, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.floatSettings)
    public void settings(View rootView) {
        // TODO submit data to server...
        //FragmentManager fm = getActivity().getFragmentManager();


        FragmentTransaction fm = ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        //ProfileViewFragment editNameDialogFragment = new ProfileViewFragment(listUserAttending.get(position).getUserProfileUrl(), listUserAttending.get(position).getUserId());
        editNameDialogFragment = new SettingFragment(getContext(),listOfALlSport,listOfSubs,  this);
        editNameDialogFragment.show(fm, "test");
        Log.d("ABC1", "FragmentTransaction Called ");




    }

    private void displayUserInfo() {
        Log.d(TAG, "displayUserInfo");
        String uri = auth.getCurrentUser().getPhotoUrl().toString();
        Picasso.with(getContext())
                .load(uri)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(400, 400)
                .transform(new CircleTransform())
                .into(profilePictureView);

        txtUsernameProfile.setText(user.get(0).getUserFullName());
        txtOrganizedProfile.setText(user.get(0).getOrganized().toString());
        txtPlayedProfile.setText(user.get(0).getAttendances().toString());

        double reliability = (double)100-(((double)user.get(0).getFailedAttendances()/((double)user.get(0).getFailedAttendances()+(double)user.get(0).getAttendances())*100));

        txtReliability.setText(Integer.toString((int) reliability) + "%");
    }

    public void checkToken() {
        //Toast.makeText(getContext(), "Checking Token...", Toast.LENGTH_SHORT).show();
        //Log.d("ACTPROF: user DB:", user.get(0).getUserToken().toString() + " - mFirebaseInstanceId" + mFirebaseInstanceId.getToken().toString() );
        if (!user.isEmpty()) {
            if (!user.get(0).getUserToken().toString().equals(mFirebaseInstanceId.getToken().toString())) {
                User user = new User();
                user.setUserId(auth.getCurrentUser().getUid());
                user.setUserToken(mFirebaseInstanceId.getToken().toString());
                service.updateUserToken(user).enqueue(new Callback<RestUsers>() {
                    @Override
                    public void onResponse(Call<RestUsers> call, Response<RestUsers> response) {
                        Log.d("Token Update:", "Token updated");

                    }

                    @Override
                    public void onFailure(Call<RestUsers> call, Throwable t) {
                        Log.d("Token Update Fail: ", t.toString());

                    }
                });
            } else {
                Log.d("ACTPROF: user DB:", user.get(0).getUserToken().toString() + " - mFirebaseInstanceId" + mFirebaseInstanceId.getToken().toString());
            }
        }
    }

    private void displayEvents(List<Event> events, List<Location> locations) {
        Log.d("ABC1", "CHECKED: " + scChecked + "eventsize: " + events.size() + "locationssize: " + locations.size());
        mAdapter = new Profile_Events_Attended_Adapter(events, locations);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void OnCloseDialog() {
        Log.d("SettingFragment", "OnCloseDialog Called in profile");
        loadSubData();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
