package com.example.android.popularmovies.util;

import android.content.Context;

import com.example.android.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static com.example.android.popularmovies.util.Constants.*;

public class TrailerJsonUtils {

    public static ArrayList<Trailer> parseTrailerJson(Context context, String videosJsonString)
            throws JSONException {

        ArrayList<Trailer> parsedVideoData = new ArrayList<Trailer>();

        JSONObject videosObject = new JSONObject(videosJsonString);
        JSONArray videosArray = videosObject.getJSONArray(JSON_RESULTS);

        for (int i = 0; i < videosArray.length(); i++) {
            String id;
            String name;
            String key;

            videosObject = videosArray.getJSONObject(i);

            id = videosObject.getString(ID);
            name = videosObject.getString(NAME);
            key = videosObject.getString(KEY);

            parsedVideoData.add(new Trailer(id, name, key));

        }

        return parsedVideoData;
    }
}
