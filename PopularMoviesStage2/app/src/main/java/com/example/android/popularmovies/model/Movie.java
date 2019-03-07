package com.example.android.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import static com.example.android.popularmovies.util.Constants.BASE_ORIGINAL_IMAGE_URL;

@Entity
public class Movie implements Parcelable {

    @PrimaryKey
    private int movieID;
    private String movieTitle;
    private String movieImage;
    private String moviSynopsis;
    private double movieRating;
    private String releaseDate;
    private double popularity;


    public Movie() {
    }

    @Ignore
    public Movie(double popularity, int movieID, String movieTitle, double movieRating, String moviSynopsis, String movieImage, String releaseDate) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieImage = movieImage;
        this.moviSynopsis = moviSynopsis;
        this.movieRating = movieRating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }

    @Ignore
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

    @Ignore
    private Movie(Parcel in) {
        this.popularity   = in.readDouble();
        this.movieImage   = in.readString();
        this.movieTitle   = in.readString();
        this.releaseDate  = in.readString();
        this.movieRating  = in.readDouble();
        this.moviSynopsis = in.readString();
        this.movieID      = in.readInt();
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
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

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(double movieRating) {
        this.movieRating = movieRating;
    }

    public int getMovieID() { return movieID;  }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getMovieImage() { return movieImage; }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieTitle() { return movieTitle; }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviSynopsis() { return moviSynopsis; }

    public void setMoviSynopsis(String moviSynopsis) {
        this.moviSynopsis = moviSynopsis;
    }

    public String getReleaseDate() { return releaseDate; }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Ignore
    public Uri getFullPosterPath() {
        return Uri.parse(BASE_ORIGINAL_IMAGE_URL)
                .buildUpon()
                .appendEncodedPath(getMovieImage())
                .build();
    }
}

