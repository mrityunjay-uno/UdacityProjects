package com.example.mrityunjay.bakingapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mrityunjay.bakingapplication.R;
import com.example.mrityunjay.bakingapplication.data_model.Steps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder> {

    private List<Steps> recepieSteps;
    private OnStepClickListener listener;

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    public DetailListAdapter(List<Steps> steps, OnStepClickListener listener) {
        this.recepieSteps = steps;
        this.listener = listener;
    }


    @NonNull
    @Override
    public DetailListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_step, parent, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailListAdapter.ViewHolder holder, int position) {

        if (position > 1){
            String itemNr = String.valueOf(position - 1) + ".";
            holder.itemNr.setText(itemNr);
        }

        holder.shortDescription.setText(recepieSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return recepieSteps != null ? recepieSteps.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private OnStepClickListener listener;

        @BindView(R.id.short_description)
        TextView shortDescription;
        @BindView(R.id.item_nr)
        TextView itemNr;
        @BindView(R.id.ingredients)
        TextView ingredients;

        ViewHolder(View itemView, OnStepClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();
            listener.onStepSelected(pos);
        }
    }
}
