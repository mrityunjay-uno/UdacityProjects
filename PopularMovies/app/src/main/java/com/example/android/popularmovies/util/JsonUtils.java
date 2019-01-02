package com.example.android.popularmovies.util;

import android.util.Log;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.android.popularmovies.util.Constants.*;

public class JsonUtils {

    public static ArrayList<Movie> parseMovieJson(String json) {

        ArrayList<Movie> movieList = new ArrayList<Movie>();

        try {
            JSONObject jsonMovie = new JSONObject(json);

            if (jsonMovie != null) {
                JSONArray results = jsonMovie.getJSONArray(JSON_RESULTS);
                if (results != null) {
                    for (int i=0;i<results.length();i++){
                        JSONObject movieObject = results.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setMovieID(movieObject.getInt(JSON_ID));
                        movie.setMovieImage(movieObject.getString(JSON_IMAGE));
                        movie.setMovieRating(movieObject.getInt(JSON_RATING));
                        movie.setMovieTitle(movieObject.getString(JSON_TITLE));
                        movie.setMoviSynopsis(movieObject.getString(JSON_SYNOPSIS));
                        movie.setReleaseDate(movieObject.getString(JSON_RELEASE_DATE));

                        movieList.add(movie);
                    }
                }
            }

        } catch (Exception e) {
            Log.e("JsonUtils","JSON PARSING EXCEPTION");
        }


        return movieList;
    }
}
