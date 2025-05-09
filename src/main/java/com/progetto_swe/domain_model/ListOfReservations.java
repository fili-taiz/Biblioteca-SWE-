package com.progetto_swe.domain_model;

import java.util.ArrayList;
import java.util.Objects;

public class ListOfReservations {
    private ArrayList<Reservation> reservations = new ArrayList<>();

    public ListOfReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public ArrayList<Reservation> getReservationsByHirer(Hirer hirer) {
        ArrayList<Reservation> hirerReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getHirer().equals(hirer)) {
                hirerReservations.add(reservation);
            }
        }
        return hirerReservations;
    }

    public ArrayList<Reservation> getReservationsByItem(Item item) {
        ArrayList<Reservation> bookReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getItem().equals(item)) {
                bookReservations.add(reservation);
            }
        }
        return bookReservations;
    }

    public int getNumberOfReservationsInLibrary(Library library, Item item){
        int n = 0;
        for(Reservation r: this.reservations){
            if(r.getItem().equals(item) && r.getStoragePlace().equals(library)){
                n += 1;
            }
        }
        return n;
    }

    public boolean haveReservation(Reservation reservation){
        for(Reservation r : reservations){
            if(r.equals(reservation)){
                return true;
            }
        }
        return false;
    }

    public boolean reservationExist(Hirer hirer, Item item, Library storagePlace) {
        for(Reservation r : reservations){
            if(r.getHirer().equals(hirer) && r.getItem().equals(item) && r.getStoragePlace().equals(storagePlace)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Reservation> getReservations(){ return this.reservations; }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o.getClass() != this.getClass()){
            return false;
        }
        ListOfReservations listOfReservations = (ListOfReservations) o;
        return Objects.equals(this.reservations, listOfReservations.getReservations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservations);
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }
}
