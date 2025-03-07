package domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admin {
    private String userCode;
    private String username;
    private String name;
    private String surname;
    private String eMail;
    private String telephoneNumber;
    private Library workingPlace;
    private Catalogue catalogue;
    private ListOfHirers list_of_hirers;

    public Admin(){}

    
    public Admin(String userCode, String username, String name, String surname, String eMail, String telephoneNumber, Library workingPlace, Catalogue catalogue, ListOfHirers list_of_hirers) {
        this.userCode = userCode;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.eMail = eMail;
        this.telephoneNumber = telephoneNumber;
        this.workingPlace = workingPlace;
        this.catalogue = catalogue;
        this.list_of_hirers = list_of_hirers;
    }

    public void modifyItem(Item oldItem, Item newItem){
        oldItem.updateItem(newItem);
    }

    public void addItem(Item item) {
        this.catalogue.addItem(item);
    }

    public void removeItem(Item item) {
        this.catalogue.removeItem(item);
    }

    public void getItem(Item item) {
        this.catalogue.getItem(item);
    }

    public void setNumberOfCopies(Item item, int newN) {
        for(Library b : item.getPhysicalCopies().keySet()){
            if(b == this.workingPlace){
                item.setNumberOfCopies(b, newN);
            }
        }
    }


    public void increaseNumberOfCopies(Item item) {
        for(Library b : item.getPhysicalCopies().keySet()){
            if(b == this.workingPlace){
                item.setNumberOfCopies(b, item.getPhysicalCopies().get(b)+1);
            }
        }
    }


    public void decreaseNumberOfCopies(Item item) {
        for(Library b : item.getPhysicalCopies().keySet()){
            if(b == this.workingPlace){
                item.setNumberOfCopies(b, item.getPhysicalCopies().get(b)-1);
            }
        }
    }


    public void addHirer(Hirer hirer) {
        list_of_hirers.addhirer(hirer);
    }

    public void removeHirer(Hirer hirer) {
        list_of_hirers.removehirer(hirer);
    }

    public Hirer getHirer(int code) {
        return list_of_hirers.hirers.get(code);
    }

    public void registerLending(Hirer hirer, Item item, LocalDate lendingDate) {
        if (item.getNumberOfAvailableCopies(this.workingPlace, item) > 1) {
            Lending lending = new Lending(lendingDate, hirer, item, this.workingPlace);
            hirer.getLendings().add(lending);
        }

    }

    public void confirmReservationWithdraw(Hirer hirer, Item item, LocalDate reservationDate) {
        if (item.getNumberOfAvailableCopies(this.workingPlace, item) > 1) {
            Reservation reservation = new Reservation(reservationDate, hirer, item, this.workingPlace);
            hirer.getReservations().add(reservation);
        }
    }

    public void registerItemReturn(Lending l) {
        l.getItem().removeLending(l);
        l.getHirer().getLendings().remove(l);

    }

    public void updateItem(Item originalItem, Item newItem){
        originalItem.updateItem(newItem);
    }

    public ArrayList<Item> searchItem(String keyWord, String category, boolean dateSort, boolean asc){
        return this.catalogue.searchItem(keyWord, category, dateSort, asc);
    }

    public String getUserCode() { return userCode; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEMail() { return eMail; }
    public String getTelephoneNumber() { return telephoneNumber; }
    public Library getWorkingPlace() { return workingPlace; }
    public Catalogue getCatalogue() { return catalogue; }
    public ListOfHirers getList_of_hirers() { return list_of_hirers; }


    public void setUserCode(String userCode) { this.userCode = userCode; }
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEMail(String eMail) { this.eMail = eMail; }
    public void setTelephoneNumber(String telephoneNumber) { this.telephoneNumber = telephoneNumber; }
    public void setWorkingPlace(Library workingPlace) { this.workingPlace = workingPlace; }
    public void setCatalogue(Catalogue catalogue) { this.catalogue = catalogue; }
    public void setListOfHirers(ListOfHirers list_of_hirers) { this.list_of_hirers = list_of_hirers; }
}

