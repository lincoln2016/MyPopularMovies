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
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
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
import com.shrekware.mypopularmovies.database.MovieListContract;
import com.shrekware.mypopularmovies.database.MovieListDBHelper;
import com.shrekware.mypopularmovies.retrofitstuff.MovieDetailsRetrofitObject;
import com.shrekware.mypopularmovies.retrofitstuff.MovieListRetrofitObject;
import com.shrekware.mypopularmovies.retrofitstuff.RetrofitClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //
    private String API_KEY;
    private List<MovieDetailsRetrofitObject> resultsList;
    private SQLiteDatabase mDb;
    private ProgressBar mProgressBar;
    public MovieListAdapter mAdapter;
    //allows resources access at interface
    public static Resources resources;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getMovieJson("popularity.desc");
        mAdapter.notifyDataSetChanged();
    }

    private RecyclerView movieListRecylerView;
    private Context mContext;
       private  List<MovieDetailsRetrofitObject> listMyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Popular Movies");
        // set layout for the recycle view to this view

        mContext = this;
       //get a reference to the system resources, used in MovieListServicePopular to access string values
        resources = getResources();
          API_KEY = resources.getString(R.string.api_key);

        mProgressBar = findViewById(R.id.progressBar);

        //TODO implement    createViews();
        movieListRecylerView = findViewById(R.id.recyleView_movieList);
        // create a reference to recycler view widget




        mAdapter = new MovieListAdapter(listMyList,0,mContext);
        movieListRecylerView.setLayoutManager(new GridLayoutManager(this,2));
        movieListRecylerView.setAdapter(mAdapter);

loadMovies();
        //mAdapter.reset();

        //some of the other are options below
        //vote_count.desc      vote_average.desc
        //popularity.desc      vote_average.desc
        //release_date.desc
        // more can be found at   https://developers.themoviedb.org/3/discover/movie-discover

Log.v("MainActivity","before load movies");

    }


    private void loadMovies() {
        showLoading();
        getMovieJson("popularity.desc");
        movieListRecylerView.setAdapter(mAdapter);

    }

    public void getMovieJson(String sort_by)
    {  showLoading();

      if(resultsList != null & mAdapter !=null){
          resultsList.clear();
          mAdapter.reset();
      }
      RetrofitClient client = new RetrofitClient();
      if (getInternetStatus())
      {
          client.getApiService().getAllMovies(API_KEY, "en-US",sort_by,"false","false","1").enqueue(new Callback<MovieListRetrofitObject>()
          {
             @Override
             public void onResponse(Call<MovieListRetrofitObject> call, Response<MovieListRetrofitObject> response)
             {//TODO clear adapter on new call
                 MovieDetailsRetrofitObject myMovie = null;
                  if (response.body() != null)
                  {
                      showMovies();
                     Log.v("MainActivity", "json Object: Total Pages" + response.body().getTotalPages().toString()
                     +"\n"+"Results: "+ response.body().getTotalResults().toString());
                      resultsList = response.body().getResults();
                     Log.v("MainActivity","title 5 "+ resultsList.get(5).getTitle());

                     for(int i = 0; i < resultsList.size();i++)
                     {
                        Log.v("MainActivity","value of i: "+ i);
                        myMovie = response.body().getResults().get(i);
      Log.v("MainActivity","value of myMovie: "+myMovie.getTitle());


      }
                      movieListRecylerView.setAdapter(new MovieListAdapter(resultsList,R.layout.activity_main,mContext));
                      movieListRecylerView.setVisibility(View.VISIBLE);
                      mAdapter.notifyDataSetChanged();
                  }

             }
            @Override
            public void onFailure(Call<MovieListRetrofitObject> call, Throwable t)
            {
                Log.v("MainActivity","Error on Failure of the Popular Movie Call:"+t.toString());
            }
          });
      }
      else
      {
             Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
      }



    }

    private void showLoading() {
        //hide the movie list
        movieListRecylerView.setVisibility(View.GONE);
        //show the loading indicator
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void showMovies(){
        // hide the progressBar
        mProgressBar.setVisibility(View.GONE);
        // show the loading indicator
        movieListRecylerView.setVisibility(View.VISIBLE);
      //  movieListRecylerView.setAdapter(mAdapter);
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
        // returns true  if network is connecting or connected
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.options, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.popular_movies) {
            getSupportActionBar().setTitle("Popular Movies");
            getMovieJson("popularity.desc");
            return true;
        }
        if (id == R.id.top_rated) {
            getSupportActionBar().setTitle("Top Rated Movies");
            getMovieJson("vote_count.desc");
            return true;
        }
        if (id == R.id.about) {
      //  startActivity(new Intent(this, SettingsActivity.class));
            AboutDialogFragment about = new AboutDialogFragment();
            FragmentManager manager = getFragmentManager();
            about.show(manager,"about");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}


