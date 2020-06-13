package com.manitosdev.gcatcast.ui.main.features.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import java.io.IOException;

/**
 * Created by gilbertohdz on 13/06/20.
 */
public class MediaPlayerService extends Service
    implements
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnInfoListener,
    MediaPlayer.OnBufferingUpdateListener,
    AudioManager.OnAudioFocusChangeListener {

  // Binder given to clients
  private final IBinder iBinder = new LocalBinder();

  /**
   * 2.- Config media player
   */
  private MediaPlayer mediaPlayer;
  // path to the audio file
  private String mediaFile;
  // Used to pause/resume MediaPlayer
  private int resumePosition;

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return iBinder;
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    // Invoked when playback of a media source has completed.
    stopMedia();
    // stop the service
    stopSelf();
  }

  @Override
  public void onAudioFocusChange(int focusChange) {
    // Invoked when the audio focus of the system is updated.
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    // Invoked indicating buffering status of
    // a media resource being streamed over the network.
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    // Invoked when there has been an error during an asynchronous operation.
    switch (what) {
      case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
        Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK: " + extra);
        break;
      case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
        Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED: " + extra);
        break;
      case MediaPlayer.MEDIA_ERROR_UNKNOWN:
        Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN: " + extra);
        break;
    }
    return false;
  }

  @Override
  public boolean onInfo(MediaPlayer mp, int what, int extra) {
    // Invoked to communicate some info.
    return false;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    // Invoked when the media source is ready for playback.
    playMedia();
  }

  @Override
  public void onSeekComplete(MediaPlayer mp) {
    // Invoked indicating the completion of a seek operation.
  }

  private void initMediaPlayer() {
    mediaPlayer = new MediaPlayer();
    // Set up MediaPlayer event listeners
    mediaPlayer.setOnCompletionListener(this);
    mediaPlayer.setOnErrorListener(this);
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnBufferingUpdateListener(this);
    mediaPlayer.setOnSeekCompleteListener(this);
    mediaPlayer.setOnInfoListener(this);
    // Reset so that the MediaPlayer is not pointing to another data source
    mediaPlayer.reset();

    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    try {
      // Set the data source to the mediaFile location
      mediaPlayer.setDataSource(mediaFile);
    } catch (IOException e) {
      e.printStackTrace();
      stopSelf();
    }
    mediaPlayer.prepareAsync();
  }

  private void playMedia() {
    if (!mediaPlayer.isPlaying()) {
      mediaPlayer.start();
    }
  }

  private void stopMedia() {
    if (mediaPlayer == null) return;
    if (mediaPlayer.isPlaying()) {
      mediaPlayer.stop();
    }
  }

  private void pauseMedia() {
    if (mediaPlayer.isPlaying()) {
      mediaPlayer.pause();
      resumePosition = mediaPlayer.getCurrentPosition();
    }
  }

  private void resumeMedia() {
    if (!mediaPlayer.isPlaying()) {
      mediaPlayer.seekTo(resumePosition);
      mediaPlayer.start();
    }
  }

  public class LocalBinder extends Binder {
    public MediaPlayerService getService() {
      return MediaPlayerService.this;
    }
  }
}
