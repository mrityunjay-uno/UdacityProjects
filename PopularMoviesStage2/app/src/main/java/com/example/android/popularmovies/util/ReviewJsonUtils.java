package com.example.android.popularmovies.util;

import android.content.Context;

import com.example.android.popularmovies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static com.example.android.popularmovies.util.Constants.*;

public class ReviewJsonUtils {

    public static ArrayList<Review> parseReviewCommentsJson(Context context, String commentsJsonString)
            throws JSONException {


        ArrayList<Review> parsedCommentData = new ArrayList<Review>();

        JSONObject commentsObject = new JSONObject(commentsJsonString);
        JSONArray commentsArray = commentsObject.getJSONArray(JSON_RESULTS);

        for (int i = 0; i < commentsArray.length(); i++) {
            String author;
            String content;

            commentsObject = commentsArray.getJSONObject(i);

            author = commentsObject.getString(AUTHOR);
            content = commentsObject.getString(CONTENT);

            parsedCommentData.add(new Review(author, content));

        }

        return parsedCommentData;
    }
}
