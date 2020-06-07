package com.manitosdev.gcatcast.ui.main.db;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.manitosdev.gcatcast.ui.main.db.dao.PodCastDao;
import com.manitosdev.gcatcast.ui.main.db.entities.PodCastEntity;

/**
 * Created by gilbertohdz on 06/06/20.
 */
@Database(entities = {
    PodCastEntity.class
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
}
