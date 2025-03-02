import java.time.LocalDate;

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

    public boolean modify_item(int i, Item oldItem, String newCode, String newTitle, LocalDate newPublicationDate, boolean newIsBorrowable, String newLanguage, String newCategory, String newLink, String newPublishingHouseMagazine, String newAuthor, String newSupervisors, String newUniversity, String newIsbn, String newPublishingHouseBook, int newNumberOfPages, String newAuthors) { // i = 1 Magazine, i = 2 Thesis, i = 3 Book
        switch (i) {
            case 1:
                Magazine oldMagazine = (Magazine) oldItem;
                Magazine newMagazine = new Magazine(newCode, newTitle, newPublicationDate, newLanguage, newCategory, newLink, newIsBorrowable, newPublishingHouseMagazine);
                oldMagazine.update_magazine(newMagazine);
                return true;
            case 2:
                Thesis oldThesis = (Thesis) oldItem;
                Thesis newThesis = new Thesis(newCode, newTitle, newPublicationDate, newLanguage, newCategory, newLink, newIsBorrowable, newAuthor, newSupervisors, newUniversity);
                oldThesis.update_thesis(newThesis);
                return true;
            case 3:
                Book oldBook = (Book) oldItem;
                Book newBook = new Book(newCode, newTitle, newPublicationDate, newLanguage, newCategory, newLink, newIsBorrowable, newIsbn, newPublishingHouseBook, newNumberOfPages, newAuthors);
                oldBook.update_book(newBook);
                return true;
        }

        return false;
    }

    public void add_item(Item item) {
        this.catalogue.add_item(item);
    }

    public void remove_item(Item item) {
        this.catalogue.remove_item(item);
    }

    public void get_item(Item item) {
        this.catalogue.get_item(item);
    }

    public void set_number_of_copies(Item item, int newN) {
        for (Tupla t : item.getPhysicalCopies()) {
            if (t.get_first().equals(this.workingPlace)) {
                t.set_second(newN);
            }
        }
    }


    public void increase_number_of_copies(Item item) {
        for (Tupla t : item.getPhysicalCopies()) {
            if (t.get_first().equals(this.workingPlace)) {
                int n = (int) t.get_second();
                t.set_second(n + 1);
            }
        }
    }


    public void decrease_number_of_copies(Item item) {
        for (Tupla t : item.getPhysicalCopies()) {
            if (t.get_first().equals(this.workingPlace)) {
                int n = (int) t.get_second();
                t.set_second(n - 1);
            }
        }
    }


    public void add_hirer(Hirer hirer) {
        list_of_hirers.addhirer(hirer);
    }

    public void remove_hirer(Hirer hirer) {
        list_of_hirers.removehirer(hirer);
    }

    public Hirer get_hirer(int code) {
        return list_of_hirers.hirers.get(code);
    }

    public void registerLending(Hirer hirer, Item item, LocalDate lendingDate) {
        if (item.get_number_of_available_copies(this.workingPlace) > 1) {
            Lending lending = new Lending(lendingDate, hirer, item, this.workingPlace);
            hirer.lendings.add(lending);
        }

    }

    public void confirm_reservation(Hirer hirer, Item item, LocalDate reservationDate) {
        if (item.get_number_of_available_copies(this.workingPlace) > 1) {
            Reservation reservation = new Reservation(reservationDate, hirer, item, this.workingPlace);
            hirer.reservations.add(reservation);
        }
    }

    public void registerItemReturn(Lending l) {


    }
}

