package com.manitosdev.gcatcast.ui.main.features.popular;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.manitosdev.gcatcast.R;

public class PopularFragment extends Fragment {

  private PopularViewModel mViewModel;

  public static PopularFragment newInstance() {
    return new PopularFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.popular_fragment, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(PopularViewModel.class);
    // TODO: Use the ViewModel
  }

}
