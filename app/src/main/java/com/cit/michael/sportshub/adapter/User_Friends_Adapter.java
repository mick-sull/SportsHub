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

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by micha on 21/02/2017.
 */




public class User_Friends_Adapter extends RecyclerView.Adapter<User_Friends_Adapter.UserViewHolder>{
    List<User> users;
    Context context;

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,totalAttedances, faileAttedances;
        public Button btnUnfriend;


        public UserViewHolder(View v) {
            super(v);
            image = (ImageView)v.findViewById(R.id.image);
            title = (TextView)v.findViewById(R.id.lblUserFullname);
/*            totalAttedances = (TextView)v.findViewById(R.id.lblNoAttendences);
            faileAttedances = (TextView)v.findViewById(R.id.lblFaileAttendences);*/
            btnUnfriend = (Button) v.findViewById(R.id.btnUnfriend);
        }
    }

    public User_Friends_Adapter(List<User> users, Context context){
        this.users = users;
        this.context = context;

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
        if(users.get(position).getStatus() == 0){
            holder.btnUnfriend.setText("PENDING");
            holder.btnUnfriend.setTextColor(Color.parseColor("#ff1a1a"));
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

