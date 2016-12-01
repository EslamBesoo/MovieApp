package com.smaz.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smaz.movieapp.Adapters.MovieAdapter;
import com.smaz.movieapp.Adapters.RecyclerTouchListener;
import com.smaz.movieapp.Model.Movie;
import com.smaz.movieapp.Utility.AppStatus;
import com.smaz.movieapp.Utility.DatabaseHandler;
import com.smaz.movieapp.Utility.MovieListener;
import com.smaz.movieapp.Utility.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private MovieAdapter adapter;
    private MovieListener mListener;

    public MainActivityFragment() {
    }



    public void setmListener(MovieListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
            if (Objects.equals(sort, "favorite")) {
                LoadFavorites();
            } else {
                try {
                    LoadJson();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error , Check your internet Connection ", Toast.LENGTH_SHORT).show();
                }
            }


        } else if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        if (Objects.equals(sort, "favorite")) {
            LoadFavorites();
        } else {
            try {
                LoadJson();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    private void LoadFavorites() {
        movieArrayList.clear();
        final DatabaseHandler db = new DatabaseHandler(getContext());
        if (db.getMoviesCount() == 0) {
            Toast.makeText(getActivity(), "No Favorite Movies", Toast.LENGTH_SHORT).show();
        }
        movieArrayList = db.getAllMovies();
        adapter = new MovieAdapter(getContext(), movieArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) throws IOException {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
                try {
                    if (Objects.equals(sort, "favorite")) {
                        Movie movie2 = db.getMovie(Integer.parseInt(movieArrayList.get(position).getMovieID()));
                        mListener.setSelectedMovie(movie2);
                    } else {
                        final Movie movie = movieArrayList.get(position);
                        mListener.setSelectedMovie(movie);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error , Check your internet Connection ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


    }


    public void LoadJson() throws MalformedURLException {
        movieArrayList.clear();
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
            final String BASE_URL =
                    "https://api.themoviedb.org/3/movie/";

            final String LANG = "language";
            final String PAGE = "page";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendEncodedPath(sort)
                    .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .appendQueryParameter(LANG, "en-US")
                    .appendQueryParameter(PAGE, "1")
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
                                    String movieName;
                                    String posterImg;
                                    String movieRelease;
                                    String imageurl;
                                    movieName = jsonPart.getString("original_title");
                                    posterImg = jsonPart.getString("poster_path");

                                    movieRelease = jsonPart.getString("release_date");
                                    String overview = jsonPart.getString("overview");
                                    String movieID = jsonPart.getString("id");
                                    String movie_vote = jsonPart.getString("vote_average");

                                    imageurl = "http://image.tmdb.org/t/p/w185" + posterImg;

                                    Movie movie = new Movie(movieID, movieName, imageurl, movieRelease, movie_vote, overview);
                                    movieArrayList.add(movie);

                                }

                                adapter = new MovieAdapter(getContext(), movieArrayList);
                                recyclerView.setAdapter(adapter);
                                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) throws IOException {
                                        final DatabaseHandler db = new DatabaseHandler(getContext());

                                        if (Objects.equals(sort, "favorite")) {
                                            Movie movie2 = db.getMovie(Integer.parseInt(movieArrayList.get(position).getMovieID()));
                                            mListener.setSelectedMovie(movie2);
                                        } else {
                                            final Movie movie = movieArrayList.get(position);
                                            mListener.setSelectedMovie(movie);
                                        }
                                    }
                                    @Override
                                    public void onLongClick(View view, int position) {
                                    }
                                }));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                        }
                    });
            MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);

        } else {
            Toast.makeText(getActivity(), "Error , Check your internet Connection ", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        if (Objects.equals(sort, "favorite")) {
            LoadFavorites();
        } else {
            try {
                LoadJson();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error , Check your internet Connection ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        super.onViewStateRestored(savedInstanceState);
    }
}
