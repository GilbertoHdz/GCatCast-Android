package com.manitosdev.gcatcast.features.widget;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.db.AppDatabase;
import com.manitosdev.gcatcast.db.entities.PlaylistEntity;
import com.manitosdev.gcatcast.features.common.models.PlaylistData;
import com.manitosdev.gcatcast.features.playlist.Audio;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilbertohdz on 08/06/20.
 */
public class CCatWidgetService extends RemoteViewsService {
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new CCatWidgetViewsFactory(this.getApplicationContext());
  }
}

class CCatWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

  private List<PlaylistData> playlistItems = new ArrayList<>();
  private Context mContext;
  private AppDatabase mDb;
  private ArrayList<Audio> audioList = new ArrayList<>();

  String[] list = {"Android", "Java", "Kotlin", "C++", "C#", "Python", "Ruby"};


  public CCatWidgetViewsFactory(Context context) {
    this.mContext = context;
    mDb = AppDatabase.getInstance(context);
  }

  @Override
  public void onCreate() {
    Log.i("EXO", "onCreate");
  }

  @Override
  public void onDataSetChanged() {
    new AsyncTask<Context, Void, List<PlaylistData>>() {

      @Override
      protected List<PlaylistData> doInBackground(Context... contexts) {
        AppDatabase database = AppDatabase.getInstance(mContext);
        List<PlaylistEntity> savedPlaylist = database.playlistDao().loadPlaylist();
        for (PlaylistEntity entity : savedPlaylist) {
          PlaylistData playlist = PlaylistData.transformFromEntity(entity);
          playlistItems.add(PlaylistData.transformFromEntity(entity));
          audioList.add(new Audio(playlist.getUrl(), playlist.getName(), playlist.getDesc(), playlist.getAuthor()));
        }

        if (savedPlaylist.size() <= 0) {
          loadDefaultPlaylist();
        }

        return playlistItems;
      }

      @Override
      protected void onPostExecute(List<PlaylistData> movieEntities) {
        super.onPostExecute(movieEntities);
      }

    }.execute(mContext);
  }

  @Override
  public void onDestroy() {
    Log.i("EXO", "onDestroy");
  }

  @Override
  public int getCount() {
    return list.length;
  }

  @Override
  public RemoteViews getViewAt(int position) {
    RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.c_cat_widget_item);

    view.setTextViewText(R.id.widget_item_name, list[position]);

    return view;
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
    return 0;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  private void loadDefaultPlaylist() {
    String defaultUrl = "http://media-podcast.open.ac.uk/feeds/greek-heroes/desktop-all/greekheroesaudio01.mp3";
    String defaultName = "Achilles";
    String defaultAuthor = "The Open University";
    String defaultDesc = "The lowdown on what popular culture chooses to keep in its portrayals of the Greek hero Achilles … and what gets left out.";
    audioList.add(new Audio(defaultUrl, defaultName, defaultAuthor, defaultDesc));
  }
}
