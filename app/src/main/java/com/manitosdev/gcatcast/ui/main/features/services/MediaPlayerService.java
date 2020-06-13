package com.manitosdev.gcatcast.ui.main.features.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;

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

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return iBinder;
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    // Invoked when playback of a media source has completed.
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
  }

  @Override
  public void onSeekComplete(MediaPlayer mp) {
    // Invoked indicating the completion of a seek operation.
  }

  public class LocalBinder extends Binder {
    public MediaPlayerService getService() {
      return MediaPlayerService.this;
    }
  }
}
