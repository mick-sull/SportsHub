package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.ExpandableHeightListView;
import com.cit.michael.sportshub.adapter.ProfileListView;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestUsers;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Friends_List.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Friends_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Friends_List extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ExpandableHeightListView listview;
    private ProfileListView profileListView;
    private List<User> listFriends;
    NetworkService service;
    private FirebaseAuth auth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_Friends_List() {
        // Required empty public constructor
    }

    public static Fragment_Friends_List newInstance(/*String param1, String param2*/) {
        Fragment_Friends_List fragment = new Fragment_Friends_List();
        Bundle args = new Bundle();
/*        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        //fragment.setArguments(args);
        Log.d("FRIENDS LIST", "newInstance");
        return fragment;
    }
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
*//*        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*//*
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
/*        listview = (ExpandableHeightListView) findViewById(R.id.listview);
        profileListView = new ProfileListView(Activity_Event_Details.this, listUserAttending) {
        };

        listview.setAdapter(profileListView);

        return inflater.inflate(R.layout.fragment_fragement__friends__list, container, false);*/
        View rootView = inflater.inflate(R.layout.fragment_fragement__friends__list, container, false);
        listFriends = new ArrayList<User>();
        listview = (ExpandableHeightListView) rootView.findViewById(R.id.listview);
        ButterKnife.bind(this, rootView);

        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        service.getUserFriends(auth.getCurrentUser().getUid()).enqueue(new Callback<RestUsers>() {
            @Override
            public void onResponse(Call<RestUsers> call, Response<RestUsers> response) {
                Log.d("FRIENDS LIST", "Result: " + response.message().toString());
                //if(!response.body().getUser().isEmpty()){
                    listFriends = response.body().getUser();
                    displayFriends();
             //   }
            }

            @Override
            public void onFailure(Call<RestUsers> call, Throwable t) {

            }
        });

        return rootView;
    }

    private void displayFriends() {
        profileListView = new ProfileListView(getContext(), listFriends) {
        };

        listview.setAdapter(profileListView);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
