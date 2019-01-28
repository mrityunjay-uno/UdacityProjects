package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.popularmovies.adapter.FavoriteAdapter;
import com.example.android.popularmovies.data.Contract;
import static com.example.android.popularmovies.util.Constants.*;

public class FavoriteActivity extends AppCompatActivity implements
        FavoriteAdapter.FavoriteAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FAVORITES_LOADER_ID = 0;

    private FavoriteAdapter mAdapter;
    RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewFavorites);

        GridLayoutManager LayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(LayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new FavoriteAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(FAVORITES_LOADER_ID, null, this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, this);
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {

                    deliverResult(mTaskData);
                } else {

                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(Contract.MovieColumn.CONTENT_URI,
                            null,
                            null,
                            null,
                            Contract.MovieColumn.COLUMN_MOVIES_ID);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showFavoritesDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(long id) {
        Intent favoriteMoviesDetailIntent = new Intent(FavoriteActivity.this, FavoriteDetail.class);

        Uri uriForMovieClicked = Contract.MovieColumn.buildMoviesUriWithId(id);
        favoriteMoviesDetailIntent.setData(uriForMovieClicked);
        startActivity(favoriteMoviesDetailIntent);
    }

    private void showFavoritesDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
