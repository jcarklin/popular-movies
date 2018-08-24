package za.co.jcarklin.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import za.co.jcarklin.popularmovies.api.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> movieResults = new ArrayList<>(20);
    private String imageUrlPath;
    private int width = 0;
    private int spanCount;

    public MovieAdapter(int spanCount) {
        this.spanCount = spanCount;
        imageUrlPath = ("https://image.tmdb.org/t/p/"); //TODO Retrieve this from db Configuration and store
    }

    private void calculateImageWidthRequired(int availableWidth) {
        width = availableWidth;
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



    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (width==0) {
            calculateImageWidthRequired(parent.getMeasuredWidth()/spanCount);
        }
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
        Picasso.get()
                .load(imageUrlPath+movie.getPosterPath())
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
