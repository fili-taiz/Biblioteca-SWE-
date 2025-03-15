package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admin extends User{
    private final Library workingPlace;
    private Catalogue catalogue;
    private ListOfHirers listOfHirers;

    public Admin(String userCode, String name, String surname, String email, String telephoneMumber, Library workingPlace, Catalogue catalogue, ListOfHirers listOfHirers, UserProfile userProfile) {
        super(userCode, name, surname, email, telephoneMumber, userProfile);
        this.workingPlace = workingPlace;
        this.catalogue = catalogue;
        this.listOfHirers = listOfHirers;
    }

    public Admin(String userCode, String name, String surname, String email, String telephoneNumber, Library workingPlace, UserProfile userProfile) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.workingPlace = workingPlace;
    }

    public boolean addItem(Item item, int numberOfCopies) {
        return this.catalogue.addItem(item) && item.addCopies(workingPlace, numberOfCopies);
    }

    //admin nonn può cancellare un book, magazine, thesis ma solo le copie presenti nella biblioteca in cui è presente
    public boolean removeItem(int itemCode) {
        return this.catalogue.removeCopies(itemCode, workingPlace);
    }


    public boolean addHirer(Hirer hirer) {
        return listOfHirers.addHirer(hirer);
    }

    public boolean registerLending(Hirer hirer, Item item) {
        if(!item.isBorrowable()){
            return false;
        }

        if (item.getNumberOfAvailableCopies(this.workingPlace, item) <= 1) {
            return false;
        }

        Lending lending = new Lending(LocalDate.now(), hirer, item, this.workingPlace);
        hirer.getLendings().add(lending);
        item.addLending(lending);
        return true;
    }

    public boolean confirmReservationWithdraw(Reservation reservation) {
        reservation.getHirer().getReservations().remove(reservation);
        reservation.getItem().removeReservation(reservation);
        registerLending(reservation.getHirer(), reservation.getItem());
        return false;
    }

    public void registerItemReturn(Lending l) {
        l.getItem().removeLending(l);
        l.getHirer().getLendings().remove(l);
    }

    public boolean updateItem(Item originalItem, Item newItem, int numberOfCopies){
        return originalItem.updateItem(newItem) && originalItem.setCopies(workingPlace, numberOfCopies);
    }

    public ArrayList<Item> searchItem(String keyWords, Category category){
        return this.catalogue.searchItem(keyWords, category);
    }

    public Item getItem(int itemCode) {
        return this.catalogue.getItem(itemCode);
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

    public void setHirers(ListOfHirers newListOfHirers) {
        this.listOfHirers = newListOfHirers;
    }

}

