package com.manitosdev.gcatcast.ui.main.features.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.features.common.models.PlaylistData;
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

  private ArrayList<PlaylistData> items = new ArrayList<>();

  public interface ItemActionClicked {
    void onItemClicked(PlaylistData rssItem, int position);
    void markerClicked(PlaylistData rssItem);
    void infoClicked(PlaylistData rssItem);
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
    holder.bind(items.get(position), _callback, position);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public void updateData(List<PlaylistData> rssItems) {
    items.clear();
    items.addAll(rssItems);
    notifyDataSetChanged();
  }

  public ArrayList<PlaylistData> getPlaylist() {
    return items;
  }
}
