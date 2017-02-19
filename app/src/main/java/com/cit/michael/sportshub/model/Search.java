package com.cit.michael.sportshub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 19/01/2017.
 */
public class Search {


    @SerializedName("location_id")
    @Expose
    private Integer locationId;

    @SerializedName("search_date")
    @Expose
    private String searchDate;

    @SerializedName("time_min")
    @Expose
    private String eventTimeRangeMin;

    @SerializedName("time_max")
    @Expose
    private String eventTimeRangeMax;

    @SerializedName("sport_id")
    @Expose
    private Integer sportId;

    public Search(Integer locationId, String searchDate, String eventTimeRangeMin,String eventTimeRangeMax, Integer sportId ) {
        this.locationId = locationId;
        this.searchDate = searchDate;
        this.eventTimeRangeMin = eventTimeRangeMin;
        this.sportId = sportId;
        this.eventTimeRangeMax = eventTimeRangeMax;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String getEventTimeRangeMin() {
        return eventTimeRangeMin;
    }

    public void setEventTimeRangeMin(String eventTimeRangeMin) {
        this.eventTimeRangeMin = eventTimeRangeMin;
    }

    public String getEventTimeRangeMax() {
        return eventTimeRangeMax;
    }

    public void setEventTimeRangeMax(String eventTimeRangeMax) {
        this.eventTimeRangeMax = eventTimeRangeMax;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }
}
