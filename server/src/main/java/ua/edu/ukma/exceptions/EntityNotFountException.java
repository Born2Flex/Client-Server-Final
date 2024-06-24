package ua.edu.ukma.exceptions;

public class EntityNotFountException extends ResponseStatusException {
    public EntityNotFountException(String message) {
        super(404, message);
    }
}
