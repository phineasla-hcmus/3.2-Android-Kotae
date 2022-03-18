package com.ogif.kotae.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;

public class QuestionViewModel extends ViewModel {
    private final QuestionRepository questionRepository;
    private final MutableLiveData<StateWrapper<Question>> mutableLiveData;

    public QuestionViewModel() {
        this.questionRepository = new QuestionRepository();
        this.mutableLiveData = questionRepository.getMutableLiveData();
    }

    public void createQuestion(@NonNull String title, @NonNull String content, @NonNull String subjectId, @NonNull String gradeId, @NonNull String subject, @NonNull String grade) {

        questionRepository.createQuestion(title, content, subjectId, gradeId, subject, grade);
    }

    public LiveData<StateWrapper<Question>> getLiveData() {
        return mutableLiveData;
    }
}