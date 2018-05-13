/*
 * PROJECT LICENSE 
 * This project was submitted by Henry Ayers as part of the Nanodegree At Udacity. 
 * As part of Udacity Honor code, your submissions must be your own work,
 * hence submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account. Me, the author of the project, 
 * allow you to check the code as a reference,
 * but if you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2018 Henry Ayers
 *
 * Besides the above notice, the following license applies and
 * this license notice must be included in all works derived from this project. 
 * MIT License Permission is hereby granted, free of charge, 
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.shrekware.mypopularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.shrekware.mypopularmovies.database.MovieListContract.*;

public class MovieListDBHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "popularmovies.db";
    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;
    
    //Constructor
    public MovieListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold movies list data with columns for all entries
        final String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE " + MovieListPopular.TABLE_NAME + " (" +
                MovieListPopular._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieListPopular.COLUMN_ADULT + " BOOLEAN NOT NULL, " +
                MovieListPopular.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieListPopular.COLUMN_ID + " INTEGER NOT NULL, "+
                MovieListPopular.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                MovieListPopular.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieListPopular.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieListPopular.COLUMN_POPULARITY + " INTEGER NOT NULL, " +
                MovieListPopular.COLUMN_POSTER_PATH  + " TEXT NOT NULL, " +
                MovieListPopular.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieListPopular.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieListPopular.COLUMN_VIDEO + " BOOLEAN NOT NULL, " +
                MovieListPopular.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL, " +
                MovieListPopular.COLUMN_VOTE_COUNT + " INTEGER NOT NULL " +
                "); ";

        db.execSQL(SQL_CREATE_MOVIELIST_TABLE);


        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // if you change the DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db.execSQL("DROP TABLE IF EXISTS " + MovieListPopular.TABLE_NAME);
        onCreate(db);
    }
}
