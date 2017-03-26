package com.cit.michael.sportshub.chat.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.chat.adapter.ChatFirebaseAdapter;
import com.cit.michael.sportshub.chat.model.Chat;
import com.cit.michael.sportshub.chat.model.Group_Chat;
import com.cit.michael.sportshub.chat.util.Util;
import com.cit.michael.sportshub.model.Group;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestGroup;
import com.cit.michael.sportshub.rest.model.RestUsers;
import com.cit.michael.sportshub.ui.UserListFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cit.michael.sportshub.activities.Activity_Main.chat_active;
import static com.cit.michael.sportshub.chat.ui.Activity_Chat.ARG_CHAT_ROOMS;

public class Activity_Group_Chat extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
//public class Activity_Group_Chat extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private FirebaseAuth auth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    static final String TAG = Activity_Chat.class.getSimpleName();
    //public Chat chat;
    public Group_Chat chat;
    //Views UI
    private RecyclerView rvListMessage;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseInstanceId mFirebaseInstanceId;
    private ImageView btSendMessage,btEmoji;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;
    public ChatFirebaseAdapter firebaseAdapter;
    ProgressDialog dialog;
    NetworkService service;
    public String groupID;
    public String chatName;
    public List<User> friendsNotInGroup;
    public List<User> groupMembers;
    ArrayList<User> selectedUsersInvFriends;
    ArrayList<String> userTokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__group__chat);

        bindViews();
        Log.d("CHATISSUE", "CHAT ACT CREATED" );
        auth = FirebaseAuth.getInstance();
        mFirebaseInstanceId = FirebaseInstanceId.getInstance();
        mFirebaseUser = auth.getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        service = service = RestClient.getSportsHubApiClient();

        Intent intent = getIntent();
        //receivingUser = intent.getParcelableExtra("receivingUser");
        //receivingUser = intent.getParcelableExtra("receivingUser");
        //groupID = intent.getIntExtra("group_id", 0);
        groupID = Integer.toString(intent.getIntExtra("group_id", 0));
        chatName = intent.getStringExtra("group_name");
        groupID = intent.getStringExtra("group_id");

        friendsNotInGroup = new ArrayList<User>();
        groupMembers = new ArrayList<User>();
        selectedUsersInvFriends = new ArrayList<User>();

        loadData();

        Log.d("FRAG_GROUP ", "Activity_Group_Chat: getGroupID: " + groupID);
        Log.d("FRAG_GROUP ", "Activity_Group_Chat: getChatName: " +chatName);

/*        if (firebaseAdapter == null) {
            firebaseAdapter = new ChatFirebaseAdapter((ArrayList<Chat>) chatList);
            rvListMessage.setLayoutManager(mLinearLayoutManager);
            rvListMessage.setAdapter(firebaseAdapter);
        }*/

        getMessageFromFirebaseUser(chatName);
    }
    @Override
    protected void onStart() {
        super.onStart();

        // Store our shared preference
        chat_active = true;
    }
    @Override
    protected void onStop() {
        super.onStop();

        // Store our shared preference
        chat_active = false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Store our shared preference
        chat_active = false;
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Store our shared preference
        chat_active = true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_chat, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.inviteUSerGroup:
                //This will display the users friends who arent in the group.
                displayFriends();
                return true;
            case R.id.removeUser:
                displayGroupMembers();
                return true;
            case R.id.leaveGroup:
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayGroupMembers() {
        // WORKING
/*
        FragmentManager fm = getSupportFragmentManager();
        //ProfileViewFragment editNameDialogFragment = new ProfileViewFragment(listUserAttending.get(position).getUserProfileUrl(), listUserAttending.get(position).getUserId());
        //User user = listUserAttending.get(position);
        UserListFragment editNameDialogFragment = new UserListFragment(getApplicationContext(), (ArrayList<User>) groupMembers);
        editNameDialogFragment.show(fm, "");
*/

        FragmentManager fm = getFragmentManager();
        UserListFragment editNameDialogFragment = new UserListFragment(getApplicationContext(), (ArrayList<User>) groupMembers);
        editNameDialogFragment.show(fm, "abc");


    }

    private void loadData(){

      service.getNonGroupMembers(groupID.toString(), auth.getCurrentUser().getUid())
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
                        friendsNotInGroup = restUsers.getUser();
                        Log.d("FRAG_GROUP ", "Activity_Group_Chat: friendsNotInGroup size: " + friendsNotInGroup.size());
                    }

                });

        service.getGroupMembers(groupID)
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
                        groupMembers = restUsers.getUser();
                        Log.d("FRAG_GROUP ", "Activity_Group_Chat: friendsNotInGroup size: " + groupMembers.size());
                    }

                });


    }

    private void displayFriends() {


        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
        //final DBHelper dbHelper = new DBHelper(this);
        String[] userFullNames = new String[friendsNotInGroup.size()];
        final boolean[] selectedItems = new boolean[friendsNotInGroup.size()];
        for(int i = 0 ; i < userFullNames.length ; i++){
            userFullNames[i] = friendsNotInGroup.get(i).getUserFullName();
            selectedItems[i] = false;
            for(int j = 0 ; j < selectedUsersInvFriends.size() ; j++){
                if(selectedUsersInvFriends.get(j).getUserId() == friendsNotInGroup.get(i).getUserId()){
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
                selectedUsersInvFriends.add(friendsNotInGroup.get(i));
            }
        }).setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ii) {
                selectedUsersInvFriends.clear();
                for(int i = 0 ; i < selectedItems.length ; i++) {
                    if(selectedItems[i]) {
                        selectedUsersInvFriends.add(friendsNotInGroup.get(i));
                    }
                }
                if(!selectedUsersInvFriends.isEmpty())
                    sendRequestToSelectedUsers();


            }
        }).setCancelable(false).setTitle("Select friends").create().show();
    }

    private void sendRequestToSelectedUsers() {
        if(!selectedUsersInvFriends.isEmpty()){
            userTokens = new ArrayList<String>();
            for(int i = 0; i< selectedUsersInvFriends.size(); i++){
                userTokens.add(selectedUsersInvFriends.get(i).getUserToken().toString());
            }
        }
        Group requestToGroup = new Group(Integer.parseInt(groupID),chatName,userTokens, auth.getCurrentUser().getDisplayName());
        service.sendUsersGroupInvite(requestToGroup).enqueue(new Callback<RestGroup>() {
            @Override
            public void onResponse(Call<RestGroup> call, Response<RestGroup> response) {
                Toast.makeText(Activity_Group_Chat.this, response.message().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RestGroup> call, Throwable t) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Util.initToast(this,"Google Play Services error.");
    }


    @Override
    public void onClick(View view) {
        sendMessageFirebase();
    }

    private void sendMessageFirebase(){

        chat = new Group_Chat(mFirebaseUser.getDisplayName(),mFirebaseUser.getUid(), edMessage.getText().toString(), Calendar.getInstance().getTime().getTime()+"", mFirebaseUser.getPhotoUrl().toString(), chatName, groupID);
        //final String room_type_1 = Integer.toString(groupID);
        //final String room_type_2 = receivingUser.getUserId() + "_" + auth.getCurrentUser().getUid();
        //mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(model);

        mFirebaseDatabaseReference.child(ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(groupID)) {
                    Log.d("ActChat", "sendMessageToFirebaseUser: " + groupID + " exists");
                    mFirebaseDatabaseReference.child(ARG_CHAT_ROOMS).child(groupID).child(String.valueOf(chat.getTimestamp())).setValue(chat);
                } else {
                    Log.d("ActChat",  "sendMessageToFirebaseUser else: success");
                    mFirebaseDatabaseReference.child(ARG_CHAT_ROOMS).child(groupID).child(String.valueOf(chat.getTimestamp())).setValue(chat);
                }

                ///sendNotification();

                edMessage.setText(null);
                // send push notification to the receiver
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ///mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }
    public void getMessageFromFirebaseUser(String chat_Name) {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("FRAG_GROUP", "getMessageFromFirebaseUser called" );

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        databaseReference.child(ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(groupID.toString())) {
                    Log.e("FRAG_GROUP ", "getMessageFromFirebaseUser: " + groupID + " exists");
                    //getAllMesages(room_type_1);
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(ARG_CHAT_ROOMS)
                            .child(groupID).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                try {

                                    Group_Chat model = dataSnapshot.getValue(Group_Chat.class);
                                    Log.d("FRAG_GROUP", "onChildAdded ChatList message: " + model.getMessage());
                                    onGetMessagesSuccess(model);
                                } catch (Exception ex) {
                                    Log.e(TAG, ex.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                    dialog.hide();
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                    dialog.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }
    public void onGetMessagesSuccess(Chat chat) {
        Log.d("ActChat",  "onGetMessagesSuccess " + chat.getMessage() + " added");
        if (firebaseAdapter == null) {
            firebaseAdapter = new ChatFirebaseAdapter(new ArrayList<Chat>());
            rvListMessage.setLayoutManager(mLinearLayoutManager);
            rvListMessage.setAdapter(firebaseAdapter);
        }
        firebaseAdapter.add(chat);
        rvListMessage.smoothScrollToPosition(firebaseAdapter.getItemCount() - 1);
    }
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void bindViews(){
        contentRoot = findViewById(R.id.contentRoot);
        edMessage = (EmojiconEditText)findViewById(R.id.editTextMessage);
        btSendMessage = (ImageView)findViewById(R.id.buttonMessage);
        btSendMessage.setOnClickListener(this);
        btEmoji = (ImageView)findViewById(R.id.buttonEmoji);
        emojIcon = new EmojIconActions(this,contentRoot,edMessage,btEmoji);
        emojIcon.ShowEmojIcon();
        rvListMessage = (RecyclerView)findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
    }

}
