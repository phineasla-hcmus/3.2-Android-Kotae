package com.ogif.kotae.utils.model;

import androidx.annotation.NonNull;

import com.ogif.kotae.data.model.Post;

import java.util.List;

public class PostUtils extends RecordUtils {
    public static class ListComparator extends RecordUtils.ListComparator<Post> {

        public ListComparator(@NonNull List<Post> newList, @NonNull List<Post> oldList) {
            super(newList, oldList);
        }

        // Stub
    }
}
