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
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shrekware.mypopularmovies.retrofitstuff.MovieObject;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView image;
    private RatingBar ratingBar;
    private MovieObject movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sets the UI view to the activity movie detail layout
        setContentView(R.layout.activity_movie_detail);
        // retrieves the intent and its packaged data
        Intent intent = getIntent();
        movie = intent.getParcelableExtra("movie");
        //set title on support bar to Movie Details
        getSupportActionBar().setTitle(R.string.title_movie_details);
        // finds the imageView for the movie poster
        image = findViewById(R.id.image_movie_detail);
        //textView for the movie title
        TextView movieTitle = findViewById(R.id.tv_movie_title);
        //textView for the movie overview(description)
        TextView movieOverview = findViewById(R.id.tv_movie_overview);
        //textView for the movie release date
        TextView releaseDate = findViewById(R.id.tv_release_date);
        // a 10 star - view only rating bar
        ratingBar = findViewById(R.id.ratingBar);
        // sets the rating stars value
        setStars();
        // gets the poster path and displays image in layout
        setImage();
        // sets the movie title    intent.getStringExtra("title")
        movieTitle.setText(movie.getTitle());
        // sets the movie description
        movieOverview.setText(movie.getOverview());
        // gets the release date string and the movie release date
        String rDate = getString(R.string.movie_detail_release_date_string) + movie.getReleaseDate();
        // set the movie release date
        releaseDate.setText(rDate);
    }

    private void setImage() {
        // string to get the poster path from the movie object
        String Poster = movie.getPosterPath();
        // you will need a ‘size’, which will be one of the following:
        // "w92", "w154", "w185", "w342", "w500", "w780",
        // or "original". For most phones we recommend using “w185”
        final String imageSize = getString(R.string.themoviedb_image_size);
        //the complete poster path for Picasso to use
        final String PosterPath = getString(R.string.themoviedb_base_image_url) + imageSize + Poster;
        //ask Picasso to do the heavy lifting, getting the pictures and load them into image
        Picasso.get().load(PosterPath).placeholder(R.mipmap.loading_please_wait).fit().into(image);
    }

    private void setStars() {
        //get rating from intent, which was acquired by the movie object
        //the vote_average from the movie object
        double rating = movie.getVoteAverage();
        // setting te amount of stars per rating, most of the work
        // here is done in the activity_movie_detail.xml file
        ratingBar.setRating((float) rating);
    }

    //this is for the home button action to go
    // back to the previous activity results
    // and not restart the main activity
    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }

   //adding activity to the manifest adds the home/back arrow
    // we add the selection of the home button to simulate the back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // checks the menu item id that was clicked
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // run the on Back pressed method, that goes back one screen
                onBackPressed();
                //return done
                return true;
        }
        //if no known items are selected it runs its default behavior
        return super.onOptionsItemSelected(item);
    }
}
