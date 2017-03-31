package com.cit.michael.sportshub.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micha on 30/03/2017.
 */

public class SubscriptionsForUpdate {
    List<Subscription> listOfSubs;

    @Nullable
    @SerializedName("sub_id")
    @Expose
    List<String> subID;
    @SerializedName("user_id")
    @Expose
    List<String> userID;
    @SerializedName("sport_id")
    @Expose
    List<String> sportID;
    @SerializedName("active")
    @Expose
    List<String> active;



    public SubscriptionsForUpdate(List<Subscription> listOfSubs) {
        this.listOfSubs = listOfSubs;
        subID = new ArrayList<String>();
        userID = new ArrayList<String>();
        sportID = new ArrayList<String>();
        active = new ArrayList<String>();

        intArrays(listOfSubs);
    }

    private void intArrays(List<Subscription> listOfSubs) {
        for(int i = 0; i < listOfSubs.size(); i++){
            subID.add(listOfSubs.get(i).getSubscriptionID());
            userID.add(listOfSubs.get(i).getUserID());
            sportID.add(listOfSubs.get(i).getSportID().toString());
            active.add(listOfSubs.get(i).getActive().toString());

        }
    }
}
