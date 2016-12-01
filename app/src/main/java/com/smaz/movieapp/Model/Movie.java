package com.smaz.movieapp.Model;

import java.io.Serializable;

/**
 * Created by EsLaM on 10/21/2016.
 */

public class Movie implements Serializable {

    private String movieName;
    private String movieImg;
    private String movieReleaseDate;
    private String voteAvg;
    private String movie_overview;
    private String movieID;

    public Movie() {

    }


    public Movie(String m_movieID, String m_movieName, String m_movieImg, String m_movieReleaseDate, String m_vote, String m_overview) {
        this.movieImg = m_movieImg;
        this.movieName = m_movieName;
        this.movieReleaseDate = m_movieReleaseDate;
        this.voteAvg = m_vote;
        this.movie_overview = m_overview;
        this.movieID = m_movieID;

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

    public String getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public void setMovie_overview(String movie_overview) {
        this.movie_overview = movie_overview;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

}
