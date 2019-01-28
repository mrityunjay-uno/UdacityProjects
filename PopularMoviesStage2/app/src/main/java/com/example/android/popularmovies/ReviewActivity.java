package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.util.NetworkUtils;
import com.example.android.popularmovies.util.ReviewJsonUtils;
import com.example.android.popularmovies.util.ReviewNetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends AppCompatActivity {

    ReviewAdapter mAdapter;
    RecyclerView mCommentsList;
    int MovieId;
    private static final ArrayList<Review> NUM_LIST_REVIEWS = null;

    @BindView(R.id.ivPoster)
    ImageView mMoviePoster;
    @BindView(R.id.tvMovieName) TextView mMovieName;
    @BindView(R.id.tvMovieDate) TextView mMovieDate;
    @BindView(R.id.tvVoteAverage) TextView mVoteAverage;
    @BindView(R.id.tvMovieOverview) TextView mMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ButterKnife.bind(this);

        Movie movieObject = getIntent().getParcelableExtra("movie");

        if (movieObject != null) {

            MovieId = movieObject.getMovieID();

            Picasso.with(this)
                    .load(movieObject.getFullPosterPath())
                    .resize(700,1000)
                    .into(mMoviePoster);
            mMovieName.setText(String.valueOf(movieObject.getMovieTitle()));
            mMovieDate.setText(String.valueOf(movieObject.getReleaseDate()));
            mVoteAverage.setText(String.valueOf(movieObject.getMovieRating()));
            mMovieOverview.setText(String.valueOf(movieObject.getMoviSynopsis()));
        }

        mCommentsList = (RecyclerView) findViewById(R.id.rvComments);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mCommentsList.setLayoutManager(LayoutManager);

        mCommentsList.setHasFixedSize(true);

        mAdapter = new ReviewAdapter(NUM_LIST_REVIEWS);

        mCommentsList.setAdapter(mAdapter);

        loadCommentsData(String.valueOf(MovieId));
    }

    private void loadCommentsData(String movieId) {
        showCommentDataView();
        new ReviewActivity.FetchCommentsTask().execute(movieId);
    }

    private void showCommentDataView() {
        mCommentsList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mCommentsList.setVisibility(View.INVISIBLE);
    }

    public class FetchCommentsTask extends AsyncTask<String, Void, ArrayList<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String movieId = params[0];
            URL commentsRequestUrl = ReviewNetworkUtils.buildUrl(movieId);

            try {
                String jsonCommentResponse = NetworkUtils.getResponseFromHttpUrl(commentsRequestUrl);

                ArrayList<Review> simpleJsonCommentData = ReviewJsonUtils.parseReviewCommentsJson(ReviewActivity.this, jsonCommentResponse);

                return simpleJsonCommentData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> commentData) {
            if (commentData != null) {
                showCommentDataView();
                mAdapter.setCommentData(commentData);
            } else {
                showErrorMessage();
            }
        }
    }

}
