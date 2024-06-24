package ua.edu.ukma.exceptions;

public class ConstraintViolationException extends ResponseStatusException {
    public ConstraintViolationException(String message) {
        super(409, message);
    }
}
