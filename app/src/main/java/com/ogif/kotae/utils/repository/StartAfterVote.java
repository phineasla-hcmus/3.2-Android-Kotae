package com.ogif.kotae.utils.repository;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.ogif.kotae.data.model.Record;

public class StartAfterVote implements QueryOption {
    OrderByVote orderByVote;
    Record previous;

    public StartAfterVote(Record previous) {
        this.orderByVote = new OrderByVote();
        this.previous = previous;
    }

    @Override
    public Query inject(@NonNull Query query) {
        return orderByVote.inject(query)
                .startAfter(previous.getUpvote(), previous.getDownvote(), previous.getId());
    }
}
