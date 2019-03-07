package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.util.Constants;
import com.example.android.popularmovies.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerPosterHolder> {

    Context context;
    public List<Trailer> mTrailerList;


    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public  TrailerAdapter(List<Trailer> _mTrailerList, ListItemClickListener listener)
    {
        this.mTrailerList = _mTrailerList;
        mOnClickListener = listener;

    }

    @Override
    public TrailerPosterHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailerPosterHolder viewHolder = new TrailerPosterHolder(view);

        return viewHolder;
    }

    @Override
    public void  onBindViewHolder(TrailerPosterHolder holder, int position) {
        holder.bind(context, position);
    }

    @Override
    public int getItemCount() {
        if(mTrailerList != null) {
            return mTrailerList.size();
        }

        return 0;
    }


    class TrailerPosterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView trailerPosterView;
        TextView trailerNameView;
        TextView trailerTypeView;


        public TrailerPosterHolder(View itemView) {
            super(itemView);

            trailerPosterView = (ImageView) itemView.findViewById(R.id.imageView);
            trailerPosterView.setOnClickListener(this);

            trailerNameView = (TextView) itemView.findViewById(R.id.trailer_name);
            trailerTypeView = (TextView) itemView.findViewById(R.id.trailer_type);

        }

        void bind(Context context, int position) {

            String baseURL = Constants.YOUTUBE_POSTER_BASE_URL;
            String size = Constants.YOUTUBE_POSTER_SIZE;

            if(mTrailerList != null && mTrailerList.size() >= position) {

                Trailer trailer = mTrailerList.get(position);
                String posterURL = NetworkUtils.buildYoutubePosterUrl(baseURL,size,trailer.getKey());
                Picasso.with(context).load(posterURL).into(trailerPosterView);

                String trailerName = trailer.getName();
                trailerNameView.setText(trailerName);

                String trailerType = trailer.getType();
                trailerTypeView.setText(trailerType);
            }
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
