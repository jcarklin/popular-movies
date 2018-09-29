package za.co.jcarklin.popularmovies.repository.api;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieTrailer;

import static za.co.jcarklin.popularmovies.Constants.VIDEOS;

public class FetchMovieVideosAsyncTask extends AsyncTask<Integer, Void, List<MovieTrailer>> {

    private AsyncTasksResponseHandler responseHandler;

    public FetchMovieVideosAsyncTask(AsyncTasksResponseHandler fetchResponseHandler) {
        this.responseHandler = fetchResponseHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List doInBackground(Integer... params) {
        int movieId = params[0];
        try {
            String path = String.valueOf(movieId);
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(path, VIDEOS);
            if (movieUrl==null) {
                return null;
            }
            String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
            return JsonUtils.getInstance().processMovieTrailers(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<MovieTrailer> movieTrailers) {
        responseHandler.setMovieTrailers(movieTrailers);
    }
}
