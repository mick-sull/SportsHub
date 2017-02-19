package com.cit.michael.sportshub.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Location;

import java.text.ParseException;
import java.util.List;

/**
 * Created by micha on 15/02/2017.
 */

public class Profile_Events_Attended_Adapter extends RecyclerView.Adapter<Profile_Events_Attended_Adapter.EventViewHolder> {


    private List<Event> eventsList;
    private List<Location> locationList;

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName,eventLocation, evenDate , eventTime;

        public EventViewHolder(View v) {
            super(v);
            eventName = (TextView) v.findViewById(R.id.profileEventName);
            eventLocation = (TextView) v.findViewById(R.id.profileEventLocation);
            evenDate = (TextView) v.findViewById(R.id.profileEventDate);
            eventTime = (TextView) v.findViewById(R.id.eventTime);
        }
    }


    public Profile_Events_Attended_Adapter(List<Event> eventsList, List<Location> locationList) {
        this.eventsList = eventsList;
        this.locationList = locationList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_events_attended, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        holder.eventName.setText(eventsList.get(position).getEventName());
        Log.d("EVENT ADAPTER", "" + eventsList.get(position).getEventName());
        holder.eventTime.setText(eventsList.get(position).getEventTime());
        try {
            holder.evenDate.setText(eventsList.get(position).getFormattedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //holder.eventLocation.setText(eventsList.get(position).);
        for(int i = 0; i<=locationList.size()-1;i++){
            Log.d("TEST123 i: ", i + "Position: " + position + " LocationList Size: " + locationList.size());
            if(eventsList.get(position).getLocationId().equals(locationList.get(i).getLocationId())){
                Log.d("TEST123 i: ", i + "Location ID: " + position + " LocationList Size: " + locationList.size());
               holder.eventLocation.setText(locationList.get(i).getLocationName() + ", " + locationList.get(i).getAddress1()   + ", " + locationList.get(i).getAddress2());
            }
        }

    }



    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}

