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

import com.shrekware.mypopularmovies.retrofitstuff.MovieObject;
import com.shrekware.mypopularmovies.retrofitstuff.MovieListObject;
import com.shrekware.mypopularmovies.retrofitstuff.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler
{
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    // the api key from theMovieDB, it can be found in the values/strings.xml file
    private String API_KEY;
    // the results of the retrofit call to theMovieDB
    private List<MovieObject> resultsList;
    //loading indicator
    private ProgressBar mProgressBar;
    //allows resources access at interface
    public static Resources resources;
    // a reference to the recyclerView
    private RecyclerView movieListRecyclerView;
    // app context
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // load the saved state for on rotation events and reloading
        super.onCreate(savedInstanceState);
        // set layout for this view
        setContentView(R.layout.activity_main);
        // changes the title of the activity to match the type of movie list returned
        // default is popular movies
        getSupportActionBar().setTitle(R.string.title_popular_movies);
        // grab a context reference for future use
        mContext = this;
        //get a reference to the system resources, used in MovieListService to access string values
        resources = getResources();
        // the api key you get from theMovieDB to access their api
        API_KEY = resources.getString(R.string.api_key);
        // create a reference to the spinner used to indicate loading
        mProgressBar = findViewById(R.id.progressBar);
        // create a reference to recycler view widget
        movieListRecyclerView = findViewById(R.id.recyclerView_movieList);
        // sets the recycler view to a grid layout with 2 columns
        movieListRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        //asks for the popular movies from the movie.db
        getMovieJson(getString(R.string.themoviedb_sort_by_popular));
  }
  // needed to reference this on clickHandler
     public MovieListAdapter.MovieListAdapterOnClickHandler getClickHandler(){
        return this;
     }
     // gets the movie info and populates the gridView
     // based on the sort_by value
     // 2 options  Popular or Top Rated
     private void getMovieJson(String sort_by)
     {   //shows te loading indicator
        showLoading();
        //checks if results are present, and resets them
        if(resultsList != null)
        {
          //clears the movie objects from the list
          resultsList.clear();
        }
        // gets an instance of retrofit client
        RetrofitClient client = new RetrofitClient();
        // checks if internet status is true
        if (getInternetStatus())
        {
          //checks which options was sent in the request
          switch (sort_by)
          {
              //if Most Popular is selected in the options menu
              case "popular":
                // the retrofit call to retrieve the Popular movies
                client.getApiService().getPopularMovies(API_KEY).enqueue(new Callback<MovieListObject>()
                {
                  //the response of the popular movies call
                   @Override
                   public void onResponse(@NonNull Call<MovieListObject> call, @NonNull Response<MovieListObject> response)
                   {
                      //checks if the response is null
                      if (response.body() != null)
                      {
                          //closes the loading indicator and shows the recyclerView
                          showMovies();
                          //loads the response into the resultsList
                          assert response.body() != null;
                          resultsList = response.body().getResults();
                          //adds the resultList into the RecyclerView
                          movieListRecyclerView.setAdapter(new MovieListAdapter(resultsList, getClickHandler()));
                      }
                   }
                   @Override
                   public void onFailure(@NonNull Call<MovieListObject> call, @NonNull Throwable t)
                   {
                      Log.v(LOG_TAG, getString(R.string.log_error_message_popular_movie_call) + t.toString());
                   }
                });
            break;
            // if Top Rated is selected in the options menu
            case "top_rated":
                 //the retrofit call to retrieve the Top Rated movies
                 client.getApiService().getTopRatedMovies(API_KEY).enqueue(new Callback<MovieListObject>()
                 {
                    @Override
                    public void onResponse(@NonNull Call<MovieListObject> call, @NonNull Response<MovieListObject> response)
                    {
                        if (response.body() != null)
                        {
                             showMovies();
                             resultsList = response.body().getResults();
                             movieListRecyclerView.setAdapter(new MovieListAdapter(resultsList,getClickHandler()));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<MovieListObject> call, @NonNull Throwable t)
                    {
                        Log.v(LOG_TAG, getString(R.string.log_error_message_top_rated_call) + t.toString());
                    }
                 });
                // end of switch statement
             break;
          }
        }
        else
        {
             showMovies();
             Toast.makeText(this, R.string.toast_message_no_internet_access,Toast.LENGTH_LONG).show();
        }
     }
     // shows loading indicator
     private void showLoading()
     {
        //show the loading indicator
        mProgressBar.setVisibility(View.VISIBLE);
        //hide the movie list
        movieListRecyclerView.setVisibility(View.GONE);
     }
        //hides the loading indicator
     private void showMovies()
     {
        // hide the loading indicator
        mProgressBar.setVisibility(View.GONE);
        // show the movie list
        movieListRecyclerView.setVisibility(View.VISIBLE);
     }

     //checks the device to see if there is a network connection
     private boolean getInternetStatus()
     {
        // opens a dialog to the phone about its connection to the network
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        return networkInfo != null && networkInfo.isConnected();
     }
     @Override
     public boolean onCreateOptionsMenu(Menu menu)
     {
        // Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater
        MenuInflater inflater = getMenuInflater();
        // Use the inflater's inflate method to inflate our menu layout to this menu
        inflater.inflate(R.menu.options, menu);
        // Return true so that the menu is displayed in the Toolbar
        return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item)
     {
        // get the id of the menu item selected
        int id = item.getItemId();
        // parse the menu item for which item was clicked,
        // technically a switch statement might be better
        if (id == R.id.popular_movies)
        {
            getSupportActionBar().setTitle(R.string.title_popular_movies);
            getMovieJson(getString(R.string.themoviedb_sort_by_popular));
            return true;
        }
        if (id == R.id.top_rated)
        {
            getSupportActionBar().setTitle(R.string.title_top_rated_movies);
            getMovieJson(getString(R.string.themoviedb_sort_by_top_rated));
            return true;
        }
        if (id == R.id.about)
        {
            AboutDialogFragment about = new AboutDialogFragment();
            FragmentManager manager = getFragmentManager();
            about.show(manager,"about");
            return true;
        }
        return super.onOptionsItemSelected(item);
     }

     @Override
     public void onClick(MovieObject movie)
     {
        Log.v("MainActivity", "onclick Movie");
          Intent intent = new Intent(MainActivity.this,MovieDetailActivity.class);
          intent.putExtra("title", movie.getTitle());
          intent.putExtra("overview", movie.getOverview());
          intent.putExtra("poster_path", movie.getPosterPath());
          intent.putExtra("release_date", movie.getReleaseDate());
          intent.putExtra("rating", movie.getVoteAverage());

        startActivity(intent);
     }
}


