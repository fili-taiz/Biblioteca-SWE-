package com.progetto_swe.domain_model;

import java.util.Objects;

public class UserCredentials {
    private String userCode;
    private String hashedPassword;

    public UserCredentials(String userCode, String hashedPassword){
        this.userCode = userCode;
        this.hashedPassword = hashedPassword;
    }

    public String getUserCode(){ return this.userCode; }
    public String getHashedPassword(){ return this.hashedPassword; }

}
