package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Contract;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.util.Constants.BASE_IMAGE_URL;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private final FavoriteAdapter.FavoriteAdapterOnClickHandler mClickHandler;

    public interface FavoriteAdapterOnClickHandler {
        void onClick(long id);
    }

    public FavoriteAdapter(@NonNull Context context, FavoriteAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView favoriteMoviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            favoriteMoviePoster = (ImageView) itemView.findViewById(R.id.ivMoviePoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int movieIdIndex = mCursor.getColumnIndex(Contract.MovieColumn.COLUMN_MOVIES_ID);
            long id = mCursor.getLong(movieIdIndex);
            mClickHandler.onClick(id);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.favorit_list_cell;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        int idIndex = mCursor.getColumnIndex(Contract.MovieColumn._ID);
        int movieIdIndex = mCursor.getColumnIndex(Contract.MovieColumn.COLUMN_MOVIES_ID);
        int moviePosterPathIndex = mCursor.getColumnIndex(Contract.MovieColumn.COLUMN_POSTER);
        int movieNameIndex = mCursor.getColumnIndex(Contract.MovieColumn.COLUMN_TITLE);

        final int id = mCursor.getInt(idIndex);
        int movieId = mCursor.getInt(movieIdIndex);
        String moviePosterPath = mCursor.getString(moviePosterPathIndex);

        String fullPosterPath = BASE_IMAGE_URL + moviePosterPath;

        holder.itemView.setTag(id);
        String movieIdString = "" + movieId;
        Picasso.with(this.mContext).load(fullPosterPath).placeholder(R.drawable.defaultimage).into(holder.favoriteMoviePoster);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {

        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
