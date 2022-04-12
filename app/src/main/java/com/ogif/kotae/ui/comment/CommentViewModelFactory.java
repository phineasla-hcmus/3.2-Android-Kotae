package com.ogif.kotae.ui.comment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.questiondetail.QuestionDetailViewModel;

import java.util.Objects;

public class CommentViewModelFactory implements ViewModelProvider.Factory {
    private String userId;
    private String username;
    private String postId;

    public CommentViewModelFactory(String userId, String username, String postId) {
        this.userId = userId;
        this.username = username;
        this.postId = postId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new CommentViewModel(userId, username, postId)));
    }
}

