package com.cit.michael.sportshub.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.activities.Activity_Login;
import com.cit.michael.sportshub.activities.Fragment_Profile;
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.adapter.Setting_Adapter;
import com.cit.michael.sportshub.model.Sport;
import com.cit.michael.sportshub.model.Subscription;
import com.cit.michael.sportshub.model.SubscriptionsForUpdate;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestSubscription;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cit.michael.sportshub.Constants.SP_DISTANCE;
import static com.cit.michael.sportshub.Constants.SP_NOTIFICATIONS;
import static com.cit.michael.sportshub.Constants.SP_SETTINGS;
import static com.cit.michael.sportshub.Constants.SUBSCRIPTION_NOT_SELECTED;
import static com.cit.michael.sportshub.Constants.SUBSCRIPTION_SELECTED;

/**
 * Created by micha on 30/03/2017.
 */

public class SettingFragment extends DialogFragment {
    Context ctx;
    Dialog dialog;
    NetworkService service;
    private FirebaseAuth auth;
    List<Sport> listOfAllSport;
    List<Subscription> listOfSubs;//List of current subscriptions
    private RecyclerView recyclerView;
    private Setting_Adapter mAdapter;
    List<Sport> subscribedSports;
    MyDialogListener mListener;
    TextView lblSettingDistanceSelected;
    CheckBox cbNotification;
    SharedPreferences prefs;

    public SettingFragment(Context ctx, List<Sport> listOfAllSport, List<Subscription> listOfSubs, Fragment_Profile listener  ){
        this.ctx = ctx;
        this.listOfAllSport = listOfAllSport;
        this.listOfSubs = listOfSubs;
        mListener = listener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        //listOfAllSport = new ArrayList<Sport>();
        subscribedSports = new ArrayList<Sport>();
        //listOfSubs = new ArrayList<Subscription>();


        //loadData();
        if(listOfAllSport != null) {
            for (int i = 0; i < listOfAllSport.size(); i++) {
                boolean found = false;
                for (int j = 0; j < listOfSubs.size(); j++) {
                    Log.d("SettingFragment", "FOUND:  " + listOfAllSport.get(i).getSportId() + " - " + listOfSubs.get(j).getSportID());
                    if (listOfAllSport.get(i).getSportId().equals(listOfSubs.get(j).getSportID()) && listOfSubs.get(j).getActive() == SUBSCRIPTION_SELECTED) {
                        found = true;
                        Log.d("SettingFragment", "FOUND:  " + listOfAllSport.get(i).getSportName());
                    }
                }
                if (found) {
                    listOfAllSport.get(i).setStatus(SUBSCRIPTION_SELECTED);
                    subscribedSports.add(listOfAllSport.get(i));
                } else {
                    listOfAllSport.get(i).setStatus(SUBSCRIPTION_NOT_SELECTED);
                }
            }
        }
            else {
            Toast.makeText(ctx, "Unable to retrevie subscription data...", Toast.LENGTH_SHORT).show();
        }


        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.list_settings);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final CrystalSeekbar seekbar = (CrystalSeekbar ) dialog.findViewById(R.id.rsDistance);
        //final CrystalRangeSeekbar rangeSeekbar = (SeekBar) dialog.findViewById(R.id.rsDistance);
        lblSettingDistanceSelected = (TextView) dialog.findViewById(R.id.lblSettingDistanceSelected);
        cbNotification = (CheckBox) dialog.findViewById(R.id.cbReceiveNotifiactions);
        prefs = getActivity().getSharedPreferences(SP_SETTINGS, Context.MODE_PRIVATE);
        cbNotification.setChecked(prefs.getBoolean(SP_NOTIFICATIONS, false));
        cbNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(SP_NOTIFICATIONS, isChecked);
                editor.commit();
            }
        });


        //seekbar.setPosition(prefs.getInt("distance", 50));
        seekbar.setMinStartValue(prefs.getInt(SP_DISTANCE, 50)).apply();
        lblSettingDistanceSelected.setText(prefs.getInt("distance", 50) + "km.");

        // set listener
        seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                lblSettingDistanceSelected.setText(String.valueOf(minValue) + "km.");
            }
        });

// set final value listener
        seekbar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                Log.d("CRS=>", String.valueOf(value));
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(SP_DISTANCE, value.intValue());
                editor.commit();
            }
        });

        recyclerView = (RecyclerView) dialog.findViewById(R.id.rvSettings);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ctx, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Store the indexes of the clicks
                Log.d("SettingFragment", "listOfAllSport.size() clicked: " + listOfAllSport.get(position).getSportName());
                boolean found = false;
                for(int i = 0; i < subscribedSports.size(); i++){
                    if(subscribedSports.get(i).getSportId().equals(listOfAllSport.get(position).getSportId())){
                        found = true;
                        subscribedSports.remove(i);

                    }
                }
                if (!found){
                    listOfAllSport.get(position).setStatus(SUBSCRIPTION_SELECTED);
                    subscribedSports.add(listOfAllSport.get(position));
                }
                else{
                    listOfAllSport.get(position).setStatus(SUBSCRIPTION_NOT_SELECTED);
                }
                mAdapter.notifyItemChanged(position);
            }
                })

        );
        //Log.d("SettingFragment", "listOfAllSport.size(): " + listOfAllSport.size());
        //Log.d("SettingFragment", "listOfSubs.size(): " + listOfSubs.size());
        mAdapter = new Setting_Adapter(listOfAllSport, ctx);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        ButterKnife.bind(this, dialog);
        return dialog;
    }

    private void loadData() {
  /*      service.getSubscribedSport(auth.getCurrentUser().getUid())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestSubscription>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestSubscription restSub) {
                        listOfSubs = restSub.getSubscription();
                    }

                });

        service.getSport()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestSport>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestSport restSport) {
                        listOfAllSport = restSport.getSport();
                    }

                });*/

    }

    @OnClick(R.id.txtSettingsDone)
    public void doneText(){
        int sizeOfOriginalSubs = listOfSubs.size();//Still will stop the second for going over sports that have just been added.

        for(int i = 0; i < listOfAllSport.size(); i++){
            //if(listOfAllSport.get(i).getStatus() == SUBSCRIPTION_SELECTED) {
                boolean found = false;
                for (int j = 0; j < sizeOfOriginalSubs; j++) {
                    if (listOfAllSport.get(i).getSportId().equals(listOfSubs.get(j).getSportID()) && listOfAllSport.get(i).getStatus() == SUBSCRIPTION_SELECTED ) { //if it already exists.
                        listOfAllSport.get(i).setStatus(SUBSCRIPTION_SELECTED);
                        listOfSubs.get(j).setActive(SUBSCRIPTION_SELECTED);
                        found= true;
                    }
                    else if(listOfAllSport.get(i).getSportId().equals(listOfSubs.get(j).getSportID()) && listOfAllSport.get(i).getStatus() == SUBSCRIPTION_NOT_SELECTED){
                        listOfSubs.get(j).setActive(SUBSCRIPTION_NOT_SELECTED);
                        //found= true;
                    }
                }
                if(!found && listOfAllSport.get(i).getStatus() == SUBSCRIPTION_SELECTED){
                    Subscription newSub = new Subscription("null", auth.getCurrentUser().getUid(),listOfAllSport.get(i).getSportId(), SUBSCRIPTION_SELECTED);
                    listOfSubs.add(newSub);
                }
            }
        //}
        Log.d("SettingFragment", "new list of subs size: " + listOfSubs.size());
        SubscriptionsForUpdate newList = new SubscriptionsForUpdate(listOfSubs);
        service.updateSubscription(newList).enqueue(new Callback<RestSubscription>() {
            @Override
            public void onResponse(Call<RestSubscription> call, Response<RestSubscription> response) {
                Toast.makeText(ctx,  "Settings updated....", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RestSubscription> call, Throwable t) {

            }
        });
        mListener.OnCloseDialog();
        dialog.dismiss();
    }

    @OnClick(R.id.txtSettingsCancel)
    public void cancelRequest(){
        Log.d("UserListFragment", "@OnClick(R.id.txtListUserCancel)");
        dialog.dismiss();
        dialog.cancel();

    }

    @OnClick(R.id.txtLogout)
    public void txtLogout(){
        Log.d("UserListFragment", "@OnClick(R.id.txtListUserCancel)");
        //dialog.dismiss();
        //dialog.cancel();
        logoutConfirmation();

    }



    public interface MyDialogListener {
        void OnCloseDialog();
    }

    private void logoutConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                logout();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void logout(){
        AuthUI.getInstance().signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //user logged out
                        Log.d("AUTH", "User Logged Out");
                        Intent intent = new Intent(ctx, Activity_Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ctx.startActivity(intent);


                    }
                });
        dialog.dismiss();
    }
/*
    public void onDismiss(DialogInterface dialogInterface)
    {
        //Toast.makeText(ctx, "ON_Dismiss", Toast.LENGTH_SHORT).show();

    }*/



}

