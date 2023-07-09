package com.manitosdev.gcatcast.features.common.models;

import com.manitosdev.gcatcast.db.entities.PlaylistEntity;

/**
 * Created by gilbertohdz on 09/06/20.
 */
public class PlaylistData {

  private String author;
  private String name;
  private String desc;
  private String url;
  private String type;
  private String thumbnail;

  private boolean isInfo = true;
  private boolean isMarker = true;
  private boolean isSaved = false;

  public PlaylistData(String author, String name, String desc, String url, String type, String thumbnail) {
    this.author = author;
    this.name = name;
    this.desc = desc;
    this.url = url;
    this.type = type;
    this.thumbnail = thumbnail;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public boolean isInfo() {
    return isInfo;
  }

  public void setInfo(boolean info) {
    isInfo = info;
  }

  public boolean isMarker() {
    return isMarker;
  }

  public void setMarker(boolean marker) {
    isMarker = marker;
  }

  public boolean isSaved() {
    return isSaved;
  }

  public void setSaved(boolean saved) {
    isSaved = saved;
  }

  public PlaylistEntity transformToEntity() {
    return new PlaylistEntity(
        this.getUrl(),
        this.author,
        this.name,
        this.desc,
        this.getUrl(),
        this.getType(),
        this.getThumbnail());
  }

  public static PlaylistData transformFromEntity(PlaylistEntity entity) {
    return new PlaylistData(entity.getAuthor(), entity.getName(), entity.getDesc(), entity.getUrl(), entity.getType(), entity.getThumbnail());
  }
}
