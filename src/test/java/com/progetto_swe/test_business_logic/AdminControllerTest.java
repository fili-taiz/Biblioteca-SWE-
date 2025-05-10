package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.AdminController;
import com.progetto_swe.business_logic.Hasher;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE admin, hirer, book, magazine, thesis, item, physical_copies, lending, reservation RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @Test
    public void testConstructor(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Admin admin = new Admin("usercode", "name", "surname", "email", "00000", Library.LIBRARY_1, ucs);
        assertEquals("usercode", admin.getUserCode());
        assertEquals("name", admin.getName());
        assertEquals("surname", admin.getSurname());
        assertEquals("email", admin.getEmail());
        assertEquals("00000", admin.getTelephoneNumber());
        assertEquals(Library.LIBRARY_1, admin.getWorkingPlace());
        assertEquals(ucs, admin.getUserCredentials());
    }

    /*Il seguente test è strutturato nel modo seguente:
    1) Si va a prendere un admin dal database dell'Università;
    2) L'Admin poi va a registrare l'utente esterno. */

    /* Questo test copre lo USE CASE #6 */

    @Test
    public void testRegisterExternalHirer() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);

        /* --> aggiunta con successo <-- */
        assertTrue(adminController.registerExternalHirer("abcd123", "Filippo", "Verdi", "filippo.verdi@studuni.com", "10005"));

        /* --> fallimento perché tale Hirer esiste già nel db della biblioteca e quindi l'inserimento viola il vincolo di chiave <--*/

        assertFalse(adminController.registerExternalHirer("abcd123", "Filippo", "Verdi", "filippo.verdi@studuni.com", "10005"));

        connection.close();

    }

    /* I successivi test coprono lo USE CASE #13 */

    /* Dobbiamo considerare i seguenti casi di fallimento:
    1) L'Hirer è bandito dalla biblioteca;
    2) L'Item non è prestabile;
    3) Non ci sono copie sufficienti per un prestito all'interno della biblioteca;
    4) Esiste già un prestito presso tale biblioteca che lega quell'Hirer a quell'Item

     */

    @Test
    public void testRegisterLending_Success() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);

        assertTrue(adminController.registerLending(hirer, item));

        connection.close();
    }

    // L'Hirer all'interno della tabella banned_hirers è stato inserito manualmente
    @Test
    public void testRegisterLending_Fail_1() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");
        PreparedStatement ps = connection.prepareStatement("INSERT INTO banned_hirers VALUES (?, ?)");
        ps.setString(1, "7564800");
        ps.setDate(2, Date.valueOf("2025-06-01"));
        ps.executeUpdate();

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);

        assertFalse(adminController.registerLending(hirer, item));

        connection.close();
    }

    @Test
    public void testRegisterLending_Fail_2() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, false);

        Item item = bookDAO.getBook(book_code);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);

        assertFalse(adminController.registerLending(hirer, item));

        connection.close();
    }

    @Test
    public void testRegisterLending_Fail_3() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 1, true);

        Item item = bookDAO.getBook(book_code);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);

        assertFalse(adminController.registerLending(hirer, item));

        connection.close();
    }

    @Test
    public void testRegisterLending_Fail_4() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);

        assertTrue(adminController.registerLending(hirer, item));
        assertFalse(adminController.registerLending(hirer, item));

        connection.close();
    }

    /* I seguenti test coprono lo USE CASE #11 */
    /* Ci sono due casi di fallimento da considerare:
    1) L'admin non lavora nella sede specificata nella prenotazione
    2) La prenotazione non esiste
     */

    @Test
    public void testConfirmReservationWithdraw_Success() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        ReservationDAO reservationDAO = new ReservationDAO();

        reservationDAO.addReservation("7564800", book_code, Library.LIBRARY_1.toString());

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservation WHERE user_code = ? and code = ? and storage_place = ?");
        ps.setString(1, "7564800");
        ps.setInt(2, book_code);
        ps.setString(3, Library.LIBRARY_1.toString());
        ResultSet rs = ps.executeQuery();
        rs.next();
        Reservation reservation = new Reservation(rs.getDate("reservation_date").toLocalDate(), hirer, item, Library.LIBRARY_1);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);


        assertTrue(adminController.confirmReservationWithdraw(reservation));

        connection.close();

    }

    @Test
    public void testConfirmReservationWithdraw_Fail_1() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_2.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        ReservationDAO reservationDAO = new ReservationDAO();

        reservationDAO.addReservation("7564800", book_code, Library.LIBRARY_2.toString());

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservation WHERE user_code = ? and code = ? and storage_place = ?");
        ps.setString(1, "7564800");
        ps.setInt(2, book_code);
        ps.setString(3, Library.LIBRARY_2.toString());
        ResultSet rs = ps.executeQuery();
        rs.next();
        Reservation reservation = new Reservation(rs.getDate("reservation_date").toLocalDate(), hirer, item, Library.LIBRARY_2);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);


        assertFalse(adminController.confirmReservationWithdraw(reservation));

        connection.close();

    }

    @Test
    public void testConfirmReservationWithdraw_Fail_2() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        ReservationDAO reservationDAO = new ReservationDAO();

        reservationDAO.addReservation("7564800", book_code, Library.LIBRARY_1.toString());

        Reservation reservation = new Reservation(LocalDate.now(), hirer, item, Library.LIBRARY_3);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);


        assertFalse(adminController.confirmReservationWithdraw(reservation));

        connection.close();

    }

    /* I seguenti test coprono lo USE_CASE #12 */
    /* Ci sono due casi di fallimento da considerare:
    1) L'admin non lavora nella sede presso cui l'utente ha preso in prestito l'articolo
    2) Il prestito non esiste
     */

    @Test
    public void testRegisterReturnOfItem_Success() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        LendingDAO lendingDAO = new LendingDAO();

        lendingDAO.addLending("7564800", book_code, Library.LIBRARY_1.toString());

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM lending WHERE user_code = ? and code = ? and storage_place = ?");
        ps.setString(1, "7564800");
        ps.setInt(2, book_code);
        ps.setString(3, Library.LIBRARY_1.toString());
        ResultSet rs = ps.executeQuery();
        rs.next();
        Lending lending = new Lending(rs.getDate("lending_date").toLocalDate(), hirer, item, Library.LIBRARY_1);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);


        assertTrue(adminController.registerReturnOfItem(lending));

        connection.close();
    }

    @Test
    public void testRegisterReturnOfItem_Fail_1() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_2.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        LendingDAO lendingDAO = new LendingDAO();

        lendingDAO.addLending("7564800", book_code, Library.LIBRARY_2.toString());

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM lending WHERE user_code = ? and code = ? and storage_place = ?");
        ps.setString(1, "7564800");
        ps.setInt(2, book_code);
        ps.setString(3, Library.LIBRARY_2.toString());
        ResultSet rs = ps.executeQuery();
        rs.next();
        Lending lending = new Lending(rs.getDate("lending_date").toLocalDate(), hirer, item, Library.LIBRARY_2);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);


        assertFalse(adminController.registerReturnOfItem(lending));

        connection.close();

    }

    @Test
    public void testRegisterReturnOfItem_Fail_2() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");

        Hirer hirer = hirerDAO.getHirer("7564800");

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("Titolo", LocalDate.of(2011,4,6).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "Link", "" +
                "Isbn", "Publishing House", 200, "Authors");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);

        Item item = bookDAO.getBook(book_code);

        LendingDAO lendingDAO = new LendingDAO();

        lendingDAO.addLending("7564800", book_code, Library.LIBRARY_1.toString());

        Lending lending = new Lending(LocalDate.now(), hirer, item, Library.LIBRARY_3);

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");

        AdminController adminController = new AdminController(admin);


        assertFalse(adminController.registerReturnOfItem(lending));

        connection.close();

    }









}
