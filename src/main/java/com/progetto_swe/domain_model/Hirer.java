package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Hirer extends User{
    ArrayList<Lending> lendings;
    ArrayList<Reservation> reservations;
    Catalogue catalogue;
    LocalDate unbannedDate;

    public Hirer(String userCode, String name, String surname, String email, String telephoneNumber,
                 UserCredentials userProfile, ArrayList<Lending> lendings, ArrayList<Reservation> reservations, Catalogue catalogue, LocalDate unbannedDate) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.lendings = lendings;
        this.reservations = reservations;
        this.catalogue = catalogue;
        this.unbannedDate = unbannedDate;
    }

    /*public Hirer(String userCode, String name, String surname, String email, String telephoneNumber, UserCredentials userProfile, LocalDate unbannedDate) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.unbannedDate = unbannedDate;
    }*/


    public ArrayList<Item> searchItem(String keywords, Category category){
        return catalogue.searchItem(keywords, category);
    } //HO GIA' TESTATO catalogue.searchitem...

    public boolean reservePhysicalCopy(Item item, Library storagePlace){
        Reservation r = new Reservation(LocalDate.now(), this, item, storagePlace);
        return reservations.add(r) /*&& item.addReservation(r)*/;
    }//E' semplicemente un add...

    public boolean haveReservation(Reservation reservation){
        for(Reservation r : reservations){
            if(r.equals(reservation)){
                return true;
            }
        }
        return false;
    } //GIA' TESTATO NELLA CLASSE ITEM

    public boolean haveLending(Lending lending){
        for(Lending l : lendings){
            if(l.equals(lending)){
                return true;
            }
        }
        return false;
    } //GIA' TESTATO NELLA CLASSE ITEM

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


    //controllo autorizzazione
    public ArrayList<Lending> getLendings() { return this.lendings; }
    public ArrayList<Reservation> getReservations() { return this.reservations; }
    public Catalogue getCatalogue(){ return this.catalogue; }
    public LocalDate getUnbannedDate() {
        return this.unbannedDate;
    }


}
