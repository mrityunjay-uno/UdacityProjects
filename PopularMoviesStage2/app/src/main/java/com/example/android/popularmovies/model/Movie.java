package com.example.android.popularmovies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import static com.example.android.popularmovies.util.Constants.BASE_ORIGINAL_IMAGE_URL;

public class Movie implements Parcelable {
    private int movieID;
    private String movieTitle;
    private String movieImage;
    private String moviSynopsis;
    private double movieRating;
    private String releaseDate;
    private double popularity;


    public Movie() {
    }

    public Movie(double popularity, int movieID, String movieTitle, double movieRating, String moviSynopsis, String movieImage, String releaseDate) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieImage = movieImage;
        this.moviSynopsis = moviSynopsis;
        this.movieRating = movieRating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(popularity);
        out.writeString(movieImage);
        out.writeString(movieTitle);
        out.writeString(releaseDate);
        out.writeDouble(movieRating);
        out.writeString(moviSynopsis);
        out.writeInt(movieID);
    }

    private Movie(Parcel in) {
        this.popularity   = in.readDouble();
        this.movieImage   = in.readString();
        this.movieTitle   = in.readString();
        this.releaseDate  = in.readString();
        this.movieRating  = in.readDouble();
        this.moviSynopsis = in.readString();
        this.movieID      = in.readInt();
    }

    public void Movie() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    public double getPopularity() {
        return popularity;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public int getMovieID() { return movieID;  }

    public String getMovieImage() { return movieImage; }

    public String getMovieTitle() { return movieTitle; }

    public String getMoviSynopsis() { return moviSynopsis; }

    public String getReleaseDate() { return releaseDate; }



    public Uri getFullPosterPath() {
        return Uri.parse(BASE_ORIGINAL_IMAGE_URL)
                .buildUpon()
                .appendEncodedPath(getMovieImage())
                .build();
    }
}

