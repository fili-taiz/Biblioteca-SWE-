package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ListOfReservationsTest {
    @Test
    public void testConstructor(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "12345",
                ucs, null);
        Book book = new Book(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing_house", 100, "authors");
        Reservation reservation_1 = new Reservation(LocalDate.of(2025,4,2), hirer, book, Library.LIBRARY_1);
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation_1);

        ListOfReservations listOfReservations = new ListOfReservations(reservations);

        assertEquals(reservations, listOfReservations.getReservations());
    }

    @Test
    public void testGetReservationsByHirer(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "12355",
                ucs_2, null);
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Book book_2 = new Book(2, "title_2", LocalDate.of(2024, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_2", 200, "authors_2");
        Reservation reservation_1 = new Reservation(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.of(2025,4,2), hirer_2, book_2, Library.LIBRARY_1);
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation_1);
        reservations.add(reservation_2);

        ListOfReservations listOfReservations = new ListOfReservations(reservations);

        ArrayList<Reservation> result = new ArrayList<>();
        result.add(reservation_1);

        assertEquals(result, listOfReservations.getReservationsByHirer(hirer_1));
        assertNotEquals(reservations, listOfReservations.getReservationsByHirer(hirer_2));
    }

    @Test
    public void testGetNumberOfReservationsInLibrary(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "12355",
                ucs_2, null);
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Reservation reservation_1 = new Reservation(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.of(2025,4,2), hirer_2, book_1, Library.LIBRARY_1);
        Reservation reservation_3 = new Reservation(LocalDate.of(2025,4,2), hirer_2, book_1, Library.LIBRARY_2);
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation_1);
        reservations.add(reservation_2);
        reservations.add(reservation_3);

        ListOfReservations listOfReservations = new ListOfReservations(reservations);

        assertEquals(2, listOfReservations.getNumberOfReservationsInLibrary(Library.LIBRARY_1, book_1));
        assertNotEquals(3, listOfReservations.getNumberOfReservationsInLibrary(Library.LIBRARY_1, book_1));

    }

    @Test
    public void testReservationExist(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "12355",
                ucs_2, null);
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Book book_2 = new Book(2, "title_2", LocalDate.of(2024, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_2", 200, "authors_2");
        Reservation reservation_1 = new Reservation(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.of(2025,4,2), hirer_1, book_2, Library.LIBRARY_1);
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation_1);
        reservations.add(reservation_2);

        ListOfReservations listOfReservations = new ListOfReservations(reservations);

        assertTrue(listOfReservations.reservationExist(hirer_1, book_1, Library.LIBRARY_1));
        assertFalse(listOfReservations.reservationExist(hirer_2, book_1, Library.LIBRARY_1));
    }

    @Test
    public void testHaveReservation(){
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

        ListOfLendings listOfLendings = new ListOfLendings(lendings);

        assertTrue(listOfLendings.haveLending(lending_1));
        assertFalse(listOfLendings.haveLending(lending_2));

    }

    @Test
    public void testGetReservationsByItem(){
        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "12345",
                ucs_1, null);
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Book book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Book book_2 = new Book(2, "title_2", LocalDate.of(2024, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_2", 200, "authors_2");
        Reservation reservation_1 = new Reservation(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.of(2025,4,2), hirer_1, book_1, Library.LIBRARY_1);
        Reservation reservation_3 = new Reservation(LocalDate.of(2025,4,2), hirer_1, book_2, Library.LIBRARY_1);
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation_1);
        reservations.add(reservation_2);
        reservations.add(reservation_3);

        ListOfReservations listOfReservations = new ListOfReservations(reservations);

        ArrayList<Reservation> result = new ArrayList<>();
        result.add(reservation_1);
        result.add(reservation_2);

        assertEquals(result, listOfReservations.getReservationsByItem(book_1));
        assertNotEquals(reservations, listOfReservations.getReservationsByItem(book_1));

    }

    @Test
    public void testEquals(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null);
        Item item_1 = new Magazine(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Item item_2 = new Magazine(2, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Reservation reservation_1 = new Reservation(LocalDate.of(2025,5,6), hirer, item_1, Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.of(2025,5,6), hirer, item_1, Library.LIBRARY_1);
        Reservation reservation_3 = new Reservation(LocalDate.of(2025,5,6), hirer, item_2, Library.LIBRARY_1);

        ArrayList<Reservation> expected_reservations_1 = new ArrayList<>();
        expected_reservations_1.add(reservation_1);
        expected_reservations_1.add(reservation_2);
        expected_reservations_1.add(reservation_3);

        ArrayList<Reservation> expected_reservations_2 = expected_reservations_1;

        ArrayList<Reservation> unexpected_reservations = new ArrayList<>();
        unexpected_reservations.add(reservation_1);

        assertTrue(expected_reservations_1.equals(expected_reservations_2));
        assertFalse(expected_reservations_1.equals(unexpected_reservations));
        assertFalse(expected_reservations_1.equals(null));


    }
}
