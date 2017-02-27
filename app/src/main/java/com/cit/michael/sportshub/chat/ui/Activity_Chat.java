package com.cit.michael.sportshub.chat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.chat.FcmNotificationBuilder;
import com.cit.michael.sportshub.chat.adapter.ChatFirebaseAdapter;
import com.cit.michael.sportshub.chat.model.Chat;
import com.cit.michael.sportshub.chat.util.Util;
import com.cit.michael.sportshub.model.User;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class Activity_Chat extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;

    static final String TAG = Activity_Chat.class.getSimpleName();
    static final String CHAT_REFERENCE = "chatmodel";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;


    public Chat chat;
    //Views UI
    private RecyclerView rvListMessage;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseInstanceId mFirebaseInstanceId;
    private ImageView btSendMessage,btEmoji;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;
    private String mId;
    private User receivingUser;
    public static final String ARG_USERS = "users";
    public static final String ARG_RECEIVER = "receiver";
    public static final String ARG_RECEIVER_UID = "receiver_uid";
    public static final String ARG_CHAT_ROOMS = "chat_rooms";
    public static final String ARG_FIREBASE_TOKEN = "firebaseToken";
    public static final String ARG_FRIENDS = "friends";
    public static final String ARG_UID = "uid";
    private RecyclerView mRecyclerViewChat;
    public ChatFirebaseAdapter firebaseAdapter;
    ProgressDialog dialog;




    //File
    private File filePathImageCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__chat);
        bindViews();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseInstanceId = FirebaseInstanceId.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        receivingUser = intent.getParcelableExtra("receivingUser");
/*        if (firebaseAdapter == null) {
            firebaseAdapter = new ChatFirebaseAdapter((ArrayList<Chat>) chatList);
            rvListMessage.setLayoutManager(mLinearLayoutManager);
            rvListMessage.setAdapter(firebaseAdapter);
        }*/

        getMessageFromFirebaseUser(mFirebaseAuth.getCurrentUser().getUid(),  receivingUser.getUserId());

        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        //ChatModel model = new ChatModel(userModel,edMessage.getText().toString(), Calendar.getInstance().getTime().getTime()+"",null);
        //mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(model);
        //mFirebaseDatabaseReference.child(mId).push().setValue(model);
        //edMessage.setText(null);
        chat = new Chat(mFirebaseUser.getDisplayName(),receivingUser.getUserFullName(),mFirebaseUser.getUid(), receivingUser.getUserId(),edMessage.getText().toString(), Calendar.getInstance().getTime().getTime()+"", mFirebaseUser.getPhotoUrl().toString());
        final String room_type_1 = mFirebaseAuth.getCurrentUser().getUid() + "_" + receivingUser.getUserId();
        final String room_type_2 = receivingUser.getUserId() + "_" + mFirebaseAuth.getCurrentUser().getUid();
        //mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(model);

        mFirebaseDatabaseReference.child(ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.d("ActChat", "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    mFirebaseDatabaseReference.child(ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                    //onGetMessagesSuccess(room_type_1);
                    //onGetMessagesSuccess(chat);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.d("ActChat",  "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    mFirebaseDatabaseReference.child(ARG_CHAT_ROOMS).child(room_type_2).child(String.valueOf(chat.timestamp)).setValue(chat);
                    //onGetMessagesSuccess(room_type_2);
                    //onGetMessagesSuccess(chat);
                } else {
                    Log.d("ActChat",  "sendMessageToFirebaseUser else: success");
                    mFirebaseDatabaseReference.child(ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                    //onGetMessagesSuccess(room_type_1);
                    //onGetMessagesSuccess(chat);
                }
                //getMessageFromFirebaseUser(mFirebaseAuth.getCurrentUser().getUid(),  receivingUser.getUserId());


                edMessage.setText(null);
                // send push notification to the receiver
                sendPushNotificationToReceiver(mFirebaseUser.getDisplayName(),
                        edMessage.getText().toString(),
                        mFirebaseUser.getUid(),
                        mFirebaseInstanceId.getToken(),
                        receivingUser.getUserToken());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ///mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }



    private void sendPushNotificationToReceiver(String username,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                String receiverFirebaseToken) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    }




    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        databaseReference.child(ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    //getAllMesages(room_type_1);
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                try {

                                    Chat model = dataSnapshot.getValue(Chat.class);
                                    Log.d("ActChat room_type_2: ", "onChildAdded ChatList message: " + model.getMessage());
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
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    //getAllMesages(room_type_2);
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            //Chat chat = dataSnapshot.getValue(Chat.class);
                            //onGetMessagesSuccess(chat);
                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                try {
                                    Chat model = dataSnapshot.getValue(Chat.class);
                                    Log.d("ActChat room_type_2: ", "onChildAdded ChatList message: " + model.getMessage());
                                    //firebaseAdapter.add(model);
                                    onGetMessagesSuccess(model);
                                    //rvListMessage.scrollToPosition(chatList.size() - 1);
                                    //firebaseAdapter.notifyItemInserted(chatList.size() - 1);
                                } catch (Exception ex) {
                                    Log.e(TAG, ex.getMessage());
                                }
                            }
                            //getMessagensFirebase(room_type_2);
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }


    public void getAllMesages(final String childID) {
        Log.d("ActChat", "ChatList childID: " + childID);
    }


    public void onGetMessagesSuccess(Chat chat) {
        Log.d("ActChat",  "onGetMessagesSuccess " + chat.message + " added");
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
