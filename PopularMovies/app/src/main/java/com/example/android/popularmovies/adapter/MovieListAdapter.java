package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.util.JsonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.popularmovies.util.Constants.BASE_IMAGE_URL;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ArrayList<Movie> mMovies;
    private Context context;

    // data is passed into the constructor
    public MovieListAdapter(Context context, String json) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        mMovies = JsonUtils.parseMovieJson(json);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_list_cell, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String movieTitle = ((Movie)mMovies.get(position)).getMovieTitle();
        String moviePoster = BASE_IMAGE_URL + ((Movie)mMovies.get(position)).getMovieImage();

        Picasso.with(this.context)
                .load(moviePoster)
                .into(holder.mPosterImage);
        holder.mTitleText.setText(movieTitle);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitleText;
        ImageView mPosterImage;
        ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.tvMovieTitle);
            mPosterImage = itemView.findViewById(R.id.ivMovieThemeImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Movie getItem(int index) {
        return mMovies.get(index);
    }

    // allows clicks events to be caught
    public void setClickListener(MainActivity itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
