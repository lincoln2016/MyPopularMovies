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
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.shrekware.mypopularmovies.database.MovieContract;
import com.shrekware.mypopularmovies.mainactivity.FavoritesListAdapter;
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
 *  Main Class implements the onClickHandler for the MovieListAdapter
 *  and the favorites list adapter
 *  shows the results of the API call to theMovieDB.org
 */
public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler, FavoritesListAdapter.FavoritesListAdapterOnClickHandler{

    // tag for log messages
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    // the api key from theMovieDB, it can be found in the values/strings.xml file
    private String API_KEY;
    // the results of the retrofit call to theMovieDB
    private List<MovieObject> resultsList;
    // allows resources access at interface
    public static Resources resources;
    // app context
    private Context mContext;
    // string for savedInstanceState data, defines either
    // popular or top rated as the state of the activity
    private String mySavedState;
    // MovieListAdapter create
    private MovieListAdapter myAdapter;

    private Bundle mySavedBundle;
    // create a reference to the spinner used to indicate loading
    private GridLayoutManager myGridLayoutManager;

    private int myAdapterPosition;
    // loading indicator and the Bind call to mProgressBar
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    // binding the movieListRecyclerView to recyclerView View
    @BindView(R.id.recyclerView_movieList)
    RecyclerView movieListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // load the saved state for on rotation events and reloading, onStop
        super.onCreate(savedInstanceState);

        mySavedBundle = savedInstanceState;
        // set layout for this view
        setContentView(R.layout.activity_main);
        // the call to ButterKnife to bind to this activity
        ButterKnife.bind(this);
        // grab a context reference for future use
        mContext = this;
        // get a reference to the system resources, used in MovieListService to access string values
        resources = getResources();
        // the api key you get from theMovieDB to access their api
        API_KEY = resources.getString(R.string.api_key);
        // sets the recycler view to a grid layout with 2 columns
        myAdapter = new MovieListAdapter(resultsList,getClickHandler());
        // sets the number of columns dynamically
         int columns = calculateNoOfColumns(mContext);
        //  creates and instantiates a 2 column grid layout
        myGridLayoutManager = new GridLayoutManager(this, columns);
        // set recycler view to a 2 column grid
        movieListRecyclerView.setLayoutManager(myGridLayoutManager);
        // attach the adapter to the layout
        movieListRecyclerView.setAdapter(myAdapter);
        // checks is there is data available to reset the UI
        // or load the default view
        checkSavedInstanceState();
    }
    /*
    *     checks is there is data available to reset the UI
    *     or load the default view
    */
    public void checkSavedInstanceState() {
        // creates a string to use to set the title bar to the correct name
        String titleBar;
        // checks if the app has saved state data or not
        if (mySavedBundle != null) {
            // title/ supportBar string set to popular
            titleBar = getString(R.string.themoviedb_sort_by_popular);
            // if there is data saved for the previous state, restore the state of the UI
            // the adapter position is stored as an int in saved state of the layout manager
            myAdapterPosition = mySavedBundle.getInt("grid_layout_position");
            // fetch the previous state string
            mySavedState = mySavedBundle.getString("sort_by");
            // we need to set the title bar to the correct title
            // checks the sort by value and compares it to top rated
            if (mySavedState.equals(getString(R.string.themoviedb_sort_by_top_rated))) {
                // if the values are equal to top rated, set the title to top rated
                titleBar = getString(R.string.title_top_rated_movies);
                //sets the recycler view to the top rated list and adapter position
                getMovieJson(mySavedState, myAdapterPosition);
            }

            // if the values are not equal, set the title to popular
            else if (mySavedState.equals(getString(R.string.themoviedb_sort_by_popular))) {
                titleBar = getString(R.string.title_popular_movies);
                //sets the recycler view to the popular list and adapter position
                getMovieJson(mySavedState, myAdapterPosition);
            } else if (mySavedState.equals(getString(R.string.themoviedb_sort_by_favorites))) {
                //sets title bar to favorites
                titleBar = getString(R.string.title_favorites_movies);
                //sets the recycler view to the favorites list and adapter position
                getFavorites(myAdapterPosition);
            }
            // changes the title of the activity to match the type of movie list returned
            // default is popular movies
            Objects.requireNonNull(getSupportActionBar()).setTitle(titleBar);
        } else {
            //if saved instance state is null, on initial start
            mySavedState = getString(R.string.themoviedb_sort_by_popular);
            titleBar = getString(R.string.title_popular_movies);
            //sets the recycler view to the popular list
            Objects.requireNonNull(getSupportActionBar()).setTitle(titleBar);
            //sets the recycler view to the popular list and position 0
            getMovieJson(mySavedState, 0);
        }
    }
   /*
    * this will get the favorites from the database and
    * display them in the movies recycler view
   */
    private void getFavorites(int position)
    {
        // query the database for all favorites
        Cursor myCursor = getContentResolver().query(MovieContract.MovieFavorites.CONTENT_URI, null,
             null, null,null);
        // initialize the favorites adapter to the new database cursor
        FavoritesListAdapter favAdapter = new FavoritesListAdapter(myCursor, getClickHandler(myCursor));
        // set the movie recycler view to a linear layout for the favorites list
        movieListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // sets the movie recycler view data to the favorites list adapter with the cursor
        movieListRecyclerView.setAdapter(favAdapter);
        // sets the recyclerView to the last viewed position if any
        movieListRecyclerView.getLayoutManager().scrollToPosition(position);
    }
    // needed to reference this on clickHandler
    private FavoritesListAdapter.FavoritesListAdapterOnClickHandler getClickHandler(Cursor cursor)
    {
     //returns this for the OnClickHandler
         return this;
    }
    // needed to reference this on clickHandler
    private MovieListAdapter.MovieListAdapterOnClickHandler getClickHandler()
    {
        //returns this for the OnClickHandler
        return this;
    }
    // gets the movie info and populates the gridView
    // based on the sort_by value
    // 2 options  Popular or Top Rated
    private void getMovieJson(String sort_by, final int position)
    {
        // sets the saved state value to the sort by value
        mySavedState = sort_by;
        // shows the loading indicator
        showLoading();
        // checks to see if the API KEY matches the initial "Your API KEY Here" value
        // if they match, Toast pops stating missing API KEY
        isApiKeyPresent();
        // checks if the list of movie objects exists
        if (resultsList != null) {
            // if there is a list, we clear the movie objects from it
            resultsList.clear();
        }
        // gets an instance of retrofit client
        RetrofitClient client = new RetrofitClient();
        // checks if internet/network status is available
        if (getInternetStatus()) {
            // checks which option was sent with the request
            switch (sort_by) {
                // if Most Popular is selected in the options menu
                case "popular":
                    // the retrofit call to retrieve the Popular movies
                    client.getApiService().getMovies("popular",API_KEY).enqueue(new Callback<MovieListObject>()
                    {
                        // the response of the popular movies call
                        @Override
                        public void onResponse(@NonNull Call<MovieListObject> call, @NonNull Response<MovieListObject> response) {
                            // checks if the response is null
                            if (response.body() != null)
                            {
                                // closes the loading indicator and shows the recyclerView
                                showMovies();
                                // loads the response into the resultsList
                                resultsList = response.body().getResults();
                            }
                            // set recycler view to a 2 column grid
                            movieListRecyclerView.setLayoutManager(myGridLayoutManager);
                            // sets the movie recycler view data to the popular list
                            movieListRecyclerView.setAdapter(new MovieListAdapter(resultsList, getClickHandler()));
                            // sets the the adapter position to the last viewed position if any
                            movieListRecyclerView.getLayoutManager().scrollToPosition(position);
                        }
                        // if the call to theMovieDb should fail
                        @Override
                        public void onFailure(@NonNull Call<MovieListObject> call, @NonNull Throwable t)
                        {
                            // logs the error message of a failure
                            Log.d(LOG_TAG, getString(R.string.log_error_message_popular_movie_call) + t.toString());
                        }
                    });
                    break;
                // if Top Rated is selected in the options menu
                case "top_rated":
                    // the retrofit call to retrieve the Top Rated movies
                    client.getApiService().getMovies(getString(R.string.themoviedb_sort_by_top_rated),API_KEY).enqueue(new Callback<MovieListObject>()
                    {
                        // response from the top rated movies call
                        @Override
                        public void onResponse(@NonNull Call<MovieListObject> call, @NonNull Response<MovieListObject> response)
                        {
                            // checks is there was a response
                            if (response.body() != null)
                            {
                                // closes the loading indicator and shows the recyclerView
                                showMovies();
                                // loads the response into the resultsList
                                resultsList = response.body().getResults();
                            }
                            // set recycler view to a 2 column grid
                            movieListRecyclerView.setLayoutManager(myGridLayoutManager);
                            // sets the movie recycler view data to the top rated list
                            movieListRecyclerView.setAdapter(new MovieListAdapter(resultsList, getClickHandler()));
                            //sets the recyclerView to the last viewed position if there is one
                            movieListRecyclerView.getLayoutManager().scrollToPosition(position);
                        }
                        // if the call to theMovieDb should fail
                        @Override
                        public void onFailure(@NonNull Call<MovieListObject> call, @NonNull Throwable t)
                        {
                            // logs the error message of a failure
                            Log.d(LOG_TAG, getString(R.string.log_error_message_top_rated_call) + t.toString());
                        }
                    });
                    // end of switch statement
                    break;
            }
        }
        else
        {   // if no internet connection
            //set recyclerView to the empty list
            movieListRecyclerView.setAdapter(new MovieListAdapter(resultsList, getClickHandler()));
            //  we close the loading indicator and shows the recyclerView
            showMovies();
            // shows toast stating No Internet Connection
            Toast.makeText(this, R.string.toast_no_internet_access,Toast.LENGTH_LONG).show();
        }
    }
    // shows loading indicator
    private void showLoading() {
        // show the loading indicator
        mProgressBar.setVisibility(View.VISIBLE);
        // hide the movie list
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
            // hides the spinner, to indicate not searching
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
    private boolean getInternetStatus() {
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // get the id of the menu item selected
        int id = item.getItemId();
        // parse the menu item for which item was clicked,
        switch(id)
        {
            // if the menu id was popular movies
           case R.id.popular_movies:
            // set title of the actionBar to popular movies
            getSupportActionBar().setTitle(R.string.title_popular_movies);
            // makes the call that retrieves the resultsList of popular movie objects and sets position to 1
            getMovieJson(getString(R.string.themoviedb_sort_by_popular),1);
            // saves the state of the app in the mySavedState value, popular
            mySavedState= getString(R.string.themoviedb_sort_by_popular);
            // returns done
            break;
             // if the menu id was top rated
          case R.id.top_rated:
            // set title of the actionBar to top movies
            getSupportActionBar().setTitle(R.string.title_top_rated_movies);
            // makes the call that retrieves the resultsList of top rated movie objects and sets position to 1
            getMovieJson(getString(R.string.themoviedb_sort_by_top_rated),1);
            // saves the state of the app in the mySavedState value, top rated
            mySavedState = getString(R.string.themoviedb_sort_by_top_rated);
            // returns done
           break;
             // if the menu id was favorites
          case R.id.favorites:
            //set title of the actionBar to top movies
            getSupportActionBar().setTitle(R.string.title_favorites_movies);
            //makes the call that retrieves the favorites movie list and sets position to 1
            getFavorites(1);
            // saves the state of the app in the mySavedState value, favorites list
            mySavedState = getString(R.string.themoviedb_sort_by_favorites);
            // returns done
            break;
              // if the menu id was about
          case R.id.about:
            // creates an instance of the AboutDialogFragment
            AboutDialogFragment about = new AboutDialogFragment();
            // creates an instance of fragmentManager
            FragmentManager manager = getFragmentManager();
            //asks to show the about object, tells the manager its tagged as about
            about.show(manager, "about");
            //returns done
            break;
        }
       // the default behaviour
        return super.onOptionsItemSelected(item);
    }
    // when a movie is clicked, opens Movie Detail Activity
    @Override
    public void onClick(MovieObject movie)
    {
        // creates an intent that will open a Movie Detail Activity
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        // adds the movie object as a parcelable movie object
        intent.putExtra("movie", movie);
        //opens movieDetailActivity and sends the extra data
        startActivity(intent);
    }
    /*
    *   called when the activity is stopped or destroyed,
    *   save your data here, to restore in onCreate
    */
    @Override
    public void onSaveInstanceState(Bundle outState)
    {    Log.v(LOG_TAG,"onSavedInstanceState is equal to: "+ myGridLayoutManager.findFirstVisibleItemPosition());
        outState.putInt("grid_layout_position", myGridLayoutManager.findFirstVisibleItemPosition());
        // when app is saved, save the mySavedState as sort_by
        outState.putString("sort_by", mySavedState);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
    // when a favorite movie More Details is clicked,  it opens Movie Detail Activity
    @Override
    public void onClick(Cursor cursor)
    {
        // creates a movie object from the cursor details to send to the Movie Detail Activity
        MovieObject myMovie = new MovieObject(
                cursor.getInt(6),
                cursor.getInt(1),
                true,
                cursor.getDouble(6),
                cursor.getString(2),
                0.0,
                cursor.getString(4),
                "en-us",
                "",
                null,
                cursor.getString(4),
                false,
                cursor.getString(3),
                cursor.getString(5));

        // creates an intent that will open a Movie Detail Activity
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        // adds the movie object to the intent as a parcelable movie object
        intent.putExtra("movie", myMovie);
        //opens movieDetailActivity and sends the extra data
        startActivity(intent);
    }
/*
* Code given from Udacity reviewer to scale columns in grid view dynamically
*
*/
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
}


