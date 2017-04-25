package com.cit.michael.sportshub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Sport;

import java.util.List;

import static com.cit.michael.sportshub.Constants.SUBSCRIPTION_NOT_SELECTED;
import static com.cit.michael.sportshub.Constants.SUBSCRIPTION_SELECTED;

/**
 * Created by micha on 30/03/2017.
 */

public class Setting_Adapter extends RecyclerView.Adapter<Setting_Adapter.SettingViewHolder> {
    List<Sport> sports;
    Context context;
    public class SettingViewHolder extends RecyclerView.ViewHolder {

        TextView txtSport;
        CheckBox cbSport;

        public SettingViewHolder(View v) {
            super(v);
            txtSport = (TextView)v.findViewById(R.id.lblSportsName);
            cbSport = (CheckBox)v.findViewById(R.id.cbSportSub);


        }
    }

    public Setting_Adapter(List<Sport> sports, Context context){
        this.sports = sports;
        this.context = context;
        //Log.d("SettingFragment", "Setting_Adapter .size(): " + sports.size());

    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user_subs, parent, false);

        return new SettingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SettingViewHolder holder, int position) {
        holder.txtSport.setText(sports.get(position).getSportName().toUpperCase());
        if(sports.get(position).getStatus() == SUBSCRIPTION_SELECTED){
            Log.d("SettingFragment", "Setting_Adapter .setChecked(true)");
            holder.cbSport.setChecked(true);
        }
        else if(sports.get(position).getStatus() == SUBSCRIPTION_NOT_SELECTED){
            holder.cbSport.setChecked(false);
            Log.d("SettingFragment", "Setting_Adapter .setChecked(false)");
        }
    }


    @Override
    public int getItemCount() {
        if (sports != null) {
            return sports.size();
        } else {
            return 0;
        }
    }

}
