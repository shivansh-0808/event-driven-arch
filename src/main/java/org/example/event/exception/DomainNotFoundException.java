package org.example.event.exception;

public class DomainNotFoundException extends RuntimeException {
    public DomainNotFoundException(String s) {
        super(s);
    }
}
