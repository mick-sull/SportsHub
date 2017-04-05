package com.cit.michael.sportshub.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Event;

import java.text.ParseException;
import java.util.List;

/**
 * Created by micha on 04/04/2017.
 */

public class Latest_Events_Adapter extends   RecyclerView.Adapter<Latest_Events_Adapter.Latest_Events_Holder>{
    private List<Event> eventsList;

    public class Latest_Events_Holder extends RecyclerView.ViewHolder {
        public TextView eventName,eventCost, eventSpaces, eventTime, eventLocation, latest_time_remaining;

        public Latest_Events_Holder(View v) {
            super(v);
            eventName = (TextView) v.findViewById(R.id.latest_eventName);
            eventCost = (TextView) v.findViewById(R.id.latest_eventCost);
            eventSpaces = (TextView) v.findViewById(R.id.latest_eventSpaces);
            eventTime = (TextView) v.findViewById(R.id.latest_eventTime);
            eventLocation = (TextView) v.findViewById(R.id.latest_eventLocation);
            latest_time_remaining = (TextView) v.findViewById(R.id.latest_time_remaining);
        }
    }


    public Latest_Events_Adapter(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    @Override
    public Latest_Events_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_latest_event, parent, false);

        return new Latest_Events_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Latest_Events_Holder holder, int position) {

        holder.eventName.setText(eventsList.get(position).getEventName());
        Log.d("EVENT ADAPTER", "" + eventsList.get(position).getEventName());
        holder.eventTime.setText(eventsList.get(position).getEventTime());
        try {
            holder.eventTime.setText(eventsList.get(position).getFormattedDate() + " | " +eventsList.get(position).getFormattedTime()  );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.eventSpaces.setText("Space remaining: " + eventsList.get(position).getSpaceLeft());
        holder.eventCost.setText(eventsList.get(position).getFormattedCost());
        holder.eventLocation.setText(eventsList.get(position).getLocation_name());
        try {
            holder.latest_time_remaining.setText(eventsList.get(position).converteTimestamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}

