package za.co.jcarklin.popularmovies.repository.api;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class FetchMovieListingsAsyncTask extends AsyncTask<String, Void, List<MovieListing>> {

    FetchMoviesResponseHandler fetchMoviesResponseHandler;

    public FetchMovieListingsAsyncTask(FetchMoviesResponseHandler fetchMoviesResponseHandler) {
        this.fetchMoviesResponseHandler = fetchMoviesResponseHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<MovieListing> doInBackground(String... params) {
        String sortBy = params[0];
        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(sortBy);
            if (movieUrl==null) {
//                fetchMoviesResponseHandler.setError(R.string.network_unavailable);
                return null;
            }
            String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
            return JsonUtils.getInstance().processMovieListingResults(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<MovieListing> movies) {
        fetchMoviesResponseHandler.setMovies(movies);
    }

}
