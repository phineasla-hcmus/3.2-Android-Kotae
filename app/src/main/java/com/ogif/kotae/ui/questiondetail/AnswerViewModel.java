package com.ogif.kotae.ui.questiondetail;

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
import com.ogif.kotae.data.repository.AnswerCounterRepository;
import com.ogif.kotae.data.repository.AnswerRepository;
import com.ogif.kotae.data.repository.UserRepository;

import java.util.List;

public class AnswerViewModel extends ViewModel {
    public static final String TAG = "AnswerViewModel";
    private final AnswerRepository answerRepository;
    private final AnswerCounterRepository answerCounterRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<StateWrapper<Answer>> mutableLiveData;

    public AnswerViewModel() {
        this.answerCounterRepository = new AnswerCounterRepository();
        this.answerRepository = new AnswerRepository();
        this.userRepository = new UserRepository();
        this.mutableLiveData = new MutableLiveData<>();
    }

    public void createAnswer(@NonNull String questionId, @NonNull String content, List<String> imgIds) {
        userRepository.getCurrentUser(new TaskListener.State<User>() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

            @Override
            public void onSuccess(User result) {
                String authorId = result.getId();
                String authorName = result.getUsername();
                Answer answer = new Answer.Builder().question(questionId)
                        .imageIds(imgIds)
                        .author(authorId, authorName).content(content)
                        .build();
                answerCounterRepository.increment(questionId);
                answerRepository.createAnswer(answer).addOnSuccessListener(answerId -> {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + answerId);
                }).addOnFailureListener(e -> Log.w(TAG, "Error adding answer", e));
            }
        });

    }

    public LiveData<StateWrapper<Answer>> getLiveData() {
        return mutableLiveData;
    }
}