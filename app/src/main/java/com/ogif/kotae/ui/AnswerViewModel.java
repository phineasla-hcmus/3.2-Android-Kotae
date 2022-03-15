package com.ogif.kotae.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.AnswerRepository;
import com.ogif.kotae.data.repository.QuestionRepository;

public class AnswerViewModel extends ViewModel {
    private final AnswerRepository answerRepository;
    private final MutableLiveData<StateWrapper<Answer>> mutableLiveData;

    public AnswerViewModel() {
        this.answerRepository = new AnswerRepository();
        this.mutableLiveData = answerRepository.getMutableLiveData();
    }

    public void createAnswer(@NonNull String content) {
        answerRepository.createAnswer(content);
    }

    public LiveData<StateWrapper<Answer>> getLiveData() {
        return mutableLiveData;
    }
}