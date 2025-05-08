package com.progetto_swe.domain_model;

import java.util.Objects;

public class UserCredentials {
    private String userCode;
    private String hashedPassword;

    public UserCredentials(String userCode, String hashedPassword){
        this.userCode = userCode;
        this.hashedPassword = hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        UserCredentials ucs = (UserCredentials) o;
        return Objects.equals(this.userCode, ucs.userCode) && Objects.equals(this.hashedPassword, ucs.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCode, hashedPassword);
    }

    public String getUserCode(){ return this.userCode; }
    public String getHashedPassword(){ return this.hashedPassword; }

}
