package com.example.mrityunjay.bakingapplication.ui_activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mrityunjay.bakingapplication.BakingApplication;
import com.example.mrityunjay.bakingapplication.R;
import com.example.mrityunjay.bakingapplication.adapter.MainActivityAdapter;
import com.example.mrityunjay.bakingapplication.data_model.ResponseHandler;
import com.example.mrityunjay.bakingapplication.utils.Client;
import com.example.mrityunjay.bakingapplication.utils.CommonIdelingResource;
import com.example.mrityunjay.bakingapplication.utils.ConnectionReceiver;
import com.example.mrityunjay.bakingapplication.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  implements MainActivityAdapter.OnRecipeClickListener, ConnectionReceiver.ConnectivityReceiverListener {

    private List<ResponseHandler> recipeList;

    @BindView(R.id.recipe_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recipe_list_empty_state_text)
    TextView recipeEmptyStateText;
    @BindView(R.id.recipe_list_progress_bar)
    ProgressBar recipeListProgressBar;
    @BindView(R.id.recipe_list_main_layout)
    ConstraintLayout constraintLayout;

    @Nullable
    CommonIdelingResource idlingResource;

    private boolean isWaitingForInternetConnection;
    private boolean hasLoadedRecipes = false;

    private ConnectionReceiver connectivityReceiver;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new CommonIdelingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipeList = new ArrayList<>();

        getIdlingResource();
        getRecipesIfConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityReceiver = new ConnectionReceiver();
            this.registerReceiver(connectivityReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }


        BakingApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (connectivityReceiver != null) {
            this.unregisterReceiver(connectivityReceiver);
        }
    }

    private void getRecipesIfConnected() {
        if (ConnectionReceiver.isConnected()) {
            getRecipes();
        } else {
            hasLoadedRecipes = false;
            showEmptyStateNoConnection();
        }
    }

    private void getRecipes() {
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        showLoading();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Client.RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Client client = retrofit.create(Client.class);

        Call<List<ResponseHandler>> call = client.getRecipes();

        call.enqueue(new Callback<List<ResponseHandler>>() {
            @Override
            public void onResponse(Call<List<ResponseHandler>> call, Response<List<ResponseHandler>> response) {
                recipeList = response.body();
                setUpUi();

                hasLoadedRecipes = true;

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }

                showRecipes();
            }

            @Override
            public void onFailure(Call<List<ResponseHandler>> call, Throwable t) {
                hasLoadedRecipes = false;
                showEmptyStateError();
                recipeEmptyStateText.setText(R.string.recipe_list_empty_error);
            }
        });
    }

    private void setUpUi() {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
        }

        MainActivityAdapter adapter = new MainActivityAdapter(this, recipeList, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onRecipeSelected(int position) {
        ResponseHandler recipe = recipeList.get(position);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_RECIPE, recipe);

        Intent intent = new Intent(MainActivity.this,
                DetailActivity.class);

        intent.putExtra(Constants.KEY_RECIPE_BUNDLE, bundle);

        startActivity(intent);
    }

    private void showLoading() {
        recipeListProgressBar.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.INVISIBLE);
        recipeEmptyStateText.setVisibility(View.INVISIBLE);
    }

    private void showRecipes() {
        recyclerView.setVisibility(View.VISIBLE);

        recipeListProgressBar.setVisibility(View.INVISIBLE);
        recipeEmptyStateText.setVisibility(View.INVISIBLE);
    }

    private void showEmptyStateNoConnection() {
        recipeEmptyStateText.setVisibility(View.VISIBLE);
        recipeEmptyStateText.setText(R.string.recipe_list_empty_no_connection);

        recipeListProgressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showEmptyStateError() {
        recipeEmptyStateText.setVisibility(View.VISIBLE);
        recipeEmptyStateText.setText(R.string.recipe_list_empty_error);

        recipeListProgressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (isWaitingForInternetConnection) {
                if (!hasLoadedRecipes) {
                    getRecipes();
                }
                isWaitingForInternetConnection = false;
            }
        } else {
            if (!isWaitingForInternetConnection) {
                showSnackbar(getString(R.string.connectivity_lost_msg));
                isWaitingForInternetConnection = true;
            }

        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
