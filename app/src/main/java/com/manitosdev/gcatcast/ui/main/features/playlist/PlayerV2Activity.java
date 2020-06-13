package com.manitosdev.gcatcast.ui.main.features.playlist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.api.models.ApiResult;
import com.manitosdev.gcatcast.ui.main.api.models.search.RssChannel;
import com.manitosdev.gcatcast.ui.main.api.models.search.RssFeed;
import com.manitosdev.gcatcast.ui.main.api.models.search.RssItem;
import com.manitosdev.gcatcast.ui.main.api.network.AppExecutors;
import com.manitosdev.gcatcast.ui.main.api.network.InternetCheck;
import com.manitosdev.gcatcast.ui.main.db.AppDatabase;
import com.manitosdev.gcatcast.ui.main.features.common.models.PlaylistData;
import com.manitosdev.gcatcast.ui.main.features.main.MainViewModel;
import com.manitosdev.gcatcast.ui.main.features.services.MediaPlayerService;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlayerV2Activity extends AppCompatActivity {

  private static final String TAG = PlayerV2Activity.class.getSimpleName();

  public static final String ARG_RSS_FEED_URL = "playerV2Activity.feed.rss.url.value";
  public static final String ARG_RSS_FEED_THUMBNAIL_URL = "playerV2Activity.feed.rss.thumbnail.url.value";

  private static final String KEY_MEDIA_URL = "playerV2Activity.media.url.value";
  private static final String KEY_SERVICE_STATE = "playerV2Activity.service.state.value";

  private MainViewModel mMainViewModel;
  private PlaylistAdapter mPlaylistAdapter;

  private AppDatabase mDb;
  private Handler handler;

  private MediaPlayerService player;
  boolean serviceBound = false;
  private int currentPlaylistIndex;

  private TextView _error_message;
  private ProgressBar _loader;
  private Button _retry;
  private RecyclerView _playlistItemsRecycler;
  private CardView _shortActionContainer;
  private CardView _playerActionContainer;

  private TextView _currentTime, _endTime;
  private ImageView _prev, _next, _playPause;
  private SeekBar _seekProgress;

  @Nullable
  private String localMediaUrl;
  private String rssFeedUrl;
  private String rssFeedThumbnailUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }

    // Initial instances
    mDb = AppDatabase.getInstance(PlayerV2Activity.this);
    mPlaylistAdapter = new PlaylistAdapter(actionCallback());

    _error_message = (TextView) findViewById(R.id.player_view_error_message_text);
    _loader = (ProgressBar) findViewById(R.id.playerViewLoader);
    _retry = (Button) findViewById(R.id.playerViewRetryAction);
    _shortActionContainer = (CardView) findViewById(R.id.playerViewShortActionContainer);
    _playerActionContainer = (CardView) findViewById(R.id.payerViewPlayerlistActionContainer);

    _currentTime = (TextView) findViewById(R.id.payerViewPlayerlistActionSeekStartValue);
    _endTime = (TextView) findViewById(R.id.payerViewPlayerlistActionSeekEndValue);
    _seekProgress = (SeekBar) findViewById(R.id.payerViewPlayerlistActionSeek);
    _prev = (ImageView) findViewById(R.id.payerViewPlayerlistActionPrev);
    _next = (ImageView) findViewById(R.id.payerViewPlayerlistActionNext);
    _playPause = (ImageView) findViewById(R.id.payerViewPlayerlistActionPlay);

    _playlistItemsRecycler = (RecyclerView) findViewById(R.id.payerViewPlayerlistItemsRecycler);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    _playlistItemsRecycler.setHasFixedSize(true);
    _playlistItemsRecycler.setLayoutManager(mLayoutManager);
    _playlistItemsRecycler.setAdapter(mPlaylistAdapter);

    mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    loadRssFeed();

    if (null != savedInstanceState && savedInstanceState.containsKey(KEY_MEDIA_URL)) {
      localMediaUrl = (String) savedInstanceState.getString(KEY_MEDIA_URL);
    }

    if (savedInstanceState == null) {
      Intent extras = getIntent();
      if (extras.hasExtra(ARG_RSS_FEED_URL) && extras.hasExtra(ARG_RSS_FEED_THUMBNAIL_URL)) {
        rssFeedUrl = (String) extras.getStringExtra(ARG_RSS_FEED_URL);
        rssFeedThumbnailUrl = (String) extras.getStringExtra(ARG_RSS_FEED_THUMBNAIL_URL);
      } else {
        throw new IllegalArgumentException("the params shouldn't be null");
      }
    }

    checkNetworkConnection();
    playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putString(KEY_MEDIA_URL, localMediaUrl);
    outState.putBoolean(KEY_SERVICE_STATE, serviceBound);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    serviceBound = savedInstanceState.getBoolean(KEY_SERVICE_STATE);
    localMediaUrl = savedInstanceState.getString(KEY_MEDIA_URL);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (serviceBound) {
      unbindService(serviceConnection);
      // service is active
      player.stopSelf();
    }
  }

  //Binding this Client to the AudioPlayer Service
  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      // We've bound to LocalService, cast the IBinder and get LocalService instance
      MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
      player = binder.getService();
      serviceBound = true;

      Toast.makeText(PlayerV2Activity.this, "Service Bound", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      serviceBound = false;
    }
  };

  private MediaSource buildMediaSource(Uri uri) {
    // Produces DataSource instances through which media data is loaded.
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
        PlayerV2Activity.this,
        Util.getUserAgent(PlayerV2Activity.this, "com.manitosdev.gcatcast")
    );

    // This is the MediaSource representing the media to be played.
    MediaSource videoSource = new ProgressiveMediaSource
        .Factory(dataSourceFactory)
        .createMediaSource(uri);

    // Prepare the player with the source.
    return videoSource;
  }

  private void playAudio(String media) {
    //Check is service is active
    if (!serviceBound) {
      Intent playerIntent = new Intent(this, MediaPlayerService.class);
      playerIntent.putExtra("media", media);
      startService(playerIntent);
      bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    } else {
      //Service is active
      //Send media with BroadcastReceiver
    }
  }



  private void initMediaControls() {
    initPlayButton();
    // initSeekBar();
    initNextPrevButton();
  }

  private void initPlayButton() {
    _playPause.requestFocus();
    _playPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });
  }

  private void initNextPrevButton() {
    _next.setOnClickListener(_view -> {
      changeItemToPlay(true, true);
    });

    _prev.setOnClickListener(_view -> {
      changeItemToPlay(false, true);
    });
  }

  private void changeItemToPlay(boolean isNext, boolean isMediaControlActions) {
    if (mPlaylistAdapter.getItemCount() == 0) return;

    if (currentPlaylistIndex + 1 > (mPlaylistAdapter.getItemCount() - 1) && isNext && isMediaControlActions) {
      return;
    }

    if (currentPlaylistIndex -1 < 0 && !isNext) {
      return;
    }

    if (isMediaControlActions) {
      currentPlaylistIndex += isNext ? 1 : -1;
    }

    _prev.setColorFilter(ContextCompat.getColor(this, R.color.colorError));
    _next.setColorFilter(ContextCompat.getColor(this, R.color.colorError));

    if (currentPlaylistIndex == (mPlaylistAdapter.getItemCount() - 1)) {
      _prev.setColorFilter(ContextCompat.getColor(this, R.color.colorError));
      _prev.setClickable(true);
      _next.setColorFilter(ContextCompat.getColor(this, R.color.colorErrorInverse));
      _next.setClickable(false);
    }

    if (currentPlaylistIndex == 0) {
      _prev.setColorFilter(ContextCompat.getColor(this, R.color.colorErrorInverse));
      _prev.setClickable(false);
      _next.setColorFilter(ContextCompat.getColor(this, R.color.colorError));
      _next.setClickable(true);
    }

    if (isMediaControlActions) {
      String nextToPlayUrl = mPlaylistAdapter.getPlaylist().get(currentPlaylistIndex).getUrl();
      callPlaylistItem(nextToPlayUrl);
    }
  }

  /**
   * Starts or stops playback. Also takes care of the Play/Pause button toggling
   * @param play True if playback should be started
   */
  private void setPlayPause(boolean play){
    if (play) {
      _playPause.setImageResource(R.drawable.exo_controls_play);
    } else {
      _playPause.setImageResource(R.drawable.exo_controls_pause);
    }
  }

  private String stringForTime(int timeMs) {
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    mFormatBuilder = new StringBuilder();
    mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    int totalSeconds =  timeMs / 1000;

    int seconds = totalSeconds % 60;
    int minutes = (totalSeconds / 60) % 60;
    int hours   = totalSeconds / 3600;

    mFormatBuilder.setLength(0);
    if (hours > 0) {
      return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
    } else {
      return mFormatter.format("%02d:%02d", minutes, seconds).toString();
    }
  }

  /*private void setProgress() {
    _seekProgress.setMax((int) exoPlayer.getDuration() / 1000);
    _currentTime.setText(stringForTime((int) exoPlayer.getCurrentPosition()));
    _endTime.setText(stringForTime((int) exoPlayer.getDuration()));

    if (handler == null) handler = new Handler();

    // Make sure you update Seekbar on UI thread
    handler.post(new Runnable() {
      @Override
      public void run() {
        if (exoPlayer != null && isPlaying) {
          _seekProgress.setMax((int) exoPlayer.getDuration() / 1000);
          int mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000;
          _seekProgress.setProgress(mCurrentPosition);
          _currentTime.setText(stringForTime((int)exoPlayer.getCurrentPosition()));
          _endTime.setText(stringForTime((int)exoPlayer.getDuration()));

          handler.postDelayed(this, 1000);
        }
      }
    });
  }

  private void initSeekBar() {
    _seekProgress.requestFocus();

    _seekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
          // We're not interested in programmatically generated changes to
          // the progress bar's position.
          return;
        }

        exoPlayer.seekTo(progress * 1000);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    _seekProgress.setMax(0);
    _seekProgress.setMax((int) exoPlayer.getDuration() / 1000);
  }*/

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
  }

  private void renderUiByChannel(RssChannel channel) {
    ArrayList<PlaylistData> items = new ArrayList<>();
    for (RssItem media : channel.getItems()) {
      if (null != media.getEnclosure() && isAudioOrVideoType(media.getEnclosure().getType())) {
        items.add(new PlaylistData(media.getAuthor(), media.getTitle(), media.getDescription(), media.getEnclosure().getUrl(), media.getEnclosure().getType(), rssFeedThumbnailUrl));
      }
    }

    // Initialize player and controls by the first element and with pause player
    if (!items.isEmpty()) {
      localMediaUrl = items.get(0).getUrl();

      currentPlaylistIndex = 0;
      _prev.setColorFilter(ContextCompat.getColor(this, R.color.colorErrorInverse));
      _prev.setClickable(false);
    }

    mPlaylistAdapter.updateData(items);
  }

  public static boolean isAudioOrVideoType(String mimeType) {
    return mimeType.contains(MimeTypes.BASE_TYPE_VIDEO) || mimeType.contains(MimeTypes.BASE_TYPE_AUDIO);
  }

  private void callPlaylistItem(String url) {
    localMediaUrl = url;
    playAudio(localMediaUrl);
  }

  private void loadRssFeed() {
    mMainViewModel.getRssFeedMutableLiveData() .observe(this, new Observer<ApiResult<RssFeed>>() {
      @Override
      public void onChanged(ApiResult<RssFeed> result) {

        if (null == result) return;
        if (null != result.getError()) {
          result.getError().printStackTrace();
        } else if (null != result.getFailureMessage()) {
          Log.i(TAG, result.getFailureMessage());
        } else {
          assert result.getResult() != null;
          _loader.setVisibility(View.INVISIBLE);
          renderUiState(true);
          renderUiByChannel(result.getResult().getChannel());
        }
      }
    });
  }

  public PlaylistAdapter.ItemActionClicked actionCallback() {
    return new PlaylistAdapter.ItemActionClicked() {
      @Override
      public void onItemClicked(PlaylistData rssItem, int position) {
        currentPlaylistIndex = position;
        callPlaylistItem(rssItem.getUrl());
        changeItemToPlay(true, false);
      }

      @Override
      public void markerClicked(PlaylistData rssItem) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
          @Override
          public void run() {
            mDb.playlistDao().deletePlaylist(rssItem.transformToEntity());
            mDb.playlistDao().insertPlaylist(rssItem.transformToEntity());
          }
        });
      }

      @Override
      public void infoClicked(PlaylistData rssItem) {
        // TODO(PENDING) here we will create a new popup screen with podcast or track description
      }
    };
  }

  private ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
      Log.i(TAG,"onPlayerStateChanged: playWhenReady: " + playWhenReady + " playbackState: " + playbackState);
      switch (playbackState){
        case ExoPlayer.STATE_ENDED:
          Log.i(TAG,"Playback ended!");
          //Stop playback and return to start position
          setPlayPause(false);
          //exoPlayer.seekTo(0);
          break;
        case ExoPlayer.STATE_READY:
          //Log.i(TAG,"ExoPlayer ready! pos: " + exoPlayer.getCurrentPosition() + " max: " + stringForTime((int) exoPlayer.getDuration()));
          //setProgress();
          break;
        case ExoPlayer.STATE_BUFFERING:
          Log.i(TAG,"Playback buffering!");
          break;
        case ExoPlayer.STATE_IDLE:
          Log.i(TAG,"ExoPlayer idle!");
          break;
      }
    }
  };
}
