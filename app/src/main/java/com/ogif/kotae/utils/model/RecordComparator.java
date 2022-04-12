package com.ogif.kotae.utils.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.Callback;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.data.model.Record;

public class RecordComparator<T extends Record> extends DiffUtil.ItemCallback<T> {

    /**
     * Called to check whether two objects represent the same item.
     * <p>
     * For example, if your items have unique ids, this method should check their id equality.
     * <p>
     * Note: {@code null} items in the list are assumed to be the same as another {@code null}
     * item and are assumed to not be the same as a non-{@code null} item. This callback will
     * not be invoked for either of those cases.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the two items represent the same object or false if they are different.
     * @see Callback#areItemsTheSame(int, int)
     */
    @Override
    public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    /**
     * Called to check whether two items have the same data.
     * <p>
     * This information is used to detect if the contents of an item have changed.
     * <p>
     * This method to check equality instead of {@link Object#equals(Object)} so that you can
     * change its behavior depending on your UI.
     * <p>
     * For example, if you are using DiffUtil with a
     * {@link RecyclerView.Adapter RecyclerView.Adapter}, you should
     * return whether the items' visual representations are the same.
     * <p>
     * This method is called only if
     * {@link RecordComparator#areItemsTheSame(T, T)} (T, T)} returns {@code true} for
     * these items.
     * <p>
     * Note: Two {@code null} items are assumed to represent the same contents. This callback
     * will not be invoked for this case.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the contents of the items are the same or false if they are different.
     * @see Callback#areContentsTheSame(int, int)
     */
    @Override
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        String oldContent = oldItem.getContent();
        String newContent = newItem.getContent();
        int oldVote = oldItem.getUpvote() - oldItem.getDownvote();
        int newVote = newItem.getUpvote() - newItem.getDownvote();
        boolean oldIsBlocked = oldItem.getBlocked();
        boolean newIsBlocked = newItem.getBlocked();
        return oldIsBlocked == newIsBlocked && oldVote != newVote && oldContent.equals(newContent);
    }
}
