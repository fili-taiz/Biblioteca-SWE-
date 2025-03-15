package com.progetto_swe.domain_model;

public class UserProfile {
    private String username;
    private String password;

    public UserProfile(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){ return this.username; }
    public String getPassword(){ return this.password; }
}
