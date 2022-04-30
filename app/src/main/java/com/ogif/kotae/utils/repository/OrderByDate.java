package com.ogif.kotae.utils.repository;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.ogif.kotae.data.model.Record;

public class OrderByDate implements QueryOption {
    Query.Direction direction;

    public OrderByDate() {
        this.direction = Query.Direction.DESCENDING;
    }

    public OrderByDate(@Direction int direction) {
        this.direction = direction == ASCENDING ?
                Query.Direction.ASCENDING :
                Query.Direction.DESCENDING;
    }

    @Override
    public Query inject(@NonNull Query query) {
        return query.orderBy(Record.Field.POST_TIME, direction);
    }
}
