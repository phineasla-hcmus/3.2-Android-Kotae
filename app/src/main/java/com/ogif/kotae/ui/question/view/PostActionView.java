package com.ogif.kotae.ui.question.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.ogif.kotae.databinding.PartialPostActionBinding;

public class PostActionView extends ConstraintLayout {
    PartialPostActionBinding binding;
    private MaterialButton upvote;
    private MaterialButton downvote;
    private MaterialButton comment;

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

    }
}
