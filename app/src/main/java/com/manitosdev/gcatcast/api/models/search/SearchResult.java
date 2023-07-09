package com.manitosdev.gcatcast.api.models.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by gilbertohdz on 26/05/20.
 */
public class SearchResult {

  @SerializedName("resultCount")
  @Expose
  private Integer resultCount;
  @SerializedName("results")
  @Expose
  private List<Result> results = null;

  public Integer getResultCount() {
    return resultCount;
  }

  public void setResultCount(Integer resultCount) {
    this.resultCount = resultCount;
  }

  public List<Result> getResults() {
    return results;
  }

  public void setResults(List<Result> results) {
    this.results = results;
  }
}
