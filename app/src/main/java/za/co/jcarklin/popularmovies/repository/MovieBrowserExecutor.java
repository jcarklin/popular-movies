package za.co.jcarklin.popularmovies.repository;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

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

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}