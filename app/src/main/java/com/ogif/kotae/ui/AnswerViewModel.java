package com.ogif.kotae.ui;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.AnswerRepository;
import com.ogif.kotae.data.repository.QuestionRepository;

public class AnswerViewModel extends ViewModel {
    private final AnswerRepository answerRepository;
    private final MutableLiveData<StateWrapper<Answer>> mutableLiveData;

    public AnswerViewModel() {
        this.answerRepository = new AnswerRepository();
        this.mutableLiveData = new MutableLiveData<>();
    }

    public void createAnswer(@NonNull String content) {
        Answer answer = new Answer.Builder().question("0FDZ97sbxRf17ac07Sx260inaPR2")
                .author("abc", "Tam Nguyen").content(content)
                .build();
        answerRepository.createAnswer(answer, new TaskListener.State<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference result) {
                Log.d("data", "DocumentSnapshot written with ID: " + result.getId());
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("data", "Error adding document", e);

            }
        });
    }

    public LiveData<StateWrapper<Answer>> getLiveData() {
        return mutableLiveData;
    }
}