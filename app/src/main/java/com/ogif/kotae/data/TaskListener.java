package com.ogif.kotae.data;

import androidx.annotation.NonNull;

/**
 * Provide callbacks between ViewModel and Repository, aims to remove LiveData from Repository.
 */
public interface TaskListener {
    interface Success<TSuccess> {
        void onSuccess(TSuccess result);
    }

    interface State<TComplete> extends Success<TComplete> {
        void onFailure(@NonNull Exception e);
    }

    interface Progress<TComplete, TProgress> extends State<TComplete> {
        void onProgress(@NonNull TProgress snapshot);
    }
}