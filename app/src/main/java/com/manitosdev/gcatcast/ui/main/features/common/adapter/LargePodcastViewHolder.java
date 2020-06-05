package com.manitosdev.gcatcast.ui.main.features.common.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.features.common.models.LgPodcast;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class LargePodcastViewHolder extends RecyclerView.ViewHolder {

  private ImageView thumbnail;
  private ImageView btnInfo;
  private ImageView btnMarker;
  private TextView name;
  private TextView desc;
  private ConstraintLayout viewContainer;

  public LargePodcastViewHolder(@NonNull View itemView) {
    super(itemView);
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

    viewContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        action.onItemClicked(data.getRssUrl());
      }
    });
  }
}
