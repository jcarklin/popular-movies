package za.co.jcarklin.popularmovies.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import za.co.jcarklin.popularmovies.api.data.Configuration;
import za.co.jcarklin.popularmovies.api.data.MovieDetails;
import za.co.jcarklin.popularmovies.api.data.MovieResults;

/*Retrofit*/
public interface MovieApiClient {

    @GET("/movie/{sort}")
    Call<MovieResults> fetchMovieResults(@Path("sort") String sortBy);

    @GET("/movie/{id}")
    Call<MovieDetails> fetchMovieDetails(@Path("id") String movieId);

    @GET("/configuration")
    Call<Configuration> getImageConfigurationDetails();
}
