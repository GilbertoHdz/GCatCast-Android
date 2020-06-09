package com.manitosdev.gcatcast.ui.main.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by gilbertohdz on 09/06/20.
 */
@Entity(tableName = "playlist")
public class PlaylistEntity {

  @PrimaryKey
  @NonNull
  private String id; // Url as a ID
  private String author;
  private String name;
  private String desc;
  private String url;
  private String type;
  private String thumbnail;

  public PlaylistEntity(String id, String author, String name, String desc, String url, String type, String thumbnail) {
    this.id = id;
    this.author = author;
    this.name = name;
    this.desc = desc;
    this.url = url;
    this.type = type;
    this.thumbnail = thumbnail;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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
}
