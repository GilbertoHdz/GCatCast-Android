package com.manitosdev.gcatcast.ui.main.features.common.helpers;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one observer is going to be notified of changes.
 *
 * Created by gilbertohdz on 07/06/20.
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {

  private static final String TAG = SingleLiveEvent.class.getSimpleName();

  private AtomicBoolean pending = new AtomicBoolean(false);

  @Override
  public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    if (hasActiveObservers()) {
      Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
    }

    super.observe(owner, new Observer<T>() {
      @Override
      public void onChanged(T t) {
        if (pending.compareAndSet(true, false)) {
          observer.onChanged(t);
        }
      }
    });
  }

  @Override
  public void setValue(T value) {
    pending.set(true);
    super.setValue(value);
  }

  public void call() {
    setValue(null);
  }
}
