package com.smaz.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaz.movieapp.Model.Movie;
import com.smaz.movieapp.R;
import com.smaz.movieapp.Utility.AppStatus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by EsLaM on 10/21/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<Movie> MovieArray;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> Movies) {
        this.context = context;
        this.MovieArray = Movies;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.movieName.setText(MovieArray.get(position).getMovieName());
        holder.movieReleaseDate.setText(MovieArray.get(position).getMovieReleaseDate());
        if (AppStatus.getInstance(context).isOnline()) {
            Picasso.with(context).load(MovieArray.get(position).getMovieImg()).resize(150, 220).into(holder.movieImg);
        } else {
            holder.movieImg.setImageResource(R.drawable.no_poster);
        }
    }

    @Override
    public int getItemCount() {
        return MovieArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView movieName;
        private ImageView movieImg;
        private TextView movieReleaseDate;


        public ViewHolder(View view) {
            super(view);
            movieName = (TextView) view.findViewById(R.id.movieName);
            movieImg = (ImageView) view.findViewById(R.id.movieImg);
            movieReleaseDate = (TextView) view.findViewById(R.id.movieRelease);
        }

    }
}
