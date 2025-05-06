package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ReservationDAOTest {
    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE reservation, hirer, physical_copies, book, magazine, thesis, item RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @Test
    public void testGetReservations_() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);
        try{
            Book book = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
            Magazine magazine = new Magazine(2, "titolo2", LocalDate.of(2023,4,7), Language.LANGUAGE_1, Category.CATEGORY_1, "link2", 50, "publishing house 2");
            BookDAO bookDAO = new BookDAO();
            MagazineDAO magazineDAO = new MagazineDAO();
            bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
            magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,7).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2",  "publishing house 2", 50);

            HirerDAO hirerDAO = new HirerDAO();
            Hirer hirer = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
            hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);
            physicalCopiesDAO.addPhysicalCopies(2, Library.LIBRARY_2.toString(), 6, true);


            ReservationDAO reservationDAO = new ReservationDAO();
            reservationDAO.addReservation("uc1", 1, Library.LIBRARY_1.toString());
            reservationDAO.addReservation("uc1", 2, Library.LIBRARY_2.toString());


            ArrayList<Reservation> expected_reservations = new ArrayList<>();

            Reservation r1 = new Reservation(LocalDate.now(), hirer, book, Library.LIBRARY_1);
            Reservation r2 = new Reservation(LocalDate.now(), hirer, magazine, Library.LIBRARY_2);

            expected_reservations.add(r1);
            expected_reservations.add(r2);

            ListOfReservations expected_list_of_reservations = new ListOfReservations(expected_reservations);

            assertEquals(expected_list_of_reservations.getReservations().size(), reservationDAO.getReservations_().getReservations().size());
            assertTrue(reservationDAO.getReservations_().getReservations().containsAll(expected_reservations));

            Reservation r3 = new Reservation(LocalDate.now(), hirer, magazine, Library.LIBRARY_3);

            assertFalse(reservationDAO.getReservations_().getReservations().contains(r3));

        }finally{
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    @Test
    public void testAddReservation() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);
        try{
            Book book = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
            Hirer hirer = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
            Reservation r1 = new Reservation(LocalDate.now(), hirer, book, Library.LIBRARY_1);
            ReservationDAO reservationDAO = new ReservationDAO();
            BookDAO bookDAO = new BookDAO();
            bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
            HirerDAO hirerDAO = new HirerDAO();
            hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);

            assertTrue(reservationDAO.addReservation("uc1", 1, Library.LIBRARY_1.toString()));
            assertFalse(reservationDAO.addReservation("uc1", 1, Library.LIBRARY_2.toString()));

        }finally{
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    @Test
    public void testRemoveReservation() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);
        try{
            Book book = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
            Hirer hirer = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
            Reservation r1 = new Reservation(LocalDate.now(), hirer, book, Library.LIBRARY_1);
            ReservationDAO reservationDAO = new ReservationDAO();
            BookDAO bookDAO = new BookDAO();
            bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
            HirerDAO hirerDAO = new HirerDAO();
            hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);

            reservationDAO.addReservation("uc1", 1, Library.LIBRARY_1.toString());
            assertTrue(reservationDAO.removeReservation("uc1", 1, Library.LIBRARY_1.toString()));
            assertFalse(reservationDAO.removeReservation("uc1", 1, Library.LIBRARY_2.toString()));
            assertFalse(reservationDAO.removeReservation("uc2", 1, Library.LIBRARY_2.toString()));

        }finally{
            connection.setAutoCommit(true);
            connection.close();
        }
    }
}
