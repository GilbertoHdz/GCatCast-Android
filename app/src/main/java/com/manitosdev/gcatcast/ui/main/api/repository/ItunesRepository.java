package com.manitosdev.gcatcast.ui.main.api.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.manitosdev.gcatcast.ui.main.api.models.ApiResult;
import com.manitosdev.gcatcast.ui.main.api.models.search.RssFeed;
import com.manitosdev.gcatcast.ui.main.api.models.search.SearchResult;
import com.manitosdev.gcatcast.ui.main.api.network.AppExecutors;
import com.manitosdev.gcatcast.ui.main.api.network.CCatHttpClient;
import com.manitosdev.gcatcast.ui.main.api.services.ItunesService;
import com.manitosdev.gcatcast.ui.main.api.services.RssService;
import com.manitosdev.gcatcast.ui.main.features.common.helpers.SingleLiveEvent;
import java.net.MalformedURLException;
import java.net.URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class ItunesRepository {

  private static final String TAG = ItunesRepository.class.getSimpleName();
  private static final Object LOCK = new Object();
  private static ItunesRepository sInstance;

  private CCatHttpClient httpClient = new CCatHttpClient();

  public static ItunesRepository getInstance() {
    if (sInstance == null) {
      synchronized (LOCK) {
        sInstance = new ItunesRepository();
      }
    }

    return sInstance;
  }

  private SingleLiveEvent<ApiResult<SearchResult>> searchResultMutableLiveData = new SingleLiveEvent<>();
  public SingleLiveEvent<ApiResult<SearchResult>> getSearchResultMutableLiveData() {
    return searchResultMutableLiveData;
  }

  private SingleLiveEvent<ApiResult<RssFeed>> _rssFeedMutableLiveData = new SingleLiveEvent<>();
  public SingleLiveEvent<ApiResult<RssFeed>> getRssFeedMutableLiveData() {
    return _rssFeedMutableLiveData;
  }

  public void loadPodcasts(final String term) {
    AppExecutors.getInstance().getNetworkIO().execute(new Runnable() {
      @Override
      public void run() {
        ItunesService service = httpClient.provideItunesService();

        Call<SearchResult> resultCall = service.getItunesSearch(
            term,
            "podcast",
            25
        );

        resultCall.enqueue(new Callback<SearchResult>() {
          @Override
          public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
            if (!response.isSuccessful()) {
              ApiResult<SearchResult> result = new ApiResult<>(null, "Error: " + response.code(), null);
              searchResultMutableLiveData.postValue(result);

              Log.i(TAG, "Error" + response.code());
            } else {
              SearchResult searchResult = response.body();

              ApiResult<SearchResult> result = new ApiResult<>(searchResult, null, null);
              searchResultMutableLiveData.postValue(result);
              Log.i(TAG, "Result size" + searchResult.getResultCount());
            }
          }

          @Override
          public void onFailure(Call<SearchResult> call, Throwable t) {
            ApiResult<SearchResult> result = new ApiResult<>(null, null, t);
            searchResultMutableLiveData.postValue(result);
            Log.i(TAG, "Error" + t.getMessage());
          }
        });
      }
    });
  }

  public void loadFeedFromRss(final String feedUrl) {
    Log.i(TAG, "Feed URL: " + feedUrl);

    AppExecutors.getInstance().getNetworkIO().execute(new Runnable() {
      @Override
      public void run() {

        URL url = null;
        try {
          url = new URL(feedUrl);
        } catch (MalformedURLException e) {
          ApiResult<RssFeed> result = new ApiResult<>(null, "Error: malformed url for rss feed", null);
          _rssFeedMutableLiveData.postValue(result);
          e.printStackTrace();
        }

        String protocol = url.getProtocol();
        String host = url.getHost();
        int port = url.getPort();

        String baseUrl;
        // if the port is not explicitly specified in the input, it will be -1.
        if (port == -1) {
          baseUrl = String.format("%s://%s", protocol, host);
        } else {
          baseUrl = String.format("%s://%s:%d", protocol, host, port);
        }

        String lastPath = url.getPath().replace(baseUrl, "");

        Log.i(TAG, "Feed BasePath: " + baseUrl);
        Log.i(TAG, "Feed LastPath: " + lastPath);

        RssService service = httpClient.provideRssService(baseUrl);
        Call<RssFeed> feedCall = service.getRssFeed(lastPath);

        feedCall.enqueue(new Callback<RssFeed>() {
          @Override
          public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
            if (!response.isSuccessful()) {
              ApiResult<RssFeed> result = new ApiResult<>(null, "Error: " + response.code(), null);
              _rssFeedMutableLiveData.postValue(result);
              Log.i(TAG, "Error: " + response.code());
            } else {
              RssFeed rssBody = response.body();
              ApiResult<RssFeed> result = new ApiResult<>(rssBody, null, null);
              _rssFeedMutableLiveData.postValue(result);
              Log.i(TAG, "Result success by title: " + rssBody.getChannel().getTitle());
            }
          }

          @Override
          public void onFailure(Call<RssFeed> call, Throwable t) {
            ApiResult<RssFeed> result = new ApiResult<>(null, null, t);
            _rssFeedMutableLiveData.postValue(result);
            Log.i(TAG, "Error: " + t.getMessage());
          }
        });
      }
    });
  }
}
