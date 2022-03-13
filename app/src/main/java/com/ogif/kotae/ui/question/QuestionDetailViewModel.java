package com.ogif.kotae.ui.question;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;

import java.util.List;

public class QuestionDetailViewModel extends ViewModel {
    private final QuestionRepository questionRepository;
    private final MutableLiveData<Question> question;

    public QuestionDetailViewModel() {
        this.questionRepository = new QuestionRepository();

    }
}
