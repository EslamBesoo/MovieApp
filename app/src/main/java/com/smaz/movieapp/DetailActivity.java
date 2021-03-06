package com.smaz.movieapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();
        if (savedInstanceState == null) {
        DetailActivityFragment detailFrag = new DetailActivityFragment();
        detailFrag.setArguments(sentBundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_detail, detailFrag,"")
                .commit();}

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
//        super.onBackPressed();
    }
}
