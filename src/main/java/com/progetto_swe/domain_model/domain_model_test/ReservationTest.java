package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ReservationTest {

    @Test
    public void testConstructor(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000", ucs, null, null, null, null);
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");

        Reservation reservation = new Reservation(LocalDate.now(), hirer, item, Library.LIBRARY_1);

        assertEquals(LocalDate.now(), reservation.getReservationDate());
        assertEquals(hirer, reservation.getHirer());
        assertEquals(item, reservation.getItem());
        assertEquals(Library.LIBRARY_1, reservation.getStoragePlace());
    }
}
