package com.ogif.kotae.ui.questiondetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ogif.kotae.data.model.Question;

import java.util.Objects;

public class QuestionDetailViewModelFactory implements ViewModelProvider.Factory {
    private Question question;

    public QuestionDetailViewModelFactory(Question question) {
        this.question = question;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new QuestionDetailViewModel(question)));
    }
}
