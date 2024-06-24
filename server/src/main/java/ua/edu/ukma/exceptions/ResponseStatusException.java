package ua.edu.ukma.exceptions;

public class ResponseStatusException extends RuntimeException {
    private final int statusCode;

    public ResponseStatusException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public ResponseStatusException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
