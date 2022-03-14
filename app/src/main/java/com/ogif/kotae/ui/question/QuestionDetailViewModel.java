package com.ogif.kotae.ui.question;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;

public class QuestionDetailViewModel extends ViewModel {
    private final QuestionRepository questionRepository;
    private final MutableLiveData<Question> questionLiveData;

    public QuestionDetailViewModel() {
        this.questionRepository = new QuestionRepository();
        this.questionLiveData = new MutableLiveData<>();
    }

    public QuestionDetailViewModel(Question question) {
        this.questionRepository = new QuestionRepository();
        this.questionLiveData = new MutableLiveData<>(question);
    }

    public LiveData<Question> getQuestionLiveData() {
        return questionLiveData;
    }

    public void getQuestion(String id) {
        questionRepository.get(id, new TaskListener<Question>() {
            @Override
            public void onSuccess(@Nullable Question result) {
                questionLiveData.postValue(result);
            }
        });
    }
}
