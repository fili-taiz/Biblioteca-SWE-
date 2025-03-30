package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admin extends User{
    private final Library workingPlace;
    private Catalogue catalogue;
    private ListOfHirers listOfHirers;

    public Admin(String userCode, String name, String surname, String email, String telephoneNumber, Library workingPlace, UserCredentials userProfile) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.workingPlace = workingPlace;
    }


    public ArrayList<Item> searchItem(String keyWords, Category category){
        return this.catalogue.searchItem(keyWords, category);
    }

    public ListOfHirers getListOfHirers() {
        return listOfHirers;
    }

    public ArrayList<Hirer> searchHirer(String keyWords){
        return this.listOfHirers.searchHirer(keyWords);
    }


    public Library getWorkingPlace() {
        return workingPlace;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setHirers(ListOfHirers newListOfHirers) {
        this.listOfHirers = newListOfHirers;
    }

}

