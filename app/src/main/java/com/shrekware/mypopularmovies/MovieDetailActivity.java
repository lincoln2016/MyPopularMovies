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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView image;
    private RatingBar ratingBar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        intent = getIntent();
        String mTitle = intent.getStringExtra("title");
        // sets the title bar to the text string in strings.xml plus movie title
       // Objects.requireNonNull(getSupportActionBar()).setTitle(mTitle);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //imageView for the movie poster
        image = findViewById(R.id.image_movie_detail);

        TextView movieTitle = findViewById(R.id.tv_movie_title);
        //textView for the movie overview(description)
        TextView movieOverview = findViewById(R.id.tv_movie_overview);
        //textView for the movie release date
        TextView releaseDate = findViewById(R.id.tv_release_date);
        // a 10 star - view only rating bar
        ratingBar = findViewById(R.id.ratingBar);
        // sets the ratting stars value
        setStars();
        // sets te poster path to image
        setImage();

        movieTitle.setText(intent.getStringExtra("title"));

        movieOverview.setText(intent.getStringExtra("overview"));
        String rDate = "Release Date: "+intent.getStringExtra("release_date");
        releaseDate.setText(rDate);
    }

    public void setImage()
    {
        // string to get the poster path from the movie object
        String Poster = intent.getStringExtra("posterpath");
        // you will need a ‘size’, which will be one of the following:
        // "w92", "w154", "w185", "w342", "w500", "w780",
        // or "original". For most phones we recommend using “w185”
        final String imageSize = "w342/";
        //the complete poster path for Picasso to use
        final String PosterPath = "http://image.tmdb.org/t/p/"+ imageSize + Poster;
        //ask Picasso to do the heavy lifting, getting the pictures and load them into image
//        Picasso.get().load(PosterPath).placeholder(R.drawable.ic_launcher_foreground).fit().into(image);
        Picasso.get().load(PosterPath).placeholder(R.mipmap.ic_launcher).fit().into(image);
    }

    public void setStars()
    {
        //get rating from intent, which was acquired by the movie object
        //the vote_average from the movie object
        double rating = intent.getDoubleExtra("rating", 0);
        // setting te amount of stars per rating, most of the work
        // here is done in the activity_movie_detail.xml file
        ratingBar.setRating((float) rating);
    }
}
