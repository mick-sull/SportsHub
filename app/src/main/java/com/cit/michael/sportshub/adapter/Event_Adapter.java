package com.cit.michael.sportshub.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Event;

import java.util.List;

/**
 * Created by micha on 20/01/2017.
 */

public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.EventViewHolder> {


    private List<Event> eventsList;

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName,eventCost, eventSpaces, eventTime;

        public EventViewHolder(View v) {
            super(v);
            eventName = (TextView) v.findViewById(R.id.eventName);
            eventCost = (TextView) v.findViewById(R.id.eventCost);
            eventSpaces = (TextView) v.findViewById(R.id.eventSpaces);
            eventTime = (TextView) v.findViewById(R.id.eventTime);
        }
    }


    public Event_Adapter(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Event_Adapter.EventViewHolder holder, int position) {

        holder.eventName.setText(eventsList.get(position).getEventName());
        Log.d("EVENT ADAPTER", "" + eventsList.get(position).getEventName());
        holder.eventTime.setText(eventsList.get(position).getEventTime());
        holder.eventSpaces.setText("Space remaining: " + eventsList.get(position).getSpaceLeft());
        holder.eventCost.setText(eventsList.get(position).getFormattedCost());
/*      //  holder.eventLocation.setText(eventsList.get(position).getLocationId());
        holder.eventSpaces.setText("Space remaining: " + eventsList.get(position).getSpaceLeft());
        holder.eventTime.setText(eventsList.get(position).getEventTime());*/
/*        Event event = eventsList.get(position);


        holder.eventName.setText(event.getEventName());
        holder.eventLocation.setText(event.getLocationId());
        holder.eventSpaces.setText("Space remaining: " + event.getSpaceLeft());
        holder.eventTime.setText(event.getEventTime());*/
    }


    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}


