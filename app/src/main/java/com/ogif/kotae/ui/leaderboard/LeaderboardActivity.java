package com.ogif.kotae.ui.leaderboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ogif.kotae.databinding.ActivityCreateQuestionBinding;
import com.ogif.kotae.databinding.ActivityLeaderboardBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaderboardActivity extends AppCompatActivity {
    private ActivityLeaderboardBinding binding;
    private View view;
    private List<LeaderboardItem> items = new ArrayList<>();
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        this.setSupportActionBar(binding.tbLeaderboard);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Leaderboard");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}