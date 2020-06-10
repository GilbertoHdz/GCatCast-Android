package com.manitosdev.gcatcast.ui.main.features.widget;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.db.AppDatabase;
import com.manitosdev.gcatcast.ui.main.db.entities.PlaylistEntity;
import com.manitosdev.gcatcast.ui.main.features.common.models.PlaylistData;
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
        for (PlaylistEntity entity : database.playlistDao().loadPlaylist()) {
          playlistItems.add(PlaylistData.transformFromEntity(entity));
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
}
