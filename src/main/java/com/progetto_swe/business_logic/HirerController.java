package com.progetto_swe.business_logic;

import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;

public class HirerController {
    Hirer hirer;

    public HirerController(Hirer hirer) {
        this.hirer = hirer;
    }

    public ArrayList<Item> searchItem(String keyWords, Category category) {
        CatalogueDAO catalogueDAO = new CatalogueDAO();
        Catalogue catalogue = catalogueDAO.getCatalogue();
        return hirer.searchItem(catalogue, keyWords, category);
    }

    public ArrayList<Item> advanceSearchItem(String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate) {
        CatalogueDAO catalogueDAO = new CatalogueDAO();
        Catalogue catalogue = catalogueDAO.getCatalogue();
        return catalogue.advanceSearchItem(keywords, category, language, borrowable, startDate, endDate);
    }

    public boolean addToWaitingList(Item item, Library storagePlace) {
        CatalogueDAO catalogueDAO = new CatalogueDAO();
        Catalogue catalogue = catalogueDAO.getCatalogue();
        int code = catalogue.contains(item);
        if (code == -1) {
            return false;
        }

        ReservationDAO reservationDAO = new ReservationDAO();
        if (reservationDAO.getReservations().reservationExist(this.hirer, item, storagePlace)) {
            return false;
        }

        LendingDAO lendingDAO = new LendingDAO();
        if (lendingDAO.getLendings().lendingExist(this.hirer, item, storagePlace)) {
            return false;
        }

        WaitingListDAO waitingListDAO = new WaitingListDAO();
        if (waitingListDAO.addToWaitingList(item.getCode(), storagePlace.toString(), hirer.getEmail())) {
            MailSender mailSender = new MailSender();
            mailSender.mandaMail(); //avviso aggiunta in lista attesa
            return true;
        }
        return false;
    }

    public boolean reserveItem(Item item, Library storagePlace) {
        if (this.hirer.getUnbannedDate() == null) {
            return false;
        }
        if(!item.getLibraryPhysicalCopies(storagePlace).isBorrowable()){
            return false;
        }
        ReservationDAO reservationDAO = new ReservationDAO();
        ListOfReservation reservations = reservationDAO.getReservations();
        if (reservations.reservationExist(this.hirer, item, storagePlace)) {
            return false;
        }
        return reservationDAO.addReservation(this.hirer.getUserCode(), item.getCode(), storagePlace.name());
    }

    public boolean deleteReservation(Reservation reservation) {
        ReservationDAO reservationDAO = new ReservationDAO();
        ListOfReservation reservations = reservationDAO.getReservations();
        if (!reservations.haveReservation(reservation)) {
            return false;
        }
        return reservationDAO.removeReservation(hirer.getUserCode(), reservation.getItem().getCode(), reservation.getStoragePlace().toString());
    }

    public ArrayList<Lending> getLendings() {
        LendingDAO lendingDAO = new LendingDAO();
        return lendingDAO.getLendings().getLendingsByHirer(this.hirer);
    }

    public ArrayList<Reservation> getReservation() {
        ReservationDAO reservationDAO = new ReservationDAO();
        return reservationDAO.getReservations().getReservationsByHirer(this.hirer);
    }

    public ListOfReservation getListOfReservation() {
        ReservationDAO reservationDAO = new ReservationDAO();
        return reservationDAO.getReservations();
    }

    public ListOfLending getListOfLending() {
        LendingDAO lendingDAO = new LendingDAO();
        return lendingDAO.getLendings();
    }
}
