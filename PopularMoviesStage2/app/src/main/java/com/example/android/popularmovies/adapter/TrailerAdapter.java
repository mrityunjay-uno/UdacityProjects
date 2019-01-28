package com.example.android.popularmovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Trailer;

import java.util.ArrayList;
import static com.example.android.popularmovies.util.Constants.*;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    ArrayList<Trailer> mVideosItems;
    private Context context;

    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer video);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView _VideoName;
        public ImageView _VideoImage;

        public ViewHolder(View view) {
            super(view);
            _VideoName = (TextView)itemView.findViewById(R.id.tvVideoName);
            _VideoImage = (ImageView)itemView.findViewById(R.id.ivVideoImage);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer video = mVideosItems.get(adapterPosition);
            mClickHandler.onClick(video);
            final String trailerId = mVideosItems.get(adapterPosition).getKey();
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_INTENT + ":" + trailerId));
                context.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + trailerId));
                context.startActivity(intent);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_cell;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder._VideoName.setText(String.valueOf(mVideosItems.get(position).getName()));
    }


    @Override
    public int getItemCount() {
        if (null == mVideosItems)
            return 0;
        else {
            return mVideosItems.size();
        }
    }

    public void setVideoData(ArrayList<Trailer> videoData) {
        mVideosItems = videoData;
        notifyDataSetChanged();
    }
}
