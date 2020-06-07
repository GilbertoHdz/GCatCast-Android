package com.manitosdev.gcatcast.ui.main.features.playlist;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.api.models.ApiResult;
import com.manitosdev.gcatcast.ui.main.api.models.search.RssFeed;
import com.manitosdev.gcatcast.ui.main.api.network.InternetCheck;
import com.manitosdev.gcatcast.ui.main.features.main.MainViewModel;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlayerActivity extends AppCompatActivity {

  private static final String TAG = PlayerActivity.class.getSimpleName();

  public static final String ARG_RSS_FEED_URL = "playerActivity.feed.rss.url.value";
  private static final String KEY_MEDIA_URL = "playerActivity.media.url.value";

  private MainViewModel mMainViewModel;

  private PlayerView playerView;
  private SimpleExoPlayer exoPlayer;
  private TextView _error_message;
  private ProgressBar _loader;
  private Button _retry;
  private RecyclerView _playlistItemsRecycler;
  private CardView _shortActionContainer;
  private CardView _playerActionContainer;

  @Nullable
  private String localMediaUrl;
  private String rssFeedUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }

    playerView = (PlayerView) findViewById(R.id.playerViewWidgetContainer);
    _error_message = (TextView) findViewById(R.id.player_view_error_message_text);
    _loader = (ProgressBar) findViewById(R.id.playerViewLoader);
    _retry = (Button) findViewById(R.id.playerViewRetryAction);
    _playlistItemsRecycler = (RecyclerView) findViewById(R.id.payerViewPlayerlistItemsRecycler);
    _shortActionContainer = (CardView) findViewById(R.id.playerViewShortActionContainer);
    _playerActionContainer = (CardView) findViewById(R.id.payerViewPlayerlistActionContainer);

    mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    loadRssFeed();

    if (null != savedInstanceState && savedInstanceState.containsKey(KEY_MEDIA_URL)) {
      localMediaUrl = (String) savedInstanceState.getString(KEY_MEDIA_URL);
    }

    if (savedInstanceState == null) {
      Intent extras = getIntent();
      if (extras.hasExtra(ARG_RSS_FEED_URL)) {
        rssFeedUrl = (String) extras.getStringExtra(ARG_RSS_FEED_URL);
      } else {
        throw new IllegalArgumentException("the params shouldn't be null");
      }
    }

    checkNetworkConnection();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(KEY_MEDIA_URL, "");
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer(localMediaUrl);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
      initializePlayer(localMediaUrl);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @Override
  protected void onDestroy() {
    releasePlayer();
    super.onDestroy();
  }

  private void initializePlayer(String mediaUrl) {
    if (null == mediaUrl) return;
    exoPlayer = new SimpleExoPlayer.Builder(PlayerActivity.this).build();
    playerView.setPlayer(exoPlayer);
    exoPlayer.seekTo(0);

    MediaSource mediaSource = buildMediaSource(Uri.parse(mediaUrl));

    // Prepare the player with the source.
    exoPlayer.prepare(mediaSource);
    exoPlayer.setPlayWhenReady(true);
  }

  private MediaSource buildMediaSource(Uri uri) {
    // Produces DataSource instances through which media data is loaded.
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
        PlayerActivity.this,
        Util.getUserAgent(PlayerActivity.this, "com.manitosdev.gcatcast")
    );

    // This is the MediaSource representing the media to be played.
    MediaSource videoSource = new ProgressiveMediaSource
        .Factory(dataSourceFactory)
        .createMediaSource(uri);

    // Prepare the player with the source.
    return videoSource;
  }

  private void releasePlayer() {
    if (exoPlayer != null) {
      exoPlayer.release();
      exoPlayer = null;
    }
  }

  private void checkNetworkConnection() {
    initialState();
    new InternetCheck(new InternetCheck.Consumer() {
      @Override
      public void accept(Boolean internet) {
        if (internet) {
          assert rssFeedUrl != null;
          mMainViewModel.loadRssFeedsByPodcast(rssFeedUrl);
        } else {
          showErrorMessage(R.string.error_network_message);
        }
      }
    });
  }

  private void initialState() {
    _loader.setVisibility(View.VISIBLE);
    renderUiState(false);

    _error_message.setVisibility(View.GONE);
    _retry.setVisibility(View.GONE);
    _retry.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkNetworkConnection();
      }
    });
  }

  private void showErrorMessage(int resMsgId) {
    String message = this.getString(resMsgId);
    _error_message.setText(message);
    _error_message.setVisibility(View.VISIBLE);
    _retry.setVisibility(View.VISIBLE);
    _loader.setVisibility(View.GONE);
    renderUiState(false);
  }

  private void renderUiState(boolean isVisible) {
    Log.i(TAG, "initial ui state: " + isVisible);
    int renderUi = isVisible ? View.VISIBLE : View.INVISIBLE;
    _playlistItemsRecycler.setVisibility(renderUi);
    _shortActionContainer.setVisibility(renderUi);
    _playerActionContainer.setVisibility(renderUi);
    playerView.setVisibility(renderUi);
  }

  private void loadRssFeed() {
    mMainViewModel.getRssFeedMutableLiveData() .observe(this, new Observer<ApiResult<RssFeed>>() {
      @Override
      public void onChanged(ApiResult<RssFeed> result) {

        if (null != result.getError()) {
          result.getError().printStackTrace();
        } else if (null != result.getFailureMessage()) {
          Log.i(TAG, result.getFailureMessage());
        } else {
          assert result.getResult() != null;
          Log.i(TAG, "Success result!");
          _loader.setVisibility(View.INVISIBLE);
          renderUiState(true);
        }
      }
    });
  }
}
