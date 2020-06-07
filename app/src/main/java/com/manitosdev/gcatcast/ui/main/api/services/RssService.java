package com.manitosdev.gcatcast.ui.main.api.services;

import com.manitosdev.gcatcast.ui.main.api.models.search.RssFeed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by gilbertohdz on 27/05/20.
 */
public interface RssService {

  @GET
  Call<RssFeed> getRssFeed(@Url String feed);
}
