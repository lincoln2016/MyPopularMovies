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

package com.shrekware.mypopularmovies.moviedetailactivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shrekware.mypopularmovies.R;
import com.squareup.picasso.Picasso;
import java.util.List;

/*
 * this class is for the adapter layout
 * that the MovieDetail trailers recyclerView
 * uses to inflate each position
*/
public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {
    // create a List for the movieDetail objects
    final private List<MovieTrailerObject> myListOfTrailers;
    // create an ImageView called image
     private ImageView image;
    // create a MovieListAdapter OnClickHandler
    final private MovieTrailersAdapter.MovieTrailersAdapterOnClickHandler myOnClickHandler;
    //textView for the trailer title
    private TextView trailerName;
    //textView for the trailer type
    private TextView trailerType;
    // get application context
    private Context context;
    /*
     *Constructor for the movie trailers adapter
     */
    public MovieTrailersAdapter(List<MovieTrailerObject> myList, MovieTrailersAdapter.MovieTrailersAdapterOnClickHandler clickHandler  ) {
        //set the local list of Movies to the offered list
        myListOfTrailers = myList;
        // sets the local MovieListAdapter OnClickHandler to offered clickHandler
        myOnClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        // sets the view to the movie item layout
        View view = inflater.inflate(R.layout.movie_trailers_item, parent, false);
        // returns the view that will be used in the recyclerView
        return new MovieTrailersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        image = holder.itemView.findViewById(R.id.imageView_movie_trailer);
        trailerName = holder.itemView.findViewById(R.id.tv_trailer_title);
        trailerType = holder.itemView.findViewById(R.id.tv_trailer_type);
        MovieTrailerObject myTrailer = myListOfTrailers.get(position);
        trailerName.setText(myTrailer.getName());
        //string for the trailer type name and then type
        String myType = context.getResources().getString(R.string.movie_trailer_type)+myTrailer.getType();
        trailerType.setText(myType);
        String youTube =context.getString(R.string.youtube_image_url_beginning)+myTrailer.getKey()+context.getString(R.string.trailer_image_size_end);
        Picasso.get().load(youTube).placeholder(R.mipmap.loading_please_wait).into(image);

    }

    @Override
    public int getItemCount() {
        //if there are items in the list, we count them
        if (myListOfTrailers != null) {
            // return the size of the list
            return myListOfTrailers.size();
        }
        // if there is no list we return a size of 0
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(View view) {
            super(view);
            // find the image for the recyclerView trailer
            image = view.findViewById(R.id.imageView_movie_trailer);
            // find the text for the trailer title
            trailerName = view.findViewById(R.id.tv_trailer_title);
            // find the textView for the trailer type
            trailerType = view.findViewById(R.id.tv_trailer_type);
            // sets a listener for clicks on this view, which implements onClick below
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           //TODO  play trailer
            //on the click of a recyclerView item, we retrieve the
            //movie object in the view clicked
            MovieTrailerObject myTrailer = myListOfTrailers.get(getAdapterPosition());
            // pass the movie object to the instance of the MovieListAdapterOnClickHandler
            myOnClickHandler.onClick(myTrailer);


        }
    }

    public interface MovieTrailersAdapterOnClickHandler {
        void onClick(MovieTrailerObject myMovieTrailer);
    }
}



