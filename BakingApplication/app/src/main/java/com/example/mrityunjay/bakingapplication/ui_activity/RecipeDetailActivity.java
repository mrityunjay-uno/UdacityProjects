package com.example.mrityunjay.bakingapplication.ui_activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mrityunjay.bakingapplication.R;
import com.example.mrityunjay.bakingapplication.data_model.ResponseHandler;
import com.example.mrityunjay.bakingapplication.utils.Constants;
import com.example.mrityunjay.bakingapplication.utils.Options;
import com.example.mrityunjay.bakingapplication.utils.SharedPreferences;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnDetailStepClickListener{

    private ResponseHandler recipe;
    private boolean isPinnedToWidget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_RECIPE_BUNDLE);
        recipe = bundle.getParcelable(Constants.KEY_RECIPE);
        int stepId = bundle.getInt(Constants.KEY_STEP_ID);

        if (savedInstanceState == null){
            addDetailFragment(stepId);
        }
    }

    @Override
    public void onDetailStepClicked(int position) {
        replaceDetailFragment(position);
    }

    private void addDetailFragment(int stepId) {
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        detailFragment.setArguments(getArgsForDetailFragment(stepId));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail_container, detailFragment)
                .commit();
    }

    private void replaceDetailFragment(int position) {
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        detailFragment.setArguments(getArgsForDetailFragment(position));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_container, detailFragment)
                .commit();
    }

    @NonNull
    private Bundle getArgsForDetailFragment(int position) {
        Bundle bundle = new Bundle();
        if (position == 0){
            bundle.putParcelableArrayList(Constants.KEY_INGREDIENT_LIST,
                    (ArrayList) recipe.getIngredients());
            bundle.putInt(Constants.KEY_RECIPE_SERVINGS, recipe.getServings());
        } else {
            bundle.putParcelable(Constants.KEY_STEP, recipe.getSteps().get(position - 1));
        }

        bundle.putString(Constants.KEY_RECIPE_NAME, recipe.getName());
        bundle.putInt(Constants.KEY_STEP_ID, position);
        int stepCount = recipe.getSteps().size();
        int actualStepCount = stepCount - 1;
        bundle.putInt(Constants.KEY_STEP_COUNT, actualStepCount);


        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        String nameInPrefs = SharedPreferences.getRecipeNameFromPreferences(this);
        isPinnedToWidget = nameInPrefs.contentEquals(recipe.getName());
        if(isPinnedToWidget){
            MenuItem pinToWidget = menu.getItem(0);
            pinToWidget.setIcon(R.drawable.ic_action_show_in_widget_enabled);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_pin_to_widget:
                if (!isPinnedToWidget){
                    Options.pinToWidget(this, getApplication(), item, recipe);
                } else {
                    Options.removeFromWidget(this, getApplication(), item);
                }
                return true;
        }

        super.onOptionsItemSelected(item);

        return false;
    }

}

