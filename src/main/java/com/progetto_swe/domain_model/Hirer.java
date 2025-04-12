package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

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

    public LocalDate getUnbannedDate() {
        return this.unbannedDate;
    }


}
