package com.ogif.kotae.ui.question;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;

public class QuestionDetailViewModel extends ViewModel {
    private final QuestionRepository questionRepository;
    private final MutableLiveData<Question> questionLiveData;
    private final MutableLiveData<Answer[]> answerMutableLiveData;
    private final MutableLiveData<Boolean> failLiveData;

    public QuestionDetailViewModel() {
        this.questionRepository = new QuestionRepository();
        this.questionLiveData = new MutableLiveData<>();
        this.answerMutableLiveData = new MutableLiveData<>();
        this.failLiveData = new MutableLiveData<>();
    }

    public QuestionDetailViewModel(Question question) {
        this();
    }

    public LiveData<Question> getQuestionLiveData() {
        return questionLiveData;
    }

    public LiveData<Answer[]> getAnswerLiveData() {
        return answerMutableLiveData;
    }

    public void getQuestion(String id) {
        questionRepository.get(id, new TaskListener.State<Question>() {
            @Override
            public void onSuccess(@Nullable Question result) {
                questionLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO add another LiveData to notify View on fail
            }

        });
    }
}
