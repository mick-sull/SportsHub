package com.cit.michael.sportshub.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
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
import com.cit.michael.sportshub.adapter.RecyclerItemClickListener;
import com.cit.michael.sportshub.adapter.Setting_Adapter;
import com.cit.michael.sportshub.model.Sport;
import com.cit.michael.sportshub.model.Subscription;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

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

    public SettingFragment(Context ctx,List<Sport> listOfAllSport, List<Subscription> listOfSubs ){
        this.ctx = ctx;
        this.listOfAllSport = listOfAllSport;
        this.listOfSubs = listOfSubs;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        //listOfAllSport = new ArrayList<Sport>();
        subscribedSports = new ArrayList<Sport>();
        //listOfSubs = new ArrayList<Subscription>();

        //loadData();

        for(int i = 0; i < listOfAllSport.size(); i++){
            boolean found = false;
            for(int j = 0; j< listOfSubs.size(); j++){
                Log.d("SettingFragment", "FOUND:  " + listOfAllSport.get(i).getSportId() + " - " + listOfSubs.get(j).getSportID());
                if(listOfAllSport.get(i).getSportId().equals(listOfSubs.get(j).getSportID())){
                    found = true;
                    Log.d("SettingFragment", "FOUND:  " + listOfAllSport.get(i).getSportName());
                }
            }
            if(found) {
                listOfAllSport.get(i).setStatus(SUBSCRIPTION_SELECTED);
                subscribedSports.add(listOfAllSport.get(i));
            }
            else {
                listOfAllSport.get(i).setStatus(SUBSCRIPTION_NOT_SELECTED);
            }
        }

        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.list_users);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        recyclerView = (RecyclerView) dialog.findViewById(R.id.rvDialogUserView);

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
        Log.d("SettingFragment", "listOfAllSport.size(): " + listOfAllSport.size());
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

    @OnClick(R.id.txtListUserDone)
    public void doneText(){
        int sizeOfOriginalSubs = listOfSubs.size();//Still will stop the second for going over sports that have just been added.
        for(int i = 0; i < listOfAllSport.size(); i++){
            if(listOfAllSport.get(i).getStatus() == SUBSCRIPTION_SELECTED) {
                boolean found = false;
                for (int j = 0; j < sizeOfOriginalSubs; j++) {
                    if (listOfAllSport.get(i).getSportId().equals(listOfSubs.get(j).getSportID())) { //if it already exists.
                        listOfAllSport.get(j).setStatus(SUBSCRIPTION_SELECTED);
                        found= true;
                    }
                }
                if(!found){
                    Subscription newSub = new Subscription(null, auth.getCurrentUser().getUid(),listOfAllSport.get(i).getSportId(), SUBSCRIPTION_SELECTED);
                    listOfSubs.add(newSub);
                }
            }
        }
        Log.d("SettingFragment", "new list of subs size: " + listOfSubs.size());
       /* service.updateSubscription(listOfSubs).enqueue(new Callback<RestSubscription>() {
            @Override
            public void onResponse(Call<RestSubscription> call, Response<RestSubscription> response) {

            }

            @Override
            public void onFailure(Call<RestSubscription> call, Throwable t) {

            }
        });*/
        dialog.dismiss();
    }

    @OnClick(R.id.txtListUserCancel)
    public void cancelRequest(){
        Log.d("UserListFragment", "@OnClick(R.id.txtListUserCancel)");
        dialog.dismiss();
    }


    public void onDismiss(DialogInterface dialogInterface)
    {
        //Toast.makeText(ctx, "ON_Dismiss", Toast.LENGTH_SHORT).show();

    }



}

