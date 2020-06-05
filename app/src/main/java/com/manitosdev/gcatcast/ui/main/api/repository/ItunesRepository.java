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
import java.net.MalformedURLException;
import java.net.URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class ItunesRepository {


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

  private MutableLiveData<ApiResult<SearchResult>> searchResultMutableLiveData = new MutableLiveData<>();
  public MutableLiveData<ApiResult<SearchResult>> getSearchResultMutableLiveData() {
    return searchResultMutableLiveData;
  }

  private MutableLiveData<ApiResult<RssFeed>> _rssFeedMutableLiveData = new MutableLiveData<>();
  public MutableLiveData<ApiResult<RssFeed>> getRssFeedMutableLiveData() {
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

              Log.i("GIL_", "Error" + response.code());
            } else {
              SearchResult searchResult = response.body();

              ApiResult<SearchResult> result = new ApiResult<>(searchResult, null, null);
              searchResultMutableLiveData.postValue(result);
              Log.i("GIL_", "Result size" + searchResult.getResultCount());
            }
          }

          @Override
          public void onFailure(Call<SearchResult> call, Throwable t) {
            ApiResult<SearchResult> result = new ApiResult<>(null, null, t);
            searchResultMutableLiveData.postValue(result);
            Log.i("GIL_", "Error" + t.getMessage());
          }
        });
      }
    });
  }

  public void loadFeedFromRss(final String feedUrl) {
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
        String baseUrl = url.getProtocol() + "://" + url.getHost() + "/";


        RssService service = httpClient.provideRssService(baseUrl);

        Call<RssFeed> feedCall = service.getRssFeed(url.getPath().replace("/", ""));

        feedCall.enqueue(new Callback<RssFeed>() {
          @Override
          public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
            if (!response.isSuccessful()) {
              ApiResult<RssFeed> result = new ApiResult<>(null, "Error: " + response.code(), null);
              _rssFeedMutableLiveData.postValue(result);
              Log.i("GIL_RSS", "Error" + response.code());
            } else {
              RssFeed rssBody = response.body();
              ApiResult<RssFeed> result = new ApiResult<>(rssBody, null, null);
              _rssFeedMutableLiveData.postValue(result);
              Log.i("GIL_RSS", "Result size" + rssBody.getChannel().getmTitle());
            }
          }

          @Override
          public void onFailure(Call<RssFeed> call, Throwable t) {
            ApiResult<RssFeed> result = new ApiResult<>(null, null, t);
            _rssFeedMutableLiveData.postValue(result);
            Log.i("GIL_RSS", "Error" + t.getMessage());
          }
        });
      }
    });
  }
}
