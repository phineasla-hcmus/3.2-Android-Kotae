package com.ogif.kotae.utils.model;

import androidx.annotation.NonNull;

import com.ogif.kotae.data.model.Comment;

import java.util.List;

public class CommentUtils extends RecordUtils {
    public static class ListComparator extends RecordUtils.ListComparator<Comment> {

        public ListComparator(@NonNull List<Comment> newList, @NonNull List<Comment> oldList) {
            super(newList, oldList);
        }

        // Stub
    }
}
