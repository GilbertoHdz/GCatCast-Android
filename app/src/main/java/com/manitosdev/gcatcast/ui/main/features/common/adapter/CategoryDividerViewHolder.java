package com.manitosdev.gcatcast.ui.main.features.common.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.features.common.models.CategoryDivider;

/**
 * Created by gilbertohdz on 06/06/20.
 */
public class CategoryDividerViewHolder extends RecyclerView.ViewHolder {

  private TextView name;

  public CategoryDividerViewHolder(@NonNull View itemView) {
    super(itemView);
    name = (TextView) itemView.findViewById(R.id.common_item_podcast_separator_title);
  }

  void bind(final CategoryDivider data) {
    name.setText(data.getName());
  }
}
