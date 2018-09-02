package za.co.jcarklin.popularmovies;

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
import za.co.jcarklin.popularmovies.api.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieResults = new ArrayList<>(20);
    private static String imageUrlPath;
    private final MovieAdapterOnClickHandler movieAdapterOnClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie selectedMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, int availableWidth) {
        imageUrlPath = ("https://image.tmdb.org/t/p/"); //TODO Retrieve this from db Configuration and store
        movieAdapterOnClickHandler = clickHandler;
        calculateImageWidthRequired(availableWidth);
    }

    private void calculateImageWidthRequired(int availableWidth) {
        int width = availableWidth;
        int[] imageWidthOptions = {92, 154, 185, 342, 500, 780}; //TODO Retrieve this from db Configuration and store
        int option1, option2;
        for (int i = 0; i < imageWidthOptions.length - 1; i++) {
            option1 = imageWidthOptions[i];
            option2 = imageWidthOptions[i + 1];
            if (availableWidth - option1 >= option2 - availableWidth) {
                width = option2;
            } else {
                width = option1;
                break;
            }
        }
        imageUrlPath+=("w"+width);
    }

    public static String getImageUrlPath() {
        return imageUrlPath;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieResults.get(position);
        Picasso.get()
                .load(imageUrlPath+movie.getPosterPath())
                .into(holder.moviePosterView);
        //TODO add placeholder and error
    }

    @Override
    public int getItemCount() {
        return movieResults.size();
    }

    public void setMovieResults(List<Movie> results) {
        this.movieResults = results;
        notifyDataSetChanged();
    }

    //ViewHolder Class
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster)
        ImageView moviePosterView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = movieResults.get(adapterPosition);
            movieAdapterOnClickHandler.onClick(selectedMovie);
        }
    }
}
