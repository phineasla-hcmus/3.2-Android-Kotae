package com.ogif.kotae.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.data.repository.AuthRepository;
import com.ogif.kotae.data.repository.VoteCounterRepository;
import com.ogif.kotae.data.repository.VoteRepository;

import java.util.Map;
import java.util.Objects;

public class VoteViewModel extends ViewModel {
    // Create new vote
    private final VoteRepository voteRepository;
    // Increment/decrement counter for both answer and question
    private final VoteCounterRepository voteCounterRepository;
    private final MutableLiveData<Map<String, Vote>> mutableLiveData;


    public VoteViewModel() {
        AuthRepository userRepository = new AuthRepository();
        String userId = Objects.requireNonNull(userRepository.getCurrentUser()).getUid();

        voteRepository = new VoteRepository(userId);
        voteCounterRepository = new VoteCounterRepository();
        mutableLiveData = new MutableLiveData<>();

    }
}
