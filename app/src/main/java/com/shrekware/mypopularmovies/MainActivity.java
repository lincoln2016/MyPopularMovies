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
import com.shrekware.AboutDialogFragment;
import com.shrekware.mypopularmovies.retrofitstuff.MovieDetailsRetrofitObject;
import com.shrekware.mypopularmovies.retrofitstuff.MovieListRetrofitObject;
import com.shrekware.mypopularmovies.retrofitstuff.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler
{   // the api key from themoviebd, it can be found in the values/strings.xml file
    private String API_KEY;
    // the results of the retrofit call to the movie db
    private List<MovieDetailsRetrofitObject> resultsList;
    //loading indicator
    private ProgressBar mProgressBar;
    // an instance of the MovieListAdapter class, it contains
    // the instructions on how to populate the movie item layout
    public MovieListAdapter mAdapter;
    //allows resources access at interface
    public static Resources resources;
    // a reference to the recyclerView
    private RecyclerView movieListRecylerView;
    // app context
    private Context mContext;
    // the list of movie objects returned in the resultsList
    private  List<MovieDetailsRetrofitObject> listMyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         // set layout for this view
        setContentView(R.layout.activity_main);
        // changes the title of the activity to match the type of movie list returned
        getSupportActionBar().setTitle(R.string.TitlePopularMovies);
         mContext = this;
       //get a reference to the system resources, used in MovieListServicePopular to access string values
        resources = getResources();
        // the api key you get from themoviedb to access their api
          API_KEY = resources.getString(R.string.api_key);
        // create a reference to the spinner used to indicate loading
        mProgressBar = findViewById(R.id.progressBar);
        // create a reference to recycler view widget
        movieListRecylerView = findViewById(R.id.recyleView_movieList);
        // creating the movie item adapter with the required parameters

        mAdapter = new MovieListAdapter(listMyList,this,mContext);
        // sets the recycler view to a grid layout with 2 columns
        movieListRecylerView.setLayoutManager(new GridLayoutManager(this,2));
        //attaches the movie item adapter to the recycler view
        movieListRecylerView.setAdapter(mAdapter);
        //asks for the popular movies from the movie.db
        getMovieJson(getString(R.string.themoviedb_sort_by_popular));
  }
     public MovieListAdapter.MovieListAdapterOnClickHandler getClickhandler(){
        MovieListAdapter.MovieListAdapterOnClickHandler clickHandler = this;
        return clickHandler;
     }
    public void getMovieJson(String sort_by)
    {   //shows te loading indicator
        showLoading();
        //checks if results are present, and resets them
        if(resultsList != null & mAdapter !=null)
        {
          resultsList.clear();
          mAdapter.reset();
        }
        RetrofitClient client = new RetrofitClient();
        if (getInternetStatus())
        {
          switch (sort_by)
          {
            //on initial app opening and on options menu popular selected
            case "popular":
                client.getApiService().getPopularMovies(API_KEY).enqueue(new Callback<MovieListRetrofitObject>()
                {
                    @Override
                  public void onResponse(Call<MovieListRetrofitObject> call, Response<MovieListRetrofitObject> response) {//TODO clear adapter on new call
                     MovieDetailsRetrofitObject myMovie = null;
                         if (response.body() != null) {
                            showMovies();
                            resultsList = response.body().getResults();
                          //   movieListRecylerView.setAdapter(mAdapter);
                         movieListRecylerView.setAdapter(new MovieListAdapter(resultsList, getClickhandler(), mContext));
                         movieListRecylerView.setVisibility(View.VISIBLE);
                         mAdapter.notifyDataSetChanged();
                      }

                        }
                       @Override
                       public void onFailure(Call<MovieListRetrofitObject> call, Throwable t) {
                      Log.v("MainActivity", "Error on Failure of the Popular Movie Call:" + t.toString());
                     }
                });
             break;
             // if top rated in the options menu is selected
             case "top_rated":
                 client.getApiService().getTopRatedMovies(API_KEY).enqueue(new Callback<MovieListRetrofitObject>()
                 {
                     @Override
                     public void onResponse(Call<MovieListRetrofitObject> call, Response<MovieListRetrofitObject> response)
                     {//TODO clear adapter on new call
                         MovieDetailsRetrofitObject myMovie = null;
                         if (response.body() != null)
                         {
                             showMovies();
                             resultsList = response.body().getResults();
                            // movieListRecylerView.setAdapter(mAdapter);
                        movieListRecylerView.setAdapter(new MovieListAdapter(resultsList,getClickhandler() , mContext));
                             movieListRecylerView.setVisibility(View.VISIBLE);
                             mAdapter.notifyDataSetChanged();
                         }
                     }
                     @Override
                     public void onFailure(Call<MovieListRetrofitObject> call, Throwable t)
                     {
                         Log.v("MainActivity", "Error on Failure of the Top Rated Movie Call:" + t.toString());
                     }
                 });
          // end of switch statement
          break;
          }
        }
      else
      {
               showMovies();
             Toast.makeText(this,"No Internet Connection, Please try again.",Toast.LENGTH_LONG).show();
      }
    }
    // shows loading indicator
    private void showLoading()
    {
        //hide the movie list
        movieListRecylerView.setVisibility(View.GONE);
        //show the loading indicator
        mProgressBar.setVisibility(View.VISIBLE);
    }
    //hides the loading indicator
    private void showMovies(){
        // hide the loading indicator
        mProgressBar.setVisibility(View.GONE);
        // show the movie list
        movieListRecylerView.setVisibility(View.VISIBLE);
    }

    //checks the device to see if there is a network connection
    public boolean getInternetStatus()
    {
        // opens a dialog to the phone about its connection to the internet
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater
        MenuInflater inflater = getMenuInflater();
        // Use the inflater's inflate method to inflate our menu layout to this menu
        inflater.inflate(R.menu.options, menu);
        // Return true so that the menu is displayed in the Toolbar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {      // get the id of the menu item selected
        int id = item.getItemId();
        // parse the menu item for which item was clicked,
        // technically a switch statement might be better
        if (id == R.id.popular_movies)
        {
            getSupportActionBar().setTitle("Popular Movies");
            getMovieJson("popular");
            return true;
        }
        if (id == R.id.top_rated)
        {
            getSupportActionBar().setTitle("Top Rated Movies");
            getMovieJson("top_rated");
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
    public void onClick(MovieDetailsRetrofitObject movie) {
        Log.v("MainActivity", "onclick Movie");
          Intent intent = new Intent(MainActivity.this,MovieDetailActivity.class);
          intent.putExtra("title", movie.getTitle());
          intent.putExtra("overview", movie.getOverview());
          intent.putExtra("posterpath", movie.getPosterPath());
        intent.putExtra("release_date", movie.getReleaseDate());
        intent.putExtra("rating", movie.getVoteCount());
        startActivity(intent);
    }
}


