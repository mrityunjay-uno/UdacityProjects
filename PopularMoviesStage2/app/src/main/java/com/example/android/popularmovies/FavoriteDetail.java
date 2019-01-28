package com.example.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.data.Contract;
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

public class FavoriteDetail extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, TrailerAdapter.TrailerAdapterOnClickHandler {

    TrailerAdapter mAdapterTrailer;
    ReviewAdapter mAdapterReview;

    public static final String[] FAVORITE_MOVIES_DETAIL_PROJECTION = {
            Contract.MovieColumn.COLUMN_MOVIES_ID,
            Contract.MovieColumn.COLUMN_RATING,
            Contract.MovieColumn.COLUMN_POSTER,
            Contract.MovieColumn.COLUMN_TITLE,
            Contract.MovieColumn.COLUMN_SYNOPSIS,
            Contract.MovieColumn.COLUMN_RELEASE_DATE
    };

    private Uri mUri;
    private static final ArrayList<Review> NUM_LIST_REVIEWS = null;

    @BindView(R.id.tvRating)
    TextView mMovieRating;
    @BindView(R.id.ivMoviePoster)
    ImageView mMoviePoster;
    @BindView(R.id.tvMovieTitle)
    TextView mMovieTitle;
    @BindView(R.id.tvSynopsis)
    TextView mSynopsis;
    @BindView(R.id.tvReleaseDate)
    TextView mReleaseDate;
    @BindView(R.id.ibfavorite)
    ImageButton mFavorite;

    @BindView(R.id.rvVideos)
    RecyclerView mVideosList;
    @BindView(R.id.rvReviews)
    RecyclerView mReviewsList;
    @BindView(R.id.tvTrailers)
    TextView mErrorTrailers;
    @BindView(R.id.tvReviews)
    TextView mErrorReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        mUri = getIntent().getData();

        if (mUri == null) throw new NullPointerException("Add URI for DetailActivity, it can not be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

        mFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));

    }

    public void onClickFavorite(View view) {
        String favoriteMovieId = mUri.getLastPathSegment();

        Uri uri = Contract.MovieColumn.CONTENT_URI;
        uri = uri.buildUpon().appendPath(favoriteMovieId).build();
        getContentResolver().delete(uri, null, null);
        String text = "Favorite movie deleted";
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
        mFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

        switch (loaderId) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        FAVORITE_MOVIES_DETAIL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {

            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {

            return;
        }

        int indexMovieId = data.getInt(INDEX_MOVIES_ID);
        double movieVoteAverageIndex = data.getDouble(INDEX_VOTE_AVERAGE);
        String moviePosterPathIndex = data.getString(INDEX_POSTER_PATH);
        String movieNameIndex = data.getString(INDEX_ORIGINAL_TITLE);
        String movieOverviewIndex = data.getString(INDEX_OVERVIEW);
        String movieReleaseDateIndex = data.getString(INDEX_RELEASE_DATE);

        String fullPosterPath = BASE_IMAGE_URL + moviePosterPathIndex;

        mMovieRating.setText(String.valueOf(movieVoteAverageIndex));
        Picasso.with(this).load(fullPosterPath).placeholder(R.drawable.defaultimage).resize(700, 1000).into(mMoviePoster);
        mMovieTitle.setText(movieNameIndex);
        mSynopsis.setText(movieOverviewIndex);
        mReleaseDate.setText(movieReleaseDateIndex);

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

        loadVideosData(String.valueOf(indexMovieId));
        loadReviewsData(String.valueOf(indexMovieId));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void loadVideosData(String movieId) {
        showVideoDataView();
        new FavoriteDetail.FetchVideosTask().execute(movieId);
    }

    private void loadReviewsData(String movieId) {
        showReviewDataView();
        new FavoriteDetail.FetchReviewsTask().execute(movieId);
    }

    private void showVideoDataView() {
        mVideosList.setVisibility(View.VISIBLE);
    }

    private void showReviewDataView() {
        mReviewsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Trailer video) {

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

                ArrayList<Trailer> simpleJsonVideoData = TrailerJsonUtils.parseTrailerJson(FavoriteDetail.this, jsonVideoResponse);

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

                ArrayList<Review> simpleJsonReviewData = ReviewJsonUtils.parseReviewCommentsJson(FavoriteDetail.this, jsonReviewResponse);

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
