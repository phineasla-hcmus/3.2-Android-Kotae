package com.ogif.kotae.ui.leaderboard;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ogif.kotae.databinding.ActivityLeaderboardBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaderboardActivity extends AppCompatActivity {
    private List<LeaderboardItem> items = new ArrayList<>();
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.ogif.kotae.databinding.ActivityLeaderboardBinding binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.setSupportActionBar(binding.tbLeaderboard);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Leaderboard");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}