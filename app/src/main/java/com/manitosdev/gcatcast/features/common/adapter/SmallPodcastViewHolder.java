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
import com.manitosdev.gcatcast.features.common.models.SmPodcast;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class SmallPodcastViewHolder extends RecyclerView.ViewHolder {

  private ImageView thumbnail;
  private ImageView btnMarker;
  private TextView name;
  private ConstraintLayout viewContainer;
  private Context mContext;

  public SmallPodcastViewHolder(@NonNull View itemView) {
    super(itemView);
    mContext = itemView.getContext();
    thumbnail = (ImageView) itemView.findViewById(R.id.common_item_podcast_sm_thumbnail);
    btnMarker = (ImageView) itemView.findViewById(R.id.common_item_podcast_sm_marker);
    name = (TextView) itemView.findViewById(R.id.common_item_podcast_sm_name);
    viewContainer = (ConstraintLayout) itemView.findViewById(R.id.common_item_podcast_sm_container);
  }

  void bind(final SmPodcast data, final PodcastAdapter.ItemActionClicked action) {
    name.setText(data.getName());
    loadThumbnail(data.getUrlImg());
    btnMarker.setVisibility(data.isHasMarkerIcon() ? View.VISIBLE : View.INVISIBLE);
    btnMarker.setTag("" + data.isSaved());
    setMarkerIcon(data.isSaved());

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
        action.markerClicked(data);      }
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
