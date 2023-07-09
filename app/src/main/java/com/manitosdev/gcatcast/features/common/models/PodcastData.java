package com.manitosdev.gcatcast.features.common.models;

import com.manitosdev.gcatcast.db.entities.PodCastEntity;

/**
 * Created by gilbertohdz on 02/06/20.
 */
public class PodcastData {

  private int trackId;
  private String name;
  private String description;
  private String urlImg;
  private String rssUrl;
  private boolean hasInfoIcon;
  private boolean hasMarkerIcon;
  private boolean isSaved = false;

  public PodcastData(int trackId, String name, String description, String urlImg, boolean hasInfoIcon, boolean hasMarkerIcon, String rssUrl) {
    this.trackId = trackId;
    this.name = name;
    this.description = description;
    this.urlImg = urlImg;
    this.hasInfoIcon = hasInfoIcon;
    this.hasMarkerIcon = hasMarkerIcon;
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

  public String getDescription() {
    return description;
  }

  public String getUrlImg() {
    return urlImg;
  }

  public String getRssUrl() {
    return rssUrl;
  }

  public boolean isHasInfoIcon() {
    return hasInfoIcon;
  }

  public boolean isHasMarkerIcon() {
    return hasMarkerIcon;
  }

  public boolean isSaved() {
    return isSaved;
  }

  public void setSaved(boolean saved) {
    isSaved = saved;
  }

  public PodCastEntity transformToPodCEntity() {
    return new PodCastEntity(this.trackId, this.name, this.urlImg, this.rssUrl);
  }
}
