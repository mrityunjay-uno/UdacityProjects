package com.example.mrityunjay.bakingapplication.data_model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Steps implements Parcelable {

    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Steps (String shortDescription){
        this.shortDescription = shortDescription;
        this.description = shortDescription;
        this.videoURL = null;
        this.thumbnailURL = null;
    }

    protected Steps(Parcel in) {
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        if (TextUtils.isEmpty(videoURL)) return null;
        return videoURL.endsWith(".mp4") ? videoURL : null;
    }

    public String getThumbnailURL() {
        return  (thumbnailFormatValid(thumbnailURL)) ? thumbnailURL : null;
    }

    @Override
    public String toString() {
        return "Step{" +
                "shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

    private boolean thumbnailFormatValid(String thumbnailURL){
        return !TextUtils.isEmpty(thumbnailURL) && !thumbnailURL.endsWith(".mp4");
    }
}
