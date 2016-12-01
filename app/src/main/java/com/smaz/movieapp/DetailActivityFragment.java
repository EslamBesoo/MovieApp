package com.smaz.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smaz.movieapp.Adapters.RecyclerTouchListener;
import com.smaz.movieapp.Adapters.ReviewAdapter;
import com.smaz.movieapp.Adapters.TrailerAdapter;
import com.smaz.movieapp.Model.Movie;
import com.smaz.movieapp.Model.Review;
import com.smaz.movieapp.Model.Trailer;
import com.smaz.movieapp.Utility.AppStatus;
import com.smaz.movieapp.Utility.DatabaseHandler;
import com.smaz.movieapp.Utility.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    Movie movie;
    Boolean movie_isFav;
    private TextView movie_title, movie_date, movie_vote, movie_overview;
    private ImageView movie_poster;
    private FloatingActionButton fab;
    private RecyclerView recyclerView, recyclerView_reviews;
    private ArrayList<Trailer> trailerArrayList = new ArrayList<>();
    private TrailerAdapter adapter;
    private ArrayList<Review> reviewArrayList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private DatabaseHandler db;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle sentBundle = getArguments();

        movie = (Movie) sentBundle.getSerializable("movie");
        movie_title = (TextView) rootview.findViewById(R.id.movie_title);
        movie_date = (TextView) rootview.findViewById(R.id.movie_date);
        movie_vote = (TextView) rootview.findViewById(R.id.movie_vote);
        movie_overview = (TextView) rootview.findViewById(R.id.movie_overview);
        movie_poster = (ImageView) rootview.findViewById(R.id.movie_poster);
        fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        db = new DatabaseHandler(getActivity());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movie_isFav) {
                    db = new DatabaseHandler(getContext());
                    db.addMovie(movie, getContext());

                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    movie_isFav = true;
                } else {
                    db = new DatabaseHandler(getContext());

                    fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    db.deleteMovie(movie);
                    movie_isFav = false;
                }
            }
        });
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView_reviews = (RecyclerView) rootview.findViewById(R.id.recyclerView_reviews);
        recyclerView_reviews.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager_reviews = new LinearLayoutManager(getActivity());
        recyclerView_reviews.setLayoutManager(layoutManager_reviews);


        movie_title.setText(movie.getMovieName());
        movie_date.setText("Release Date: " + movie.getMovieReleaseDate());
        movie_vote.setText("Avg Vote: " + movie.getVoteAvg() + " / 10");
        movie_overview.setText(movie.getMovie_overview());
        this.getActivity().setTitle(movie.getMovieName());

        if (AppStatus.getInstance(getActivity()).isOnline()) {
            Picasso.with(getActivity()).load(movie.getMovieImg()).resize(150, 220).into(movie_poster);
        } else {
            movie_poster.setImageResource(R.drawable.no_poster);
        }

        if (db.CheckIsDataAlreadyInDBorNot(movie)) {
            movie_isFav = true;
            fab.setImageResource(R.drawable.ic_favorite_black_24dp);

        } else {
            movie_isFav = false;
            fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        return rootview;
    }

    @Override
    public void onStart() {
        LoadTrailers();
        LoadReviews();
        super.onStart();
    }

    public void LoadTrailers() {
        final String movie_id = movie.getMovieID();
        final String BASE_URL =
                "https://api.themoviedb.org/3/movie/" + movie_id + "/videos?";

        final String API_KEY = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build();

        String url2 = builtUri.toString();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String data = response.getString("results");
                            JSONArray array1 = new JSONArray(data);

                            for (int i = 0; i < array1.length(); i++) {
                                JSONObject jsonPart = array1.getJSONObject(i);
                                String trailerName;
                                String trailerKey;
                                String videoUrl;
                                trailerKey = jsonPart.getString("key");
                                trailerName = jsonPart.getString("name");
                                videoUrl = "https://www.youtube.com/watch?v=" + trailerKey;
                                Trailer trailer = new Trailer(trailerName, videoUrl);
                                trailerArrayList.add(trailer);
                                adapter = new TrailerAdapter(getContext(), trailerArrayList);
                                recyclerView.setAdapter(adapter);
                                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        Trailer trailer1 = trailerArrayList.get(position);
                                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer1.getKey()));
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }
                                }));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error , Check your internet Connection for loading Trailers ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);

    }

    public void LoadReviews() {
        final String movie_id = movie.getMovieID();
        final String BASE_URL =
                "https://api.themoviedb.org/3/movie/" + movie_id + "/reviews?";
        final String API_KEY = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build();

        String url2 = builtUri.toString();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String data = response.getString("results");
                            JSONArray array1 = new JSONArray(data);

                            for (int i = 0; i < array1.length(); i++) {
                                JSONObject jsonPart = array1.getJSONObject(i);
                                String review_author;
                                String review_content;
                                review_author = jsonPart.getString("author");
                                review_content = jsonPart.getString("content");
                                String review_url = jsonPart.getString("url");
                                Review review = new Review(review_author, review_content, review_url);
                                reviewArrayList.add(review);
                                reviewAdapter = new ReviewAdapter(getContext(), reviewArrayList);
                                recyclerView_reviews.setAdapter(reviewAdapter);
                                recyclerView_reviews.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView_reviews, new RecyclerTouchListener.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        Review review1 = reviewArrayList.get(position);
                                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(review1.getReview_url()));
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }
                                }));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error , Check your internet Connection for Loading Reviews ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);

    }
}

