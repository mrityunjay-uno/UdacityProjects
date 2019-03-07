package com.example.android.popularmovies.util;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static List<Movie> parseMovieJson(String json)
    {

        try {
            JSONObject movieListJSON = new JSONObject(json);
            JSONArray moviesListArray = movieListJSON.getJSONArray("results");

            List<Movie> movies = new ArrayList<Movie>();


            for (int i = 0; i < moviesListArray.length(); i++) {

                JSONObject movieJSON = moviesListArray.getJSONObject(i);

                Movie movie = new Movie();

                movie.setID(Integer.parseInt(movieJSON.getString("id")));
                movie.setTitle(movieJSON.getString("title"));
                movie.setOriginal_title(movieJSON.getString("original_title"));
                movie.setPopularity(movieJSON.getDouble("popularity"));
                movie.setPoster_path(movieJSON.getString("poster_path"));
                movie.setVote_average(movieJSON.getDouble("vote_average"));
                movie.setRelease_date(movieJSON.getString("release_date"));
                movie.setOverview(movieJSON.getString("overview"));

                movies.add(movie);
            }





            return movies;
        }
        catch (JSONException e){

        }

        return null;

    }

    public static List<Trailer> parseTrailerJson(String json)
    {

        try {
            JSONObject trailerListJSON = new JSONObject(json);
            JSONArray trailerListArray = trailerListJSON.getJSONArray("results");

            List<Trailer> trailers = new ArrayList<Trailer>();


            for (int i = 0; i < trailerListArray.length(); i++) {

                JSONObject trailerJSON = trailerListArray.getJSONObject(i);

                Trailer trailer = new Trailer();

                trailer.setName(trailerJSON.getString("name"));
                trailer.setID(trailerJSON.getString("id"));
                trailer.setKey(trailerJSON.getString("key"));
                trailer.setType(trailerJSON.getString("type"));


                trailers.add(trailer);
            }

            return trailers;
        }
        catch (JSONException e){

        }

        return null;
    }

    public static List<Review> parseReviewJson(String json)
    {

        try {
            JSONObject reviewListJSON = new JSONObject(json);
            JSONArray reviewListArray = reviewListJSON.getJSONArray("results");

            List<Review> reviews = new ArrayList<Review>();


            for (int i = 0; i < reviewListArray.length(); i++) {

                JSONObject trailerJSON = reviewListArray.getJSONObject(i);

                Review review = new Review();

                review.setAuthor(trailerJSON.getString("author"));
                review.setContent(trailerJSON.getString("content"));


                reviews.add(review);
            }

            return reviews;
        }
        catch (JSONException e){

        }

        return null;
    }
}
