package com.manitosdev.gcatcast.features.common.models;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class LgPodcast extends PodcastData {
  public LgPodcast(int trackId, String name, String description, String urlImg, boolean hasInfoIcon, boolean hasMarkerIcon, String rssUrl) {
    super(trackId, name, description, urlImg, hasInfoIcon, hasMarkerIcon, rssUrl);
  }
}
