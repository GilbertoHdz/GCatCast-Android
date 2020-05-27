package com.manitosdev.gcatcast.ui.main.api.services;

import com.manitosdev.gcatcast.ui.main.api.models.search.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * term=jack+johnson&limit=25&country=ca&entity=podcast.
 *
 * Created by gilbertohdz on 26/05/20.
 */
public interface ItunesService {


  /**
   *
   * @param term like name of author, album, podcast
   * @param entity like movie, movieArtist, musicVideo tvEpisode or podcast
   * @param country us, mx, es, etc.
   * @param limit default 50
   * @return
   */
  @GET("search")
  Call<SearchResult> getItunesSearch(
      @Query("term") String term,
      @Query("entity") String entity,
      @Query("country") String country,
      @Query("limit") Integer limit
  );
}
