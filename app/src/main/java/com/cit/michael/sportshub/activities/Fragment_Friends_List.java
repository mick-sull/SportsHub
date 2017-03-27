package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.ExpandableHeightListView;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.adapter.User_Friends_Adapter;
import com.cit.michael.sportshub.model.Friendship;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestRelationship;
import com.cit.michael.sportshub.rest.model.RestUsers;
import com.cit.michael.sportshub.ui.ProfileViewFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cit.michael.sportshub.Constants.ACCEPTED;
import static com.cit.michael.sportshub.Constants.ACTION_FRIENDS_LIST;
import static com.cit.michael.sportshub.Constants.CANCELLED;
import static com.cit.michael.sportshub.Constants.PENDING_REQUEST;
import static com.facebook.FacebookSdk.getApplicationContext;

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
    private List<User> listFriends;
    private List<User> listFriendsRequests;
    NetworkService service;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private User_Friends_Adapter mAdapter;
    public Button btnFriendStatus;
    ProfileViewFragment editNameDialogFragment;
    //@BindView(R.id.btnUnfriend) Button btnFriendStatus;

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
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fragement__friends__list, container, false);
        listFriends = new ArrayList<User>();
        listFriendsRequests = new ArrayList<User>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.friends_list_recycler_view);
        ButterKnife.bind(this, rootView);

        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        service.getUserFriends(auth.getCurrentUser().getUid()).enqueue(new Callback<RestUsers>() {
            @Override
            public void onResponse(Call<RestUsers> call, Response<RestUsers> response) {
                Log.d("FRIENDS LIST", "Result: " + response.message().toString());
                Log.d("ABC", "Request data " + new Gson().toJson(response));
                //if(!response.body().getUserDetails().isEmpty()){
                    listFriends = response.body().getUser();
//                    Log.d("ABC", "getAction_user " + listFriends.get(0).getAction_user());
                    displayFriends();
             //   }
            }

            @Override
            public void onFailure(Call<RestUsers> call, Throwable t) {

            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("FRIEND_DEBUG", "Action User: " + listFriends.get(position).getAction_user() + "Current User: " + auth.getCurrentUser().getUid() );
                        //if(listFriends.get(position).getStatus() == PENDING_REQUEST && listFriends.get(position).getAction_user() != auth.getCurrentUser().getUid()) {
                        if(listFriends.get(position).getStatus() == PENDING_REQUEST && !listFriends.get(position).getAction_user().equals(auth.getCurrentUser().getUid())) {

                            displayDialog(listFriends.get(position), position,"Confirm friendship...","Are you sure you want accept ", ACCEPTED, "Now friends with ");
                        }

                        else if(listFriends.get(position).getStatus() == PENDING_REQUEST && listFriends.get(position).getAction_user().equals(auth.getCurrentUser().getUid())) {

                            displayDialog(listFriends.get(position), position,"Cancel Request...","Are you sure you want cancel the request to add ", CANCELLED, "Request cancelled with ");
                        }
                        else {
                            //FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction fm = ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                            //ProfileViewFragment editNameDialogFragment = new ProfileViewFragment(listUserAttending.get(position).getUserProfileUrl(), listUserAttending.get(position).getUserId());
                            User user = listFriends.get(position);
                            editNameDialogFragment = new ProfileViewFragment(user, getContext());
                            editNameDialogFragment.show(fm, "test");
                        }
                    }
                })
        );
        return rootView;
    }




    private void displayDialog(User user, final int position, String title, String message, final int ACTION, final String toastMessage) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        // Setting Dialog Title
       // alertDialog.setTitle("Confirm friendship...");
        alertDialog.setTitle(title);

        // Setting Dialog Message
        //alertDialog.setMessage("Are you sure you want accept " + user.getUserFullName() + " as a friend?");
        alertDialog.setMessage(message + user.getUserFullName() + " as a friend?");

        // Setting Icon to Dialog
       // alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                Friendship friendship = new Friendship(auth.getCurrentUser().getUid(), listFriends.get(position).getUserId(), ACTION, auth.getCurrentUser().getUid());
                service.friendRequestResponse(friendship).enqueue(new Callback<RestRelationship>() {
                    @Override
                    public void onResponse(Call<RestRelationship> call, Response<RestRelationship> response) {
                        Toast.makeText(getContext(), toastMessage +  listFriends.get(position).getUserFullName(), Toast.LENGTH_SHORT).show();
                        listFriends.get(position).setStatus(ACTION);
                        mAdapter.notifyItemChanged(position, listFriends.get(position));
                    }

                    @Override
                    public void onFailure(Call<RestRelationship> call, Throwable t) {

                    }
                });
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        alertDialog.setNeutralButton("View Profile",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                Intent intent = new Intent(getApplicationContext(), Activity_Profile.class);
                intent.putExtra("user_id", listFriends.get(position).getUserId());
                startActivity(intent);

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    private void displayFriends() {
        //for(User user : listFriends){
        for(int i = 0; i < listFriends.size(); i++){
            if(listFriends.get(i).getStatus() != ACCEPTED && listFriends.get(i).getStatus() != PENDING_REQUEST){
                //Log.d("REMOVED", listFriends.get(i).getUserFullName() + "STATUS: " + listFriends.get(i).getStatus());
                //listFriends.remove(listFriends.get(i));
                listFriends.remove(i);
            }

        }

        mAdapter = new User_Friends_Adapter(listFriends, getContext(), auth.getCurrentUser().getUid(), ACTION_FRIENDS_LIST);
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
