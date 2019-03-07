package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.popularmovies.adapter.MovieListAdapter;
import com.example.android.popularmovies.data.MovieDatabase;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieViewModel;
import com.example.android.popularmovies.util.Constants;
import com.example.android.popularmovies.util.JsonUtils;
import com.example.android.popularmovies.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.ListItemClickListener, AdapterView.OnItemSelectedListener {


    private static final String POSITION_RECYCLER_LAYOUT = "recycler_layout_position";
    private static final String SPINNER_POSITION = "spinner_position";

    public List<Movie> mMovieList;
    public List<Movie> mFavoriteMovieList;
    private MovieListAdapter mAdapter;
    private RecyclerView mMoviesListView;
    private Spinner spinner;
    private static final String[] paths = {"Popular", "Top rated", "Favorites"};
    private MovieDatabase mDb;
    private int currentVisiblePosition;


    String mMoviesState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mDb = MovieDatabase.getInstance(getApplicationContext());

        mMoviesListView = (RecyclerView) findViewById(R.id.rv_posters);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesListView.setLayoutManager(gridLayoutManager);

        mAdapter = new MovieListAdapter(mMovieList, this);
        mMoviesListView.setAdapter(mAdapter);


        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(savedInstanceState != null)
        {
            int spinnerPosition = savedInstanceState.getInt(SPINNER_POSITION);

            if(spinnerPosition == 0)
            {
                spinner.setSelection(1,false);
            }
            else {
                spinner.setSelection(0,false);
            }
        }

        spinner.setOnItemSelectedListener(this);


        if(savedInstanceState != null)
        {
            int mScrollPosition = savedInstanceState.getInt(POSITION_RECYCLER_LAYOUT);

            LinearLayoutManager manager = (LinearLayoutManager)mMoviesListView.getLayoutManager();
        }

        setupViewModel();
    }

    private void setupViewModel() {
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.loadMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mFavoriteMovieList = movies;

                if(spinner.getSelectedItemPosition() == 2)
                {
                    mAdapter.mMovieList = mFavoriteMovieList;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {

        URL movieListURL = null;
        String apiKey = Constants.API_KEY;
        final String movieParamQuery = apiKey;

        switch (position)
        {
            case 0:
            {
                movieListURL = NetworkUtils.buildUrl(movieParamQuery, 0);
            }
            break;

            case 1:
            {
                movieListURL = NetworkUtils.buildUrl(movieParamQuery, 1);
            }
            break;

            case 2:
            {
                mAdapter.mMovieList = mFavoriteMovieList;
                mAdapter.notifyDataSetChanged();
            }
            break;

            default:
            {

            }
            break;
        }


        if(movieListURL != null)
        {
            new JsonQueryTask().execute(movieListURL);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void launchDetailActivity(int position) {

        Movie movie;

        if(spinner.getSelectedItemPosition() == 2)
        {
            movie = mFavoriteMovieList.get(position);
        }
        else
        {
            movie = mMovieList.get(position);
        }

        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(MovieDetail.EXTRA_POSITION, position);
        intent.putExtra(MovieDetail.MOVIE, movie);

        startActivity(intent);

    }

    @Override
    public void onListItemClick(int clickedItemIndex){

        launchDetailActivity(clickedItemIndex);
    }


    public class JsonQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL movieListURL = params[0];
            String movieListResults = null;
            try {
                movieListResults = NetworkUtils.getResponseFromHttpUrl(movieListURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieListResults;
        }

        @Override
        protected void onPostExecute(String movieListResults) {
            if (movieListResults != null && !movieListResults.equals("")) {

                Log.i("postExecute", "Post execute started");

                mMovieList = JsonUtils.parseMovieJson(movieListResults);
                mAdapter.mMovieList = mMovieList;
                mAdapter.notifyDataSetChanged();


                mMoviesListView.scrollToPosition(currentVisiblePosition);
                currentVisiblePosition = 0;
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)  {

        super.onSaveInstanceState(outState);


        int spinnerPosition = spinner.getSelectedItemPosition();
        outState.putInt(SPINNER_POSITION, spinnerPosition);

        int currentVisiblePosition = ((LinearLayoutManager)mMoviesListView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(POSITION_RECYCLER_LAYOUT, currentVisiblePosition);



    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {

            currentVisiblePosition = savedInstanceState.getInt(POSITION_RECYCLER_LAYOUT);
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();


    }
}
