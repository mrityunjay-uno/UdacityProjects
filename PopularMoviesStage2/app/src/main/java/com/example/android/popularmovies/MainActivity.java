package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.adapter.MovieListAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.util.JsonUtils;
import com.example.android.popularmovies.util.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import static com.example.android.popularmovies.util.Constants.*;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> {


    MovieListAdapter mAdapter;
    RecyclerView mMoviesList;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesList = (RecyclerView) findViewById(R.id.rvMovies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tvErrorMessageDisplay);

        GridLayoutManager LayoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(LayoutManager);
        mMoviesList.setHasFixedSize(true);
        mAdapter = new MovieListAdapter(this);
        mMoviesList.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pbLoadingIndicator);

        int loaderId = MOVIES_LOADER_ID;

        LoaderManager.LoaderCallbacks<ArrayList<Movie>> callback = MainActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(SORT_ORDER_EXTRA, POPULAR_MOVIE);
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mMoviesList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mMoviesList.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    private void loadMoviesData(String sortOrder) {
        String sortOrderExtra = sortOrder;

        Bundle moviesBundle = new Bundle();

        moviesBundle.putString(SORT_ORDER_EXTRA, sortOrderExtra.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Movie>> moviesLoader = loaderManager.getLoader(MOVIES_LOADER_ID);

        if (moviesLoader == null) {
            loaderManager.initLoader(MOVIES_LOADER_ID, moviesBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER_ID, moviesBundle, this);
        }
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie> mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Movie> loadInBackground() {

                String sortOrder = loaderArgs.getString(SORT_ORDER_EXTRA);

                URL moviesRequestUrl = NetworkUtils.buildUrl(sortOrder);

                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

                    ArrayList<Movie> simpleJsonMovieData = JsonUtils.parseMovieJson(MainActivity.this, jsonMovieResponse);

                    return simpleJsonMovieData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(ArrayList<Movie> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mAdapter.setMovieData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
    }

    private void invalidateData() {
        mAdapter.setMovieData(null);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetail.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("movie", movie);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMoviesList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popular) {
            String sortOrder="popular";
            loadMoviesData(POPULAR_MOVIE);
            return true;
        }

        if (id == R.id.top_rated) {
            String sortOrder="top_rated";
            loadMoviesData(TOP_RATED_MOVIE);
            return true;
        }

        if (id == R.id.favorites) {
            Intent favoritesIntent = new Intent(MainActivity.this, FavoriteActivity.class);
            MainActivity.this.startActivity(favoritesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
