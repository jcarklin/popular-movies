package za.co.jcarklin.popularmovies.repository.api;

import android.os.AsyncTask;

import java.net.URL;

import za.co.jcarklin.popularmovies.repository.model.MovieDetails;

public class FetchMovieDetailsAsyncTask extends AsyncTask<Integer, Void, MovieDetails> {

    private final AsyncTasksResponseHandler fetchMovieResponseHandler;

    public FetchMovieDetailsAsyncTask(AsyncTasksResponseHandler fetchMovieResponseHandler) {
        this.fetchMovieResponseHandler = fetchMovieResponseHandler;
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
        fetchMovieResponseHandler.setMovieDetailsLiveData(movie);
    }
}
