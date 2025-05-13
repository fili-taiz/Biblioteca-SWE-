package com.progetto_swe.domain_model;

import java.util.Objects;

public class Admin extends User{
    private Library workingPlace;

    public Admin(String userCode,
                 String name,
                 String surname,
                 String email,
                 String telephoneNumber,
                 Library workingPlace,
                 Token userProfile) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.workingPlace = workingPlace;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        Admin admin = (Admin) o;
        return Objects.equals(this.getUserCode(), admin.getUserCode()) &&
                Objects.equals(this.getName(), admin.getName()) &&
                Objects.equals(this.getSurname(), admin.getSurname()) &&
                Objects.equals(this.getEmail(), admin.getEmail()) &&
                Objects.equals(this.getTelephoneNumber(), admin.getTelephoneNumber()) &&
                Objects.equals(this.getWorkingPlace(), admin.getWorkingPlace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCode, name, surname, email, telephoneNumber, workingPlace);
    }

    public Library getWorkingPlace() {
        return workingPlace;
    }

}

