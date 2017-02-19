package com.cit.michael.sportshub.rest;

/**
 * Created by micha on 17/01/2017.
 */

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class RestClient {
    public static final String BASE_URL = "http://192.168.1.9:3000/api/";
    //public static final String BASE_URL = "http://192.168.43.189:3000/api/"; //phone hotspot

    private static NetworkService sNetworkService;

    public static NetworkService getSportsHubApiClient() {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        if (sNetworkService == null) {
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(rxAdapter)
                    .build();

            sNetworkService = retrofit.create(NetworkService.class);
            return sNetworkService;
        } else {
            return sNetworkService;
        }
    }
}
