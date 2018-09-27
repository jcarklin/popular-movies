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
import za.co.jcarklin.popularmovies.repository.MovieBrowserRepository;
import za.co.jcarklin.popularmovies.repository.MovieListingData;
import za.co.jcarklin.popularmovies.repository.model.MovieListing;
import za.co.jcarklin.popularmovies.ui.moviedetails.MovieDetailsActivity;

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
        movieListingsViewModel.getMovieListings().observe(this, new Observer<MovieListingData>() {
            @Override
            public void onChanged(@Nullable MovieListingData movieListings) {
                if (movieListings.getStatus() == MovieListingData.STATUS_PROCESSING) {
                    showProgressBar();
                } else if (movieListings.getStatus()==MovieListingData.STATUS_FAILED) {
                    errorMessage.setText(movieListings.getMessage());
                    showError();
                } else {
                    setMovies(movieListings.getMovies());
                }
            }
        });
        movieListingsViewModel.getFavouriteMovies().observe(this, new Observer<List<MovieListing>>() {
            @Override
            public void onChanged(@Nullable List<MovieListing> movieListings) {
                favouriteMoviesCache = movieListings;
                if (sortingIndex==MovieBrowserRepository.SORT_BY_FAVOURITES) {
                    setMovies(favouriteMoviesCache);
                }
            }
        });
        movieListingsViewModel.getHeading().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer newHeading) {
                heading.setText(getString(R.string.top_20,getResources().getString(newHeading)));
            }
        });
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (item.getItemId()) {
            case R.id.action_sort:
                builder.setTitle(R.string.sort_by)
                    .setSingleChoiceItems(getResources().getStringArray(R.array.sort_options), sortingIndex,new DialogInterface.OnClickListener() {
                        @SuppressLint("StringFormatInvalid")
                        public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                            sortingIndex = selectedIndex;
                            pbLoadMovies.setVisibility(View.VISIBLE);
                            movieListingsViewModel.setMovieListings(sortingIndex, false);
                            if (sortingIndex==MovieBrowserRepository.SORT_BY_FAVOURITES) {
                                setMovies(favouriteMoviesCache);
                            }
                            dialogInterface.dismiss();

                        }
                    }).show();
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

    private void setMovies(List<MovieListing> movies) {
        movieAdapter.setMovieResults(movies);
        movieListingsViewModel.setHeading();
        showMovies();
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

}
