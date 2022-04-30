package com.ogif.kotae.utils.repository;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.ogif.kotae.data.model.Record;

public class OrderByVote implements QueryOption {
    @Override
    public Query inject(@NonNull Query query) {
        return query.orderBy(Record.Field.UPVOTE, Query.Direction.DESCENDING)
                .orderBy(Record.Field.DOWNVOTE, Query.Direction.ASCENDING);
    }
}
