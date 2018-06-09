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

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shrekware.mypopularmovies.database.MovieContract;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieReviewListObject;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieReviewObject;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieReviewsAdapter;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieTrailerListObject;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieTrailerObject;
import com.shrekware.mypopularmovies.mainactivity.MovieObject;
import com.shrekware.mypopularmovies.moviedetailactivity.MovieTrailersAdapter;
import com.shrekware.mypopularmovies.moviedetailactivity.RetrofitMovieDetailsClient;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements
        MovieTrailersAdapter.MovieTrailersAdapterOnClickHandler
{



    // tag for log messages
    private final static String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    // the api key from theMovieDB, it can be found in the values/strings.xml file
    private String API_KEY;
    // get a reference to this context
    private Context mContext;
    // the movie object displayed in the MovieDetailActivity
    private MovieObject movie;
    // list of movie trailer objects
    private List<MovieTrailerObject> trailersList;
    // list of movie review objects
    private List<MovieReviewObject> reviewsList;
    // movie reviews list
    private MovieReviewListObject reviewListObject;
    // gets a reference to the activity that can be passed to the innerclass
    private final Context thisActivity = this;
    //toolbar object
    private android.support.v7.widget.Toolbar toolbar;
    // binds the imageView to the movie_detail image
    @BindView(R.id.image_background_poster)
    ImageView myBackGroundImage;
    //loading indicator
    @BindView(R.id.progressBar_movie_trailers)
    ProgressBar mProgressBar;
    @BindView(R.id.image_movie_detail)
    ImageView image;
    // a rating bar with 10 stars to show the vote average
    // binding it to the layout rating bar id
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    // recyclerView for the trailers list and bind it to the recycler trailers id
    @BindView(R.id.recyclerView_movieTrailers)
    RecyclerView movieTrailersRecyclerView;
    // recyclerView for the reviews list
    @BindView(R.id.recyclerView_movieReviews)
    RecyclerView movieReviewsRecyclerView;
    // textView for the movie title
    @BindView(R.id.tv_movie_title)
    TextView movieTitle;
    // textView for the movie overview(description)
    @BindView(R.id.tv_movie_overview)
    TextView movieOverview;
    // textView for the movie release date
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    // textView for the average user rating to add the int score to the end of the string
    @BindView(R.id.tv_average_user_rating)
    TextView userRatingText;
    //textView for the Reviews title view
    @BindView(R.id.tv_main_title_reviews)
    TextView reviewsMainTitle;
    // textView for the number of reviews
    @BindView(R.id.tv_title_reviews)
    TextView numberReviews;
    // favorites add to list button
    @BindView(R.id.btn_favorite)
    ImageButton btnFavorite;
    // share button, shares the first trailer
    @BindView(R.id.btn_share)
    ImageButton btnShare;
    // remove from favorites button
    @BindView(R.id.btn_not_favorite)
    ImageButton btn_notFavorite;
    // the collapsing toolbar

    /*
    *  called when activity is created
    */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // the default create
        super.onCreate(savedInstanceState);
        // sets the UI view to the activity movie detail layout
        setContentView(R.layout.activity_movie_detail_collapsing);
        // calls ButterKnife to bind the views
        ButterKnife.bind(this);
        // gets a reference to app context
        mContext = this;
        // retrieve api key
        API_KEY = getString(R.string.api_key);
        // retrieves the intent and its packaged data, used to display the movie
        Intent intent = getIntent();
        //gets a reference to the layout toolbar
        toolbar = findViewById(R.id.toolbar);
        // retrieves the attached movie object
        movie = intent.getParcelableExtra("movie");
        // string to add vote average to heading end
        String averageRating = getString(R.string.average_user_rating)+": "+movie.getVoteAverage().toString();
        // setting the userRating textView to the title and vote average
        userRatingText.setText(averageRating);
        // sets the layout manager to a Grid layout on that scrolls horizontally
        movieTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //alternate vertical layout below  - sets the layout manager to a linear layout on this activity
       //   movieTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // instantiates a new instance of the Movie trailers adapter class
        MovieTrailersAdapter movieTrailersAdapter = new MovieTrailersAdapter(trailersList, null);
        // sets the adapter to the movie trailers recycler view
        movieTrailersRecyclerView.setAdapter(movieTrailersAdapter);
        // reviews recyclerView
        MovieReviewsAdapter mReviewAdapter = new MovieReviewsAdapter(reviewsList);
        // gets an instance of the reviews adapter
        movieReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // sets the review recycler view to the review adapter
        movieReviewsRecyclerView.setAdapter(mReviewAdapter);
        // sets the collapsing toolbar image and home button
        setToolbarImage();
        // sets the rating stars value
        setStars();
        // sets the movie title
        movieTitle.setText(movie.getTitle());
        // sets the movie description
        movieOverview.setText(movie.getOverview());
        // gets the release date string and the movie release date
        String rDate = getString(R.string.movie_detail_release_date_string) + " " + movie.getReleaseDate();
        // set the movie release date
        releaseDate.setText(rDate);
        // the retrofit call the returns a list of trailer objects
        getMovieTrailers();
        // the retrofit call the returns a list of review objects
        getMovieReviews();
        // checks if there is a favorite
        if(isFavorite(movie))
        {
            // returns true, show star button
            btnFavorite.setVisibility(View.VISIBLE);
            // hide the plus button
            btn_notFavorite.setVisibility(View.GONE);
        }
        else
        {
            // if returns false, hide star button
            btnFavorite.setVisibility(View.GONE);
            // show the add to favorites button
            btn_notFavorite.setVisibility(View.VISIBLE);
        }
        //  set on click listener, on the add to favorites button
        btn_notFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // initializes a content values object to store the values for the table columns
                ContentValues myContent = new ContentValues();
                // add the movie id to the content values
                myContent.put(MovieContract.MovieFavorites.cMOVIE_ID, movie.getId());
                // adds the title to the content values
                myContent.put(MovieContract.MovieFavorites.cTITLE, movie.getTitle());
                // adds the movie overview/description to the content values
                myContent.put(MovieContract.MovieFavorites.cOVERVIEW, movie.getOverview());
                myContent.put(MovieContract.MovieFavorites.cPOSTER,movie.getPosterPath());
                // adds the movie poster path to the content values
                myContent.put(MovieContract.MovieFavorites.cBACKDROP_PATH,movie.getBackdropPath());
                // adds the movie release date to the content values
                myContent.put(MovieContract.MovieFavorites.cRELEASE_DATE,movie.getReleaseDate());
                // adds the movie vote average to the content values
                myContent.put(MovieContract.MovieFavorites.cVOTE_AVERAGE, movie.getVoteAverage());
                // retrieves a local reference to the favorites db Uri
                Uri uri = MovieContract.MovieFavorites.CONTENT_URI;
                // insert a new movie into the favorites db, could retrieve the uri of the insert
                getContentResolver().insert(uri,myContent);
                // shows favorite star after clicking add sign
                showFavoriteButton();
                // shows toast, movie added to favorites
                Toast.makeText(mContext, R.string.add_movie_to_favorites_toast_movie_detail, Toast.LENGTH_SHORT).show();
            }
        });
       /*
       *   sets an onClickListener for the share button
       */
        btnShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            //when the button is clicked
            public void onClick(View v)
            {
                if (getInternetStatus())
                {
                    // gets the first trailer key and sends it with the request
                    String trailerKey = trailersList.get(0).getKey();
                    // checks if there is a trailer before sharing
                    if (trailerKey!=null)
                    {
                        getShareIntent(trailerKey);
                    }
                }
                else
                {
                    // show a toast messages stating no internet
                    Toast.makeText(thisActivity, R.string.toast_no_internet_access,Toast.LENGTH_LONG).show();
                }
            }
        });
        /*
        *   sets an onClickListener for the favorite button
        *   if clicked it removes your movie from favorites
        */
        btnFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // hides favorite button
                btnFavorite.setVisibility(View.GONE);
                // shows add to favorites button
                btn_notFavorite.setVisibility(View.VISIBLE);
                // show a toast that movie removed from favorites
                Toast.makeText(mContext, R.string.removed_from_favorites_toast, Toast.LENGTH_SHORT).show();
                // gets the value of the movie id
                int movieId = movie.getId();
                // initializes a uri to the favorites db
                Uri uri = MovieContract.MovieFavorites.CONTENT_URI;
                // deletes the movie id from the database with the content resolver
                getContentResolver().delete(uri,"MOVIE_ID = ?", new String[]{String.valueOf(movieId)});
            }
        });
    }

    // shows favorite star after clicking add button
    private void showFavoriteButton()
    {
        // when clicked, favorite button made visible
        btnFavorite.setVisibility(View.VISIBLE);
        // the add favorite button hidden
        btn_notFavorite.setVisibility(View.GONE);
    }

    /*
    * used to share a movie trailer with email,
    * text or an app available to send a message
    */
    private void getShareIntent(String trailerKey) {

            // checks if the activity has the external storage permission and can write to it
            if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED & !isExternalStorageWritable())
            {
                // if you able to write to external storage,  share the trailer and picture
                Share(trailerKey);
            }
            else
            {
                // request permission to save the picture, if not already granted
                getPermission();
            }
    }

    //  checks if the permission to save the picture is given
    private void getPermission()
    {       // requests the permission to write to external storage
            ActivityCompat.requestPermissions((Activity) thisActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
    }

    // checks for request permission response, shows toast if denied
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         // checks if request code is for our request to write to external storage
         if(requestCode==2)
         {
             // if the first permission request is granted
             if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
             {
                 // share the trailer
                 Share(trailersList.get(0).getKey());
             }
             else
             {
                 // if not granted show a toast message saying no permission
                 Toast.makeText(thisActivity, getString(R.string.we_dont_have_permission),Toast.LENGTH_LONG).show();
             }
         }
         else
         {
             // if request code doesn't match our request, do the default behavior
             super.onRequestPermissionsResult(requestCode,permissions,grantResults);
         }
    }

    /*
    *  the method to share the move and a trailer with a friend
    */
    private void Share(String trailerKey)
    {
        // string for the saved image description
        String myTitle = movie.getTitle();
        // get the image from the MovieDetailActivity ImageView
        Drawable mDrawable = myBackGroundImage.getDrawable();
        // convert the drawable to Bitmap, needed to save the file
        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
        // create a string for the path to the saved file,
        //will be used to share the picture
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                          mBitmap, myTitle, null);
        // convert the path string to a uri in order to retrieve the image
        Uri uri = Uri.parse(path);
        // the string for the first movie trailer, shared as a clickable link
        String link = getString(R.string.you_tube_link) + trailerKey;
        // create the intent to share something
        Intent intent = new Intent(Intent.ACTION_SEND);
        // set the data shared type to any any, is that a performance issue or a security risk?
        intent.setType("*/*");
        // adds a subject, if used in the share medium
        intent.putExtra(Intent.EXTRA_SUBJECT, myTitle + getString(R.string.trailer_was_shared_movie_detail));
        // adds the clickable link to the shared message
        intent.putExtra(Intent.EXTRA_TEXT, link);
        // adds the picture of the movie poster to the message
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        // adds a chooser to the intent
        intent = Intent.createChooser(intent, "share");
        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            // starts the intent to share
            startActivity(intent);
        }
    }

    /*
    * the method that fetches the movie trailers using retrofit
    */
    private void getMovieTrailers()
    {    showLoading();
        //checks if the list of movie trailer objects exists
        if (trailersList != null)
        {
            //if there is a list, we clear the movie trailer objects from it
            trailersList.clear();
        }
        // gets an instance of retrofit client for MovieDetails
        RetrofitMovieDetailsClient client = new RetrofitMovieDetailsClient();
        //checks if there is an internet connection
        if (getInternetStatus())
        {
            // retrofit client call to get the movie trailers list
            client.getMovieDetailService().getMovieTrailers(movie.getId(),API_KEY).enqueue(new Callback<MovieTrailerListObject>()
            {
                // standard on response method
                @Override
                public void onResponse(@NonNull Call<MovieTrailerListObject> call, @NonNull Response<MovieTrailerListObject> response)
                {    // checks if there is a response
                    if (response.body() != null)
                    {   showTrailers();
                        // loads the response into the resultsList
                        trailersList = response.body().getResults();
                        //adds the resultList to the RecyclerView and adds an onClickHandler
                        //  to the movieReviewsRecyclerView
                        movieTrailersRecyclerView.setAdapter(new MovieTrailersAdapter(trailersList, getTrailersClickHandler()));
                    }
                }
                //if the retrofit call fails, we will get a reason why
                @Override
                public void onFailure(@NonNull Call<MovieTrailerListObject> call, @NonNull Throwable t)
                {
                    // logs the error message on failure
                    Log.v(LOG_TAG,getString(R.string.movie_trailers_fetch_error) +t.toString());
                }
            });
        }
    }

    /*
     * the method that fetches the movie reviews using retrofit
     */
    private void getMovieReviews()
    {
        //checks if the list of movie trailer objects exists
        if (reviewsList != null)
        {
            //if there is a list, we clear the movie trailer objects from it
            reviewsList.clear();
        }
        // gets an instance of retrofit client
        RetrofitMovieDetailsClient client = new RetrofitMovieDetailsClient();
        // checks to see if there is an internet connection
        if (getInternetStatus())
        {
            // the retrofit call to retrieve the movie reviews
            client.getMovieDetailService().getMovieReviews(movie.getId(),API_KEY).enqueue(new Callback<MovieReviewListObject>()
            {
                // the response for te call to get movie reviews
                @Override
                public void onResponse(@NonNull Call<MovieReviewListObject> call, @NonNull Response<MovieReviewListObject> response)
                {
                    // checks to see if there is a response
                    if (response.body() != null)
                    {
                        // /loads the response into the reviewsList
                        reviewsList = response.body().getResults();
                        // gets the whole object from theMovieDB.org for the
                        // total number of reviews and pages of reviews
                        reviewListObject = response.body();
                        // calls the setReviewTile method which
                        // sets the Review title and fills in the
                        // information of how many reviews and total pages
                      setReviewTitle(reviewListObject);
                        // adds the resultList to the RecyclerView
                        movieReviewsRecyclerView.setAdapter(new MovieReviewsAdapter(reviewsList));
                    }
                }
                // if the retrofit call should fail, we log the error message
                @Override
                public void onFailure(@NonNull Call<MovieReviewListObject> call, @NonNull Throwable t)
                {
                        // logs the error message of why the call didn't complete
                        Log.v(LOG_TAG,getString(R.string.movie_reviews_fetch_error) +t.toString());
                }
            });
        }
    }

    /*
    *   used to fetch image and show it in the scrolling imageView
    */
    private void setToolbarImage()
    {
        // needed to add home/back button to the collapsing toolbar
        setSupportActionBar(toolbar);
        // adds the home/up/back button to the collapsing toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initializes the collapsing toolbar tto the layout toolbar
        CollapsingToolbarLayout myCollapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        // allows the title to be placed on the collapsing toolbar
        myCollapsingToolbarLayout.setTitleEnabled(true);
        //sets the title on the collapsing toolbar to the movie title
        myCollapsingToolbarLayout.setTitle(movie.getTitle());
        // string to get the poster path from the movie object
        String Poster = movie.getBackdropPath();
        // you will need a ‘size’, which will be one of the following:
        // "w92", "w154", "w185", "w342", "w500", "w780",
        // or "original". For most phones we recommend using “w185”
        final String imageSize = getString(R.string.themoviedb_image_size);
        Log.v(LOG_TAG,"backdrop path: "+ Poster);
        //the complete poster path for Picasso to use
        final String PosterPath = getString(R.string.themoviedb_base_image_url) + imageSize + Poster;
       // Picasso.get().load(PosterPath).fetch();
        //ask Picasso to do the heavy lifting, getting the pictures and load them into image
        Picasso.get().load(PosterPath).fit().into(myBackGroundImage);

    }

    /*
     * used to set the rating stars to the correct amount
    */
    private void setStars()
    {
        //get rating from intent, which was acquired by the movie object
        //the vote_average from the movie object
        double rating = movie.getVoteAverage();
        // setting te amount of stars per rating, most of the work
        // here is done in the activity_movie_detail.xml file
        ratingBar.setRating((float) rating);
    }

    /*
     * used to set the Review Title Bar
     * and the number of reviews with the proper syntax
     * example of 3 different cases 1 review, 2 reviews, or sorry no reviews
    */
    private void setReviewTitle(MovieReviewListObject object)
    {
        // the string for the number of reviews
        String numberOfReviewsString;
        //if no reviews, hide the number of reviews textView
        // and displays no reviews
        if (object.getTotalResults()<1)
        {
            // hides the textView for number of reviews and pages
            numberReviews.setVisibility(View.GONE);
            // set the review title to no reviews
            reviewsMainTitle.setText(R.string.movie_detail_no_reviews);

        }    //if only 1 page, hides the number of pages
        else if(object.getTotalPages()==1)
        {
            // set the review title to reviews
             reviewsMainTitle.setText(R.string.movie_detail_reviews);
             // if only 1 review, sets the number of reviews to review
             if(object.getTotalResults()==1)
             {
                 // removes the "s" from reviews
                 numberOfReviewsString   = reviewListObject.getTotalResults().toString() +" Review";
                 // sets the text to 1 review
                 numberReviews.setText(numberOfReviewsString);
             }
             else
             {
                 // gets the number of reviews when more than 1 review
                 numberOfReviewsString  = reviewListObject.getTotalResults().toString() +" Reviews";
                 // sets the textView to the number of reviews
                 numberReviews.setText(numberOfReviewsString);
             }
        }
        else
        {
            // defaults to main title is reviews
            reviewsMainTitle.setText(R.string.movie_detail_reviews);
            // review string adds the number of pages to the textView
            numberOfReviewsString = reviewListObject.getTotalResults().toString() +" Reviews, page "+ reviewListObject.getPage().toString()+" of "+ reviewListObject.getTotalPages().toString();
            // sets the textView to the number of reviews and pages of reviews
            numberReviews.setText(numberOfReviewsString);
        }
    }

     //this is for the home button action to go
     // back to the previous activity results
     // and not restart the main activity
    @Override
    public void onBackPressed()
    {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/

        super.onBackPressed();
    }

    //adding activity to the manifest adds the home/back arrow
     // we add the selection of the home button to simulate the back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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

     // needed to reference the on clickHandler
    private MovieTrailersAdapter.MovieTrailersAdapterOnClickHandler getTrailersClickHandler()
    {
        //returns this for the OnClickHandler
        return this;
    }

    /*
     *  when a trailer is clicked, opens youtube or a browser to view trailer
     */
    @Override
    public void onClick(MovieTrailerObject movie)
    {
        // creates a new intent that sends a URI for a youtube video
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + movie.getKey()));
        // web intent for youtube's trailer
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movie.getKey()));
        // Verify that the intent will resolve to an activity
        if (youTubeIntent.resolveActivity(getPackageManager()) != null)
        {
            // starts the intent to watch the trailer with the you tube app
            startActivity(youTubeIntent);
        }
        else
        {
            // starts the intent to watch the trailer with a browser
            startActivity(webIntent);
        }


    }

    /*
    *  queries the favorites database to check whether the movie
    *  is in the database
    */
    private boolean isFavorite(MovieObject favMovie)
    {
        // gets a local movie id instance to use in the query
        int movieID = favMovie.getId();
        // grabs the row of movie favorites that match the movie id
        Cursor myCursor = getContentResolver()
                .query(
                MovieContract.MovieFavorites.CONTENT_URI,
                null,
                "MOVIE_ID = ?",
                new String[]{String.valueOf(movieID)},
                null);

         // test whether the cursor returned a result or not
        if(myCursor.getCount()>0)
        {
             //close the cursor
             myCursor.close();
             // return true if the query finds a match
             return true;
        }
        //close the cursor
        myCursor.close();
        // if no match found return false
        return false;
    }

    /*
     *  used to check if the phone has internet access
     */
    private boolean getInternetStatus()
    {
        // opens a dialog to the phone about its connection to the network
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // gets an instance of the NetworkInfo.class
        NetworkInfo networkInfo = null;
        // checks to see if the connectivity manager is available
        if (connectivityManager != null)
        {
            //asks the phone if it has a network connection
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        // returns true  if network information is not null and the network is connected
        // does not test internet access, it could be blocked!, but not likely...
        return (networkInfo != null && networkInfo.isConnected());
    }

    /* Checks if external storage is available for read and write
    *  used in share movie intent
    */
    private boolean isExternalStorageWritable()
    {
        // initializes a string to the state of the external storage drive, present or not
        String state = Environment.getExternalStorageState();
        // returns whether there is media mounted or not
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // shows loading indicator
    private void showLoading()
    {
        // show the loading indicator
        mProgressBar.setVisibility(View.VISIBLE);
        // hide the movie list
        movieTrailersRecyclerView.setVisibility(View.GONE);
    }

    // shows loading indicator
    private void showTrailers()
    {
        // show the loading indicator
        mProgressBar.setVisibility(View.GONE);
        // hide the movie list
        movieTrailersRecyclerView.setVisibility(View.VISIBLE);
    }


}
