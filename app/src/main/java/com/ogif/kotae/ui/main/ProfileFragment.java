package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.ogif.kotae.Global;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.databinding.FragmentProfileBinding;
import com.ogif.kotae.ui.ProfileViewModel;
import com.ogif.kotae.ui.auth.LoginActivity;
import com.ogif.kotae.utils.LocaleHelper;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    @Deprecated
    private static final String[] languages = {"English", "Tiếng Việt"};
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        profileViewModel.getCurrentUser();
        profileViewModel.getUserLiveData().observe(requireActivity(), user -> {
            if (user == null)
                return;
            binding.tvProfileUsername.setText(user.getUsername());
            binding.tvProfileXp.setText(String.valueOf(user.getXp()));
            if (user.getRole().equals(User.ROLE_ADMIN))
                binding.tvProfileAdmin.setVisibility(View.VISIBLE);
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        prefsEditor = prefs.edit();

        SwitchMaterial switchNightMode = binding.switchProfileNightMode;
        switchNightMode.setChecked(prefs.getBoolean(Global.SHARED_PREF_NIGHT_MODE, false));
        switchNightMode.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            prefsEditor.putBoolean(Global.SHARED_PREF_NIGHT_MODE, isChecked);
            prefsEditor.commit();
            AppCompatDelegate.setDefaultNightMode(isChecked
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO);
            requireActivity().recreate();
        });

        // binding.tvProfileLanguage.setOnClickListener(view -> {
        //     showLanguageDialog();
        // });

        binding.tvProfileAdmin.setOnClickListener(view -> {
            // Launch admin activity
            Intent intent = new Intent(getActivity(), AdminActivity.class);
            startActivity(intent);
        });

        binding.tvProfileEdit.setOnClickListener(view -> {
            // Launch change profile info activity
        });

        binding.tvProfileChangePassword.setOnClickListener(view -> {
            // Launch change password activity
        });

        binding.tvProfileLogout.setOnClickListener(view -> {
            profileViewModel.logout();
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            requireActivity().finish();
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar())
                .hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar())
                .show();
    }

    @SuppressWarnings("unused")
    // Change language is no longer supported
    @Deprecated
    private void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getResources().getString(R.string.all_language))
                .setSingleChoiceItems(languages, -1, (dialogInterface, i) -> {
                    if (i == 0) {
                        LocaleHelper.setLocale(requireContext(), "en");
                    } else if (i == 1) {
                        LocaleHelper.setLocale(requireContext(), "vi");
                    }
                    requireActivity().recreate();
                    dialogInterface.dismiss();
                });
        builder.create().show();
    }
}