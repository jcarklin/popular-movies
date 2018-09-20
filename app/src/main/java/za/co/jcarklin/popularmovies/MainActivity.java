package za.co.jcarklin.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import za.co.jcarklin.popularmovies.model.FetchMoviesAsyncTaskLoader;
import za.co.jcarklin.popularmovies.model.data.Movie;
import za.co.jcarklin.popularmovies.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int FETCH_MOVIES_LOADER_ID = 1;

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

        getSupportLoaderManager().initLoader(FETCH_MOVIES_LOADER_ID, null, this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        fetchMovies();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
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
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            setError(R.string.network_unavailable);
            return;
        }
        Bundle sortByBundle = new Bundle();
        sortByBundle.putString(FetchMoviesAsyncTaskLoader.SORT_BY_KEY,sortingIndex==0? NetworkUtils.SORT_BY_POPULARITY:NetworkUtils.SORT_BY_TOP_RATED);
        getSupportLoaderManager().restartLoader(FETCH_MOVIES_LOADER_ID, sortByBundle, this);
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
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
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

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        pbLoadMovies.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.GONE);
        return new FetchMoviesAsyncTaskLoader(this,args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        pbLoadMovies.setVisibility(View.GONE);
        if (data != null && movieAdapter != null) {
            setMovies(data);
        } else {
            setError(R.string.network_unavailable);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }

    private void setError(int error) {
        errorMessage.setText(error);
        errorMessage.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        heading.setVisibility(View.INVISIBLE);
    }

    private void setMovies(List<Movie> movies) {
        movieAdapter.setMovieResults(movies);
        moviesRecyclerView.setVisibility(View.VISIBLE);
        heading.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_num_cols_key))) {
            sortingIndex = Integer.valueOf(sharedPreferences.getString(key,"2"));
            ((GridLayoutManager)moviesRecyclerView.getLayoutManager()).setSpanCount(sortingIndex);
            fetchMovies();
        }
    }
}
