package com.manitosdev.gcatcast.features.common.adapter;

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
import com.manitosdev.gcatcast.features.common.models.LgPodcast;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class LargePodcastViewHolder extends RecyclerView.ViewHolder {

  private ImageView thumbnail;
  private ImageView btnInfo;
  private final ImageView btnMarker;
  private TextView name;
  private TextView desc;
  private ConstraintLayout viewContainer;
  private Context mContext;

  public LargePodcastViewHolder(@NonNull View itemView) {
    super(itemView);
    mContext = itemView.getContext();
    thumbnail = (ImageView) itemView.findViewById(R.id.common_item_podcast_lg_thumbnail);
    btnInfo = (ImageView) itemView.findViewById(R.id.common_item_podcast_lg_info);
    btnMarker = (ImageView) itemView.findViewById(R.id.common_item_podcast_lg_marker);
    name = (TextView) itemView.findViewById(R.id.common_item_podcast_lg_name);
    desc = (TextView) itemView.findViewById(R.id.common_item_podcast_lg_desc);
    viewContainer = (ConstraintLayout) itemView.findViewById(R.id.common_item_podcast_lg_container);
  }

  void bind(final LgPodcast data, final PodcastAdapter.ItemActionClicked action) {
    name.setText(data.getName());
    desc.setText(data.getDescription());
    btnMarker.setVisibility(data.isHasMarkerIcon() ? View.VISIBLE : View.INVISIBLE);
    btnMarker.setTag("" + data.isSaved());
    setMarkerIcon(data.isSaved());
    btnInfo.setVisibility(data.isHasInfoIcon() ? View.VISIBLE : View.INVISIBLE);
    loadThumbnail(data.getUrlImg());

    viewContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        action.onItemClicked(data);
      }
    });

    btnMarker.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isMarked = Boolean.getBoolean((String) btnMarker.getTag());
        btnMarker.setTag(""+!isMarked);

        setMarkerIcon(!isMarked);
        action.markerClicked(data);
      }
    });

    btnInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        action.infoClicked(data);
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
    progressDrawable.setCenterRadius(15f);
    progressDrawable.start();
    Glide
        .with(mContext)
        .load(thumbnailUrl)
        .placeholder(progressDrawable)
        .into(thumbnail);
  }
}
