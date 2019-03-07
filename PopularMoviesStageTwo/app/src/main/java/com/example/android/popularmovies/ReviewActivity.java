package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.util.Constants;
import com.example.android.popularmovies.util.JsonUtils;
import com.example.android.popularmovies.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class ReviewActivity extends AppCompatActivity {

    public List<Review> mReviewList;
    private ReviewAdapter mAdapter;
    private RecyclerView mReviewsListView;
    private Movie movie;

    public static final String MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        movie = (Movie)getIntent().getSerializableExtra(MOVIE);

        mReviewsListView = (RecyclerView) findViewById(R.id.rv_reviews);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReviewsListView.setLayoutManager(linearLayoutManager);

        mAdapter = new ReviewAdapter(mReviewList);
        mReviewsListView.setAdapter(mAdapter);

        String apiKey = Constants.API_KEY;
        final String reviewParamQuery = apiKey;

        URL reviewListURL = NetworkUtils.buildReviewUrl(reviewParamQuery, String.valueOf(movie.getId()));
        new JsonQueryTask().execute(reviewListURL);
    }

    public class JsonQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL reviewListURL = params[0];
            String reviewListResults = null;
            try {
                reviewListResults = NetworkUtils.getResponseFromHttpUrl(reviewListURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reviewListResults;
        }

        @Override
        protected void onPostExecute(String movieListResults) {
            if (movieListResults != null && !movieListResults.equals("")) {

                Log.i("postExecute", "Post execute started");

                mReviewList = JsonUtils.parseReviewJson(movieListResults);
                mAdapter.mReviewList = mReviewList;

                mAdapter.notifyDataSetChanged();
            }
        }



    }

}
