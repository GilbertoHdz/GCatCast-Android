package com.manitosdev.gcatcast.ui.main.features.subscribed;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.manitosdev.gcatcast.R;

public class SubscribedFragment extends Fragment {

  private SubscribedViewModel mViewModel;

  public static SubscribedFragment newInstance() {
    return new SubscribedFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.subscribed_fragment, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(SubscribedViewModel.class);
    // TODO: Use the ViewModel
  }

}
