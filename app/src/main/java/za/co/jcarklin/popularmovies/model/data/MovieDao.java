package za.co.jcarklin.popularmovies.model.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM fav_movies")
    List<Movie> fetchFavouriteMovies();

    @Insert
    void addMovieToFavourites(Movie movie);

    @Delete
    void removeMovieFromFavourites(Movie movie);
}
