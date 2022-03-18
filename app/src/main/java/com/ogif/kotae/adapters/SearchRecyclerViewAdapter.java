package com.ogif.kotae.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.model.BaseItem;
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter;
import com.ogif.kotae.adapters.model.SearchItem;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public final class SearchRecyclerViewAdapter extends TrackableRecyclerViewAdapter {
    @Nullable
    private OnItemClickListener<SearchItem> mOnItemClickListener;

    @Nullable
    public final OnItemClickListener<SearchItem> getMOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public final void setMOnItemClickListener(@Nullable OnItemClickListener<SearchItem> var1) {
        this.mOnItemClickListener = var1;
    }


    protected void assignListeners(@NonNull SearchItem.ViewHolder holder, int position, @NonNull SearchItem item) {
        super.assignListeners((com.arthurivanets.adapster.model.BaseItem.ViewHolder) holder, position, item);
        item.setOnItemClickListener(holder, this.mOnItemClickListener);
    }

    public void assignListeners(SearchItem.ViewHolder var1, int var2, BaseItem var3) {
        this.assignListeners((SearchItem.ViewHolder) var1, var2, (SearchItem) var3);
    }

    public SearchRecyclerViewAdapter(@NonNull Context context, @NonNull List items) {
        super(context, items);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
