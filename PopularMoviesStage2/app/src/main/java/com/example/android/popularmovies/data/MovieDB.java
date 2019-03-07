package com.example.android.popularmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.android.popularmovies.model.Movie;

@Database(entities = {Movie.class},version = 1, exportSchema = false)
public abstract class MovieDB extends RoomDatabase {

    public abstract MovieDAO movieDAO();

}
