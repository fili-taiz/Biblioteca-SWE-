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

    public void modifyItem(Item old_item, Item new_item){
        old_item.updateItem(new_item);
    }

    public void addItem(Item item) {
        this.catalogue.addItem(item);
    }

    public void removeItem(Item item) {
        this.catalogue.removeItem(item);
    }


    public void setNumberOfCopies(Item item, int new_n) {
        for(Library b : item.getPhysicalCopies().keySet()){
            if(b == this.working_place){
                item.setNumberOfCopies(b, new_n);
            }
        }
    }

    public void addHirer(Hirer hirer) {
        list_of_hirers.addhirer(hirer);
    }

    public void registerLending(Hirer hirer, Item item, LocalDate lending_date) {
        if (item.getNumberOfAvailableCopies(this.working_place, item) > 1) {
            Lending lending = new Lending(lending_date, hirer, item, this.working_place);
            hirer.getLendings().add(lending);
        }

    }

    public void confirmReservationWithdraw(Hirer hirer, Item item, LocalDate reservation_date) {
        if (item.getNumberOfAvailableCopies(this.working_place, item) > 1) {
            Reservation reservation = new Reservation(reservation_date, hirer, item, this.working_place);
            hirer.getReservations().add(reservation);
        }
    }

    public void registerItemReturn(Lending l) {
        l.getItem().removeLending(l);
        l.getHirer().getLendings().remove(l);
    }

    public void updateItem(Item original_item, Item new_item){
        original_item.updateItem(new_item);
    }

    public ArrayList<Item> searchItem(String keyword, Category category){
        return this.catalogue.searchItem(keyword, category);
    }


    public Library getWorkingPlace() { return this.working_place; }
    public Catalogue getCatalogue() { return this.catalogue; }
    public ListOfHirers getList_of_hirers() { return this.list_of_hirers; }
    public Item getItem(Item item) {
        return this.catalogue.getItem(item);
    }


    public void setWorkingPlace(Library workingPlace) { this.working_place = workingPlace; }
    public void setCatalogue(Catalogue catalogue) { this.catalogue = catalogue; }
    public void setListOfHirers(ListOfHirers list_of_hirers) { this.list_of_hirers = list_of_hirers; }
}

