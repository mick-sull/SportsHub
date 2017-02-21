package com.cit.michael.sportshub.rest;

import com.cit.michael.sportshub.model.Attendee;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.Search;
import com.cit.michael.sportshub.model.Sport;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.model.RestAttendee;
import com.cit.michael.sportshub.rest.model.RestEvent;
import com.cit.michael.sportshub.rest.model.RestEventDetails;
import com.cit.michael.sportshub.rest.model.RestLocation;
import com.cit.michael.sportshub.rest.model.RestProfile;
import com.cit.michael.sportshub.rest.model.RestSport;
import com.cit.michael.sportshub.rest.model.RestUsers;

import java.util.List;

import dagger.Module;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by micha on 17/01/2017.
 */

@Module
public interface NetworkService {

    //Sport
    @GET("sport/")
    Call<RestSport> getSport();

    @POST("sport/new")
    Call<RestSport> createSport(@Body Sport sport);

    @GET("sport/{id}")
    Call<List<RestSport>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    //users
    @POST("users/new")
    Call<RestUsers> addUser(@Body User user);

    @GET("user/{user_id}")
    Call<RestProfile> getUser(@Query("user_id") String userID);

    @GET("user/friends/{user_id}")
    Call<RestUsers> getUserFriends(@Query("user_id") String userID);




    //Location
    @GET("location/")
    Call<RestLocation> getLocation();

    @POST("location/new")
    Call<RestLocation> createLocation(@Body Location location);

    //Event
    @GET("event/")
    Call<RestEvent> getEvent();

    @POST("event/new")
    Call<RestEvent> createEvent(@Body Event event);

    @POST("event/search")
    Call<RestEvent> searchEvent(@Body Search search);

    @GET("event/details/{event_id}")
    Call<RestEventDetails> getEventDetails(@Query("event_id") int eventId);

    @POST("event/removeAttendee")//Possibly should be delete but cant use @Body
    Call<RestAttendee> deleteAttendee(@Body Attendee attendee);

    @POST("event/addAttendee")
    Call<RestAttendee> addAttendee(@Body Attendee attendee);


    //Call<RestEventDetails> getEventDetails();

/*    //Get the users who are attending an event
    @GET("event/attendee")
    Call<RestUsers> getEventAttendee(@Query("event_id"))*/


/*    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("users/new")
    Call<User> createUser(@Body User user);*/
}
