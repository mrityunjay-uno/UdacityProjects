package com.example.mrityunjay.bakingapplication.ui_activity;

import android.content.Intent;
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

public class DetailActivity extends AppCompatActivity implements
        DetailListFragment.OnMasterListStepClickListener,
        RecipeDetailFragment.OnDetailStepClickListener{

    public static final int GET_RECIPE_FROM_SHARED_PREFS = 9321;
    public static final String KEY_GET_RECIPE_FROM_SHARED_PREFS = "get_recipe_from_shared_prefs";

    private boolean isTwoPane;
    private ResponseHandler recipe;

    private boolean isPinnedToWidget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle;

        Intent intent = getIntent();

        if (intent.hasExtra(Constants.KEY_RECIPE_BUNDLE)){
            bundle = getIntent().getBundleExtra(Constants.KEY_RECIPE_BUNDLE);
            recipe = bundle.getParcelable(Constants.KEY_RECIPE);
        } else {
            recipe = SharedPreferences.getRecipeFromPreferences(this);
            bundle = new Bundle();
            bundle.putParcelable(Constants.KEY_RECIPE, recipe);
        }

        if(savedInstanceState == null) addMasterListFragment(bundle);

        if (findViewById(R.id.detail_container) != null) {
            isTwoPane = true;
            // populate detail view
            if (savedInstanceState == null) {
                addDetailFragment();
            }
        } else {
            isTwoPane = false;
        }
    }

    @Override
    public void onMasterListStepClicked(int position) {
        if (isTwoPane) {
            replaceDetailFragment(position);
        } else {
            startDetailActivity(position);
        }
    }

    @Override
    public void onDetailStepClicked(int position) {
        if (isTwoPane) {
            getSupportFragmentManager().popBackStack();
            replaceDetailFragment(position);
        }
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
                    isPinnedToWidget = true;
                } else {
                    Options.removeFromWidget(this, getApplication(), item);
                    isPinnedToWidget = false;
                }
                return true;
        }

        super.onOptionsItemSelected(item);

        return false;
    }

    private void addMasterListFragment(Bundle bundle) {
        DetailListFragment masterListFragment = new DetailListFragment();
        masterListFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.master_list_container, masterListFragment)
                .commit();
    }

    private void addDetailFragment() {
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        detailFragment.setArguments(getArgsForDetailFragment
                (Constants.DEFAULT_FRAGMENT_DETAIL_ITEM));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void replaceDetailFragment(int position) {
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        detailFragment.setArguments(getArgsForDetailFragment(position));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void startDetailActivity(int position) {
        Intent intent = new Intent(DetailActivity.this,
                RecipeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_RECIPE, recipe);
        bundle.putInt(Constants.KEY_STEP_ID, position);

        intent.putExtra(Constants.KEY_RECIPE_BUNDLE, bundle);
        startActivity(intent);
    }

    @NonNull
    private Bundle getArgsForDetailFragment(int position) {
        Bundle bundle = new Bundle();
        if (position == 0) {
            bundle.putParcelableArrayList(Constants.KEY_INGREDIENT_LIST,
                    (ArrayList) recipe.getIngredients());
            bundle.putInt(Constants.KEY_RECIPE_SERVINGS, recipe.getServings());
        } else {
            bundle.putParcelable(Constants.KEY_STEP, recipe.getSteps().get(position - 1));
        }

        bundle.putString(Constants.KEY_RECIPE_NAME, recipe.getName());
        bundle.putInt(Constants.KEY_STEP_ID, position);
        int stepCount = recipe.getSteps().size();
        int actualStepCount = stepCount - 2;
        bundle.putInt(Constants.KEY_STEP_COUNT, actualStepCount);

        return bundle;
    }
}
