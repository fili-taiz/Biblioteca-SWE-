package com.progetto_swe.business_logic;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Book;
import com.progetto_swe.domain_model.Catalogue;
import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import com.progetto_swe.domain_model.Lending;
import com.progetto_swe.domain_model.Magazine;
import com.progetto_swe.domain_model.Reservation;
import com.progetto_swe.domain_model.Thesis;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.orm.LendingDAO;
import com.progetto_swe.orm.MagazineDAO;
import com.progetto_swe.orm.ReservationDAO;
import com.progetto_swe.orm.ThesisDAO;

public class AdminController {

    Admin admin;

    public AdminController(Admin admin) {
        this.admin = admin;
    }

    /* 
    private Admin refreshAdmin(){
        AdminDAO adminDAO = new AdminDAO();
        Admin newAdmin = adminDAO.getAdmin(admin.getUserCode());
        newAdmin.setUserProfile(this.admin.getUserProfile());
        this.admin = newAdmin;
    }
     */
    private void refreshCatalogue() {
        AdminDAO adminDAO = new AdminDAO();
        this.admin.setCatalogue(adminDAO.refreshCatalogue());
    }

    private void refreshHirers() {
        AdminDAO adminDAO = new AdminDAO();
        this.admin.setHirers(adminDAO.refreshHirers());
    }

    /*da riguardare i parametri */ //la password non è inserita dall'utente è il codice di verifica dell'email ottenuto in fase di registrazione
    public boolean registerExternalHirer(String password, String name, String surname, String eMail, String telephoneNumber) {
        HirerDAO hirerDAO = new HirerDAO();
        String userCode = hirerDAO.addExternalHirer(name, password, surname, eMail, telephoneNumber);
        if (userCode.equals("")) {
            return false;
        }
        return admin.addHirer(hirerDAO.getHirer(userCode));
    }

    //controllo validità parametri;
    //da spostare nella cli
    private boolean checkParametersValidity(String publicationDate, String language, String category, String borrowable) {
        //convalida data
        try {
            LocalDate.parse(publicationDate);
        } catch (DateTimeParseException e) {
            return false;
        }

        //convalida borrowable
        try {
            Boolean.valueOf(borrowable);
        } catch (IllegalArgumentException e) {
            return false;
        }

        //convalida lingua
        try {
            Language.valueOf(language);
        } catch (IllegalArgumentException e) {
            return false;
        }

        //convalida categoria
        try {
            Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    public boolean addBook(String title, String publicationDate, String borrowable, String language, String category, String link, String isbn,
            String publishingHouse, int numberOfPages, String authors, int numberOfCopies) {
        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category, borrowable)) {
            return false;
        }

        BookDAO bookDAO = new BookDAO();
        int code = bookDAO.addBook(title, publicationDate, borrowable, language, category, link, isbn, publishingHouse, numberOfPages, authors,
                this.admin.getWorkingPlace().name(), numberOfCopies);
        if (code > - 1) {
            Item newItem = new Book(code, title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link,
                    Boolean.parseBoolean(borrowable), isbn, publishingHouse, numberOfPages, authors);
            return this.admin.addItem(newItem, numberOfCopies);
        }
        return false;
    }

    public boolean addMagazine(String title, String publicationDate, String language, String category, String link, String borrowable, String publishingHouse,
            int numberOfCopies) {
        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category, borrowable)) {
            return false;
        }

        MagazineDAO magazineDAO = new MagazineDAO();
        int code = magazineDAO.addMagazine(title, publicationDate, language, category, link, borrowable, publishingHouse, this.admin.getWorkingPlace().name(), numberOfCopies);
        if (code > -1) {
            return this.admin.addItem(new Magazine(code, title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, Boolean.parseBoolean(borrowable), publishingHouse), numberOfCopies);
        }
        return false;
    }

    public boolean addThesis(String title, String publicationDate, String borrowable, String language, String category, String link, String author,
            String supervisors, String university, int numberOfCopies) {
        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category, borrowable)) {
            return false;
        }

        ThesisDAO thesisDAO = new ThesisDAO();
        int code = thesisDAO.addThesis(title, publicationDate, borrowable, language, category, link, author, supervisors, university,
                this.admin.getWorkingPlace().name(), numberOfCopies);
        if (code > -1) {
            return this.admin.addItem(new Thesis(code, title, LocalDate.parse(publicationDate), Boolean.parseBoolean(borrowable), Language.valueOf(language), Category.valueOf(category),
                    link, author, supervisors, university), numberOfCopies);
        }
        return false;
    }

    public boolean removeBook(int code) {
        BookDAO bookDAO = new BookDAO();
        if (bookDAO.removeBook(code, this.admin.getWorkingPlace().name())) {
            return this.admin.removeItem(code);
        }
        return false;
    }

    public boolean removeMagazine(int code) {
        MagazineDAO magazineDAO = new MagazineDAO();
        if (magazineDAO.removeMagazine(code, this.admin.getWorkingPlace().name())) {
            return this.admin.removeItem(code);
        }
        return false;
    }

    public boolean removeThesis(int code) {
        ThesisDAO thesisDAO = new ThesisDAO();
        if (thesisDAO.removeThesis(code, this.admin.getWorkingPlace().name())) {
            return this.admin.removeItem(code);
        }
        return false;
    }

    public boolean updateBook(int originalItemCode, String title, String publicationDate, String borrowable, String language, String category, String link,
            String isbn, String publishingHouse, int numberOfPages, String authors, int numberOfCopies) {
        BookDAO bookDAO = new BookDAO();
        if (bookDAO.updateBook(originalItemCode, title, publicationDate, borrowable, language, category, link, isbn, publishingHouse, numberOfPages, authors,
                this.admin.getWorkingPlace().name(), numberOfCopies)) {

            Item originalItem = this.admin.getItem(originalItemCode);
            Item newItem = new Book(originalItemCode, title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category),
                    link, Boolean.parseBoolean(borrowable), isbn, publishingHouse, numberOfPages, authors);

            return admin.updateItem(originalItem, newItem, numberOfCopies);
        }
        return false;
    }

    public boolean updateMagazine(int originalItemCode, String title, String publicationDate, String language, String category, String link, String borrowable, String publishingHouse, int numberOfCopies) {
        MagazineDAO magazineDAO = new MagazineDAO();
        if (magazineDAO.updateMagazine(originalItemCode, title, publicationDate, language, category, link, borrowable, publishingHouse,
                this.admin.getWorkingPlace().name(), numberOfCopies)) {

            Item originalItem = this.admin.getItem(originalItemCode);
            Item newItem = new Magazine(originalItemCode, title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category),
                    link, Boolean.parseBoolean(borrowable), publishingHouse);
            return admin.updateItem(originalItem, newItem, numberOfCopies);
        }
        return false;
    }

    public boolean updateThesis(int originalItemCode, String title, String publicationDate, String borrowable, String language, String category,
            String link, String author, String supervisors, String university, int numberOfCopies) {
        ThesisDAO thesisDAO = new ThesisDAO();
        if (thesisDAO.updateThesis(originalItemCode, title, publicationDate, borrowable, language, category, link, author, supervisors, university,
                this.admin.getWorkingPlace().name(), numberOfCopies)) {
            Item originalItem = this.admin.getItem(originalItemCode);
            Item newItem = new Thesis(originalItemCode, title, LocalDate.parse(publicationDate), Boolean.parseBoolean(borrowable), Language.valueOf(language),
                    Category.valueOf(category), link, author, supervisors, university);
            admin.updateItem(originalItem, newItem, numberOfCopies);
        }
        return false;
    }

    public boolean registerLending(Hirer hirer, Item item) {
        if (!item.isBorrowable()) {
            return false;
        }

        LendingDAO lendingDAO = new LendingDAO();
        if (lendingDAO.addLending(hirer.getUserCode(), item.getCode(), this.admin.getWorkingPlace().name())) {
            admin.registerLending(hirer, item);
        }
        return false;
    }

    public boolean confirmReservationWithdraw(Reservation reservation) {
        LendingDAO lendingDAO = new LendingDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        Hirer hirer = reservation.getHirer();
        Item item = reservation.getItem();
        /*cancella reservation */
        if (!reservationDAO.removeReservation(hirer.getUserCode(), item.getCode(), this.admin.getWorkingPlace().name())) {
            return false;
        }
        admin.confirmReservationWithdraw(reservation);
        return lendingDAO.addLending(hirer.getUserCode(), item.getCode(), this.admin.getWorkingPlace().name());
    }

    public boolean registerItemReturn(Lending lending) {
        LendingDAO lendingDAO = new LendingDAO();
        /*cancella reservation */
        if (lendingDAO.removeLending(lending.getHirer().getUserCode(), lending.getItem().getCode(), lending.getStoragePlace().name())) {
            admin.registerItemReturn(lending);
        }
        return false;
    }

    public ArrayList<Item> searchItem(String keyWords, String category) {
        AdminDAO adminDAO = new AdminDAO();
        adminDAO.refreshCatalogue();
        return admin.searchItem(keyWords, Category.valueOf(category));
    }

    public ArrayList<Hirer> searchHirer(String keyWords) {

        return admin.searchHirer(keyWords);
    }

}

/**
 *
 *
 *
 *
 *         //spostare nella cli //creazione stringa contenente tutti gli autori (ordine
 * alfabetico) authors.sort(String::compareTo); String concatenateAuthors = "";
 * for (String author : authors) { concatenateAuthors += author + ", "; }
 * concatenateAuthors = concatenateAuthors.substring(0,
 * concatenateAuthors.length() - 1);
 *
 *
 *
 *
 *
 *
 *         //spostare nella cli //creazione stringa contenente tutti gli autori (ordine
 * alfabetico) supervisors.sort(String::compareTo); String
 * concatenateSupervisors = ""; for (String supervisor : supervisors) {
 * concatenateSupervisors += supervisor + ", "; } concatenateSupervisors =
 * concatenateSupervisors.substring(0, concatenateSupervisors.length() - 1);
 */
