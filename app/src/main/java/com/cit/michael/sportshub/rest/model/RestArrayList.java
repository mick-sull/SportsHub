package com.cit.michael.sportshub.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by micha on 03/04/2017.
 */

public class RestArrayList {
    @SerializedName("item")
    @Expose
    List<String> item;

    public RestArrayList(List<String> item) {
        this.item = item;
    }

/*    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }*/
}
