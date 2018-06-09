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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shrekware.mypopularmovies.MainActivity;
import com.shrekware.mypopularmovies.R;
import com.shrekware.mypopularmovies.database.MovieContract;
import com.squareup.picasso.Picasso;
/*
*  the class to display the favorites adapter
*/
public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {
    // creates a cursor to retrieve the favorites information
    private Cursor myCursor;
    // constructor for the favorites list adapter
    private final FavoritesListAdapterOnClickHandler myOnClickHandler;
    //context reference
    private Context context;
   /*
   *   favorites list adapter constructor
   *
   */
    public FavoritesListAdapter(Cursor cursor, FavoritesListAdapterOnClickHandler clickHandler)
    {
        // initializing the cursor
        myCursor = cursor;
        // initializing the on click handler for the favorites list
        myOnClickHandler = clickHandler;
    }
    /*
     * the view holder to display the favorites list
     */
    @NonNull
    @Override
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  get a reference to the parent activity
        context = parent.getContext();
        // gets a layout inflater for the application
        LayoutInflater inflater = LayoutInflater.from(context);
        // sets the view to the movie item layout
        View view = inflater.inflate(R.layout.favorites_item, parent, false);
        // returns the view that will be used in the recyclerView
        return new FavoritesListAdapter.ViewHolder(view);
    }
/*
*    binds the views to the cursor data
*/
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // gets the corresponding row of data from the favorites database cursor
        myCursor.moveToPosition(position);
        // get a reference to the image in the favorites layout
        ImageView image = holder.itemView.findViewById(R.id.favorites_imageView);
        // reference to the remove button from favorites layout
        ImageButton btn_favRemoveMovie = holder.itemView.findViewById(R.id.favorites_imageButton);
        // reference to the more details button, which opens MovieDetailActivity
        Button btn_favMoreDetails = holder.itemView.findViewById(R.id.favorites_details_button);
        // reference to the movie title in the favorites layout
        TextView tv_favTitle = holder.itemView.findViewById(R.id.tv_favorites_title);
        // reference to the movie overview/description in the favorites layout
        TextView tv_favOverview = holder.itemView.findViewById(R.id.tv_favorites_overview);
        // reference to the movie release date in the favorites layout
        TextView tv_favReleaseDate = holder.itemView.findViewById(R.id.tv_favorites_release_date);
        // reference to the movie vote average in the favorites layout
        TextView tv_favVoteAverage = holder.itemView.findViewById(R.id.tv_favorites_vote_average);
        // sets an on click listener for the More Details Button in the favorites layout
        btn_favMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on the click of a recyclerView item, we retrieve the
                //movie object in the view clicked
                myCursor.moveToPosition(holder.getAdapterPosition());
                // pass the movie object to the instance of the MovieListAdapterOnClickHandler
                myOnClickHandler.onClick(myCursor);
            }
        });
        // sets an on click listener for the Remove the Movie Button in the favorites layout
        btn_favRemoveMovie.setOnClickListener
         (new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //on the click of a recyclerView item, we retrieve the
                //movie object in the view clicked
                myCursor.moveToPosition(holder.getAdapterPosition());
                // initialize a movie ID variable to the favorites movie id that was clicked
                // column 1 is the movie Id column in the favorites db
                int movieID = myCursor.getInt(1);
                // calls the delete Movie method and retrieves a new cursor with the
                // updated favorites list
                Cursor newCursor = deleteMovie(movieID);
                // calls the swap cursor method with the new cursor to update the UI screen
                swapCursor(newCursor);
                // show toast removing movie
                Toast.makeText(context, R.string.removed_from_favorites_toast, Toast.LENGTH_SHORT).show();

            }
        });
        // sets the favorites title textView to the movie row column 2/name
        tv_favTitle.setText(myCursor.getString(2));
        // sets the favorites overView textView to the movie row column 3/overview
        tv_favOverview.setText(myCursor.getString(3));
        // initializes and sets a string to the average votes from movie row column 6/voteAverage
        String voteAverage = MainActivity.resources.getString(R.string.average_user_rating) + " " + myCursor.getString(6);
        // sets the favorites vote Average textView to the string voteAverage
        tv_favVoteAverage.setText(voteAverage);
        // initializes and sets a string to the movie release date movie row column 5/release date
        String releaseDate = context.getString(R.string.release_date_favoritesListAdapter) + myCursor.getString(5);
        // sets the favorites release date text view to the string release date
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
    /*
    *    counts the size of the cursor for the adapter
    */
    @Override
    public int getItemCount()
    {
        // checks if the cursor is null or not
        if (myCursor != null)
        {
            // if its not null, return the size
            return myCursor.getCount();
        }
        else
        {
            // if its null return 0
            return 0;
        }
    }
    /*
    *    adds the  item view to the adapter
    */
    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
    /*
    *  Favorites list adapter On Click handler that accepts a cursor
    */
    public interface FavoritesListAdapterOnClickHandler
    {
        // adds the onClick method for a cursor input
        void onClick(Cursor cursor);
    }
/*
*   the method to update the favorites list recyclerView adapter with the new database information
*/
    private void swapCursor(Cursor mCursor)
    {
        // sets the adapters cursor to the incoming cursor
        myCursor = mCursor;
        // tells the adapter that the cursor has new information
        notifyDataSetChanged();
    }
    /*
    *   the method called to delete a movie from the favorites database
    */
    private Cursor deleteMovie(int movieID)
    {
        // initialize a resolver
        ContentResolver resolver = MyMovieContext.getMyContext().getContentResolver();
        // initialize a uri to the favorites database
        Uri uri = MovieContract.MovieFavorites.CONTENT_URI;
        // delete the specified row that matches the movie id, it returns an int of rows deleted
        resolver.delete(uri, "MOVIE_ID = ?", new String[]{String.valueOf(movieID)});
        // returns a new cursor of the favorites database
        return resolver.query(MovieContract.MovieFavorites.CONTENT_URI, null,
                null, null,null);
    }

}
