package com.manitosdev.gcatcast.ui.main.features.discovery;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.api.models.ApiResult;
import com.manitosdev.gcatcast.ui.main.api.models.search.Result;
import com.manitosdev.gcatcast.ui.main.api.models.search.SearchResult;
import com.manitosdev.gcatcast.ui.main.api.repository.ItunesRepository;
import com.manitosdev.gcatcast.ui.main.features.common.adapter.PodcastAdapter;
import com.manitosdev.gcatcast.ui.main.features.common.models.CategoryDivider;
import com.manitosdev.gcatcast.ui.main.features.common.models.PodcastData;
import com.manitosdev.gcatcast.ui.main.features.common.models.SmPodcast;
import com.manitosdev.gcatcast.ui.main.features.main.MainViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoveryFragment extends Fragment {

  private MainViewModel mMainViewModel;

  private RecyclerView mCategoryRecycler;
  private PodcastAdapter mDiscoveryAdapter;

  public static DiscoveryFragment newInstance() {
    return new DiscoveryFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.discovery_fragment, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mMainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    mMainViewModel.getSearchResultMutableLiveData().observe(this.getViewLifecycleOwner(), podcastOberver());
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mDiscoveryAdapter = new PodcastAdapter(requireActivity());
    initializeRecyler(view);

    ItunesRepository.getInstance().loadPodcasts("category");
  }

  private void initializeRecyler(View view) {

    final int GRID_SPAN = 3;
    final int SINGLE_ROW = 1;

    GridLayoutManager gridLayoutManager;
    gridLayoutManager = new GridLayoutManager(
        requireContext(),
        GRID_SPAN
    );
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        int viewSpan = (mDiscoveryAdapter.getItemViewType(position) == PodcastAdapter.DIVIDER_ITEM) ? GRID_SPAN : SINGLE_ROW;
        return viewSpan;
      }
    });

    mCategoryRecycler = (RecyclerView) view.findViewById(R.id.discoverySectionRecycler);
    mCategoryRecycler.setHasFixedSize(true);
    mCategoryRecycler.setLayoutManager(gridLayoutManager);
    mCategoryRecycler.setAdapter(mDiscoveryAdapter);
  }

  private Observer<ApiResult<SearchResult>> podcastOberver() {
    return new Observer<ApiResult<SearchResult>>() {
      @Override
      public void onChanged(ApiResult<SearchResult> result) {
        ArrayList<PodcastData> categories = new ArrayList<>();

        Map<String, List<Result>> map = new HashMap<>();
        for (Result item : result.getResult().getResults()) {
          String key = item.getPrimaryGenreName();
          if (map.get(key) == null) {
            map.put(key, new ArrayList<Result>());
          }
          map.get(key).add(item);
        }

        for (Map.Entry<String, List<Result>> groups : map.entrySet()) {
          categories.add(new CategoryDivider(0, groups.getKey(), null, null, false, false, null));
          for (Result item : groups.getValue()) {
            categories.add(new SmPodcast(item.getCollectionId(), item.getArtistName(), null, item.getArtworkUrl100(), false, false, item.getFeedUrl()));
          }
        }

        mDiscoveryAdapter.updateData(categories);
      }
    };
  }
}
