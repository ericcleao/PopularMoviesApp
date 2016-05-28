package com.ericcleao.popularmoviesapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    MovieAdapter mPopularMoviesAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<Movie> popularMovies = new ArrayList<Movie>();

        mPopularMoviesAdapter = new MovieAdapter(getActivity(), popularMovies);

        final GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(mPopularMoviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(this.getClass().getSimpleName(), position + "");
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchPopularMoviesTask popularMoviesTask = new FetchPopularMoviesTask();
        popularMoviesTask.execute();
    }

    private class FetchPopularMoviesTask extends AsyncTask<Void, Void, Movie[]> {
        private final String LOG_TAG = FetchPopularMoviesTask.class.getSimpleName();
        private String language;

        public FetchPopularMoviesTask() {
            this.language = Locale.getDefault().getLanguage();
        }

        @Override
        protected Movie[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String type = preferences.getString(getString(R.string.pref_type_key), getString(R.string.pref_type_default));
            int numPosters = Integer.valueOf(preferences.getString(getString(R.string.pref_numPoster_key),getString(R.string.pref_numPoster_default)));
            String apiKey = "fd8810612013a8d561d6243ca12f9975";
            int page = ((numPosters - 1) / 20) + 1;
            String language = this.language;

            String[] popularMoviesJsonStr = new String[page];

            try {
                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/" + type + "?";
                final String API_KEY_PARAM = "api_key";
                final String PAGE_PARAM = "page";
                final String LANGUAGE_PARAM = "language";

                for (int i = 1; i <= page; ++i) {
                    Uri uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                            .appendQueryParameter(API_KEY_PARAM, apiKey)
                            .appendQueryParameter(PAGE_PARAM, Integer.toString(i))
                            .appendQueryParameter(LANGUAGE_PARAM, language)
                            .build();

                    URL url = new URL(uri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();// Read the input stream into a String

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        return null;
                    }
                    popularMoviesJsonStr[i - 1] = buffer.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getPopularMoviesDataFromJson(popularMoviesJsonStr, numPosters);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies != null) {
                mPopularMoviesAdapter.clear();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mPopularMoviesAdapter.addAll(movies);
                } else {
                    for (Movie movie :
                            movies) {
                        mPopularMoviesAdapter.add(movie);
                    }
                }

            }
            super.onPostExecute(movies);
        }

        private Movie[] getPopularMoviesDataFromJson(String[] popularMoviesJsonStr,
                                                     int numPosters)
                throws JSONException {

            int page = ((numPosters - 1) / 20) + 1;

            final String OWM_RESULTS = "results";
            final String OWM_TITLE = "title";
            final String OWM_VOTE = "vote_average";
            final String OWM_OVERVIEW = "overview";
            final String OWM_MOVIEID = "id";
            final String OWM_POSTER = "poster_path";

            Movie[] movies = new Movie[numPosters];

            for (int i = 0; i < page; ++i) {
                JSONObject popularMoviesJson = new JSONObject(popularMoviesJsonStr[i]);
                JSONArray resultsArray = popularMoviesJson.getJSONArray(OWM_RESULTS);

                int aux;
                if (i < page - 1) {
                    aux = 20;
                } else {
                    aux = numPosters - i * 20;
                }

                for (int j = 0; j < aux; j++) {
                    // For now, using the format "Day, description, hi/low"
                    String title;
                    double vote;
                    String overview;
                    String id;
                    String posterPath;

                    // Get the JSON object representing the day
                    JSONObject movie = resultsArray.getJSONObject(j);

                    title = movie.getString(OWM_TITLE);

                    vote = movie.getDouble(OWM_VOTE);

                    overview = movie.getString(OWM_OVERVIEW);

                    id = movie.getString(OWM_MOVIEID);

                    posterPath = movie.getString(OWM_POSTER);

                    movies[i * 20 + j] = new Movie(id, title, overview, posterPath, vote);
                }
            }

            return movies;
        }
    }
}
