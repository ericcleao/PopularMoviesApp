package com.ericcleao.popularmoviesapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Eric Cerqueira on 20/05/2016.
 */
public class MovieView extends FrameLayout {
    ImageView posterImageView;
    Movie movie;

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
        this.movie = movie;
       if (movie.getPoster() == null) {
           new ImageDownloaderTask(posterImageView, movie).execute("https://image.tmdb.org/t/p/w396" + movie.getPosterPath());
       }
        else
            posterImageView.setImageBitmap(movie.getPoster());
    }
}
