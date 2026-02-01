package edu.altu.medapp.service;

public class Result<T> {
    private final boolean success;
    private final T data;
    private final String errorMessage;

    private Result(boolean success, T data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(false, null, errorMessage);
    }

    public static <T> Result<T> failure(String errorMessage, T partialData) {
        return new Result<>(false, partialData, errorMessage);
    }

    // Getters
    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getErrorMessage() { return errorMessage; }

    public T getDataOrThrow() {
        if (!success) {
            throw new RuntimeException(errorMessage);
        }
        return data;
    }
}