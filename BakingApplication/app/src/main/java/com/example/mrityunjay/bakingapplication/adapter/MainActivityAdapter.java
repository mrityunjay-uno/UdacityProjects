package com.example.mrityunjay.bakingapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrityunjay.bakingapplication.R;
import com.example.mrityunjay.bakingapplication.data_model.ResponseHandler;
import com.example.mrityunjay.bakingapplication.data_model.Steps;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    private Context context;
    private List<ResponseHandler> recipeList;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener{
        void onRecipeSelected(int position);
    }

    public MainActivityAdapter(Context context, List<ResponseHandler> recipeList, OnRecipeClickListener listener) {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityAdapter.ViewHolder holder, int position) {
        ResponseHandler recipe = recipeList.get(position);
        holder.name.setText(recipe.getName());
        String imageUrl = recipe.getImage();
        boolean imageFound = false;

        if (TextUtils.isEmpty(imageUrl)){
            List<Steps> steps = recipe.getSteps();

            // go through each step searching for thumbnail
            for (Steps step : steps){
                String thumbnailUrl = step.getThumbnailURL();
                if(thumbnailUrl != null && !thumbnailUrl.isEmpty()){
                    imageUrl = thumbnailUrl;
                    imageFound = true;
                    // break out of loop if image url found
                    break;
                }
            }

        } else {
            imageFound = true;
        }

        if (imageFound){
            Picasso.with(context).load(imageUrl).into(holder.recipeImageView);
        }

    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    public void clear(){
        int size = getItemCount();
        this.recipeList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<ResponseHandler> recipeList){
        this.recipeList.clear();
        this.recipeList.addAll(recipeList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        private OnRecipeClickListener listener;

        @BindView(R.id.list_item_name)
        TextView name;
        @BindView(R.id.recipe_image_view)
        ImageView recipeImageView;

        ViewHolder(View itemView, OnRecipeClickListener listener){
            super(itemView);

            this.listener = listener;

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecipeSelected(getAdapterPosition());
        }
    }
}
