package za.co.jcarklin.popularmovies.ui.movielistings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.MovieBrowserRepository;
import za.co.jcarklin.popularmovies.repository.MovieListingData;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

import static za.co.jcarklin.popularmovies.Constants.SORT_BY_POPULARITY;
import static za.co.jcarklin.popularmovies.Constants.SORT_BY_TOP_RATED;

public class MovieListingsViewModel extends AndroidViewModel {

    private static final String TAG = MovieListingsViewModel.class.getSimpleName();

    private List<MovieListing> favouriteMovies;
    private List<MovieListing> popularMovies;
    private List<MovieListing> topRatedMovies;
    private int heading = R.string.popularity;

    private int sortingIndex = 0;

    private final MovieBrowserRepository movieBrowserRepository;
    private final LiveData<MovieListingData> movieListingsLiveData;
    private final LiveData<MovieListingData> favouriteMoviesLiveData;

    public MovieListingsViewModel(@NonNull Application application) {
        super(application);
        movieBrowserRepository = MovieBrowserRepository.getInstance(application);
        movieListingsLiveData = movieBrowserRepository.getMovieListings();
        favouriteMoviesLiveData = movieBrowserRepository.getFavouriteMovies();
        setMovieListings(sortingIndex);
    }

    public void setMovieListings(int sortBy) {
        sortingIndex = sortBy;
        if (getMovieListings() == null || getMovieListings().isEmpty()) {
            movieBrowserRepository.refreshMovieData(sortingIndex);
        }
    }

    public LiveData<MovieListingData> getMovieListingsLiveData() {
        return movieListingsLiveData;
    }

    public LiveData<MovieListingData> getFavouriteMoviesLiveData() {
        return favouriteMoviesLiveData;
    }

    public void setFavouriteMovies(List<MovieListing> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }

    public void setPopularMovies(List<MovieListing> popularMovies) {
        this.popularMovies = popularMovies;
    }

    public void setTopRatedMovies(List<MovieListing> topRatedMovies) {
        this.topRatedMovies = topRatedMovies;
    }

    public List<MovieListing> getMovieListings() {
        switch (sortingIndex) {
            case SORT_BY_POPULARITY:
                return popularMovies;
            case SORT_BY_TOP_RATED:
                return topRatedMovies;
            default:
                return favouriteMovies;
        }
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
