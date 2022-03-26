package com.ogif.kotae.utils;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kotlin.collections.CollectionsKt;


public final class DataProvider implements Serializable {
    private final List PROFILE_IMAGE_RESOURCE_IDS = CollectionsKt.listOf(new Integer[]{700109, 700071, 700164, 700111, 700122, 700082, 700035, 700046, 700003, 700061, 700116, 700069, 700012, 700064, 700029, 700171, 700093, 700077, 700113, 700027, 700101, 700044, 700068, 700051, 700007, 700136, 700165, 700097, 700133, 700023});
    private List initialSearchQueries = CollectionsKt.mutableListOf(new String[]{"dinosoaring", "liquidathor", "cobrawl", "advicewalker", "coachsoul", "lightqueen", "messymosquito", "coldox", "froghurt", "rangerman"});

    @NonNull
    public final List<String> getInitialSearchQueries() {
        return this.initialSearchQueries;
    }

    @NonNull
    public final List<String> getSuggestionsForQuery(@NonNull String query) {
        List pickedSuggestions = (List) (new ArrayList());
        CharSequence var3 = (CharSequence) query;
        if (var3.length() == 0) {
            pickedSuggestions.addAll((Collection) this.initialSearchQueries);
        } else {
            Iterable $this$forEach$iv = (Iterable) this.initialSearchQueries;
            boolean $i$f$forEach = false;
            Iterator var5 = $this$forEach$iv.iterator();

            while (var5.hasNext()) {
                Object element$iv = var5.next();
                String it = (String) element$iv;
                boolean var8 = false;
                if (it == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }

                String var10000 = it.toLowerCase();
                String var10001 = query.toLowerCase();
//                if (StringsKt.startsWith$default(var10000, var10001, false, 2, (Object) null)) {
//                    pickedSuggestions.add(it);
//                }
                pickedSuggestions.add(it);
            }
        }

        return pickedSuggestions;
    }

    public final void saveSearchQuery(String searchQuery) {
        List var2 = this.initialSearchQueries;
        boolean var4 = false;
        var2.remove(searchQuery);
        var2.add(0, searchQuery);
    }

    public final void removeSearchQuery(String searchQuery) {
        ;
        this.initialSearchQueries.remove(searchQuery);
    }
}
