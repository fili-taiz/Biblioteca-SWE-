package com.progetto_swe.business_logic.business_logic_exception;

public class ActionDeniedException extends RuntimeException {
    public ActionDeniedException(String message) {
        super(message);
    }
}


