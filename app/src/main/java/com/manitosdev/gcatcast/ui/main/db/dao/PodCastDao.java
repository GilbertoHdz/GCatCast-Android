package com.manitosdev.gcatcast.ui.main.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.manitosdev.gcatcast.ui.main.db.entities.PodCastEntity;
import io.reactivex.Flowable;
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
  public List<Integer> loadSavedPodCasts();

  @Query("SELECT * from pod_cast")
  public Flowable<PodCastEntity> loadPodcasts(int trackId);

  @Query("SELECT * from pod_cast where trackId = :trackId LIMIT 1")
  public Flowable<PodCastEntity> liadPodCastByTrackId(int trackId);
}
