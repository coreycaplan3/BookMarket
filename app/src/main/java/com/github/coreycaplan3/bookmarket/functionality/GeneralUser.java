package com.github.coreycaplan3.bookmarket.functionality;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class GeneralUser implements Parcelable {

    private String displayName;
    private String email;
    private String userId;
    private String university;

    public GeneralUser(String displayName, String email, String userId, String university) {
        this.displayName = displayName;
        this.email = email;
        this.userId = userId;
        this.university = university;
    }

    protected GeneralUser(Parcel in) {
        displayName = in.readString();
        email = in.readString();
        userId = in.readString();
        university = in.readString();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getUniversity() {
        return university;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(email);
        dest.writeString(userId);
        dest.writeString(university);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GeneralUser> CREATOR = new Parcelable.Creator<GeneralUser>() {
        @Override
        public GeneralUser createFromParcel(Parcel in) {
            return new GeneralUser(in);
        }

        @Override
        public GeneralUser[] newArray(int size) {
            return new GeneralUser[size];
        }
    };
}
