package com.ogif.kotae.ui.common.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.data.model.Record;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.ui.common.view.VoteView;
import com.ogif.kotae.utils.model.RecordUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class RecordAdapter<T extends Record> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final Context context;
    protected final List<T> items;
    protected OnVoteChangeListener voteChangeListener;

    /**
     * Wrapper for {@link VoteView.OnStateChangeListener} to add position
     */
    public interface OnVoteChangeListener {
        void onChange(int position, VoteView view, @Vote.State int previous, @Vote.State int current);
    }

    public RecordAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return this.items.get(position);
    }

    public void setItem(int position, @NonNull T item) {
        this.items.set(position, item);
    }

    protected <U extends RecordUtils.ListComparator<T>> void setItems(@NonNull List<T> items, U comparator) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(comparator);
        this.items.clear();
        this.items.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    public abstract void setItems(@NonNull List<T> items);

    public void addItem(int position, @NonNull T item) {
        this.items.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(@NonNull T item) {
        this.items.add(item);
        notifyItemInserted(this.items.size() - 1);
    }

    public void addItems(@NonNull List<? extends T> items) {
        int from = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(from, items.size());
    }

    protected void onVoteChangeListenerIfNotNull(int position, VoteView view, @Vote.State int previous, @Vote.State int current) {
        if (voteChangeListener != null)
            voteChangeListener.onChange(position, view, previous, current);
    }

    public OnVoteChangeListener getOnVoteChangeListener() {
        return voteChangeListener;
    }

    public void setOnVoteChangeListener(@Nullable OnVoteChangeListener listener) {
        this.voteChangeListener = listener;
    }
}
