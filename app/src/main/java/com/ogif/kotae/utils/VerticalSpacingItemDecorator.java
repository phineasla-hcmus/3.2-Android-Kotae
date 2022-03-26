package com.ogif.kotae.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;

public final class VerticalSpacingItemDecorator extends ItemDecoration {
    private final int verticalSpacing;
    private final int verticalSpacingCompensation;

    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
        int position = parent.getChildAdapterPosition(view);
        Adapter adapter = parent.getAdapter();
        if (position != adapter.getItemCount() - 1) {
            outRect.bottom = this.verticalSpacing - this.verticalSpacingCompensation;
        } else {
            outRect.bottom = this.verticalSpacing;
        }

    }

    public VerticalSpacingItemDecorator(int verticalSpacing, int verticalSpacingCompensation) {
        this.verticalSpacing = verticalSpacing;
        this.verticalSpacingCompensation = verticalSpacingCompensation;
    }
}
