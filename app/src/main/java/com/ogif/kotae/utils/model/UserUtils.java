package com.ogif.kotae.utils.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.ogif.kotae.Global;

import java.time.Year;
import java.util.regex.Pattern;

public class UserUtils {
    public static final int OK = 0;
    public static final int INVALID_USERNAME_LENGTH = 1;
    public static final int INVALID_USERNAME_FIRST_CHAR = 2;
    public static final int INVALID_USERNAME_CHAR = 3;
    public static final int INVALID_PASSWORD_LENGTH = 4;

    public static final Pattern usernamePattern = Pattern.compile("[A-Za-z0-9_]+");

    public static int isPasswordValid(@NonNull CharSequence password) {
        return (password.length() < Global.PASSWORD_MIN || password.length() > Global.PASSWORD_MAX)
                ? INVALID_PASSWORD_LENGTH
                : OK;
    }

    public static int isUsernameValid(@NonNull CharSequence username) {
        if (username.length() < Global.USERNAME_MIN || username.length() > Global.USERNAME_MAX)
            return INVALID_USERNAME_LENGTH;
        if (Character.isDigit(username.charAt(0)))
            return INVALID_USERNAME_FIRST_CHAR;
        if (!usernamePattern.matcher(username).matches())
            return INVALID_USERNAME_CHAR;
        return OK;
    }

    public static int getYearOfBirth(int age) {
        return Year.now().getValue() - age;
    }

    public static void cacheUser(@NonNull String userId, @NonNull String username, @NonNull Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Global.SHARED_PREF_USER_ID, userId);
        prefsEditor.putString(Global.SHARED_PREF_USERNAME, username);
        prefsEditor.apply();
    }

    @Nullable
    public static String getCachedUsername(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Global.SHARED_PREF_USERNAME, null);
    }

    @Nullable
    public static String getCachedUserId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Global.SHARED_PREF_USER_ID, null);
    }
}
