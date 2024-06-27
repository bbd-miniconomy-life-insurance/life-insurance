package bbd.miniconomy.lifeinsurance.services.api.utils;

public class Http5xxException extends RuntimeException {
    public Http5xxException(String message) {
        super(message);
    }
}
