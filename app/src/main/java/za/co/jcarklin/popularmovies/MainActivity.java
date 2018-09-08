package za.co.jcarklin.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import za.co.jcarklin.popularmovies.api.NetworkUtils;
import za.co.jcarklin.popularmovies.api.data.Movie;
import za.co.jcarklin.popularmovies.model.FetchMoviesResponseHandler;
import za.co.jcarklin.popularmovies.model.FetchMoviesTaskAsyncTask;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, FetchMoviesResponseHandler {

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
        fetchMovies();
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void fetchMovies() {
        //Check if network is available
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm==null?null:cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            setError(R.string.network_unavailable);
            return;
        }
        pbLoadMovies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        heading.setVisibility(View.INVISIBLE);
        new FetchMoviesTaskAsyncTask(this).execute(sortingIndex==0? NetworkUtils.SORT_BY_POPULARITY:NetworkUtils.SORT_BY_TOP_RATED);
        heading.setText(getResources().getString(R.string.top_20, sortingIndex==0?getResources().getString(R.string.popularity):getResources().getString(R.string.rating)));
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
                            fetchMovies();
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
    public void onClick(Movie selectedMovie) {
        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(MovieDetailsActivity.INTENT_EXTRA_SELECTED_MOVIE, selectedMovie);
        startActivity(movieDetailsIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sortingIndex", sortingIndex);
    }

    @Override
    public void setMovies(List<Movie> movies) {
        pbLoadMovies.setVisibility(View.GONE);
        if (movies != null && movieAdapter != null) {
            heading.setVisibility(View.VISIBLE);
            moviesRecyclerView.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.GONE);
            movieAdapter.setMovieResults(movies);
        } else {
            setError(R.string.network_unavailable);
        }
    }

    @Override
    public void setError(int errorResource) {
        errorMessage.setText(errorResource);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
