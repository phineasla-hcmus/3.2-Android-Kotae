package com.ogif.kotae.ui.auth;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.AuthRepository;
import com.ogif.kotae.data.repository.DeviceRepository;
import com.ogif.kotae.utils.model.UserUtils;

public class AuthViewModel extends ViewModel {
    private static final String TAG = "AuthViewModel";
    private final AuthRepository authRepository;
    private final DeviceRepository deviceRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthViewModel() {
        this.authRepository = new AuthRepository();
        this.deviceRepository = new DeviceRepository();
        this.userMutableLiveData = new MutableLiveData<>();
    }

    public void login(@NonNull String email, @NonNull String password) {
        authRepository.login(email, password, new TaskListener.State<FirebaseUser>() {
            @Override
            public void onSuccess(FirebaseUser result) {
                userMutableLiveData.postValue(result);
                deviceRepository.addDevice();
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                userMutableLiveData.postValue(null);
            }
        });
    }

    public void createUser(@NonNull String email, @NonNull String password, @NonNull User extraInfo) {
        authRepository.createUser(email, password, extraInfo, new TaskListener.State<FirebaseUser>() {
            @Override
            public void onSuccess(FirebaseUser result) {
                userMutableLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                userMutableLiveData.postValue(null);
            }
        });
    }

    public void cacheCurrentUser(Context context) {
        authRepository.getCurrentUser(new TaskListener.State<User>() {
            @Override
            public void onSuccess(User result) {
                UserUtils.cache(result.getId(), result.getUsername(), context);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, e);
            }
        });
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    public boolean isEmailVerified() {
        return authRepository.isEmailVerified();
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userMutableLiveData;
    }
}
