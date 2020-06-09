package com.manitosdev.gcatcast.ui.main.features.subscribed;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.db.AppDatabase;
import com.manitosdev.gcatcast.ui.main.db.entities.PlaylistEntity;
import com.manitosdev.gcatcast.ui.main.db.entities.PodCastEntity;
import com.manitosdev.gcatcast.ui.main.features.common.adapter.PodcastAdapter;
import com.manitosdev.gcatcast.ui.main.features.common.models.PlaylistData;
import com.manitosdev.gcatcast.ui.main.features.common.models.PodcastData;
import com.manitosdev.gcatcast.ui.main.features.common.models.SmPodcast;
import com.manitosdev.gcatcast.ui.main.features.main.MainViewModel;
import com.manitosdev.gcatcast.ui.main.features.playlist.PlaylistAdapter;
import java.util.ArrayList;
import java.util.List;

public class SubscribedFragment extends Fragment {

  private MainViewModel mMainViewModel;

  private RecyclerView mSubscribedRecycler;
  private RecyclerView mSubscribedPlaylistRecycler;
  private PodcastAdapter mSubscribedAdapter;
  private PlaylistAdapter mSubscribedPlaylistAdapter;

  private AppDatabase mDb;

  public static SubscribedFragment newInstance() {
    return new SubscribedFragment();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.subscribed_fragment, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mMainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    loadSavedPodCasts();
    loadSavedPlaylist();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mDb = AppDatabase.getInstance(requireActivity());
    mSubscribedAdapter = new PodcastAdapter(requireActivity(), mDb);
    mSubscribedPlaylistAdapter = new PlaylistAdapter(actionCallback());
    initializeRecycler(view);
  }

  private void initializeRecycler(View view) {
    final int GRID_SPAN = 3;
    GridLayoutManager gridLayoutManager;
    gridLayoutManager = new GridLayoutManager(
        requireContext(),
        GRID_SPAN
    );

    mSubscribedRecycler = (RecyclerView) view.findViewById(R.id.subscribedSectionRecycler);
    mSubscribedRecycler.setHasFixedSize(true);
    mSubscribedRecycler.setLayoutManager(gridLayoutManager);
    mSubscribedRecycler.setAdapter(mSubscribedAdapter);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
        requireContext(),
        RecyclerView.VERTICAL,
        false
    );

    mSubscribedPlaylistRecycler = (RecyclerView) view.findViewById(R.id.subscribedPlaylistSectionRecycler);
    mSubscribedPlaylistRecycler.setHasFixedSize(true);
    mSubscribedPlaylistRecycler.setLayoutManager(layoutManager);
    mSubscribedPlaylistRecycler.setAdapter(mSubscribedPlaylistAdapter);
  }

  private void loadSavedPodCasts() {
    mMainViewModel.loadSavedPodCasts().observe(this.getViewLifecycleOwner(), new Observer<List<PodCastEntity>>() {
      @Override
      public void onChanged(List<PodCastEntity> entities) {
        ArrayList podcasts = new ArrayList(entities.size());
        for (PodCastEntity item : entities) {
          PodcastData data = new SmPodcast(item.getTrackId(), item.getName(), null, item.getUrlImg(), false, true, item.getRssUrl());
          data.setSaved(true);
          podcasts.add(data);
        }
        mSubscribedAdapter.updateData(podcasts);
      }
    });
  }

  private void loadSavedPlaylist() {
    mMainViewModel.loadSavedPlaylist().observe(this.getViewLifecycleOwner(), new Observer<List<PlaylistEntity>>() {
      @Override
      public void onChanged(List<PlaylistEntity> playlistEntities) {
        ArrayList<PlaylistData> items = new ArrayList<>();
        for (PlaylistEntity entity : playlistEntities) {
          PlaylistData item = PlaylistData.transformFromEntity(entity);
          item.setInfo(false);
          item.setSaved(true);
          items.add(item);
        }
        mSubscribedPlaylistAdapter.updateData(items);
      }
    });
  }

  public PlaylistAdapter.ItemActionClicked actionCallback() {
    return new PlaylistAdapter.ItemActionClicked() {
      @Override
      public void onItemClicked(PlaylistData rssItem, int position) { }

      @Override
      public void markerClicked(PlaylistData rssItem) { }

      @Override
      public void infoClicked(PlaylistData rssItem) {
        // TODO(PENDING) here we will create a new popup screen with podcast or track description
      }
    };
  }

}
