package za.co.jcarklin.popularmovies.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public class MovieListingData {

    private LiveData<List<MovieListing>> movies;
    private int sortType = 0;
    private int status;
    private Integer message;

    public MovieListingData(LiveData<List<MovieListing>> movies, int sortType, int status, Integer messageRvalue) {
        this.movies = movies;
        this.sortType = sortType;
        this.status = status;
        this.message = message;
    }

    public LiveData<List<MovieListing>> getMovies() {
        return movies;
    }

    public int getSortType() {
        return sortType;
    }

    public int getStatus() {
        return status;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }
}
