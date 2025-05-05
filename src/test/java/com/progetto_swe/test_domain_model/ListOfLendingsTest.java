package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ListOfLendingsTest {

    @Test
    public void testConstructor(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "12345",
                ucs, null);
        Book book = new Book(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing_house", 100, "authors");
        Lending lending_1 = new Lending(LocalDate.of(2025,4,2), hirer, book, Library.LIBRARY_1);
        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);

        ListOfLendings listOfLendings = new ListOfLendings(lendings);

        assertEquals(lendings, listOfLendings.getLendings());
    }

    @Test
    public void testGetLendingsByHirer(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "12355",
                ucs_2, null);
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Book book_2 = new Book(2, "title_2", LocalDate.of(2024, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_2", 200, "authors_2");
        Lending lending_1 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.of(2025,4,2), hirer_2, book_2, Library.LIBRARY_1);
        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);
        lendings.add(lending_2);

        ListOfLendings listOfLendings = new ListOfLendings(lendings);

        ArrayList<Lending> result = new ArrayList<>();
        result.add(lending_1);

        assertEquals(result, listOfLendings.getLendingsByHirer(hirer_1));
        assertNotEquals(lendings, listOfLendings.getLendingsByHirer(hirer_2));
    }

    @Test
    public void testGetNumberOfLendingsInLibrary(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "12355",
                ucs_2, null);
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Lending lending_1 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.of(2025,4,2), hirer_2, book_1, Library.LIBRARY_1);
        Lending lending_3 = new Lending(LocalDate.of(2025,4,2), hirer_2, book_1, Library.LIBRARY_2);
        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);
        lendings.add(lending_2);
        lendings.add(lending_3);

        ListOfLendings listOfLendings = new ListOfLendings(lendings);

        assertEquals(2, listOfLendings.getNumberOfLendingsInLibrary(Library.LIBRARY_1, book_1));
        assertNotEquals(3, listOfLendings.getNumberOfLendingsInLibrary(Library.LIBRARY_1, book_1));

    }

    @Test
    public void testLendingExist(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "12355",
                ucs_2, null);
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Book book_2 = new Book(2, "title_2", LocalDate.of(2024, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_2", 200, "authors_2");
        Lending lending_1 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_2, Library.LIBRARY_1);
        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);
        lendings.add(lending_2);

        ListOfLendings listOfLendings = new ListOfLendings(lendings);

        assertTrue(listOfLendings.lendingExist(hirer_1, book_1, Library.LIBRARY_1));
        assertFalse(listOfLendings.lendingExist(hirer_2, book_1, Library.LIBRARY_1));
    }

    @Test
    public void testHaveLending(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Book book_2 = new Book(2, "title_2", LocalDate.of(2024, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_2", 200, "authors_2");
        Lending lending_1 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_2, Library.LIBRARY_1);
        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);

        ListOfLendings listOfLendings = new ListOfLendings(lendings);

        assertTrue(listOfLendings.haveLending(lending_1));
        assertFalse(listOfLendings.haveLending(lending_2));

    }

    @Test
    public void testGetLendingsByItem(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "12355",
                ucs_2, null);
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Book book_2 = new Book(2, "title_2", LocalDate.of(2024, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_2", 200, "authors_2");
        Lending lending_1 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Lending lending_3 = new Lending(LocalDate.of(2025,4,2), hirer_1, book_2, Library.LIBRARY_1);
        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);
        lendings.add(lending_2);
        lendings.add(lending_3);

        ListOfLendings listOfLendings = new ListOfLendings(lendings);

        ArrayList<Lending> result = new ArrayList<>();
        result.add(lending_1);
        result.add(lending_2);

        assertEquals(result, listOfLendings.getLendingsByItem(book_1));
        assertNotEquals(lendings, listOfLendings.getLendingsByItem(book_1));

    }

    @Test
    public void testEquals(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null);
        Item item_1 = new Magazine(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Item item_2 = new Magazine(2, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Lending lending_1 = new Lending(LocalDate.of(2025,5,6), hirer, item_1, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.of(2025,5,6), hirer, item_1, Library.LIBRARY_1);
        Lending lending_3 = new Lending(LocalDate.of(2025,5,6), hirer, item_2, Library.LIBRARY_1);

        ArrayList<Lending> expected_lendings_1 = new ArrayList<>();
        expected_lendings_1.add(lending_1);
        expected_lendings_1.add(lending_2);
        expected_lendings_1.add(lending_3);

        ArrayList<Lending> expected_lendings_2 = expected_lendings_1;

        ArrayList<Lending> unexpected_lendings = new ArrayList<>();
        unexpected_lendings.add(lending_1);

        assertTrue(expected_lendings_1.equals(expected_lendings_2));
        assertFalse(expected_lendings_1.equals(unexpected_lendings));
        assertFalse(expected_lendings_1.equals(null));


    }
}
