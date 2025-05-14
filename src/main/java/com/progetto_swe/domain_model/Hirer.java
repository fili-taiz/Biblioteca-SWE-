package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.Objects;

public class Hirer extends User{
    private LocalDate unbannedDate;

    public Hirer(String userCode,
                 String name,
                 String surname,
                 String email,
                 String telephoneNumber,
                 Token userProfile,
                 LocalDate unbannedDate) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.unbannedDate = unbannedDate;
    }

    public boolean contains(String keyword){
        if(this.getUserCode().toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(this.getName().toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(this.getSurname().toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(this.getEmail().toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        return this.getTelephoneNumber().toUpperCase().contains(keyword.toUpperCase());
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        if(!super.equals(o)){
            return false;
        }
        Hirer hirer = (Hirer) o;
        if(this.unbannedDate == null){
            return true;
        }
        return this.unbannedDate.equals(hirer.unbannedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserCode(), this.getName(), this.getSurname(), this.getEmail(), this.getTelephoneNumber(), unbannedDate);
    }

    public LocalDate getUnbannedDate() {
        return this.unbannedDate;
    }
    public void setUnbannedDate(LocalDate new_unbannedDate) { this.unbannedDate = new_unbannedDate; }
}
