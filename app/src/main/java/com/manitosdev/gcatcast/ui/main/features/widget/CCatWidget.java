package com.manitosdev.gcatcast.ui.main.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.RemoteViews;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.db.AppDatabase;
import com.manitosdev.gcatcast.ui.main.db.entities.PlaylistEntity;
import com.manitosdev.gcatcast.ui.main.features.common.models.PlaylistData;
import com.manitosdev.gcatcast.ui.main.features.playlist.Audio;
import com.manitosdev.gcatcast.ui.main.features.services.MediaPlayerService;
import com.manitosdev.gcatcast.ui.main.features.services.StorageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.manitosdev.gcatcast.ui.main.features.services.MediaPlayerService.Broadcast_PLAY_NEW_AUDIO;

/**
 * Implementation of App Widget functionality.
 */
public class CCatWidget extends AppWidgetProvider {

  private MediaPlayerService player;
  static boolean serviceBound = false;
  static boolean isPlay = false;
  private static ArrayList<Audio> audioList = new ArrayList<>(); // List of available Audio files

  private static final String ACTION_PLAY = "ACTION_G_CAT_CAST_PLAY";
  private static final String ACTION_NEXT = "ACTION_G_CAT_CAST_NEXT";
  private static final String ACTION_PREV = "ACTION_G_CAT_CAST_PREV";
  private static final String EXTRA_VAL = "EXTRA_G_CAT_CAST_EXTRA_VAL";

  static void updateAppWidget(
      Context context,
      AppWidgetManager appWidgetManager,
      int appWidgetId
  ) {

    // Construct the RemoteViews object
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.c_cat_widget);

    Intent playIntent = new Intent(context, CCatWidget.class);
    playIntent.setAction(ACTION_PLAY);

    Intent nextIntent = new Intent(context, CCatWidget.class);
    nextIntent.setAction(ACTION_NEXT);

    Intent prevIntent = new Intent(context, CCatWidget.class);
    prevIntent.setAction(ACTION_PREV);

    String number = String.format("%03d Number", (new Random().nextInt(900) + 100));
    remoteViews.setTextViewText(R.id.widgetTitleTxt, number);

    playIntent.putExtra(EXTRA_VAL, number);

    PendingIntent pendingIntent;
    pendingIntent = PendingIntent.getBroadcast(context, 7, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.widgetActionPlay, pendingIntent);

    pendingIntent = PendingIntent.getBroadcast(context, 8, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.widgetActionPrev, pendingIntent);

    pendingIntent = PendingIntent.getBroadcast(context, 9, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.widgetActionNext, pendingIntent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
    loadAsyncData(context);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    // Construct the RemoteViews object
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.c_cat_widget);
    Intent servicesPlayAction = new Intent(context, MediaPlayerService.class);

    StorageUtil storage = new StorageUtil(context.getApplicationContext());
    int audioIndex = storage.loadAudioIndex() + 1;
    if (audioIndex == audioList.size()) audioIndex = 0;

    if (ACTION_NEXT.equals(intent.getAction())) {
      // Next
      servicesPlayAction.setAction(MediaPlayerService.ACTION_NEXT);
      context.startService(servicesPlayAction);

      remoteViews.setTextViewText(R.id.widgetAuthorTxt, "Next");
      remoteViews.setTextViewText(R.id.widgetTitleTxt, audioList.get(audioIndex).getTitle());
      remoteViews.setTextViewText(R.id.widgetAuthorTxt, audioList.get(audioIndex).getArtist());
      updatedAppWidgetManager(context, remoteViews);
    }

    if (ACTION_PREV.equals(intent.getAction())) {
      // Prev
      servicesPlayAction.setAction(MediaPlayerService.ACTION_PREVIOUS);
      context.startService(servicesPlayAction);

      remoteViews.setTextViewText(R.id.widgetAuthorTxt, "Prev");
      remoteViews.setTextViewText(R.id.widgetTitleTxt, audioList.get(audioIndex).getTitle());
      remoteViews.setTextViewText(R.id.widgetAuthorTxt, audioList.get(audioIndex).getArtist());
      updatedAppWidgetManager(context, remoteViews);
    }

    if (ACTION_PLAY.equals(intent.getAction())) {
      isPlay = !isPlay;

      if (isPlay) {
        if (!serviceBound) {
          playAudio(context);
        }

        // Play
        servicesPlayAction.setAction(MediaPlayerService.ACTION_PLAY);

        remoteViews.setImageViewResource(R.id.widgetActionPlay, R.drawable.exo_controls_pause);
      } else {
        // Pause
        servicesPlayAction.setAction(MediaPlayerService.ACTION_PAUSE);

        remoteViews.setImageViewResource(R.id.widgetActionPlay, R.drawable.exo_controls_play);
      }

      context.startService(servicesPlayAction);

      remoteViews.setTextViewText(R.id.widgetTitleTxt, audioList.get(audioIndex).getTitle());
      remoteViews.setTextViewText(R.id.widgetAuthorTxt, audioList.get(audioIndex).getArtist());

      updatedAppWidgetManager(context, remoteViews);
    }
  }

  private void updatedAppWidgetManager(Context context, RemoteViews remoteViews) {
    // This time we don't have widgetId. Reaching our widget with that way.
    ComponentName appWidget = new ComponentName(context, CCatWidget.class);
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidget, remoteViews);
  }

  @Override
  public void onEnabled(Context context) {

  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
    if (serviceBound) {
      context.getApplicationContext().unbindService(serviceConnection);
      // service is active
      player.stopSelf();
    }
  }

  private void playAudio(Context context) {
    // Enter relevant functionality for when the first widget is created
    // Check is service is active
    if (!serviceBound) {
      Intent playerIntent = new Intent(context, MediaPlayerService.class);
      //playerIntent.putExtra(Broadcast_MEDIA_URL_VALUE, audioIndex);
      context.getApplicationContext().startService(playerIntent);
      context.getApplicationContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    } else {
      // Service is active
      // Send a broadcast to the service -> PLAY_NEW_AUDIO
      Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
      //broadcastIntent.putExtra(Broadcast_MEDIA_URL_VALUE, audioIndex);
      context.getApplicationContext().sendBroadcast(broadcastIntent);
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

  private static void loadDefaultPlaylist() {
    String defaultUrl = "http://media-podcast.open.ac.uk/feeds/greek-heroes/desktop-all/greekheroesaudio01.mp3";
    String defaultName = "Achilles";
    String defaultAuthor = "The Open University";
    String defaultDesc = "The lowdown on what popular culture chooses to keep in its portrayals of the Greek hero Achilles … and what gets left out.";
    audioList.add(new Audio(defaultUrl, defaultName, defaultAuthor, defaultDesc));
  }

  private static void loadAsyncData(Context context) {
    new AsyncTask<Context, Void, List<Audio>>() {

      @Override
      protected List<Audio> doInBackground(Context... contexts) {
        AppDatabase database = AppDatabase.getInstance(context);
        List<PlaylistEntity> savedPlaylist = database.playlistDao().loadPlaylist();
        for (PlaylistEntity entity : savedPlaylist) {
          PlaylistData playlist = PlaylistData.transformFromEntity(entity);
          audioList.add(new Audio(playlist.getUrl(), playlist.getName(), playlist.getDesc(), playlist.getAuthor()));
        }

        if (savedPlaylist.size() <= 0) {
          loadDefaultPlaylist();
        }

        //Store Serializable audioList to SharedPreferences
        StorageUtil storage = new StorageUtil(context.getApplicationContext());
        storage.storeAudio(audioList);
        storage.storeAudioIndex(0);

        return audioList;
      }

      @Override
      protected void onPostExecute(List<Audio> audioEntity) {
        super.onPostExecute(audioEntity);
      }

    }.execute(context);
  }
}

