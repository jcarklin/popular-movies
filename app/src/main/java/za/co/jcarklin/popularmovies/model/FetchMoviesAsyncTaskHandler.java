package za.co.jcarklin.popularmovies.model;

import java.util.List;

import za.co.jcarklin.popularmovies.model.data.Movie;

public interface FetchMoviesAsyncTaskHandler {

    void setMovies(List<Movie> movies);
    void setError(int errorResourceId);
    void setMoviesVisibility(int visibility);
    void setErrorVisibility(int visibility);
    void setProgressVisibilty(int visibilty);

}
