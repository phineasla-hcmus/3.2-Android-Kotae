package com.ogif.kotae.utils.query;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.ogif.kotae.data.repository.PagingOption;

import java.util.Date;

public class StartAfterDate implements PagingOption {
    OrderByDate orderByDate;
    Date previous;

    public StartAfterDate(Date previous) {
        this.orderByDate = new OrderByDate();
        this.previous = previous;
    }

    public StartAfterDate(Date previous, @Direction int direction) {
        this.orderByDate = new OrderByDate(direction);
        this.previous = previous;
    }

    @Override
    public Query inject(@NonNull Query query) {
        return orderByDate.inject(query).startAfter(previous);
    }
}
