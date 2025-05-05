package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Hirer extends User{
    private LocalDate unbannedDate;

    public Hirer(String userCode, String name, String surname, String email, String telephoneNumber,
                 UserCredentials userProfile, LocalDate unbannedDate) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.unbannedDate = unbannedDate;
    }

    public ArrayList<Item> searchItem(Catalogue catalogue, String keywords, Category category){
        return catalogue.searchItem(keywords, category);
    } //HO GIA' TESTATO catalogue.searchitem...

    public ArrayList<Item> advanceSearchItem(Catalogue catalogue, String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate){
        return catalogue.advancedSearchItem(keywords, category, language, borrowable, startDate, endDate);
    }

    public boolean contains(String keyword){
        if(userCode.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(name.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(surname.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(email.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        return telephoneNumber.toUpperCase().contains(keyword.toUpperCase());
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
        return Objects.hash(userCode, name, surname, email, telephoneNumber, unbannedDate);
    }

    public LocalDate getUnbannedDate() {
        return this.unbannedDate;
    }
}
