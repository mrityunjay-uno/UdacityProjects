package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String user;
    private String comment;

    public Review(String user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(user);
        out.writeString(comment);
    }

    private Review(Parcel in) {
        this.user                 = in.readString();
        this.comment                 = in.readString();
    }

    public Review() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }
        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };

    public String getAuthor() {
        return user;
    }

    public String getContent() {
        return comment;
    }
}
