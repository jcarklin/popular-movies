package za.co.jcarklin.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.jcarklin.popularmovies.api.JsonUtils;
import za.co.jcarklin.popularmovies.api.NetworkUtils;
import za.co.jcarklin.popularmovies.api.data.Movie;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private MovieAdapter movieAdapter = null;
    @BindView(R.id.rv_movies)
    RecyclerView moviesRecyclerView;
    @BindView(R.id.tv_heading)
    TextView heading;
    @BindView(R.id.pb_load_movies)
    ProgressBar pbLoadMovies;
    @BindView(R.id.error_message)
    TextView errorMessage;
    private GridLayoutManager gridLayoutManager;
    private int spanCount = 2;
    private int sortingIndex = 0;

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gridLayoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL,false);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        movieAdapter = new MovieAdapter(this,displayMetrics.widthPixels/spanCount);
        moviesRecyclerView.setAdapter(movieAdapter);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.setHasFixedSize(true);
        heading.setText(getResources().getString(R.string.top_20, getResources().getString(R.string.popularity)));
        new FetchMoviesTaskAsyncTask().execute();
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
                            String sortBy = sortingIndex==0?getResources().getString(R.string.popularity):getResources().getString(R.string.rating);
                            new FetchMoviesTaskAsyncTask().execute();
                            heading.setText(getResources().getString(R.string.top_20, sortBy));
                            dialogInterface.dismiss();
                        }
                    }).show();
                return true;
            case R.id.action_about:
                LayoutInflater factory = LayoutInflater.from(this);
                final View view = factory.inflate(R.layout.dialog_about, null);
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

    private class FetchMoviesTaskAsyncTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoadMovies.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.GONE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            String sortBy = sortingIndex==0?NetworkUtils.SORT_BY_POPULARITY:NetworkUtils.SORT_BY_TOP_RATED;
            //Check if network is available
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(sortBy);
                if (movieUrl==null) {
                    errorMessage.setText(R.string.network_unavailable);
                    return null;
                }
                try {
                    String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
                    return JsonUtils.getInstance().processMovieResults(jsonResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                errorMessage.setText(R.string.network_unavailable);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            pbLoadMovies.setVisibility(View.GONE);
            if (movies != null) {
                if (movieAdapter != null) {
                    movieAdapter.setMovieResults(movies);
                }
            } else {
                errorMessage.setVisibility(View.VISIBLE);
            }

        }
    }
}
