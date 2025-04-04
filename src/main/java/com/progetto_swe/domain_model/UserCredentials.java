package com.progetto_swe.domain_model;

public class UserCredentials {
    private String userCode;
    private String HashedPassword;

    public UserCredentials(String userCode, String HashedPassword){
        this.userCode = userCode;
        this.HashedPassword = HashedPassword;
    }

    public String getUserCode(){ return this.userCode; }
    public String getHashedPassword(){ return this.HashedPassword; }
}
