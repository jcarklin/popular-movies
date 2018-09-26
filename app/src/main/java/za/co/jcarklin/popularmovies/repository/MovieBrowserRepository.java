package za.co.jcarklin.popularmovies.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import za.co.jcarklin.popularmovies.repository.api.FetchMovieListingsAsyncTask;
import za.co.jcarklin.popularmovies.repository.api.FetchMoviesResponseHandler;
import za.co.jcarklin.popularmovies.repository.api.NetworkUtils;
import za.co.jcarklin.popularmovies.repository.db.MovieBrowserDatabase;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class MovieBrowserRepository implements FetchMoviesResponseHandler {

    private static final String TAG = MovieBrowserRepository.class.getSimpleName();
    public static final int SORT_BY_POPULARITY = 0;
    public static final int SORT_BY_TOP_RATED = 1;
    public static final int SORT_BY_FAVOURITES = 2;


    private static MovieBrowserRepository repository;

    private List<MovieListing> popularMovieListingsCache;
    private List<MovieListing> topRatedMovieListingsCache;
    private LiveData<List<MovieListing>> favouriteMovies;

    private MutableLiveData<List<MovieListing>> movieListings = new MutableLiveData<>();


    private MovieBrowserRepository() {
    }

    private MovieBrowserRepository(Application application) {
        favouriteMovies = MovieBrowserDatabase.getInstance(application).movieDao().fetchFavouriteMovies();
        setMovieListings(SORT_BY_POPULARITY, true);
    }

    public LiveData<List<MovieListing>> getMovieListings() {
        return movieListings;
    }

    public LiveData<List<MovieListing>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setMovieListings(int sortBy, boolean refresh) {
        switch (sortBy) {
            case SORT_BY_POPULARITY:
                if (popularMovieListingsCache != null && !refresh) {
                    movieListings.setValue(popularMovieListingsCache);
                } else {
                    new FetchMovieListingsAsyncTask(this)
                            .execute(NetworkUtils.SORT_BY_POPULARITY);
                }
                break;
            case SORT_BY_TOP_RATED:
                if (topRatedMovieListingsCache != null && !refresh) {
                    movieListings.setValue(topRatedMovieListingsCache);
                } else {
                    new FetchMovieListingsAsyncTask(this)
                            .execute(NetworkUtils.SORT_BY_TOP_RATED);
                }
        }
    }

    public static MovieBrowserRepository getInstance(Application application) {
        if (repository == null) {
            repository = new MovieBrowserRepository(application);
        }
        return repository;
    }


    @Override
    public void setMovies(List<MovieListing> movies) {
        movieListings.setValue(movies);
    }

    @Override
    public void setError(int errorResourceId) {

    }
}
