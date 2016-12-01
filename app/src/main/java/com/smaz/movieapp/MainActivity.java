package com.smaz.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.smaz.movieapp.Model.Movie;
import com.smaz.movieapp.Utility.MovieListener;


public class MainActivity extends AppCompatActivity implements MovieListener {
    Boolean misTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            MainActivityFragment mainFrag = new MainActivityFragment();
            mainFrag.setmListener(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mainFrag)
                    .commit();

        }

        misTwoPane = null != findViewById(R.id.fDetail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {

        }

        return super.onOptionsItemSelected(item);
    }

    public void setSelectedMovie(Movie movie) {
        if (!misTwoPane) {

            Intent i = new Intent(this, DetailActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable("movie", movie);
            i.putExtra("movie", movie);
            startActivity(i);
        } else {
            DetailActivityFragment mDetailsFragment = new DetailActivityFragment();
            Bundle extras = new Bundle();
            extras.putSerializable("movie", movie);
            mDetailsFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.fDetail, mDetailsFragment, "").commit();
        }

    }
}
