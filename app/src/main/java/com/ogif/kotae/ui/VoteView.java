package com.ogif.kotae.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Record;
import com.ogif.kotae.data.model.Vote;

import java.util.Locale;

public class VoteView extends ConstraintLayout {
    @IdRes
    protected int RES_UPVOTE;
    @IdRes
    protected int RES_DOWNVOTE;
    @IdRes
    protected int RES_TOGGLE_GROUP;
    @Vote.State
    protected int currentState;
    protected int upvoteCount;
    protected int downvoteCount;
    protected Record holder;
    protected MaterialButtonToggleGroup toggleGroup;
    protected Button upvote;
    protected Button downvote;
    protected OnStateChangeListener listener;

    public interface OnStateChangeListener {
        void onUpvote(VoteView view, boolean isActive);

        void onDownvote(VoteView view, boolean isActive);
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

    /**
     * On state change from {@link Vote#UPVOTE} to {@link Vote#DOWNVOTE} or vice versa,
     * this callback will be invoked twice.
     * Example, when switch from {@link Vote#DOWNVOTE} to {@link Vote#UPVOTE}, the following code
     * will be executed:
     * <ol>
     *     <li><code>onVoteStateChanged(group, RES_DOWNVOTE, false)</code></li>
     *     <li><code>onVoteStateChanged(group, RES_UPVOTE, true)</code></li>
     * </ol>
     *
     * @see MaterialButtonToggleGroup.OnButtonCheckedListener
     */
    protected void onVoteStateChanged(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (checkedId == RES_UPVOTE) {
            if (isChecked) {
                setUpvoteValue(getUpvoteValue() + 1);
                if (listener != null)
                    listener.onUpvote(this, true);
                this.currentState = Vote.UPVOTE;
            } else {
                setUpvoteValue(getUpvoteValue() - 1);
                if (listener != null)
                    listener.onUpvote(this, false);
                this.currentState = Vote.NONE;
            }
        } else if (checkedId == RES_DOWNVOTE) {
            if (isChecked) {
                setDownvoteValue(getDownvoteValue() + 1);
                if (listener != null)
                    listener.onDownvote(this, true);
                this.currentState = Vote.DOWNVOTE;
            } else {
                setDownvoteValue(getDownvoteValue() - 1);
                if (listener != null)
                    listener.onDownvote(this, false);
                this.currentState = Vote.NONE;
            }
        }
    }

    protected void inflate() {
        inflate(getContext(), R.layout.partial_vote_view, this);
        RES_UPVOTE = R.id.btn_vote_view_upvote;
        RES_DOWNVOTE = R.id.btn_vote_view_downvote;
        RES_TOGGLE_GROUP = R.id.toggle_group_vote_view;
    }

    protected void init() {
        inflate();
        this.currentState = Vote.NONE;
        this.toggleGroup = findViewById(RES_TOGGLE_GROUP);
        this.upvote = findViewById(RES_UPVOTE);
        this.downvote = findViewById(RES_DOWNVOTE);
        toggleGroup.addOnButtonCheckedListener(this::onVoteStateChanged);
    }

    protected void setUpvoteText() {
        upvote.setText(String.format(Locale.getDefault(), "%d", upvoteCount));
    }

    protected void setDownvoteText() {
        downvote.setText(String.format(Locale.getDefault(), "%d", downvoteCount));
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

    public void setHolder(Record holder) {
        this.holder = holder;
    }

    @Nullable
    public Record getHolder() {
        return holder;
    }
}
