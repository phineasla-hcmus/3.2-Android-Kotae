package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.ActivityMainBinding;
import com.ogif.kotae.ui.ProfileViewModel;
import com.ogif.kotae.ui.auth.AuthViewModel;
import com.ogif.kotae.ui.auth.LoginActivity;
import com.ogif.kotae.ui.leaderboard.LeaderboardActivity;
import com.ogif.kotae.ui.search.SearchActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private AuthViewModel authViewModel;
    private ProfileViewModel profileViewModel;
    final int REQUEST_CODE_FILTER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        boolean isNightModeEnabled = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Global.SHARED_PREF_NIGHT_MODE, false);
        AppCompatDelegate.setDefaultNightMode(isNightModeEnabled
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        this.authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        this.profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        if (!this.authViewModel.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Get toolbar
        toolbar = (Toolbar) binding.includedToolBar.toolbar;
        MainActivity.this.setSupportActionBar(toolbar);
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

    private void startFilterActivity() {
        Intent intent = new Intent(getApplicationContext(), FilterQuestionActivity.class);
        intent.putExtra("FROM_HOME", true);
        startActivityForResult(intent, REQUEST_CODE_FILTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_FILTER && resultCode == RESULT_OK && data != null) {
            String sort = data.getStringExtra("sort");
            String status = data.getStringExtra("status");
            ArrayList<String> lstGrades = data.getStringArrayListExtra("lstGrades");
            ArrayList<String> lstCourses = data.getStringArrayListExtra("lstCourses");

            Bundle bundle = new Bundle();
            bundle.putString("sort", sort);
            bundle.putString("status", status);
            bundle.putStringArrayList("lstGrades", lstGrades);
            bundle.putStringArrayList("lstCourses", lstCourses);

            Fragment fragment = new HomeFragment();
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
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

    @Override
    public void onBackPressed() {
        this.finish();
    }

    // For Testing
    private void printQuestions(ArrayList<Question> questions) {
        for (Question question : questions) {
            Log.d("AAA", question.getTitle());
        }
    }
}