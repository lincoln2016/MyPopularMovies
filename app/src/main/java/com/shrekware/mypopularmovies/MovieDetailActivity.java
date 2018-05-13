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

package com.shrekware.mypopularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shrekware.mypopularmovies.retrofitstuff.MovieDetailsRetrofitObject;
import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends AppCompatActivity {
    private ImageView image;
    private TextView movieOverview, releaseDate,movieTitle;
    private RatingBar ratingBar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
      intent = getIntent();

        image = findViewById(R.id.image_movie_detail);
        movieTitle = findViewById(R.id.tv_movie_title);
        movieOverview = findViewById(R.id.tv_movie_overview);
        releaseDate = findViewById(R.id.tv_release_date);
        ratingBar = findViewById(R.id.ratingBar);
        int rating = intent.getIntExtra("rating", 0);
        setStars();
        String Poster = intent.getStringExtra("posterpath");
        final String imageSize = "w185/";
        final String PosterPath = "http://image.tmdb.org/t/p/"+ imageSize + Poster;
        Log.v("MovieDetailActivity", "movie poster path: " +  PosterPath);
        Log.v("MovieDetailActivity", "rating " +  rating);
        Picasso.get().load(PosterPath).placeholder(R.drawable.ic_launcher_foreground).into(image);
         movieTitle.setText(intent.getStringExtra("title"));
         movieOverview.setText(intent.getStringExtra("overview"));
         String rDate = "Release Date: "+intent.getStringExtra("release_date");
         releaseDate.setText(rDate);




    }
    public void setStars(){
        int rating = intent.getIntExtra("rating", 0);
        if(rating < 10000){
           rating = (10*rating)/10000;

            ratingBar.setRating(rating);
            Log.v("MovieDetailActivity", "SETSTARS rating " +  rating);
        }


    }



}
       //   intent.putExtra("title", movie.getTitle());
       //           intent.putExtra("overview", movie.getOverview());
        //          intent.putExtra("posterpath", movie.getPosterPath());
        //          intent.putExtra("release_date", movie.getReleaseDate());
         //         intent.putExtra("rating", movie.getVoteCount());