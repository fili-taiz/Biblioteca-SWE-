package com.progetto_swe.business_logic;

import java.sql.Statement;
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
import com.progetto_swe.orm.*;

public class AdminController {

    Admin admin;

    public AdminController(Admin admin) {
        this.admin = admin;
    }

    //la password non è inserita dall'utente è il codice di verifica dell'email ottenuto in fase di registrazione
    public boolean registerExternalHirer(String password, String name, String surname, String eMail, String telephoneNumber) {
        HirerDAO hirerDAO = new HirerDAO();
        String userCode = "";
        do { //generazione codice univoco per chiave primaria con prefisso E per non occupare future possibili matricole
            userCode = "E" + Math.round((Math.random() * 1000000));
        } while (hirerDAO.getHirer(userCode) != null);

        return hirerDAO.addHirer(userCode, name, password, surname, eMail, telephoneNumber);
    }


    public boolean addBook(String title, String publicationDate, String borrowable, String language, String category, String link, String isbn,
                           String publishingHouse, int numberOfPages, String authors, int numberOfCopies) {
        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category, borrowable)) {
            return false;
        }
        Book booksCopy = new Book(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, Boolean.getBoolean(borrowable), isbn, publishingHouse, numberOfPages, authors);
        ItemDAO itemDAO = new ItemDAO();
        BookDAO bookDAO = new BookDAO();

        try{
            int code = -1;
            if(!this.admin.getCatalogue().contains(booksCopy)){
                code = itemDAO.addItem(title, publicationDate, language, category, link, borrowable);
                bookDAO.addBook(code, isbn, publishingHouse, numberOfPages, authors);
            }
            if (this.admin.getCatalogue().getItem(code) == null){

            }
        } catch (Exception e) {

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
        magazineDAO.addMagazine(title, publicationDate, language, category, link, borrowable, publishingHouse, this.admin.getWorkingPlace().name(), numberOfCopies);
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
        thesisDAO.addThesis(title, publicationDate, borrowable, language, category, link, author, supervisors, university,
                this.admin.getWorkingPlace().name(), numberOfCopies);
        return false;
    }

    public boolean removeBook(int code) {
        BookDAO bookDAO = new BookDAO();
        return bookDAO.removeBook(code, this.admin.getWorkingPlace().name());
    }

    public boolean removeMagazine(int code) {
        MagazineDAO magazineDAO = new MagazineDAO();
        return magazineDAO.removeMagazine(code, this.admin.getWorkingPlace().name());
    }

    public boolean removeThesis(int code) {
        ThesisDAO thesisDAO = new ThesisDAO();
        return thesisDAO.removeThesis(code, this.admin.getWorkingPlace().name());
    }

    public boolean updateBook(int originalItemCode, String title, String publicationDate, String borrowable, String language, String category, String link,
                              String isbn, String publishingHouse, int numberOfPages, String authors, int numberOfCopies) {
        BookDAO bookDAO = new BookDAO();
        return bookDAO.updateBook(originalItemCode, title, publicationDate, borrowable, language, category, link, isbn, publishingHouse,
                numberOfPages, authors, this.admin.getWorkingPlace().name(), numberOfCopies);
    }

    public boolean updateMagazine(int originalItemCode, String title, String publicationDate, String language, String category, String link, String borrowable, String publishingHouse, int numberOfCopies) {
        MagazineDAO magazineDAO = new MagazineDAO();
        return magazineDAO.updateMagazine(originalItemCode, title, publicationDate, language, category, link, borrowable, publishingHouse,
                this.admin.getWorkingPlace().name(), numberOfCopies);
    }

    public boolean updateThesis(int originalItemCode, String title, String publicationDate, String borrowable, String language, String category,
                                String link, String author, String supervisors, String university, int numberOfCopies) {
        ThesisDAO thesisDAO = new ThesisDAO();
        return thesisDAO.updateThesis(originalItemCode, title, publicationDate, borrowable, language, category, link, author, supervisors, university,
                this.admin.getWorkingPlace().name(), numberOfCopies);
    }

    public boolean registerLending(Hirer hirer, Item item) {
        if (!item.isBorrowable()) {
            return false;
        }

        LendingDAO lendingDAO = new LendingDAO();
        return lendingDAO.addLending(hirer.getUserCode(), item.getCode(), this.admin.getWorkingPlace().name());
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

}

/**
 * //spostare nella cli //creazione stringa contenente tutti gli autori (ordine
 * alfabetico) authors.sort(String::compareTo); String concatenateAuthors = "";
 * for (String author : authors) { concatenateAuthors += author + ", "; }
 * concatenateAuthors = concatenateAuthors.substring(0,
 * concatenateAuthors.length() - 1);
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * //spostare nella cli //creazione stringa contenente tutti gli autori (ordine
 * alfabetico) supervisors.sort(String::compareTo); String
 * concatenateSupervisors = ""; for (String supervisor : supervisors) {
 * concatenateSupervisors += supervisor + ", "; } concatenateSupervisors =
 * concatenateSupervisors.substring(0, concatenateSupervisors.length() - 1);
 */
