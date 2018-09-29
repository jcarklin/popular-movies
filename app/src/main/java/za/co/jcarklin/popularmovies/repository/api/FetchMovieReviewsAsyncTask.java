package za.co.jcarklin.popularmovies.repository.api;

import android.os.AsyncTask;

import java.net.URL;

import za.co.jcarklin.popularmovies.repository.db.MovieDao;
import za.co.jcarklin.popularmovies.repository.model.MovieDetails;

public class FetchMovieReviewsAsyncTask extends AsyncTask<Integer, Void, MovieDetails> {

    private FetchMovieDetailsResponseHandler fetchMovieResponseHandler;
    private MovieDao movieDao;

    public FetchMovieReviewsAsyncTask(FetchMovieDetailsResponseHandler fetchMovieResponseHandler, MovieDao movieDao) {
        this.fetchMovieResponseHandler = fetchMovieResponseHandler;
        this.movieDao = movieDao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MovieDetails doInBackground(Integer... params) {
        int movieId = params[0];
        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(String.valueOf(movieId));
            if (movieUrl==null) {
                return null;
            }
            String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
            return JsonUtils.getInstance().processMovieDetails(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieDetails movie) {
        fetchMovieResponseHandler.setMovie(movie);
    }

    public interface FetchMovieDetailsResponseHandler {
        void setMovie(MovieDetails movie);
    }
}
