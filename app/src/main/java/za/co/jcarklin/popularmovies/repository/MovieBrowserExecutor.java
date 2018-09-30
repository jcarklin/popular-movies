package za.co.jcarklin.popularmovies.repository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieBrowserExecutor {

    private static final Object LOCK = new Object();
    private static MovieBrowserExecutor sInstance;
    private final Executor diskIO;

    private MovieBrowserExecutor(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static MovieBrowserExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieBrowserExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }
}
