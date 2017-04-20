package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.adapter.Event_Adapter;
import com.cit.michael.sportshub.model.Event;

import java.util.ArrayList;
import java.util.List;

public class Activity_Search_Results extends AppCompatActivity {

    Context ctx;
    //ParkingAdapter parkingAdapter;
    //List<Event> arrayEvents;
    private List<Event> arrayEvents = new ArrayList<>();
    private RecyclerView recyclerView;
    private Event_Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__search__results);
        arrayEvents = getIntent().getParcelableArrayListExtra("searchResults");
        Log.d("EVENT", "SEARCH RESULTS SIZE: " + arrayEvents.size());


        recyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);

        mAdapter = new Event_Adapter(arrayEvents);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(ctx, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                       // changeActivity(position);
                        Intent intent = new Intent(Activity_Search_Results.this,Activity_Event_Details.class);
                        //Intent intent = new Intent(Activity_Search_Results.this,MapsActivity.class);
                        intent.putExtra("eventSelected", arrayEvents.get(position).getEventId());
                        startActivity(intent);
                    }


                })
        );


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Search Results");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}


