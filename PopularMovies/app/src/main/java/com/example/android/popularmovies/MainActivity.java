package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.adapter.MovieListAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import static com.example.android.popularmovies.util.Constants.*;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private MovieListAdapter mRecyclerAdapter;
    private GridLayoutManager mGridManager;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        makeMovieQuery(POPULAR_MOVIE);
    }

    @Override
    public void onItemClick(View view, int position) {
//        Log.i("TAG", "You clicked number " + mRecyclerAdapter.getItem(position) + ", which is at cell position " + position);
        Log.i("TAG", "You clicked number , which is at cell position " + position);

        mMovie = mRecyclerAdapter.getItem(position);

        Intent intent = new Intent(MainActivity.this, MovieDetail.class);
        if (mMovie != null) {
            intent.putExtra(JSON_TITLE, mMovie.getMovieTitle() != null ? mMovie.getMovieTitle() : "");
            intent.putExtra(JSON_ID, String.valueOf(mMovie.getMovieID()) != null ? String.valueOf(mMovie.getMovieID()) : "");
//            Log.i("Image STRING", "Movie Image url: "+mMovie.getMovieImage());
            intent.putExtra(JSON_IMAGE, mMovie.getMovieImage() != null ? mMovie.getMovieImage() : "");
            intent.putExtra(JSON_RATING, String.valueOf(mMovie.getMovieRating()) != null ? String.valueOf(mMovie.getMovieRating()) : "");
            intent.putExtra(JSON_SYNOPSIS, mMovie.getMoviSynopsis() != null ? mMovie.getMoviSynopsis() : "");
            intent.putExtra(JSON_RELEASE_DATE, mMovie.getReleaseDate() != null ? mMovie.getReleaseDate() : "");
        }

        startActivity(intent);
    }


    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String results = null;
            try {
                results = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;


        }

        @Override
        protected void onPostExecute(String results) {
            if (results != null && !results.equals("")) {
                Log.i("TAG", "Return JSON result " + results);

                int numberOfColumns = 2;
                // set up the RecyclerView
                mRecyclerView = (RecyclerView) findViewById (R.id.rvMovieList);
                mGridManager = new GridLayoutManager(MainActivity.this,numberOfColumns);

                mRecyclerView.setLayoutManager(mGridManager);
                mRecyclerAdapter = new MovieListAdapter(MainActivity.this, results);
                mRecyclerAdapter.setClickListener(MainActivity.this);
                mRecyclerView.setAdapter(mRecyclerAdapter);
            }
        }
    }

    private void makeMovieQuery(String sortingType) {

        URL url = NetworkUtils.buildUrl(sortingType);
        new MovieQueryTask().execute(url);

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.xml.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                makeMovieQuery(POPULAR_MOVIE);
                return true;

            case R.id.top_rated:
                makeMovieQuery(TOP_RATED_MOVIE);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return true;

        }
    }
}
