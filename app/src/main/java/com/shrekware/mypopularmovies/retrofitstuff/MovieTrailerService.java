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
import retrofit2.http.Path;
import retrofit2.http.Query;
/*
 * This interface is used with the Retrofit client as a service, the Retrofit client will use
 * the service to retrieve the trailers and review for the movie object clicked
 */
public interface MovieTrailerService {

    // a reference call
    Call<MovieListObject> getAllMovies(@Query("api_key") String api_key, @Query("language") String language, @Query("sort_by") String sort_by,
                                               @Query("include_adult")String include_adult, @Query("include_video") String include_video,
                                               @Query("page") String page);


    //  builds the query part of the path for theMovieDB.org API call
    //  that returns the trailers for the movie object clicked, it adds the id of the movie to the path
    //  and the api key as a query parameter
    @GET("/3/movie/{id}/videos")
    Call<MovieListObject> getMovieTrailers(@Path("id") int id, @Query("api_key") String api_key);

    //  builds the query part of the path for theMovieDB.org API call
    //  that returns the reviews for the movie object clicked, it adds the id of the movie to the path
    //  and the api key as a query parameter
    @GET("/3/movie/{id}/reviews")
    Call<MovieListObject> getMovieReviews(@Path("id") int id,@Query("api_key") String api_key);

}
