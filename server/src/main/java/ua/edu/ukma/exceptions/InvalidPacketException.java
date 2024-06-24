package ua.edu.ukma.exceptions;

public class InvalidPacketException extends RuntimeException {
    public InvalidPacketException(String message) {
        super("Received invalid packet. " + message);
    }
}
