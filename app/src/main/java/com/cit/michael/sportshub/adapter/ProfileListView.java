package com.cit.michael.sportshub.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by micha on 10/02/2017.
 */

public class ProfileListView extends BaseAdapter {
    Context context;

    List<User> users;
    Typeface fonts1,fonts2;





    public ProfileListView(Context context, List<User> bean) {
        this.context = context;
        this.users = bean;
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        fonts1 =  Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Light.ttf");

        fonts2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Regular.ttf");

        ViewHolder viewHolder = null;

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.profile_list,null);

            viewHolder = new ViewHolder();

            viewHolder.image = (ImageView)convertView.findViewById(R.id.image);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.discription = (TextView)convertView.findViewById(R.id.description);
            viewHolder.date = (TextView)convertView.findViewById(R.id.date);




            viewHolder.title.setTypeface(fonts2);
            viewHolder.discription.setTypeface(fonts1);

            viewHolder.date.setTypeface(fonts2);

            convertView.setTag(viewHolder);


        }else {

            viewHolder = (ViewHolder)convertView.getTag();
        }


        User user = (User)getItem(position);
        String uri = user.getUserProfileUrl();
        Picasso.with(context).load(uri).placeholder(R.drawable.img_circle_placeholder).resize(100,100).transform(new CircleTransform()).into(viewHolder.image);


        viewHolder.title.setText(user.getUserFullName());
        viewHolder.discription.setText("Total games played: " + user.getAttendances());
        viewHolder.date.setText("Failed attendances: " + user.getFailedAttendances());

        return convertView;
    }

    private class ViewHolder{
        ImageView image;
        TextView title;
        TextView discription;
        TextView date;





    }
}
