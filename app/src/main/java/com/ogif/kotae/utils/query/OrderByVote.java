package com.ogif.kotae.utils.query;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.ogif.kotae.data.model.Record;
import com.ogif.kotae.data.repository.PagingOption;

public class OrderByVote implements PagingOption {
    @Override
    public Query inject(@NonNull Query query) {
        return query.orderBy(Record.Field.UPVOTE, Query.Direction.DESCENDING)
                .orderBy(Record.Field.DOWNVOTE, Query.Direction.ASCENDING);
    }
}
