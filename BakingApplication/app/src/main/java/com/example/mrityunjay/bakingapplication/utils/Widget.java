package com.example.mrityunjay.bakingapplication.utils;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import com.example.mrityunjay.bakingapplication.R;

public class Widget {

    public static void updateWidget(Application application) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(application);
        int[] ids = appWidgetManager
                .getAppWidgetIds(new ComponentName(application, WidgetProvider.class));

        WidgetProvider.updateAppWidgets(application, appWidgetManager, ids);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list_view);
    }
}
