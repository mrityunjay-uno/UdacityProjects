package com.example.mrityunjay.bakingapplication.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.mrityunjay.bakingapplication.R;
import com.example.mrityunjay.bakingapplication.data_model.Ingredients;
import com.example.mrityunjay.bakingapplication.utils.SharedPreferences;

import java.util.List;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteWidgetViewsFactory(getApplicationContext());
    }
}

class RemoteWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Ingredients> ingredients;
    private Context context;

    public RemoteWidgetViewsFactory(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        ingredients = SharedPreferences.getIngredientListFromPreferences(context);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredients ingredient = ingredients.get(position);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
        remoteViews.setTextViewText(R.id.widget_list_item_ingredient, ingredient.getIngredient());
        remoteViews.setTextViewText(R.id.widget_list_item_quantity_with_measure,
                ingredient.getQuantityWithMeasure());

        Intent fillInIntent = new Intent();
        remoteViews.setOnClickFillInIntent(R.id.widget_list_item_container, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
