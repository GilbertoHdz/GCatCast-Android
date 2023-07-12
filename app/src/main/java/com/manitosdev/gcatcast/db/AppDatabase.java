package com.manitosdev.gcatcast.db;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.manitosdev.gcatcast.db.entities.PlaylistEntity;
import com.manitosdev.gcatcast.db.entities.PodCastEntity;
import com.manitosdev.gcatcast.db.dao.PlaylistDao;
import com.manitosdev.gcatcast.db.dao.PodCastDao;

/**
 * Created by gilbertohdz on 06/06/20.
 */
@Database(entities = {
    PodCastEntity.class,
    PlaylistEntity.class
}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

  private static final String LOG_TAG = AppDatabase.class.getSimpleName();
  private static final Object LOCK = new Object();
  private static final String DATABAS_NAME = "gilinho.podcas";
  private static AppDatabase sInstance;

  public static AppDatabase getInstance(Context context) {
    if (null == sInstance) {
      synchronized (LOCK) {
        Log.d(LOG_TAG, "Creating new database instance");
        sInstance = Room.databaseBuilder(
            context.getApplicationContext(),
            AppDatabase.class,
            AppDatabase.DATABAS_NAME)
            .build();
      }
    }

    Log.d(LOG_TAG, "Getting the database instance");
    return sInstance;
  }

  public abstract PodCastDao podCastDao();

  public abstract PlaylistDao playlistDao();
}
