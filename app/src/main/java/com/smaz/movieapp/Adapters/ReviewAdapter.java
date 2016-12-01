package com.smaz.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smaz.movieapp.Model.Review;
import com.smaz.movieapp.R;

import java.util.ArrayList;

/**
 * Created by Eslam on 11/25/2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> reviews;
    private Context context;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.review_author.setText(reviews.get(position).getReview_author());
        holder.review_content.setText(reviews.get(position).getReview_content());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView review_author;
        private TextView review_content;


        public ViewHolder(View view) {
            super(view);
            review_author = (TextView) view.findViewById(R.id.review_author);
            review_content = (TextView) view.findViewById(R.id.review_content);

        }
    }
}
