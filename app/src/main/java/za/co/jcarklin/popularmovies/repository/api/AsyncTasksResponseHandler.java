package za.co.jcarklin.popularmovies.repository.api;

import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieDetails;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;
import za.co.jcarklin.popularmovies.repository.model.MovieTrailer;

public interface AsyncTasksResponseHandler {
    void setMovies(List<MovieListing> movies, int sortBy);
    void setMovieDetailsLiveData(MovieDetails movie);
    void setMovieTrailers(List<MovieTrailer> movieTrailers);
}
