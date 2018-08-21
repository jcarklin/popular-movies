package za.co.jcarklin.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.net.URL;

import za.co.jcarklin.popularmovies.api.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private TextView testText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testText = findViewById(R.id.testText);
        new FetchMoviesTaskAsyncTask().execute();
    }

    public class FetchMoviesTaskAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO loading indicaror
        }

        @Override
        protected String[] doInBackground(String... params) {

            URL movieUrl = NetworkUtils.getInstance().buildMovieUrl(NetworkUtils.SORT_BY_POPULARITY);
            try {
                return new String[]{NetworkUtils.getInstance().getResponse(movieUrl)};
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movies) {

            if (movies != null) {
                testText.setText(movies[0]);
            }


        }
    }
}
