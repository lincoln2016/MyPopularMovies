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

package com.shrekware.mypopularmovies.retrofitstuff;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This interface is used with the Retrofit client as the service the client will use
 * to retrieve the most popular movies list from theMovieDB
 */

public interface MovieListServicePopular
{

       //  builds the query part of the path for theMovieDB.org API call
     //  "/3/discover/movie?api_key="+ API_KEY + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";

       //Every method must have an HTTP annotation that provides
       // the request method and relative URL.
       // There are five built-in annotations:
       // GET, POST, PUT, DELETE, and HEAD.
/*
       @GET("/3/discover/movie")

       // the method Call the Retrofit client will use to return the MOST POPULAR movie list from theMovieDB.org
       Call<MovieListRetrofitObject> getAllMovies(@Query("api_key") String api_key, @Query("language") String language, @Query("sort_by") String sort_by,
                                                  @Query("include_adult")String include_adult, @Query("include_video") String include_video,
                                                  @Query("page") String page);
*/
       @GET("/3/movie/popular")
       Call<MovieListRetrofitObject> getPopularMovies(@Query("api_key") String api_key);

       @GET("/3/movie/top_rated")
       Call<MovieListRetrofitObject> getTopRatedMovies(@Query("api_key") String api_key);







}
