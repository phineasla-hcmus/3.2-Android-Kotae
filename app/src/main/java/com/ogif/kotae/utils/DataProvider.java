package com.ogif.kotae.utils;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public final class DataProvider implements Serializable {
    // private List initialSearchQueries = CollectionsKt.mutableListOf(new String[]{"dinosoaring", "liquidathor", "cobrawl", "advicewalker", "coachsoul", "lightqueen", "messymosquito", "coldox", "froghurt", "rangerman"});

    private final List<String> initialSearchQueries = new ArrayList<>();

    @NonNull
    public final List<String> getInitialSearchQueries() {
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

                // String var10000 = it.toLowerCase();
                // String var10001 = query.toLowerCase();
                // if (StringsKt.startsWith$default(var10000, var10001, false, 2, (Object) null)) {
                //     pickedSuggestions.add(it);
                // }
                pickedSuggestions.add(it);
            }
        }

        return pickedSuggestions;
    }

    public final void saveSearchQuery(String searchQuery) {
        List queries = this.initialSearchQueries;
        queries.remove(searchQuery);
        queries.add(0, searchQuery);
    }

    public final void removeSearchQuery(String searchQuery) {
        this.initialSearchQueries.remove(searchQuery);
    }
}
