package com.ogif.kotae.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.paulrybitskyi.commons.ktx.NumberUtils;

public abstract class HeaderedRecyclerViewListener extends OnScrollListener {
    private int scrollDetectionDistance;
    private int firstVisiblePosition;
    private int previousFirstVisiblePosition;

    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) {
            this.onScrolledDownwards(recyclerView, dy);
        } else if (dy < 0) {
            this.onScrolledUpwards(recyclerView, dy);
        }

    }

    private final void onScrolledUpwards(RecyclerView recyclerView, int deltaY) {
        this.firstVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
        boolean isFirstItem = this.firstVisiblePosition == 0;
        if (this.shouldShowHeader(deltaY, isFirstItem)) {
            this.showHeader();
        }

        this.previousFirstVisiblePosition = this.firstVisiblePosition;
    }

    private final boolean shouldShowHeader(int deltaY, boolean isFirstItem) {
        return Math.abs(deltaY) >= this.scrollDetectionDistance || isFirstItem && this.firstVisiblePosition != this.previousFirstVisiblePosition;
    }

    private final void onScrolledDownwards(RecyclerView recyclerView, int deltaY) {
        this.firstVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
        if (this.shouldHideHeader(deltaY)) {
            this.hideHeader();
        }

        this.previousFirstVisiblePosition = this.firstVisiblePosition;
    }

    private final boolean shouldHideHeader(int deltaY) {
        return this.firstVisiblePosition > 0 && (Math.abs(deltaY) >= this.scrollDetectionDistance || this.firstVisiblePosition > this.previousFirstVisiblePosition);
    }

    public void showHeader() {
    }

    public void hideHeader() {
    }

    public HeaderedRecyclerViewListener(@NonNull Context context) {
        super();
        this.scrollDetectionDistance = NumberUtils.dpToPx(10, context);
    }
}
