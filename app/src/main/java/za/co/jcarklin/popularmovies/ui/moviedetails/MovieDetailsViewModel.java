package za.co.jcarklin.popularmovies.ui.moviedetails;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import za.co.jcarklin.popularmovies.repository.MovieBrowserRepository;
import za.co.jcarklin.popularmovies.repository.model.FetchStatus;
import za.co.jcarklin.popularmovies.repository.model.MovieDetails;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;
import za.co.jcarklin.popularmovies.repository.model.MovieTrailer;

public class MovieDetailsViewModel extends AndroidViewModel {

    private final MovieBrowserRepository movieBrowserRepository;
    private LiveData<MovieDetails> movieDetailsLiveData;
    private LiveData<List<MovieTrailer>> movieTrailersLiveData;
    private LiveData<FetchStatus> fetchStatus;
    private LiveData<Boolean> isFavourite;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieBrowserRepository = MovieBrowserRepository.getInstance(application);
        movieDetailsLiveData = movieBrowserRepository.getMovieDetailsLiveData();
        movieTrailersLiveData = movieBrowserRepository.getMovieTrailersLiveData();
        isFavourite = movieBrowserRepository.getFavouriteLiveData();
        fetchStatus = movieBrowserRepository.getFetchStatus();
    }

    public LiveData<MovieDetails> getMovieDetailsLiveData() {
        return movieDetailsLiveData;
    }

    public LiveData<List<MovieTrailer>> getMovieTrailersLiveData() {
        return movieTrailersLiveData;
    }

    public LiveData<FetchStatus> getFetchStatus() {
        return fetchStatus;
    }

    public void updateMovieFavourite(MovieListing movieListing, boolean makeFavourite) {
        movieBrowserRepository.updateFavourite(movieListing, makeFavourite);
    }

    public LiveData<Boolean> getIsFavouriteLiveData() {
        return isFavourite;
    }
}
