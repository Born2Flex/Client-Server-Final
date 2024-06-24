package ua.edu.ukma.exceptions;

public class BadRequestException extends ResponseStatusException {
    public BadRequestException(String message, Exception e) {
        super(400, message, e);
    }
}
