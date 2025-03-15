package com.progetto_swe.business_logic;

import java.util.ArrayList;

import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Lending;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.Reservation;
import com.progetto_swe.orm.HirerDAO;

public class HirerController {
    Hirer hirer;

    public HirerController(Hirer hirer){
        this.hirer = hirer;
    }

    public ArrayList<Item> searchItem(String keyWords, String category ){
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.refreshCatalogue();
        return hirer.searchItem(keyWords, Category.valueOf(category));
    }


    public boolean reserveItem(Hirer hirer, Item item, Library storagePlace){
        return hirer.reservePhysicalCopy(item, storagePlace);
    }

    public ArrayList<Lending> getLendings(Hirer hirer){
        return hirer.getLendings();
    }

    public ArrayList<Reservation> getReservations(Hirer hirer){
        return hirer.getReservations();
    }




}
