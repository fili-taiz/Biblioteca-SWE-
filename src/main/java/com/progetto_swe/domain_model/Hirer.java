package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Hirer extends User{
    ArrayList<Lending> lendings = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();
    Catalogue catalogue = new Catalogue();
    LocalDate unbannedDate;

    public Hirer(String userCode, String name, String surname, String email, String telephoneNumber,
                 UserCredentials userProfile, ArrayList<Lending> lendings, ArrayList<Reservation> reservations, Catalogue catalogue, LocalDate unbannedDate) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.lendings = lendings;
        this.reservations = reservations;
        this.catalogue = catalogue;
        this.unbannedDate = unbannedDate;
    }

    public Hirer(String userCode, String name, String surname, String email, String telephoneNumber, UserCredentials userProfile, LocalDate unbannedDate) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.unbannedDate = unbannedDate;
    }

    public ArrayList<Item> searchItem(String keyWords, Category category){
        return catalogue.searchItem(keyWords, category);
    }

    public boolean reservePhysicalCopy(Item item, Library storagePlace){
        Reservation r = new Reservation(LocalDate.now(), this, item, storagePlace);
        return reservations.add(r) /*&& item.addReservation(r)*/;
    }

    public boolean haveReservation(Reservation reservation){
        for(Reservation r : reservations){
            if(r.equals(reservation)){
                return true;
            }
        }
        return false;
    }

    public boolean haveLending(Lending lending){
        for(Lending l : lendings){
            if(l.equals(lending)){
                return true;
            }
        }
        return false;
    }

    public boolean contains(String keyWord){
        if(userCode.toUpperCase().contains(keyWord)){
            return true;
        }
        if(name.toUpperCase().contains(keyWord)){
            return true;
        }
        if(surname.toUpperCase().contains(keyWord)){
            return true;
        }
        if(email.toUpperCase().contains(keyWord)){
            return true;
        }
        if(telephoneNumber.toUpperCase().contains(keyWord)){
            return true;
        }
        return false;
    }

    public LocalDate getUnbannedDate() {
        return unbannedDate;
    }

    //controllo autorizzazione
    public ArrayList<Lending> getLendings() { return lendings; }
    public ArrayList<Reservation> getReservations() { return reservations; }


}
