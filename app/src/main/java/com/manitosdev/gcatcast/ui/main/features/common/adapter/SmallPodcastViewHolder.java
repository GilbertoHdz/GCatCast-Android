package com.manitosdev.gcatcast.ui.main.features.common.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.features.common.models.SmPodcast;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class SmallPodcastViewHolder extends RecyclerView.ViewHolder {

  private ImageView thumbnail;
  private ImageView btnMarker;
  private TextView name;
  private ConstraintLayout viewContainer;

  public SmallPodcastViewHolder(@NonNull View itemView) {
    super(itemView);
    thumbnail = (ImageView) itemView.findViewById(R.id.common_item_podcast_sm_thumbnail);
    btnMarker = (ImageView) itemView.findViewById(R.id.common_item_podcast_sm_marker);
    name = (TextView) itemView.findViewById(R.id.common_item_podcast_sm_name);
    viewContainer = (ConstraintLayout) itemView.findViewById(R.id.common_item_podcast_sm_container);
  }

  void bind(final SmPodcast data, final PodcastAdapter.ItemActionClicked action) {
    name.setText(data.getName());

    viewContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        action.onItemClicked(data.getRssUrl());
      }
    });
  }
}
