package com.ogif.kotae.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.AuthRepository;
import com.ogif.kotae.data.repository.DeviceRepository;
import com.ogif.kotae.data.repository.UserRepository;
import com.ogif.kotae.utils.model.UserUtils;

public class ProfileViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final DeviceRepository deviceRepository;
    private final MutableLiveData<User> userLiveData;

    public ProfileViewModel() {
        authRepository = new AuthRepository();
        deviceRepository = new DeviceRepository();
        userLiveData = new MutableLiveData<>();
    }

    public void getCurrentUser() {
        authRepository.getCurrentUser(new TaskListener.State<User>() {
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

    public void logout() {
        deviceRepository.removeDevice();
        authRepository.logout();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
