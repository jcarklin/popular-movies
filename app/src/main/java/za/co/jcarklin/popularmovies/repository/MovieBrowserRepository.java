package za.co.jcarklin.popularmovies.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.api.FetchMovieListingsAsyncTask;
import za.co.jcarklin.popularmovies.repository.api.FetchMoviesResponseHandler;
import za.co.jcarklin.popularmovies.repository.db.MovieBrowserDatabase;
import za.co.jcarklin.popularmovies.repository.db.MovieDao;
import za.co.jcarklin.popularmovies.repository.model.FetchStatus;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

import static za.co.jcarklin.popularmovies.Constants.SORT_BY_POPULARITY;
import static za.co.jcarklin.popularmovies.Constants.SORT_BY_TOP_RATED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_FAILED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_PROCESSING;
import static za.co.jcarklin.popularmovies.Constants.STATUS_SUCCESS;

public class MovieBrowserRepository implements FetchMoviesResponseHandler{

    private static final String TAG = MovieBrowserRepository.class.getSimpleName();

    private static MovieBrowserRepository repository;

    private final LiveData<List<MovieListing>> favouriteMovies;
    private final MutableLiveData<List<MovieListing>> popularMovies;
    private final MutableLiveData<List<MovieListing>> topRatedMovies;
    private final MutableLiveData<FetchStatus> status;

    private MovieDao movieDao;

    private MovieBrowserRepository(Application application) {
        favouriteMovies = MovieBrowserDatabase.getInstance(application).movieDao().fetchFavouriteMovies();
        popularMovies = new MutableLiveData<>();
        topRatedMovies = new MutableLiveData<>();
        status = new MutableLiveData<>();
        refreshPopularMovieData();
    }

    public LiveData<List<MovieListing>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<MovieListing>> getTopRatedMovies() {
        return topRatedMovies;
    }

    public LiveData<List<MovieListing>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public LiveData<FetchStatus> getFetchStatus() {
        return status;
    }

    public void refreshPopularMovieData() {
        status.setValue(new FetchStatus(STATUS_PROCESSING, null));
        new FetchMovieListingsAsyncTask(this).execute(SORT_BY_POPULARITY);
    }

    public void refreshTopRatedMovieData() {
        status.setValue(new FetchStatus(STATUS_PROCESSING, null));
        new FetchMovieListingsAsyncTask(this).execute(SORT_BY_TOP_RATED);
    }

    public static MovieBrowserRepository getInstance(Application application) {
        if (repository == null) {
            repository = new MovieBrowserRepository(application);
        }
        return repository;
    }

    @Override
    public void setMovies(List<MovieListing> movies, int sortBy) {
        if (movies != null) {
            status.setValue(new FetchStatus(STATUS_SUCCESS,null));
            if (sortBy == SORT_BY_POPULARITY) {
                popularMovies.setValue(movies);
            } else {
                topRatedMovies.setValue(movies);
            }
        } else {
            status.setValue(new FetchStatus(STATUS_FAILED,R.string.network_unavailable));
        }


    }
}
