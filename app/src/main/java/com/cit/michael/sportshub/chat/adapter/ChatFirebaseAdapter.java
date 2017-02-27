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


}
