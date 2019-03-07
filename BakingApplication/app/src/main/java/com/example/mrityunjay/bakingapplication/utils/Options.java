package com.example.mrityunjay.bakingapplication.utils;

import android.app.Application;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mrityunjay.bakingapplication.R;
import com.example.mrityunjay.bakingapplication.data_model.ResponseHandler;

public class Options {

    public static void removeFromWidget(Context context, Application application, MenuItem item) {
        SharedPreferences.removeFromPreferences(context);
        Widget.updateWidget(application);
        item.setIcon(R.drawable.ic_action_show_in_widget_default);

        Toast.makeText(context, R.string.msg_removed_from_widget, Toast.LENGTH_SHORT)
                .show();
    }

    public static void pinToWidget(Context context, Application application, MenuItem item,
                                   ResponseHandler recipe) {
        SharedPreferences.saveRecipeInPreferences(context, recipe);
        Widget.updateWidget(application);
        item.setIcon(R.drawable.ic_action_show_in_widget_enabled);

        Toast.makeText(context, R.string.msg_pinned_to_widget, Toast.LENGTH_SHORT)
                .show();
    }
}
