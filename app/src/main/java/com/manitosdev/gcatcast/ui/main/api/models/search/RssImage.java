package com.manitosdev.gcatcast.ui.main.api.models.search;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by gilbertohdz on 27/05/20.
 */
@Root(name="itunes:image", strict = false)
public class RssImage {

  @Attribute(name = "href", required = false)
  private String href;

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }
}
