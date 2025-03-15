package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Hirer extends User{
    ArrayList<Lending> lendings = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();
    Catalogue catalogue = new Catalogue();

    public Hirer(String userCode, String name, String surname, String email, String telephoneNumber, 
                UserProfile userProfile, ArrayList<Lending> lendings, ArrayList<Reservation> reservations, Catalogue catalogue) {

        super(userCode, name, surname, email, telephoneNumber, userProfile);
        this.lendings = lendings;
        this.reservations = reservations;
        this.catalogue = catalogue;
    }

    public Hirer(String userCode, String name, String surname, String email, String telephoneNumber, UserProfile userProfile) {
        super(userCode, name, surname, email, telephoneNumber, userProfile);
    }

    public ArrayList<Item> searchItem(String keyWords, Category category){
        return catalogue.searchItem(keyWords, category);
    }

    public boolean reservePhysicalCopy(Item item, Library storagePlace){
        Reservation r = new Reservation(LocalDate.now(), this, item, storagePlace);
        return reservations.add(r) && item.addReservation(r);
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

    //controllo autorizzazione
    public ArrayList<Lending> getLendings() { return lendings; }
    public ArrayList<Reservation> getReservations() { return reservations; }


}
