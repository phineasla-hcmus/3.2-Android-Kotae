package com.ogif.kotae.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Provide callbacks between ViewModel and Repository, aims to remove LiveData from Repository.
 * All methods are optional
 */
public abstract class TaskListener<T> {
    private static final String TAG = "TaskListener";

    public void onSuccess(@Nullable T result) {
        Log.d(TAG, "onSuccess is not implemented");
    }

    public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure is not implemented");
    }

    public void onComplete(@NonNull T result) {
        Log.d(TAG, "onComplete is not implemented");
    }

    public void onProgress(@NonNull T snapshot) {
        Log.d(TAG, "onProgress is not implemented");
    }
}
