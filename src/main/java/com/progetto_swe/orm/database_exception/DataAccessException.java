package com.progetto_swe.orm.database_exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause){
        super(message, cause);
    }
}
