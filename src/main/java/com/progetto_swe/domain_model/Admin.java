package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admin extends User{
    private final Library workingPlace;

    public Admin(String userCode, String name, String surname, String email, String telephoneNumber, Library workingPlace, UserCredentials userProfile) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.workingPlace = workingPlace;
    }

    public ArrayList<Item> searchItem(Catalogue catalogue, String keywords, Category category){
        ArrayList<Item> result = catalogue.searchItem(keywords, category);
        result.removeIf(item -> item.getLibraryPhysicalCopies(this.workingPlace) == null);
        return result;
    }

    public ArrayList<Item> advancedSearchItem(Catalogue catalogue, String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate){
        ArrayList<Item> result = catalogue.advancedSearchItem(keywords, category, language, borrowable, startDate, endDate);
        result.removeIf(item -> item.getLibraryPhysicalCopies(this.workingPlace) == null);
        return result;
    }

    public Library getWorkingPlace() {
        return workingPlace;
    }

}

