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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shrekware.mypopularmovies.moviedetailactivity.MovieReviewListObject;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieReviewObject;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieReviewsAdapter;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieTrailerListObject;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieTrailerObject;
import com.shrekware.mypopularmovies.mainactivity.MovieObject;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieTrailersAdapter;
import com.shrekware.mypopularmovies.moviedetailactivity.RetrofitMovieTrailersClient;
import com.squareup.picasso.Picasso;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements MovieTrailersAdapter.MovieTrailersAdapterOnClickHandler{
    // tag for log messages
    private final static String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    // the api key from theMovieDB, it can be found in the values/strings.xml file
    private String API_KEY;
    // get a reference to this context
    private Context mContext;
    //the imageView for the movie poster binding to the layout image id
    @BindView(R.id.image_movie_detail)
    ImageView image;
    // a rating bar with 10 stars to show the vote average
    //binding it to the layout rating bar id
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    //the movie object displayed in the MovieDetailActivity
    private MovieObject movie;
    // list of movie trailer objects
    private List<MovieTrailerObject> trailersList;
    // list of movie review objects
    private List<MovieReviewObject> reviewsList;
    // recyclerView for the trailers list and bind it to the recycler trailers id
    @BindView(R.id.recyclerView_movieTrailers)
    RecyclerView movieTrailersRecyclerView;
    //recyclerView for the reviews list
    @BindView(R.id.recyclerView_movieReviews)
    RecyclerView movieReviewsRecyclerView;
    //textView for the movie title
    @BindView(R.id.tv_movie_title)
    TextView movieTitle;
    //textView for the movie overview(description)
    @BindView(R.id.tv_movie_overview)
    TextView movieOverview;
    //textView for the movie release date
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    //textView for the average user rating to add the int score to the end of the string
    @BindView(R.id.tv_average_user_rating)
    TextView userRatingText;
    @BindView(R.id.tv_main_title_reviews)
    TextView reviewsMainTitle;
    @BindView(R.id.tv_title_reviews)
    TextView reviewsTitle;
    // the recyclerView adapter for the movie trailers
    private MovieTrailersAdapter movieTrailersAdapter;
    private  MovieReviewsAdapter mReviewAdapter;
    private MovieReviewListObject reviewListObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sets the UI view to the activity movie detail layout
        setContentView(R.layout.activity_movie_detail);
        // calls Butterknife to bind the views
        ButterKnife.bind(this);
        // gets a reference to app context
        mContext = this;
        //retrieve api key
        API_KEY = getString(R.string.api_key);
        // retrieves the intent and its packaged data
        Intent intent = getIntent();
        //retrieves the attached movie object
        movie = intent.getParcelableExtra("movie");
        //set title on support bar to Movie Details
        getSupportActionBar().setTitle(movie.getTitle());
        // string to add vote average to heading end
        String averageRating = getString(R.string.average_user_rating)+": "+movie.getVoteAverage().toString();
        // setting the userRating textView to the title and vote average
        userRatingText.setText(averageRating);
        // sets the layout manager to a linear layout on this activity
        movieTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieTrailersAdapter = new MovieTrailersAdapter(trailersList,null);
        movieTrailersRecyclerView.setAdapter(movieTrailersAdapter);
        //reviews recyclerView
        mReviewAdapter = new MovieReviewsAdapter(reviewsList);
        movieReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieReviewsRecyclerView.setAdapter(mReviewAdapter);
        // sets the rating stars value
        setStars();
        // gets the poster path and displays image in layout
        setImage();
        // sets the movie title
        movieTitle.setText(movie.getTitle());
        // sets the movie description
        movieOverview.setText(movie.getOverview());
        // gets the release date string and the movie release date
        String rDate = getString(R.string.movie_detail_release_date_string) + movie.getReleaseDate();
        // set the movie release date
        releaseDate.setText(rDate);
        // the retrofit call the returns a list of trailer objects
        getMovieTrailers();
        getMovieReviews();
    }
    // the method that fetches the movie trailers using retrofit
    private void getMovieTrailers()
     {
         Log.v(LOG_TAG,"getMovie trailers");
        //checks if the list of movie trailer objects exists
        if (trailersList != null) {
            //if there is a list, we clear the movie trailer objects from it
            trailersList.clear();
        }
         Log.v(LOG_TAG,"Retrofit client");
        // gets an instance of retrofit client
        RetrofitMovieTrailersClient client = new RetrofitMovieTrailersClient();
         Log.v(LOG_TAG,"new Retrofit client");
        if (getInternetStatus())
            Log.v(LOG_TAG,"check internet");
        {
            client.getTrailerService().getMovieTrailers(movie.getId(),API_KEY).enqueue(new Callback<MovieTrailerListObject>()
            {
                @Override
                public void onResponse(Call<MovieTrailerListObject> call, Response<MovieTrailerListObject> response)
                {
                    Log.v(LOG_TAG,"on response movie id: " + movie.getId().toString());
                    if (response.body() != null)
                    {
                          //  Log.v(LOG_TAG,"response: key for you tube: "+ response.body().getResults().get(0).getKey());
                        // /loads the response into the resultsList
                        trailersList = response.body().getResults();
                        //adds the resultList to the RecyclerView
                        movieTrailersRecyclerView.setAdapter(new MovieTrailersAdapter(trailersList, getTrailersClickHandler()));
                    }
                }
                @Override
                public void onFailure(Call<MovieTrailerListObject> call, Throwable t)
                {
                    Log.v(LOG_TAG,"on failure" +t.toString());
                }
            });
        }
     }
    private void getMovieReviews()
    {
        Log.v(LOG_TAG,"getMovie reviews");
        //checks if the list of movie trailer objects exists
        if (reviewsList != null)
        {
            //if there istis a list, we clear the movie trailer objects from it
            reviewsList.clear();
        }
        Log.v(LOG_TAG,"Retrofit client");
        // gets an instance of retrofit client
        RetrofitMovieTrailersClient client = new RetrofitMovieTrailersClient();
        Log.v(LOG_TAG,"new Retrofit client");
        if (getInternetStatus())
        {
            client.getTrailerService().getMovieReviews(movie.getId(),API_KEY).enqueue(new Callback<MovieReviewListObject>()
            {
                @Override
                public void onResponse(Call<MovieReviewListObject> call, Response<MovieReviewListObject> response) {
                    Log.v(LOG_TAG, "on response movie id: " + movie.getId().toString());
                    if (response.body() != null) {
                        //Log.v(LOG_TAG,"response: key for you tube: "+ response.body().getResults().get(0).getKey());
                        // /loads the response into the resultsList
                        reviewsList = response.body().getResults();
                         // gets the whole object from theMovieDB.org for the
                        // total number of reviews and pages of reviews
                        reviewListObject = response.body();

                        // sets the Review title and fills in the
                        // information of how many reviews and total pages
                      setReviewTitle(reviewListObject);
                         //adds the resultList to the RecyclerView
                        movieReviewsRecyclerView.setAdapter(new MovieReviewsAdapter(reviewsList));
                    }
                }
                @Override
                public void onFailure(Call<MovieReviewListObject> call, Throwable t) {
                        Log.v(LOG_TAG,"on failure" +t.toString());
                }


            });
        }
    }
    private void setImage(){
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
    private void setReviewTitle(MovieReviewListObject object)
    {
        //if no reviews, hide the number of reviews textView
        String myReviewsTitle;
        if (object.getTotalResults()<1)
        {
            //hides the textView for number of reviews and pages
            reviewsTitle.setVisibility(View.GONE);
            reviewsMainTitle.setText(R.string.movie_detail_no_reviews);

        }    //if only 1 page, hides the number of pages
        else if(object.getTotalPages()==1)
        {
             reviewsMainTitle.setText(R.string.movie_detail_reviews);
             if(object.getTotalResults()==1)
             {
                 myReviewsTitle = reviewListObject.getTotalResults().toString() +" Review";
                 reviewsTitle.setText(myReviewsTitle);
             }
             else
             {
                 myReviewsTitle = reviewListObject.getTotalResults().toString() +" Reviews";
                 reviewsTitle.setText(myReviewsTitle);
             }
        }
        else
        {
              reviewsMainTitle.setText(R.string.movie_detail_reviews);
              myReviewsTitle = reviewListObject.getTotalResults().toString() +" Reviews, page "+ reviewListObject.getPage().toString()+" of "+ reviewListObject.getTotalPages().toString();
              reviewsTitle.setText(myReviewsTitle);
        }
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
     // needed to reference this on clickHandler
    private MovieTrailersAdapter.MovieTrailersAdapterOnClickHandler getTrailersClickHandler() {
        //returns this for the OnClickHandler
        return this;
    }

    public boolean getInternetStatus() {
        // opens a dialog to the phone about its connection to the network
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // gets an instance of the NetworkInfo.class
        NetworkInfo networkInfo = null;
        // checks to see if the connectivity manager is available
        if (connectivityManager != null) {
            //asks the phone if it has a network connection
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
         // returns true  if network information is not null and the network is connected
         // does not test internet access, it could be blocked!, but not likely...
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onClick(MovieTrailerObject movie) {

      Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + movie.getKey()));

      //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + video_id));
        startActivity(youTubeIntent);

    }
}
