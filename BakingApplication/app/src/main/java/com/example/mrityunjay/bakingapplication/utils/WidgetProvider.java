package com.example.mrityunjay.bakingapplication.utils;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RemoteViews;

import com.example.mrityunjay.bakingapplication.R;
import com.example.mrityunjay.bakingapplication.service.WidgetService;
import com.example.mrityunjay.bakingapplication.ui_activity.DetailActivity;
import com.example.mrityunjay.bakingapplication.ui_activity.MainActivity;

public class WidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        String name = SharedPreferences.getRecipeNameFromPreferences(context);
        if(name != null && !name.isEmpty()){
            views.setViewVisibility(R.id.widget_empty_state_text, View.GONE);

            views.setViewVisibility(R.id.widget_list_view, View.VISIBLE);

            views.setTextViewText(R.id.widget_recipe_name, name);

            PendingIntent backStackPendingIntent =
                    getPendingIntentWithBackStack(
                            context,
                            getIntentForSelectedRecipe(context));

            views.setOnClickPendingIntent(R.id.widget_recipe_name, backStackPendingIntent);
            views.setPendingIntentTemplate(R.id.widget_list_view, backStackPendingIntent);

            Intent widgetServiceIntent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_list_view, widgetServiceIntent);
        } else {
            views.setViewVisibility(R.id.widget_list_view, View.GONE);

            views.setViewVisibility(R.id.widget_empty_state_text, View.VISIBLE);

            views.setTextViewText(R.id.widget_recipe_name, context.getString(R.string.app_name));

            Intent recipeListActivityIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    recipeListActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.widget_empty_state_text, pendingIntent);
            views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private static PendingIntent getPendingIntentWithBackStack(Context context,
                                                               Intent launchActivityIntent) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntentWithParentStack(launchActivityIntent);
        return stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @NonNull
    private static Intent getIntentForSelectedRecipe(Context context) {
        Intent launchActivityIntent = new Intent(context, DetailActivity.class);
        launchActivityIntent.putExtra(DetailActivity.KEY_GET_RECIPE_FROM_SHARED_PREFS,
                DetailActivity.GET_RECIPE_FROM_SHARED_PREFS);
        return launchActivityIntent;
    }


    public static void updateAppWidgets (Context context, AppWidgetManager appWidgetManager,
                                         int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}
