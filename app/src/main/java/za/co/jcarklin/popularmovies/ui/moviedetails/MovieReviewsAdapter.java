package za.co.jcarklin.popularmovies.ui.moviedetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.jcarklin.popularmovies.R;
import za.co.jcarklin.popularmovies.repository.model.MovieReview;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsViewHolder> {

    private List<MovieReview> movieReviewResults = new ArrayList<>(20);
//    private final MovieReviewAdapterOnClickHandler movieAdapterOnClickHandler;

//    public interface MovieReviewAdapterOnClickHandler {
//        void onClickReview(MovieReview selectedReview);
//    }

    public MovieReviewsAdapter() {//(MovieReviewAdapterOnClickHandler clickHandler) {
//        movieAdapterOnClickHandler = clickHandler;
    }

    public void setMovieReviewResults(List<MovieReview> results) {
        this.movieReviewResults = results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_review_item, parent, false);
        return new MovieReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsViewHolder holder, int position) {
        MovieReview movie = movieReviewResults.get(position);
        holder.author.setText(movie.getAuthor());
        holder.review.setText(movie.getAbridgedContent());
        holder.setShowMoreVisibility(movie.isShowingAbridged()?View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return movieReviewResults.size();
    }

    //ViewHolder Class
    public class MovieReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_author)
        TextView author;
        @BindView((R.id.tv_review))
        TextView review;
        @BindView((R.id.tv_more_review))
        TextView showMore;

        MovieReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieReview selectedMovie = movieReviewResults.get(adapterPosition);
            if (selectedMovie.isShowingAbridged()) {
                review.setText(selectedMovie.getContent());
                showMore.setText("Show Less");
            } else {
                review.setText(selectedMovie.getAbridgedContent());
                showMore.setText("Show More");
            }
        }

        public void setShowMoreVisibility(int visibility) {
            showMore.setVisibility(visibility);
        }
    }
}
