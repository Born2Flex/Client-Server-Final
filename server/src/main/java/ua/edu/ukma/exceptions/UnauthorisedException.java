package ua.edu.ukma.exceptions;

public class UnauthorisedException extends ResponseStatusException {

    public UnauthorisedException(String message) {
        super(401, message);
    }
}
