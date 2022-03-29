package com.ogif.kotae.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Vote;

import java.util.Locale;

public class VoteView extends ConstraintLayout {

    protected String id;
    protected int upvoteCount;
    protected int downvoteCount;
    protected int currentState;
    protected MaterialButtonToggleGroup toggleGroup;
    protected Button upvote;
    protected Button downvote;
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

    private void onVoteStateChanged(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (checkedId == R.id.btn_vote_view_upvote) {
            if (isChecked) {
                setUpvoteValue(getUpvoteValue() + 1);
                if (listener != null)
                    listener.onUpvote(true);
                this.currentState = Vote.UPVOTE;
            } else {
                setUpvoteValue(getUpvoteValue() - 1);
                if (listener != null)
                    listener.onUpvote(false);
                this.currentState = Vote.NONE;
            }
        } else if (checkedId == R.id.btn_vote_view_downvote) {
            if (isChecked) {
                setDownvoteValue(getDownvoteValue() + 1);
                if (listener != null)
                    listener.onDownvote(true);
                this.currentState = Vote.DOWNVOTE;
            } else {
                setDownvoteValue(getDownvoteValue() - 1);
                if (listener != null)
                    listener.onDownvote(false);
                this.currentState = Vote.NONE;
            }
        }
    }

    protected void inflate() {
        inflate(getContext(), R.layout.partial_vote_view, this);
    }

    protected void init() {
        inflate();
        this.currentState = Vote.NONE;
        this.toggleGroup = findViewById(R.id.toggle_group_vote_view);
        this.upvote = findViewById(R.id.btn_vote_view_upvote);
        this.downvote = findViewById(R.id.btn_vote_view_downvote);

        toggleGroup.addOnButtonCheckedListener(this::onVoteStateChanged);
    }

    // public void setVoteState(int state) {
    //     if (state == NONE && currentState != NONE) {
    //         if (currentState == Vote.UPVOTE) {
    //             setUpvoteValue(getUpvoteValue() - 1);
    //             setUpvoteActive(false);
    //         } else if (currentState == DOWNVOTE) {
    //             setDownvoteValue(getDownvoteValue() - 1);
    //             setDownvoteActive(false);
    //         }
    //     } else if (state == Vote.UPVOTE && currentState != Vote.UPVOTE) {
    //         setUpvoteValue(getUpvoteValue() + 1);
    //         setUpvoteActive(true);
    //         if (currentState == DOWNVOTE) {
    //             setDownvoteValue(getDownvoteValue() - 1);
    //             setDownvoteActive(false);
    //         }
    //     } else if (state == DOWNVOTE && currentState != DOWNVOTE) {
    //         setDownvoteValue(getDownvoteValue() + 1);
    //         setDownvoteActive(true);
    //         if (currentState == Vote.UPVOTE) {
    //             setUpvoteValue(getUpvoteValue() - 1);
    //             setUpvoteActive(false);
    //         }
    //     }
    //     this.currentState = state;
    // }

    /**
     * Set state without calling {@link OnStateChangeListener}
     *
     * @param upvote   current upvote count
     * @param downvote current downvote count
     * @param state    {@link Vote#UPVOTE}, {@link Vote#DOWNVOTE} or {@link Vote#NONE}
     */
    public void setVoteState(int upvote, int downvote, @Vote.State int state) {
        // https://stackoverflow.com/questions/15523157/change-checkbox-value-without-triggering-oncheckchanged
        toggleGroup.clearOnButtonCheckedListeners();
        setUpvoteValue(upvote);
        setDownvoteValue(downvote);
        if (state == Vote.UPVOTE)
            toggleGroup.check(R.id.btn_vote_view_upvote);
        else if (state == Vote.DOWNVOTE)
            toggleGroup.check(R.id.btn_vote_view_downvote);
        toggleGroup.addOnButtonCheckedListener(this::onVoteStateChanged);
        this.currentState = state;
    }

    public int getVoteState() {
        return currentState;
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
