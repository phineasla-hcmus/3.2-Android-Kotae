package com.ogif.kotae.utils.repository;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;

import java.util.Date;

public class StartAfterDate implements QueryOption {
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
