package za.co.jcarklin.popularmovies.model.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM fav_movies")
    List<MovieListing> fetchFavouriteMovies();

    @Insert
    long addMovieToFavourites(MovieListing movie);

    @Delete
    void removeMovieFromFavourites(MovieListing movie);

    @Query("SELECT * FROM fav_movies WHERE themoviedb_id = :movieId")
    MovieListing getMovieByTmdbId(String movieId);
}
