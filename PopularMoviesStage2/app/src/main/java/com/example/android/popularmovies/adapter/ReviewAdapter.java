package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    ArrayList<Review> mCommentsItems;
    private Context context;

    public ReviewAdapter(ArrayList<Review> commentsItems) {
        mCommentsItems = commentsItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mUser;
        public final TextView mComment;

        public ViewHolder(View view) {
            super(view);
            mUser = (TextView)itemView.findViewById(R.id.tvUser);
            mComment = (TextView)itemView.findViewById(R.id.tvComment);
        }
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_comment_list_cell;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        holder.mUser.setText(String.valueOf(mCommentsItems.get(position).getAuthor()));
        holder.mComment.setText(String.valueOf(mCommentsItems.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        if (null == mCommentsItems)
            return 0;
        else {
            return mCommentsItems.size();
        }
    }

    public void setCommentData(ArrayList<Review> commentData) {
        mCommentsItems = commentData;
        notifyDataSetChanged();
    }
}
