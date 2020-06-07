package com.manitosdev.gcatcast.ui.main.features.common.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.api.network.AppExecutors;
import com.manitosdev.gcatcast.ui.main.db.AppDatabase;
import com.manitosdev.gcatcast.ui.main.features.common.models.CategoryDivider;
import com.manitosdev.gcatcast.ui.main.features.common.models.LgPodcast;
import com.manitosdev.gcatcast.ui.main.features.common.models.PodcastData;
import com.manitosdev.gcatcast.ui.main.features.common.models.SmPodcast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class PodcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Activity mContext;
  private AppDatabase mDb;

  public PodcastAdapter(Activity mContext, AppDatabase appDatabase) {
    this.mContext = mContext;
    this.mDb = appDatabase;
  }

  public  static final int SMALL_ITEM = 0;
  public  static final int LARGE_ITEM = 1;
  public  static final int DIVIDER_ITEM = 2;

  private ArrayList<PodcastData> podcastData = new ArrayList<>();

  public interface ItemActionClicked {
    void onItemClicked(PodcastData podcast);
    void markerClicked(PodcastData podcast);
    void infoClicked(PodcastData podcast);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View _v;
    RecyclerView.ViewHolder vh = null;
    switch (viewType) {
      case SMALL_ITEM:
        _v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item_podcast_small_detail, parent, false);
        vh = new SmallPodcastViewHolder(_v);
        break;
      case LARGE_ITEM:
        _v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item_podcast_large_detail, parent, false);
        vh=  new LargePodcastViewHolder(_v);
        break;
      case DIVIDER_ITEM:
        _v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item_podcast_separator, parent, false);
        vh=  new CategoryDividerViewHolder(_v);
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
          (SmPodcast) podcastData.get(position),
          clickObserver()
      );
    } else if (holder instanceof LargePodcastViewHolder) {
      ((LargePodcastViewHolder) holder).bind(
          (LgPodcast) podcastData.get(position),
          clickObserver()
      );
    } else if (holder instanceof CategoryDividerViewHolder) {
      ((CategoryDividerViewHolder) holder).bind(
          (CategoryDivider) podcastData.get(position)
      );
    } else {
      throw new IllegalStateException("bind holder exception by: " + holder.getClass().getSimpleName());
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (podcastData.get(position) instanceof SmPodcast) {
      return SMALL_ITEM;
    } else if (podcastData.get(position) instanceof LgPodcast) {
      return LARGE_ITEM;
    } else if (podcastData.get(position) instanceof CategoryDivider) {
      return DIVIDER_ITEM;
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

  public int getItemIdPosition(int position) {
    if (podcastData.get(position) instanceof CategoryDivider) {
      return DIVIDER_ITEM;
    }

    return -1;
  }

  public ItemActionClicked clickObserver() {
    return new ItemActionClicked() {
      @Override
      public void onItemClicked(PodcastData podcast) {
        Log.i("RSS_URL", podcast.getRssUrl());
      }

      @Override
      public void markerClicked(final PodcastData podcast) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
          @Override
          public void run() {
            if (podcast.isSaved()) {
              mDb.podCastDao().deletePodCast(podcast.transformToPodCEntity());
            } else {
              mDb.podCastDao().insertPodCast(podcast.transformToPodCEntity());
            }
          }
        });
      }

      @Override
      public void infoClicked(PodcastData podcast) {
        // TODO(PENDING) here we will create a new popup screen with podcast or track description
      }
    };
  }
}
