package com.example.android.popularmovies.model;

public class Movie {
    private int movieID;
    private String movieTitle;
    private String movieImage;
    private String moviSynopsis;
    private int movieRating;
    private String releaseDate;


    public Movie() {
    }

    public Movie(int movieID, String movieTitle, int movieRating, String moviSynopsis, String movieImage, String releaseDate) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.movieImage = movieImage;
        this.moviSynopsis = moviSynopsis;
        this.movieRating = movieRating;
        this.releaseDate = releaseDate;
    }

    public int getMovieRating() {
        return movieRating;
    }

    public void setMovieID(int movieID) { this.movieID = movieID; }

    public int getMovieID() { return movieID;  }

    public String getMovieImage() { return movieImage; }

    public String getMovieTitle() { return movieTitle; }

    public String getMoviSynopsis() { return moviSynopsis; }

    public String getReleaseDate() { return releaseDate; }

    public void setMovieImage(String movieImage) {  this.movieImage = movieImage; }

    public void setMovieRating(int movieRating) { this.movieRating = movieRating; }

    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public void setMoviSynopsis(String moviSynopsis) { this.moviSynopsis = moviSynopsis; }

    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}

