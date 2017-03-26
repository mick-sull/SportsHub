package com.cit.michael.sportshub.rest;

import com.cit.michael.sportshub.model.Attendee;
import com.cit.michael.sportshub.model.ChatNotification;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Friendship;
import com.cit.michael.sportshub.model.Group;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.Search;
import com.cit.michael.sportshub.model.Sport;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.model.RestAttendee;
import com.cit.michael.sportshub.rest.model.RestBase;
import com.cit.michael.sportshub.rest.model.RestConversations;
import com.cit.michael.sportshub.rest.model.RestEvent;
import com.cit.michael.sportshub.rest.model.RestEventDetails;
import com.cit.michael.sportshub.rest.model.RestGroup;
import com.cit.michael.sportshub.rest.model.RestLocation;
import com.cit.michael.sportshub.rest.model.RestProfile;
import com.cit.michael.sportshub.rest.model.RestRelationship;
import com.cit.michael.sportshub.rest.model.RestSport;
import com.cit.michael.sportshub.rest.model.RestUsers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by micha on 17/01/2017.
 */


public interface NetworkService {

    //Sport
    @GET("sport/")
    Call<RestSport> getSport();
/*    @GET("sport/")
    Observable<RestSport> getSport();*/


    @POST("sport/new")
    Call<RestSport> createSport(@Body Sport sport);


    @GET("sport/{id}")
    Call<List<RestSport>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    //users
    @POST("users/new")
    Call<RestUsers> addUser(@Body User user);

    @GET("user/{user_id}")
    Call<RestProfile> getUser(@Query("user_id") String userID);

    @GET("user/details/{user_id}")
    Call<RestProfile> getUserDetails(@Query("user_id") String userID);

    @GET("user/friends/{user_id}")
    Call<RestUsers> getUserFriends(@Query("user_id") String userID);

    @POST("user/friends/request")
    Call<RestRelationship> sendFriendRequest (@Body Friendship friendship);

    @GET("user/friends/friendshipStatus/{user_id}")
    Call<RestRelationship> getFriendshipStatus(@Query("user_id") String userID);

    @POST("user/friends/response")
    Call<RestRelationship> friendRequestResponse (@Body Friendship friendship);

    @GET("user/chat/{user_id}")
    Call<RestConversations> getListOfConversations(@Query("user_id") String userID);

    @POST("user/updateToken")
    Call<RestUsers> updateUserToken(@Body User user);


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


    //Notifications
    @POST("notification/chat_notification")
    Call<RestBase> sendChatNotification(@Body ChatNotification chatNotification);


    /********************************************************
     GROUPS
     ********************************************************    */


    @GET("group/groups/{user_id}")
    Call<RestGroup> getGroups(@Query("user_id") String userID);

    //This should be a post but I need the new group ID passed back.
    @GET("group/groups/new/{group_name}")
    Call<RestGroup> createGroup(@Query("group_name") String groupName,
                                @Query("user_id") String userID);

    @GET("group/invite/")
    Observable<RestUsers> getNonGroupMembers(@Query("group_id") String groupID,
                                             @Query("user_id") String userID);

    @POST("group/sendInvite/")
    Call<RestGroup> sendUsersGroupInvite(@Body Group group);

    @POST("group/acceptInvite/")
    Call<RestGroup> acceptGroupInvite(@Query("group_id") String groupID,
                                      @Query("user_id") String userID);




}

