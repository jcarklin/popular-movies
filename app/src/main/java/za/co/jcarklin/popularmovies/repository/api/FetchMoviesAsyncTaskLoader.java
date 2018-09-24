package za.co.jcarklin.popularmovies.repository.api;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.net.URL;
import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class FetchMoviesAsyncTaskLoader extends AsyncTaskLoader<List<MovieListing>> {

    public static final String SORT_BY_KEY = "sortBy";
    private List<MovieListing> movieList;
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
    public List<MovieListing> loadInBackground() {
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
            return JsonUtils.getInstance().processMovieListingResults(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable List<MovieListing> data) {
        movieList = data;
        super.deliverResult(data);
    }
}
