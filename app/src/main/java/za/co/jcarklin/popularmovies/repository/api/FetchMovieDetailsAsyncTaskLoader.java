package za.co.jcarklin.popularmovies.repository.api;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.net.URL;

import za.co.jcarklin.popularmovies.repository.db.MovieBrowserDatabase;
import za.co.jcarklin.popularmovies.repository.model.MovieDetails;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class FetchMovieDetailsAsyncTaskLoader extends AsyncTaskLoader<MovieDetails> {

    public static final String MOVIE_ID_KEY = "tmdbId";

    private MovieDetails movie;
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
    public MovieDetails loadInBackground() {
        Integer movieId = bundle.getInt(MOVIE_ID_KEY);
        if (movieId==null) {
            return null;
        }
        try {
            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(String.valueOf(movieId));
            if (movieUrl==null) {
                return null;
            }
            String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
            MovieDetails movie = JsonUtils.getInstance().processMovieDetails(jsonResponse);
            //check if favourite:
            MovieListing fav = movieBrowserDatabase.movieDao().getMovieByTmdbId(movieId);
            movie.setFavourite(fav != null);

            return movie;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable MovieDetails data) {
        movie = data;
        super.deliverResult(data);
    }
}
