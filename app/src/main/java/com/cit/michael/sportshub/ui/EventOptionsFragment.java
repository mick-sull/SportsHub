package com.cit.michael.sportshub.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.activities.Activity_Event_Details;
import com.cit.michael.sportshub.activities.Activity_Organize_Event;
import com.cit.michael.sportshub.activities.MapsActivity;
import com.cit.michael.sportshub.adapter.Options_Adapter;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.model.Event;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cit.michael.sportshub.Constants.ACTION_EDIT_EVENT;
import static com.cit.michael.sportshub.Constants.ACTION_FRAG_MAIN;
import static com.cit.michael.sportshub.Constants.ACTION_FRAG_PROFILE;
import static com.cit.michael.sportshub.Constants.ACTION_RECREATE;

/**
 * Created by micha on 04/04/2017.
 */

public class EventOptionsFragment  extends android.support.v4.app.DialogFragment {

    Event event;
    Dialog dialog;
    private RecyclerView recyclerView;
    Context ctx;
    private Options_Adapter mAdapter;
    List<String> options;
    String ACTION;

    public EventOptionsFragment(Event event, Context ctx, List<String> options, String action){
        this.event = event;
        this.ctx = ctx;
        this.options = options;
        this.ACTION = action;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.fragment_list_options);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
/*        options = new ArrayList<>();
        //options.add("Join Event");
        options.add("View Event");
        options.add("View Map");*/

        recyclerView = (RecyclerView) dialog.findViewById(R.id.rvDialogOptions);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ctx, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Store the indexes of the clicks
                Intent intent;
                if (ACTION.equals(ACTION_FRAG_MAIN)){
                    if (position == 0) {
                        intent = new Intent(ctx, Activity_Event_Details.class);
                        //Intent intent = new Intent(Activity_Search_Results.this,MapsActivity.class);
                        intent.putExtra("eventSelected", event.getEventId());
                        startActivity(intent);
                    }
                    if (position == 1) {
                        intent = new Intent(ctx, MapsActivity.class);
                        //Intent intent = new Intent(Activity_Search_Results.this,MapsActivity.class);
                        intent.putExtra("event", event);
                        startActivity(intent);
                }
            }
                if(ACTION.equals(ACTION_FRAG_PROFILE)){
                    if(position ==0){//View Event
                        intent = new Intent(ctx, Activity_Event_Details.class);
                        intent.putExtra("eventSelected", event.getEventId());
                        startActivity(intent);
                    }
                    else if(position ==1){//RecreateEvent
                        intent = new Intent(ctx, Activity_Organize_Event.class);
                        intent.putExtra("event", event);
                        intent.putExtra("action", ACTION_RECREATE);
                        startActivity(intent);

                    }
                    else if(position ==2){//Edit Event
                        intent = new Intent(ctx, Activity_Organize_Event.class);
                        intent.putExtra("event", event);
                        intent.putExtra("action", ACTION_EDIT_EVENT);
                        startActivity(intent);
                    }
                }
            }
            })
        );


        mAdapter = new Options_Adapter(options);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        ButterKnife.bind(this, dialog);
        return dialog;//txtListUserCancelOptions
    }

    @OnClick(R.id.txtListUserCancelOptions)
    public void cancelRequest(){
        Log.d("UserListFragment", "@OnClick(R.id.txtListUserCancel)");
        dialog.dismiss();
    }
}
