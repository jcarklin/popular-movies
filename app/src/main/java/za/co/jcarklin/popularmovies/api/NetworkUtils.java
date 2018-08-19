package za.co.jcarklin.popularmovies.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import za.co.jcarklin.popularmovies.BuildConfig;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_MOVIEDB_URL = "https://api.themoviedb.org/3/";
    public static final String SORT_BY_POPULARITY = "popular";
    public static final String SORT_BY_TOP_RATED = "top_rated";

    private MovieApiClient movieApiClient;
    private static NetworkUtils networkUtils = null;

    private NetworkUtils() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(loggingInterceptor);
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.TMDB_API_TOKEN).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_MOVIEDB_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        movieApiClient = retrofit.create(MovieApiClient.class);
    }



    public static NetworkUtils getInstance() {
        if (networkUtils == null) {
            networkUtils = new NetworkUtils();
        }
        return networkUtils;
    }

    public MovieApiClient getMovieApiClient() {
        return movieApiClient;
    }

}