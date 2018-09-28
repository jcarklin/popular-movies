package za.co.jcarklin.popularmovies.ui.movielistings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.MovieBrowserRepository;
import za.co.jcarklin.popularmovies.repository.model.FetchStatus;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

import static za.co.jcarklin.popularmovies.Constants.SORT_BY_POPULARITY;
import static za.co.jcarklin.popularmovies.Constants.SORT_BY_TOP_RATED;

public class MovieListingsViewModel extends AndroidViewModel {

    private static final String TAG = MovieListingsViewModel.class.getSimpleName();

    private LiveData<List<MovieListing>> favouriteMovies;
    private LiveData<List<MovieListing>> popularMovies;
    private LiveData<List<MovieListing>> topRatedMovies;
    private LiveData<FetchStatus> fetchStatus;
    private int heading = R.string.popularity;

    private int sortingIndex = 0;

    private final MovieBrowserRepository movieBrowserRepository;

    public MovieListingsViewModel(@NonNull Application application) {
        super(application);
        movieBrowserRepository = MovieBrowserRepository.getInstance(application);
        favouriteMovies = movieBrowserRepository.getFavouriteMovies();
        popularMovies = movieBrowserRepository.getPopularMovies();
        topRatedMovies = movieBrowserRepository.getTopRatedMovies();
        fetchStatus = movieBrowserRepository.getFetchStatus();
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
        return fetchStatus;
    }

    public void refreshPopularMovies() {
        movieBrowserRepository.refreshPopularMovieData();
    }

    public void refreshTopRatedMovies() {
        movieBrowserRepository.refreshTopRatedMovieData();
    }

    public int getHeading() {
        switch (sortingIndex) {
            case SORT_BY_POPULARITY:
                return R.string.popularity;
            case SORT_BY_TOP_RATED:
                return R.string.rating;
            default:
                return R.string.favourite_movies;
        }
    }

}
