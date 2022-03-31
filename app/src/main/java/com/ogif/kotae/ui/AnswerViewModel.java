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
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.AnswerRepository;
import com.ogif.kotae.data.repository.UserRepository;

public class AnswerViewModel extends ViewModel {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<StateWrapper<Answer>> mutableLiveData;

    public AnswerViewModel() {
        this.answerRepository = new AnswerRepository();
        this.userRepository = new UserRepository();
        this.mutableLiveData = new MutableLiveData<>();
    }

    public void createAnswer(@NonNull String questionId, @NonNull String content) {
        userRepository.getCurrentUser(new TaskListener.State<User>() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

            @Override
            public void onSuccess(User result) {
                String authorId = result.getId();
                String authorName = result.getUsername();
                Answer answer = new Answer.Builder().question(questionId)
                        .author(authorId, authorName).content(content)
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
        });

    }

    public LiveData<StateWrapper<Answer>> getLiveData() {
        return mutableLiveData;
    }
}