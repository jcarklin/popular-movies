package za.co.jcarklin.popularmovies.ui.movielistings;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.model.FetchStatus;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;
import za.co.jcarklin.popularmovies.ui.moviedetails.MovieDetailsActivity;

import static za.co.jcarklin.popularmovies.Constants.SORT_BY_FAVOURITES;
import static za.co.jcarklin.popularmovies.Constants.SORT_BY_POPULARITY;
import static za.co.jcarklin.popularmovies.Constants.SORT_BY_TOP_RATED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_FAILED;
import static za.co.jcarklin.popularmovies.Constants.STATUS_PROCESSING;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler {


    private MovieAdapter movieAdapter = null;
    @BindView(R.id.rv_movies)
    RecyclerView moviesRecyclerView;
    @BindView(R.id.tv_heading)
    TextView heading;
    @BindView(R.id.pb_load_movies)
    ProgressBar pbLoadMovies;
    @BindView(R.id.error_message)
    TextView errorMessage;

    MenuItem refresh;

    private int spanCount = 2;
    private int sortingIndex = 0;

    private MovieListingsViewModel movieListingsViewModel;

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        }
        movieAdapter = new MovieAdapter(this,displayMetrics.widthPixels/spanCount);
        moviesRecyclerView.setAdapter(movieAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.setHasFixedSize(true);
        if (savedInstanceState != null && savedInstanceState.containsKey("sortingIndex")) {
            sortingIndex = savedInstanceState.getInt("sortingIndex");
        }
        setupViewModel();
    }

    private void setupViewModel() {
        movieListingsViewModel = ViewModelProviders.of(this).get(MovieListingsViewModel.class);
        movieListingsViewModel.getPopularMovies().observe(this, new Observer<List<MovieListing>>() {
            @Override
            public void onChanged(@Nullable List<MovieListing> movieListings) {

                if (movieListings.isEmpty()) {
                    movieListingsViewModel.refreshPopularMovies();
                }
                if (sortingIndex==SORT_BY_POPULARITY) {
                    displayMovieListings(movieListings);
                }

            }
        });

        movieListingsViewModel.getTopRatedMovies().observe(this, new Observer<List<MovieListing>>() {
            @Override
            public void onChanged(@Nullable List<MovieListing> movieListings) {
                if (movieListings.isEmpty()) {
                    movieListingsViewModel.refreshTopRatedMovies();
                }
                if (sortingIndex==SORT_BY_TOP_RATED) {
                    displayMovieListings(movieListings);
                }

            }
        });

        movieListingsViewModel.getFavouriteMovies().observe(this, new Observer<List<MovieListing>>() {
            @Override
            public void onChanged(@Nullable List<MovieListing> movieListings) {
                if (sortingIndex==SORT_BY_FAVOURITES) {
                    displayMovieListings(movieListings);
                }
            }
        });

        movieListingsViewModel.getFetchStatus().observe(this, new Observer<FetchStatus>() {
            @Override
            public void onChanged(@Nullable FetchStatus fetchStatus) {
                if (fetchStatus.getMovieStatus() == STATUS_PROCESSING) {
                    showProgressBar();
                } else if (fetchStatus.getMovieStatus() == STATUS_FAILED) {
                    errorMessage.setText(fetchStatus.getStatusMessage());
                    showError();
                } else {
                    showMovies();
                }
            }
        });
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        refresh = menu.findItem(R.id.action_refresh);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (item.getItemId()) {
            case R.id.action_sort:
                builder.setTitle(R.string.sort_by)
                    .setSingleChoiceItems(getResources().getStringArray(R.array.sort_options), sortingIndex, new DialogInterface.OnClickListener() {
                        @SuppressLint("StringFormatInvalid")
                        public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                            sortingIndex = selectedIndex;
                            switch (sortingIndex) {
                                case SORT_BY_POPULARITY:
                                    refresh.setVisible(true);
                                    displayMovieListings(movieListingsViewModel.getPopularMovies().getValue());
                                    break;
                                case SORT_BY_TOP_RATED:
                                    refresh.setVisible(true);
                                    displayMovieListings(movieListingsViewModel.getTopRatedMovies().getValue());
                                    break;
                                default:
                                    refresh.setVisible(false);
                                    displayMovieListings(movieListingsViewModel.getFavouriteMovies().getValue());
                            }
                            dialogInterface.dismiss();
                        }
                    }).show();
                return true;
            case R.id.action_refresh:
                if (sortingIndex==SORT_BY_POPULARITY) {
                    movieListingsViewModel.refreshPopularMovies();
                } else if (sortingIndex==SORT_BY_TOP_RATED) {
                    movieListingsViewModel.refreshTopRatedMovies();
                }
                return true;
            case R.id.action_about:
                LayoutInflater factory = LayoutInflater.from(this);
                final View view = factory.inflate(R.layout.dialog_about,null);
                builder.setTitle(R.string.about)
                    .setView(view)
                    .setCancelable(true)
                    .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(MovieListing selectedMovie) {
        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(MovieDetailsActivity.INTENT_EXTRA_SELECTED_MOVIE, selectedMovie.getId());
        startActivity(movieDetailsIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sortingIndex", sortingIndex);
    }

    private void displayMovieListings(List<MovieListing> movies) {
        if (sortingIndex == SORT_BY_FAVOURITES) {
            heading.setText(getString(R.string.favourite_movies));
        } else {
            heading.setText(getString(R.string.top_20, getResources().getString(movieListingsViewModel.getHeading(sortingIndex))));
        }
        if (sortingIndex == SORT_BY_FAVOURITES && movies.isEmpty()) {
            //TODO
            showEmptyFavourites();
            errorMessage.setText("No Favourites");
            showError();
        } else {
            showMovies();
            movieAdapter.setMovieResults(movies);
        }
    }

    private void showMovies() {
        moviesRecyclerView.setVisibility(View.VISIBLE);
        heading.setVisibility(View.VISIBLE);
        pbLoadMovies.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        heading.setVisibility(View.INVISIBLE);
        pbLoadMovies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
    }

    private void showError() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        heading.setVisibility(View.INVISIBLE);
        pbLoadMovies.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void showEmptyFavourites() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        heading.setVisibility(View.INVISIBLE);
        pbLoadMovies.setVisibility(View.GONE);
        errorMessage.setVisibility(View.INVISIBLE);
    }
}
