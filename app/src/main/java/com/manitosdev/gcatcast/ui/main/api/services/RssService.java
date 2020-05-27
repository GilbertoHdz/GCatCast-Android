package com.manitosdev.gcatcast.ui.main.api.services;

import com.manitosdev.gcatcast.ui.main.api.models.search.RssFeed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by gilbertohdz on 27/05/20.
 */
public interface RssService {

  @GET("{feed}")
  Call<RssFeed> getRssFeed(@Path("feed") String feed);
}
