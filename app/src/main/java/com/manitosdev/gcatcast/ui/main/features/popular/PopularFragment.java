package com.manitosdev.gcatcast.ui.main.features.popular;

import android.util.Log;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.api.models.ApiResult;
import com.manitosdev.gcatcast.ui.main.api.models.search.SearchResult;
import com.manitosdev.gcatcast.ui.main.api.repository.ItunesRepository;
import com.manitosdev.gcatcast.ui.main.features.common.adapter.PodcastAdapter;

public class PopularFragment extends Fragment {

  private PopularViewModel mViewModel;

  private RecyclerView rvTopPodcast;
  private RecyclerView rvBodyPodcast;

  private PodcastAdapter podcastAdapter = new PodcastAdapter();

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

    mViewModel.getSearchResultMutableLiveData().observe(this.getViewLifecycleOwner(), podcastOberver());
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeRecyclers(view);

    ItunesRepository.getInstance().loadPodcasts("g");
  }

  private void initializeRecyclers(@NonNull View view) {
    RecyclerView.LayoutManager layoutManager;

    layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
    rvTopPodcast = (RecyclerView) view.findViewById(R.id.popularTopSectionRecycler);
    rvTopPodcast.setHasFixedSize(true);
    rvTopPodcast.setLayoutManager(layoutManager);
    rvTopPodcast.setAdapter(podcastAdapter);

    layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
    rvBodyPodcast = (RecyclerView) view.findViewById(R.id.popularBodySectionRecycler);
    rvBodyPodcast.setHasFixedSize(true);
    rvBodyPodcast.setLayoutManager(layoutManager);
    rvBodyPodcast.setAdapter(podcastAdapter);
  }

  private Observer<ApiResult<SearchResult>> podcastOberver() {
    return new Observer<ApiResult<SearchResult>>() {
      @Override
      public void onChanged(ApiResult<SearchResult> result) {

        Log.i("LIVEDATA", "The livedata changed: "+ result.getResult().getResultCount());
      }
    };
  }
}
