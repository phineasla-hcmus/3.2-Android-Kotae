package com.ogif.kotae.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.UserRepository;

public class AuthViewModel extends ViewModel {
    private final UserRepository authRepository;
    private final MutableLiveData<StateWrapper<FirebaseUser>> mutableLiveData;

    public AuthViewModel() {
        this.authRepository = new UserRepository();
        this.mutableLiveData = authRepository.getMutableLiveData();
    }

    public void login(@NonNull String email, @NonNull String password) {
        authRepository.login(email, password);
    }

    public void createUser(@NonNull String email, @NonNull String password, @NonNull User extraInfo) {
        authRepository.createUser(email, password, extraInfo);
    }

    MutableLiveData<StateWrapper<FirebaseUser>> getMutableLiveData() {
        return mutableLiveData;
    }
}
