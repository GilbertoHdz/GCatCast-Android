package com.manitosdev.gcatcast.ui.main.features.playlist;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.api.models.search.RssItem;

/**
 * Created by gilbertohdz on 07/06/20.
 */
public class PlaylistViewHolder extends RecyclerView.ViewHolder {

  private TextView name;
  private TextView desc;
  private ImageView thumbnail;
  private ImageView btnInfo;
  private final ImageView btnMarker;
  private ConstraintLayout viewContainer;
  private Context mContext;

  public PlaylistViewHolder(@NonNull View itemView) {
    super(itemView);
    mContext = itemView.getContext();
    thumbnail = (ImageView) itemView.findViewById(R.id.playlist_item_rss_feed_thumbnail);
    btnInfo = (ImageView) itemView.findViewById(R.id.playlist_item_rss_feed_info);
    btnMarker = (ImageView) itemView.findViewById(R.id.playlist_item_rss_feed_marker);
    name = (TextView) itemView.findViewById(R.id.playlist_item_rss_feed_name);
    desc = (TextView) itemView.findViewById(R.id.playlist_item_rss_feed_desc);
    viewContainer = (ConstraintLayout) itemView.findViewById(R.id.playlist_item_rss_feed_container);
  }

  void bind(final RssItem item, final PlaylistAdapter.ItemActionClicked action) {
    name.setText(item.getTitle());
    desc.setText(item.getDescription());
    loadThumbnail(item.getEnclosure().getThumbnail());

    viewContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        action.onItemClicked(item);
      }
    });

    btnMarker.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isMarked = Boolean.getBoolean((String) btnMarker.getTag());
        btnMarker.setTag(""+!isMarked);

        setMarkerIcon(!isMarked);
        action.markerClicked(item);
      }
    });

    btnInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        action.infoClicked(item);
      }
    });
  }

  private void setMarkerIcon(boolean isMarked) {
    int icon = isMarked ?  R.drawable.ic_turned_in_black_24dp : R.drawable.ic_turned_in_not_black_24dp;
    btnMarker.setImageDrawable(mContext.getResources().getDrawable(icon));
  }

  private void loadThumbnail(String thumbnailUrl) {
    CircularProgressDrawable progressDrawable = new CircularProgressDrawable(mContext);
    progressDrawable.setStrokeWidth(5f);
    progressDrawable.setCenterRadius(25f);
    progressDrawable.start();
    Glide
        .with(mContext)
        .load(thumbnailUrl)
        .placeholder(progressDrawable)
        .into(thumbnail);
  }
}
