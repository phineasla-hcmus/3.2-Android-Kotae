package com.ogif.kotae.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ogif.kotae.R;

import java.util.Locale;

public class VoteView extends ConstraintLayout {
    public static final int UPVOTE = 1;
    public static final int NONE = 0;
    public static final int DOWNVOTE = -1;

    protected String id;
    protected int upvoteCount;
    protected int downvoteCount;
    protected int currentState;
    protected CompoundButton upvote;
    protected CompoundButton downvote;
    protected OnStateChangeListener listener;

    interface OnStateChangeListener {
        void onUpvote(boolean isActive);

        void onDownvote(boolean isActive);
    }

    public VoteView(@NonNull Context context) {
        super(context);
        init();
    }

    public VoteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VoteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VoteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void onUpvoteChanged(CompoundButton btn, boolean isChecked) {
        if (isChecked)
            updateState(UPVOTE);
        else
            updateState(NONE);
    }

    private void onDownvoteChanged(CompoundButton btn, boolean isChecked) {
        if (isChecked)
            updateState(UPVOTE);
        else
            updateState(NONE);
    }

    protected void inflate() {
        inflate(getContext(), R.layout.partial_vote_view, this);
    }

    protected void init() {
        inflate();
        this.currentState = NONE;
        this.upvote = findViewById(R.id.chk_vote_view_upvote);
        this.downvote = findViewById(R.id.chk_vote_view_downvote);
        upvote.setOnCheckedChangeListener(this::onUpvoteChanged);
        downvote.setOnCheckedChangeListener(this::onDownvoteChanged);
    }

    public void updateState(int state) {
        if (state == NONE && currentState != NONE) {
            if (currentState == UPVOTE) {
                setUpvoteValue(getUpvoteValue() - 1);
                if (listener != null)
                    listener.onUpvote(false);
            } else if (currentState == DOWNVOTE) {
                setDownvoteValue(getDownvoteValue() - 1);
                if (listener != null)
                    listener.onDownvote(false);
            }
        } else if (state == UPVOTE && currentState != UPVOTE) {
            setUpvoteValue(getUpvoteValue() + 1);
            if (listener != null)
                listener.onUpvote(true);
            if (currentState == DOWNVOTE) {
                setDownvoteValue(getDownvoteValue() - 1);
                if (listener != null)
                    listener.onDownvote(false);
            }
        } else if (state == DOWNVOTE && currentState != DOWNVOTE) {
            setDownvoteValue(getDownvoteValue() + 1);
            if (listener != null)
                listener.onDownvote(true);
            if (currentState == UPVOTE) {
                setUpvoteValue(getUpvoteValue() - 1);
                if (listener != null)
                    listener.onUpvote(false);
            }
        }
        setState(state);
    }

    protected void setUpvoteText() {
        upvote.setText(String.format(Locale.getDefault(), "%d", upvoteCount));
    }

    protected void setDownvoteText() {
        downvote.setText(String.format(Locale.getDefault(), "%d", downvoteCount));
    }

    public void setOnStateChangeListener(@Nullable OnStateChangeListener listener) {
        this.listener = listener;
    }

    public OnStateChangeListener getOnStateChangeListener() {
        return listener;
    }

    public void setState(int state) {
        upvote.setOnCheckedChangeListener(null);
        downvote.setOnCheckedChangeListener(null);
        switch (state) {
            case NONE:
                upvote.setChecked(false);
                downvote.setChecked(false);
                break;
            case UPVOTE:
                upvote.setChecked(true);
                downvote.setChecked(false);
                break;
            case DOWNVOTE:
                upvote.setChecked(false);
                downvote.setChecked(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
        upvote.setOnCheckedChangeListener(this::onUpvoteChanged);
        downvote.setOnCheckedChangeListener(this::onDownvoteChanged);
        currentState = state;
    }

    public int getState() {
        return currentState;
    }

    public void setUpvoteValue(int value) {
        upvoteCount = value;
        setUpvoteText();
    }

    public int getUpvoteValue() {
        return upvoteCount;
    }

    public void setDownvoteValue(int value) {
        downvoteCount = value;
        setDownvoteText();
    }

    public int getDownvoteValue() {
        return downvoteCount;
    }

    public void setHolderId(String id) {
        this.id = id;
    }

    public String getHolderId() {
        return this.id;
    }
}
