package com.smaz.movieapp.Model;

/**
 * Created by EsLaM on 10/21/2016.
 */

public class Movie {

    private String movieName;
    private String movieImg;
    private String movieReleaseDate;

    public Movie (String m_movieName,String m_movieImg ,String m_movieReleaseDate){
        this.movieImg  = m_movieImg;
        this.movieName = m_movieName;
        this.movieReleaseDate = m_movieReleaseDate;

    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }
}
