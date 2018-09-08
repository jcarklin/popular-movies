package za.co.jcarklin.popularmovies.model;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.api.JsonUtils;
import za.co.jcarklin.popularmovies.api.NetworkUtils;
import za.co.jcarklin.popularmovies.api.data.Movie;

public class FetchMoviesTaskAsyncTask extends AsyncTask<String, Void, List<Movie>> {

    FetchMoviesResponseHandler fetchMoviesResponseHandler;

    public FetchMoviesTaskAsyncTask(FetchMoviesResponseHandler fetchMoviesResponseHandler) {
        this.fetchMoviesResponseHandler = fetchMoviesResponseHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        String sortBy = params[0];
        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(sortBy);
            if (movieUrl==null) {
                fetchMoviesResponseHandler.setError(R.string.network_unavailable);
                return null;
            }
            String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
            return JsonUtils.getInstance().processMovieResults(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        fetchMoviesResponseHandler.setMovies(movies);
    }

}
