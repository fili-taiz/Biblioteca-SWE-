package com.progetto_swe.domain_model;

import java.util.ArrayList;

public class ListOfReservation {
    ArrayList<Reservation> reservations = new ArrayList<>();

    public ListOfReservation(ArrayList<Reservation> reservations) {
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

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
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

    public ArrayList<Reservation> getReservationsByItem(Item item) {
        ArrayList<Reservation> bookReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getItem().equals(item)) {
                bookReservations.add(reservation);
            }
        }
        return bookReservations;
    }
}
