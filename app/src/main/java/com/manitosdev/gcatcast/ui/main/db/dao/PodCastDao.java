package com.manitosdev.gcatcast.ui.main.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.manitosdev.gcatcast.ui.main.db.entities.PodCastEntity;
import java.util.List;

/**
 * Created by gilbertohdz on 06/06/20.
 */
@Dao
public interface PodCastDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  public void insertPodCast(PodCastEntity podCastEntity);

  @Delete
  public void deletePodCast(PodCastEntity... podCastEntity);

  @Query("SELECT trackId FROM pod_cast")
  public LiveData<List<PodCastEntity>> loadSavedPodCastIds();

  @Query("SELECT * from pod_cast")
  public LiveData<List<PodCastEntity>> loadSavedPodCasts();

  @Query("SELECT * from pod_cast where trackId = :trackId LIMIT 1")
  public LiveData<PodCastEntity> loadPodCastByTrackId(int trackId);
}
