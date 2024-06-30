package bbd.miniconomy.lifeinsurance.models;

import java.util.Optional;

public class Result<T> {
    private final T value;
    private final int errorCode;
    private final String errorMessage;
    private final boolean isSuccess;

    private Result(T value, String errorMessage, boolean isSuccess, int errorCode) {
        this.value = value;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true, 200);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false, 500);
    }

    public static <T> Result<T> failure(String errorMessage, int errorCode) {
        return new Result<>(null, errorMessage, false, errorCode);
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

    public int getErrorCode() {
        if (isSuccess) {
            throw new IllegalStateException("No error message available for successful result");
        }
        return errorCode;
    }

    public Optional<T> getValueOptional() {
        return Optional.ofNullable(value);
    }
}
