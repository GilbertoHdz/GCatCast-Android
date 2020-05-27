package com.manitosdev.gcatcast.ui.main.api.models.search;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by gilbertohdz on 27/05/20.
 */
@Root(name = "rss", strict = false)
public class RssFeed {

  @Element(name="channel")
  private RssChannel channel;

  public RssChannel getChannel() {
    return channel;
  }

  public void setChannel(RssChannel channel) {
    this.channel = channel;
  }
}
