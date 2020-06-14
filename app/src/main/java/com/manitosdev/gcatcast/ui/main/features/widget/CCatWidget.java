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

  private static final String ACTION_SIMPLEAPPWIDGET = "ACTION_BROADCASTWIDGETSAMPLE";
  private static final String EXTRA_VAL = "EXTRA_BROADCASTWIDGETSAMPLE";


  static void updateAppWidget(
      Context context,
      AppWidgetManager appWidgetManager,
      int appWidgetId
  ) {

    // Construct the RemoteViews object
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.c_cat_widget);

    Intent intent = new Intent(context, CCatWidget.class);
    intent.setAction(ACTION_SIMPLEAPPWIDGET);

    String number = String.format("%03d Number", (new Random().nextInt(900) + 100));
    remoteViews.setTextViewText(R.id.widgetTitleTxt, number);

    intent.putExtra(EXTRA_VAL, number);

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
    if (ACTION_SIMPLEAPPWIDGET.equals(intent.getAction())) {
      playAudio(context, 0);

      String extrasVal = intent.getExtras().getString(EXTRA_VAL);
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

  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
    if (serviceBound) {
      context.unbindService(serviceConnection);
      // service is active
      player.stopSelf();
    }
  }

  private void playAudio(Context context, int audioIndex) {
    // Enter relevant functionality for when the first widget is created
    // Check is service is active
    if (!serviceBound) {
      Intent playerIntent = new Intent(context, MediaPlayerService.class);
      //playerIntent.putExtra(Broadcast_MEDIA_URL_VALUE, audioIndex);
      context.startService(playerIntent);
      context.getApplicationContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    } else {
      // Store the new audioIndex to SharedPreferences
      StorageUtil storage = new StorageUtil(context.getApplicationContext());
      storage.storeAudioIndex(0);

      // Service is active
      // Send a broadcast to the service -> PLAY_NEW_AUDIO
      Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
      //broadcastIntent.putExtra(Broadcast_MEDIA_URL_VALUE, audioIndex);
      context.sendBroadcast(broadcastIntent);
    }
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

