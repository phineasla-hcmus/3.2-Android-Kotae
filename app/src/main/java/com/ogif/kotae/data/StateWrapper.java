package com.ogif.kotae.data;

import androidx.annotation.Nullable;

/**
 * Provide states to LiveData by wrapping the object. Currently support SUCCESS and FAIL
 * states.
 */
public class StateWrapper<T> {
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private final int status;
    @Nullable
    private final T data;
    @Nullable
    private final Exception exception;

    public StateWrapper(int status, @Nullable T data, @Nullable Exception exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }

    public static <T> StateWrapper<T> success(T data) {
        return new StateWrapper<T>(SUCCESS, data, null);
    }

    public static <T> StateWrapper<T> fail(Exception exception) {
        return new StateWrapper<T>(FAIL, null, exception);
    }

    public boolean isSuccessful() {
        return status == SUCCESS;
    }

    public boolean isFailed() {
        return status == FAIL;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Exception getException() {
        return exception;
    }
}
