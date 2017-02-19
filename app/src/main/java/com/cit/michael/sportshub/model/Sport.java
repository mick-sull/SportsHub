
package com.cit.michael.sportshub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sport {

    @SerializedName("sport_id")
    @Expose
    private Integer sportId;
    @SerializedName("sport_name")
    @Expose
    private String sportName;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public Sport(Integer sportId,String sportName,String userId ){
        this.sportId = sportId;
        this.sportName = sportName;
        this.userId = userId;

    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}