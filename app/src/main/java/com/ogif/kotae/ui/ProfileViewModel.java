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

public class ProfileViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<Boolean> updatedLiveData;

    public ProfileViewModel() {
        authRepository = new AuthRepository();
        deviceRepository = new DeviceRepository();
        userRepository = new UserRepository();
        userLiveData = new MutableLiveData<>();
        updatedLiveData = new MutableLiveData<>();
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

    public void updateUser(String username, String job, int age) {
        userRepository.updateUser(username, job, age, new TaskListener.State<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                updatedLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                updatedLiveData.postValue(false);
            }
        });
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getUpdatedLiveData() {
        return updatedLiveData;
    }
}
