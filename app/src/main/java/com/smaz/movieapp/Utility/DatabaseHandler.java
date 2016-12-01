package com.smaz.movieapp.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smaz.movieapp.Model.Movie;

import java.util.ArrayList;


/**
 * Created by Eslam on 11/25/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "movieapp.db";
    private static final String TABLE_MOVIES = "fav_movies";


    private static final String MOVIE_ID = "id";
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_POSTER = "poster";
    private static final String MOVIE_DATE = "date";
    private static final String MOVIE_VOTE = "vote";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_ID2 = "movie_id";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MOVIES
                + "("
                + MOVIE_ID + " INTEGER PRIMARY KEY,"
                + MOVIE_ID2 + " TEXT,"
                + MOVIE_TITLE + " TEXT,"
                + MOVIE_POSTER + " TEXT ,"
                + MOVIE_DATE + " TEXT,"
                + MOVIE_VOTE + " TEXT ,"
                + MOVIE_OVERVIEW + " TEXT" +
                ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        onCreate(db);

    }

    public void addMovie(final Movie movie, final Context context) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues values = new ContentValues();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                values.put(MOVIE_POSTER, movie.getMovieImg());
                values.put(MOVIE_TITLE, movie.getMovieName());
                values.put(MOVIE_ID2, movie.getMovieID());
                values.put(MOVIE_DATE, movie.getMovieReleaseDate());
                values.put(MOVIE_VOTE, movie.getVoteAvg());
                values.put(MOVIE_OVERVIEW, movie.getMovie_overview());
                db.insert(TABLE_MOVIES, null, values);
                db.close();

            }
        });
        thread.start();

    }

    // Getting single Movie
    public Movie getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIES, new String[]{MOVIE_ID, MOVIE_ID2,
                        MOVIE_TITLE, MOVIE_POSTER, MOVIE_DATE, MOVIE_VOTE,
                        MOVIE_OVERVIEW}, MOVIE_ID2 + "=?",
                new String[]{Integer.toString(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Movie movie = new Movie();
        movie.setMovieID(cursor.getString(1));
        movie.setMovieName(cursor.getString(2));
        movie.setMovieImg(cursor.getString(3));
        movie.setMovieReleaseDate(cursor.getString(4));
        movie.setVoteAvg(cursor.getString(5));
        movie.setMovie_overview(cursor.getString(6));

        // return Movie
        db.close();
        cursor.close();
        return movie;

    }

    // Getting All Movies
    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movieList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setMovieID(cursor.getString(1));
                movie.setMovieName(cursor.getString(2));
                movie.setMovieImg(cursor.getString(3));
                movie.setMovieReleaseDate(cursor.getString(4));
                movie.setVoteAvg(cursor.getString(5));
                movie.setMovie_overview(cursor.getString(6));
                // Adding movies to list
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        // return movies list
        return movieList;
    }

    // Getting movies Count
    public int getMoviesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MOVIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }

    // Deleting single movie
    public void deleteMovie(Movie movie) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, MOVIE_ID2 + " = ?",
                new String[]{movie.getMovieID()});
        db.close();
    }

    public boolean CheckIsDataAlreadyInDBorNot(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_MOVIES + " where " + MOVIE_ID2 + " = " + movie.getMovieID();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}