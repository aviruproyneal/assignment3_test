package edu.altu.medapp.service;

public class Result<T> {
    private final boolean ok;
    private final T value;
    private final String error;

    private Result(boolean ok, T value, String error) {
        this.ok = ok;
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(true, value, null);
    }

    public static <T> Result<T> failure(String error) {
        return new Result<>(false, null, error);
    }

    public boolean isSuccess() {
        return ok;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return error;
    }
}