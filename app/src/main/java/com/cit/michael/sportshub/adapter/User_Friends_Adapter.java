package com.cit.michael.sportshub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.cit.michael.sportshub.Constants.ACCEPTED;
import static com.cit.michael.sportshub.Constants.ACTION_FRIENDS_LIST;
import static com.cit.michael.sportshub.Constants.ACTION_GROUP_ADD_MEMBER;
import static com.cit.michael.sportshub.Constants.ACTION_GROUP_NOT_SELECTED;
import static com.cit.michael.sportshub.Constants.ACTION_GROUP_REMOVE_MEMBER;
import static com.cit.michael.sportshub.Constants.ACTION_GROUP_SELECTED;
import static com.cit.michael.sportshub.Constants.CANCELLED;
import static com.cit.michael.sportshub.Constants.PENDING_REQUEST;

/**
 * Created by micha on 21/02/2017.
 */




public class User_Friends_Adapter extends RecyclerView.Adapter<User_Friends_Adapter.UserViewHolder>{
    List<User> users;
    Context context;
    String uID;
    public String ACTION;


    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,finished;
        public Button btnUnfriend;


        public UserViewHolder(View v) {
            super(v);
            image = (ImageView)v.findViewById(R.id.image);
            title = (TextView)v.findViewById(R.id.lblUserFullname);
/*            totalAttedances = (TextView)v.findViewById(R.id.lblNoAttendences);
            faileAttedances = (TextView)v.findViewById(R.id.lblFaileAttendences);*/
            btnUnfriend = (Button) v.findViewById(R.id.btnUnfriend);
            btnUnfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Status: " + btnUnfriend.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public User_Friends_Adapter(List<User> users, Context context, String uID, String action){
        this.users = users;
        this.context = context;
        this.uID = uID;
        this.ACTION = action;

    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_profile_list, parent, false);

        return new UserViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(User_Friends_Adapter.UserViewHolder holder, int position) {
        holder.title.setText(users.get(position).getUserFullName());
        Picasso.with(context).load(users.get(position).getUserProfileUrl()).placeholder(R.drawable.img_circle_placeholder).resize(100,100).transform(new CircleTransform()).into(holder.image);
        if(ACTION.equals(ACTION_FRIENDS_LIST)) {
            if (users.get(position).getStatus() == PENDING_REQUEST && users.get(position).getAction_user().equals(uID)) {
                holder.btnUnfriend.setText("PENDING");
                holder.btnUnfriend.setTextColor(Color.parseColor("#ff1a1a"));
            } else if (users.get(position).getStatus() == PENDING_REQUEST && !users.get(position).getAction_user().equals(uID)) {
                holder.btnUnfriend.setText("ACCEPT");
                holder.btnUnfriend.setTextColor(Color.parseColor("#ff1a1a"));
            } else if (users.get(position).getStatus() == CANCELLED && !users.get(position).getAction_user().equals(uID)) {
                holder.btnUnfriend.setText("CANCELLED");
                holder.btnUnfriend.setTextColor(Color.parseColor("#ff1a1a"));
            } else if (users.get(position).getStatus() == ACCEPTED) {
                holder.btnUnfriend.setText("UNFRIEND");
            }
        }

        else if(ACTION.equals(ACTION_GROUP_ADD_MEMBER)) {
            if (users.get(position).getStatus() == ACTION_GROUP_SELECTED) {
                holder.btnUnfriend.setText("UNDO");
                holder.btnUnfriend.setTextColor(Color.parseColor("#abb1b2"));
            } else if (users.get(position).getStatus() == ACTION_GROUP_NOT_SELECTED) {
                holder.btnUnfriend.setText("INVITE");
                holder.btnUnfriend.setTextColor(Color.parseColor("#abb1b2"));
            }
        }

        else if(ACTION.equals(ACTION_GROUP_REMOVE_MEMBER)){
            if (users.get(position).getStatus() == ACTION_GROUP_SELECTED) {
                holder.btnUnfriend.setText("UNDO");
                holder.btnUnfriend.setTextColor(Color.parseColor("#abb1b2"));
            } else if (users.get(position).getStatus() == ACTION_GROUP_NOT_SELECTED) {
                holder.btnUnfriend.setText("REMOVE");
                holder.btnUnfriend.setTextColor(Color.parseColor("#abb1b2"));
            }
        }


    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

