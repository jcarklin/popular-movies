package za.co.jcarklin.popularmovies.model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.net.URL;

import za.co.jcarklin.popularmovies.api.JsonUtils;
import za.co.jcarklin.popularmovies.api.NetworkUtils;
import za.co.jcarklin.popularmovies.model.data.MovieBrowserDatabase;
import za.co.jcarklin.popularmovies.model.data.MovieListing;

public class FetchMovieDetailsAsyncTaskLoader extends AsyncTaskLoader<MovieListing> {

    public static final String MOVIE_ID_KEY = "tmdbId";

    private MovieListing movie;
    private Bundle bundle;
    MovieBrowserDatabase movieBrowserDatabase;

    public FetchMovieDetailsAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        bundle = args;
        movieBrowserDatabase = MovieBrowserDatabase.getInstance(getContext().getApplicationContext());
    }

    @Override
    protected void onStartLoading() {
        if (bundle == null) {
            return;
        }
        if (movie != null) {
            deliverResult(movie);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public MovieListing loadInBackground() {
        String movieId = bundle.getString(MOVIE_ID_KEY);
        if (movieId==null || TextUtils.isEmpty(movieId)) {
            return null;
        }
        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(movieId);
            if (movieUrl==null) {
                return null;
            }
            String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
            MovieListing movie = JsonUtils.getInstance().processMovieDetails(jsonResponse);
            //check if favourite:
            MovieListing fav = movieBrowserDatabase.movieDao().getMovieByTmdbId(movieId);
            if (fav != null) {
                movie.setFaveMovieId(fav.getFaveMovieId());
            }
            return movie;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable MovieListing data) {
        movie = data;
        super.deliverResult(data);
    }
}
