package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String id;
    private String name;
    private String key;

    public Trailer(String id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(name);
        out.writeString(key);
    }

    private Trailer(Parcel in) {
        this.id                 = in.readString();
        this.name                 = in.readString();
        this.key                 = in.readString();
    }

    public Trailer() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }
        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
