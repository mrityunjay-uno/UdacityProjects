package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewDetalHolder> {

    Context context;
    public List<Review> mReviewList;

    public  ReviewAdapter(List<Review> mReviewList)
    {
        this.mReviewList = mReviewList;

    }

    @Override
    public ReviewAdapter.ReviewDetalHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewAdapter.ReviewDetalHolder viewHolder = new ReviewAdapter.ReviewDetalHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewDetalHolder holder, int position) {
        holder.bind(context, position);
    }

    @Override
    public int getItemCount() {
        if (mReviewList != null) {
            return mReviewList.size();
        }

        return 0;
    }


    class ReviewDetalHolder extends RecyclerView.ViewHolder {

        TextView authorNameView;
        TextView contentView;


        public ReviewDetalHolder(View itemView) {
            super(itemView);


            authorNameView = (TextView) itemView.findViewById(R.id.author_name);
            contentView = (TextView) itemView.findViewById(R.id.review_content);

        }

        void bind(Context context, int position) {

            if (mReviewList != null && mReviewList.size() >= position) {

                Review review = mReviewList.get(position);


                String authorName = review.getAuthor();
                authorNameView.setText(authorName);

                String reviewContent = review.getContent();
                contentView.setText(reviewContent);
            }
        }
    }
}
