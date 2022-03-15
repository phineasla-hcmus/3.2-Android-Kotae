package com.ogif.kotae.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Provide callbacks between ViewModel and Repository, aims to remove LiveData from Repository.
 */
public interface TaskListener {
    interface Success<TSuccess> {
        void onSuccess(@Nullable TSuccess result);
    }

    interface State<TSuccess> extends Success<TSuccess> {
        void onFailure(@NonNull Exception e);
    }

    interface Progress<TSuccess, TProgress> extends State<TSuccess> {
        void onProgress(@NonNull TProgress snapshot);
    }
}