package za.co.jcarklin.popularmovies.api;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import za.co.jcarklin.popularmovies.BuildConfig;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_MOVIEDB_URL = "https://api.themoviedb.org/3/";
    private static final String MOVIEDB_MOVIE = "movie";
    public static final String SORT_BY_POPULARITY = "popular";
    public static final String SORT_BY_TOP_RATED = "top_rated";

    private static final NetworkUtils networkUtils = new NetworkUtils();

    private NetworkUtils() {

    }

    public URL buildMovieUrl(String path) { //path = sort parameter or id
        Uri uri = Uri.parse(BASE_MOVIEDB_URL).buildUpon()
                .appendPath(MOVIEDB_MOVIE)
                .appendPath(path)
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_TOKEN)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public String getResponse(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                String next = scanner.next();
                Log.i(TAG,"Scanner next: " + next);
                return next;
            }
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static NetworkUtils getInstance() {
        return networkUtils;
    }

}