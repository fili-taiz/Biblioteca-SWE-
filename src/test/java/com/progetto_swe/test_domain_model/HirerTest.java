package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class HirerTest {

    @Test
    public void testConstructor(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                ucs,LocalDate.of(2025, 5,6));

        assertEquals("usercode", hirer.getUserCode());
        assertEquals("name", hirer.getName());
        assertEquals("surname", hirer.getSurname());
        assertEquals("email", hirer.getEmail());
        assertEquals("00000", hirer.getTelephoneNumber());
        assertEquals(ucs, hirer.getUserCredentials());
        assertEquals(LocalDate.of(2025, 5, 6), hirer.getUnbannedDate());
    }

    @Test
    public void testContains(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "12345",
                ucs, LocalDate.of(2025, 5,6));

        assertTrue(hirer.contains("user"));
        assertTrue(hirer.contains("nam"));
        assertTrue(hirer.contains("surn"));
        assertTrue(hirer.contains("mai"));
        assertTrue(hirer.contains("12345"));
        assertFalse(hirer.contains("universita"));
    }

}
