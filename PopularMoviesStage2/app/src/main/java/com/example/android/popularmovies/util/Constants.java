package com.example.android.popularmovies.util;

import android.os.Bundle;

public class Constants {

    public static final String API_KEY = "2037741ba1a83d5750f583dff4b8ccac";

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w342";
    public static final String BASE_ORIGINAL_IMAGE_URL = "https://image.tmdb.org/t/p/w342";
    public static final String COMMENTS_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String TRAILERS_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public final static String PARAM_QUERY = "api_key";

    public static final String POPULAR_MOVIE = "popular";
    public static final String TOP_RATED_MOVIE = "top_rated";
    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    public static final String YOUTUBE_INTENT = "vnd.youtube";

    public static final String JSON_RESULTS = "results";
    public static final String JSON_ID = "id";
    public static final String JSON_IMAGE = "poster_path";
    public static final String JSON_RATING = "vote_average";
    public static final String JSON_TITLE = "title";
    public static final String JSON_SYNOPSIS = "overview";
    public static final String JSON_RELEASE_DATE = "release_date";
    public static final String POPULARITY = "popularity";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String KEY = "key";
    public static final String VIDEOS_TRAILER  = "videos";
    public static final String COMMENTS_REVIEW  = "reviews";


    public static final int INDEX_MOVIES_ID = 0;
    public static final int INDEX_VOTE_AVERAGE = 1;
    public static final int INDEX_POSTER_PATH = 2;
    public static final int INDEX_ORIGINAL_TITLE = 3;
    public static final int INDEX_OVERVIEW = 4;
    public static final int INDEX_RELEASE_DATE = 5;

    public static final int ID_DETAIL_LOADER = 999;

    public static final int MOVIES_LOADER_ID = 0;
    public static final String SORT_ORDER_EXTRA = "";

    public static final String KEY_RECYCLER_STATE = "recycler_state";


    //DB constants
    public static final String TABLE_NAME = "movies";

    public static final String COLUMN_MOVIES_ID = "movies_id";


    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_WITH_ID = 101;

}
