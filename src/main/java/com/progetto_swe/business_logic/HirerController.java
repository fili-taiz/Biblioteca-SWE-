package com.progetto_swe.business_logic;

import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.CatalogueDAO;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.orm.LendingDAO;
import com.progetto_swe.orm.ReservationDAO;

public class HirerController {
    Hirer hirer;

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


    public boolean reserveItem(Item item, Library storagePlace){
        if(this.hirer.getUnbannedDate() != null){
            return false;
        }
        ReservationDAO reservationDAO = new ReservationDAO();
        return reservationDAO.addReservation(this.hirer.getUserCode(), item.getCode(), storagePlace.name());
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
