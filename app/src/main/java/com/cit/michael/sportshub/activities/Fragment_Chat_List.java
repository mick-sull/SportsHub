package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.chat.adapter.Chat_Adapter;
import com.cit.michael.sportshub.chat.model.Chat;
import com.cit.michael.sportshub.chat.ui.Activity_Chat;
import com.cit.michael.sportshub.model.Conversation;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestConversations;
import com.cit.michael.sportshub.rest.model.RestProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cit.michael.sportshub.activities.Activity_Main.chat_active;
import static com.cit.michael.sportshub.chat.ui.Activity_Chat.ARG_CHAT_ROOMS;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Chat_List.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Chat_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Chat_List extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    NetworkService service;


    private FirebaseAuth auth;
    private Chat_Adapter chatListAdapter;
    private RecyclerView recyclerView;
    private List<Chat> listOfChats;
    private List<Conversation> listOfConversationIDs;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OnFragmentInteractionListener mListener;

    public Fragment_Chat_List() {
        // Required empty public constructor
    }


    public static Fragment_Chat_List newInstance(String param1, String param2) {
        Fragment_Chat_List fragment = new Fragment_Chat_List();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_frag__chat__list, container, false);
        auth = FirebaseAuth.getInstance();


        listOfChats = new ArrayList<Chat>();
        listOfConversationIDs = new ArrayList<Conversation>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.chats_list_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshChat);

        chatListAdapter = new Chat_Adapter(getContext(),new ArrayList<Chat>());
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getData();
            }
        });


        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        // changeActivity(position);
                        Log.d("FRAGCHAT ", "addOnItemTouchListener: position: " + position);
                        List<Chat> sortedChatList = new  ArrayList<Chat>();
                        String receivingUser;
                        sortedChatList = chatListAdapter.getSortedArrayList();

                        if(auth.getCurrentUser().getUid().equals(sortedChatList.get(position).getSenderUid())){
                            receivingUser = sortedChatList.get(position).getReceiverUid();
                        }
                        else{
                            receivingUser = sortedChatList.get(position).getSenderUid();
                        }
                        service.getUser(receivingUser).enqueue(new Callback<RestProfile>() {
                            @Override
                            public void onResponse(Call<RestProfile> call, Response<RestProfile> response) {
                                if(!response.body().getError()){
                                    Intent intent = new Intent(getContext(), Activity_Chat.class);
                                    intent.putExtra("receivingUser",  response.body().getUser().get(0));
                                    startActivity(intent);

                                }
                            }

                            @Override
                            public void onFailure(Call<RestProfile> call, Throwable t) {

                            }
                        });
                    }


                })
        );



/*        mLinearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLinearLayoutManager.setStackFromEnd(true);*/

        ButterKnife.bind(this, rootView);

        return rootView;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("FRAGCHAT ", "onViewCreated");
        Log.d("FRAGCHAT1 ", "is chat opened: " + chat_active);
        getData();
    }

    private void getData() {
        Log.d("FRAGCHAT ", "getData");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        service = RestClient.getSportsHubApiClient();
        service.getListOfConversations(auth.getCurrentUser().getUid()).enqueue(new Callback<RestConversations>() {
            @Override
            public void onResponse(Call<RestConversations> call, Response<RestConversations> response) {
                listOfConversationIDs = response.body().getConversations();
                if(response.body().getError()){
                    Log.d("Chat ID: ", "ERROR: " + response.body().getMessage());
                }
                else {
                    if (!listOfConversationIDs.isEmpty()) {
                        for (final Conversation cID : listOfConversationIDs) {
                            Query getLastMessages = databaseReference.child(ARG_CHAT_ROOMS).child(cID.getChatId())
                                    .limitToLast(1);

                            getLastMessages.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                        try {

                                            Chat model = dataSnapshot.getValue(Chat.class);

                                            Log.d("FRAGCHAT ", "addChildEventListener ID:" + cID.getChatId() + "  message: " + model.getMessage());
                                            Log.d("FRAGCHAT ", "addChildEventListener sender name: " + model.getSender() + "  recevier name: " + model.getReceiver());

                                            onGetMessagesSuccess(model);
                                            listOfChats.add(model);
                                        } catch (Exception ex) {
                                            Log.e(getTag(), ex.getMessage());
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                    Chat model = dataSnapshot.getValue(Chat.class);
                                    Log.d("FRAGCHAT ", "onChildChanged ID:" + cID.getChatId() + "  message: " + model.getMessage());
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
            public void onFailure(Call<RestConversations> call, Throwable t) {

            }
        });
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void sortChatListByDate() {
        Log.d("FRAGCHAT",  "sortChatListByDate ");
        Collections.sort(listOfChats, new CustomComparator());
        for (final Chat chat : listOfChats) {
            onGetMessagesSuccess(chat);
        }


    }

    public void onGetMessagesSuccess(Chat chat) {
        Log.d("FRAGCHAT",  "onGetMessagesSuccess " + chat.getMessage() + " added");
/*        if (chatListAdapter == null) {
            Log.d("FRAGCHAT",  "onGetMessagesSuccess chatListAdapter == null" );
            chatListAdapter = new Chat_Adapter(getContext(),new ArrayList<Chat>());
            layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(chatListAdapter);
        }*/
        chatListAdapter.add(chat);
        recyclerView.smoothScrollToPosition(chatListAdapter.getItemCount() - 1);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("FRAGCHAT",  "onActivityResult CALLED");
        if (requestCode == 1) {
            Log.d("FRAGCHAT",  "onActivityResult" + data.getExtras().get("group_id") );
            // make use of "data" = profit
            for(int i = 0; i< listOfChats.size(); i++){

            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class CustomComparator implements Comparator<Chat> {

        @Override
        public int compare(Chat o1, Chat o2) {
            Log.d("FRAGCHAT",  "CustomComparator" + o1.getTimestamp() +" to " + o2.getTimestamp() );
            return o1.getTimestamp().compareTo(o2.getTimestamp());
        }
    }
}
