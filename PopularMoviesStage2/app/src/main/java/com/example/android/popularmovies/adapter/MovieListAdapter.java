package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    ArrayList<Movie> mMoviesItems;
    private Context context;
    private final MovieListAdapterOnClickHandler mClickHandler;

    public interface MovieListAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieListAdapter(MovieListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView moviePoster;

        public MovieListViewHolder(View view) {
            super(view);
            moviePoster = (ImageView)itemView.findViewById(R.id.ivItemMovie);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMoviesItems.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_cell;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        Picasso.with(this.context).load(mMoviesItems.get(position).getFullPosterPath()).placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaultimage).into(holder.moviePoster);
    }


    @Override
    public int getItemCount() {
        if (null == mMoviesItems)
            return 0;
        else {
            return mMoviesItems.size();
        }
    }

    public void setMovieData(ArrayList<Movie> movieData) {
        mMoviesItems = movieData;
        notifyDataSetChanged();
    }
}
