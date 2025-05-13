package com.progetto_swe.orm.database_exception;

public class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(String message) {
        super(message);
    }
}//TODO questa è Eccezione generale di SQL connection non viene gestito spacca dritto tutto
