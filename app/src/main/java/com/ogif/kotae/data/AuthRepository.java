package com.ogif.kotae.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.Global;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userMutableLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
        }
    }

    public void login(@NonNull String email, @NonNull String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // boolean isNewUser = Objects.requireNonNull(task.getResult())
                //         .getAdditionalUserInfo().isNewUser();
                if (user != null)
                    userMutableLiveData.postValue(user);
            } else {
                Log.w(Global.APP_NAME, task.getException().getMessage());
            }
        });
    }

    public void createUser(@NonNull String email, @NonNull String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    userMutableLiveData.postValue(user);
            } else {
                Log.w(Global.APP_NAME, task.getException().getMessage());
            }
        });
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
