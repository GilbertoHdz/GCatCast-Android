package com.manitosdev.gcatcast.ui.main.features.main;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.features.playlist.PlayerActivity;


public class MainActivity extends AppCompatActivity {

  private static final String TAG = com.manitosdev.gcatcast.ui.main.features.main.MainActivity.class.getSimpleName();

  private InterstitialAd mInterstitialAd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    MainSectionPagerAdapter mainSectionPagerAdapter = new MainSectionPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(mainSectionPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);

    // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
    MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

    // Config Interstitial Ads
    mInterstitialAd = new InterstitialAd(this);
    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
    mInterstitialAd.loadAd(new AdRequest.Builder().build());

    mInterstitialAd.setAdListener(interstitialListener());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PlayerActivity.PLAYER_REQUEST_CLOSE) {
      if (mInterstitialAd.isLoaded()) {
        mInterstitialAd.show();
      } else {
        Log.d(TAG, "The interstitial wasn't loaded yet.");
      }
    }
  }

  private AdListener interstitialListener() {
    return new AdListener() {
      @Override
      public void onAdClosed() {
        // Code to be executed when the interstitial ad is closed.
        // Load the next interstitial.
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Log.i(TAG, "onAdClosed");
      }

      @Override
      public void onAdFailedToLoad(int i) {
        // Code to be executed when an ad request fails.
        Log.i(TAG, "onAdFailedToLoad");
      }

      @Override
      public void onAdLeftApplication() {
        // Code to be executed when the user has left the app.
        Log.i(TAG, "onAdLeftApplication");
      }

      @Override
      public void onAdOpened() {
        // Code to be executed when the ad is displayed.
        Log.i(TAG, "onAdOpened");
      }

      @Override
      public void onAdLoaded() {
        // Code to be executed when an ad finishes loading.
        Log.i(TAG, "onAdLoaded");
      }

      @Override
      public void onAdClicked() {
        // Code to be executed when the user clicks on an ad.
        Log.i(TAG, "onAdClicked");
      }

      @Override
      public void onAdImpression() {
        Log.i(TAG, "onAdImpression");
      }
    };
  }
}

