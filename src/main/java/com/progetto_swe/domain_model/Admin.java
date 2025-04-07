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
    } //TESTATO


    public ArrayList<Item> searchItem(String keywords, Category category){
        return this.catalogue.searchItem(keywords, category);
    } //TESTO DIRETTAMENTE searchItem nella classe Catalogue

    public ArrayList<Hirer> searchHirer(String keywords){
        return this.listOfHirers.searchHirer(keywords);
    }
    //TESTO DIRETTAMENTE searchHirer nella classe ListOfHirers

    public ListOfHirers getListOfHirers() {
        return listOfHirers;
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

