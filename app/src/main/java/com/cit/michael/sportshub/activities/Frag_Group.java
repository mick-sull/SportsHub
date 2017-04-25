package com.cit.michael.sportshub.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.chat.adapter.Group_Chat_Adapter;
import com.cit.michael.sportshub.chat.model.Group_Chat;
import com.cit.michael.sportshub.chat.ui.Activity_Group_Chat;
import com.cit.michael.sportshub.model.Group;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cit.michael.sportshub.chat.ui.Activity_Chat.ARG_CHAT_ROOMS;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag_Group.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag_Group#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_Group extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    NetworkService service;
    private FirebaseAuth auth;
    private List<Group> listOfGroups;
    private Group_Chat_Adapter chatListAdapter;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private List<Group_Chat> listOfChats;
    List<Group_Chat> sortedChatList;

    private OnFragmentInteractionListener mListener;
    TextView info;
    Button createGroup;
    private DatabaseReference mFirebaseDatabaseReference;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public Frag_Group() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frag_Group.
     */
    // TODO: Rename and change types and number of parameters
    public static Frag_Group newInstance(String param1, String param2) {
        Frag_Group fragment = new Frag_Group();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        listOfGroups = new ArrayList<Group>();
        listOfChats = new ArrayList<Group_Chat>();
        sortedChatList = new ArrayList<Group_Chat>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshGroup);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.group_chats_list_recycler_view);
        chatListAdapter = new Group_Chat_Adapter(getContext(), new ArrayList<Group_Chat>());

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        // changeActivity(position);
                        Log.d("FRAG_GROUP ", "addOnItemTouchListener: position: " + position);

                        String receivingUser;
                        sortedChatList = chatListAdapter.getSortedArrayList();
                        Intent intent = new Intent(getContext(), Activity_Group_Chat.class);

                        intent.putExtra("group_name", sortedChatList.get(position).getChatName());
                        Log.d("FRAG_GROUP ", "CHAT NAME:  " +  sortedChatList.get(position).getChatName());
                        intent.putExtra("group_id", sortedChatList.get(position).getGroupID());
                        startActivityForResult(intent, 1);
                    }
                })
        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getGroups();
            }
        });

        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getGroups();
    }

    private void getGroups() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("FRAGGROUP ", "getting groups....");
        if (listOfGroups != null) {
            listOfGroups.clear();
            service = RestClient.getSportsHubApiClient();
            service.getGroups(auth.getCurrentUser().getUid()).enqueue(new Callback<RestGroup>() {
                @Override
                public void onResponse(Call<RestGroup> call, Response<RestGroup> response) {
                    listOfGroups = response.body().getGroup();
                    //displayGroups();
                    if (response.body().getError()) {
                        Log.d("Chat ID: ", "ERROR: " + response.body().getMessage());
                    } else {
                        if (!listOfGroups.isEmpty()) {
                            for (int i = 0; i < listOfGroups.size(); i++) {
                                Query getLastMessages = databaseReference.child(ARG_CHAT_ROOMS).child(listOfGroups.get(i).getGroupId().toString())
                                        .limitToLast(1);

                                getLastMessages.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                            try {

                                                Group_Chat model = dataSnapshot.getValue(Group_Chat.class);
                                                // Log.d("FRAGCHAT ", "displayGroups getMessage" + model.getMessage());
                                                Log.d("FRAG_GROUP12", "onChildAdded ChatList chat NAME: " + model.getChatName());

                                                onGetMessagesSuccess(model);
                                                listOfChats.add(model);
                                            } catch (Exception ex) {
                                                Log.e(getTag(), ex.getMessage());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                        Group_Chat model = dataSnapshot.getValue(Group_Chat.class);
                                        // Log.d("FRAGCHAT ", "onChildChanged ID:" + cID.getChatId() + "  message: " + model.getMessage());
                                        Log.d("FRAGCHAT ", "onChildChanged sender name: " + model.getSender() + "  recevier name: " + model.getReceiver());

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RestGroup> call, Throwable t) {

                }
            });
            mSwipeRefreshLayout.setRefreshing(false);
        }
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

    public void onGetMessagesSuccess(Group_Chat chat) {
        Log.d("FRAGCHAT", "onGetMessagesSuccess " + chat.getMessage() + " added");
        chatListAdapter.add(chat);
        recyclerView.smoothScrollToPosition(chatListAdapter.getItemCount() - 1);
    }

    @OnClick(R.id.floatCreateGroup)
    public void createNewGroup(View rootView) {
        // TODO submit data to server...
        createNewGroup();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("FRAGCHAT",  "onActivityResult CALLED");
        if (requestCode == 1) {

            Log.d("FRAGCHAT",  "onActivityResult CALLED: " + data.getExtras().getString("group_id"));

            if(!data.getExtras().getString("group_id").equals("null") )
                chatListAdapter.removeGroup(data.getExtras().getString("group_id"));

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

    public void createNewGroup() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.fragment_add_sport, null);
        //final String groupName;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light));

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.txtAddSport);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Create new group: ")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                final String groupName = String.valueOf((userInput.getText()));
                                //service = RestClient.getSportsHubApiClient();
                                service.createGroup(groupName, auth.getCurrentUser().getUid()).enqueue(new Callback<RestGroup>() {
                                    @Override
                                    public void onResponse(Call<RestGroup> call, Response<RestGroup> response) {

                                        Group_Chat chat = new Group_Chat(auth.getCurrentUser().getDisplayName(),auth.getCurrentUser().getUid(), "Group Created", Calendar.getInstance().getTime().getTime()+"", auth.getCurrentUser().getPhotoUrl().toString(), groupName, response.body().getGroup().get(0).getGroupId().toString());

                                        mFirebaseDatabaseReference.child(ARG_CHAT_ROOMS).child(response.body().getGroup().get(0).getGroupId().toString()).child(String.valueOf(chat.getTimestamp())).setValue(chat);
                                        listOfGroups.add(response.body().getGroup().get(0));
                                        listOfChats.add(chat);
                                        chatListAdapter.add(chat);
                                        recyclerView.smoothScrollToPosition(chatListAdapter.getItemCount() - 1);
                                    }

                                    @Override
                                    public void onFailure(Call<RestGroup> call, Throwable t) {

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
}