package za.co.jcarklin.popularmovies.ui.moviedetails;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.MovieBrowserRepository;
import za.co.jcarklin.popularmovies.repository.model.FetchStatus;
import za.co.jcarklin.popularmovies.repository.model.MovieDetails;
import za.co.jcarklin.popularmovies.repository.model.MovieReview;
import za.co.jcarklin.popularmovies.repository.model.MovieTrailer;

class MovieDetailsViewModel extends AndroidViewModel {

    private final MovieBrowserRepository movieBrowserRepository;
    private final LiveData<MovieDetails> movieDetailsLiveData;
    private final LiveData<List<MovieTrailer>> movieTrailersLiveData;
    private final LiveData<List<MovieReview>> movieReviewsLiveData;

    private final LiveData<FetchStatus> fetchStatus;
    private final LiveData<Boolean> isFavourite;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieBrowserRepository = MovieBrowserRepository.getInstance(application);
        movieDetailsLiveData = movieBrowserRepository.getMovieDetailsLiveData();
        movieTrailersLiveData = movieBrowserRepository.getMovieTrailersLiveData();
        movieReviewsLiveData = movieBrowserRepository.getMovieReviewsLiveData();
        isFavourite = movieBrowserRepository.getFavouriteLiveData();
        fetchStatus = movieBrowserRepository.getFetchStatus();
    }

    public LiveData<MovieDetails> getMovieDetailsLiveData() {
        return movieDetailsLiveData;
    }

    public LiveData<List<MovieTrailer>> getMovieTrailersLiveData() {
        return movieTrailersLiveData;
    }

    public LiveData<List<MovieReview>> getMovieReviewsLiveData() {
        return movieReviewsLiveData;
    }
    public LiveData<FetchStatus> getFetchStatus() {
        return fetchStatus;
    }

    public void toggleMovieFavourite() {
        if (movieDetailsLiveData.getValue() != null && isFavourite != null && isFavourite.getValue() != null) {
            movieBrowserRepository.updateFavourite(movieDetailsLiveData.getValue().getMovieListing(), !isFavourite.getValue());
        }
    }

    public LiveData<Boolean> getIsFavouriteLiveData() {
        return isFavourite;
    }

    public List<MovieReview> getFirstFiveReviews() {
        List<MovieReview> reviews = movieReviewsLiveData.getValue();
        if (reviews != null && reviews.size()>5) {
            return reviews.subList(0, 5);
        }
        return reviews;
    }

    public int getHeartIcon() {
        return (isFavourite != null && isFavourite.getValue() != null && isFavourite.getValue())? R.drawable.ic_favorite_red_24dp:R.drawable.ic_favorite_border_red_24dp;
    }
}
