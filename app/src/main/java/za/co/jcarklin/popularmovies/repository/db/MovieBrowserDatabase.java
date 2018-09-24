package za.co.jcarklin.popularmovies.repository.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import za.co.jcarklin.popularmovies.repository.model.MovieListing;

@Database(entities = {MovieListing.class},version = 1,exportSchema = false)
public abstract class MovieBrowserDatabase extends RoomDatabase {

    private static MovieBrowserDatabase ourInstance = null;

    private static final String LOG_TAG = MovieBrowserDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "movie_browser";

    public static MovieBrowserDatabase getInstance(Context context) {
        if (ourInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new Database");
                ourInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieBrowserDatabase.class,MovieBrowserDatabase.DB_NAME)
                        .build();
            }
        }
        return ourInstance;
    }

    public abstract MovieDao movieDao();
}
