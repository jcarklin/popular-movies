package za.co.jcarklin.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.jcarklin.popularmovies.api.JsonUtils;
import za.co.jcarklin.popularmovies.api.NetworkUtils;
import za.co.jcarklin.popularmovies.api.data.Movie;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter = null;
    @BindView(R.id.rv_movies)
    RecyclerView moviesRecyclerView;
    private FetchMoviesTaskAsyncTask fetchMoviesTaskAsyncTask = new FetchMoviesTaskAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.setHasFixedSize(true);
        fetchMoviesTaskAsyncTask.execute();
    }

    public class FetchMoviesTaskAsyncTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO loading indicator
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(NetworkUtils.SORT_BY_POPULARITY);
            try {
                String jsonResponse = NetworkUtils.getInstance().getResponse(movieUrl);
                return JsonUtils.getInstance().processMovieResults(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

            if (movies != null) {
                if (movieAdapter == null) {
                    movieAdapter = new MovieAdapter(movies);
                    moviesRecyclerView.setAdapter(movieAdapter);
                } else {
                    movieAdapter.setMovieResults(movies);
                }
            }

        }
    }
}
