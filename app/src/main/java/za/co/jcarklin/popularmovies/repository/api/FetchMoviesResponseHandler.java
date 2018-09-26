package za.co.jcarklin.popularmovies.repository.api;

import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieListing;

public interface FetchMoviesResponseHandler {
    void setMovies(List<MovieListing> movies);
    void setError(int errorResourceId);
}
