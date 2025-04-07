package com.progetto_swe.business_logic;

import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.orm.ReservationDAO;

public class HirerController {
    Hirer hirer;

    public HirerController(Hirer hirer){
        this.hirer = hirer;
    }

    public ArrayList<Item> searchItem(String keyWords, Category category ){
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.refreshCatalogue();
        return hirer.searchItem(keyWords, category);
    }

    public ArrayList<Item> advanceSearchItem(String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate){
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.refreshCatalogue();
        return hirer.advanceSearchItem(keywords, category, language, borrowable, startDate, endDate);
    }


    public boolean reserveItem(Item item, Library storagePlace){
        if(this.hirer.getUnbannedDate() != null){
            return false;
        }
        ReservationDAO reservationDAO = new ReservationDAO();
        reservationDAO.addReservation(this.hirer.getUserCode(), item.getCode(), storagePlace.name());
        return this.hirer.reservePhysicalCopy(item, storagePlace);
    }

    public ArrayList<Lending> getLendings(){
        HirerDAO hirerDAO = new HirerDAO();
        return hirerDAO.getLendings(this.hirer.getUserCode());
    }

    public ArrayList<Reservation> getReservation(){
        HirerDAO hirerDAO = new HirerDAO();
        return hirerDAO.getReservations(this.hirer.getUserCode());
    }
}
