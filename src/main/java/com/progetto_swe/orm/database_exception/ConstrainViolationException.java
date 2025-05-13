package com.progetto_swe.orm.database_exception;

public class ConstrainViolationException extends RuntimeException {
    public ConstrainViolationException(String message) {
        super(message);
    }
}
