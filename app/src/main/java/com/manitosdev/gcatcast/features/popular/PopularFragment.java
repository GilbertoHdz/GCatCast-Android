package com.manitosdev.gcatcast.features.popular;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.manitosdev.gcatcast.api.models.ApiResult;
import com.manitosdev.gcatcast.api.models.search.Result;
import com.manitosdev.gcatcast.api.models.search.SearchResult;
import com.manitosdev.gcatcast.api.network.InternetCheck;
import com.manitosdev.gcatcast.api.repository.ItunesRepository;
import com.manitosdev.gcatcast.db.AppDatabase;
import com.manitosdev.gcatcast.features.common.adapter.PodcastAdapter;
import com.manitosdev.gcatcast.features.common.models.LgPodcast;
import com.manitosdev.gcatcast.features.common.models.PodcastData;
import com.manitosdev.gcatcast.features.common.models.SmPodcast;
import com.manitosdev.gcatcast.features.main.MainViewModel;
import java.util.ArrayList;

public class PopularFragment extends Fragment {

  private MainViewModel mMainViewModel;

  private TextView _error_message;
  private ProgressBar _loader;
  private Button _retry;
  private RecyclerView rvTopPodcast;
  private RecyclerView rvBodyPodcast;

  private PodcastAdapter smPodcastAdapter;
  private PodcastAdapter lgPodcastAdapter;

  private AppDatabase mDb;

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
    mMainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    mMainViewModel.getSearchResultMutableLiveData().observe(this.getViewLifecycleOwner(), podcastOberver());
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    _error_message = (TextView) view.findViewById(R.id.popular_section_error_message_text);
    _loader = (ProgressBar) view.findViewById(R.id.popularSectionProgress);
    _retry = (Button) view.findViewById(R.id.popularSectionRetry);

    mDb = AppDatabase.getInstance(requireActivity());
    smPodcastAdapter = new PodcastAdapter(requireActivity(), mDb);
    lgPodcastAdapter = new PodcastAdapter(requireActivity(), mDb);
    initializeRecyclers(view);

    checkNetworkConnection();
  }

  private void checkNetworkConnection() {
    initialState();
    new InternetCheck(new InternetCheck.Consumer() {
      @Override
      public void accept(Boolean internet) {
        if (internet) {
          ItunesRepository.getInstance().loadPodcasts("popular");
        } else {
          showErrorMessage(R.string.error_network_message);
        }
      }
    });
  }

  private void initialState() {
    _loader.setVisibility(View.VISIBLE);

    _error_message.setVisibility(View.GONE);
    _retry.setVisibility(View.GONE);
    _retry.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkNetworkConnection();
      }
    });
    rvTopPodcast.setVisibility(View.INVISIBLE);
    rvBodyPodcast.setVisibility(View.INVISIBLE);
  }

  private void showErrorMessage(int resMsgId) {
    String message = requireContext().getString(resMsgId);
    _error_message.setText(message);
    _error_message.setVisibility(View.VISIBLE);
    _retry.setVisibility(View.VISIBLE);
    _loader.setVisibility(View.GONE);
    rvTopPodcast.setVisibility(View.INVISIBLE);
    rvBodyPodcast.setVisibility(View.INVISIBLE);
  }

  private void initializeRecyclers(@NonNull View view) {
    RecyclerView.LayoutManager layoutManager;

    layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
    rvTopPodcast = (RecyclerView) view.findViewById(R.id.popularTopSectionRecycler);
    rvTopPodcast.setHasFixedSize(true);
    rvTopPodcast.setLayoutManager(layoutManager);
    rvTopPodcast.setAdapter(smPodcastAdapter);

    layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
    rvBodyPodcast = (RecyclerView) view.findViewById(R.id.popularBodySectionRecycler);
    rvBodyPodcast.setHasFixedSize(true);
    rvBodyPodcast.setLayoutManager(layoutManager);
    rvBodyPodcast.setAdapter(lgPodcastAdapter);
  }

  private Observer<ApiResult<SearchResult>> podcastOberver() {
    return new Observer<ApiResult<SearchResult>>() {
      @Override
      public void onChanged(ApiResult<SearchResult> result) {
        ArrayList<PodcastData> smItems = new ArrayList<>();
        ArrayList<PodcastData> lgItems = new ArrayList<>();

        for (int i = 0; i < result.getResult().getResultCount(); i++) {
          Result item = result.getResult().getResults().get(i);

          if (i <= 5) {
            smItems.add(new SmPodcast(item.getCollectionId(), item.getArtistName(), null, item.getArtworkUrl100(), false, false, item.getFeedUrl()));
          } else {
            lgItems.add(new LgPodcast(item.getCollectionId(), item.getArtistName(), "", item.getArtworkUrl100(), false, true, item.getFeedUrl()));
          }
        }

        smPodcastAdapter.updateData(smItems);
        lgPodcastAdapter.updateData(lgItems);

        _loader.setVisibility(View.GONE);
        rvTopPodcast.setVisibility(View.VISIBLE);
        rvBodyPodcast.setVisibility(View.VISIBLE);
      }
    };
  }
}
