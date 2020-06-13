package com.manitosdev.gcatcast.ui.main.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.manitosdev.gcatcast.R;

/**
 * Implementation of App Widget functionality.
 */
public class CCatWidget extends AppWidgetProvider {

  static void updateAppWidget(
      Context context,
      AppWidgetManager appWidgetManager,
      int appWidgetId
  ) {

    // Construct the RemoteViews object
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.c_cat_widget);

    Intent serviceIntent = new Intent(context, CCatWidgetService.class);
    remoteViews.setRemoteAdapter(R.id.widget_list, serviceIntent);


    Intent intent = new Intent(context, CCatWidget.class);
    // intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

