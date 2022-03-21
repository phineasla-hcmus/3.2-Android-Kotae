package com.ogif.kotae.ui.question;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.AnswerRepository;
import com.ogif.kotae.data.repository.QuestionRepository;

import java.util.List;
import java.util.Objects;

public class QuestionDetailViewModel extends ViewModel {
    private static final String TAG = "QuestionDetailViewModel";
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MutableLiveData<Question> questionLiveData;
    private final MutableLiveData<List<Answer>> answerLiveData;

    public QuestionDetailViewModel() {
        this.questionRepository = new QuestionRepository();
        this.answerRepository = new AnswerRepository();
        this.questionLiveData = new MutableLiveData<>();
        this.answerLiveData = new MutableLiveData<>();
    }

    public QuestionDetailViewModel(Question question) {
        this();
        this.questionLiveData.setValue(question);
    }

    public void getQuestion(String id) {
        questionRepository.get(id, new TaskListener.State<Question>() {
            @Override
            public void onSuccess(@Nullable Question result) {
                questionLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                questionLiveData.postValue(null);
            }
        });
    }

    public void getAnswers() {
        String questionId = Objects.requireNonNull(questionLiveData.getValue()).getId();
        answerRepository.getListByQuestion(questionId, Global.QUERY_LIMIT, new TaskListener.State<List<Answer>>() {
            @Override
            public void onSuccess(List<Answer> result) {
                answerLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed to getAnswer()");
                answerLiveData.postValue(null);
            }
        });
    }

    public LiveData<Question> getQuestionLiveData() {
        return questionLiveData;
    }

    public LiveData<List<Answer>> getAnswerLiveData() {
        return answerLiveData;
    }
}
