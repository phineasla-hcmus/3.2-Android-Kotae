package com.ogif.kotae.ui;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.AuthRepository;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.data.repository.UserRepository;

public class QuestionViewModel extends ViewModel {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<StateWrapper<Question>> mutableLiveData;

    public QuestionViewModel() {
        this.questionRepository = new QuestionRepository();
        this.userRepository = new UserRepository();
        this.mutableLiveData = questionRepository.getMutableLiveData();
    }

    public void createQuestion(@NonNull String title, @NonNull String content, @NonNull String subjectId, @NonNull String gradeId, @NonNull String subject, @NonNull String grade) {
        userRepository.getCurrentUser(new TaskListener.State<User>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Fail");
            }

            @Override
            public void onSuccess(User result) {
                String authorId = result.getId();
                String authorName = result.getUsername();
                questionRepository.createQuestion(authorId, authorName, title, content, subjectId, gradeId, subject, grade);
            }
        });
    }

    public LiveData<StateWrapper<Question>> getLiveData() {
        return mutableLiveData;
    }

    public void hideReport(ImageButton report, TextView counter) {
        userRepository.getCurrentUser(new TaskListener.State<User>() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

            @Override
            public void onSuccess(User result) {
                String role = result.getRole();
                if (role != null && role.equals("admin")) {
                    report.setVisibility(View.VISIBLE);
                    counter.setVisibility(View.VISIBLE);
                } else {
                    report.setVisibility(View.INVISIBLE);
                    counter.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}