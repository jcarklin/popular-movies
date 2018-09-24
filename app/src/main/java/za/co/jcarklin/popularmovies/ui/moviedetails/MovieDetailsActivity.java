package za.co.jcarklin.popularmovies.ui.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.api.FetchMovieDetailsAsyncTaskLoader;
import za.co.jcarklin.popularmovies.repository.db.MovieBrowserDatabase;
import za.co.jcarklin.popularmovies.repository.model.MovieDetails;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;
import za.co.jcarklin.popularmovies.ui.movielistings.MovieAdapter;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetails>{

    public static final String INTENT_EXTRA_SELECTED_MOVIE = "selectedMovie";
    private static final int FETCH_MOVIE_DETAILS_LOADER_ID = 2;

    private final Executor dbExecutor;
    private MovieDetails selectedMovie;

    @BindView(R.id.iv_movie_poster)
    ImageView moviePoster;
    @BindView((R.id.tv_movie_title))
    TextView movieTitle;
    @BindView(R.id.tv_released_date)
    TextView releasedDate;
    @BindView(R.id.tv_user_rating)
    TextView userRating;
    @BindView((R.id.ratingBar))
    RatingBar ratingBar;
    @BindView(R.id.tv_original_title)
    TextView originalTitle;
    @BindView(R.id.tv_overview)
    TextView overview;
    @BindView(R.id.tv_popularity_rating)
    TextView popularityRating;
    @BindView(R.id.iv_favourite_on)
    ImageView favouriteOn;
    @BindView(R.id.iv_favourite_off)
    ImageView favouriteOff;

    private MovieBrowserDatabase movieBrowserDatabase;

    public MovieDetailsActivity() {
        this.dbExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Intent sourceIntent = getIntent();
        movieBrowserDatabase = MovieBrowserDatabase.getInstance(getApplicationContext());
        if (sourceIntent != null && sourceIntent.hasExtra(INTENT_EXTRA_SELECTED_MOVIE)) {
            Integer id = sourceIntent.getIntExtra(INTENT_EXTRA_SELECTED_MOVIE, 0);
            fetchMovieDetails(id);
        }
    }

    private void fetchMovieDetails(Integer tmdbId) {
        //Check if network is available
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm == null ? null : cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            setError(R.string.network_unavailable);
            return;
        }

        Bundle idBundle = new Bundle();
        idBundle.putInt(FetchMovieDetailsAsyncTaskLoader.MOVIE_ID_KEY, tmdbId);
        getSupportLoaderManager().restartLoader(FETCH_MOVIE_DETAILS_LOADER_ID, idBundle, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId()==R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater factory = LayoutInflater.from(this);
            final View view = factory.inflate(R.layout.dialog_about,null);
            builder.setTitle(R.string.about)
                    .setView(view)
                    .setCancelable(true)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavourites() {
        //save to db
        final MovieListing movieListing = new MovieListing(selectedMovie.getId(), selectedMovie.getPosterPath());
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                movieBrowserDatabase.movieDao().addMovieToFavourites(movieListing);
            }
        });
        selectedMovie.setFavourite(true);
        favouriteOff.setVisibility(View.GONE);
        favouriteOn.setVisibility(View.VISIBLE);
    }

    private void removeFromFavourites() {
        //remove from db
        final MovieListing movieListing = new MovieListing(selectedMovie.getId(), selectedMovie.getPosterPath());
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                movieBrowserDatabase.movieDao().removeMovieFromFavourites(movieListing);
            }
        });
        selectedMovie.setFavourite(false);
        favouriteOff.setVisibility(View.VISIBLE);
        favouriteOn.setVisibility(View.GONE);
    }

    private void setError(int error) {
        //TODO set error message
        //errorMessage.setText(error);
        //errorMessage.setVisibility(View.VISIBLE);
    }

    private void setMovieDetails(MovieDetails movieDetails) {
        selectedMovie = movieDetails;
        Picasso.get()
                .load(MovieAdapter.getImageUrlPath()+selectedMovie.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.unavailable)
                .into(moviePoster);
        movieTitle.setText(selectedMovie.getTitle());
        releasedDate.setText(selectedMovie.getReleaseDate());
        userRating.setText(String.valueOf(selectedMovie.getVoteAverage()));
        ratingBar.setRating(selectedMovie.getVoteAverage().floatValue());
        popularityRating.setText(String.valueOf(selectedMovie.getPopularity()));
        if (selectedMovie.getOriginalTitle().isEmpty()
                || selectedMovie.getTitle().equals(selectedMovie.getOriginalTitle())) {
            originalTitle.setVisibility(View.GONE);
        } else {
            String originalTitleAndLang = (selectedMovie.getOriginalTitle() + " (" + selectedMovie.getOriginalLanguageName()+")");
            originalTitle.setText(originalTitleAndLang);
        }
        overview.setText(selectedMovie.getOverview());
        if (selectedMovie.isFavourite()) {
            favouriteOff.setVisibility(View.GONE);
            favouriteOn.setVisibility(View.VISIBLE);
        }
        favouriteOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addToFavourites();
            }
        });
        favouriteOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeFromFavourites();
            }
        });
    }

    @NonNull
    @Override
    public Loader<MovieDetails> onCreateLoader(int id, @Nullable Bundle args) {
//        pbLoadMovies.setVisibility(View.VISIBLE);
//        errorMessage.setVisibility(View.GONE);
        return new FetchMovieDetailsAsyncTaskLoader(this,args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieDetails> loader, MovieDetails data) {
//        pbLoadMovies.setVisibility(View.GONE);
        if (data != null) {
            setMovieDetails(data);
        } else {
            setError(R.string.network_unavailable);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<MovieDetails> loader) {

    }
}
