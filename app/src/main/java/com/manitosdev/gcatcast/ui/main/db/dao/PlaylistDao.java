package com.manitosdev.gcatcast.ui.main.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.manitosdev.gcatcast.ui.main.db.entities.PlaylistEntity;
import java.util.List;

/**
 * Created by gilbertohdz on 09/06/20.
 */
@Dao
public interface PlaylistDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  public void insertPlaylist(PlaylistEntity playlistEntity);

  @Delete
  public void deletePlaylist(PlaylistEntity... playlistEntity);

  @Query("SELECT id FROM playlist")
  public LiveData<List<PlaylistEntity>> loadSavedPlaylistIds();

  @Query("SELECT * from playlist")
  public LiveData<List<PlaylistEntity>> loadSavedPlaylists();

  @Query("SELECT * from playlist where id = :url LIMIT 1")
  public LiveData<PlaylistEntity> loadPlaylistByUrl(String url);

  @Query("SELECT * from playlist")
  public List<PlaylistEntity> loadPlaylist();
}
