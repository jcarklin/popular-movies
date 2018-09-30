package za.co.jcarklin.popularmovies.repository.api;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieReview;

import static za.co.jcarklin.popularmovies.Constants.REVIEWS;

public class FetchMovieReviewsAsyncTask extends AsyncTask<Integer, Void, List<MovieReview>> {

    private AsyncTasksResponseHandler responseHandler;

    public FetchMovieReviewsAsyncTask(AsyncTasksResponseHandler fetchResponseHandler) {
        this.responseHandler = fetchResponseHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<MovieReview> doInBackground(Integer... params) {
        int movieId = params[0];
        try {
            String path = String.valueOf(movieId);
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(path, REVIEWS);
            if (movieUrl==null) {
                return null;
            }
            String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
            return JsonUtils.getInstance().processMovieReviews(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<MovieReview> movieReviews) {
        responseHandler.setMovieReviews(movieReviews);
    }
}
