package za.co.jcarklin.popularmovies.ui.movielistings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.MovieBrowserRepository;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class MovieListingsViewModel extends AndroidViewModel {

    private static final String TAG = MovieListingsViewModel.class.getSimpleName();


    private int sortingIndex = 0;

    private final MovieBrowserRepository movieBrowserRepository;
    private final LiveData<List<MovieListing>> movieListings;
    private final LiveData<List<MovieListing>> favouriteMovies;

    private MutableLiveData<String> heading = new MutableLiveData<>();

    public MovieListingsViewModel(@NonNull Application application) {
        super(application);
        movieBrowserRepository = MovieBrowserRepository.getInstance(application);
        movieListings = movieBrowserRepository.getMovieListings();
        favouriteMovies = movieBrowserRepository.getFavouriteMovies();
        setMovieListings(sortingIndex, true);
    }

    public void setMovieListings(int sortBy, boolean refresh) {
        sortingIndex = sortBy;
        if (sortBy != MovieBrowserRepository.SORT_BY_FAVOURITES) {
            movieBrowserRepository.setMovieListings(sortingIndex, refresh);
        }
    }

    public void setHeading() {
        Resources resources = getApplication().getResources();
        switch (sortingIndex) {
            case MovieBrowserRepository.SORT_BY_POPULARITY:
                heading.setValue(resources.getString(R.string.top_20, resources.getString(R.string.popularity)));
                break;
            case MovieBrowserRepository.SORT_BY_TOP_RATED:
                heading.setValue(resources.getString(R.string.top_20, resources.getString(R.string.rating)));
                break;
            default:
                heading.setValue(resources.getString(R.string.favourite_movies));
                break;
        }
    }

    public LiveData<List<MovieListing>> getMovieListings() {
        return movieListings;
    }

    public LiveData<List<MovieListing>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public LiveData<String> getHeading() {
        return heading;
    }


}
