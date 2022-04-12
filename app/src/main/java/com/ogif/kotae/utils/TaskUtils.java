package com.ogif.kotae.utils;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;

public class TaskUtils {
    public static <T> ListenableFuture<T> toListenableFuture(Task<T> task) {
        return CallbackToFutureAdapter.getFuture(completer -> task.addOnCompleteListener(completedTask -> {
            if (completedTask.isCanceled()) {
                completer.setCancelled();
            } else if (completedTask.isSuccessful()) {
                completer.set(completedTask.getResult());
            } else {
                Exception e = completedTask.getException();
                if (e != null) {
                    completer.setException(e);
                } else {
                    throw new IllegalStateException();
                }
            }
        }));
    }
}
