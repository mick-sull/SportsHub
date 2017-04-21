package com.cit.michael.sportshub.rest.model;

/**
 * Created by micha on 20/04/2017.
 */

import com.cit.michael.sportshub.model.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestReview {

    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Review")
    @Expose
    private List<Review> review = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }

}