package com.ogif.kotae.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.ogif.kotae.R;
import com.ogif.kotae.data.DarkModePreferenceManager;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        SwitchCompat darkMode = v.findViewById(R.id.switch_profile_dark_mode);

        darkMode.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            // prefs.getBoolean()
            DarkModePreferenceManager manager = new DarkModePreferenceManager(this.requireContext());
            manager.setDarkMode(!manager.isDarkMode());
            compoundButton.setChecked(manager.isDarkMode());
            AppCompatDelegate.setDefaultNightMode(manager.isDarkMode()
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO
            );
            requireActivity().recreate();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_profile_language) {

        } else if (id == R.id.tv_profile_change_password) {

        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        // PreferenceManager.getDefaultSharedPreferences(getContext());
        DarkModePreferenceManager manager = new DarkModePreferenceManager(this.requireContext());
        manager.setDarkMode(!manager.isDarkMode());
        compoundButton.setChecked(manager.isDarkMode());
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
        );
        requireActivity().recreate();
    }
}