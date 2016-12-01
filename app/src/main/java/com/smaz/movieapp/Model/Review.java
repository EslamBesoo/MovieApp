package com.smaz.movieapp.Model;

/**
 * Created by Eslam on 11/25/2016.
 */

public class Review {

    private String review_author;
    private String review_content;
    private String review_url;

    public Review() {

    }

    public Review(String m_author, String m_content, String m_url) {
        this.review_author = m_author;
        this.review_content = m_content;
        this.review_url = m_url;
    }

    public String getReview_author() {
        return review_author;
    }

    public void setReview_author(String review_author) {
        this.review_author = review_author;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public String getReview_url() {
        return review_url;
    }

    public void setReview_url(String review_url) {
        this.review_url = review_url;
    }
}
