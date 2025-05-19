package com.progetto_swe.orm.database_exception;

import java.sql.SQLException;

public class DatabaseConnectionException extends RuntimeException {
    SQLException exception;
    public DatabaseConnectionException(SQLException e) {
        exception = e;
    }

    public Exception getException(){
        return exception;
    }
}



