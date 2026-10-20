package org.example.event.exception;

public class InvalidEventException extends RuntimeException {
    public InvalidEventException(String dOmainIsInvalid) {
        super(dOmainIsInvalid);
    }
}
