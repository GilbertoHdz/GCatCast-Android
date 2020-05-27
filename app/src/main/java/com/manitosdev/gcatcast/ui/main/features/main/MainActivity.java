package com.manitosdev.gcatcast.ui.main.features.main;

import android.os.Bundle;

import android.util.Log;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.api.models.search.RssFeed;
import com.manitosdev.gcatcast.ui.main.api.models.search.SearchResult;
import com.manitosdev.gcatcast.ui.main.api.network.CCatHttpClient;
import com.manitosdev.gcatcast.ui.main.api.services.ItunesService;
import com.manitosdev.gcatcast.ui.main.api.services.RssService;
import java.net.URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainSectionPagerAdapter mainSectionPagerAdapter = new MainSectionPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(mainSectionPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        testApiServices();
    }

    private void testApiServices() {
        CCatHttpClient httpClient = new CCatHttpClient();

        ItunesService service = httpClient.provideItunesService();

        Call<SearchResult> resultCall = service.getItunesSearch(
            "dimesybilletes",
            "podcast",
            25
        );

        resultCall.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (!response.isSuccessful()) {
                    Log.i("GIL_", "Error" + response.code());
                } else {
                    SearchResult result = response.body();
                    Log.i("GIL_", "Result size" + result.getResultCount());

                    try {
                        callRssFeed(result.getResults().get(0).getFeedUrl());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.i("GIL_", "Error" + t.getMessage());
            }
        });
    }

    private void callRssFeed(String feedUrl) throws Exception{
        CCatHttpClient httpClient = new CCatHttpClient();

        URL url = new URL(feedUrl);

        String baseUrl = url.getProtocol() + "://" + url.getHost() + "/";

        RssService service = httpClient.provideRssService(baseUrl);

        Call<RssFeed> feedCall = service.getRssFeed(url.getPath().replace("/", ""));

        feedCall.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if (!response.isSuccessful()) {
                    Log.i("GIL_RSS", "Error" + response.code());
                } else {
                    RssFeed result = response.body();
                    Log.i("GIL_RSS", "Result size" + result.getChannel().getmTitle());
                }
            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {
                Log.i("GIL_RSS", "Error" + t.getMessage());
            }
        });
    }
}