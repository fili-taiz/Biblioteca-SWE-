package domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admin extends SoftwareUser{
    private Library working_place;
    private Catalogue catalogue;
    private ListOfHirers list_of_hirers;

    public Admin(String user_code, String username, String name, String surname, String e_mail, String telephone_number, Library working_place, Catalogue catalogue, ListOfHirers list_of_hirers, UserProfile user_profile) {
        super(user_code, username, name, surname, e_mail, telephone_number, user_profile);
        this.working_place = working_place;
        this.catalogue = catalogue;
        this.list_of_hirers = list_of_hirers;
    }


    public void addItem(Item item) {
        this.catalogue.addItem(item);
    }

    public void removeItem(Item item) {
        this.catalogue.removeItem(item);
    }


    public void addHirer(Hirer hirer) {
        list_of_hirers.addhirer(hirer);
    }

    public void registerLending(Hirer hirer, Item item) {
        if (item.getNumberOfAvailableCopies(this.working_place, item) > 1 && item.isBorrowable()) {
            Lending lending = new Lending(LocalDate.now(), hirer, item, this.working_place);
            hirer.getLendings().add(lending);
            item.addLending(lending);
        }

    }

    public boolean confirmReservationWithdraw(Hirer hirer, Item item) {
        if (item.getNumberOfAvailableCopies(this.working_place, item) > 1 && item.isBorrowable()) {
            ArrayList<Reservation> rs = hirer.getReservations();
            for (Reservation r : rs) {
                if(r.getItem().equals(item) && r.getHirer().equals(hirer) && r.getStoragePlace().equals(this.working_place)){
                    rs.remove(r);
                    item.removeReservation(r);
                    registerLending(hirer, item);
                    return true;

                }
            }
        }
        return false;
    }

    public void registerItemReturn(Lending l) {
        l.getItem().removeLending(l);
        l.getHirer().getLendings().remove(l);
        l = null;
    }

    public void updateItem(Item original_item, Item new_item){
        original_item.updateItem(new_item);
    }

    public ArrayList<Item> searchItem(String keyword, Category category){
        return this.catalogue.searchItem(keyword, category);
    }

}

