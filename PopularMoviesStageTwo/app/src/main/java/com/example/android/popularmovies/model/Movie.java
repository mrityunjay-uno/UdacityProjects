package com.example.android.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


import java.io.Serializable;

import static com.example.android.popularmovies.util.Constants.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Movie implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private  int id;
    private  String title;
    private  String original_title;
    private String poster_path;
    private double popularity;
    private double vote_average;
    private String release_date;
    private String overview;
    private Boolean isFavourite = false;

    @Ignore
    public  Movie()
    {

    }

    public Movie(int id, String title, String original_title, String poster_path, double popularity, double vote_average, String release_date, String overview) {
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.overview = overview;
        this.isFavourite = true;
    }

    public  void setID(int id) { this.id = id; }
    public  void setTitle(String title) { this.title = title; }
    public  void setPopularity(double popularity) { this.popularity = popularity; }
    public void setOriginal_title(String original_title) { this.original_title = original_title; }
    public void setPoster_path(String poster_path) { this.poster_path = poster_path; }
    public void setVote_average(double vote_average) { this.vote_average = vote_average; }
    public void setRelease_date(String release_date) { this.release_date = release_date; }
    public void setOverview(String overview) { this.overview = overview; }
    public void setFavourite(Boolean favourite) { isFavourite = favourite; }

    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getOriginal_title() { return this.original_title; }
    public String getPoster_path() { return this.poster_path; }
    public double getPopularity() { return this.popularity; }
    public double getVote_average() { return this.vote_average; }
    public String getRelease_date() { return this.release_date; }
    public String getOverview() { return this.overview; }
    public Boolean getFavourite() { return isFavourite; }
}

