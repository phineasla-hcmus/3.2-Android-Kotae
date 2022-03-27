package com.ogif.kotae.ui;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private static final String TAG = "SearchViewModel";
    private final QuestionRepository questionRepository;
    private final MutableLiveData<List<Question>> questionLiveData;

    public SearchViewModel() {
        this.questionRepository = new QuestionRepository();
        this.questionLiveData = new MutableLiveData<List<Question>>();
    }


    public void getQuestions(String keyword) {
        questionRepository.searchQuestionByKeyword(keyword, Global.QUERY_LIMIT, new TaskListener.State<List<Question>>() {
            @Override
            public void onSuccess(List<Question> result) {
                questionLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed to getQuestions()");
                questionLiveData.postValue(null);
            }
        });
    }

    public LiveData<List<Question>> getQuestionLiveData() {
        return questionLiveData;
    }
}
