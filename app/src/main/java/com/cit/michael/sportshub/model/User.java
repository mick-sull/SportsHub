package com.cit.michael.sportshub.model;

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
        public User(String userId, String userFullName, String userProfileUrl, Integer attendances, Integer failedAttendances) {
            super();
            this.userId = userId;
            this.userFullName = userFullName;
            this.userProfileUrl = userProfileUrl;
            this.attendances = attendances;
            this.failedAttendances = failedAttendances;
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
/*

    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;


    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
*/

/*    public void saveUser() {
        //Add YOUR Firebase Reference URL instead of the following URL
        Firebase myFirebaseRef = new Firebase("https://androidbashfirebase.firebaseio.com/");
        myFirebaseRef = myFirebaseRef.child("users").child(getId());
        myFirebaseRef.setValue(this);
    }*/
}