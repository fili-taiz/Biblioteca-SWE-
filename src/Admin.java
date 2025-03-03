import java.time.LocalDate;
import java.util.ArrayList;

public class Admin {
    private final String userCode;
    private final String username;
    private final String name;
    private final String surname;
    private final String eMail;
    private final String telephoneNumber;
    private final Biblioteca workingPlace;
    private final Catalogue catalogue;
    private final ListOfHirers list_of_hirers;

    public Admin(String userCode, String username, String name, String surname, String eMail, String telephoneNumber, Biblioteca workingPlace, Catalogue catalogue, ListOfHirers list_of_hirers) {
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

    public boolean modifyItem(int i, Item oldItem, String newCode, String newTitle, LocalDate newPublicationDate, boolean newIsBorrowable, String newLanguage, String newCategory, String newLink, String newPublishingHouseMagazine, String newAuthor, String newSupervisors, String newUniversity, String newIsbn, String newPublishingHouseBook, int newNumberOfPages, String newAuthors) { // i = 1 Magazine, i = 2 Thesis, i = 3 Book
        switch (i) {
            case 1:
                Magazine oldMagazine = (Magazine) oldItem;
                Magazine newMagazine = new Magazine(newCode, newTitle, newPublicationDate, newLanguage, newCategory, newLink, newIsBorrowable, newPublishingHouseMagazine);
                oldMagazine.updateMagazine(newMagazine);
                return true;
            case 2:
                Thesis oldThesis = (Thesis) oldItem;
                Thesis newThesis = new Thesis(newCode, newTitle, newPublicationDate, newLanguage, newCategory, newLink, newIsBorrowable, newAuthor, newSupervisors, newUniversity);
                oldThesis.updateThesis(newThesis);
                return true;
            case 3:
                Book oldBook = (Book) oldItem;
                Book newBook = new Book(newCode, newTitle, newPublicationDate, newLanguage, newCategory, newLink, newIsBorrowable, newIsbn, newPublishingHouseBook, newNumberOfPages, newAuthors);
                oldBook.updateBook(newBook);
                return true;
        }

        return false;
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
        for(Biblioteca b : item.getPhysicalCopies().keySet()){
            if(b == this.workingPlace){
                item.setNumberOfCopies(b, newN);
            }
        }
    }


    public void increaseNumberOfCopies(Item item) {
        for(Biblioteca b : item.getPhysicalCopies().keySet()){
            if(b == this.workingPlace){
                item.setNumberOfCopies(b, item.getPhysicalCopies().get(b)+1);
            }
        }
    }


    public void decreaseNumberOfCopies(Item item) {
        for(Biblioteca b : item.getPhysicalCopies().keySet()){
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

    public void confirmReservation(Hirer hirer, Item item, LocalDate reservationDate) {
        if (item.getNumberOfAvailableCopies(this.workingPlace, item) > 1) {
            Reservation reservation = new Reservation(reservationDate, hirer, item, this.workingPlace);
            hirer.getReservations().add(reservation);
        }
    }

    public void registerItemReturn(Lending l) {
        l.getItem().removeLending(l);
        l.getHirer().getLendings().remove(l);

    }
}

