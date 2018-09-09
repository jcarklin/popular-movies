package za.co.jcarklin.popularmovies.model;

import android.os.AsyncTask;
import android.view.View;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.api.JsonUtils;
import za.co.jcarklin.popularmovies.api.NetworkUtils;
import za.co.jcarklin.popularmovies.model.data.Movie;

public class FetchMoviesTaskAsyncTask extends AsyncTask<String, Void, List<Movie>> {

    private FetchMoviesAsyncTaskHandler fetchMoviesAsyncTaskHandler;

    public FetchMoviesTaskAsyncTask(FetchMoviesAsyncTaskHandler fetchMoviesAsyncTaskHandler) {
        this.fetchMoviesAsyncTaskHandler = fetchMoviesAsyncTaskHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fetchMoviesAsyncTaskHandler.setMoviesVisibility(View.INVISIBLE);
        fetchMoviesAsyncTaskHandler.setErrorVisibility(View.GONE);
        fetchMoviesAsyncTaskHandler.setProgressVisibilty(View.VISIBLE);
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        String sortBy = params[0];
        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(sortBy);
            if (movieUrl==null) {
                fetchMoviesAsyncTaskHandler.setError(R.string.network_unavailable);
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
        fetchMoviesAsyncTaskHandler.setProgressVisibilty(View.GONE);
        if (movies != null && !movies.isEmpty()) {
            fetchMoviesAsyncTaskHandler.setMovies(movies);
            fetchMoviesAsyncTaskHandler.setMoviesVisibility(View.VISIBLE);
            fetchMoviesAsyncTaskHandler.setErrorVisibility(View.GONE);
        } else {
            fetchMoviesAsyncTaskHandler.setMoviesVisibility(View.GONE);
            fetchMoviesAsyncTaskHandler.setErrorVisibility(View.VISIBLE);
        }
    }
}
