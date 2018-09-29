package za.co.jcarklin.popularmovies.repository.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import za.co.jcarklin.popularmovies.repository.model.MovieListing;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM fav_movies")
    LiveData<List<MovieListing>> fetchFavouriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addMovieToFavourites(MovieListing movie);

    @Delete
    int removeMovieFromFavourites(MovieListing movie);

    @Query("SELECT count(id) FROM fav_movies WHERE id = :movieId")
    int getMovieCountByTmdbId(Integer movieId);
}
