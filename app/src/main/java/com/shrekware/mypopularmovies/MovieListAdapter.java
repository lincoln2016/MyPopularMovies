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
/**
 * this class is for the adapter layout that the recyclerView uses to inflate each position
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>
{
   // get an int for the movieDetail object
   final private List<MovieObject> myListOfMovies;
   private ImageView image;
   final private MovieListAdapterOnClickHandler myOnClickHandler;
   /*
   * Constructor using the List of Movie Objects and a clickHandler
   */
   public MovieListAdapter(List<MovieObject> myList, MovieListAdapterOnClickHandler clickHandler)
   {
       myListOfMovies = myList;
       myOnClickHandler = clickHandler;
   }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
     //  myListOfMovies.clear();
         Context context = parent.getContext();
         // Get the RecyclerView item layout
         LayoutInflater inflater = LayoutInflater.from(context);
         View view = inflater.inflate(R.layout.movie_item_layout, parent,false);
         return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        // get a reference to the viewHHolder
        image = holder.itemView.findViewById(R.id.movie_imageView);
        MovieObject myMovie = myListOfMovies.get(position);
        //you will need a ‘size’, which will be one of the following:
        //"w92", "w154", "w185", "w342", "w500", "w780",
        // or "original". For most phones we recommend using “w185”
        final String imageSize = "w185/";
        final String PosterPath = "http://image.tmdb.org/t/p/"+ imageSize + myMovie.getPosterPath();
        Picasso.get().load(PosterPath).placeholder(R.mipmap.loading_please_wait).fit().into(image);
        // didn't work on lollipop  no fit??
        // Picasso.get().load(PosterPath).placeholder(R.drawable.ic_launcher_foreground).into(image);
    }
       //must have, to keep track of size of list
    @Override
    public int getItemCount()
    {
        if(myListOfMovies != null){
            return myListOfMovies.size();
        }
        return 0;
    }
    //must have to reset the list to 0
    public void reset()
    {
        if(myListOfMovies!= null)
        {
            myListOfMovies.clear();
        }
    }
    /**
     * Inner class to hold the views needed to display a movie item view in the recycler-view
     */

    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Will display the image of the movie
        /*
         * Constructor for our ViewHolder.
         * Within this constructor,
         * we get a reference to our ImageView
         */

        private ViewHolder(View view)
        {
            super(view);
            //the image for the recyclerView
            image = view.findViewById(R.id.movie_imageView);
            // sets a listener for a click, which implements onClick below
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            MovieObject myMovie = myListOfMovies.get(getAdapterPosition());
           myOnClickHandler.onClick(myMovie);
        }



    }
    /**
     * The interface that receives onClick messages.
     */
    public interface MovieListAdapterOnClickHandler {
        void onClick(MovieObject movie);
    }
    /*
     * This gets called by the child views during a click. We fetch the movie that has been
     * selected, and then call the onClick handler registered with this adapter, passing that
     * movie object.
     *
     */


}

