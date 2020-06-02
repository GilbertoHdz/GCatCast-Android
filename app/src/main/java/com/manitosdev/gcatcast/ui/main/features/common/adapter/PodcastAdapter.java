package com.manitosdev.gcatcast.ui.main.features.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.features.common.models.LgPodcast;
import com.manitosdev.gcatcast.ui.main.features.common.models.PodcastData;
import com.manitosdev.gcatcast.ui.main.features.common.models.SmPodcast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class PodcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final int SMALL_ITEM = 0;
  private final int LARGE_ITEM = 1;

  private ArrayList<PodcastData> podcastData;

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View _v;
    RecyclerView.ViewHolder vh = null;
    switch (viewType) {
      case SMALL_ITEM:
        _v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item_podcast_small_detail, parent, false);
        vh = new SmallPodcastViewHolder(_v);
        break;
      case LARGE_ITEM:
        _v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item_podcast_small_detail, parent, false);
        vh=  new LargePodcastViewHolder(_v);
        break;
      default:
        throw new IllegalStateException("viewType id not found: " + viewType);
    }

    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof SmallPodcastViewHolder) {
      ((SmallPodcastViewHolder) holder).bind(
          (SmPodcast) podcastData.get(position)
      );
    } else if (holder instanceof LargePodcastViewHolder) {
      ((LargePodcastViewHolder) holder).bind(
          (LgPodcast) podcastData.get(position)
      );
    } else {
      throw new IllegalStateException("bind holder exception by: " + holder.getClass().getSimpleName());
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (podcastData.get(position) instanceof SmPodcast) {
      return 0;
    } else if (podcastData.get(position) instanceof LgPodcast) {
      return 1;
    } else {
      throw new IllegalStateException("item view type position not found: " + position);
    }
  }

  @Override
  public int getItemCount() {
    return podcastData.size();
  }

  public void updateData(List<PodcastData> items) {
    podcastData.clear();
    podcastData.addAll(items);

    notifyDataSetChanged();
  }
}
