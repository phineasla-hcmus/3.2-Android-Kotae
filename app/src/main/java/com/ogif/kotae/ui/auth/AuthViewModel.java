package com.ogif.kotae.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.AuthRepository;

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

    public void createUser(@NonNull String email, @NonNull String password, @NonNull String username, @NonNull User extraInfo) {
        authRepository.createUser(email, password, username, extraInfo);
    }

    MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

}
