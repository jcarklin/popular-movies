package za.co.jcarklin.popularmovies.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.api.AsyncTasksResponseHandler;
import za.co.jcarklin.popularmovies.repository.api.FetchMovieDetailsAsyncTask;
import za.co.jcarklin.popularmovies.repository.api.FetchMovieListingsAsyncTask;
import za.co.jcarklin.popularmovies.repository.api.FetchMovieReviewsAsyncTask;
import za.co.jcarklin.popularmovies.repository.api.FetchMovieVideosAsyncTask;
import za.co.jcarklin.popularmovies.repository.db.MovieBrowserDatabase;
import za.co.jcarklin.popularmovies.repository.db.MovieDao;
import za.co.jcarklin.popularmovies.repository.model.FetchStatus;
import za.co.jcarklin.popularmovies.repository.model.MovieDetails;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;
import za.co.jcarklin.popularmovies.repository.model.MovieReview;
import za.co.jcarklin.popularmovies.repository.model.MovieTrailer;

import static za.co.jcarklin.popularmovies.Constants.SORT_BY_POPULARITY;
import static za.co.jcarklin.popularmovies.Constants.SORT_BY_TOP_RATED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_FAILED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_PROCESSING;
import static za.co.jcarklin.popularmovies.Constants.STATUS_SUCCESS;

public class MovieBrowserRepository implements AsyncTasksResponseHandler {

    private static final String TAG = MovieBrowserRepository.class.getSimpleName();

    private static MovieBrowserRepository repository;
    private final MovieDao movieDao;

    //Movie Listings
    private final LiveData<List<MovieListing>> favouriteMovies;
    private final MutableLiveData<List<MovieListing>> popularMovies;
    private final MutableLiveData<List<MovieListing>> topRatedMovies;
    private final MutableLiveData<FetchStatus> status;
    private final MutableLiveData<Boolean> isFavourite;

    //Movie Details
    private final MutableLiveData<MovieDetails> movieDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MovieTrailer>> movieTrailersLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MovieReview>> movieReviewsLiveData = new MutableLiveData<>();

    private final ConnectivityManager connectivityManager;

    private MovieBrowserRepository(Application application) {
        movieDao = MovieBrowserDatabase.getInstance(application).movieDao();
        favouriteMovies = movieDao.fetchFavouriteMovies();
        popularMovies = new MutableLiveData<>();
        topRatedMovies = new MutableLiveData<>();
        status = new MutableLiveData<>();
        isFavourite = new MutableLiveData<>();
        connectivityManager = (ConnectivityManager)application.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public LiveData<Boolean> getFavouriteLiveData() {
        return isFavourite;
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

    public void refreshPopularMovieData() {
        if (checkNetworkAvailability()) {
            status.setValue(new FetchStatus(STATUS_PROCESSING, null));
            new FetchMovieListingsAsyncTask(this).execute(SORT_BY_POPULARITY);
        }
    }

    public void refreshTopRatedMovieData() {
        if (checkNetworkAvailability()) {
            status.setValue(new FetchStatus(STATUS_PROCESSING, null));
            new FetchMovieListingsAsyncTask(this).execute(SORT_BY_TOP_RATED);
        }
    }

    private boolean checkNetworkAvailability() {
        //Check if network is available
        NetworkInfo networkInfo = connectivityManager==null ? null : connectivityManager.getActiveNetworkInfo();
        boolean available = true;
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            status.setValue(new FetchStatus(STATUS_FAILED,R.string.network_unavailable));
            available = false;
        }
        return available;
    }

    @Override
    public void setMovies(List<MovieListing> movies, int sortBy) {
        if (movies != null) {
            status.setValue(new FetchStatus(STATUS_SUCCESS,null));
            if (sortBy == SORT_BY_POPULARITY) {
                popularMovies.setValue(movies);
                if (topRatedMovies.getValue()==null) {
                    refreshTopRatedMovieData();
                }
            } else {
                topRatedMovies.setValue(movies);
            }
        } else {
            status.setValue(new FetchStatus(STATUS_FAILED,R.string.network_unavailable));
        }
    }

    @Override
    public void setMovieDetailsLiveData(final MovieDetails movie) {
        if (movie != null) {
            refreshMovieTrailers(movie.getId());
            refreshMovieReviews(movie.getId());
            MovieBrowserExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    int count = movieDao.getMovieCountByTmdbId(movie.getId());
                    isFavourite.postValue(count>0);
                    status.postValue(new FetchStatus(STATUS_SUCCESS,null));
                    movieDetailsLiveData.postValue(movie);
                }
            });

        }else {
            status.setValue(new FetchStatus(STATUS_FAILED,R.string.network_unavailable));
        }
    }

    @Override
    public void setMovieTrailers(List<MovieTrailer> movieTrailers) {
        if (movieTrailers != null) {
            movieTrailersLiveData.setValue(movieTrailers);
        }
    }

    @Override
    public void setMovieReviews(List<MovieReview> movieReviews) {
        if (movieReviews != null) {
            movieReviewsLiveData.setValue(movieReviews);
        }
    }

    public void refreshMovieDetails(Integer id) {
        if (checkNetworkAvailability()) {
            status.setValue(new FetchStatus(STATUS_PROCESSING, null));
            new FetchMovieDetailsAsyncTask(this).execute(id);
        }
    }

    private void refreshMovieTrailers(Integer id) {
        new FetchMovieVideosAsyncTask(this).execute(id);
    }

    private void refreshMovieReviews(Integer id) {
        new FetchMovieReviewsAsyncTask(this).execute(id);
    }


    public void updateFavourite(final MovieListing movieListing, final boolean makeFavourite) {
        //save to db
        MovieBrowserExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (makeFavourite) {
                    isFavourite.postValue(movieDao.addMovieToFavourites(movieListing)!=null);
                } else {
                    isFavourite.postValue(movieDao.removeMovieFromFavourites(movieListing)!=1);
                }
                if (movieDetailsLiveData.getValue() != null) {
                    movieDetailsLiveData.getValue().setFavourite((makeFavourite));
                }
            }
        });
    }


    public static MovieBrowserRepository getInstance(Application application) {
        if (repository == null) {
            repository = new MovieBrowserRepository(application);
        }
        return repository;
    }

}
