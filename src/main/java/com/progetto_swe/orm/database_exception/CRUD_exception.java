package com.progetto_swe.orm.database_exception;

public class CRUD_exception extends RuntimeException{
    public CRUD_exception(String message, Throwable cause){
        super(message, cause);
    }
}
