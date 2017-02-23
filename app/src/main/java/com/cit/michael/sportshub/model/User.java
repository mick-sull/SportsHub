package com.cit.michael.sportshub.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by micha on 05/01/2017.
 */


public class User implements Parcelable {



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



    protected User(Parcel in) {
        userId = in.readString();
        userFullName = in.readString();
        userProfileUrl = in.readString();
        attendances = in.readByte() == 0x00 ? null : in.readInt();
        failedAttendances = in.readByte() == 0x00 ? null : in.readInt();
        organized = in.readByte() == 0x00 ? null : in.readInt();
        userToken = in.readString();
        status = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userFullName);
        dest.writeString(userProfileUrl);
        if (attendances == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(attendances);
        }
        if (failedAttendances == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(failedAttendances);
        }
        if (organized == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(organized);
        }
        dest.writeString(userToken);
        dest.writeInt(status);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}