package za.co.jcarklin.popularmovies.ui.movielistings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import za.co.jcarklin.popularmovies.repository.db.MovieBrowserDatabase;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class MovieListingsViewModel extends AndroidViewModel {

    private LiveData<List<MovieListing>> favouritesMovieListings;
    private List<MovieListing> popularMovieListings;
    private List<MovieListing> topRatedMovieListings;

    public MovieListingsViewModel(@NonNull Application application) {
        super(application);
        favouritesMovieListings = MovieBrowserDatabase.getInstance(this.getApplication()).movieDao().fetchFavouriteMovies();

    }

    public LiveData<List<MovieListing>> getFavouriteMovieListings() {
        return favouritesMovieListings;
    }

    public List<MovieListing> getPopularMovieListings() {
        return popularMovieListings;
    }

    public List<MovieListing> getTopRatedMovieListings() {
        return topRatedMovieListings;
    }
}
