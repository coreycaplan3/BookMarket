package com.github.coreycaplan3.bookmarket.functionality;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class UserProfile implements Parcelable {

    private String displayName;
    private String email;
    private String connectionToken;
    private String userId;
    private String university;

    public UserProfile(String displayName, String email, String connectionToken, String userId,
                       String university) {
        this.displayName = displayName;
        this.email = email;
        this.connectionToken = connectionToken;
        this.userId = userId;
        this.university = university;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getConnectionToken() {
        return connectionToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getUniversity() {
        return university;
    }

    protected UserProfile(Parcel in) {
        displayName = in.readString();
        email = in.readString();
        connectionToken = in.readString();
        userId = in.readString();
        university = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(email);
        dest.writeString(connectionToken);
        dest.writeString(userId);
        dest.writeString(university);
    }

}
