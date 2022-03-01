package com.ogif.kotae.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.ogif.kotae.Global;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.FragmentProfileBinding;
import com.ogif.kotae.utils.LocaleHelper;

import java.util.Objects;

/**
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String[] languages = {"English", "Tiếng Việt"};
    private FragmentProfileBinding binding;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        prefsEditor = prefs.edit();

        SwitchMaterial switchNightMode = binding.switchProfileNightMode;
        switchNightMode.setChecked(prefs.getBoolean(Global.SETTING_KEY_NIGHT_MODE, false));
        switchNightMode.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            prefsEditor.putBoolean(Global.SETTING_KEY_NIGHT_MODE, isChecked);
            prefsEditor.commit();
            AppCompatDelegate.setDefaultNightMode(isChecked
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO);
            // prefs.getBoolean()
            // DarkModePreferenceManager manager = new DarkModePreferenceManager(this.requireContext());
            // manager.setDarkMode(!manager.isDarkMode());
            // compoundButton.setChecked(manager.isDarkMode());
            // AppCompatDelegate.setDefaultNightMode(manager.isDarkMode()
            //         ? AppCompatDelegate.MODE_NIGHT_YES
            //         : AppCompatDelegate.MODE_NIGHT_NO
            // );
            requireActivity().recreate();
        });

        binding.tvProfileLanguage.setOnClickListener(view -> {
            showLanguageDialog();
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

    private void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getResources().getString(R.string.all_language))
                .setSingleChoiceItems(languages, -1, (dialogInterface, i) -> {
                    if (i == 0) {
                        LocaleHelper.setLocale(requireContext(), "en");
                    } else if (i == 1) {
                        Log.d("TESTING", "Set VN");
                        LocaleHelper.setLocale(requireContext(), "vi");
                    }
                    requireActivity().recreate();
                    dialogInterface.dismiss();
                });
        builder.create().show();
    }
}