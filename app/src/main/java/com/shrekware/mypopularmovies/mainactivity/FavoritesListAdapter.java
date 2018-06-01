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

package com.shrekware.mypopularmovies.mainactivity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.shrekware.mypopularmovies.MainActivity;
import com.shrekware.mypopularmovies.MyMovieContext;
import com.shrekware.mypopularmovies.R;
import com.shrekware.mypopularmovies.database.MovieContract;
import com.squareup.picasso.Picasso;
/*
*  the class to display the favorites adapter
*/
public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {
    // create an ImageView called image to display movie poster
    private ImageView image;
    // textView for movie title
    private TextView tv_favTitle;
    //textView for movie release date
    private TextView tv_favReleaseDate;
    //textView for movie overview
    private TextView tv_favOverview;
    //textView for vote average
    private TextView tv_favVoteAverage;
    //button to add/remove from favorites
    private ImageButton btn_favAdd;
    // button to show movie details
    private Button btn_favDetails;
    // creates a cursor to retrieve the favorites information
    private Cursor myCursor;
    // constructor for the favorites list adapter
    private FavoritesListAdapterOnClickHandler myOnClickHandler;

    private MainActivity mainA;

    public FavoritesListAdapter(Cursor cursor, FavoritesListAdapterOnClickHandler clickHandler) {

        // initializing the cursor
        myCursor = cursor;

        myOnClickHandler = clickHandler;

        mainA = new MainActivity();
    }

    /*
     * the view holder to display the favorites list
     */
    @NonNull
    @Override
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  myListOfMovies.clear();
        Context context = parent.getContext();
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        // sets the view to the movie item layout
        View view = inflater.inflate(R.layout.favorites_item, parent, false);

        // returns the view that will be used in the recyclerView
        return new FavoritesListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        myCursor.moveToPosition(position);
        // get a reference to the image
        image = holder.itemView.findViewById(R.id.favorites_imageView);
        // reference to the remove from favorites button
        btn_favAdd = holder.itemView.findViewById(R.id.favorites_imageButton);
        // reference to the more details button, which opens MovieDetailActivity
        btn_favDetails = holder.itemView.findViewById(R.id.favorites_details_button);

        tv_favTitle = holder.itemView.findViewById(R.id.tv_favorites_title);

        tv_favOverview = holder.itemView.findViewById(R.id.tv_favorites_overview);

        tv_favReleaseDate = holder.itemView.findViewById(R.id.tv_favorites_release_date);

        tv_favVoteAverage = holder.itemView.findViewById(R.id.tv_favorites_vote_average);

        btn_favDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on the click of a recyclerView item, we retrieve the
                //movie object in the view clicked
                Log.v("MainActivity", "onclick for favorites list");
                myCursor.moveToPosition(holder.getAdapterPosition());
                // pass the movie object to the instance of the MovieListAdapterOnClickHandler
                myOnClickHandler.onClick(myCursor);

            }
        });

        btn_favAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCursor.moveToPosition(holder.getAdapterPosition());
                int movieID = myCursor.getInt(1);
               Cursor newCursor = deleteMovie(v,movieID);

             swapCursor(newCursor);

            }
        });


        tv_favTitle.setText(myCursor.getString(2));
        Log.v("Favorite list adapter", tv_favTitle.getText().toString());
        tv_favOverview.setText(myCursor.getString(3));
        String voteAverage = MainActivity.resources.getString(R.string.average_user_rating) + " " + myCursor.getString(6);
        tv_favVoteAverage.setText(voteAverage);
        String releaseDate = "Release Date: " + myCursor.getString(5);
        tv_favReleaseDate.setText(releaseDate);
        //you will need a ‘size’, which will be one of the following:
        //"w92", "w154", "w185", "w342", "w500", "w780",
        // or "original". For most phones we recommend using “w185”
        final String imageSize = MainActivity.resources.getString(R.string.themoviedb_image_size);
        //the complete poster path used to retrieve the image
        final String PosterPath = MainActivity.resources.getString(R.string.themoviedb_base_image_url) + imageSize + myCursor.getString(4);
        Picasso.get().load(PosterPath).placeholder(R.mipmap.loading_please_wait).fit().into(image);
        // didn't work on lollipop  no .fit??
        //TODO  add exception for Lollipop, below looks better
        // Picasso.get().load(PosterPath).placeholder(R.drawable.ic_launcher_foreground).into(image);

    }

    @Override
    public int getItemCount() {
        if (myCursor != null) {
            return myCursor.getCount();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);

        }
    }

    public interface FavoritesListAdapterOnClickHandler {
        void onClick(Cursor cursor);


    }

    public void swapCursor(Cursor mCursor){
        myCursor = mCursor;
        notifyDataSetChanged();
    }

    // called to update the recycler view adapter

    public Cursor deleteMovie(View v, int movieID){
        ContentResolver resolver = MyMovieContext.getMyContext().getContentResolver();
        Uri uri = MovieContract.MovieFavorites.CONTENT_URI;

        int deletedCount = resolver.delete(uri, "MOVIE_ID = ?", new String[]{String.valueOf(movieID)});

          // Cursor cursor = MyMovieContext.getMyContext().getContentResolver().query(uri,null, null, null);
        Cursor newCursor = resolver.query(MovieContract.MovieFavorites.CONTENT_URI, null,
                null, null,null);
            return newCursor;
    }

}
