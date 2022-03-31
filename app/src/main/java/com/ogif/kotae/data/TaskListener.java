package com.ogif.kotae.data;

import androidx.annotation.NonNull;

/**
 * Provide callbacks between ViewModel and Repository, aims to remove LiveData from Repository.
 *
 * @implNote Mar/30/2022, found that Google already have
 * <a href="https://developers.google.com/android/reference/com/google/android/gms/tasks/TaskCompletionSource">
 * TaskCompletionSource
 * </a>
 * @see <a href="https://stackoverflow.com/q/40161354/12405558">
 * How to create custom tasks for Firebase using the Google Play services Task API
 * </a>
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