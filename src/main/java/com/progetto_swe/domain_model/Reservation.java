package com.progetto_swe.domain_model;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private LocalDate reservationDate;
    private Hirer hirer;
    private Item item;
    private Library storagePlace;

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

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        Reservation reservation = (Reservation) o;
        return (this.reservationDate.equals(reservation.reservationDate)) && (this.hirer.equals(reservation.hirer)) && (this.item.equals(reservation.item)) && (this.storagePlace.equals(reservation.storagePlace));
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationDate, hirer, item, storagePlace);
    }

}
