package com.ogif.kotae.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class TaskListener<T> {
    private static final String TAG = "TaskListener";

    public void onSuccess(@Nullable T result) {
        Log.v(TAG, "onSuccess is not implemented");
    }

    public void onFailure(@NonNull Exception e) {
        Log.v(TAG, "onFailure is not implemented");
    }

    public void onComplete(@NonNull T result) {
        Log.v(TAG, "onComplete is not implemented");
    }

    public void onProgress(@NonNull T snapshot) {
        Log.v(TAG, "onProgress is not implemented");
    }
}
