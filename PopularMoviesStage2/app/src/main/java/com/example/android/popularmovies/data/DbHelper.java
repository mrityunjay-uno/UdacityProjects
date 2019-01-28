package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.popularmovies.util.Constants.*;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favoritemovies.db";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + TABLE_NAME + " (" +

                        Contract.MovieColumn._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Contract.MovieColumn.COLUMN_MOVIES_ID + " INTEGER NOT NULL, "                 +
                        Contract.MovieColumn.COLUMN_RATING + " REAL NOT NULL, "                 +
                        Contract.MovieColumn.COLUMN_POSTER + " BLOB NOT NULL, "                 +
                        Contract.MovieColumn.COLUMN_TITLE + " TEXT NOT NULL, "                 +
                        Contract.MovieColumn.COLUMN_SYNOPSIS + " TEXT NOT NULL, "                 +
                        Contract.MovieColumn.COLUMN_RELEASE_DATE    + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
