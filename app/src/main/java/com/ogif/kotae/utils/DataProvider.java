package com.ogif.kotae.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public final class DataProvider implements Serializable {
    private List<String> initialSearchQueries = new ArrayList<>();
    private Context context;
    private String key = "search";

    @NonNull
    public final List<String> getInitialSearchQueries(Context context) {
        this.context = context;
        if (getArrayList(key) != null)
            this.initialSearchQueries = getArrayList(key);
        return this.initialSearchQueries;
    }

    @NonNull
    public final List<String> getSuggestionsForQuery(@NonNull String query) {
        List<String> pickedSuggestions = new ArrayList<>();
        if (((CharSequence) query).length() == 0) {
            pickedSuggestions.addAll(this.initialSearchQueries);
        } else {

            for (Object object : (Iterable) this.initialSearchQueries) {
                String it = (String) object;
                if (it == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                pickedSuggestions.add(it);
            }
        }

        return pickedSuggestions;
    }

    /**
     * @see <a href="https://stackoverflow.com/a/56682835">
     * */

    public void saveArrayList(ArrayList<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }

    public ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public final void saveSearchQuery(String searchQuery) {
        List queries = this.initialSearchQueries;
        queries.remove(searchQuery);
        queries.add(0, searchQuery);
        saveArrayList((ArrayList<String>) queries, key);
    }

    public final void removeSearchQuery(String searchQuery) {
        this.initialSearchQueries.remove(searchQuery);
    }
}
