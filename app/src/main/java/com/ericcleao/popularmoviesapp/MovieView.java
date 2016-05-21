package com.ericcleao.popularmoviesapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Eric Cerqueira on 20/05/2016.
 */
public class MovieView extends FrameLayout {
    ImageView posterImageView;

    public MovieView(Context context) {
        this(context, null);
    }

    public MovieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.movie_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        posterImageView = (ImageView) findViewById(R.id.movie_poster);
    }

    public static MovieView inflate(ViewGroup parent) {
        MovieView itemView = (MovieView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_view, parent, false);
        return itemView;
    }

    public void setMovie(Movie movie) {
        posterImageView.setImageBitmap(movie.getPoster());
    }
}
