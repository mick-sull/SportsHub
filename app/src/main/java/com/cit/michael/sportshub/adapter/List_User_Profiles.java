package com.cit.michael.sportshub.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class List_User_Profiles  extends BaseAdapter {
    Context context;

    List<User> users;
    Typeface fonts1,fonts2;





    public List_User_Profiles(Context context, List<User> users) {
        this.context = context;
        this.users = users;
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
            convertView = layoutInflater.inflate(R.layout.list_profile_list,null);

            viewHolder = new ViewHolder();

            viewHolder.image = (ImageView)convertView.findViewById(R.id.image);
            viewHolder.title = (TextView)convertView.findViewById(R.id.lblUserFullname);
            viewHolder.totalAttedances = (TextView)convertView.findViewById(R.id.lblNoAttendences);
            viewHolder.faileAttedances = (TextView)convertView.findViewById(R.id.lblFaileAttendences);
            viewHolder.btnUnfriend = (Button) convertView.findViewById(R.id.btnUnfriend);




            viewHolder.title.setTypeface(fonts2);
            viewHolder.totalAttedances.setTypeface(fonts1);

            viewHolder.faileAttedances.setTypeface(fonts2);

            convertView.setTag(viewHolder);


        }else {

            viewHolder = (ViewHolder)convertView.getTag();
        }


        User user = (User)getItem(position);
        String uri = user.getUserProfileUrl();
        Picasso.with(context).load(uri).placeholder(R.drawable.img_circle_placeholder).resize(100,100).transform(new CircleTransform()).into(viewHolder.image);


        viewHolder.title.setText(user.getUserFullName());
        viewHolder.totalAttedances.setText("Total games played: " + user.getAttendances());
        viewHolder.faileAttedances.setText("Failed attendances: " + user.getFailedAttendances());

        return convertView;
    }

    private class ViewHolder{
        ImageView image;
        TextView title;
        TextView totalAttedances;
        TextView faileAttedances;
        Button btnUnfriend;


    }
}
