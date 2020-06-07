package com.manitosdev.gcatcast.ui.main.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by gilbertohdz on 06/06/20.
 */
@Entity(tableName = "pod_cast")
public class PodCastEntity {

  @PrimaryKey private int trackId;
  private String name;
  private String urlImg;
  private String rssUrl;

  public PodCastEntity(int trackId, String name, String urlImg, String rssUrl) {
    this.trackId = trackId;
    this.name = name;
    this.urlImg = urlImg;
    this.rssUrl = rssUrl;
  }

  public int getTrackId() {
    return trackId;
  }

  public void setTrackId(int trackId) {
    this.trackId = trackId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrlImg() {
    return urlImg;
  }

  public void setUrlImg(String urlImg) {
    this.urlImg = urlImg;
  }

  public String getRssUrl() {
    return rssUrl;
  }

  public void setRssUrl(String rssUrl) {
    this.rssUrl = rssUrl;
  }
}
