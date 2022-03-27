package com.ogif.kotae.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ogif.kotae.R;

import java.util.Locale;

public class VerticalVoteView extends VoteView {
    private TextView upvoteCounter;
    private TextView downvoteCounter;

    public VerticalVoteView(@NonNull Context context) {
        super(context);
    }

    public VerticalVoteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalVoteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VerticalVoteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void inflate() {
        inflate(getContext(), R.layout.partial_vertical_vote_view, this);
    }

    @Override
    protected void init() {
        super.init();
        this.upvoteCounter = findViewById(R.id.tv_vertical_vote_view_upvote);
        this.downvoteCounter = findViewById(R.id.tv_vertical_vote_view_downvote);
    }

    @Override
    protected void setUpvoteText() {
        upvoteCounter.setText(String.format(Locale.getDefault(), "%d", upvoteCount));
    }

    @Override
    protected void setDownvoteText() {
        downvoteCounter.setText(String.format(Locale.getDefault(), "%d", downvoteCount));
    }
}
