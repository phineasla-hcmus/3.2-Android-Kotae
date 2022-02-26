package com.ogif.kotae.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.ogif.kotae.ui.main.ProfileFragment;

public class DarkModePreferenceManager {
    public static final String PREF_NAME = "settings";
    public static final String IS_DARK_MODE = "is_dark_mode";

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public DarkModePreferenceManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setDarkMode(boolean enable) {
        editor.putBoolean(IS_DARK_MODE, enable);
        editor.commit();
    }

    public boolean isDarkMode() {
        return preferences.getBoolean(IS_DARK_MODE, false);
    }
}
