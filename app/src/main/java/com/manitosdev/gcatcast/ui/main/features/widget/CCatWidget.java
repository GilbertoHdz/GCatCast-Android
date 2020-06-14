package com.manitosdev.gcatcast.ui.main.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.RemoteViews;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.features.playlist.Audio;
import com.manitosdev.gcatcast.ui.main.features.playlist.PlayerV2Activity;
import com.manitosdev.gcatcast.ui.main.features.services.MediaPlayerService;
import com.manitosdev.gcatcast.ui.main.features.services.StorageUtil;
import java.util.ArrayList;
import java.util.Random;

import static com.manitosdev.gcatcast.ui.main.features.services.MediaPlayerService.Broadcast_PLAY_NEW_AUDIO;

/**
 * Implementation of App Widget functionality.
 */
public class CCatWidget extends AppWidgetProvider {

  private MediaPlayerService player;
  static boolean serviceBound = false;
  private ArrayList<Audio> audioList; // List of available Audio files
  private Context mContext;

  static void updateAppWidget(
      Context context,
      AppWidgetManager appWidgetManager,
      int appWidgetId
  ) {

    // Construct the RemoteViews object
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.c_cat_widget);

    Intent intent = new Intent(context, CCatWidget.class);
    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

    String number = String.format("%03d", (new Random().nextInt(900) + 100));
    remoteViews.setTextViewText(R.id.widgetTitleTxt, number);

    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, number);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.widgetActionPlay, pendingIntent);

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
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
      String extrasVal = intent.getExtras().getString(AppWidgetManager.EXTRA_APPWIDGET_IDS);
      // Construct the RemoteViews object
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.c_cat_widget);
      views.setTextViewText(R.id.widgetAuthorTxt, extrasVal);
      // This time we dont have widgetId. Reaching our widget with that way.
      ComponentName appWidget = new ComponentName(context, CCatWidget.class);
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      // Instruct the widget manager to update the widget
      appWidgetManager.updateAppWidget(appWidget, views);
    }

  }

  @Override
  public void onEnabled(Context context) {
    /*mContext = context;

    Intent playerIntent;
    // Enter relevant functionality for when the first widget is created
    //Check is service is active
    if (!serviceBound) {
      playerIntent = new Intent(mContext, MediaPlayerService.class);
      //playerIntent.putExtra(Broadcast_MEDIA_URL_VALUE, audioIndex);
      mContext.startService(playerIntent);
      mContext.bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    } else {
      // Service is active
      // Send a broadcast to the service -> PLAY_NEW_AUDIO
      playerIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
      //broadcastIntent.putExtra(Broadcast_MEDIA_URL_VALUE, audioIndex);
      mContext.sendBroadcast(playerIntent);
    }*/
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
    /*if (serviceBound) {
      context.unbindService(serviceConnection);
      // service is active
      player.stopSelf();
    }*/
  }

  //Binding this Client to the AudioPlayer Service
  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      // We've bound to LocalService, cast the IBinder and get LocalService instance
      MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
      player = binder.getService();
      serviceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      serviceBound = false;
    }
  };
}

