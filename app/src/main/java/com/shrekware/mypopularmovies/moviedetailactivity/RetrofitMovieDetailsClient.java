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

package com.shrekware.mypopularmovies.moviedetailactivity;

import com.shrekware.mypopularmovies.MainActivity;
import com.shrekware.mypopularmovies.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMovieDetailsClient
{

    // constant string for theMovieDB.org base API URL - https://api.themoviedb.org/
    private static final String BASE_URL = MainActivity.resources.getString(R.string.retrofit_client_base_url);
    // creates an instance of the MovieListService which sets up the call for retrofit
    // the MovieListService includes the API_KEY
    final private MovieDetailsService movieDetailsService;
    /*
     * the constructor for the Retrofit Client
     */
    public RetrofitMovieDetailsClient()
    {
        // creating the retrofit object we will use to call theMovieDB.org
        Retrofit retrofit = new Retrofit.Builder()
                //adds base url to the retrofit client
                .baseUrl(BASE_URL)
                //adds the Gson converter to the client to interpret the response into Gson notation
                .addConverterFactory(GsonConverterFactory.create())
                //builds the retrofit client
                .build();

        // adds the movie trailer service to the retrofit client
        movieDetailsService = retrofit.create(MovieDetailsService.class);
    }
    //  the method the retrofit client will call to retrieve the movie trailers or reviews list
    public MovieDetailsService getMovieDetailService()
    {
        //returns the trailers list
        return movieDetailsService;
    }
}
