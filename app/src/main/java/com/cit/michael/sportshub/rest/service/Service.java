package com.cit.michael.sportshub.rest.service;

/**
 * Created by micha on 26/01/2017.
 */

public class Service {


}
/*

    //private final RestClient mRestClient;


*/
/*    @Inject
    public Service(*//*
*/
/*RestClient restClient, *//*
*/
/*NetworkService networkService) {
        //mRestClient = restClient;
        mNetworkService = networkService;
    }*//*


    @Inject
    public Service() {

    }

    public Observable<RestSport> getSport(){
        return mNetworkService.getSport().distinct();
    }
*/


/*
    @Inject
    NetworkService networkService;

    public Service(NetworkService networkService) {
        //this.networkService = networkService;

    }

    public Subscription getSport(final GetSportCallback callback) {

        return networkService.getSport()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RestSport>>() {
                    @Override
                    public Observable<? extends RestSport> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<RestSport>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError();
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(RestSport sportListResponse) {
                        callback.onSuccess(sportListResponse);

                    }
                });
    }

    public interface GetSportCallback{
        void onSuccess(RestSport sportListResponse);

        void onError();




    }
}

  */