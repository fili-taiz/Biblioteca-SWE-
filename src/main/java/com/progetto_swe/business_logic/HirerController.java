package com.progetto_swe.business_logic;

import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;

public class HirerController {
    private Hirer hirer;

    public HirerController(Hirer hirer){
        this.hirer = hirer;
    }

    public ArrayList<Item> searchItem(String keyWords, Category category ){
        CatalogueDAO catalogueDAO = new CatalogueDAO();
        Catalogue catalogue = catalogueDAO.getCatalogue();
        return hirer.searchItem(catalogue, keyWords, category);
    }

    public ArrayList<Item> advanceSearchItem(String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate){
        CatalogueDAO catalogueDAO = new CatalogueDAO();
        Catalogue catalogue = catalogueDAO.getCatalogue();
        return catalogue.advancedSearchItem(keywords, category, language, borrowable, startDate, endDate);
    }

    public boolean addInWaitingList(Item item, String storagePlace){
        CatalogueDAO catalogueDAO = new CatalogueDAO();
        if(catalogueDAO.getCatalogue().contains(item) == -1){
            return false;
        }
        WaitingListDAO waitingListDAO = new WaitingListDAO();
        return waitingListDAO.addToWaitingList(item.getCode(), storagePlace, this.hirer.getEmail());
    }

    public boolean reserveItem(Item item, Library storagePlace){
        if(this.hirer.getUnbannedDate() != null){
            return false;
        }
        ReservationDAO reservationDAO = new ReservationDAO();
        if(reservationDAO.addReservation(this.hirer.getUserCode(), item.getCode(), storagePlace.name())){
            MailSender.sendReservationSuccessMail(this.hirer.getEmail(), this.hirer.getUserCode(), item.getCode(), item.getTitle(), storagePlace.toString(), LocalDate.now().plusDays(7));
            return true;
        }
        return false;
    }

    public boolean removeReservation(Reservation reservation){
        ReservationDAO reservationDAO = new ReservationDAO();
        ListOfReservations listOfReservations = reservationDAO.getReservations();

        if(!listOfReservations.haveReservation(reservation)){
            return false;
        }

        if(reservation.getHirer()!=this.hirer){
            return false;
        }

        reservationDAO.removeReservation(this.hirer.getUserCode(), reservation.getItem().getCode(), reservation.getStoragePlace().toString());
        return true;
    }

    public ArrayList<Lending> getLendings(){
        LendingDAO lendingDAO = new LendingDAO();
        return lendingDAO.getLendings().getLendingsByHirer(this.hirer);
    }

    public ArrayList<Reservation> getReservation(){
        ReservationDAO reservationDAO = new ReservationDAO();
        return reservationDAO.getReservations().getReservationsByHirer(this.hirer);
    }

    public ListOfReservations getListOfReservation() {
        ReservationDAO reservationDAO = new ReservationDAO();
        return reservationDAO.getReservations();
    }

    public ListOfLendings getListOfLending() {
        LendingDAO lendingDAO = new LendingDAO();
        return lendingDAO.getLendings();
    }
}
