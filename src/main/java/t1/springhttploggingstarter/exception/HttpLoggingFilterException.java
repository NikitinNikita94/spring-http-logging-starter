package t1.springhttploggingstarter.exception;

public class HttpLoggingFilterException extends RuntimeException {
    public HttpLoggingFilterException(String message, Throwable cause) {
        super(message);
    }
}
