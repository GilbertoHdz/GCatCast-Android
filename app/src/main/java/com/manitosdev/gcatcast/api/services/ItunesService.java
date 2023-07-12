package com.manitosdev.gcatcast.api.services;

import com.manitosdev.gcatcast.api.models.search.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * term=jack+johnson&limit=25&country=MX&entity=podcast.
 *
 * Created by gilbertohdz on 26/05/20.
 */
public interface ItunesService {


  /**
   *
   * @param term like name of author, album, podcast
   * @param entity like movie, movieArtist, musicVideo tvEpisode or podcast
   * @param limit default 50
   * @return
   */
  @GET("search?sort=recent")
  Call<SearchResult> getItunesSearch(
      @Query("term") String term,
      @Query("entity") String entity,
      @Query("limit") Integer limit
  );
}
