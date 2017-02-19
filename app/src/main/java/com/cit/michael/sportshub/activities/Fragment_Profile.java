package com.cit.michael.sportshub.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.RecyclerItemClickListener;
import com.cit.michael.sportshub.adapter.Profile_Events_Attended_Adapter;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestProfile;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Profile extends Fragment {
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

    //Context ctx;
    NetworkService service;
    List<Event> listEvents;
    List<Location> listLocaton;
    List<User>  user;

    // TODO: Rename and change types of parameters
    private String userID;
    private String mParam2;

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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        listEvents = new ArrayList<Event>();
        listLocaton = new ArrayList<Location>();
        user = new ArrayList<User>();
        recyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);
        Log.d("TEST123", "Activity PROFILE");
        service = RestClient.getSportsHubApiClient();
        service.getUser(userID).enqueue(new Callback<RestProfile>() {
            @Override
            public void onResponse(Call<RestProfile> call, Response<RestProfile> response) {
                Log.w("TEST123", "JSON: " + new Gson().toJson(response));

                listEvents = response.body().getEvent();
                listLocaton = response.body().getLocation();
                user = response.body().getUser();
                if(listEvents.isEmpty()){
                    Toast.makeText(getContext(), "No Previous Events...", Toast.LENGTH_SHORT).show();
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

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity__profile, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void displayUserInfo() {
        Log.d(TAG, "displayUserInfo");
        String uri = user.get(0).getUserProfileUrl();
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

    private void displayEvents() {

        mAdapter = new Profile_Events_Attended_Adapter(listEvents, listLocaton);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
