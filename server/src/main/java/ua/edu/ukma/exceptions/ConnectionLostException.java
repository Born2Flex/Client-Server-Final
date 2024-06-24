package ua.edu.ukma.exceptions;

public class ConnectionLostException extends RuntimeException {
    public ConnectionLostException(Throwable cause) {
        super(cause);
    }
}
