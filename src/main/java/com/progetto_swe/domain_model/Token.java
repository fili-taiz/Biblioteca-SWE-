package com.progetto_swe.domain_model;

import com.progetto_swe.business_logic.Hasher;

import java.util.Objects;

public class Token {
    protected String token;

    public Token(Hirer hirer) {
        this.token = Hasher.hash("Hirer") + ";";
    }

    public Token(Admin admin) {
        this.token = Hasher.hash("Admin") + ";" + admin.getWorkingPlace() + ";";
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        Token t = (Token) o;
        return Objects.equals(this.token, t.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    public String getTokenRole(){ return this.token.split(";")[0]; }
    public String getTokenWorkingPlace(){ return this.token.split(";")[1]; }
}
