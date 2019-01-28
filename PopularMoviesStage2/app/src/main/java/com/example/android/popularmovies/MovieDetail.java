package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.data.Contract;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.util.ReviewJsonUtils;
import com.example.android.popularmovies.util.ReviewNetworkUtils;
import com.example.android.popularmovies.util.TrailerJsonUtils;
import com.example.android.popularmovies.util.TrailerNetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies.util.Constants.*;

public class MovieDetail extends AppCompatActivity implements
        TrailerAdapter.TrailerAdapterOnClickHandler {

    TrailerAdapter mAdapterTrailer;
    ReviewAdapter mAdapterReview;

    int _MovieId;
    Double _MovieVoteAverage;
    String _MoviePoster;
    String _MovieName;
    String _MovieOverview;
    String _MovieDate;

    private static final ArrayList<Review> NUM_LIST_REVIEWS = null;

    @BindView(R.id.ivMoviePoster)
    ImageView mMoviePoster;
    @BindView(R.id.tvMovieTitle)
    TextView mMovieName;
    @BindView(R.id.tvReleaseDate)
    TextView mMovieDate;
    @BindView(R.id.tvRating)
    TextView mVoteAverage;
    @BindView(R.id.tvSynopsis)
    TextView mMovieOverview;

    @BindView(R.id.rvVideos)
    RecyclerView mVideosList;
    @BindView(R.id.rvReviews)
    RecyclerView mReviewsList;
    @BindView(R.id.tvTrailers)
    TextView mErrorTrailers;
    @BindView(R.id.tvReviews)
    TextView mErrorReviews;

    @BindView(R.id.ibfavorite)
    ImageButton mFavorite;

    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        Movie movieObject = getIntent().getParcelableExtra("movie");

        if (movieObject != null) {

            _MovieId = movieObject.getMovieID();
            _MovieVoteAverage = movieObject.getMovieRating();
            _MoviePoster = movieObject.getMovieImage();
            _MovieName = movieObject.getMovieTitle();
            _MovieOverview = movieObject.getMoviSynopsis();
            _MovieDate = movieObject.getReleaseDate();

            Picasso.with(this)
                    .load(movieObject.getFullPosterPath()).placeholder(R.drawable.defaultimage)
                    .resize(700, 1000)
                    .into(mMoviePoster);
            mMovieName.setText(String.valueOf(_MovieName));
            mMovieDate.setText(String.valueOf(_MovieDate));
            mVoteAverage.setText(String.valueOf(_MovieVoteAverage));
            mMovieOverview.setText(String.valueOf(_MovieOverview));
        }

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mVideosList.setLayoutManager(LayoutManager);
        mVideosList.setHasFixedSize(true);
        mAdapterTrailer = new TrailerAdapter(this);
        mVideosList.setAdapter(mAdapterTrailer);

        LinearLayoutManager LayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsList.setLayoutManager(LayoutManager2);
        mReviewsList.setHasFixedSize(true);
        mAdapterReview = new ReviewAdapter(NUM_LIST_REVIEWS);
        mReviewsList.setAdapter(mAdapterReview);

        loadVideosData(String.valueOf(_MovieId));
        loadReviewsData(String.valueOf(_MovieId));

        final String[] FAVORITE_MOVIES_PROJECTION = {
                Contract.MovieColumn.COLUMN_MOVIES_ID
        };

        Uri uri = Contract.MovieColumn.buildMoviesUriWithId(_MovieId);

        Cursor fmovie = getContentResolver().query(uri,
                FAVORITE_MOVIES_PROJECTION,
                null,
                null,
                null);

        if (fmovie.getCount() != 0) {
            mFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
        } else {
            mFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listStateVideo = mVideosList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listStateVideo);
        Parcelable listStateReview = mReviewsList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listStateReview);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listStateVideo = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mVideosList.getLayoutManager().onRestoreInstanceState(listStateVideo);
            Parcelable listStateReview = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mReviewsList.getLayoutManager().onRestoreInstanceState(listStateReview);
        }
    }

    private void loadVideosData(String movieId) {
        showVideoDataView();
        new FetchVideosTask().execute(movieId);
    }

    private void loadReviewsData(String movieId) {
        showReviewDataView();
        new FetchReviewsTask().execute(movieId);
    }

    public void onClickFavorite(View view) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.MovieColumn.COLUMN_MOVIES_ID, _MovieId);
        contentValues.put(Contract.MovieColumn.COLUMN_RATING, _MovieVoteAverage);
        contentValues.put(Contract.MovieColumn.COLUMN_POSTER, _MoviePoster);
        contentValues.put(Contract.MovieColumn.COLUMN_TITLE, _MovieName);
        contentValues.put(Contract.MovieColumn.COLUMN_SYNOPSIS, _MovieOverview);
        contentValues.put(Contract.MovieColumn.COLUMN_RELEASE_DATE, _MovieDate);

        final String[] FAVORITE_MOVIES_DETAIL_PROJECTION = {
                Contract.MovieColumn.COLUMN_MOVIES_ID,
                Contract.MovieColumn.COLUMN_RATING,
                Contract.MovieColumn.COLUMN_POSTER,
                Contract.MovieColumn.COLUMN_TITLE,
                Contract.MovieColumn.COLUMN_SYNOPSIS,
                Contract.MovieColumn.COLUMN_RELEASE_DATE
        };

        Uri truuri = Contract.MovieColumn.buildMoviesUriWithId(_MovieId);

        Cursor data = getContentResolver().query(truuri,
                FAVORITE_MOVIES_DETAIL_PROJECTION,
                null,
                null,
                null);

        if (data.getCount() == 0) {

            Uri uri = getContentResolver().insert(Contract.MovieColumn.CONTENT_URI, contentValues);
            String text = "Movie made Favorite";
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
            mFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));

        } else {
            String text = "Moview is already Favorite";
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(Trailer video) {

    }

    private void showVideoDataView() {
        mVideosList.setVisibility(View.VISIBLE);
    }

    private void showReviewDataView() {
        mReviewsList.setVisibility(View.VISIBLE);
    }

    public class FetchVideosTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String movieId = params[0];
            URL videosRequestUrl = TrailerNetworkUtils.buildUrl(movieId);

            try {
                String jsonVideoResponse = TrailerNetworkUtils.getResponseFromHttpUrl(videosRequestUrl);

                ArrayList<Trailer> simpleJsonVideoData = TrailerJsonUtils.parseTrailerJson(MovieDetail.this, jsonVideoResponse);

                return simpleJsonVideoData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> videoData) {
            if (videoData != null) {
                showVideoDataView();
                mAdapterTrailer.setVideoData(videoData);
            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Review>> {

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
            URL reviewsRequestUrl = ReviewNetworkUtils.buildUrl(movieId);

            try {
                String jsonReviewResponse = ReviewNetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);

                ArrayList<Review> simpleJsonReviewData = ReviewJsonUtils.parseReviewCommentsJson(MovieDetail.this, jsonReviewResponse);

                return simpleJsonReviewData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviewData) {
            if (reviewData != null) {
                showReviewDataView();
                mAdapterReview.setCommentData(reviewData);
            }
        }
    }
}
