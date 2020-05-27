package com.manitosdev.gcatcast.ui.main.api.network;

import com.manitosdev.gcatcast.ui.main.api.services.ItunesService;
import com.manitosdev.gcatcast.ui.main.api.services.RssService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by gilbertohdz on 25/05/20.
 */
public class CCatHttpClient {

  private String itunesUrl = "https://itunes.apple.com/";

  private HttpLoggingInterceptor provideInterceptor() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return interceptor;
  }

  private OkHttpClient provideClient() {
    return new OkHttpClient.Builder()
        .addInterceptor(provideInterceptor())
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();
  }

  public Retrofit provideRetrofit(String url) {
    return new Retrofit.Builder()
        .baseUrl(url)
        .client(provideClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public Retrofit provideRetrofitXml(String url) {
    return new Retrofit.Builder()
        .baseUrl(url)
        .client(provideClient())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build();
  }

  public ItunesService provideItunesService() {
    return provideRetrofit(itunesUrl).create(ItunesService.class);
  }

  public RssService provideRssService(String baseUrl) {
    return provideRetrofitXml(baseUrl).create(RssService.class);
  }
}
