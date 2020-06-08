package com.manitosdev.gcatcast.ui.main.api.models.search;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by gilbertohdz on 27/05/20.
 */
@Root(name="enclosure", strict = false)
public class RssItemEnclosure {

  @Attribute(name = "url", required = false)
  private String url;

  @Attribute(name = "length", required = false)
  private String length;

  @Attribute(name = "type", required = false)
  private String type;

  private String thumbnail;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
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
