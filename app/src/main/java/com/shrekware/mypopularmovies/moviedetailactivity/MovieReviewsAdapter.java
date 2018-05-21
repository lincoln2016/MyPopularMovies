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
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shrekware.mypopularmovies.R;
import java.util.List;

/*
 * this class is for the adapter layout
 * that the MovieDetail Reviews recyclerView
 * uses to inflate each position
 */
public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ViewHolder>{

    final private List<MovieReviewObject> myListOfReviews;

    public MovieReviewsAdapter(List<MovieReviewObject> listOfReviews) {
        // get a local list of reviews
        myListOfReviews=listOfReviews;


    }
    @NonNull
    @Override
    public MovieReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //
        Context context = parent.getContext();
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        // sets the view to the movie reviews layout
        View view = inflater.inflate(R.layout.movie_reviews_item, parent, false);
        // returns the view that will be used in the recyclerView
        return new MovieReviewsAdapter.ViewHolder(view);
    }
   //find layout item and set value
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView author = holder.itemView.findViewById(R.id.tv_review_author);
        TextView review = holder.itemView.findViewById(R.id.tv_review_content);
        String authorName = "Written by: " + myListOfReviews.get(position).getAuthor();
        author.setText(authorName);
        review.setText(myListOfReviews.get(position).getContent());
    }


    @Override
    public int getItemCount() {
        //if there are items in the list, we count them
        if (myListOfReviews != null) {
            // return the size of the list
            return myListOfReviews.size();
        }
        // if there is no list we return a size of 0
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);

        }
    }

}
