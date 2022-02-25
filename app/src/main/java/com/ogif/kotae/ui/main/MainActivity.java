package com.ogif.kotae.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

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