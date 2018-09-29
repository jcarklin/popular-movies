package za.co.jcarklin.popularmovies.ui.moviedetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.model.FetchStatus;
import za.co.jcarklin.popularmovies.repository.model.MovieDetails;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;
import za.co.jcarklin.popularmovies.repository.model.MovieTrailer;
import za.co.jcarklin.popularmovies.ui.movielistings.MovieAdapter;

import static za.co.jcarklin.popularmovies.Constants.BASE_YOUTUBE_VIDEO_URL;
import static za.co.jcarklin.popularmovies.Constants.STATUS_FAILED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_PROCESSING;

public class MovieDetailsActivity extends AppCompatActivity implements
        MovieTrailersAdapter.MovieTrailerAdapterOnClickHandler {

    public static final String INTENT_EXTRA_SELECTED_MOVIE = "selectedMovie";
    private static final int FETCH_MOVIE_DETAILS_LOADER_ID = 2;

    private MovieDetails selectedMovie;
    private MovieTrailersAdapter movieTrailersAdapter;

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
    @BindView(R.id.ll_movieDetails)
    LinearLayout movieDetailsLayout;
    @BindView(R.id.pb_load_movies)
    ProgressBar pbLoadMovies;
    @BindView(R.id.error_message)
    TextView errorMessage;
    @BindView(R.id.rv_trailers)
    RecyclerView trailersRecyclerView;

    private MovieDetailsViewModel movieDetailsViewModel;

    public MovieDetailsActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        movieTrailersAdapter = new MovieTrailersAdapter(this);
        trailersRecyclerView.setAdapter(movieTrailersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
        trailersRecyclerView.setHasFixedSize(true);
        setupViewModel();
    }

    private void setupViewModel() {
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        movieDetailsViewModel.getMovieDetailsLiveData().observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(@Nullable MovieDetails movieDetails) {
                setMovieDetails(movieDetails);
            }
        });
        movieDetailsViewModel.getIsFavouriteLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFavourite) {
                if (isFavourite) {
                    favouriteOff.setVisibility(View.GONE);
                    favouriteOn.setVisibility(View.VISIBLE);
                } else {
                    favouriteOff.setVisibility(View.VISIBLE);
                    favouriteOn.setVisibility(View.GONE);
                }
            }
        });
        movieDetailsViewModel.getFetchStatus().observe(this, new Observer<FetchStatus>() {
            @Override
            public void onChanged(@Nullable FetchStatus fetchStatus) {
                if (fetchStatus.getMovieStatus() == STATUS_PROCESSING) {
                    showProgressBar();
                } else if (fetchStatus.getMovieStatus() == STATUS_FAILED) {
                    errorMessage.setText(fetchStatus.getStatusMessage());
                    showError();
                } else {
                    showMovieDetails();
                }
            }
        });
        movieDetailsViewModel.getMovieTrailersLiveData().observe(this, new Observer<List<MovieTrailer>>() {
            @Override
            public void onChanged(@Nullable List<MovieTrailer> movieTrailers) {
                if (movieTrailers != null && !movieTrailers.isEmpty()) {
                    trailersRecyclerView.setVisibility(View.VISIBLE);
                    movieTrailersAdapter.setMovieTrailerResults(movieTrailers);
                } else {
                    trailersRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater factory = LayoutInflater.from(this);
            final View view = factory.inflate(R.layout.dialog_about, null);
            builder.setTitle(R.string.about)
                    .setView(view)
                    .setCancelable(true)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMovieDetails(MovieDetails movieDetails) {
        selectedMovie = movieDetails;
        Picasso.get()
                .load(MovieAdapter.getImageUrlPath() + selectedMovie.getPosterPath())
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
            String originalTitleAndLang = (selectedMovie.getOriginalTitle() + " (" + selectedMovie.getOriginalLanguageName() + ")");
            originalTitle.setText(originalTitleAndLang);
        }
        overview.setText(selectedMovie.getOverview());
        if (selectedMovie.isFavourite()) {

        }
        favouriteOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFavourite(true);
            }
        });
        favouriteOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                makeFavourite(false);
            }
        });
    }

    private void makeFavourite(boolean favourite) {
        MovieListing movieListing = new MovieListing(selectedMovie.getId(), selectedMovie.getPosterPath());
        movieDetailsViewModel.updateMovieFavourite(movieListing, favourite);
    }

    private void showMovieDetails() {
        movieDetailsLayout.setVisibility(View.VISIBLE);
        pbLoadMovies.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        movieDetailsLayout.setVisibility(View.GONE);
        pbLoadMovies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
    }

    private void showError() {
        movieDetailsLayout.setVisibility(View.GONE);
        pbLoadMovies.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickTrailer(MovieTrailer selectedTrailer) {
        Uri uri = Uri.parse(BASE_YOUTUBE_VIDEO_URL).buildUpon()
                .appendPath(selectedTrailer.getKey())
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        String title = getResources().getString(R.string.choose_app_to_view_video);
        Intent chooser = Intent.createChooser(intent, title);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
