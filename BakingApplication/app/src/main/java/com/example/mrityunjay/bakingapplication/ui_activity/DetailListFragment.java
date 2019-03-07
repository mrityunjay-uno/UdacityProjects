package com.example.mrityunjay.bakingapplication.ui_activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrityunjay.bakingapplication.R;
import com.example.mrityunjay.bakingapplication.adapter.DetailListAdapter;
import com.example.mrityunjay.bakingapplication.data_model.ResponseHandler;
import com.example.mrityunjay.bakingapplication.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailListFragment extends Fragment implements DetailListAdapter.OnStepClickListener{

    public DetailListFragment(){}

    private ResponseHandler recipe;

    private OnMasterListStepClickListener stepClickListener;

    public interface OnMasterListStepClickListener {
        void onMasterListStepClicked(int position);
    }

    @BindView(R.id.master_list_recycler_view)
    RecyclerView masterListRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            stepClickListener = (OnMasterListStepClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnMasterListStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_list, container,
                false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if(bundle != null){
            recipe = getArguments().getParcelable(Constants.KEY_RECIPE);
            setUpMasterListView();

            Activity activity = getActivity();
            if (activity != null) {
                activity.setTitle(recipe.getName());
            }
        }

        return rootView;
    }

    @Override
    public void onStepSelected(int position) {
        stepClickListener.onMasterListStepClicked(position);
    }

    private void setUpMasterListView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DetailListAdapter adapter = new DetailListAdapter(recipe.getStepsWithIngredients(), this);
        masterListRecyclerView.setLayoutManager(layoutManager);
        masterListRecyclerView.setAdapter(adapter);
        masterListRecyclerView.setHasFixedSize(true);
    }

}
