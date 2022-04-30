package com.ogif.kotae.data.repository;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface PagingOption {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ASCENDING, DESCENDING})
    @interface Direction {
    }

    int ASCENDING = 1;
    int DESCENDING = -1;

    Query inject(@NonNull Query query);
}