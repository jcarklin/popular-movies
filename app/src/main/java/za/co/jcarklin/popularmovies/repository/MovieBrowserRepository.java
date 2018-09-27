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

    private MutableLiveData<MovieListingData> favouriteMoviesData = new MutableLiveData<>();
    private MutableLiveData<MovieListingData> movieListings = new MutableLiveData<>();

    private MovieBrowserRepository() {
    }

    private MovieBrowserRepository(Application application) {
        LiveData<List<MovieListing>> moviesLD = MovieBrowserDatabase.getInstance(application).movieDao().fetchFavouriteMovies();
        int status = MovieListingData.STATUS_PROCESSING;
        if (moviesLD != null) {
            status = MovieListingData.STATUS_SUCCESS;
        } else {
            status = MovieListingData.STATUS_FAILED;
        }

        favouriteMoviesData.setValue(new MovieListingData(moviesLD, SORT_BY_FAVOURITES, status, null));
        new FetchMovieListingsAsyncTask(this).execute(SORT_BY_POPULARITY);
    }

    public void refreshMovieData(int sortBy) {
        new FetchMovieListingsAsyncTask(this).execute(sortBy);
    }

    public LiveData<MovieListingData> getMovieListings() {
        return movieListings;
    }

    public LiveData<MovieListingData> getFavouriteMovies() {
        return favouriteMoviesData;
    }

    public static MovieBrowserRepository getInstance(Application application) {
        if (repository == null) {
            repository = new MovieBrowserRepository(application);
        }
        return repository;
    }

    @Override
    public void setResult(MovieListingData movies) {
        movieListings.setValue(movies);
    }
}
