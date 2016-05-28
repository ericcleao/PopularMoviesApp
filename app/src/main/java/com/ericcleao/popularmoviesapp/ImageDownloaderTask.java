package com.ericcleao.popularmoviesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eric Cerqueira on 27/05/2016.
 */
public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private final Movie movie;

    public ImageDownloaderTask(ImageView imageView, Movie movie) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.movie = movie;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    movie.setPoster(bitmap);
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection connection = null;
        try {
            URL urlConnection = new URL(url);
            connection= (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            connection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
