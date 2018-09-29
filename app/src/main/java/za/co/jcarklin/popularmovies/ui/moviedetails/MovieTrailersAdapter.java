package za.co.jcarklin.popularmovies.ui.moviedetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.model.MovieTrailer;

import static za.co.jcarklin.popularmovies.Constants.BASE_YOUTUBE_IMAGE_URL;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersViewHolder> {

    private List<MovieTrailer> movieTrailerResults = new ArrayList<>(20);
    private final MovieTrailerAdapterOnClickHandler movieAdapterOnClickHandler;

    public interface MovieTrailerAdapterOnClickHandler {
        void onClickTrailer(MovieTrailer selectedTrailer);
    }

    public MovieTrailersAdapter(MovieTrailerAdapterOnClickHandler clickHandler) {
        movieAdapterOnClickHandler = clickHandler;
    }

    public void setMovieTrailerResults(List<MovieTrailer> results) {
        this.movieTrailerResults = results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieTrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_trailer_item, parent, false);
        return new MovieTrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailersViewHolder holder, int position) {
        MovieTrailer movie = movieTrailerResults.get(position);
        Picasso.get()
                .load(BASE_YOUTUBE_IMAGE_URL+movie.getKey()+"/0.jpg")
                .placeholder(R.drawable.loading)
                .error(R.drawable.unavailable)
                .into(holder.movieTrailerImageView);
    }

    @Override
    public int getItemCount() {
        return movieTrailerResults.size();
    }

    //ViewHolder Class
    public class MovieTrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_trailer_image)
        ImageView movieTrailerImageView;

        MovieTrailersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieTrailer selectedMovie = movieTrailerResults.get(adapterPosition);
            movieAdapterOnClickHandler.onClickTrailer(selectedMovie);
        }
    }
}
