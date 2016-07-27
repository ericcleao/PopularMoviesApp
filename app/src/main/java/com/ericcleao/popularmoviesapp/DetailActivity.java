package com.ericcleao.popularmoviesapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView titleTextView, voteTextView, overviewTextView;
    ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setupViews();
        Movie movie = getMovieFromBudle(getIntent().getBundleExtra("movie"));
        updateText(movie);

        toolbar.setTitle(movie.getTitle());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateText(Movie movie) {
        titleTextView.setText(movie.getTitle());
        voteTextView.setText("Vote: " + movie.getVote());
        overviewTextView.setText(movie.getOverview());
    }

    private void setupViews() {
        titleTextView = (TextView) findViewById(R.id.title_detail);
        voteTextView = (TextView) findViewById(R.id.vote_detail);
        overviewTextView = (TextView) findViewById(R.id.overview_detail);
        posterImageView = (ImageView) findViewById(R.id.poster_detail);
    }

    public Movie getMovieFromBudle(Bundle movieBundle){
        Movie movie = new Movie(movieBundle.getString("id"), movieBundle.getString("title"),
                movieBundle.getString("overview"), movieBundle.getString("posterPath"), movieBundle.getDouble("vote"));
        new ImageDownloaderTask(posterImageView, movie).execute("https://image.tmdb.org/t/p/w396" + movie.getPosterPath());
        return movie;
    }
}
