package com.cit.michael.sportshub.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.chat.model.Group_Chat;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by micha on 23/03/2017.
 */

public class Group_Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Group_Chat> mChats;
    Context ctx;

    public Group_Chat_Adapter(Context ctx, List<Group_Chat> chats) {
        mChats = chats;
        this.ctx = ctx;
    }

    public void add(Group_Chat chat) {
        //mChats.add(chat);
        Boolean found = false;

        //Not ideal if the app is to scale
        for (int i = 0; i <= mChats.size() - 1; i++) {
/*            if ((mChats.get(i).getReceiverUid().equals(chat.getReceiverUid()) && mChats.get(i).getSenderUid().equals(chat.getSenderUid()))
                    || (mChats.get(i).getReceiverUid().equals(chat.getSenderUid()) && mChats.get(i).getSenderUid().equals(chat.getReceiverUid()))) {
                mChats.set(i, chat);
                Log.d("FRAGCHAT", "Chat found" +
                        "mChat rID: " + mChats.get(i).getReceiverUid() + "mChat sID" + mChats.get(i).getSenderUid() +
                        "chat rID: " + chat.getReceiverUid() + "chat sID" + chat.getSenderUid());*/

                if (mChats.get(i).getGroupID().equals(chat.getGroupID())) {
                    mChats.set(i, chat);
                }
                //Collections.sort(mChats, new CustomComparator());
                notifyItemChanged(i);
                notifyItemRangeRemoved(0, mChats.size());
/*                mChats.remove(i);
                notifyItemRemoved(i);
                mChats.add(chat);
                notifyItemInserted(mChats.size() - 1);*/
                found = true;
            }


        if (!found) {
            Log.d("FRAGCHAT", "Chat not found");
            mChats.add(chat);
            Collections.sort(mChats, new Group_Chat_Adapter.CustomComparator());
            notifyItemInserted(mChats.size() - 1);
        }
        Collections.sort(mChats, new Group_Chat_Adapter.CustomComparator());
        notifyItemInserted(mChats.size() - 1);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ctx = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.list_open_chats, parent, false);
                viewHolder = new Group_Chat_Adapter.MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.list_open_chats, parent, false);
                viewHolder = new Group_Chat_Adapter.OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;


/*        View viewChat = layoutInflater.inflate(R.layout.list_open_chats, parent, false);
        viewHolder = new Chat_Adapter(.)
        return viewHolder;*/
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).getSenderUid(),
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((Group_Chat_Adapter.MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((Group_Chat_Adapter.OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(Group_Chat_Adapter.MyChatViewHolder myChatViewHolder, int position) {
        Group_Chat chat = mChats.get(position);



        if(chat.getReceiverPictureUrl() == null){
            Picasso.with(myChatViewHolder.profilePic.getContext()).load(R.drawable.fb_default_icon).placeholder(R.drawable.img_circle_placeholder).resize(200, 200).transform(new CircleTransform()).into(myChatViewHolder.profilePic);
        }
        else{
            Picasso.with(myChatViewHolder.profilePic.getContext()).load(chat.getReceiverPictureUrl()).placeholder(R.drawable.img_circle_placeholder).resize(200, 200).transform(new CircleTransform()).into(myChatViewHolder.profilePic);
        }

        //Picasso.with(myChatViewHolder.profilePic.getContext()).load(chat.getProfilePictureUrl()).placeholder(R.drawable.img_circle_placeholder).resize(200, 200).transform(new CircleTransform()).into(myChatViewHolder.profilePic);

        //Last person to send a message
        myChatViewHolder.username.setText(chat.getChatName());
        myChatViewHolder.message.setText("You: " + chat.getMessage());
        myChatViewHolder.timestamp.setText(converteTimestamp(chat.getTimestamp()));
    }

    private void configureOtherChatViewHolder(Group_Chat_Adapter.OtherChatViewHolder otherChatViewHolder, int position) {
        Group_Chat chat = mChats.get(position);

        if(chat.getReceiverPictureUrl() == null){
            Picasso.with(otherChatViewHolder.profilePic.getContext()).load(R.drawable.fb_default_icon).placeholder(R.drawable.img_circle_placeholder).resize(200, 200).transform(new CircleTransform()).into(otherChatViewHolder.profilePic);
        }
        else{
            Picasso.with(otherChatViewHolder.profilePic.getContext()).load(chat.getProfilePictureUrl()).placeholder(R.drawable.img_circle_placeholder).resize(200, 200).transform(new CircleTransform()).into(otherChatViewHolder.profilePic);

        }


        otherChatViewHolder.username.setText(chat.getChatName());
        otherChatViewHolder.message.setText(chat.getSender() +": " + chat.getMessage());
        otherChatViewHolder.timestamp.setText(converteTimestamp(chat.getTimestamp()));


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
        if (TextUtils.equals(mChats.get(position).getSenderUid(),
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }


    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView username;
        TextView message;
        TextView timestamp;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.user_profile_pic);
            username = (TextView) itemView.findViewById(R.id.chat_username);
            message = (TextView) itemView.findViewById(R.id.chat_message);
            timestamp = (TextView) itemView.findViewById(R.id.chat_date);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView username;
        TextView message;
        TextView timestamp;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.user_profile_pic);
            username = (TextView) itemView.findViewById(R.id.chat_username);
            message = (TextView) itemView.findViewById(R.id.chat_message);
            timestamp = (TextView) itemView.findViewById(R.id.chat_date);
        }
    }

    private CharSequence converteTimestamp(String milliseconds) {
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(milliseconds), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }


    public class CustomComparator implements Comparator<Group_Chat> {
        @Override
        public int compare(Group_Chat o1, Group_Chat o2) {
            Log.d("FRAGCHAT", "CustomComparator" + o2.getTimestamp() + " to " + o1.getTimestamp());
            return o2.getTimestamp().compareTo(o1.getTimestamp());
        }
    }

    public List<Group_Chat> getSortedArrayList(){
        return mChats;
    }
}
