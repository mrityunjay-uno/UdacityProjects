package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.util.Constants.BASE_IMAGE_URL;
import static com.example.android.popularmovies.util.Constants.BASE_ORIGINAL_IMAGE_URL;
import static com.example.android.popularmovies.util.Constants.JSON_IMAGE;
import static com.example.android.popularmovies.util.Constants.JSON_RATING;
import static com.example.android.popularmovies.util.Constants.JSON_RELEASE_DATE;
import static com.example.android.popularmovies.util.Constants.JSON_SYNOPSIS;
import static com.example.android.popularmovies.util.Constants.JSON_TITLE;

public class MovieDetail extends AppCompatActivity {

    private ImageView mPosterImage;
    private TextView mTitle;
    private TextView mSynopsis;
    private TextView mRating;
    private TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPosterImage = (ImageView) findViewById(R.id.ivDetailsImage);
        mTitle = (TextView) findViewById(R.id.tvMovieTitle);
        mSynopsis = (TextView) findViewById(R.id.tvSynopsis);
        mRating = (TextView) findViewById(R.id.tvRating);
        mReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);




        if (intent.hasExtra(JSON_IMAGE)) {
            String moviePoster = BASE_ORIGINAL_IMAGE_URL+intent.getStringExtra(JSON_IMAGE);
            Picasso.with(this)
                    .load(moviePoster)
                    .into(mPosterImage);
        }

        if (intent.hasExtra(JSON_TITLE)) {
            mTitle.setText(intent.getStringExtra(JSON_TITLE));
            getSupportActionBar().setTitle(intent.getStringExtra(JSON_TITLE));
        }
        if (intent.hasExtra(JSON_SYNOPSIS)) {
            mSynopsis.setText(intent.getStringExtra(JSON_SYNOPSIS));
        }
        if (intent.hasExtra(JSON_RATING)) {
            mRating.setText(intent.getStringExtra(JSON_RATING));
        }
        if (intent.hasExtra(JSON_RELEASE_DATE)) {
            mReleaseDate.setText(intent.getStringExtra(JSON_RELEASE_DATE));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
