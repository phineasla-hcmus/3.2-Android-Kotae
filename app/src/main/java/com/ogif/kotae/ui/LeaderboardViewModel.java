package com.ogif.kotae.ui;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.UserRepository;

import java.util.List;

public class LeaderboardViewModel extends ViewModel {
    private static final String TAG = "LeaderboardViewModel";
    private final UserRepository userRepository;
    private final MutableLiveData<List<User>> userLiveData;

    public LeaderboardViewModel() {
        this.userRepository = new UserRepository();
        this.userLiveData = new MutableLiveData<List<User>>();
    }


    public void getLeaderboard(String category) {
        userRepository.getLeaderboard(category, Global.QUERY_LIMIT, new TaskListener.State<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                userLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed to getLeaderboard()");
                userLiveData.postValue(null);
            }
        });
    }

    public LiveData<List<User>> getUserLiveData() {
        return userLiveData;
    }
}
