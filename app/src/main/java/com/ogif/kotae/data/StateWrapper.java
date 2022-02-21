package com.ogif.kotae.data;


import androidx.annotation.Nullable;

public class StateWrapper<T> {
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private final int status;
    @Nullable
    private final T data;
    @Nullable
    private final String message;

    public StateWrapper(int status, @Nullable T data, @Nullable String msg) {
        this.status = status;
        this.data = data;
        this.message = msg;
    }

    public static <T> StateWrapper<T> success(T data) {
        return new StateWrapper<T>(SUCCESS, data, null);
    }

    public static <T> StateWrapper<T> fail(String message) {
        return new StateWrapper<T>(FAIL, null, message);
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
    public String getMessage() {
        return message;
    }
}
