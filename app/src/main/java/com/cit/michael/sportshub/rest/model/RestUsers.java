package com.cit.michael.sportshub.rest.model;

import com.cit.michael.sportshub.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by micha on 07/02/2017.
 */

public class RestUsers {

        @SerializedName("Error")
        @Expose
        private Boolean error;
        @SerializedName("Message")
        @Expose
        private String message;
        @SerializedName("User")
        @Expose
        private List<User> user = null;

        /**
         * @param message
         * @param error
         * @param user
         */
        public RestUsers(Boolean error, String message, List<User> user) {
            super();
            this.error = error;
            this.message = message;
            this.user = user;
        }

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

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }
    }
