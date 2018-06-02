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

/*
*    Class used by MovieProvider to access the favorites database
*/
class MovieDataBaseHelper extends SQLiteOpenHelper
{
    //// name of database
    private static final String DB_NAME = MovieContract.MovieFavorites.cDATABASE_NAME;
    //  database version
    private static final int DB_VERSION = 1;
    // create database table string - column names
    private static final String CREATE_FAVORITE = "CREATE TABLE " +
            MovieContract.MovieFavorites.cTABLE_NAME + "(" +
            MovieContract.MovieFavorites.cID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MovieFavorites.cMOVIE_ID + " INTEGER, " +
            MovieContract.MovieFavorites.cTITLE + " TEXT, " +
            MovieContract.MovieFavorites.cOVERVIEW + " TEXT," +
            MovieContract.MovieFavorites.cPOSTER + " TEXT," +
            MovieContract.MovieFavorites.cRELEASE_DATE + " TEXT," +
            MovieContract.MovieFavorites.cVOTE_AVERAGE + " REAL );";
    /*
    * Movie Database constructor
    */
     MovieDataBaseHelper(Context context)
    {
        //default constructor
        super(context, DB_NAME, null, DB_VERSION);
    }
    //this should include everything your database needs to create a table
    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE THE DATABASE
        db.execSQL(CREATE_FAVORITE);
    }
    // used to modify the structure of the table after release
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // if we needed to upgrade the database
    }

}
