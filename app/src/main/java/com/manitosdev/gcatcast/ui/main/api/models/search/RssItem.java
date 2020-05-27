package com.manitosdev.gcatcast.ui.main.api.models.search;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by gilbertohdz on 27/05/20.
 */
@Root(name="item", strict = false)
public class RssItem {

  @Path("title")
  @Text(required=false)
  private String title;

  @Element(name="description", required = false)
  private String description;

  @Element(name="pubDate", required = false)
  private String pubDate;

  @Element(name="author", required = false)
  private String author;

  @Element(name="summary", required = false)
  private String summary;

  @Element(name="duration", required = false)
  private Integer duration;

  @Element(name="season", required = false)
  private Integer season;

  @Element(name="episode", required = false)
  private Integer episode;

  @Element(name = "enclosure", required = false)
  private RssItemEnclosure enclosure;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPubDate() {
    return pubDate;
  }

  public void setPubDate(String pubDate) {
    this.pubDate = pubDate;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public Integer getSeason() {
    return season;
  }

  public void setSeason(Integer season) {
    this.season = season;
  }

  public Integer getEpisode() {
    return episode;
  }

  public void setEpisode(Integer episode) {
    this.episode = episode;
  }

  public RssItemEnclosure getEnclosure() {
    return enclosure;
  }

  public void setEnclosure(RssItemEnclosure enclosure) {
    this.enclosure = enclosure;
  }
}
