package za.co.jcarklin.popularmovies.model;

import java.util.List;

import za.co.jcarklin.popularmovies.api.data.Movie;

public interface FetchMoviesResponseHandler {

    void setMovies(List<Movie> movies);
    void setError(int errorResourceId);

}
