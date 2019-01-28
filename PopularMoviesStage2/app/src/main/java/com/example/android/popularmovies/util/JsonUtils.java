package com.example.android.popularmovies.util;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.android.popularmovies.util.Constants.*;

public class JsonUtils {

    public static ArrayList<Movie> parseMovieJson(Context context, String moviesJsonString)
            throws JSONException {

        ArrayList<Movie> parsedMovieData = new ArrayList<Movie>();

        JSONObject moviesObject = new JSONObject(moviesJsonString);
        JSONArray moviesArray = moviesObject.getJSONArray(JSON_RESULTS);

        for (int i = 0; i < moviesArray.length(); i++) {
            double popularity;
            String posterUrl;
            String title;
            String releaseDate;
            double rating;
            String synopsys;
            int id;

            moviesObject = moviesArray.getJSONObject(i);

            popularity = moviesObject.getDouble(POPULARITY);
            posterUrl = moviesObject.getString(JSON_IMAGE);
            title = moviesObject.getString(JSON_TITLE);
            releaseDate = moviesObject.getString(JSON_RELEASE_DATE);
            rating = moviesObject.getDouble(JSON_RATING);
            synopsys = moviesObject.getString(JSON_SYNOPSIS);
            id = moviesObject.getInt(JSON_ID);

            parsedMovieData.add(new Movie(popularity, id, title, rating, synopsys, posterUrl, releaseDate));

        }

        return parsedMovieData;
    }
}
