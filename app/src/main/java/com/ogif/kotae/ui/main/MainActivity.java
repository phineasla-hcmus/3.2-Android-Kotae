package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.ogif.kotae.ui.UserViewModel;
import com.ogif.kotae.ui.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private UserViewModel viewModel;

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

        this.viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (!this.viewModel.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // Get toolbar
        toolbar = (Toolbar) binding.includedToolBar.toolbar;
        this.setSupportActionBar(toolbar);

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

    private void loadFragment(Fragment fragment) {
        // Load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}