package za.co.jcarklin.popularmovies.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.api.FetchMovieListingsAsyncTask;
import za.co.jcarklin.popularmovies.repository.api.FetchMoviesResponseHandler;
import za.co.jcarklin.popularmovies.repository.db.MovieBrowserDatabase;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

import static za.co.jcarklin.popularmovies.Constants.SORT_BY_FAVOURITES;
import static za.co.jcarklin.popularmovies.Constants.SORT_BY_POPULARITY;
import static za.co.jcarklin.popularmovies.Constants.STATUS_FAILED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_PROCESSING;
import static za.co.jcarklin.popularmovies.Constants.STATUS_SUCCESS;

public class MovieBrowserRepository implements FetchMoviesResponseHandler{

    private static final String TAG = MovieBrowserRepository.class.getSimpleName();

    private static MovieBrowserRepository repository;

    private MutableLiveData<MovieListingData> favouriteMoviesData = new MutableLiveData<>();
    private MutableLiveData<MovieListingData> movieListings = new MutableLiveData<>();

    private MovieBrowserRepository() {
    }

    private MovieBrowserRepository(Application application) {
        LiveData<List<MovieListing>> favouriteMoviesLiveData = MovieBrowserDatabase.getInstance(application).movieDao().fetchFavouriteMovies();
        int status = STATUS_PROCESSING;
        if (favouriteMoviesLiveData != null) {
            status = STATUS_SUCCESS;
        } else {
            status = STATUS_FAILED;
        }
        favouriteMoviesData.setValue(new MovieListingData(favouriteMoviesLiveData, SORT_BY_FAVOURITES, status, null));
        refreshMovieData(SORT_BY_POPULARITY);
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
    public void setMovies(List<MovieListing> movies, int sortBy) {
        Integer status = STATUS_SUCCESS;
        Integer message = null;
        MutableLiveData<List<MovieListing>> moviesLiveData = null;
        if (movies != null) {
            moviesLiveData  = new MutableLiveData<>();
            moviesLiveData.setValue(movies);
        } else {
            status = STATUS_FAILED;
            message = R.string.network_unavailable;
        }
        moviesLiveData.setValue(movies);
        movieListings.setValue(new MovieListingData(moviesLiveData, sortBy, status, message));
    }
}
