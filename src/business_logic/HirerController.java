package business_logic;

import domain_model.*;
import java.util.ArrayList;

public class HirerController {
    Hirer hirer;

    public HirerController(Hirer hirer){
        this.hirer = hirer;
    }

    public ArrayList<Item> searchItem(String keyWord){
        return hirer.searchItem(keyWord, "ALL", false, false);
    }

    public ArrayList<Item> searchItem(String keyWord, String category, boolean dateSort, boolean asc){
        return hirer.searchItem(keyWord, category, dateSort, asc);
    }

    public boolean reserveItem(Hirer hirer, Item item, Biblioteca storagePlace){
        return hirer.reservePhysicalCopy(item, storagePlace);
    }

    public ArrayList<Lending> getLendings(Hirer hirer){
        return hirer.getLendings();
    }

    public ArrayList<Reservation> getReservations(Hirer hirer){
        return hirer.getReservations();
    }




}
