package bbd.miniconomy.lifeinsurance.models;

import java.util.Optional;

public class Result<T> {
    private final T value;
    private final String errorMessage;
    private final boolean isSuccess;

    private Result(T value, String errorMessage, boolean isSuccess) {
        this.value = value;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public T getValue() {
        if (!isSuccess) {
            throw new IllegalStateException("Cannot get value from failed result");
        }
        return value;
    }

    public String getErrorMessage() {
        if (isSuccess) {
            throw new IllegalStateException("No error message available for successful result");
        }
        return errorMessage;
    }

    public Optional<T> getValueOptional() {
        return Optional.ofNullable(value);
    }
}
