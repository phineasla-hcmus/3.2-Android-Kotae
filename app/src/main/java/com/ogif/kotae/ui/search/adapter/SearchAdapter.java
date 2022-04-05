package com.ogif.kotae.ui.search.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arthurivanets.adapster.model.BaseItem;
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter;

import java.util.List;

public final class SearchAdapter extends TrackableRecyclerViewAdapter {
    public SearchAdapter(@NonNull Context context, @NonNull List items) {
        super(context, items);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder((BaseItem.ViewHolder<?>) holder, position);
    }
}
