
package com.example.android.popularmovies.util;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String PARAM_QUERY = "api_key";

    public static URL buildUrl(String movieSearchQuery,int catergoryID) {

        Uri builtUri;

        if(catergoryID == 0) {
            builtUri = Uri.parse(Constants.POPULAR_BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, movieSearchQuery)
                    .build();
        }
        else {
            builtUri = Uri.parse(Constants.POPULAR_TOPRATED_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, movieSearchQuery)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String buildPosterUrl(String baseURL, String size, String poster_path) {

        String builtURL = baseURL + size + poster_path;
        return builtURL;
    }
    public static URL buildTrailerUrl(String trailerSearchQuery, String id) {

        Uri builtUri;

        String trailerURL = Constants.TRAILER_BASE_URL + id + "/videos";

        builtUri = Uri.parse(trailerURL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, trailerSearchQuery)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewUrl(String reviewSearchQuery, String id) {

        Uri builtUri;

        String trailerURL = Constants.REVIEWS_BASE_URL + id + "/reviews";

        builtUri = Uri.parse(trailerURL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, reviewSearchQuery)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String buildYoutubePosterUrl(String baseURL, String size, String id) {

        String builtURL = baseURL + id  + "/" + size + ".jpg";
        return builtURL;
    }

    public static String buildVideoUrl(String baseURL, String id) {

        String builtURL = baseURL + id;
        return builtURL;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}