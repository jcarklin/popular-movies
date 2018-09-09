package za.co.jcarklin.popularmovies.model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.api.JsonUtils;
import za.co.jcarklin.popularmovies.api.NetworkUtils;
import za.co.jcarklin.popularmovies.model.data.Movie;

public class FetchMoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

    public static final String SORT_BY_KEY = "sortBy";
    private List<Movie> movieList;
    private Bundle bundle;

    public FetchMoviesAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        bundle = args;
    }

    @Override
    protected void onStartLoading() {
        if (bundle == null) {
            return;
        }
        if (movieList != null) {
            deliverResult(movieList);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        String sortBy = bundle.getString(SORT_BY_KEY);
        if (sortBy==null || TextUtils.isEmpty(sortBy)) {
            return null;
        }
        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(sortBy);
            if (movieUrl==null) {
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
    public void deliverResult(@Nullable List<Movie> data) {
        movieList = data;
        super.deliverResult(data);
    }
}
