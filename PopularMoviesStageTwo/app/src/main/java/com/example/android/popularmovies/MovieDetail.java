package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieDatabase;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.util.Constants;
import com.example.android.popularmovies.util.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity implements View.OnClickListener {

    Context context;

    public static final String EXTRA_POSITION = "extra_position";
    public static final String MOVIE = "movie";

    private static final int DEFAULT_POSITION = -1;

    Button watchButton;
    Button reviewButton;
    CheckBox favoriteButton;

    Movie movie;

    private MovieDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = (Movie)getIntent().getSerializableExtra(MOVIE);

        watchButton = (Button) findViewById(R.id.Trailer);
        watchButton.setOnClickListener(this);

        reviewButton = (Button) findViewById(R.id.Reviews);
        reviewButton.setOnClickListener(this);

        favoriteButton = (CheckBox) findViewById(R.id.favoriteCheckbox);
        favoriteButton.setOnClickListener(this);

        populateUI(movie);

    }

    private void populateUI(Movie movie) {

        String baseURL = Constants.POSTER_BASE_URL;
        String size = Constants.POSTER_SIZE;
        String posterURL = NetworkUtils.buildPosterUrl(baseURL,size,movie.getPoster_path());

        ImageView posterView = findViewById(R.id.image_iv);
        Picasso.with(context).load(posterURL).into(posterView);

        TextView title = findViewById(R.id.title);
        TextView originalTitle = findViewById(R.id.original_title);
        TextView populariry = findViewById(R.id.populariry);
        TextView vote_average = findViewById(R.id.vote_average);
        TextView release_date = findViewById(R.id.release_date);
        TextView overview = findViewById(R.id.overview);

        title.setText("Title: " + movie.getTitle());
        originalTitle.setText("Original Title :" + movie.getOriginal_title());

        double popularityParam = movie.getPopularity();
        populariry.setText("Popularity: " + Double.toString(popularityParam));

        double voteAverageParam = movie.getVote_average();
        vote_average.setText("Ratings: " + Double.toString(voteAverageParam));

        release_date.setText("Release Date : " + movie.getRelease_date());
        overview.setText("Plot:" + "\n" + movie.getOverview());

        if(movie.getFavourite() == true)
        {
            favoriteButton.setChecked(true);
        }

    }




    @Override
    public void onClick(View view) {
        if (view.equals(watchButton))
        {
            Intent intent = new Intent(this, TrailerActivity.class);
            intent.putExtra(MovieDetail.MOVIE, movie);
            startActivity(intent);
        }

        if (view.equals(reviewButton))
        {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra(MovieDetail.MOVIE, movie);
            startActivity(intent);
        }

        if (view.equals(favoriteButton))
        {
            mDb = MovieDatabase.getInstance(getApplicationContext());

            Boolean isChecked = favoriteButton.isChecked();

            isChecked = favoriteButton.isChecked();
            movie.setFavourite(isChecked);

            Movie movieEntry = new Movie(movie.getId(), movie.getTitle(), movie.getOriginal_title(),
                    movie.getPoster_path(), movie.getPopularity(), movie.getVote_average(), movie.getRelease_date(), movie.getOverview());

            if(isChecked) {

                // COMPLETED (9) Use the taskDao in the AppDatabase variable to insert the taskEntry
                mDb.movieDao().insertMovie(movieEntry);
                // COMPLETED (10) call finish() to come back to MainActivity
            }
            else
            {
                mDb.movieDao().deleteMovie(movieEntry);
            }
        }
    }
}
