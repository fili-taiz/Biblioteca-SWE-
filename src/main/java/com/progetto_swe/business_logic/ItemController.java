package com.progetto_swe.business_logic;

import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class ItemController {


    public ArrayList<Item> searchItem(String keywords) {
        ArrayList<Item> items = getAllItems();

        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : items) {
            for (String keyword : splittedKeyword) {
                if (i.contains(keyword)) {
                    result.add(i);
                }
            }
        }
        return result;
    }


    private ArrayList<Item> getAllItems() {
        BookDAO bookDAO = new BookDAO();
        MagazineDAO magazineDAO = new MagazineDAO();
        ThesisDAO thesisDAO = new ThesisDAO();
        ArrayList<Item> items = new ArrayList<>();
        items.addAll(bookDAO.getAllBooks());
        items.addAll(magazineDAO.getAllMagazines());
        items.addAll(thesisDAO.getAllThesis());

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        for (Item i : items) {
            i.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(i.getCode()));
        }
        return items;
    }


    public ArrayList<Item> advanceSearchItem(String keywords, String category, String language, boolean borrowable, String startDate, String endDate) {
        ArrayList<Item> items = getAllItems();
        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : items) {
            for (String keyword : splittedKeyword) {
                if (i.getCategory().equals(Category.valueOf(category)) &&
                        i.getLanguage().equals(Language.valueOf(language)) &&
                        i.isBorrowable() == borrowable &&
                        i.getPublicationDate().isAfter(LocalDate.parse(startDate)) &&
                        i.getPublicationDate().isBefore(LocalDate.parse(endDate)) &&
                        i.contains(keyword)) {
                    result.add(i);
                }
            }
        }
        return result;
    }


    public void addBook(String title, String publicationDate, String language, String category, String link, String isbn, String publishingHouse,
                        int numberOfPages, String authors, int numberOfCopies, boolean borrowable, Token token) {

        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }

        Book bookCopy = new Book(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link,
                isbn, publishingHouse, numberOfPages, authors);
        BookDAO bookDAO = new BookDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if (i.sameField(bookCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                return;
            }
        }

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();
            int itemCode = bookDAO.addBook(
                    title,
                    publicationDate,
                    language,
                    category,
                    link,
                    isbn,
                    publishingHouse,
                    numberOfPages,
                    authors);
            if (numberOfCopies >= 0) {
                PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
                physicalCopiesDAO.addPhysicalCopies(itemCode, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
            }
            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }

    public void addMagazine(String title,
                            String publicationDate,
                            String language,
                            String category,
                            String link,
                            int numberOfPages,
                            String publishingHouse,
                            int numberOfCopies,
                            boolean borrowable,
                            Token token) {

        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }

        Magazine magazineCopy = new Magazine(
                title,
                LocalDate.parse(publicationDate),
                Language.valueOf(language),
                Category.valueOf(category),
                link,
                numberOfPages,
                publishingHouse);
        MagazineDAO magazineDAO = new MagazineDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if (i.sameField(magazineCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                return;
            }
        }

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();
            int itemCode = magazineDAO.addMagazine(
                    title,
                    publicationDate,
                    language,
                    category,
                    link,
                    publishingHouse,
                    numberOfPages);
            if (numberOfCopies >= 0) {
                PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
                physicalCopiesDAO.addPhysicalCopies(itemCode, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
            }
            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }

    public void addThesis(String title,
                          String publicationDate,
                          String language,
                          String category,
                          String link,
                          int numberOfPages,
                          String author,
                          String supervisors,
                          String university,
                          int numberOfCopies,
                          boolean borrowable,
                          Token token) {

        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        if (borrowable) {
            throw new ActionDeniedException("Errore: un Thesis non può essere noleggiato.");
        }

        Thesis thesisCopy = new Thesis(
                title,
                LocalDate.parse(publicationDate),
                Language.valueOf(language),
                Category.valueOf(category),
                link,
                numberOfPages,
                author,
                supervisors,
                university);
        ThesisDAO thesisDAO = new ThesisDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if (i.sameField(thesisCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                return;
            }
        }

        //gestione transazione
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();
            int itemCode = thesisDAO.addThesis(
                    title,
                    publicationDate,
                    language,
                    category,
                    link,
                    numberOfPages,
                    author,
                    supervisors,
                    university);
            if (numberOfCopies > 0) {
                PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
                physicalCopiesDAO.addPhysicalCopies(itemCode, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
            }
            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }

    private void addPhysicalCopies(int itemCode, Library storagePlace, int numberOfCopies, boolean borrowable) {
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        int physicalCopies = physicalCopiesDAO.getPhysicalCopies(itemCode).get(storagePlace).getNumberOfPhysicalCopies();
        if (physicalCopies == 0) {
            physicalCopiesDAO.addPhysicalCopies(itemCode, storagePlace.toString(), numberOfCopies, borrowable);
        } else {
            physicalCopiesDAO.updatePhysicalCopies(itemCode, storagePlace.toString(), (physicalCopies + numberOfCopies), borrowable);
        }
    }


    public void removeBook(int itemCode, Token token) {
        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.getBook(itemCode);
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        book.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(itemCode));

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();
            if (book.getNumberOfLibraries() == 0) {
                if (book.getLink().isEmpty()) {
                    bookDAO.removeBook(itemCode);
                }
            } else {
                if (book.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) == 0) {
                    throw new ActionDeniedException("Errore: questo Book con itemCode [" + itemCode + "] non è presente nella tua sede [" + token.getTokenWorkingPlace() + "]");
                }
                if (book.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) !=
                        book.getNumberOfAvailableCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace()))) {
                    throw new ActionDeniedException("Errore: questo Book con itemCode [" + itemCode + "] ha ancora prenotazioni/prestiti nella tua sede [" +
                            token.getTokenWorkingPlace() + "]");
                }
                physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
            }
            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }


    public void removeMagazine(int itemCode, Token token) {
        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        MagazineDAO magazineDAO = new MagazineDAO();
        Magazine magazine = magazineDAO.getMagazine(itemCode);
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        magazine.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(itemCode));

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();
            if (magazine.getNumberOfLibraries() == 0) {
                if (magazine.getLink().isEmpty()) {
                    magazineDAO.removeMagazine(itemCode);
                }
            } else {
                if (magazine.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) == 0) {
                    throw new ActionDeniedException("Errore: questo Magazine con itemCode [" + itemCode + "] non è presente nella tua sede [" + token.getTokenWorkingPlace() + "]");
                }
                if (magazine.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) != magazine.getNumberOfAvailableCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace()))) {
                    throw new ActionDeniedException("Errore: questo Magazine con itemCode [" + itemCode + "] ha ancora prenotazioni/prestiti nella tua sede [" + token.getTokenWorkingPlace() + "]");
                }
                physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
            }
            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }


    public void removeThesis(int itemCode, Token token) {
        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        ThesisDAO thesisDAO = new ThesisDAO();
        Thesis thesis = thesisDAO.getThesis(itemCode);
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        thesis.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(itemCode));

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();
            if (thesis.getNumberOfLibraries() == 0) {
                if (thesis.getLink().isEmpty()) {
                    thesisDAO.removeThesis(itemCode);
                }
            } else {
                if (thesis.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) == 0) {
                    throw new ActionDeniedException("Errore: questo Thesis con itemCode [" + itemCode + "] non è presente nella tua sede [" + token.getTokenWorkingPlace() + "]");
                }
                if (thesis.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) != thesis.getNumberOfAvailableCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace()))) {
                    throw new ActionDeniedException("Errore: questo Thesis con itemCode [" + itemCode + "] ha ancora prenotazioni/prestiti nella tua sede [" + token.getTokenWorkingPlace() + "]");
                }
                physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
            }
            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }


    public void updateBook(int itemCode,
                           String title,
                           String publicationDate,
                           boolean borrowable,
                           String language,
                           String category,
                           String link,
                           String isbn,
                           String publishingHouse,
                           int numberOfPages,
                           String authors,
                           int numberOfCopies,
                           Token token) {

        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        LocalDate.parse(publicationDate);
        Language.valueOf(language);
        Category.valueOf(category);

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();

            BookDAO bookDAO = new BookDAO();
            bookDAO.updateBook(
                    itemCode,
                    title,
                    publicationDate,
                    language,
                    category,
                    link,
                    isbn,
                    publishingHouse,
                    authors,
                    numberOfPages);

            if (numberOfCopies > 0) {
                PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
                physicalCopiesDAO.updatePhysicalCopies(itemCode, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
            }

            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }

    public void updateMagazine(int itemCode,
                               String title,
                               String publicationDate,
                               boolean borrowable,
                               String language,
                               String category,
                               String link,
                               String publishingHouse,
                               int numberOfCopies,
                               Token token,
                               int numberOfPages) {

        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        LocalDate.parse(publicationDate);
        Language.valueOf(language);
        Category.valueOf(category);


        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();

            MagazineDAO magazineDAO = new MagazineDAO();
            magazineDAO.updateMagazine(
                    itemCode,
                    title,
                    publicationDate,
                    language,
                    category,
                    link,
                    publishingHouse,
                    numberOfPages);

            if (numberOfCopies > 0) {
                PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
                physicalCopiesDAO.updatePhysicalCopies(itemCode, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
            }

            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }

    public void updateThesis(int itemCode,
                             String title,
                             String publicationDate,
                             boolean borrowable,
                             String language,
                             String category,
                             String link,
                             String author,
                             String supervisors,
                             String univeristy,
                             int numberOfCopies,
                             Token token,
                             int numberOfPages) {
        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        if (borrowable) {
            throw new ActionDeniedException("Errore: un Thesis non può essere noleggiato.");
        }
        LocalDate.parse(publicationDate);
        Language.valueOf(language);
        Category.valueOf(category);

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        try {
            connectionManager.closeAutoCommit();

            ThesisDAO thesisDAO = new ThesisDAO();
            thesisDAO.updateThesis(
                    itemCode,
                    title,
                    publicationDate,
                    language,
                    category,
                    link,
                    author,
                    supervisors,
                    univeristy,
                    numberOfPages);

            if (numberOfCopies > 0) {
                PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
                physicalCopiesDAO.updatePhysicalCopies(itemCode, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
            }

            connectionManager.commit();
            connectionManager.openAutoCommit();
        } catch (Exception e) {
            connectionManager.rollback();
            connectionManager.openAutoCommit();
            throw e;
        }
    }
}
