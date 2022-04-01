package com.ogif.kotae.ui.questiondetail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ogif.kotae.databinding.PartialAuthorBinding;

import java.util.Locale;

public class AuthorView extends ConstraintLayout {
    PartialAuthorBinding binding;
    private ImageView avatar;
    private TextView username;
    private TextView reputation;

    public AuthorView(@NonNull Context context) {
        super(context);
        init();
    }

    public AuthorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AuthorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AuthorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // inflate(getContext(), R.layout.partial_author, this);
        binding = PartialAuthorBinding.inflate(LayoutInflater.from(getContext()), this);
        avatar = binding.tvAuthorAvatar;
        username = binding.tvAuthorUsername;
        reputation = binding.tvAuthorReputation;
    }

    public ImageView getAvatarView() {
        return avatar;
    }

    public void setName(String username) {
        this.username.setText(username);
    }

    public void setReputation(int reputation) {
        this.reputation.setText(String.format(Locale.getDefault(), "%d", reputation));
    }
}
