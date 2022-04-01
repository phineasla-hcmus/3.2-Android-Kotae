package com.ogif.kotae.ui.leaderboard;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.ogif.kotae.databinding.ActivityLeaderboardBinding;
import com.ogif.kotae.ui.LeaderboardViewModel;
import com.ogif.kotae.ui.leaderboard.adapter.LeaderboardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaderboardActivity extends AppCompatActivity {
    private List<LeaderboardItem> items = new ArrayList<>();
    private LeaderboardAdapter adapter;
    private ActivityLeaderboardBinding binding;
    private LeaderboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.viewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        this.setSupportActionBar(binding.tbLeaderboard);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Leaderboard");
        this.adapter = new LeaderboardAdapter((Context) this, this.items);

        initRecyclerView();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabChanged(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        refreshLayout("day");
    }

    private void onTabChanged(int position) {
        // get a reference to the tabs container view
        LinearLayout ll = (LinearLayout) binding.tabLayout.getChildAt(0);
        // get the child view at the position of the currently selected tab and set selected to false
        ll.getChildAt(binding.tabLayout.getSelectedTabPosition()).setSelected(false);
        // get the child view at the new selected position and set selected to true
        ll.getChildAt(position).setSelected(true);
        // move the selection indicator
        binding.tabLayout.setScrollPosition(position, 0, true);

        switch (position) {
            case 0: {
                refreshLayout("day");
                break;
            }
            case 1: {
                refreshLayout("all");
                break;
            }
            default:
                break;
        }
    }

    private void refreshLayout(String category) {
        this.adapter.clear();

        this.viewModel.getLeaderboard(category);
        this.viewModel.getUserLiveData().observe(this, users -> {
            this.items.clear();
            for (int i = 0; i < users.size(); i++) {
                this.items.add(new LeaderboardItem(users.get(i)));
            }
            this.adapter.setItems(this.items);
        });
    }

    private void initRecyclerView() {
        binding.rvLeaderboard.setLayoutManager((RecyclerView.LayoutManager) this.initLayoutManager());
        binding.rvLeaderboard.setAdapter(this.adapter);
    }

    private LinearLayoutManager initLayoutManager() {
        return new LinearLayoutManager((Context) this);
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