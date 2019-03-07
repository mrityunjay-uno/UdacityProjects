package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.util.Constants;
import com.example.android.popularmovies.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviePosterHolder> {

    Context context;
    public List<Movie> mMovieList;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public  MovieListAdapter(List<Movie> _mMovieList, ListItemClickListener listener)
    {
        this.mMovieList = _mMovieList;
        mOnClickListener = listener;

    }


    @Override
    public MoviePosterHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.grid_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MoviePosterHolder viewHolder = new MoviePosterHolder(view);

        return viewHolder;
    }

    @Override
    public void  onBindViewHolder(MoviePosterHolder holder, int position) {
        holder.bind(context, position);
    }

    @Override
    public int getItemCount() {
        if(mMovieList != null) {
            return mMovieList.size();
        }

        return 0;
    }


    class MoviePosterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView moviePosterView;

        public MoviePosterHolder(View itemView) {
            super(itemView);

            moviePosterView = (ImageView) itemView.findViewById(R.id.imageView);
            moviePosterView.setOnClickListener(this);
        }


        void bind(Context context, int position) {

            String baseURL = Constants.POSTER_BASE_URL;
            String size = Constants.POSTER_SIZE;

            if(mMovieList != null && mMovieList.size() >= position) {

                Movie movie = mMovieList.get(position);
                String posterURL = NetworkUtils.buildPosterUrl(baseURL,size,movie.getPoster_path());
                Picasso.with(context).load(posterURL).into(moviePosterView);
            }
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
