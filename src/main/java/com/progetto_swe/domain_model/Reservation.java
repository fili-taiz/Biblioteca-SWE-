package com.progetto_swe.domain_model;
import java.time.LocalDate;

public class Reservation {
    final LocalDate reservationDate;
    Hirer hirer;
    Item item;
    Library storagePlace;

    public Reservation(LocalDate reservationDate, Hirer hirer, Item item, Library storagePlace){
        this.reservationDate = reservationDate;
        this.hirer = hirer;
        this.item = item;
        this.storagePlace = storagePlace;
    }

    public LocalDate getReservationDate() { return this.reservationDate; }
    public Hirer getHirer(){ return this.hirer; }
    public Item getItem(){ return this.item; }
    public Library getStoragePlace(){ return this.storagePlace; }

    public void setHirer(Hirer newHirer){ this.hirer = newHirer; }
    public void setItem(Item newItem){ this.item = newItem; }

}
