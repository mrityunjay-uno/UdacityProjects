package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "favoritemovies";

    public static final class MovieColumn implements BaseColumns {

        public static final String COLUMN_MOVIES_ID = "movies_id";
        public static final String COLUMN_RATING = "reting";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static Uri buildMoviesUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

        public static String getSqlSelectForTodayOnwards() {
            return MovieColumn.COLUMN_MOVIES_ID + " >= ";
        }

    }
}
