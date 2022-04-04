package com.ogif.kotae.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.UserRepository;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<User> userLiveData;

    public ProfileViewModel() {
        userRepository = new UserRepository();
        userLiveData = new MutableLiveData<>();
    }

    public void getCurrentUser() {
        userRepository.getCurrentUser(new TaskListener.State<User>() {
            @Override
            public void onSuccess(User result) {
                userLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                userLiveData.postValue(null);
            }
        });
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
