package com.manitosdev.gcatcast.ui.main.api.models.search;

import java.util.ArrayList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by gilbertohdz on 27/05/20.
 */
@Root(name="channel", strict = false)
public class RssChannel {

  @Element(name = "title", required = false)
  private String mTitle;

  @Element(name = "lastBuildDate", required = false)
  private String lastBuildDate;

  @Element(name = "link", required = false)
  @Path("rss/channel")
  private String link;

  @Element(name = "language", required = false)
  private String language;

  @Element(name = "copyright", required = false)
  private String copyright;

  @Element(name = "description", required = false)
  private String description;

  @Element(name = "itunes:image", required = false)
  private RssImage image;

  @ElementList(name="item", inline = true)
  private ArrayList<RssItem> items;

  public String getmTitle() {
    return mTitle;
  }

  public void setmTitle(String mTitle) {
    this.mTitle = mTitle;
  }

  public String getLastBuildDate() {
    return lastBuildDate;
  }

  public void setLastBuildDate(String lastBuildDate) {
    this.lastBuildDate = lastBuildDate;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public RssImage getImage() {
    return image;
  }

  public void setImage(RssImage image) {
    this.image = image;
  }

  public ArrayList<RssItem> getItems() {
    return items;
  }

  public void setItems(ArrayList<RssItem> items) {
    this.items = items;
  }
}
