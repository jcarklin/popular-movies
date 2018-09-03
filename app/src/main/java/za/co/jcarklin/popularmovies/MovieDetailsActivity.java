package za.co.jcarklin.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.jcarklin.popularmovies.api.data.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_SELECTED_MOVIE = "selectedMovie";

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

    public MovieDetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Intent sourceIntent = getIntent();
        if (sourceIntent != null && sourceIntent.hasExtra(INTENT_EXTRA_SELECTED_MOVIE)) {
            Movie selectedMovie = sourceIntent.getParcelableExtra(INTENT_EXTRA_SELECTED_MOVIE);
            Picasso.get()
                    .load(MovieAdapter.getImageUrlPath()+selectedMovie.getPosterPath())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.unavailable)
                    .into(moviePoster);
            movieTitle.setText(selectedMovie.getTitle());
            releasedDate.setText(selectedMovie.getReleaseDate());
            userRating.setText(String.valueOf(selectedMovie.getVoteAverage()));
            ratingBar.setRating(selectedMovie.getVoteAverage());
            popularityRating.setText(String.valueOf(selectedMovie.getPopularity()));
            if (selectedMovie.getOriginalTitle().isEmpty()
            || selectedMovie.getTitle().equals(selectedMovie.getOriginalTitle())) {
                originalTitle.setVisibility(View.GONE);
            } else {
                String originalTitleAndLang = (selectedMovie.getOriginalTitle() + " (" + selectedMovie.getOriginalLanguageName()+")");
                originalTitle.setText(originalTitleAndLang);
            }
            overview.setText(selectedMovie.getOverview());
        }
    }
}
