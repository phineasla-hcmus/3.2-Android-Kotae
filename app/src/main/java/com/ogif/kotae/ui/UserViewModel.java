package com.ogif.kotae.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private final UserRepository authRepository;
    private final MutableLiveData<StateWrapper<FirebaseUser>> mutableLiveData;

    public UserViewModel() {
        this.authRepository = new UserRepository();
        this.mutableLiveData = authRepository.getMutableLiveData();
    }

    public void login(@NonNull String email, @NonNull String password) {
        authRepository.login(email, password);
    }

    public void createUser(@NonNull String email, @NonNull String password, @NonNull User extraInfo) {
        authRepository.createUser(email, password, extraInfo);
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    public boolean isEmailVerified() {
        return authRepository.isEmailVerified();
    }

    public LiveData<StateWrapper<FirebaseUser>> getLiveData() {
        return mutableLiveData;
    }
}
