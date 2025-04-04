package com.progetto_swe.orm.database_exception;

public class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(String message, Throwable cause){
        super(message, cause);
    }
}
