package com.ogif.kotae.utils.query;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.ogif.kotae.data.model.Record;
import com.ogif.kotae.data.repository.PagingOption;

public class StartAfterVote implements PagingOption {
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
