package za.co.jcarklin.popularmovies.repository.api;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import za.co.jcarklin.popularmovies.BuildConfig;

import static za.co.jcarklin.popularmovies.Constants.BASE_MOVIEDB_URL;
import static za.co.jcarklin.popularmovies.Constants.MOVIEDB_MOVIE;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final NetworkUtils networkUtils = new NetworkUtils();

    private NetworkUtils() {

    }

    public URL buildMovieUrl(String path) { //path = sort parameter or id
        return buildMovieUrl(path, null);
    }

    public URL buildMovieUrl(String path, String extraParameter) { //path = sort parameter or id
        Uri.Builder builder = Uri.parse(BASE_MOVIEDB_URL).buildUpon()
                .appendPath(MOVIEDB_MOVIE)
                .appendPath(path);
        if (extraParameter != null) {
            builder.appendPath(extraParameter);
        }
        builder.appendQueryParameter("api_key", BuildConfig.TMDB_API_TOKEN);
        Uri uri = builder.build();

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