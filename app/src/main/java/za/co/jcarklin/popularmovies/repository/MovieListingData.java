package za.co.jcarklin.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class MovieListingData<T> {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAILED = 0;
    public static final int STATUS_PROCESSING = 0;

    private T movies;
    private int sortType = 0;
    private int status;
    private Integer message;

    public MovieListingData(T movies, int sortType, int status, Integer messageRvalue) {
        this.movies = movies;
        this.sortType = sortType;
        this.status = status;
        this.message = message;
    }

    public T getMovies() {
        return movies;
    }

    public void setMovies(T movies) {
        this.movies = movies;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }
}
