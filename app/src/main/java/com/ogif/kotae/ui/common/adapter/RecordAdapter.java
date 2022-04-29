package com.ogif.kotae.ui.common.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.data.model.Record;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.ui.common.view.VoteView;
import com.ogif.kotae.ui.questiondetail.adapter.QuestionDetailAdapter;
import com.ogif.kotae.utils.model.RecordUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class RecordAdapter<T extends Record> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final Context context;
    protected final List<T> items;
    private OnVoteChangeListener voteChangeListener;

    /**
     * Wrapper for {@link VoteView.OnStateChangeListener} to add position
     */
    public interface OnVoteChangeListener {
        void onChange(VoteView view, int position, @Vote.State int previous, @Vote.State int current);
    }

    public RecordAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public <U extends RecordUtils.ListComparator<T>> void setItems(@NonNull List<T> items, U comparator) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(comparator);
        this.items.clear();
        this.items.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    public abstract void setItems(@NonNull List<T> items);

    public void setOnVoteChangeListener(@Nullable OnVoteChangeListener listener) {
        this.voteChangeListener = listener;
    }
}
