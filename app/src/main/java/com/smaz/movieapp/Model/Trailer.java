package com.smaz.movieapp.Model;

/**
 * Created by Eslam on 11/25/2016.
 */

public class Trailer {

    private String name;
    private String key;

    public Trailer() {

    }

    public Trailer(String m_name, String m_key) {
        this.key = m_key;
        this.name = m_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
