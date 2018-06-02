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

import com.shrekware.mypopularmovies.mainactivity.MyMovieContext;
/*
*  this class is to provide the content provider
*  with instructions
*/
public class MovieProvider extends ContentProvider
{
    // creates an instance of the movie database helper
    private MovieDataBaseHelper mDataBaseHelper;
    // on created method
    @Override
    public boolean onCreate()
    {
        // create a reference to the app context
        Context context = getContext();
        // initialize a new Movie Database helper
        mDataBaseHelper = new MovieDataBaseHelper(context);
        // return done
        return true;
    }
    // method to query the database
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder)
    {
        //get an instance of a readable Movie database helper
        final SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
        // return the query results
        return db.query(MovieContract.MovieFavorites.cTABLE_NAME, projection,
                selection, selectionArgs, null, null, sortOrder);
    }
     // we are not using the type of data
    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        return null;
    }
    /*
    *   adds a movie to the favorites database
    */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        // checks to see if the Movie database helper exists
        if (mDataBaseHelper == null)
            // if there isn't a Movie database helper, we make a database helper
            // requires a good context,  used a class to get an instance of the app context
            mDataBaseHelper = new MovieDataBaseHelper(MyMovieContext.getMyContext());
        // create a a final db from the helper
        final SQLiteDatabase sqlDB = mDataBaseHelper.getWritableDatabase();
        // insert the movie into the table
        sqlDB.insert(MovieContract.MovieFavorites.cTABLE_NAME, null, values);
        // returns nothing for a uri
        return null;
    }
/*
*     the method to delete a favorites movie from the database
*/
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        // initialize a db to a writable object
        final SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        // return the count of movies deleted
        return db.delete(MovieContract.MovieFavorites.cTABLE_NAME, selection, selectionArgs);
    }
    // not implementing update
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs)
    {
        return 0;
    }
}