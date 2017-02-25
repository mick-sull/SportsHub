package com.cit.michael.sportshub.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.chat.model.Chat;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by micha on 16/02/2017.
 */

//public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<Chat,ChatFirebaseAdapter.MyChatViewHolder> {
public class ChatFirebaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Chat> mChats;
    Context ctx;

    public ChatFirebaseAdapter(List<Chat> chats) {
        mChats = chats;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ctx = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.chat_item_right, parent, false);
                viewHolder = new ChatFirebaseAdapter.MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.chat_item_left, parent, false);
                viewHolder = new ChatFirebaseAdapter.OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((ChatFirebaseAdapter.MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((ChatFirebaseAdapter.OtherChatViewHolder) holder, position);
        }
    }
    private void configureMyChatViewHolder(ChatFirebaseAdapter.MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        myChatViewHolder.txtChatMessage.setText(chat.message);
        myChatViewHolder.tvTimestamp.setText(converteTimestamp(chat.getTimestamp()));
        Picasso.with(myChatViewHolder.ivChatPhoto.getContext()).load(chat.getProfilePictureUrl()).placeholder(R.drawable.img_circle_placeholder).resize(40,40).transform(new CircleTransform()).into(myChatViewHolder.ivChatPhoto);
        //myChatViewHolder.txtUserAlphabet.setText(alphabet);
    }

    private void configureOtherChatViewHolder(ChatFirebaseAdapter.OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        otherChatViewHolder.txtChatMessage.setText(chat.message);
        otherChatViewHolder.tvTimestamp.setText(converteTimestamp(chat.getTimestamp()));
        Picasso.with(otherChatViewHolder.ivChatPhoto.getContext()).load(chat.getProfilePictureUrl()).placeholder(R.drawable.img_circle_placeholder).resize(40,40).transform(new CircleTransform()).into(otherChatViewHolder.ivChatPhoto);

    }



    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, tvTimestamp;
        ImageView ivChatPhoto;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView)itemView.findViewById(R.id.timestamp);
            txtChatMessage = (EmojiconTextView)itemView.findViewById(R.id.txtMessage);
            ivChatPhoto = (ImageView)itemView.findViewById(R.id.ivUserChat);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, tvTimestamp;
        ImageView ivChatPhoto;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView)itemView.findViewById(R.id.timestamp);
            txtChatMessage = (EmojiconTextView)itemView.findViewById(R.id.txtMessage);
            ivChatPhoto = (ImageView)itemView.findViewById(R.id.ivUserChat);
        }
    }
    private CharSequence converteTimestamp(String milliseconds){
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(milliseconds),System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }



/*    public ChatFirebaseAdapter(DatabaseReference ref, String nameUser, Activity_Chat mClickListenerChatFirebase) {
        super(Chat.class, R.layout.chat_item_left, ChatFirebaseAdapter.MyChatViewHolder.class, ref);
        this.nameUser = nameUser;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        //this.mClickListenerChatFirebase = mClickListenerChatFirebase;
    }

    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            return new MyChatViewHolder(view);
        }else*/
/* if (viewType == LEFT_MSG)*//*
 {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MyChatViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Chat model = getItem(position);
        if (model.senderUid.equals(mFirebaseUser.getUid())){
            return RIGHT_MSG;
        }else{
            return LEFT_MSG;
        }
    }

    @Override
    protected void populateViewHolder(MyChatViewHolder viewHolder, Chat model, int position) {
        viewHolder.setIvUser(model.getProfilePictureUrl());
        viewHolder.setTxtMessage(model.getMessage());
        viewHolder.setTvTimestamp(model.getTimestamp());
        viewHolder.tvIsLocation(View.GONE);
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTimestamp,tvLocation;
        EmojiconTextView txtMessage;
        ImageView ivUser,ivChatPhoto;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView)itemView.findViewById(R.id.timestamp);
            txtMessage = (EmojiconTextView)itemView.findViewById(R.id.txtMessage);
*/
/*            tvLocation = (TextView)itemView.findViewById(R.id.tvLocation);
            ivChatPhoto = (ImageView)itemView.findViewById(R.id.img_chat);*//*

            ivUser = (ImageView)itemView.findViewById(R.id.ivUserChat);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Chat model = getItem(position);
*/
/*            if (model.getMapModel() != null){
                mClickListenerChatFirebase.clickImageMapChat(view,position,model.getMapModel().getLatitude(),model.getMapModel().getLongitude());
            }else{
                mClickListenerChatFirebase.clickImageChat(view,position,model.getUserModel().getName(),model.getUserModel().getPhoto_profile(),model.getFile().getUrl_file());
            }*//*

        }

        public void setTxtMessage(String message){
            if (txtMessage == null)return;
            txtMessage.setText(message);
        }

        public void setIvUser(String urlPhotoUser){
            if (ivUser == null)return;
            //Glide.with(ivUser.getContext()).load(urlPhotoUser).centerCrop().transform(new CircleTransform(ivUser.getContext())).override(40,40).into(ivUser);

            Picasso.with(ivUser.getContext()).load(urlPhotoUser).placeholder(R.drawable.img_circle_placeholder).resize(40,40).transform(new CircleTransform()).into(ivUser);
        }

        public void setTvTimestamp(String timestamp){
            if (tvTimestamp == null)return;
            tvTimestamp.setText(converteTimestamp(timestamp));
        }

        public void setIvChatPhoto(String url){
            if (ivChatPhoto == null)return;
            Glide.with(ivChatPhoto.getContext()).load(url)
                    .override(100, 100)
                    .fitCenter()
                    .into(ivChatPhoto);
            ivChatPhoto.setOnClickListener(this);
        }

        public void tvIsLocation(int visible){
            if (tvLocation == null)return;
            tvLocation.setVisibility(visible);
        }

    }

    private CharSequence converteTimestamp(String milliseconds){
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(milliseconds),System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }
*/

}
