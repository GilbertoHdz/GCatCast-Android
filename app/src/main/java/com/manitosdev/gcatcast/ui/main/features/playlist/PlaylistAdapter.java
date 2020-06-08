package com.manitosdev.gcatcast.ui.main.features.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.api.models.search.RssItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilbertohdz on 07/06/20.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

  private static String TAG = PlaylistAdapter.class.getSimpleName();

  private ItemActionClicked _callback;

  public PlaylistAdapter(ItemActionClicked actionCallback) {
    this._callback = actionCallback;
  }

  private ArrayList<RssItem> items = new ArrayList<>();

  public interface ItemActionClicked {
    void onItemClicked(RssItem rssItem);
    void markerClicked(RssItem rssItem);
    void infoClicked(RssItem rssItem);
  }

  @NonNull
  @Override
  public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View _view = (CardView) LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.playlist_item_rss_feed, parent, false);

    return new PlaylistViewHolder(_view);
  }

  @Override
  public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
    holder.bind(items.get(position), _callback);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public void updateData(List<RssItem> rssItems) {
    items.clear();
    items.addAll(rssItems);
    notifyDataSetChanged();
  }
}
