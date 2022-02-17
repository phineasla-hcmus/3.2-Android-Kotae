package com.ogif.kotae.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.data.AuthRepository;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthViewModel() {
        this.authRepository = new AuthRepository();
        this.userMutableLiveData = authRepository.getUserMutableLiveData();
    }

    public void login(@NonNull String email, @NonNull String password) {
        authRepository.login(email, password);
    }

    // TODO: support job and age
    public void createUser(@NonNull String email, @NonNull String password) {
        authRepository.createUser(email, password);
    }

    MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

}
