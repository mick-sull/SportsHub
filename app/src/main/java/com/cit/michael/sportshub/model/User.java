package com.cit.michael.sportshub.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 05/01/2017.
 */


public class User {



        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("userFullName")
        @Expose
        private String userFullName;
        @SerializedName("userProfileUrl")
        @Expose
        private String userProfileUrl;
        @SerializedName("attendances")
        @Expose
        private Integer attendances;
        @SerializedName("failed_attendances")
        @Expose
        private Integer failedAttendances;
        @SerializedName("organized")
        @Expose
        private Integer organized;


        @SerializedName("userToken")
        @Expose
        private String userToken;


        @SerializedName("status")
        @Expose
        @Nullable
        private int status;

        /**
         * No args constructor for use in serialization
         *
         */

        /**
         *
         * @param attendances
         * @param userId
         * @param userFullName
         * @param userProfileUrl
         * @param failedAttendances
         */
        public User(String userId, String userFullName, String userProfileUrl, Integer attendances, Integer failedAttendances, String userToken) {
            super();
            this.userId = userId;
            this.userFullName = userFullName;
            this.userProfileUrl = userProfileUrl;
            this.attendances = attendances;
            this.failedAttendances = failedAttendances;
            this.userToken = userToken;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserFullName() {
            return userFullName;
        }

        public void setUserFullName(String userFullName) {
            this.userFullName = userFullName;
        }

        public String getUserProfileUrl() {
            return userProfileUrl;
        }

        public void setUserProfileUrl(String userProfileUrl) {
            this.userProfileUrl = userProfileUrl;
        }

        public Integer getAttendances() {
            return attendances;
        }

        public void setAttendances(Integer attendances) {
            this.attendances = attendances;
        }

        public Integer getFailedAttendances() {
            return failedAttendances;
        }

        public void setFailedAttendances(Integer failedAttendances) {
            this.failedAttendances = failedAttendances;
        }

        public Integer getOrganized() {
            return organized;
        }

        public void setOrganized(Integer organized) {
            this.organized = organized;
        }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }


    @Nullable
    public int getStatus() {
        return status;
    }

    public void setStatus(@Nullable int status) {
        this.status = status;
    }


}