package com.ogif.kotae.ui.questiondetail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.ogif.kotae.databinding.PartialPostActionBinding;

import java.util.Locale;

public class PostActionView extends ConstraintLayout {
    public static final int UPVOTE = 1;
    public static final int DOWNVOTE = -1;

    PartialPostActionBinding binding;
    private MaterialButton upvote;
    private MaterialButton downvote;
    private MaterialButton comment;

    interface PostActionListener {
        public void onUpvote(int newValue, boolean isActive);

        public void onDownvote(int newValue, boolean isActive);

        public void onVoteChanged(int newUpvoteValue, int newDownvoteValue, int newActiveState);
    }

    public PostActionView(@NonNull Context context) {
        super(context);
        init();
    }

    public PostActionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PostActionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PostActionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // inflate(getContext(), R.layout.partial_post_action, this);
        binding = PartialPostActionBinding.inflate(LayoutInflater.from(getContext()), this);
        upvote = binding.btnPostActionUpvote;
        downvote = binding.btnPostActionDownvote;
        comment = binding.btnPostActionComment;

        upvote.setOnClickListener(view -> {
            // upvote.setIconTint(ContextCompat.getColor());
        });
    }

    public void setUpvote(int value) {
        upvote.setText(String.format(Locale.getDefault(), "%d", value));
    }

    public void setDownvote(int value) {
        downvote.setText(String.format(Locale.getDefault(), "%d", value));
    }

    public void setVoteState(int state) {
        switch (state) {
            case UPVOTE:
                upvote.setPressed(true);
                downvote.setPressed(false);
                break;
            case DOWNVOTE:
                upvote.setPressed(false);
                downvote.setPressed(true);
        }
    }
}
