package com.progetto_swe.domain_model;

public abstract class User {
    String userCode;
    String name;
    String surname;
    String email;
    String telephoneNumber;
    UserProfile userProfile;

    public User(String userCode, String name, String surname, String email, String telephoneNumber, UserProfile userProfile){
        this.userCode = userCode;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.userProfile = userProfile;
    }

    public String getUserCode() { return this.userCode; }
    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEmail() { return this.email; }
    public String getTelephoneNumber() { return this.telephoneNumber; }
    public UserProfile getUserProfile() { return this.userProfile; }

    public void setUserCode(String userCode) { this.userCode = userCode; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEMail(String email) { this.email = email; }
    public void setTelephoneNumber(String telephoneNumber) { this.telephoneNumber = telephoneNumber; }
    public void setUserProfile(UserProfile newUserProfile){ this.userProfile = newUserProfile; }
}
