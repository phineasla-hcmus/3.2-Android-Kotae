package com.ogif.kotae.ui.leaderboard.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.model.BaseItem;
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter;
import com.ogif.kotae.ui.leaderboard.LeaderboardItem;

import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("rawtypes")
public final class LeaderboardAdapter extends TrackableRecyclerViewAdapter {
    @Nullable
    private OnItemClickListener<LeaderboardItem> mOnItemClickListener;


    protected void assignListeners(@NonNull LeaderboardItem.ViewHolder holder, int position, @NonNull LeaderboardItem item) {
        super.assignListeners((com.arthurivanets.adapster.model.BaseItem.ViewHolder) holder, position, item);
        item.setOnItemClickListener(holder, this.mOnItemClickListener);
    }

    public void assignListeners(LeaderboardItem.ViewHolder var1, int var2, BaseItem var3) {
        this.assignListeners((LeaderboardItem.ViewHolder) var1, var2, (LeaderboardItem) var3);
    }

    public LeaderboardAdapter(@NonNull Context context, @NonNull List items) {
        super(context, items);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder((BaseItem.ViewHolder<?>) holder, position);
    }
}
