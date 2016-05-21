package com.ericcleao.popularmoviesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eric Cerqueira on 20/05/2016.
 */
public class Movie {
    private String id, title, overview, posterPath;
    private Bitmap poster;
    private double vote;

    public Movie(String id, String title, String overview, String posterPath, double vote) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.vote = vote;
        new ImageLoadTask().execute("https://image.tmdb.org/t/p/w396" + this.posterPath);
    }

    public Bitmap getPoster() {
        return poster;
    }

    public String getId() {
        return id;
    }


    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        public ImageLoadTask() {}

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL urlConnection = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            poster = result;
        }
    }
}
