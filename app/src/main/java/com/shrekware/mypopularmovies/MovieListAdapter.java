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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shrekware.mypopularmovies.retrofitstuff.MovieObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
 * this class is for the adapter layout that the Main Activity recycler GridView uses to
 * inflate each movie poster and listen for a click to show the movie details
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    // create a List for the movieDetail objects
    final private List<MovieObject> myListOfMovies;
    // create an ImageView called image
    private ImageView image;
    // create a MovieListAdapter OnClickHandler
    final private MovieListAdapterOnClickHandler myOnClickHandler;

    /*
     * Constructor using the List of Movie Objects and a clickHandler
     */
    public MovieListAdapter(List<MovieObject> myList, MovieListAdapterOnClickHandler clickHandler) {
        //set the local list of Movies to the offered list
        myListOfMovies = myList;
        // sets the local MovieListAdapter OnClickHandler to offered clickHandler
        myOnClickHandler = clickHandler;
    }

    /*
     * creates the view holder
     */
    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  myListOfMovies.clear();
        Context context = parent.getContext();
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        // sets the view to the movie item layout
        View view = inflater.inflate(R.layout.movie_item_layout, parent, false);
        // returns the view that will be used in the recyclerView
        return new ViewHolder(view);
    }

    /*
     *    Overrides the binding
     *    movie info to the layout
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get a reference to the viewHolder
        image = holder.itemView.findViewById(R.id.movie_imageView);
        //obtain an instance of the movie object and get the poster path
        MovieObject myMovie = myListOfMovies.get(position);
        //you will need a ‘size’, which will be one of the following:
        //"w92", "w154", "w185", "w342", "w500", "w780",
        // or "original". For most phones we recommend using “w185”
        final String imageSize = MainActivity.resources.getString(R.string.themoviedb_image_size);
        //the complete poster path used to retrieve the image
        final String PosterPath = MainActivity.resources.getString(R.string.themoviedb_base_image_url) + imageSize + myMovie.getPosterPath();
        Picasso.get().load(PosterPath).placeholder(R.mipmap.loading_please_wait).fit().into(image);
        // didn't work on lollipop  no .fit??
        //TODO  add exception for Lollipop, below looks better
        // Picasso.get().load(PosterPath).placeholder(R.drawable.ic_launcher_foreground).into(image);
    }

    //counts the items, to keep track of size of list
    @Override
    public int getItemCount() {
        //if there are items in the list, we count them
        if (myListOfMovies != null) {
            // return the size of the list
            return myListOfMovies.size();
        }
        // if there is no list we return a size of 0
        return 0;
    }

    /*
     * Inner class to hold the views needed to display a movie object in the recycler-view
     * hold a reference to the onClickListener
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*
         *  Will display the image of the movie
         *
         *  Constructor for our ViewHolder.
         *  Within this constructor,
         *  we get a reference to our ImageView
         */
        private ViewHolder(View view) {
            super(view);
            // find the image for the recyclerView
            image = view.findViewById(R.id.movie_imageView);
            // sets a listener for clicks on this view, which implements onClick below
            view.setOnClickListener(this);
        }

        /*
         * The onClick called by the child views during a click. We fetch the movie that has been
         * selected, and then call the onClick handler registered with this adapter, passing the
         * movie object fetched.
         */
        @Override
        public void onClick(View v) {
            //on the click of a recyclerView item, we retrieve the
            //movie object in the view clicked
            MovieObject myMovie = myListOfMovies.get(getAdapterPosition());
            // pass the movie object to the instance of the MovieListAdapterOnClickHandler
            myOnClickHandler.onClick(myMovie);
        }
    }

    /*
     * The interface for the click handler
     */
    public interface MovieListAdapterOnClickHandler {
        //defines the interface methods
        void onClick(MovieObject movie);
    }
}

