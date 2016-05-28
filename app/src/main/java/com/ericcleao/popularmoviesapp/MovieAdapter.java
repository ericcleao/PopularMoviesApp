package com.ericcleao.popularmoviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Eric Cerqueira on 20/05/2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context c, List<Movie> movies) {
        super(c, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieView movieView = (MovieView)convertView;
        if (null == movieView) {
            movieView = MovieView.inflate(parent);
        }
        movieView.setMovie(getItem(position));
        return movieView;
    }

    public void add(String movie) {

    }
}
