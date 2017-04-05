package com.cit.michael.sportshub.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Event implements Parcelable {

    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("creator_user_id")
    @Expose
    private String creatorUserId;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("event_time")
    @Expose
    private String eventTime;
    @SerializedName("sport_id")
    @Expose
    private Integer sportId;
    @SerializedName("no_space")
    @Expose
    private Integer noSpace;
    @SerializedName("space_left")
    @Expose
    private Integer spaceLeft;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("cost")
    @Expose
    private Double cost;
    @SerializedName("public_game")
    @Expose
    private Integer publicGame;



    @SerializedName("event_name")
    @Expose
    private String eventName;



    @SerializedName("appearance")
    @Expose
    @Nullable
    private int appearance;

    @SerializedName("friends_invited")
    @Expose
    @Nullable
    private ArrayList<String> friends_tokens;

    @SerializedName("location_name")
    @Expose
    @Nullable
    private String location_name;

    /**
     * No args constructor for use in serialization
     */
    public Event() {
    }

    /**
     * @param duration
     * @param spaceLeft
     * @param sportId
     * @param eventDate
     * @param creatorUserId
     * @param eventId
     * @param noSpace
     * @param publicGame
     * @param locationId
     * @param gender
     * @param eventTime
     * @param cost
     * @param eventName
     */
    public Event(Integer eventId, Integer locationId, String creatorUserId, String eventDate, String eventTime, Integer sportId, Integer noSpace, Integer spaceLeft, Integer duration, String gender, Double cost, Integer publicGame, String eventName,ArrayList<String> friends_tokens ) {
        super();
        this.eventId = eventId;
        this.locationId = locationId;
        this.creatorUserId = creatorUserId;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.sportId = sportId;
        this.noSpace = noSpace;
        this.spaceLeft = spaceLeft;
        this.duration = duration;
        this.gender = gender;
        this.cost = cost;
        this.publicGame = publicGame;
        this.eventName = eventName;
        this.friends_tokens = friends_tokens;
    }


    protected Event(Parcel in) {
        eventId = in.readInt();
        locationId =  in.readInt();
        creatorUserId = in.readString();
        eventDate = in.readString();
        eventTime = in.readString();
        sportId = in.readInt();
        noSpace = in.readInt();
        spaceLeft = in.readInt();
        duration = in.readInt();
        gender = in.readString();
        cost = in.readDouble();
        publicGame = in.readInt();
        eventName = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
                Log.d("Event", "Create from parcel : event");
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public Integer getNoSpace() {
        return noSpace;
    }

    public void setNoSpace(Integer noSpace) {
        this.noSpace = noSpace;
    }

    public Integer getSpaceLeft() {
        return spaceLeft;
    }

    public void setSpaceLeft(Integer spaceLeft) {
        this.spaceLeft = spaceLeft;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getPublicGame() {
        return publicGame;
    }

    public void setPublicGame(Integer publicGame) {
        this.publicGame = publicGame;
    }

    public String getEventName() {return eventName;}

    public void setEventName(String eventName) {this.eventName = eventName;}


    @Nullable
    public int getAppearance() {
        return appearance;
    }

    public void setAppearance(@Nullable int appearance) {
        this.appearance = appearance;
    }

    @Nullable
    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(@Nullable String location_name) {
        this.location_name = location_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(eventId);
        dest.writeInt(locationId);
        dest.writeString(creatorUserId);
        dest.writeString(eventDate);
        dest.writeString(eventTime);
        dest.writeInt(sportId);
        dest.writeInt(noSpace);
        dest.writeInt(spaceLeft);
        dest.writeInt(duration);
        dest.writeString(gender);
        dest.writeDouble(cost);
        dest.writeInt(publicGame);
        dest.writeString(eventName);
    }

    public String getFormattedCost(){
        DecimalFormat formatter = new DecimalFormat("€0.00");
        return formatter.format(cost);
    }

    public String getFormattedDate() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(eventDate);
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
            return formattedDate.toString();
    }

    public String getFormattedTime()throws ParseException{
        //Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(eventDate);
        Date date = new SimpleDateFormat("HH:mm").parse(eventTime);
        String formattedDate = new  SimpleDateFormat("HH:mm").format(date);
        return formattedDate.toString();
    }

    public long  getTimeInMilliSeconds() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(eventDate + " " + eventTime);
        return date.getTime();
    }

    public CharSequence converteTimestamp() throws ParseException {
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(String.valueOf(getTimeInMilliSeconds())), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

}