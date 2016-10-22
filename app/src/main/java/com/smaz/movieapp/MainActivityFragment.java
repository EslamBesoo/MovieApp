package com.smaz.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smaz.movieapp.Adapters.MovieAdapter;
import com.smaz.movieapp.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.smaz.movieapp.R.id.recyclerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;

    public MainActivityFragment() {
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
        if(id == R.id.action_refresh){

            try {
                LoadJson();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


        }
        else if(id == R.id.action_settings){
            startActivity(new Intent(getActivity(), SettingsActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            LoadJson();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void LoadJson() throws MalformedURLException {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));

        final String BASE_URL =
                "https://api.themoviedb.org/3/movie/";


        final String LANG = "language";
        final String PAGE = "page";
        final String API_KEY = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(sort)
                .appendQueryParameter(API_KEY, "f2510ee9fba3eb688842e6a2e12eb3da")
                .appendQueryParameter(LANG, "en-US")
                .appendQueryParameter(PAGE, "1")
                .build();

        String url2 = builtUri.toString();
        Log.i("URL is :",url2);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String data = response.getString("results");
                            JSONArray array1 = new JSONArray(data);
                            ArrayList<Movie> movieArrayList = new ArrayList<>();
                            for (int i = 0; i < array1.length(); i++) {
                                JSONObject jsonPart = array1.getJSONObject(i);
                                String movieName;
                                String posterImg;
                                String movieRelease;
                                String imageurl;
                                movieName = jsonPart.getString("original_title");
                                posterImg = jsonPart.getString("poster_path");

                                movieRelease = jsonPart.getString("release_date");



                                imageurl ="http://image.tmdb.org/t/p/w185"+posterImg;

                                Log.i("new Image URL is : ",imageurl);
                                Movie movie = new Movie(movieName,imageurl,movieRelease);
                                movieArrayList.add(movie);

                            }

                            adapter = new MovieAdapter(getContext(), movieArrayList);
                            recyclerView.setAdapter(adapter);


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
    }
}
