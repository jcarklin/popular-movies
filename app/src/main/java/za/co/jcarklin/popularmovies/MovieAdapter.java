package za.co.jcarklin.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import za.co.jcarklin.popularmovies.api.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> movieResults;
    private String imageUrlPath;

    public MovieAdapter(List<Movie> results) {
        this.movieResults = results;
        imageUrlPath = "https://image.tmdb.org/t/p/w185"; //TODO Retrieve this from db Configuration
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.movie_item, parent, shouldAttachToParentImmediately);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieResults.get(position);
        holder.movieTitle.setText(movie.getTitle());
        Picasso.get()
                .load(imageUrlPath+movie.getPosterPath())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.moviePosterView);
    }

    @Override
    public int getItemCount() {
        return movieResults.size();
    }

    public void setMovieResults(List<Movie> results) {
        this.movieResults = results;
        notifyDataSetChanged();
    }
}
