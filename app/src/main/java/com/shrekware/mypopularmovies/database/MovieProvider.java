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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shrekware.mypopularmovies.MyMovieContext;

public class MovieProvider extends ContentProvider
{

   private  Context context;
    private MovieDataBaseHelper mDataBaseHelper;




    @Override
    public boolean onCreate() {
       context = getContext();
        mDataBaseHelper = new MovieDataBaseHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

         Cursor cursor;
       final SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        // try to get a write able database

            //set the db to a write able instance of the movie databaseHelper

            // get an instance data in the favorites database
            cursor = db.query(MovieContract.MovieFavorites.cTABLE_NAME, projection,
                    selection, selectionArgs, null ,null,sortOrder);

            // if there is an error, show toast message saying database unavailable
          //  Toast.makeText(this, R.string.database_unavailable_toast_movie_detail,Toast.LENGTH_LONG).show();

//   new String[]{"_id","MOVIE_ID","TITLE","OVERVIEW","POSTERPATH","RELEASE_DATE","VOTE_AVERAGE"}


             return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // grabs an instance of ContentValues

    if(mDataBaseHelper==null)mDataBaseHelper = new MovieDataBaseHelper(MyMovieContext.getMyContext());
       final SQLiteDatabase sqlDB = mDataBaseHelper.getWritableDatabase();

       sqlDB.insert(MovieContract.MovieFavorites.cTABLE_NAME,null,values);

     return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
     int count;
     final SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
     count = db.delete(MovieContract.MovieFavorites.cTABLE_NAME, selection,selectionArgs);
     return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}
