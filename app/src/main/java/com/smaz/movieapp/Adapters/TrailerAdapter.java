package com.smaz.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaz.movieapp.Model.Trailer;
import com.smaz.movieapp.R;

import java.util.ArrayList;

/**
 * Created by Eslam on 11/25/2016.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private ArrayList<Trailer> trailers;
    private Context context;

    public TrailerAdapter(Context context, ArrayList<Trailer> m_trailers) {
        this.trailers = m_trailers;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.trailerName.setText(trailers.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView trailerName;
        private ImageView trailerImg;


        public ViewHolder(View view) {
            super(view);
            trailerName = (TextView) view.findViewById(R.id.trailer_name);
            trailerImg = (ImageView) view.findViewById(R.id.play);

        }
    }
}
