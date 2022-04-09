package com.ogif.kotae.ui.search.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arthurivanets.adapster.model.BaseItem;
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter;
import com.ogif.kotae.ui.search.SearchItem;
import com.ogif.kotae.utils.text.MarkdownUtils;

import java.util.List;

public final class SearchAdapter extends TrackableRecyclerViewAdapter {
    private static Context context;

    public SearchAdapter(@NonNull Context context, @NonNull List items) {
        super(context, items);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder((BaseItem.ViewHolder<?>) holder, position);
    }

    public static void setMarkdown(String content, TextView tvContent) {
        MarkdownUtils.setMarkdown(context, content, tvContent);
    }
}
