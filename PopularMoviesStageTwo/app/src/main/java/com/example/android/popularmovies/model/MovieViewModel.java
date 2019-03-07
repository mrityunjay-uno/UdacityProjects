package com.example.android.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.popularmovies.data.MovieDatabase;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;


    public  MovieViewModel(Application application)
    {
        super(application);
        MovieDatabase mDb = MovieDatabase.getInstance(this.getApplication());
        movies = mDb.movieDao().loadAllMovies();

    }


    public LiveData<List<Movie>> loadMovies() {
        // Do an asynchronous operation to fetch users.

        return movies;
    }

}

