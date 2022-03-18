package com.ogif.kotae.utils;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

public final class ListExtension {
    @Nullable
    public static final Object random(@NonNull List $this$random, @NonNull Random random) {
        return $this$random.size() > 0 ? $this$random.get(random.nextInt($this$random.size())) : null;
    }
}
