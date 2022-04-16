package com.ogif.kotae.utils.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Record;

import java.util.List;

public class RecordUtils {

    public static abstract class ListComparator<T extends Record> extends DiffUtil.Callback {
        List<T> newList;
        List<T> oldList;

        public ListComparator(@NonNull List<T> newList, @NonNull List<T> oldList) {
            this.newList = newList;
            this.oldList = oldList;
        }

        /**
         * Returns the size of the old list.
         *
         * @return The size of the old list.
         */
        @Override
        public final int getOldListSize() {
            return oldList.size();
        }

        /**
         * Returns the size of the new list.
         *
         * @return The size of the new list.
         */
        @Override
        public final int getNewListSize() {
            return newList.size();
        }

        /**
         * Called by the DiffUtil to decide whether two object represent the same Item.
         * <p>
         * For example, if your items have unique ids, this method should check their id equality.
         *
         * @param oldItemPosition The position of the item in the old list
         * @param newItemPosition The position of the item in the new list
         * @return True if the two items represent the same object or false if they are different.
         */
        @Override
        public final boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId()
                    .equals(newList.get(newItemPosition).getId());
        }

        public final boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return oldItem.getContent().equals(newItem.getContent());
        }

        public final boolean areVotesTheSame(@NonNull T oldItem, @NonNull T newItem) {
            int oldVote = oldItem.getUpvote() - oldItem.getDownvote();
            int newVote = newItem.getUpvote() - newItem.getDownvote();
            return oldVote == newVote;
        }

        public final boolean areBlockStatusesTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return oldItem.getBlocked() == newItem.getBlocked();
        }

        /**
         * Called by the DiffUtil when it wants to check whether two items have the same data.
         * DiffUtil uses this information to detect if the contents of an item has changed.
         * <p>
         * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
         * so that you can change its behavior depending on your UI.
         * For example, if you are using DiffUtil with a
         * {@link RecyclerView.Adapter RecyclerView.Adapter}, you should
         * return whether the items' visual representations are the same.
         * <p>
         * This method is called only if {@link #areItemsTheSame(int, int)} returns
         * {@code true} for these items.
         *
         * @param oldItemPosition The position of the item in the old list
         * @param newItemPosition The position of the item in the new list which replaces the
         *                        oldItem
         * @return True if the contents of the items are the same or false if they are different.
         */
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldList.get(oldItemPosition);
            T newItem = newList.get(newItemPosition);
            return areVotesTheSame(oldItem, newItem)
                    && areBlockStatusesTheSame(oldItem, newItem)
                    && areContentsTheSame(oldItem, newItem);
        }
    }

    public static boolean isContentValid(@NonNull CharSequence content) {
        return content.length() >= Global.CONTENT_MIN;
    }
}
