package com.progetto_swe.business_logic;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.CatalogueDAO;
import com.progetto_swe.orm.LendingDAO;
import com.progetto_swe.orm.ReservationDAO;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

public class AnonymousUserController {
    private Connection connection;

    public AnonymousUserController(Connection connection){
        this.connection = connection;
    }
    public ArrayList<Item> searchItem(String keywords, Category category) {
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        return catalogue.searchItem(keywords, category);
    }

    public ArrayList<Item> advanceSearchItem(String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate) {
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        return catalogue.advancedSearchItem(keywords, category, language, borrowable, startDate, endDate);
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
