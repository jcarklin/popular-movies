package za.co.jcarklin.popularmovies.ui.moviedetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import za.co.jcarklin.popularmovies.repository.model.MovieReview;
import za.co.jcarklin.popularmovies.repository.model.MovieTrailer;
import za.co.jcarklin.popularmovies.ui.movielistings.MovieAdapter;

import static za.co.jcarklin.popularmovies.Constants.BASE_YOUTUBE_VIDEO_URL;
import static za.co.jcarklin.popularmovies.Constants.STATUS_FAILED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_PROCESSING;

public class MovieDetailsActivity extends AppCompatActivity implements
        MovieTrailersAdapter.MovieTrailerAdapterOnClickHandler {

    private MovieTrailersAdapter movieTrailersAdapter;
    private MovieReviewsAdapter movieReviewsAdapter;

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
    @BindView(R.id.ll_movieDetails)
    LinearLayout movieDetailsLayout;
    @BindView(R.id.pb_load_movies)
    ProgressBar pbLoadMovies;
    @BindView(R.id.error_message)
    TextView errorMessage;
    @BindView(R.id.rv_trailers)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.rv_reviews)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.btn_show_all_reviews)
    Button showAllReviews;
    @BindView((R.id.tv_trailers_heading))
    TextView trailersHeading;
    @BindView((R.id.tv_reviews_heading))
    TextView reviewsHeading;

    private MovieDetailsViewModel movieDetailsViewModel;
    private MenuItem favouriteMenuItem;

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

        movieReviewsAdapter = new MovieReviewsAdapter();
        reviewsRecyclerView.setAdapter(movieReviewsAdapter);
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        reviewsRecyclerView.setHasFixedSize(true);

        showAllReviews.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                movieReviewsAdapter.setMovieReviewResults(movieDetailsViewModel.getMovieReviewsLiveData().getValue());
                showAllReviews.setVisibility(View.GONE);
            }
        });

        setupViewModel();
    }

    private void setupViewModel() {
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        movieDetailsViewModel.getMovieDetailsLiveData().observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(@Nullable MovieDetails movieDetails) {
                setMovieDetailsViews(movieDetails);
            }
        });
        movieDetailsViewModel.getIsFavouriteLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFavourite) {
                if (favouriteMenuItem != null) {
                    favouriteMenuItem.setIcon(movieDetailsViewModel.getHeartIcon());
                    favouriteMenuItem.setVisible(true);
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
                    trailersHeading.setVisibility(View.VISIBLE);
                    trailersRecyclerView.setVisibility(View.VISIBLE);
                    movieTrailersAdapter.setMovieTrailerResults(movieTrailers);
                } else {
                    trailersHeading.setVisibility(View.GONE);
                    trailersRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        movieDetailsViewModel.getMovieReviewsLiveData().observe(this, new Observer<List<MovieReview>>() {
            @Override
            public void onChanged(@Nullable List<MovieReview> movieReviews) {
                if (movieReviews != null && !movieReviews.isEmpty()) {
                    reviewsHeading.setVisibility(View.VISIBLE);
                    reviewsRecyclerView.setVisibility(View.VISIBLE);
                    movieReviewsAdapter.setMovieReviewResults(movieDetailsViewModel.getFirstFiveReviews());
                    if (movieReviews.size()>5) {
                        showAllReviews.setVisibility(View.VISIBLE);
                    } else {
                        showAllReviews.setVisibility(View.GONE);
                    }
                } else {
                    reviewsHeading.setVisibility(View.GONE);
                    reviewsRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        favouriteMenuItem = menu.findItem(R.id.action_favourite);
        favouriteMenuItem.setIcon(movieDetailsViewModel.getHeartIcon());
        favouriteMenuItem.setVisible(false);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.movie_details);
        }

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
        } else if (item.getItemId() == R.id.action_favourite) {
            movieDetailsViewModel.toggleMovieFavourite();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setMovieDetailsViews(final MovieDetails selectedMovie) {
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
