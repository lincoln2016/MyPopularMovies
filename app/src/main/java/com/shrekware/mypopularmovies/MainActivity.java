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

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shrekware.mypopularmovies.mainactivity.MovieListAdapter;
import com.shrekware.mypopularmovies.mainactivity.MovieListObject;
import com.shrekware.mypopularmovies.mainactivity.MovieObject;
import com.shrekware.mypopularmovies.mainactivity.RetrofitClient;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
 * Main Class implements the onClickHandler for the MovieListAdapter
 * and shows the results of the API call to theMovieDB.org
 */

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {
    // tag for log messages
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    // the api key from theMovieDB, it can be found in the values/strings.xml file
    private String API_KEY;
    // the results of the retrofit call to theMovieDB
    private List<MovieObject> resultsList;
    //loading indicator and the Bind call to mProgressBar
    // create a reference to the spinner used to indicate loading
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    //allows resources access at interface
    public static Resources resources;
    // binding the movieListRecyclerView to recyclerView View
    @BindView(R.id.recyclerView_movieList)
    RecyclerView movieListRecyclerView;
    // app context
    private Context mContext;
    // string for savedInstanceState data, defines either
    // popular or top rated as the state of the activity
    private String mySavedState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // load the saved state for on rotation events and reloading, onStop
        super.onCreate(savedInstanceState);
        // checks if the app has saved data or not
        // title/ supportBar string
         String titleBar = getString(R.string.title_popular_movies);
        if (savedInstanceState != null) {
            //if there is data saved for the previous state, restore the state of the UI;
              mySavedState = savedInstanceState.getString("sort_by");
              //we need to set the title bar to the correct title
            // checks the sort by value and compares it to top rated
            if(mySavedState.equals(getString(R.string.themoviedb_sort_by_top_rated))){
                // if the values are equal to top rated, set the title to top rated
                titleBar = getString(R.string.title_top_rated_movies);}
                // if the values are not equal, set the title to popular
                else{titleBar = getString(R.string.title_popular_movies);}
            // changes the title of the activity to match the type of movie list returned
            // default is popular movies
            Objects.requireNonNull(getSupportActionBar()).setTitle(titleBar);
        }else{
            // if there is no savedState data, get default value, sort by popular
           mySavedState = getString(R.string.themoviedb_sort_by_popular);
           // set the title to the default value 'popular'
            Objects.requireNonNull(getSupportActionBar()).setTitle(titleBar);
        }
        // set layout for this view
        setContentView(R.layout.activity_main);
        // the call to ButterKnife to bind to this activity
        ButterKnife.bind(this);
        // grab a context reference for future use
        mContext = this;
        //get a reference to the system resources, used in MovieListService to access string values
        resources = getResources();
        // the api key you get from theMovieDB to access their api
        API_KEY = resources.getString(R.string.api_key);
        // sets the recycler view to a grid layout with 2 columns
        movieListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //asks for the movieDb for either popular or top rated
        //depending
        getMovieJson(mySavedState);
    }


    // needed to reference this on clickHandler
    private MovieListAdapter.MovieListAdapterOnClickHandler getClickHandler() {
        //returns this for the OnClickHandler
        return this;
    }

    // gets the movie info and populates the gridView
    // based on the sort_by value
    // 2 options  Popular or Top Rated
    private void getMovieJson(String sort_by) {   //shows te loading indicator
        mySavedState = sort_by;
        showLoading();
        // checks to see if the API KEY matches the initial "Your API KEY Here" value
        // if they match, Toast pops stating missing API KEY
        isApiKeyPresent();
        //checks if the list of movie objects exists
        if (resultsList != null) {
            //if there is a list, we clear the movie objects from it
            resultsList.clear();
        }
        // gets an instance of retrofit client
        RetrofitClient client = new RetrofitClient();
        // checks if internet/network status is available
        if (getInternetStatus()) {
            //checks which option was sent with the request
            switch (sort_by) {
                //if Most Popular is selected in the options menu
                case "popular":
                    // the retrofit call to retrieve the Popular movies
                    client.getApiService().getPopularMovies(API_KEY).enqueue(new Callback<MovieListObject>() {
                        //the response of the popular movies call
                        @Override
                        public void onResponse(@NonNull Call<MovieListObject> call, @NonNull Response<MovieListObject> response) {
                            //checks if the response is null
                            if (response.body() != null) {
                                //closes the loading indicator and shows the recyclerView
                                showMovies();
                                //loads the response into the resultsList
                                resultsList = response.body().getResults();
                                //adds the resultList to the RecyclerView
                                movieListRecyclerView.setAdapter(new MovieListAdapter(resultsList, getClickHandler()));
                            }
                        }

                        // if the call to theMovieDb should fail
                        @Override
                        public void onFailure(@NonNull Call<MovieListObject> call, @NonNull Throwable t) {

                            // logs the error message of a failure
                            Log.d(LOG_TAG, getString(R.string.log_error_message_popular_movie_call) + t.toString());
                        }
                    });
                    break;
                // if Top Rated is selected in the options menu
                case "top_rated":
                    //the retrofit call to retrieve the Top Rated movies
                    client.getApiService().getTopRatedMovies(API_KEY).enqueue(new Callback<MovieListObject>() {
                        // response from the top rated movies call
                        @Override
                        public void onResponse(@NonNull Call<MovieListObject> call, @NonNull Response<MovieListObject> response) {
                            // checks is there was a response
                            if (response.body() != null) {
                                //closes the loading indicator and shows the recyclerView
                                showMovies();
                                //loads the response into the resultsList
                                resultsList = response.body().getResults();
                                //adds the resultList to the RecyclerView
                                movieListRecyclerView.setAdapter(new MovieListAdapter(resultsList, getClickHandler()));
                            }
                        }

                        // if the call to theMovieDb should fail
                        @Override
                        public void onFailure(@NonNull Call<MovieListObject> call, @NonNull Throwable t) {
                            // logs the error message of a failure
                            Log.d(LOG_TAG, getString(R.string.log_error_message_top_rated_call) + t.toString());
                        }
                    });
                    // end of switch statement
                    break;
            }
        } else {
            //closes the loading indicator and shows the recyclerView
            showMovies();
            // shows toast stating No Internet Connection
            Toast.makeText(this, R.string.toast_message_no_internet_access, Toast.LENGTH_LONG)
                    .show();
        }
    }

    // shows loading indicator
    private void showLoading() {
        //show the loading indicator
        mProgressBar.setVisibility(View.VISIBLE);
        //hide the movie list
        movieListRecyclerView.setVisibility(View.GONE);
    }

    // checks to see if the API KEY was changed in the strings.xml file
    private void isApiKeyPresent() {
        // checks to see if the API KEY values are the same
        if (getString(R.string.api_key).equals(getString(R.string.api_test_value))) {
            // if they are the same, then there is No API KEY and we pop a toast message
            // that says Missing API KEY
            Toast.makeText(this, R.string.mainActivity_missing_api_key, Toast.LENGTH_LONG)
                    .show();
            //hides the spinner, to indicate not searching
            mProgressBar.setVisibility(View.GONE);
        }
    }

    //shows movies, hides the loading indicator
    private void showMovies() {
        // hide the loading indicator
        mProgressBar.setVisibility(View.GONE);
        // show the movie list
        movieListRecyclerView.setVisibility(View.VISIBLE);
    }

    //checks the device to see if there is a network connection
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

    // creates the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater
        MenuInflater inflater = getMenuInflater();
        // Use the inflater's inflate method to inflate our menu layout to this menu
        inflater.inflate(R.menu.options, menu);
        // Return true so that the menu is displayed in the Toolbar
        return true;
    }
    // directs the menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the id of the menu item selected
        int id = item.getItemId();
        // parse the menu item for which item was clicked,
        // technically a switch statement might be better
        if (id == R.id.popular_movies) {
            //set title of the actionBar to popular movies
            getSupportActionBar().setTitle(R.string.title_popular_movies);
            //makes the call that retrieves the resultsList of popular movie objects
           mySavedState= getString(R.string.themoviedb_sort_by_popular);
            getMovieJson(getString(R.string.themoviedb_sort_by_popular));
            // returns done
            return true;
        }
        if (id == R.id.top_rated) {
            //set title of the actionBar to top movies
            getSupportActionBar().setTitle(R.string.title_top_rated_movies);
            //makes the call that retrieves the resultsList of top rated movie objects
            getMovieJson(getString(R.string.themoviedb_sort_by_top_rated));
            mySavedState = getString(R.string.themoviedb_sort_by_top_rated);
            // returns done
            return true;
        }
        if (id == R.id.about) {
            // creates an instance of the AboutDialogFragment
            AboutDialogFragment about = new AboutDialogFragment();
            // creates an instance of fragmentManager
            FragmentManager manager = getFragmentManager();
            //asks to show the about object, tells the manager its tagged as about
            about.show(manager, "about");
            //returns done
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // when a movie is clicked
    @Override
    public void onClick(MovieObject movie) {
        // creates an intent that will open a Movie Detail Activity
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        // adds the movie object as a parcelable movie object
        intent.putExtra("movie", (Parcelable) movie);
        //opens movieDetailActivity and sends the extra data
        startActivity(intent);
    }
    // called when the activity is stopped or destroyed,
    // save your data here, to restore in onCreatte
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("sort_by", mySavedState);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}


