package com.progetto_swe.domain_model;

import java.util.Objects;

public abstract class User {
    protected String userCode;
    protected String name;
    protected String surname;
    protected String email;
    protected String telephoneNumber;
    protected UserCredentials userCredentials;

    public User(String userCode, String name, String surname, String email, String telephoneNumber, UserCredentials userCredentials){
        this.userCode = userCode;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.userCredentials = userCredentials;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        User user = (User) o;
        return (Objects.equals(this.userCode, user.userCode)) && Objects.equals(this.name, user.name) && Objects.equals(this.surname, user.surname) && Objects.equals(this.email, user.email) && Objects.equals(this.telephoneNumber, user.telephoneNumber);
    }

    public String getUserCode() { return this.userCode; }
    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEmail() { return this.email; }
    public String getTelephoneNumber() { return this.telephoneNumber; }
    public UserCredentials getUserCredentials() { return this.userCredentials; }

    public void setUserCode(String userCode) { this.userCode = userCode; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setUserCredentials(UserCredentials newUserCredentials){ this.userCredentials = newUserCredentials; }
}
