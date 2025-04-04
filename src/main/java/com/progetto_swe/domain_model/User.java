package com.progetto_swe.domain_model;

public abstract class User {
    String userCode;
    String name;
    String surname;
    String email;
    String telephoneNumber;
    UserCredentials userCredentials;

    public User(String userCode, String name, String surname, String email, String telephoneNumber, UserCredentials userCredentials){
        this.userCode = userCode;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.userCredentials = userCredentials;
    }

    public String getUserCode() { return this.userCode; }
    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEmail() { return this.email; }
    public String getTelephoneNumber() { return this.telephoneNumber; }
    public UserCredentials getUserProfile() { return this.userCredentials; }

    public void setUserCode(String userCode) { this.userCode = userCode; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setUserCredentials(UserCredentials newUserCredentials){ this.userCredentials = newUserCredentials; }
}
