package com.cit.michael.sportshub.rest.service;

import android.util.Log;

import com.cit.michael.sportshub.model.Sport;
import com.cit.michael.sportshub.rest.NetworkService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micha on 17/01/2017.
 */


public class SportService {
    List<Sport> sports;
    NetworkService service;
    public SportService() {
        Log.d("SportService", "" + "reached");
        sports = new ArrayList<Sport>();
    }


    /*

    public List<Sport> listOfSports;
*/

   /* public List<Sport> getSport(){

     *//*   Log.d("SportService", "" + "reached");
        sports = new ArrayList<Sport>();
        service = RestClient.getSportsHubApiClient();
        service.getSport().enqueue(new Callback<RestSport>() {
            @Override
            public void onResponse(Call<RestSport> call, Response<RestSport> response) {
                if(response.body().getError()){
                    //Toast.makeText(ctx.getApplicationContext(), "Error: Couldnt not retrieve sport data...", Toast.LENGTH_SHORT).show();
                    sports = null;
                    Log.d("SportService", " null " + sports.size());

                }
                else{
                    sports = response.body().getSport();
                    Log.d("SportService", " else: " + sports.size());


                }
            }
            @Override
            public void onFailure(Call<RestSport> call, Throwable t) {

            }
        });
        Log.d("SportService", " return size: " + sports.size());
        return sports;
*//*
        //return null;
    }*/

}


