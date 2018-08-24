package za.co.jcarklin.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_movie_poster)
    ImageView moviePosterView;
    @BindView(R.id.tv_movie_title)
    TextView movieTitle;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
