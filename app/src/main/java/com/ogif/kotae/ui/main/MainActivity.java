package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ogif.kotae.Global;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityMainBinding;
import com.ogif.kotae.ui.auth.AuthViewModel;
import com.ogif.kotae.ui.auth.LoginActivity;
import com.ogif.kotae.ui.leaderboard.LeaderboardActivity;
import com.ogif.kotae.ui.search.SearchActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private AuthViewModel viewModel;
    final int REQUEST_CODE_FILTER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        boolean isNightModeEnabled = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Global.SETTING_KEY_NIGHT_MODE, false);

        AppCompatDelegate.setDefaultNightMode(isNightModeEnabled
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        this.viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        if (!this.viewModel.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // Get toolbar
        toolbar = (Toolbar) binding.includedToolBar.toolbar;
        this.setSupportActionBar(toolbar);

        binding.includedToolBar.btnSearch.setOnClickListener(v -> startSearchActivity());

        binding.includedToolBar.btnFilter.setOnClickListener(v -> startFilterActivity());

        binding.includedToolBar.btnLeaderBoard.setOnClickListener(v -> startLeaderboardActivity());

        // Initialize home screen
        loadFragment(new HomeFragment());

        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        BottomNavigationView bottomNav = (BottomNavigationView) binding.includedBottomNav.bottomNav;
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.page_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    break;
                case R.id.page_bookmark:
                    fragment = new BookmarkFragment();
                    loadFragment(fragment);
                    break;
                case R.id.page_noti:
                    fragment = new NotiFragment();
                    loadFragment(fragment);
                    break;
                case R.id.page_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    break;
            }
            return true;
        });
    }

    private void startLeaderboardActivity() {
        Intent intent = new Intent(getApplicationContext(), LeaderboardActivity.class);
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    private void startFilterActivity(){
        Intent intent = new Intent(getApplicationContext(), FilterQuestionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FILTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_FILTER && resultCode == RESULT_OK && data != null){
            // Get questions after filter & render
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFragment(Fragment fragment) {
        // Load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}