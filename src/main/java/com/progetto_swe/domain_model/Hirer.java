package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Hirer extends User{
    ArrayList<Lending> lendings = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();
    Catalogue catalogue = new Catalogue();

    public Hirer(String userCode, String name, String surname, String eMail, String telephoneNumber, 
                UserProfile userProfile, ArrayList<Lending> lendings, ArrayList<Reservation> reservations, Catalogue catalogue) {

        super(userCode, name, surname, eMail, telephoneNumber, userProfile);
        this.lendings = lendings;
        this.reservations = reservations;
        this.catalogue = catalogue;
    }

    public Hirer(String userCode, String name, String surname, String eMail, String telephoneNumber, UserProfile userProfile) {
        super(userCode, name, surname, eMail, telephoneNumber, userProfile);
    }

    //lascia poi sistemo
    public boolean addLending(Lending lending){
        //controllo autorizzazione
        return lendings.add(lending);
    }
    public boolean removeLending(Lending lending){
        //controllo autorizzazione
        return lendings.remove(lending);
    }
    public boolean addReservation(Reservation reservation){
        //controllo autorizzazione
        return reservations.add(reservation);
    }
    public boolean removeReservation(Reservation reservation){
        //controllo autorizzazione
        return reservations.remove(reservation);
    }

    public ArrayList<Item> searchItem(String keyWords, Category category){
        return catalogue.searchItem(keyWords, category);
    }

    public boolean reservePhysicalCopy(Item item, Library storagePlace){
        Reservation r = new Reservation(LocalDate.now(), this, item, storagePlace);
        return reservations.add(r) && item.addReservation(r);
    }

    public ArrayList<Lending> getLendings() { return lendings; }
    public ArrayList<Reservation> getReservations() { return reservations; }


}
