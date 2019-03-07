package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.util.Constants;
import com.example.android.popularmovies.util.JsonUtils;
import com.example.android.popularmovies.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TrailerActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener  {

    public List<Trailer> mTrailerList;
    private TrailerAdapter mAdapter;
    private RecyclerView mTrailersListView;
    private Movie movie;


    public static final String MOVIE = "movie";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        movie = (Movie)getIntent().getSerializableExtra(MOVIE);

        mTrailersListView = (RecyclerView) findViewById(R.id.rv_trailers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mTrailersListView.setLayoutManager(linearLayoutManager);

        mAdapter = new TrailerAdapter(mTrailerList, this);
        mTrailersListView.setAdapter(mAdapter);

        String apiKey = Constants.API_KEY;
        final String trailerParamQuery = apiKey;

        URL trailerListURL = NetworkUtils.buildTrailerUrl(trailerParamQuery, String.valueOf(movie.getId()));
        new JsonQueryTask().execute(trailerListURL);
    }

    @Override
    public void onListItemClick(int clickedItemIndex){

        Trailer trailer = mTrailerList.get(clickedItemIndex);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_VIDEO_BASE_URL + trailer.getKey()));
        startActivity(intent);
    }


    public class JsonQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL trailerListURL = params[0];
            String trailerListResults = null;
            try {
                trailerListResults = NetworkUtils.getResponseFromHttpUrl(trailerListURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return trailerListResults;
        }

        @Override
        protected void onPostExecute(String trailerListResults) {
            if (trailerListResults != null && !trailerListResults.equals("")) {

                Log.i("postExecute", "Post execute started");

                mTrailerList = JsonUtils.parseTrailerJson(trailerListResults);
                mAdapter.mTrailerList = mTrailerList;

                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
