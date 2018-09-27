package za.co.jcarklin.popularmovies.repository.api;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.MovieBrowserRepository;
import za.co.jcarklin.popularmovies.repository.MovieListingData;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class FetchMovieListingsAsyncTask extends AsyncTask<Integer, Void, MovieListingData> {

    FetchMoviesResponseHandler fetchMoviesResponseHandler;

    public FetchMovieListingsAsyncTask(FetchMoviesResponseHandler fetchMoviesResponseHandler) {
        this.fetchMoviesResponseHandler = fetchMoviesResponseHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MovieListingData doInBackground(Integer... params) {
        Integer sortBy = params[0];
        Integer message = null;
        List<MovieListing> movieListings = null;
        int status;
        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(sortBy == MovieBrowserRepository.SORT_BY_POPULARITY?NetworkUtils.SORT_BY_POPULARITY:NetworkUtils.SORT_BY_TOP_RATED);
            if (movieUrl==null) {
                message = R.string.network_unavailable;
                status = MovieListingData.STATUS_FAILED;
            }
            String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
            movieListings = JsonUtils.getInstance().processMovieListingResults(jsonResponse);
            status = MovieListingData.STATUS_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            message = R.string.network_unavailable;
            status = MovieListingData.STATUS_FAILED;
        }
        return new MovieListingData(movieListings,sortBy,status,message);
    }

    @Override
    protected void onPostExecute(MovieListingData movies) {
        fetchMoviesResponseHandler.setResult(movies);
    }

}
