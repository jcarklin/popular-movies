package za.co.jcarklin.popularmovies.repository.api;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieListing;

import static za.co.jcarklin.popularmovies.Constants.POPULARITY;
import static za.co.jcarklin.popularmovies.Constants.SORT_BY_POPULARITY;
import static za.co.jcarklin.popularmovies.Constants.TOP_RATED;

public class FetchMovieListingsAsyncTask extends AsyncTask<Integer, Void, List<MovieListing>> {

    FetchMoviesResponseHandler fetchMoviesResponseHandler;
    int sortBy;

    public FetchMovieListingsAsyncTask(FetchMoviesResponseHandler fetchMoviesResponseHandler) {
        this.fetchMoviesResponseHandler = fetchMoviesResponseHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<MovieListing> doInBackground(Integer... params) {
        sortBy = params[0];
        Integer message = null;
        List<MovieListing> movieListings = null;
        int status;

        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(sortBy == SORT_BY_POPULARITY?POPULARITY:TOP_RATED);
            if (movieUrl!=null) {
                String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
                movieListings = JsonUtils.getInstance().processMovieListingResults(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieListings;
    }

    @Override
    protected void onPostExecute(List<MovieListing> movies) {
        fetchMoviesResponseHandler.setMovies(movies, sortBy);
    }

}
